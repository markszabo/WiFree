package com.github.markszabo.wifree;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestCrack {
    @Test
    public void test_Crack_1() throws Exception {
        Crack c = new Crack("538420", "e4:48:c7:88:7f:58");
        assertEquals("AADENWIB", c.getPSK(200324188));
    }

    @Test
    public void test_Crack_2() throws Exception {
        Crack c = new Crack("538420", "e4:48:c7:88:7f:58");
        assertEquals("QFMNTAOQ", c.getPSK(201461780));
    }

    @Test
    public void test_Crack_3() throws Exception {
        Crack c = new Crack("538420", "e4:48:c7:88:7f:58");
        assertEquals("KEFWMZIT", c.getPSK(204455244));
    }

    @Test
    public void test_Crack_4() throws Exception {
        Crack c = new Crack("538420", "e4:48:c7:88:7f:58");
        assertNull(c.getPSK(204455245));
    }

}
