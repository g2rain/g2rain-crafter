package com.g2rain.crafter.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Constants常量类的测试类
 */
public class ConstantsTest {

    @Test
    void testConstantsValues() {
        // 测试所有常量值
        assertEquals("=========================================", Constants.HORIZONTAL_LINE);
        assertEquals("%-15s: %s", Constants.LOG_FORMAT);
        assertEquals("g2rain-example", Constants.TEMPLATE_BASE);
    }
}