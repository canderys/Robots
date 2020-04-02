package gui;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import Localization.ResourceBundleLoader;
import serialization.IJsonSavable;
import serialization.LoadStatus;

public class ConfirmDialog {
	
	private final static ResourceBundle resourceBundle = ResourceBundleLoader.load("ConfirmDialog");
	
	public boolean createExitConfirmDialog(String exitMessage, String exitTitle,
								Object frame, FrameType frameType)
	{
		String[] options = new String[2];
		options[0] = resourceBundle.getString("Agree");
		options[1] = resourceBundle.getString("Disagree");
        int result = JOptionPane.showOptionDialog((Component) frame, exitMessage, exitTitle,
        		JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
        		null, options, options[1]);
        
            if (result == JOptionPane.YES_OPTION) 
            {
            	if(frameType == FrameType.JFrame) 
            	{
            		if(frame instanceof IJsonSavable)
            			((IJsonSavable) frame).saveJSON();
					((JFrame) frame).dispose();
					System.exit(0);
				}
            	else 
            	{
            		if (frame instanceof IJsonSavable)
						((IJsonSavable) frame).saveJSON();
					((JInternalFrame) frame).dispose();
				}
            	return true;
            }
            return false;
	}
	
	
	public InternalFrameAdapter showExitConfirmDialogJInternalFrame(String exitMessage, 
			String exitTitle, CloseInternalFrame close)
	{
		return new InternalFrameAdapter(){
        	public void internalFrameClosing(InternalFrameEvent e) {
        		if(createExitConfirmDialog(exitMessage, exitTitle, e.getSource(), FrameType.JInternalFrame) &&
        				close != null)
        			close.close(e);
        	}
        };
	}
	
	public WindowAdapter showExitConfirmDialogJFrame(String exitMessage, String exitTitle)
	{
		 return new WindowAdapter(){
	            public void windowClosing(WindowEvent e){
	            	createExitConfirmDialog(exitMessage, exitTitle, e.getSource(), FrameType.JFrame);
	            }
	        };
	}
	
	private void createConfirmDialogWithAction(String exitMessage, String exitTitle,
			Object frame, FrameType frameType, ActionDialog action)
	{
		String[] options = new String[2];
		options[0] = resourceBundle.getString("Agree");
		options[1] = resourceBundle.getString("Disagree");
		int result = JOptionPane.showOptionDialog((Component) frame, exitMessage, exitTitle,
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
		if (result == JOptionPane.YES_OPTION) 
			action.executeTrue();
		else
			action.executeFalse();
	}
	
	public WindowAdapter showOpenConfirmDialogJFrame(String exitMessage, 
			String exitTitle, ActionDialog action)
	{
		 return new WindowAdapter(){
	            public void windowOpened(WindowEvent e)
	            {
	            	createConfirmDialogWithAction(exitMessage, exitTitle, 
	            			e.getSource(), FrameType.JFrame, action);
	            }
	        };
	}
}
