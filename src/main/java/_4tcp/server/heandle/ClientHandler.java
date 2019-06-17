package _4tcp.server.heandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 客户端消息处理
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private boolean flag = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("新客户端连接:" + socket.getInetAddress() + ":" + socket.getPort());
        try {
            //得到打印流,用于数据输出;服务器回送数据使用
            PrintStream socketOutPut = new PrintStream(socket.getOutputStream());
            //得到输入流,用于接收数据
            BufferedReader socketInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            do {
                Thread.sleep(500);
                //客户端拿到一条数据
                String str = socketInput.readLine();
                if ("bye".equalsIgnoreCase(str)) {
                    flag = false;
                    //回送
                    socketOutPut.println("bye");
                } else {
                    //打印到屏幕,并回送数据长度
                    System.out.println("接收到消息(" + socket.getInetAddress() + ":" + socket.getPort() + "):" + str);
                    socketOutPut.println("回送:" + (str == null ? "空消息" : str.length()));
                }
            } while (flag);

            socketOutPut.close();
            socketInput.close();
        } catch (IOException e) {
            System.out.println("连接异常断开");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("客户端已退出:" + socket.getInetAddress() + ":" + socket.getPort());
    }

    public void send(String message) {


    }
    public void exit(){

    }
}
