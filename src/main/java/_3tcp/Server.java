package _3tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Server {
    private static final int PORT = 20000;

    public static void main(String[] args) throws IOException {
        ServerSocket server = createServerSocket();

        initServerSocket(server);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息" + server.getInetAddress()
                + " P:" + server.getLocalPort());

        //等到客户端链接
        for (; ; ) {
            //得到客户端
            Socket client = server.accept();
            //客户端构建异步线程
            ClientHandler clientHandeler = new ClientHandler(client);
            //启动线程
            clientHandeler.start();

        }
    }


    private static ServerSocket createServerSocket() throws IOException {
        //创建基础的ServerSocket
        ServerSocket serverSocket = new ServerSocket();

        //方法1:绑定到本地端口上
        serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 50);

        //方法2:绑定到本地端口20000上,并且设置当前可允许等待链接的队列50个
        //超过后客户端将会抛出异常
        //serverSocket = new ServerSocket(PORT,50);

        //方法3:等效于上面的方案
//        serverSocket = new ServerSocket(PORT,50,Inet4Address.getLocalHost());

        return serverSocket;
    }

    private static void initServerSocket(ServerSocket server) throws SocketException {
        //是否复用未完全关闭的地址端口
        server.setReuseAddress(true);

        //等效Socket#setReceiveBufferSize
        server.setReceiveBufferSize(64 * 1024 * 1024);

        //设置serverSocket#accept超时时间
        //server.setSoTimeout(2000);

        //设置性能参数: 短链接,延迟 带宽的相对重要性
        //这里设置的权重
        server.setPerformancePreferences(1, 2, 3);
    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private Socket socket;

        private ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("新客户端连接:" + socket.getInetAddress() + ":" + socket.getPort());
            try {
                //得到套接字流
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                byte[] buffer = new byte[128];
                int readCount = inputStream.read(buffer);
                if (readCount > 0) {
                    System.out.println("收到数量:" + readCount + " 数据:" + new String(buffer, 0, readCount));
                    //回送数据
                    outputStream.write(buffer, 0, readCount);
                } else {
                    System.out.println("没有收到:" + readCount);
                    //回送数据
                    outputStream.write(new byte[]{0});
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                System.out.println("连接异常断开:" + socket.getInetAddress() + ":" + socket.getPort());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("已关闭客户端:" + socket.getInetAddress() + ":" + socket.getPort());
        }
    }
}
