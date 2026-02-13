module chav1961.ai.voice {
	requires transitive chav1961.purelib;
	requires java.base;
	requires java.desktop;
	requires java.datatransfer;
	requires chav1961.ai.api;
	requires java.xml;
	requires com.google.gson;
	requires java.net.http;
	
	exports chav1961.ai.voice;
	exports chav1961.ai.voice.interfaces;
	opens chav1961.ai.voice.json.response to com.google.gson;
}
