package udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPProvider_1 {
    public static void main(String[] args) throws IOException {
        System.out.println("UDPProvider_1 Started");
        //作为接受者,指定端口用于数据接收
        DatagramSocket ds = new DatagramSocket(20000);

        startWait(ds);
    }

    public static int count = 0;

    static void startWait(DatagramSocket ds) throws IOException {
        //构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        //接收
        ds.receive(receivePack);

        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDPProvider_1 receive from ip:" + ip + "\tport:" + port + "\tdata:" + data);

        //构建回送数据
        String responseData = "Receive data length:" + dataLen;
        byte[] responceDataBytes = responseData.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responceDataBytes,
                responceDataBytes.length, receivePack.getAddress(), receivePack.getPort());
        ds.send(responsePacket);
        //完成
        System.out.println("UDPProvider_1 Finished. " + (count++));
//        ds.close();
        startWait(ds);
    }
}
