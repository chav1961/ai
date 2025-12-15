package chav1961.ai.api.json.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GenerateResponse {

    @SerializedName("model")
    private String model;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("response")
    private String response;

    @SerializedName("done")
    private boolean done;

    @SerializedName("context")
    private List<Integer> context;

    @SerializedName("total_duration")
    private long totalDuration;

    @SerializedName("load_duration")
    private long loadDuration;

    @SerializedName("prompt_eval_count")
    private int promptEvalCount;

    @SerializedName("prompt_eval_duration")
    private long promptEvalDuration;

    @SerializedName("eval_count")
    private int evalCount;

    @SerializedName("eval_duration")
    private long evalDuration;

    public GenerateResponse() {
    }

    public GenerateResponse(String model, Date createdAt, String response, boolean done,
                         List<Integer> context, long totalDuration, long loadDuration,
                         int promptEvalCount, long promptEvalDuration,
                         int evalCount, long evalDuration) {
        this.model = model;
        this.createdAt = createdAt;
        this.response = response;
        this.done = done;
        this.context = context;
        this.totalDuration = totalDuration;
        this.loadDuration = loadDuration;
        this.promptEvalCount = promptEvalCount;
        this.promptEvalDuration = promptEvalDuration;
        this.evalCount = evalCount;
        this.evalDuration = evalDuration;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public List<Integer> getContext() { return context; }
    public void setContext(List<Integer> context) { this.context = context; }

    public long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(long totalDuration) { this.totalDuration = totalDuration; }

    public long getLoadDuration() { return loadDuration; }
    public void setLoadDuration(long loadDuration) { this.loadDuration = loadDuration; }

    public int getPromptEvalCount() { return promptEvalCount; }
    public void setPromptEvalCount(int promptEvalCount) { this.promptEvalCount = promptEvalCount; }

    public long getPromptEvalDuration() { return promptEvalDuration; }
    public void setPromptEvalDuration(long promptEvalDuration) { this.promptEvalDuration = promptEvalDuration; }

    public int getEvalCount() { return evalCount; }
    public void setEvalCount(int evalCount) { this.evalCount = evalCount; }

    public long getEvalDuration() { return evalDuration; }
    public void setEvalDuration(long evalDuration) { this.evalDuration = evalDuration; }
}