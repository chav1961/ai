package chav1961.ai.api.json;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a message with a role and content.
 */
public class ChatMessage {

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
    public ChatMessage() {
    }

    /**
     * Constructs a Message with specified role and content.
     *
     * @param role    the role of the sender
     * @param content the message content
     */
    public ChatMessage(String role, String content) {
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

	@Override
	public String toString() {
		return "ChatMessage [role=" + role + ", content=" + content + "]";
	}
}