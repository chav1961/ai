package chav1961.ai.client.interfaces;

public enum APIAction {
	GENERATE(true,true,"POST","/api/generate"),
	CHAT(true,true,"POST","/api/chat"),
	CREATE_MODEL(true,true,"POST","/api/create"),
	BLOBS_CHECK(true,false,"HEAD","/api/blobs/sha256:"),
	BLOBS_PUSH(true,true,"POST","/api/blobs/sha256:"),
	MODEL_LIST(true,false,"GET","/api/tags"),
	MODEL_INFO(true,true,"POST","/api/show"),
	MODEL_COPY(true,true,"POST","/api/copy"),
	MODEL_DELETE(true,true,"DELETE","/api/delete"),
	MODEL_PULL(true,true,"POST","/api/pull"),
	MODEL_PUSH(true,true,"POST","/api/push"),
	EMBEDDINGS(true,true,"POST","/api/embed"),
	MODEL_RUNNING_LIST(true,false,"GET","/api/ps"),
	VERSION(true,false,"GET","/api/version")
	;
	
	private final boolean doInput;
	private final boolean doOutput;
	private final String method;
	private final String path;

	private APIAction(boolean doInput, boolean doOutput, String method, String path) {
		this.doInput = doInput;
		this.doOutput = doOutput;
		this.method = method;
		this.path = path;
	}

	public boolean isDoInput() {
		return doInput;
	}

	public boolean isDoOutput() {
		return doOutput;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}
}
