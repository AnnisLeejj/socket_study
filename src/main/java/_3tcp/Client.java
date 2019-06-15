package _3tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client {
    private static final int PORT = 20000;
    private static final int LOCAL_PORT = 20001;

    public static void main(String[] args) throws IOException {
        Socket socket = createSocket();
        initSocket(socket);

        //连接本地 20000端口,超时时间3秒,超过则抛出超时异常
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 3000);

        System.out.println("已发起服务器连接,并进入后续超时异常");
        System.out.println("客户端信息:" + socket.getLocalAddress() + ":" + socket.getLocalPort());
        System.out.println("服务端信息:" + socket.getInetAddress() + ":" + socket.getPort());
        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭!");
        }
        //释放资源
        socket.close();
        System.out.println("客户端已退出!");
    }

    static Socket createSocket() throws IOException {
        /*
        //无代理模式,等效于空构造函数
        Socket socket = new Socket(Proxy.NO_PROXY);

        //新建一份具有HTTP代理的套接字,传输数据将通过www.badu.com:8800端口转发
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(Inet4Address.getByName("www.baidu.com"), 8800));
        socket = new Socket(proxy);

        //新建一个套接字,并且直接连接到本地20000的服务器上
        socket = new Socket("localhost",PORT);

        //新建一个套接字,并且直接连接到本地20000的服务器上
        socket = new Socket(Inet4Address.getLocalHost(),PORT);

        //新建一个套接字,并且直接连接到本地20000的服务器上,并且绑定大本地20001端口上
        socket = new Socket("localhost",PORT,Inet4Address.getLocalHost(),LOCAL_PORT);
        socket = new Socket(Inet4Address.getLocalHost(),PORT,Inet4Address.getLocalHost(),LOCAL_PORT);
         */
        Socket socket = new Socket();
        socket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), LOCAL_PORT));
        return socket;
    }

    static void initSocket(Socket socket) throws SocketException {
        //设置读取超时时间
        socket.setSoTimeout(3000);

        //是否复用未完全关闭的Socket地址,对于指定bind操作后的套接字有效
        socket.setReuseAddress(true);

        //是否开启Nagel算法
        socket.setTcpNoDelay(false);

        //是否需要在长时间无数据响应时发送确认数据(类似心跳包),时间大约为2小时
        socket.setKeepAlive(true);

        //对于close 关闭操作行为进行怎样的处理;默认为false,0
        //false,0   :默认情况,关闭时立即返回,底层系统接管输出流,将缓冲区内的数据发送完成
        //true,0    :关闭时立即返回,缓冲区数据抛弃,知己发送RST结束命令道对方,并无需经过2MSL等待
        //true,200  :关闭时最长阻塞200毫秒,随后按第二情况处理
        socket.setSoLinger(true, 100);

        //是否让紧急数据内敛,默认false;紧急数据通过socket.senUrgentData(1);发送
        socket.setOOBInline(true);

        //设置接受发送缓冲大小
        socket.setReceiveBufferSize(64 * 1024 * 1024);
        socket.setSendBufferSize(64 * 1024 * 1024);

        //设置性能参数: 短链接,延迟 带宽的相对重要性
        //这里设置的权重
        socket.setPerformancePreferences(1,2,3);
    }

    static void todo(Socket client) throws IOException {
        //得到Socket输出流
        OutputStream outputStream = client.getOutputStream();

        //得到Socket输入流
        InputStream inputStream = client.getInputStream();
        byte[] buffer = new byte[128];
        //构建

        //发送到服务器
        outputStream.write(new byte[]{'a', 'b', 'c'});

        //接受服务器返回
        int read = inputStream.read(buffer);
        if (read > 0) {
            System.out.println("收到数量:" + read + " 数据:" + new String(buffer, 0, read));
        } else {
            System.out.println("没有收到:" + read);
        }

        //资源释放
        outputStream.close();
        inputStream.close();
    }
}
