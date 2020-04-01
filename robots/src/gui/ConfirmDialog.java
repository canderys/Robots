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
	
	public void ShowConfirmDialog(String exitMessage, String exitTitle,
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
            }
	}
	
	public void ShowConfirmLoadDialog(String exitMessage, String exitTitle,
			Object frame, FrameType frameType, LoadStatus loadStatus)
	{
		String[] options = new String[2];
		options[0] = resourceBundle.getString("Agree");
		options[1] = resourceBundle.getString("Disagree");
		int result = JOptionPane.showOptionDialog((Component) frame, exitMessage, exitTitle,
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
		if (result == JOptionPane.YES_OPTION) 
		{
			loadStatus.setLoad(true);
			if(frameType == FrameType.JFrame) 
			{
				//((JFrame) frame).dispose();
				
			}
			else 
			{
				//((JInternalFrame) frame).dispose();
			}
		}
		else
		{
			loadStatus.setLoad(false);
		}
	}
	
	public InternalFrameAdapter ShowConfirmDialogJInternalFrame(String exitMessage, String exitTitle)
	{
		return new InternalFrameAdapter(){
        	public void internalFrameClosing(InternalFrameEvent e) {
        		ShowConfirmDialog(exitMessage, exitTitle, e.getSource(), FrameType.JInternalFrame);
        	}
        };
	}
	
	public WindowAdapter ShowConfirmDialogJFrame(String exitMessage, String exitTitle)
	{
		 return new WindowAdapter(){
	            public void windowClosing(WindowEvent e){
	            	ShowConfirmDialog(exitMessage, exitTitle, e.getSource(), FrameType.JFrame);
	            }
	        };
	}
	
	public WindowAdapter ShowConfirmLoadDialogJFrame(String exitMessage, String exitTitle, LoadStatus loadStatus)
	{
		 return new WindowAdapter(){
	            public void windowOpened(WindowEvent e)
	            {
	            	ShowConfirmLoadDialog(exitMessage, exitTitle, e.getSource(), FrameType.JFrame, loadStatus);
	            }
	        };
	}
}
