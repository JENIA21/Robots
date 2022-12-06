package network.local;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    ServerSocket server;
    Socket client;

    public void startServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public void waitUser() throws IOException {
        client = server.accept();
    }

    public String getMessage() throws IOException {
        StringBuilder request = new StringBuilder();
        Scanner in = new Scanner(client.getInputStream());
        while(in.hasNext())
            request.append(in.nextLine());
        in.close();
        return request.toString();
    }
    public void closeConnection() throws IOException {
        server.close();
    }
}
