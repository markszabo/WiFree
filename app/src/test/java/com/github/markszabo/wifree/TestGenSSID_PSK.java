package com.github.markszabo.wifree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGenSSID_PSK {
    @Test
    public void test_SSID_1() throws Exception {
        Crack c = new Crack("", "");
        assertEquals("121223", c.genSSID_PSK("00:00:00:00:00:00", 10)[0]);
    }

    @Test
    public void test_SSID_2() throws Exception {
        Crack c = new Crack("", "");
        assertEquals("063421", c.genSSID_PSK("45:B5:A3:94:48:FF", 1000456)[0]);
    }

    @Test
    public void test_SSID_3() throws Exception {
        Crack c = new Crack("","");
        assertEquals("757970", c.genSSID_PSK("45:65:A6:44:A8:FF",1089632)[0]);
    }
}