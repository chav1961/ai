package chav1961.ai.voice;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class VoicePlayer {
	private static final  int	BUFFER_SIZE = 4096;

    public static void play(final InputStream source) throws IOException {
    	if (source == null) {
    		throw new NullPointerException("Source stream can't be null"); 
    	}
    	else {
            try(final AudioInputStream 	audioStream = AudioSystem.getAudioInputStream(source)) {
                final AudioFormat 		format = audioStream.getFormat();
                final DataLine.Info 	info = new DataLine.Info(SourceDataLine.class, format);
                final SourceDataLine	line = (SourceDataLine) AudioSystem.getLine(info);
                final byte[] 			buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                
                line.open(format);
                line.start();

                while ((bytesRead = audioStream.read(buffer)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                line.drain();
                line.stop();
                line.close();
            } catch (LineUnavailableException | UnsupportedAudioFileException e) {
            	throw new IOException(e);
    		}
    	}
    }
}
