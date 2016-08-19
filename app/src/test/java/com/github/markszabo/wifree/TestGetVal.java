package com.github.markszabo.wifree;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestGetVal {
    @Test
    public void test_a() throws Exception {
        Crack c = new Crack("","");
        assertEquals(37, c.getVal("a"));
    }
    @Test
    public void test_b() throws Exception {
        Crack c = new Crack("","");
        assertEquals(38, c.getVal("b"));
    }
    @Test
    public void test_A() throws Exception {
        Crack c = new Crack("","");
        assertEquals(25, c.getVal("A"));
    }
    @Test
    public void test_F() throws Exception {
        Crack c = new Crack("","");
        assertEquals(27, c.getVal("F"));
    }
    @Test
    public void test_0() throws Exception {
        Crack c = new Crack("","");
        assertEquals(18, c.getVal("0"));
    }
    @Test
    public void test_6() throws Exception {
        Crack c = new Crack("","");
        assertEquals(21, c.getVal("6"));
    }
}