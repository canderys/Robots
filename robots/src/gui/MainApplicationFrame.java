package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import Localization.ResourceBundleLoader;
import log.LogEntry;
import log.Logger;
import serialization.FieldInfo;
import serialization.IJsonSavable;
import serialization.JSONSaveLoader;
import serialization.LoadStatus;
import serialization.GameFieldInfo;
import serialization.LogFieldInfo;
import Localization.LanguageChangeable;

public class MainApplicationFrame extends JFrame implements 
		IJsonSavable, LanguageChangeable, ActionDialog
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private ResourceBundle resourceBundle = ResourceBundleLoader.load("MainApplicationFrame");

    private final List<IJsonSavable> saveOnClose = new ArrayList<>();
    
    private boolean dataLoaded = false;
    private FieldInfo fieldInfo;
    private GameFieldInfo gameFieldInfo;
    private LogFieldInfo logInfo;
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
    	dataLoaded = loadJSON();
    	
    	ResourceBundleLoader.addElementToUpdate(this);
        int inset = 50;
        	
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ConfirmDialog dialog = new ConfirmDialog();
        this.addWindowListener(dialog.showExitConfirmDialogJFrame(
        		resourceBundle.getString("exitMessage"), 
        		resourceBundle.getString("exitAppTitle"))
        );

        setContentPane(desktopPane);
        
        LoadStatus loadStatus = new LoadStatus();
        if (dataLoaded)
        {
        	ConfirmDialog loadDialog = new ConfirmDialog();
        	this.addWindowListener(
        			loadDialog.showOpenConfirmDialogJFrame(resourceBundle.getString("loadMessage"), 
            		resourceBundle.getString("loadTitle"), this));
        }
        else
        	createMainApplication();
    }
    
    private void createMainApplication()
    {
    	LogWindow logWindow = createLogWindow(10, 10, 300, 800, null);
    	addWindow(logWindow);
    	saveOnClose.add(logWindow);
    	GameWindow gameWindow = new GameWindow();
    	gameWindow.setSize(400,  400);
    	gameWindow.setLocation(10, 10);
        addWindow(gameWindow);
        saveOnClose.add(gameWindow);
        setJMenuBar(generateMenuBar());
    }
    
    private void createMainApplicationBySaves()
    {
    	LogWindow logWindow = createLogWindow(logInfo.xCoord, logInfo.yCoord, 
    						logInfo.width, logInfo.height, logInfo.logInfo);
        addWindow(logWindow);
        saveOnClose.add(logWindow);
        GameWindow gameWindow = new GameWindow(gameFieldInfo);
    	gameWindow.setSize(gameFieldInfo.width, gameFieldInfo.height);
    	gameWindow.setLocation(gameFieldInfo.xCoord, gameFieldInfo.yCoord);
        addWindow(gameWindow);
        saveOnClose.add(gameWindow);
        setJMenuBar(generateMenuBar());
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
    			FrameType.JFrame);});
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

    @Override
    public void saveJSON() {
        JSONSaveLoader saver = new JSONSaveLoader();
        FieldInfo info = saver.getMainFrameInfo(this);
        checkSaveFolder("saves");
        saver.saveMainFrame(getSavePath(), info);
        for (IJsonSavable item : saveOnClose)
        {
            item.saveJSON();
        }
    }
    
    private void checkSaveFolder(String name)
    {
    	if (!checkFileExist(name)) {
    		 File file = new File(name);
    	     file.mkdir();
    	}
    }

    @Override
    public String getSavePath() {
        return "saves/MainFrame.json";
    }
    
    private boolean loadJSON()
    {
    	if (!checkFileExist("saves/MainFrame.json") || !checkFileExist("saves/game.json") || !checkFileExist("saves/log.json"))
    		return false;
    	
    	JSONSaveLoader loader = new JSONSaveLoader();
    	logInfo = loader.loadLogInfo("saves/log.json");
    	gameFieldInfo = loader.loadGameInfo("saves/game.json");
    	fieldInfo = loader.loadMainFrameInfo("saves/MainFrame.json");
    	return true;
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
        		resourceBundle.getString("exitAppTitle"))
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
}
