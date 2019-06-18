package _4tcp.server.heandle;

import _4tcp.tools.CloseUtils;

import java.io.*;
import java.net.Socket;

/**
 * 客户端消息处理
 */
public class ClientHandler {
    private Socket socket;
    private boolean flag = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("新客户端连接:" + socket.getInetAddress() + ":" + socket.getPort());

    }


    public void send(String message) {


    }

    public void readToPrint() {

    }

    public void exit() {

    }

    private void exitBySelf() {
        exit();

    }

    class ClientReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;

        ClientReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                //得到输入流,用于接收数据
                BufferedReader socketInput = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                do {
                    Thread.sleep(500);
                    //客户端拿到一条数据
                    String str = socketInput.readLine();
                    if (str == null) {
                        System.out.println("客户端已无法读取数据");
                        ClientHandler.this.exitBySelf();
                        break;
                    }
                    //打印到屏幕,并回送数据长度
                    System.out.println("接收到消息(" + socket.getInetAddress() + ":" + socket.getPort() + "):" + str);

                } while (!done);
            } catch (IOException e) {
                if (!done) {
                    System.out.println("连接异常断开");
                    ClientHandler.this.exitBySelf();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                 //连接关闭
                CloseUtils.close(inputStream);
            }
            //System.out.println("客户端已退出:" + socket.getInetAddress() + ":" + socket.getPort());
        }

    }

}
