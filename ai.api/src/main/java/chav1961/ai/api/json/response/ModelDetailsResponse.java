package chav1961.ai.api.json.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

// Внутренний класс для поля details
public class ModelDetailsResponse {
    @SerializedName("parent_model")
    private String parentModel;

    @SerializedName("format")
    private String format;

    @SerializedName("family")
    private String family;

    @SerializedName("families")
    private List<String> families;

    @SerializedName("parameter_size")
    private String parameterSize;

    @SerializedName("quantization_level")
    private String quantizationLevel;

    public ModelDetailsResponse() {
    }
    
    // Конструктор
    public ModelDetailsResponse(String parentModel, String format, String family, List<String> families,
                   String parameterSize, String quantizationLevel) {
        this.parentModel = parentModel;
        this.format = format;
        this.family = family;
        this.families = families;
        this.parameterSize = parameterSize;
        this.quantizationLevel = quantizationLevel;
    }

    // Геттеры и сеттеры
    public String getParentModel() { return parentModel; }
    public void setParentModel(String parentModel) { this.parentModel = parentModel; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public List<String> getFamilies() { return families; }
    public void setFamilies(List<String> families) { this.families = families; }

    public String getParameterSize() { return parameterSize; }
    public void setParameterSize(String parameterSize) { this.parameterSize = parameterSize; }

    public String getQuantizationLevel() { return quantizationLevel; }
    public void setQuantizationLevel(String quantizationLevel) { this.quantizationLevel = quantizationLevel; }
}