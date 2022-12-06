package network.local;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

public class Client {
    private Socket socket;
    private final String ip;

    public Client(String ip) {
        this.ip = ip;
    }

    public void connect(int port) throws IOException {
        socket = new Socket(ip, port);
    }

    public void send(String text) throws IOException {
        var pw = new PrintWriter(socket.getOutputStream());
        pw.print(text);
        pw.close();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
