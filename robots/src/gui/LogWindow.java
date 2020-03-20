package gui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import Localization.ResourceBundleLoader;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import serialization.IJsonSavable;
import serialization.JSONSaveLoader;
import serialization.LogFieldInfo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener, IJsonSavable
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private final static ResourceBundle resourceBundle = ResourceBundleLoader.load("LogWindow");

    public LogWindow(LogWindowSource logSource) 
    {
        super(resourceBundle.getString("title"), true, true, true, true);
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
        this.addInternalFrameListener(dialog.ShowConfirmDialogJInternalFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"))
        );
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
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
}
