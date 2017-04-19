package com.centling.util;

/**
 * ByteUtil
 * Created by fionera on 17-4-11 in sweeping_robot.
 */

@SuppressWarnings("unused")
public class ByteUtil {

    public static byte[] int2Bytes(int num) {
        byte[] result = new byte[4];
        result[3] = (byte) ((num >>> 24) & 0xff);
        result[2] = (byte) ((num >>> 16) & 0xff);
        result[1] = (byte) ((num >>> 8) & 0xff);
        result[0] = (byte) ((num) & 0xff);
        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static byte[] hexStr2Bytes(String src) {
        int m, n;
        int l = src.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            result[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return result;
    }
}
