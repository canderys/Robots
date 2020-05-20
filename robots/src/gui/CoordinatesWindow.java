package gui;

import java.awt.BorderLayout;
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
import serialization.ISavableWindow;

public class CoordinatesWindow extends JInternalFrame implements LanguageChangeable, ISavableWindow, RobotObserver
{
	public static ResourceBundle resourceBundle = ResourceBundleLoader.load("CoordinatesWindow");
	
	private TextArea content;
	private GameModel model;
	
	public CoordinatesWindow(GameModel m)
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
        onCoordinateChange(m.getRobotState());
        
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
		return "CoordinatesWindowSave.json";
	}

	@Override
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("CoordinatesWindow");
		this.setTitle(resourceBundle.getString("title"));
		this.removeInternalFrameListener(this.getInternalFrameListeners()[0]);
		ConfirmDialog dialog = new ConfirmDialog();
		this.addInternalFrameListener(dialog.showExitConfirmDialogJInternalFrame(
                resourceBundle.getString("exitMessage"),
                resourceBundle.getString("exitAppTitle"),
                null)
        );
	}

	@Override
	public void onCoordinateChange(RobotState state) {
		content.setText("X:" + Math.round(state.x) + " Y:" + Math.round(state.y));
		content.invalidate();
	}

}
