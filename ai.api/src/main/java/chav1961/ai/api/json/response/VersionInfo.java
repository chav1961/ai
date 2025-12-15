package chav1961.ai.api.json.response;

import com.google.gson.annotations.SerializedName;

/**
 * Класс, представляющий JSON-объект с полем version.
 */
public class VersionInfo {

    /**
     * Версия.
     */
    @SerializedName("version")
    private String version;

    /**
     * Конструктор по умолчанию.
     */
    public VersionInfo() {
    }

    /**
     * Конструктор с параметром.
     *
     * @param version версия
     */
    public VersionInfo(String version) {
        this.version = version;
    }

    /**
     * Получить версию.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Установить версию.
     */
    public void setVersion(String version) {
        this.version = version;
    }

	@Override
	public String toString() {
		return "VersionInfo [version=" + version + "]";
	}
}