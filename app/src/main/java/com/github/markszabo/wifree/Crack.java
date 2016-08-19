package com.github.markszabo.wifree;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crack {
    private String SSID;
    private String BSSID;

    public Crack(String SSID, String BSSID){
        this.SSID = SSID;
        this.BSSID = BSSID;
    }

    public int getVal(byte v0) {
        return ((((v0 << 1) + v0) << 3)+ v0) >> 6;
    }

    public int getVal(String v0) {
        return getVal(v0.getBytes()[0]);
    }

    public String[] genSSID_PSK(String mac, int serial) {
        String text = mac + "-" + String.valueOf(serial);

        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] m = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            m = md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String ssid = String.format("%02d%02d%02d",getVal(m[0]),getVal(m[1]),getVal(m[2]));

        String[] ret = new String[]{ssid, ""};
        return ret;
    }

}
