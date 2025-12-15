package chav1961.ai.api.json;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the configuration options for the model.
 */
public class Options {
    /**
     * The size of the context window used for generation.
     */
    @SerializedName("num_ctx")
    private Integer num_ctx = 2048;

    /**
     * How far back to look to prevent repetitions (0=disabled, -1=num_ctx).
     */
    @SerializedName("repeat_last_n")
    private Integer repeat_last_n = 64;

    /**
     * Penalty factor for repetitions. Higher values penalize repetitions more.
     */
    @SerializedName("repeat_penalty")
    private Float repeat_penalty = 1.1f;

    /**
     * The temperature of the model. Higher values produce more creative answers.
     */
    @SerializedName("temperature")
    private Float temperature = 0.8f;

    /**
     * Random seed for generation to produce reproducible results.
     */
    @SerializedName("seed")
    private Integer seed = 0;

    /**
     * Stop sequences; generation stops when encountered.
     */
    @SerializedName("stop")
    private String stop = "ZZZ";

    /**
     * Maximum number of tokens to predict. -1 for infinite.
     */
    @SerializedName("num_predict")
    private Integer num_predict = -1;

    /**
     * Top-k sampling parameter to reduce nonsense. Higher values increase diversity.
     */
    @SerializedName("top_k")
    private Integer top_k = 40;

    /**
     * Top-p (nucleus) sampling parameter to balance diversity and focus.
     */
    @SerializedName("top_p")
    private Float top_p = 0.9f;

    /**
     * Minimum probability for tokens to be considered.
     */
    @SerializedName("min_p")
    private Float min_p = 0.0f;

    /**
     * Default constructor.
     */
    public Options() {
    }

    public Integer getNum_ctx() {
        return num_ctx;
    }

    public void setNum_ctx(Integer num_ctx) {
        this.num_ctx = num_ctx;
    }

    public Integer getRepeat_last_n() {
        return repeat_last_n;
    }

    public void setRepeat_last_n(Integer repeat_last_n) {
        this.repeat_last_n = repeat_last_n;
    }

    public Float getRepeat_penalty() {
        return repeat_penalty;
    }

    public void setRepeat_penalty(Float repeat_penalty) {
        this.repeat_penalty = repeat_penalty;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public Integer getNum_predict() {
        return num_predict;
    }

    public void setNum_predict(Integer num_predict) {
        this.num_predict = num_predict;
    }

    public Integer getTop_k() {
        return top_k;
    }

    public void setTop_k(Integer top_k) {
        this.top_k = top_k;
    }

    public Float getTop_p() {
        return top_p;
    }

    public void setTop_p(Float top_p) {
        this.top_p = top_p;
    }

    public Float getMin_p() {
        return min_p;
    }

    public void setMin_p(Float min_p) {
        this.min_p = min_p;
    }

    @Override
    public String toString() {
        return "Options [num_ctx=" + num_ctx + ", repeat_last_n=" + repeat_last_n + 
               ", repeat_penalty=" + repeat_penalty + ", temperature=" + temperature + 
               ", seed=" + seed + ", stop=" + stop + ", num_predict=" + num_predict +
               ", top_k=" + top_k + ", top_p=" + top_p + ", min_p=" + min_p + "]";
    }
}