package gui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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
import model.GameModel;
import serialization.ISavableWindow;
import serialization.WindowDescriptor;
import Localization.LanguageChangeable;

public class GameWindow extends JInternalFrame implements LanguageChangeable, ISavableWindow
{
    private GameVisualizer m_visualizer;
    private static ResourceBundle resourceBundle = ResourceBundleLoader.load("GameWindow");

    public GameWindow() 
    {
        super(resourceBundle.getString("title"), true, true, true, true);
    	ResourceBundleLoader.addElementToUpdate(this);
        m_visualizer = new GameVisualizer();
        setUp();
    }
    
    public GameWindow(WindowDescriptor window)
    {
    	super(resourceBundle.getString("title"), true, true, true, true);
    	HashMap<String, Object> gameInfo = window.windowInfo;
        ResourceBundleLoader.addElementToUpdate(this);
        this.setBounds(window.x, window.y,window.width, window.height);
        try
        {
            setIcon(window.isIcon);
            setMaximum(window.isMaximum);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        m_visualizer = new GameVisualizer((double)gameInfo.get("xRobot"), (double)gameInfo.get("yRobot"), 
        		(double)gameInfo.get("robotDirection"), (double)gameInfo.get("xTarget"), 
        		(double)gameInfo.get("yTarget"));
        setUp();
    }

    public GameModel getGameModel()
    {
    	return m_visualizer.getGameModel();
    }
    
    public RobotState getRobotState()
    {
        return m_visualizer.getRobotState();
    }
  

    public Point2D.Double getTargerCoordinates()
    {
        return m_visualizer.getTargetCoordinates();
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
        this.addComponentListener(new ComponentAdapter() {
        	public void componentResized(ComponentEvent e)
        	{
        		m_visualizer.changeSize(getWidth(), getHeight());
        	}
		});
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
	
	public String getId() {
		return "Game";
	}

	public HashMap<String, Object> getWindowInfo() {
		HashMap<String, Object> info = new HashMap<String, Object>();
		RobotState state = this.getRobotState();
		Point2D.Double target = this.getTargerCoordinates();
		info.put("xRobot", state.x);
		info.put("yRobot", state.y);
		info.put("robotDirection", state.direction);
		info.put("xTarget", target.x);
		info.put("yTarget", target.y);
		return info;
	}
	
	public String getFileName() {
		return "gameWindowSave.json";
	}

	@Override
	public boolean isMaximised() {
		return this.isMaximum();
	}
}
