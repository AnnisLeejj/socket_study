package _3tcp;

public class Tools {
    public static int byteArrayToInt(byte[] b) {
        //第一步: 在在低八位计算出自己的二进制 (通过与运算,其他无关位置 值都为0)
        //第二步: 位运算到自己的位置(不是自己位置的值为0)
        //第三步: 合成运算 (此时有值得位置不会出现重复有值)
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static void main(String[] args) {
        int a = 412356;
        byte[] bytes = intToByteArray(a);
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
        System.out.println(byteArrayToInt(bytes));
    }
}
