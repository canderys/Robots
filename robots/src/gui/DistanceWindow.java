package gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.TextArea;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import Localization.LanguageChangeable;
import Localization.ResourceBundleLoader;
import model.GameModel;
import model.RobotObserver;
import model.TargetObserver;
import serialization.ISavableWindow;

public class DistanceWindow extends JInternalFrame implements LanguageChangeable, ISavableWindow, RobotObserver, TargetObserver
{
	public static ResourceBundle resourceBundle = ResourceBundleLoader.load("DistanceWindow");
	
	private TextArea content;
	private GameModel model;
	
	private Point robot = new Point(0, 0);
	private Point target = new Point(0, 0);
	
	public DistanceWindow(GameModel m)
	{
		super(resourceBundle.getString("title"), true, true, true, true);
		ResourceBundleLoader.addElementToUpdate(this);
		content = new TextArea("");
		content.setSize(50, 30);
		content.setEditable(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(50, 30);
        panel.add(content, BorderLayout.CENTER);
        getContentPane().add(panel);
        m.getRobotModel().registerObserver(this);
        m.getTargetModel().registerObserver(this);
        onCoordinateChange(m.getRobotState());
        onTargetCoordinateChange(m.getTargetPosition().x, m.getTargetPosition().y);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"),
        		null)
        );
	}
	
	@Override
	public boolean isMaximised() {
		return this.isMaximum;
	}

	@Override
	public HashMap<String, Object> getWindowInfo() {
		return null;
	}

	@Override
	public String getFileName() {
		return "DistanceWindowSave.json";
	}

	@Override
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("DistanceWindow");
		this.setTitle(resourceBundle.getString("title"));
		this.removeInternalFrameListener(this.getInternalFrameListeners()[0]);
		ConfirmDialog dialog = new ConfirmDialog();
		this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
                resourceBundle.getString("exitMessage"),
                resourceBundle.getString("exitAppTitle"),
                null)
        );
		calculateDistance();
	}

	@Override
	public void onCoordinateChange(RobotState state) {
		robot = new Point((int)state.x, (int)state.y);
		calculateDistance();
	}

	@Override
	public void onTargetCoordinateChange(int x, int y) {
		target = new Point(x, y);
		calculateDistance();
	}
	
	private void calculateDistance()
	{
		int distance = (int)Math.sqrt((robot.x - target.x) * (robot.x - target.x) + (robot.y - target.y) * (robot.y - target.y)); 
		content.setText(resourceBundle.getString("title") + ": " + distance);
		content.invalidate();
	}

}
