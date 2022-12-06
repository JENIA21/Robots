package network.local;

import gui.IMainApp;
import log.Logger;
import utilities.Parser;

import java.io.IOException;

public class ClientThread extends Thread {
    private IMainApp app;
    private String ip;
    private boolean isActive;

    public ClientThread(IMainApp app, String ip) {
        super("Client thread");
        this.app = app;
        this.ip = ip;
        isActive = true;
    }

    public void run() {
        while (isActive) {
            try {
                var client = new Client(ip);
                client.connect(8081);
                var gameVisualizer = app.getGameVisualizer();
                var text = Parser.convertObjectToJson(gameVisualizer);
                client.send(text);
                client.closeConnection();
                Thread.sleep(400);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disable() {
        isActive = false;
    }
}
