package chav1961.ai.voice.json.response;

public class RecognizeResponse {
	public String[]	result;
	public RecognizeEmotionResponse[] emotions;
	public RecognizePersonIdentityResponse[] person_identity;
	public int		status;
}
