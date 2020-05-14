package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.google.gson.internal.LinkedTreeMap;

import Localization.ResourceBundleLoader;
import log.LogLevel;
import log.LogEntry;
import log.Logger;
import serialization.ISavableWindow;
import serialization.JsonWindowsSaver;
import serialization.LoadStatus;
import serialization.WindowDescriptor;
import serialization.WindowsSaver;
import Localization.LanguageChangeable;

public class MainApplicationFrame extends JFrame implements 
		LanguageChangeable, ActionDialog, ISavableWindow, ExitAction
{
	
    private final JDesktopPane desktopPane = new JDesktopPane();
    private ResourceBundle resourceBundle = ResourceBundleLoader.load("MainApplicationFrame");
    private WindowsSaver windowsSaver = new JsonWindowsSaver();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
    	ResourceBundleLoader.addElementToUpdate(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addWindowListener(dialog.showExitConfirmDialogJFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"), this)
        );
        setContentPane(desktopPane);
        if (windowsSaver.isSavesExits())
        {
        	ConfirmDialog loadDialog = new ConfirmDialog();
        	this.addWindowListener(
        			loadDialog.showOpenConfirmDialogJFrame(resourceBundle.getString("loadMessage"), 
            		resourceBundle.getString("loadTitle"), this));
        }
        else {
        	createMainApplication();
        }
    }
    
    private void createMainApplication()
    {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);
        
    	LogWindow logWindow = createLogWindow(10, 10, 300, 800, null);
    	addWindow(logWindow);
    	GameWindow gameWindow = new GameWindow();
    	gameWindow.setSize(400,  400);
    	gameWindow.setLocation(10, 10);
        addWindow(gameWindow);
        setJMenuBar(generateMenuBar());
        windowsSaver.registerWindow(logWindow, "log");
        windowsSaver.registerWindow(gameWindow, "game");
        windowsSaver.registerWindow(this, "main");
    }
    
    private void createMainApplicationBySaves()
    {
    	WindowDescriptor loadedMainFrame = windowsSaver.load(windowsSaver.getFileNameWindowById("main"));
    	this.setBounds(loadedMainFrame.x, loadedMainFrame.y, 
    			loadedMainFrame.width, loadedMainFrame.height);
    	
    	WindowDescriptor loadedLogWin = windowsSaver.load(windowsSaver.getFileNameWindowById("log"));
    	HashMap<String, Object> logInfo = loadedLogWin.windowInfo;
    	ArrayList<LinkedTreeMap> logs = (ArrayList<LinkedTreeMap>)(logInfo.get("logEntry"));
    	LogEntry[] logEntryArray = new LogEntry[logs.size()];
    	int i = 0;
    	for(LinkedTreeMap log : logs)
    	{
    		String logLevel = (String) log.get("m_logLevel");
    		String strMessage = (String)log.get("m_strMessage");
    		LogLevel level = LogLevel.valueOf(logLevel);
    		logEntryArray[i] = new LogEntry(level, strMessage);
    		i++;
    	}
    	LogWindow logWindow = createLogWindow(loadedLogWin.x,loadedLogWin.y, 
    			loadedLogWin.width, loadedLogWin.height, logEntryArray);
    	logWindow.setBounds(loadedLogWin.x,loadedLogWin.y, 
    			loadedLogWin.width, loadedLogWin.height);
    	try {
			logWindow.setIcon(loadedLogWin.isIcon);
			logWindow.setMaximum(loadedLogWin.isMaximum);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
        addWindow(logWindow);
        
        WindowDescriptor loadedGameWin = windowsSaver.load(windowsSaver.getFileNameWindowById("game"));
        GameWindow gameWindow = new GameWindow(loadedGameWin);
        gameWindow.setBounds(loadedGameWin.x, loadedGameWin.y, loadedGameWin.width, loadedGameWin.height);
    	try {
    		gameWindow.setIcon(loadedGameWin.isIcon);
    		gameWindow.setMaximum(loadedGameWin.isMaximum);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
        addWindow(gameWindow);
        setJMenuBar(generateMenuBar());
        
        windowsSaver.registerWindow(logWindow, "log");
        windowsSaver.registerWindow(gameWindow, "game");
        windowsSaver.registerWindow(this, "main");
        
    }
    
    protected LogWindow createLogWindow(int posX, int posY, int sizeX, int sizeY, LogEntry[] content)
    {
    	LogWindow logWindow;
    	if (content != null)
    		logWindow = new LogWindow(Logger.getDefaultLogSource(), content);
    	else
    	{
    		logWindow = new LogWindow(Logger.getDefaultLogSource());
    		Logger.debug("LogWindowMessage");
    	}
        logWindow.setLocation(posX, posY);
        logWindow.setSize(sizeX, sizeY);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    private JMenuBar generateMenuBar()
    {	
        JMenuBar menuBar = new JMenuBar();
        JMenu lookAndFeelMenu = createLookAndFeel();
        JMenu testMenu = createTestMenu();
        JMenuItem exitMenu = createExitMenu();
        JMenuItem languageMenu = createLanguageMenu();

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(languageMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private JMenu createLookAndFeel()
    {
        JMenu lookAndFeelMenu = new JMenu(resourceBundle.getString("titleViewMode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
        		resourceBundle.getString("controlViewMode"));

        {
            JMenuItem systemLookAndFeel = new JMenuItem(resourceBundle.getString("systemLookAndFeel"), 
            		KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(resourceBundle.getString("crossplatformLookAndFeel"), 
            		KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu()
    {
        JMenu testMenu = new JMenu(resourceBundle.getString("testMenuTitle"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(resourceBundle.getString("testCommands"));
        {
            JMenuItem addLogMessageItem = new JMenuItem(resourceBundle.getString("testMessageLog"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("testDebugMessageLog");
            });
            testMenu.add(addLogMessageItem);
        }

        {
            JMenuItem addLogMessageItem = new JMenuItem(resourceBundle.getString("testAnotherLog"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("testDebugAnotherLog");
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }
    
    private JMenuItem createExitMenu()
    {
    	JMenuItem exitMenu = new JMenuItem(resourceBundle.getString("exitMenuTitle"));
    	exitMenu.setMnemonic(KeyEvent.VK_S);
    	ConfirmDialog dialog = new ConfirmDialog();
    	exitMenu.addActionListener((event)-> {dialog.createExitConfirmDialog(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"),
    			this, 
    			FrameType.JFrame, 
    			this);});
    	return exitMenu;
    }
    
    private JMenuItem createLanguageMenu()
    {
    	JMenu languageMenu = new JMenu(resourceBundle.getString("languageMenuTitle"));
    	languageMenu.setMnemonic(KeyEvent.VK_L);
        languageMenu.getAccessibleContext().setAccessibleDescription(resourceBundle.getString("testCommands"));
        {
            JMenuItem addLogMessageItem = new JMenuItem(resourceBundle.getString("languageRu"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("languageChangedRu");
                ResourceBundleLoader.updateLanguage(Locale.getDefault());
            });
            languageMenu.add(addLogMessageItem);
        }

        {
            JMenuItem addLogMessageItem = new JMenuItem(resourceBundle.getString("languageEn"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("languageChangedEn");
                ResourceBundleLoader.updateLanguage(Locale.US);
            });
            languageMenu.add(addLogMessageItem);
        }
        return languageMenu;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
    
    private void checkSaveFolder(String name)
    {
    	if (!checkFileExist(name)) {
    		 File file = new File(name);
    	     file.mkdir();
    	}
    }

    
    private boolean checkFileExist(String name)
    {
    	Path path = Paths.get(name);
    	return Files.exists(path);
    }

	@Override
	public void changeLanguage() {
		resourceBundle = ResourceBundleLoader.load("MainApplicationFrame");
		this.getJMenuBar().removeAll();
        setJMenuBar(generateMenuBar());
        
        this.removeWindowListener(this.getWindowListeners()[0]);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addWindowListener(dialog.showExitConfirmDialogJFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"),
        		null)
        );
	}

	@Override
	public void executeTrue() {
		createMainApplicationBySaves();
	}

	@Override
	public void executeFalse() {
		createMainApplication();
	}


	@Override
	public HashMap<String, Object> getWindowInfo() {
		return null;
	}

	@Override
	public String getFileName() {
		return "MainFrameSaved.json";
	}

	@Override
	public void doExitAction() {
		windowsSaver.save();
		
	}

	@Override
	public boolean isIcon() {
		return false;
	}

	@Override
	public boolean isMaximised() {
		return false;
	}
}
