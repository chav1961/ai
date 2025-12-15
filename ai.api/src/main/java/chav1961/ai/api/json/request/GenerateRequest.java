package chav1961.ai.api.json.request;

import com.google.gson.annotations.SerializedName;

import chav1961.ai.api.json.Options;

/**
 * Represents the request data structure for interacting with the model.
 */
public class GenerateRequest {

    /**
     * The model name to use.
     */
    @SerializedName("model")
    private String model;

    /**
     * The prompt for the model.
     */
    @SerializedName("prompt")
    private String prompt;

    /**
     * The suffix to append after the response.
     */
    @SerializedName("suffix")
    private String suffix;

    /**
     * Additional options as a string.
     */
    @SerializedName("options")
    private Options options;

    /**
     * Contextual information as a list of integers.
     */
    @SerializedName("context")
    private int[] context;

    /**
     * The response format, e.g., "json".
     */
    @SerializedName("format")
    private String format;

    /**
     * Whether to stream the response.
     */
    @SerializedName("stream")
    private boolean stream;

    /**
     * List of images encoded as strings; can be empty.
     */
    @SerializedName("images")
    private String[] images;

    /**
     * Default constructor.
     */
    public GenerateRequest() {
    }

    /**
     * Parameterized constructor.
     *
     * @param model    the model name
     * @param prompt   the prompt string
     * @param suffix   the suffix string
     * @param options  additional options
     * @param context  list of context integers
     * @param format   response format
     * @param stream   stream flag
     * @param images   list of image strings
     */
    public GenerateRequest(String model, String prompt, String suffix, Options options,
                        int[] context, String format, boolean stream, String[] images) {
        this.model = model;
        this.prompt = prompt;
        this.suffix = suffix;
        this.options = options;
        this.context = context;
        this.format = format;
        this.stream = stream;
        this.images = images;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public int[] getContext() {
        return context;
    }

    public void setContext(int[] context) {
        this.context = context;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}