package chav1961.ai.api.json.response;

import com.google.gson.annotations.SerializedName;

// Основной класс, соответствующий всему JSON
public class ModelsResponse {

    @SerializedName("models")
    private ModelResponse[] models;

    // Конструктор
    public ModelsResponse() {
    	
    }
    
    public ModelsResponse(ModelResponse[] models) {
        this.models = models;
    }
    
    // Геттеры и сеттеры
    public ModelResponse[] getModels() {
        return models;
    }

    public void setModels(ModelResponse[] models) {
        this.models = models;
    }
}