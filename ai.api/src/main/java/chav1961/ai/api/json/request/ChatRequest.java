package chav1961.ai.api.json.request;

import com.google.gson.annotations.SerializedName;

import chav1961.ai.api.json.ChatMessage;

/**
 * Represents a request containing a model identifier and a list of messages.
 */
public class ChatRequest {

    /**
     * The name or identifier of the model.
     */
    @SerializedName("model")
    private String model;

    /**
     * The list of messages exchanged in the chat, each with a role and content.
     */
    @SerializedName("messages")
    private ChatMessage[] messages;

    /**
     * Default constructor.
     */
    public ChatRequest() {
    }

    /**
     * Constructs a ChatRequest with specified model and messages.
     *
     * @param model    the model identifier
     * @param messages the list of message objects
     */
    public ChatRequest(String model, ChatMessage[] messages) {
        this.model = model;
        this.messages = messages;
    }

    /**
     * Gets the model identifier.
     * 
     * @return the model string
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier.
     * 
     * @param model the model string to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the list of messages.
     * 
     * @return the list of Message objects
     */
    public ChatMessage[] getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages.
     * 
     * @param messages the list of Message objects to set
     */
    public void setMessages(ChatMessage[] messages) {
        this.messages = messages;
    }

    /**
     * Represents a single message with a role and content.
     */
    public static class Message {

        /**
         * The role of the message sender (e.g., "user", "system", "assistant").
         */
        @SerializedName("role")
        private String role;

        /**
         * The content of the message.
         */
        @SerializedName("content")
        private String content;

        /**
         * Default constructor.
         */
        public Message() {
        }

        /**
         * Constructs a Message with role and content.
         *
         * @param role    the role of the sender
         * @param content the message content
         */
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        /**
         * Gets the role of the message.
         *
         * @return the role string
         */
        public String getRole() {
            return role;
        }

        /**
         * Sets the role of the message.
         *
         * @param role the role string to set
         */
        public void setRole(String role) {
            this.role = role;
        }

        /**
         * Gets the message content.
         *
         * @return the content string
         */
        public String getContent() {
            return content;
        }

        /**
         * Sets the message content.
         *
         * @param content the content string to set
         */
        public void setContent(String content) {
            this.content = content;
        }
    }
}