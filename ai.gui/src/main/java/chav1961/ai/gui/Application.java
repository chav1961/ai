package chav1961.ai.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import chav1961.ai.api.json.Options;
import chav1961.ai.api.json.request.GenerateRequest;
import chav1961.ai.api.json.response.GenerateResponse;
import chav1961.ai.api.json.response.ModelResponse;
import chav1961.ai.api.json.response.ModelsResponse;
import chav1961.ai.api.json.response.VersionInfo;
import chav1961.ai.client.AIConnector;
import chav1961.ai.client.interfaces.APIAction;
import chav1961.purelib.basic.ArgParser;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.CommandLineParametersException;
import chav1961.purelib.basic.exceptions.ContentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.basic.interfaces.LoggerFacade.Severity;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JCommandLine;
import chav1961.purelib.ui.swing.useful.JRichFrame;

public class Application extends JRichFrame {
	private static final long serialVersionUID = 7390506926122202670L;
	
	public static final File	FILE_SETTINGS = new File("./.ai.gui.properties"); 
	public static final String	ARG_SERVER_URI = "serverUri";
	public static final String	SETTINGS_CURRENT_MODEL = "currentModel";
	public static final String	SETTINGS_DEFAULT_CURRENT_MODEL = "deepseek-r1:8b";
	public static final String	LRU_PREFIX = "lru";
	
	public static final String	KEY_APPLICATION_TITLE = "chav1961.ai.gui.Application.title";
	public static final String	KEY_APPLICATION_MESSAGE_SERVER_OK = "chav1961.ai.gui.Application.message.server.ok";
	public static final String	KEY_APPLICATION_MESSAGE_SERVER_FAILED = "chav1961.ai.gui.Application.message.server.failed";
	public static final String	KEY_APPLICATION_MESSAGE_ERROR_DETECTED = "chav1961.ai.gui.Application.message.error.detected";
	public static final String	KEY_APPLICATION_HELP_TITLE = "chav1961.ai.gui.Application.help.title";
	public static final String	KEY_APPLICATION_HELP_CONTENT = "chav1961.ai.gui.Application.help.content";

	public static final String	KEY_MODELS = "menu.main.tools.models";
	
	
    private final JTextArea 	outputArea = new JTextArea();
    private final JCommandLine 	inputField;
    private final AIConnector 	connector;
    private final Options		options = new Options();
    private String 				currentModel = "???";

    public Application(final ContentMetadataInterface mdi, final ArgParser args, final SubstitutableProperties settings) throws NullPointerException {
    	super(mdi, args, settings, true);
        this.inputField = new JCommandLine(getLocalizer(), true, (c)->processCommand(c));
        this.outputArea.setEditable(false);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
        		new JScrollPane(outputArea), 
        		new JScrollPane(inputField));
        splitPane.setResizeWeight(0.8);
        splitPane.setDividerSize(5);
        
        getContentPane().add(splitPane, BorderLayout.CENTER);
        try {
			this.connector = new AIConnector(args.getValue(ARG_SERVER_URI, URI.class));
			this.currentModel = settings.getProperty(SETTINGS_CURRENT_MODEL, String.class, SETTINGS_DEFAULT_CURRENT_MODEL);
	        pingServer();
		} catch (CommandLineParametersException | NullPointerException | IllegalStateException | IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
    }
    
    @OnAction("action:/exit")
    @Override
	protected void exitApplication() {
   		try {
			getSettings().store(FILE_SETTINGS);
		} catch (IOException e) {
		}
    	super.exitApplication();
		System.exit(0);
	}

    private void processCommand(final String command) {
    	final GenerateRequest rq = new GenerateRequest();
    	
    	rq.setModel(currentModel);
    	rq.setPrompt(command);
    	rq.setStream(false);
    	
		try {
			final GenerateResponse	rs = connector.call(APIAction.GENERATE, rq, GenerateResponse.class);
			
			outputArea.append(rs.getResponse());
		} catch (IOException e) {
			getLogger().message(Severity.error, KEY_APPLICATION_MESSAGE_SERVER_FAILED, connector.getServerAddress(), e.getLocalizedMessage());
		}
    }

    @OnAction("action:/settings")
    private void showOptions() {
    	try {
			final Options temp = (Options) options.clone();
			
			if (ask(temp, getLocalizer(), 400, 280)) {
				options.set(temp);
			}
		} catch (CloneNotSupportedException | ContentException e) {
			getLogger().message(Severity.error, e, KEY_APPLICATION_MESSAGE_ERROR_DETECTED, e.getLocalizedMessage());
		}
    }
    
    @OnAction("action:/ping")
    private void pingServer() {
    	try {
			final VersionInfo		info = connector.call(APIAction.VERSION, VersionInfo.class);
			final ModelsResponse	models = connector.call(APIAction.MODEL_LIST, ModelsResponse.class);
			final JMenu 			mwm = (JMenu) SwingUtils.findComponentByName(getJMenuBar(), KEY_MODELS);
			final List<String>		content = new ArrayList<>();
			
			for(ModelResponse model  : models.getModels()) {
				content.add(model.getName()+';'+model.getName());
			}
			SwingUtils.fillRadioSubmenu(mwm, content, currentModel);
			getLogger().message(Severity.info, KEY_APPLICATION_MESSAGE_SERVER_OK, connector.getServerAddress(), info.getVersion());
		} catch (IOException e) {
			getLogger().message(Severity.error, KEY_APPLICATION_MESSAGE_SERVER_FAILED, connector.getServerAddress(), e.getLocalizedMessage());
		}
    }

    @OnAction("action:/menu.main.tools.models")
    private void selectModel(final Hashtable<String,String[]> models) throws LocalizationException {
    	currentModel = models.get("name")[0];
    	getSettings().setProperty(SETTINGS_CURRENT_MODEL, currentModel);
    }
    
    @OnAction("action:/about")
    private void about() {
		SwingUtils.showAboutScreen(this, getLocalizer(), KEY_APPLICATION_HELP_TITLE, KEY_APPLICATION_HELP_CONTENT, URI.create("root://"+getClass().getCanonicalName()+"/chav1961/ai/gui/avatar.jpg"), new Dimension(640, 400));
    }

	public static void main(String[] args) {
		final ArgParser	parser = new ApplicationArgParser();

		try {
			final ArgParser parsed = parser.parse(args);
			final SubstitutableProperties settings = SubstitutableProperties.ofOrEmpty(FILE_SETTINGS);
			
	        SwingUtilities.invokeLater(() -> {
	            final Application frame = new Application(
	            		ContentModelFactory.forXmlDescription(Application.class.getResourceAsStream("application.xml")),
	            		parsed,
	            		settings
	            		);
	            frame.setVisible(true);
	        });
		} catch (CommandLineParametersException exc) {
			System.err.println(exc.getLocalizedMessage());
			System.err.println(parser.getUsage("ai.gui"));
			System.exit(128);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(129);
		}
	}
	
	private static class ApplicationArgParser extends ArgParser {
		private static final ArgParser.AbstractArg[]	KEYS = {
			new URIArg(ARG_SERVER_URI, true, true, "AI server reference")
		};
		
		private ApplicationArgParser() {
			super(KEYS);
		}
	}
	
}
