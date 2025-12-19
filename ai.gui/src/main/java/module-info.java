module chav1961.ai.gui {
	requires transitive chav1961.purelib;
	requires java.base;
	requires java.desktop;
	requires java.datatransfer;
	requires chav1961.ai.api;
	requires chav1961.ai.client;
	requires java.xml;
	
	exports chav1961.ai.gui;
	opens chav1961.ai.gui to chav1961.purelib;
}
