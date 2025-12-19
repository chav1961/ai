module chav1961.ai.client {
	requires transitive chav1961.purelib;
	requires java.base;
	requires java.desktop;
	requires java.datatransfer;
	requires java.xml;
	requires com.google.gson;
	
	exports chav1961.ai.client;
	exports chav1961.ai.client.interfaces;
}
