package chav1961.ai.api.json.response;

import com.google.gson.annotations.SerializedName;

import chav1961.ai.api.json.ChatMessage;

/**
 * Represents a detailed response or record, including model info,
 * timestamps, messaging, status, and durations.
 */
public class ChatResponse {

    /**
     * The name or identifier of the model.
     */
    @SerializedName("model")
    private String model;

    /**
     * The creation timestamp in ISO 8601 format.
     */
    @SerializedName("created_at")
    private String createdAt;

    /**
     * The message object containing role and content.
     */
    @SerializedName("message")
    private ChatMessage message;

    /**
     * Indicates whether the process is complete.
     */
    @SerializedName("done")
    private boolean done;

    /**
     * Total duration in milliseconds or nanoseconds.
     */
    @SerializedName("total_duration")
    private long totalDuration;

    /**
     * Duration of load operation.
     */
    @SerializedName("load_duration")
    private long loadDuration;

    /**
     * Count of prompt evaluations.
     */
    @SerializedName("prompt_eval_count")
    private int promptEvalCount;

    /**
     * Duration of prompt evaluation in nanoseconds or milliseconds.
     */
    @SerializedName("prompt_eval_duration")
    private long promptEvalDuration;

    /**
     * Count of evaluations performed.
     */
    @SerializedName("eval_count")
    private int evalCount;

    /**
     * Total evaluation duration.
     */
    @SerializedName("eval_duration")
    private long evalDuration;

    /**
     * Default constructor.
     */
    public ChatResponse() {
    }

    /**
     * Parameterized constructor.
     */
    public ChatResponse(String model, String createdAt, ChatMessage message, boolean done,
                         long totalDuration, long loadDuration, int promptEvalCount,
                         long promptEvalDuration, int evalCount, long evalDuration) {
        this.model = model;
        this.createdAt = createdAt;
        this.message = message;
        this.done = done;
        this.totalDuration = totalDuration;
        this.loadDuration = loadDuration;
        this.promptEvalCount = promptEvalCount;
        this.promptEvalDuration = promptEvalDuration;
        this.evalCount = evalCount;
        this.evalDuration = evalDuration;
    }

    // Getters and Setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public long getLoadDuration() {
        return loadDuration;
    }

    public void setLoadDuration(long loadDuration) {
        this.loadDuration = loadDuration;
    }

    public int getPromptEvalCount() {
        return promptEvalCount;
    }

    public void setPromptEvalCount(int promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
    }

    public long getPromptEvalDuration() {
        return promptEvalDuration;
    }

    public void setPromptEvalDuration(long promptEvalDuration) {
        this.promptEvalDuration = promptEvalDuration;
    }

    public int getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }

    public long getEvalDuration() {
        return evalDuration;
    }

    public void setEvalDuration(long evalDuration) {
        this.evalDuration = evalDuration;
    }

    /**
     * Nested class representing the message details.
     */
    public static class Message {
        /**
         * The role of the message sender.
         */
        @SerializedName("role")
        private String role;

        /**
         * The message content.
         */
        @SerializedName("content")
        private String content;

        /**
         * Default constructor.
         */
        public Message() {
        }

        /**
         * Constructor with parameters.
         *
         * @param role    sender role
         * @param content message content
         */
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}