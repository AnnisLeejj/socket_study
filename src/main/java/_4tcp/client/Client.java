package _4tcp.client;

import _4tcp.bean.ServerInfo;

public class Client {
    public static void main(String[] args) {
        ServerInfo info = ClientSearcher.searchServer(10);
        System.out.println(info);

    }
}
