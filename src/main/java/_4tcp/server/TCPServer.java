package _4tcp.server;

import _4tcp.server.heandle.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 与客户端 TCP 通讯
 */
public class TCPServer {
    private final int port;
    private ClientListener mListener;
    private List<ClientHandler> clientHandlerList = new ArrayList<>();

    public TCPServer(int portServer) {
        this.port = portServer;
    }

    public boolean start() {
        try {
            ClientListener listener = new ClientListener(port);
            mListener = listener;
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stop() {
        if (mListener != null) {
            mListener.exit();
        }
        clientHandlerList.forEach(client -> {
            client.exit();
        });
        clientHandlerList.clear();
    }

    public void broadcast(String message) {
        clientHandlerList.forEach(client -> {
            client.send(message);
        });
    }

    private static class ClientListener extends Thread {
        private ServerSocket server;
        private boolean done = false;

        private ClientListener(int port) throws IOException {
            server = new ServerSocket(port);
            System.out.println("服务器信息:" + server.getInetAddress() + ":" + server.getLocalPort());
        }

        @Override
        public void run() {
            System.out.println("服务器准备就绪~~");
            //等待客户端连接
            do {
                Socket client;
                try {
                    client = server.accept();
                } catch (IOException e) {
                    continue;
                }
                //客户端构建异步线程
                ClientHandler clientHandler = new ClientHandler(client);
                //读取信息并打印
                clientHandler.readToPrint();
            } while (!done);
            System.out.println("服务器已关闭!");
        }

        public void exit() {
            done = true;
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
