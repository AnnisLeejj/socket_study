import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);

        //链接本地,端口2000 , 超时时间3000ms
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2000), 3000);

        System.out.println("已发起服务器链接,并进入后续流程~~~");
        System.out.println("客户端信息:" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
        System.out.println("服务器信息:" + socket.getInetAddress() + " P:" + socket.getPort());
        try {
            //发送接收数据
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }
//        释放资源
        socket.close();
        System.out.println("客户端已退出");
    }

    private static void todo(Socket client) throws IOException {
        //构建键盘输入流
        InputStream in = System.in;
        BufferedReader input  = new BufferedReader(new InputStreamReader(in));

        //得到Socket输出流,并转换为打印流
        InputStream inputStream = client.getInputStream();

    }
}
