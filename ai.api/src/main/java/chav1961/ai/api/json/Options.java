package chav1961.ai.api.json;

import com.google.gson.annotations.SerializedName;

import chav1961.purelib.basic.exceptions.FlowException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.ModuleAccessor;
import chav1961.purelib.i18n.interfaces.LocaleResource;
import chav1961.purelib.i18n.interfaces.LocaleResourceLocation;
import chav1961.purelib.ui.interfaces.FormManager;
import chav1961.purelib.ui.interfaces.Format;
import chav1961.purelib.ui.interfaces.RefreshMode;

/**
 * Represents the configuration options for the model.
 */
@LocaleResourceLocation("i18n:xml:root://chav1961.ai.gui.Application/chav1961/ai/gui/i18n/localization.xml")
@LocaleResource(value="options.title",tooltip="options.title.tt",help="options.title.help")
public class Options implements FormManager<Object, Options>, ModuleAccessor, Cloneable {
    /**
     * The size of the context window used for generation.
     */
	@LocaleResource(value="options.num_ctx",tooltip="options.num_ctx.tt")
	@Format("10ms")
    @SerializedName("num_ctx")
    private Integer num_ctx = 2048;

    /**
     * How far back to look to prevent repetitions (0=disabled, -1=num_ctx).
     */
	@LocaleResource(value="options.repeat_last_n",tooltip="options.repeat_last_n.tt")
	@Format("10ms")
    @SerializedName("repeat_last_n")
    private Integer repeat_last_n = 64;

    /**
     * Penalty factor for repetitions. Higher values penalize repetitions more.
     */
	@LocaleResource(value="options.repeat_penalty",tooltip="options.repeat_penalty.tt")
	@Format("10.4ms")
    @SerializedName("repeat_penalty")
    private Float repeat_penalty = 1.1f;

    /**
     * The temperature of the model. Higher values produce more creative answers.
     */
	@LocaleResource(value="options.temperature",tooltip="options.temperature.tt")
	@Format("10.2ms")
    @SerializedName("temperature")
    private Float temperature = 0.8f;

    /**
     * Random seed for generation to produce reproducible results.
     */
	@LocaleResource(value="options.seed",tooltip="options.seed.tt")
	@Format("10ms")
    @SerializedName("seed")
    private Integer seed = 0;

    /**
     * Stop sequences; generation stops when encountered.
     */
	@LocaleResource(value="options.stop",tooltip="options.stop.tt")
	@Format("30ms")
    @SerializedName("stop")
    private String stop = "ZZZ";

    /**
     * Maximum number of tokens to predict. -1 for infinite.
     */
	@LocaleResource(value="options.num_predict",tooltip="options.num_predict.tt")
	@Format("10ms")
    @SerializedName("num_predict")
    private Integer num_predict = -1;

    /**
     * Top-k sampling parameter to reduce nonsense. Higher values increase diversity.
     */
	@LocaleResource(value="options.top_k",tooltip="options.top_k.tt")
	@Format("10ms")
    @SerializedName("top_k")
    private Integer top_k = 40;

    /**
     * Top-p (nucleus) sampling parameter to balance diversity and focus.
     */
	@LocaleResource(value="options.top_p",tooltip="options.top_p.tt")
	@Format("10.4ms")
    @SerializedName("top_p")
    private Float top_p = 0.9f;

    /**
     * Minimum probability for tokens to be considered.
     */
	@LocaleResource(value="options.min_p",tooltip="options.min_p.tt")
	@Format("10.4ms")
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

    public void set(final Options another) {
    	if (another == null) {
    		throw new NullPointerException("Another options can't be null");
    	}
    	else {
    	    this.num_ctx = another.num_ctx;
    	    this.repeat_last_n = another.repeat_last_n;
    	    this.repeat_penalty = another.repeat_penalty;
    	    this.temperature = another.temperature;
    	    this.seed = another.seed;
    	    this.stop = another.stop;
    	    this.num_predict = another.num_predict;
    	    this.top_k = another.top_k;
    	    this.top_p = another.top_p;
    	    this.min_p = another.min_p;
    	}
    }

	@Override
    public String toString() {
        return "Options [num_ctx=" + num_ctx + ", repeat_last_n=" + repeat_last_n + 
               ", repeat_penalty=" + repeat_penalty + ", temperature=" + temperature + 
               ", seed=" + seed + ", stop=" + stop + ", num_predict=" + num_predict +
               ", top_k=" + top_k + ", top_p=" + top_p + ", min_p=" + min_p + "]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
    
	@Override
	public RefreshMode onField(final LoggerFacade logger, final Options inst, final Object id, final String fieldName, final Object oldValue, final boolean beforeCommit) throws FlowException, LocalizationException {
		return RefreshMode.DEFAULT;
	}

	@Override
	public void allowUnnamedModuleAccess(Module... unnamedModules) {
		for (Module item : unnamedModules) {
			this.getClass().getModule().addExports(this.getClass().getPackageName(), item);
		}
	}
}