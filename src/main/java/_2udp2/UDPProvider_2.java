package _2udp2;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

public class UDPProvider_2 {
    public static void main(String[] args) throws IOException {
        String SN = UUID.randomUUID().toString();

        Provider provider = new Provider(SN);
        provider.start();

        System.in.read();
        provider.exit();
    }

    public static class Provider extends Thread {
        String SN;
        boolean done = false;
        DatagramSocket ds;

        public Provider(String SN) {
            this.SN = SN;
        }

        @Override
        public synchronized void start() {
            System.out.println("UDPProvider_2 Started");
            try {
                //监听20000端口
                ds = new DatagramSocket(20000);
                while (!done) {

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

                    //解析回送的端口号
                    int responsePort = MessageCreator.parsePort(data);
                    if (responsePort != -1) {
                        //构建回送数据
                        String responseData = MessageCreator.buildWithSn(SN);
                        byte[] responseDataBytes = responseData.getBytes();
                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes,
                                responseDataBytes.length, receivePack.getAddress(), responsePort);
                        ds.send(responsePacket);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
            //完成
            System.out.println("UDPProvider_1 Finished. ");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        public void exit() {
            done = true;
            close();
        }
    }
}
