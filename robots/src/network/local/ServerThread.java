package network.local;

import gui.IMainApp;
import log.Logger;
import utilities.Parser;

import java.io.IOException;

public class ServerThread extends Thread {
    private IMainApp app;
    private boolean isActive;

    public ServerThread(IMainApp app) {
        super("Server thread");
        this.app = app;
        isActive = true;
    }

    public void run() {
        while (isActive) {
            try {
                var server = new Server();
                server.startServer(8081);
                server.waitUser();
                Logger.info("Клиент подключился");
                var msg = server.getMessage();
                var obj = Parser.parseFromJsonToObject(msg);
                app.setGameVisualizer(obj);
                server.closeConnection();
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
