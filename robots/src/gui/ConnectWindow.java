package gui;

import network.local.ClientThread;
import log.Logger;
import network.local.Client;
import utilities.Parser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConnectWindow extends JInternalFrame {
    private ClientThread thread;
    private Client client;
    private boolean isActive = false;
    private final JButton button;

    public ConnectWindow(IMainApp app) {
        super("Ввод ip", false, true, false, true);
        var inputField = new JTextField("");
        button = new JButton("Подключиться");
        this.setBounds(600, 100, 300, 120);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        button.addActionListener((e) -> {
            if (isActive) {
                thread.disable();
                isActive = false;
                button.setText("Подключиться");
            } else {
                thread = new ClientThread(app, inputField.getText());
                Logger.info("Успешное подключение");
                thread.start();
                isActive = true;
                button.setText("Отключиться");

            }
        });
        var container = this.getContentPane();
        container.setLayout(new GridLayout(2, 1));
        container.add(inputField);
        container.add(button);
    }
}
