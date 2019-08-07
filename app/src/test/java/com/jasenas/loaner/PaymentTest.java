package com.jasenas.loaner;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void constructor1() {
        final Payment payment = new Payment("test", 200);

        assertEquals("test", payment.name);
        assertEquals(200, payment.amount);
        assertFalse(payment.isPaid);
    }

    @Test
    public void constructor2() {
        final Payment payment = new Payment("test", 200, true);

        assertEquals("test", payment.name);
        assertEquals(200, payment.amount);
        assertTrue(payment.isPaid);
    }

    @Test
    public void toggleStatus() {
        final Payment payment = new Payment("test" , 1, false);

        payment.toggleStatus();

        assertTrue(payment.isPaid);
    }
}