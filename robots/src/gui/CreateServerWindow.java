package gui;

import network.local.ClientThread;
import network.local.Server;
import network.local.ServerThread;
import utilities.Parser;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CreateServerWindow extends JInternalFrame {
    private final IMainApp app;
    private Server server;
    private ServerThread thread;
    private boolean isActive = false;
    private final JButton button;
    public CreateServerWindow(IMainApp app) {
        super("Ожидание подключения", false, true, false, true);
        this.app = app;
        var infoLabel = new JLabel("Вы уверены что хотите запустить сервер?");
        button = new JButton("Запустить сервер");
        this.setBounds(600, 100, 300, 120);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        button.addActionListener((event) -> {
            create();
        });

        var container = this.getContentPane();
        container.setLayout(new GridLayout(2, 1));
        container.add(infoLabel);
        container.add(button);
    }

    private void create() {
        if(isActive){
            thread.disable();
            isActive = false;
            button.setText("Запустить сервер");
        }
        else{
            thread = new ServerThread(app);
            thread.start();
            isActive = true;
            button.setText("Отключить сервер");
        }
    }
}
