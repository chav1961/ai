package chav1961.ai.voice.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface VoiceConnectorInterface {
    String recognize(final InputStream audioSource) throws IOException;
	InputStream vocalize(final CharSequence source) throws IOException; 
}
