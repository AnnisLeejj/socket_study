package udp;

import java.io.IOException;
import java.net.*;

public class UDPSearcher {
    public static void main(String[] args) throws IOException {
        System.out.println("UDPSearcher_2 Started");
        //作为搜索方,让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();
        //构建一份请求数据
        String requestData = "Hello world!";
        byte[] requestDataBytes = requestData.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);
        requestPacket.setAddress(InetAddress.getLocalHost());
        requestPacket.setPort(20000);
        ds.send(requestPacket);


        //构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        //接收
        ds.receive(receivePack);
        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDPSearcher_2 receive from ip:" + ip + "\tport:" + port + "\tdata:" + data);


        //完成
        System.out.println("UDPSearcher_2 Finished.");
        ds.close();
    }
}
