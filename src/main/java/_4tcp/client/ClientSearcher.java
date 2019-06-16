package _4tcp.client;

import _4tcp.bean.ServerInfo;
import _4tcp.constants.UDPConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientSearcher {
    private static final int LISTEN_PORT = UDPConstants.PORT_CLIENT_RESPONSE;

    public static ServerInfo searchServer(int timeout) {
        System.out.println("UDPSearchServer Started.");

        //成功收到回送的栅格
        CountDownLatch recerveLatch = new CountDownLatch(1);
        Listener listener = null;
        try {
            listener = listen(recerveLatch);
            sendBroadcast();
            recerveLatch.await(timeout, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
        }
        //完成
        System.out.println("UDPSearched Finished.");
        if (listener == null) {
            return null;
        }
        List<ServerInfo> devices = listener.getServerAndClose();
        if (devices.size() > 0) {
            return devices.get(0);
        }
        return null;
    }

    private static Listener listen(CountDownLatch recerveLatch) {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch startDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, startDownLatch, recerveLatch);
        listener.start();
        return listener;
    }

    private static void sendBroadcast() {

    }

    private static class Listener extends Thread {
        private final int listenPort;
        private final CountDownLatch startDownLatch;
        private final CountDownLatch receiveDownLatch;

        private final List<ServerInfo> serverInfoList = new ArrayList<>();
        private final byte[] buffer = new byte[128];
        private final int minLen = UDPConstants.HEADER.length + 2 + 4;
        private boolean done = false;
        private DatagramSocket ds = null;


        private Listener(int listenPort, CountDownLatch startDownLatch, CountDownLatch receiveDownLatch) {
            this.listenPort = listenPort;
            this.startDownLatch = startDownLatch;
            this.receiveDownLatch = receiveDownLatch;
        }

        @Override
        public void run() {
            //通知已启动
            startDownLatch.countDown();
            try {
                //监听回送端口
                ds = new DatagramSocket(listenPort);
                DatagramPacket receivePack = new DatagramPacket(buffer, buffer.length);
                //构建接收实体
                while (!done) {
                    //接收
                    ds.receive(receivePack);
                    //打印接收到的信息
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
