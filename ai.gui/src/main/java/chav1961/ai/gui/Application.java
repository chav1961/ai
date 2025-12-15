package chav1961.ai.gui;

import java.awt.BorderLayout;

import javax.swing.*;

import chav1961.purelib.basic.PureLibSettings;
import chav1961.purelib.basic.interfaces.LoggerFacade;
import chav1961.purelib.basic.interfaces.LoggerFacadeOwner;
import chav1961.purelib.i18n.interfaces.Localizer;
import chav1961.purelib.ui.swing.SwingUtils;
import chav1961.purelib.ui.swing.useful.JCommandLine;
import chav1961.purelib.ui.swing.useful.JStateString;

public class Application extends JFrame implements LoggerFacadeOwner {
	private static final long serialVersionUID = 7390506926122202670L;

    private JTextArea outputArea = new JTextArea();
    private JCommandLine inputField;
    private final JStateString ss;

    public Application(final Localizer localizer) {
        setTitle("(c) 2025 chav191");
        this.inputField = new JCommandLine(localizer, (c)->{});

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
        		new JScrollPane(outputArea), 
        		new JScrollPane(inputField));
        splitPane.setResizeWeight(0.8);
        splitPane.setDividerSize(5);
        
        getContentPane().add(splitPane, BorderLayout.CENTER);
        this.ss = new JStateString(localizer);
        
        outputArea.setEditable(false);
        
		SwingUtils.assignExitMethod4MainWindow(this, ()->exitApplication());
		SwingUtils.centerMainWindow(this, 0.75f);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	private void exitApplication() {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	@Override
	public LoggerFacade getLogger() {
		return ss;
	}

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Application frame = new Application(PureLibSettings.PURELIB_LOCALIZER);
            
            frame.setVisible(true);
        });
	}
}
