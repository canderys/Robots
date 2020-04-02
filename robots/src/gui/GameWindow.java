package gui;

import java.awt.BorderLayout;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ResourceBundle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

import Localization.LanguageChangeable;

public class GameWindow extends JInternalFrame implements IJsonSavable, LanguageChangeable
{
    private final GameVisualizer m_visualizer;
    private static ResourceBundle resourceBundle = ResourceBundleLoader.load("GameWindow");

    public GameWindow() 
    {
        super(resourceBundle.getString("title"), true, true, true, true);
    	ResourceBundleLoader.addElementToUpdate(this);
        m_visualizer = new GameVisualizer();
        setUp();
    }

    public GameWindow(GameFieldInfo info)
    {
        super(resourceBundle.getString("title"), true, true, true, true);
        ResourceBundleLoader.addElementToUpdate(this);
        this.setBounds(info.xCoord, info.yCoord, info.width, info.height);
        try
        {
            setIcon(info.isIcon);
            setMaximum(info.isMaximised);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        m_visualizer = new GameVisualizer(info.xRobot, info.yRobot, info.robotDirection, info.xTarget, info.yTarget);
        setUp();
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

    private void setUp()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
                resourceBundle.getString("exitMessage"),
                resourceBundle.getString("exitAppTitle"),
                null)
        );
        pack();
    }

	@Override
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("GameWindow");
		this.removeInternalFrameListener(this.getInternalFrameListeners()[0]);
		ConfirmDialog dialog = new ConfirmDialog();
		this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
                resourceBundle.getString("exitMessage"),
                resourceBundle.getString("exitAppTitle"),
                null)
        );
		this.setTitle(resourceBundle.getString("title"));
	}
}
