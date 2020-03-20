package gui;

import java.awt.BorderLayout;
import java.awt.geom.Point2D;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import Localization.ResourceBundleLoader;
import serialization.GameFieldInfo;
import serialization.IJsonSavable;
import serialization.JSONSaveLoader;

public class GameWindow extends JInternalFrame implements IJsonSavable
{
    private final GameVisualizer m_visualizer;
    private final static ResourceBundle resourceBundle = ResourceBundleLoader.load("GameWindow");
    public GameWindow() 
    {
        super(resourceBundle.getString("title"), true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addInternalFrameListener(dialog.ShowConfirmDialogJInternalFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"))
        );
        pack();
    }

    public RobotState getRobotState()
    {
        return m_visualizer.getRobotState();
    }

    public Point2D.Double getTargerCoordinates()
    {
        return m_visualizer.getTargetCoordinates();
    }

    @Override
    public void saveJSON()
    {
        JSONSaveLoader saver = new JSONSaveLoader();
        GameFieldInfo info = saver.getGameFieldInfo(this);
        saver.save(getSavePath(), info);
    }

    @Override
    public String getSavePath() {
        return "saves/game.json";
    }
}
