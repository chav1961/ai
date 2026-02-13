package chav1961.ai.voice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import chav1961.ai.voice.interfaces.VoiceConnectorInterface;
import chav1961.purelib.concurrent.LightWeightListenerList;
import chav1961.purelib.concurrent.interfaces.ExecutionControl;

public class VoiceListener implements ExecutionControl {
	private static final int	SAMPLE_RATE = 16000;
	private static final int	SAMPLE_PIECE = 320;
	private static final int	WINDOWS_IN_SECOND = 50;
	private static final int	FRAME_SECONDS = 60;
	private static final String	TEMPLATE_PATTERN = "000000000000000";
	
	private static final ExecutorService	FTP = Executors.newFixedThreadPool(1); 
	
	private final LightWeightListenerList<ActionListener>	listeners = new LightWeightListenerList<>(ActionListener.class);
    private final AudioFormat 		format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
    private final TargetDataLine 	line;
    private final VoiceConnectorInterface	conn;
    private final int 				bytesPerSample = format.getSampleSizeInBits() / 8;
    private final double			scale = 1.0 / Math.pow(2, format.getSampleSizeInBits() - 1);
    private final double			meanEnergy = 0.3;
    private Thread 		recordingThread;
    private boolean		running = false, paused = true;

    public VoiceListener(final VoiceConnectorInterface conn) throws IOException {
    	if (conn == null) {
    		throw new NullPointerException("Connector can't be null");
    	}
    	else {
            try {
                final DataLine.Info 	info = new DataLine.Info(TargetDataLine.class, format);
                
    			this.line = (TargetDataLine) AudioSystem.getLine(info);
    			this.conn = conn;
    		} catch (LineUnavailableException e) {
    			throw new IOException(e);
    		}
    	}
    }

	@Override
	public synchronized void start() throws Exception {
        line.open(format);
        running = true;
        resume(); 
	}

	@Override
	public synchronized void suspend() throws Exception {
		recordingThread.interrupt();
        paused = true;
	}

	@Override
	public synchronized void resume() throws Exception {
		recordingThread = new Thread(() -> recording());
        recordingThread.start();
        paused = false;
	}

	@Override
	public synchronized void stop() throws Exception {
		resume();
		line.close();
        running = false;
	}

	@Override
	public synchronized boolean isStarted() {
		return running;
	}

	@Override
	public synchronized boolean isSuspended() {
		return paused;
	}

	public void addActionListener(final ActionListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener to add can't be null"); 
		}
		else {
			listeners.addListener(listener);
		}
	}

	public void removeActionListener(final ActionListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener to remove can't be null"); 
		}
		else {
			listeners.removeListener(listener);
		}
	}
	
	private void recording() {
		final byte[]	buffer = new byte[bytesPerSample * SAMPLE_PIECE];
		final byte[]	content = new byte[bytesPerSample * SAMPLE_RATE * FRAME_SECONDS];
		final char[]	template = new char[WINDOWS_IN_SECOND * FRAME_SECONDS];
		double			loWater = meanEnergy * 32768;
		int				templateDispl = 0;
		int				displ = 0;
        
        try{
            line.start();
            
            while (!paused && !Thread.currentThread().isInterrupted() ) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                
                if (bytesRead == buffer.length) {
                	System.arraycopy(buffer, 0, content, displ, bytesRead);
                	template[templateDispl] = calcEnergy(buffer) > loWater ? '1' : '0';

                	final String	templateStr = new String(template, 0, templateDispl);
                	final int		patternIndex = templateStr.indexOf(TEMPLATE_PATTERN);
                	
                	if (patternIndex >= 0) {
                    	if (patternIndex > 0) {
                    		upload(content, displ);
                    	}
                		System.arraycopy(content, displ, content, 0, patternIndex);
                		templateDispl = 0;
                		displ = 0;
                	}
                	else {
                    	displ += bytesRead;
                		templateDispl++;
                	}
                }
            }
        } finally {
    		line.stop();
        }
	}
	
	private double calcEnergy(final byte[] source) {
		double	sum = 0; 
		
		for (int index = 0; index < source.length; index += 2) {
			final int	sample = (short) ((source[index + 1] << 8) | (source[index] & 0xFF));
			
			sum += sample * sample;
		}
		return sum * scale / source.length;
	}

	private void upload(final byte[] content, final int to) {
		final ByteArrayOutputStream		baos = new ByteArrayOutputStream();
		
        try(final ByteArrayInputStream 	bais = new ByteArrayInputStream(content, 0, to);
        	final AudioInputStream 		ais = new AudioInputStream(bais, format, content.length / format.getFrameSize())) {

        	AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
        	baos.flush();
        	
        	FTP.submit(()->{
        		try {
        			final ActionEvent	ae = new ActionEvent(this, 0, conn.recognize(new ByteArrayInputStream(baos.toByteArray())));

					listeners.fireEvent((l)->l.actionPerformed(ae));
				} catch (IOException e) {
					final ActionEvent	ae = new ActionEvent(this, 0, "???");
					
					listeners.fireEvent((l)->l.actionPerformed(ae));
				}
        	});
        } catch (IOException e) {
		}	
	}
}
