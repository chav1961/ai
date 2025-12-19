module chav1961.ai.api {
	requires transitive chav1961.purelib;
	requires java.base;
	requires java.desktop;
	requires java.datatransfer;
	requires java.xml;
	requires com.google.gson;
	
	exports chav1961.ai.api.json;
	exports chav1961.ai.api.json.request;
	exports chav1961.ai.api.json.response;

	opens chav1961.ai.api.json to com.google.gson, chav1961.purelib;
	opens chav1961.ai.api.json.request to com.google.gson, chav1961.purelib;
	opens chav1961.ai.api.json.response to com.google.gson, chav1961.purelib;
}
