package chav1961.ai.api.json.response;

import com.google.gson.annotations.SerializedName;

// Внутренний класс для элементов массива models
public class ModelResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("modified_at")
    private String modifiedAt; // Можно оставить как String или парсить как Date с помощью дополнений

    @SerializedName("size")
    private Long size; // Используем Long, так как число большое

    @SerializedName("digest")
    private String digest;

    @SerializedName("details")
    private ModelDetailsResponse details;

    // Конструктор
    public ModelResponse() {
    	
    }
    
    public ModelResponse(String name, String model, String modifiedAt, Long size, String digest, ModelDetailsResponse details) {
        this.name = name;
        this.model = model;
        this.modifiedAt = modifiedAt;
        this.size = size;
        this.digest = digest;
        this.details = details;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(String modifiedAt) { this.modifiedAt = modifiedAt; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public String getDigest() { return digest; }
    public void setDigest(String digest) { this.digest = digest; }

    public ModelDetailsResponse getDetails() { return details; }
    public void setDetails(ModelDetailsResponse details) { this.details = details; }
}