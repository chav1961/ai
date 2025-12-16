package chav1961.ai.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import chav1961.purelib.basic.ArgParser;
import chav1961.purelib.basic.SubstitutableProperties;
import chav1961.purelib.basic.exceptions.CommandLineParametersException;
import chav1961.purelib.model.ContentModelFactory;
import chav1961.purelib.model.interfaces.ContentMetadataInterface;
import chav1961.purelib.ui.swing.interfaces.OnAction;
import chav1961.purelib.ui.swing.useful.JCommandLine;
import chav1961.purelib.ui.swing.useful.JRichFrame;

public class Application extends JRichFrame {
	private static final long serialVersionUID = 7390506926122202670L;
	
	public static final File	FILE_SETTINGS = new File("./.ai.gui.properties"); 
	public static final String	ARG_SERVER_URI = "serverUri";
	public static final String	LRU_PREFIX = "lru";

    private JTextArea outputArea = new JTextArea();
    private JCommandLine inputField;

    public Application(final ContentMetadataInterface mdi, final ArgParser args, final SubstitutableProperties props) {
    	super(mdi, args, props);
        this.inputField = new JCommandLine(getLocalizer(), (c)->{});
        this.outputArea.setEditable(false);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
        		new JScrollPane(outputArea), 
        		new JScrollPane(inputField));
        splitPane.setResizeWeight(0.8);
        splitPane.setDividerSize(5);
        
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }
    
    @OnAction("action:/exit")
    @Override
	protected void exitApplication() {
    	super.exitApplication();
		System.exit(0);
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
