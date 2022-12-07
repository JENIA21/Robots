package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.menu.MenuGenerator;
import log.Logger;
import utilities.parsedclass.GameVisualizerData;

public class MainApplicationFrame extends JFrame implements IMainApp
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final GameWindow gameWindow;
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public GameVisualizerData getGameVisualizer(){
        return gameWindow.getGameVisualizer();
    }
    public void setGameVisualizer(GameVisualizerData gvd){
        gameWindow.setGameVisualizer(gvd);
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(1000,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    private JMenuBar generateMenuBar()
    {
        var menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createGameMenu());
        return menuBar;
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
    private JMenu createLookAndFeelMenu(){
        var lookAndFeelMenu = MenuGenerator.createMenu("Режим отображения",
                "Управление режимом отображения приложения",
                KeyEvent.VK_V);

        var systemLookAndFeel = MenuGenerator.createMenuItem("Системная схема",
                KeyEvent.VK_S, (event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate(); });

        var crossplatformLookAndFeel = MenuGenerator.createMenuItem("Универсальная схема",
                KeyEvent.VK_U, (event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate(); });

        lookAndFeelMenu.add(systemLookAndFeel);
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        return lookAndFeelMenu;
    }
    private JMenu createTestMenu(){
        var testMenu = MenuGenerator.createMenu("Тесты","Тестовые команды",KeyEvent.VK_T);
        var addLogMessageItem = MenuGenerator.createMenuItem("Сообщение в лог",
                KeyEvent.VK_S, (event) -> Logger.debug("Новая строка"));
        testMenu.add(addLogMessageItem);
        return testMenu;
    }
    private JMenu createGameMenu(){
        var gameMenu = MenuGenerator.createMenu("Управление",
                "Управление симуляцией", KeyEvent.VK_L);
        var addRobot = MenuGenerator.createMenuItem("Добавить робота на стандартную позицию",
                KeyEvent.VK_A, (event) -> {
            gameWindow.addBot();
            Logger.debug("Робот добавлен");});
        var connectWithSomeProgram = MenuGenerator.createMenuItem("Передать симуляцию по локальной сети",
                KeyEvent.VK_C, (event) ->{
                    ConnectWindow c = new ConnectWindow(this);
                    addWindow(c);
                });
        var createServer = MenuGenerator.createMenuItem("Получить симуляцию по локальной сети",
                KeyEvent.VK_C, (event) ->{
                    CreateServerWindow c = new CreateServerWindow(this);
                    addWindow(c);
                });
        gameMenu.add(addRobot);
        gameMenu.add(connectWithSomeProgram);
        gameMenu.add(createServer);
        return gameMenu;
    }
}
