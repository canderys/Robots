package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.google.gson.internal.LinkedTreeMap;

import Localization.ResourceBundleLoader;
import log.LogEntry;
import log.LogLevel;
import model.EnemyModel;
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
        ArrayList<LinkedTreeMap>  list= (ArrayList<LinkedTreeMap>)gameInfo.get("listEnemy");
        ArrayList<EnemyModel>  enemyList = new ArrayList<EnemyModel>();
    	int i = 0;
    	for(LinkedTreeMap enemy : list)
    	{
    		double x = (double) enemy.get("x");
    		double y = (double)enemy.get("y");
    		double direction = (double)enemy.get("direction");
    		double width = (double)enemy.get("fieldWidth");
    		double height = (double)enemy.get("fieldHeight");
    		enemyList.add(new EnemyModel(x, y, direction, width, height));
    	}
        m_visualizer = new GameVisualizer((double)gameInfo.get("xRobot"), (double)gameInfo.get("yRobot"), 
        		(double)gameInfo.get("robotDirection"), (double)gameInfo.get("xTarget"), 
        		(double)gameInfo.get("yTarget"), enemyList);
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
		info.put("listEnemy", m_visualizer.getGameModel().getEnemyList());
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
