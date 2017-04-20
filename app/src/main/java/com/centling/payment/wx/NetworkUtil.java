package com.centling.payment.wx;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * NetworkUtil
 * Created by fionera on 16-2-24.
 */

public class NetworkUtil {

    public static String getIP() {
        String ip = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress local = ips.nextElement();
                    if (!local.isLoopbackAddress()) {
                        ip = local.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
}
