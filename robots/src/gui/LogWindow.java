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
import serialization.ISavableWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener, 
	LanguageChangeable, CloseInternalFrame, ISavableWindow
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private LogEntry[] m_startContent;
    public static ResourceBundle resourceBundle = ResourceBundleLoader.load("LogWindow");
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
        this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
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
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("LogWindow");
		this.setTitle(resourceBundle.getString("title"));
		this.removeInternalFrameListener(this.getInternalFrameListeners()[0]);
		ConfirmDialog dialog = new ConfirmDialog();
		this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
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


	@Override
	public HashMap<String, Object> getWindowInfo() {
		int arrayLen = 0;
        Iterable<LogEntry> iter = this.getLogInfo();
        LogEntry[] startContent = this.getStartContent();
        if (startContent != null)
	        for (LogEntry current : startContent)
	        {
	        	arrayLen++;
	        }
	        
        for (LogEntry current : iter)
        {
        	arrayLen++;
        }
        LogEntry[] logInfo = new LogEntry[arrayLen];
        iter = this.getLogInfo();
        startContent = this.getStartContent();
        int i = 0;
        if (startContent != null)
	        for (LogEntry current : startContent)
	        {
	            logInfo[i] = current;
	            i++;
	        }
	        
        for (LogEntry current : iter)
        {
            logInfo[i] = current;
            i++;
        }
        System.out.print(logInfo.length);
		HashMap<String, Object> info = new HashMap<String, Object>();
        info.put("logEntry", logInfo);
        
        return info;
	}

	@Override
	public String getFileName() {
		return "LogWindowSave.json";
	}

	@Override
	public boolean isMaximised() {
		return this.isMaximum();
	}
	
}
