package gui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import Localization.ResourceBundleLoader;
import Localization.LanguageChangeable;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import serialization.IJsonSavable;
import serialization.JSONSaveLoader;
import serialization.LogFieldInfo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener, 
	IJsonSavable, LanguageChangeable, CloseInternalFrame
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private LogEntry[] m_startContent;
    private static ResourceBundle resourceBundle = ResourceBundleLoader.load("LogWindow");

    public LogWindow(LogWindowSource logSource) 
    {
        super(resourceBundle.getString("title"), true, true, true, true);
        initialize(logSource);
    }
    
    public LogWindow(LogWindowSource logSource, LogEntry[] content)
    {
    	super(resourceBundle.getString("title"), true, true, true, true);
    	m_startContent = content;
    	initialize(logSource);
    }

    private void initialize(LogWindowSource logSource)
    {
    	ResourceBundleLoader.addElementToUpdate(this);
    	m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addInternalFrameListener(dialog.showConfirmDialogJInternalFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"),
        		this)
        );
    }
    
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        if (m_startContent != null)
        {
	        for (LogEntry entry : m_startContent)
	        {
	        	content.append(resourceBundle.getString(entry.getMessage())).append("\n");
	        }
        }
        for (LogEntry entry : m_logSource.all())
        {
            content.append(resourceBundle.getString(entry.getMessage())).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    public LogEntry[] getStartContent()
    {
    	return m_startContent;
    }
    
    public Iterable<LogEntry> getLogInfo()
    {
        return m_logSource.all();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void saveJSON() {
        JSONSaveLoader saver = new JSONSaveLoader();
        LogFieldInfo info = saver.getLogFieldInfo(this);
        saver.save(getSavePath(), info);
    }

    @Override
    public String getSavePath() {
        return "saves/log.json";
    }

	@Override
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("LogWindow");
		this.setTitle(resourceBundle.getString("title"));
		this.removeInternalFrameListener(this.getInternalFrameListeners()[0]);
		ConfirmDialog dialog = new ConfirmDialog();
		this.addInternalFrameListener(dialog.showConfirmDialogJInternalFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"),
        		this)
        );
		updateLogContent();
	}

	@Override
	public void close(InternalFrameEvent e) {
		 m_logSource.unregisterListener((LogChangeListener) e.getInternalFrame());
		
	}
}
