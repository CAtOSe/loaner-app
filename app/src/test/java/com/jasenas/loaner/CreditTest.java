package com.jasenas.loaner;

import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CreditTest {
    private  int creditId = 0;
    private String creditName = "Credit";
    private Calendar creditDate = new GregorianCalendar(2019, 8, 2);
    private int creditAmount = 10;

    @Test
    public void constructSimple() {
        final Credit credit = new Credit(this.creditId, this.creditName, this.creditDate, this.creditAmount);

        assertEquals(this.creditId, credit.id);
        assertEquals(this.creditName, credit.name);
        assertEquals(this.creditDate, credit.date);
        assertEquals(this.creditAmount, credit.amount);

        assertFalse(credit.isMonthly);

        List<Payment> expected = Collections.emptyList();
        assertThat(credit.payments, is(expected));
        assertThat(credit.payments.size(), is(expected.size()));
    }

    @Test
    public void constructMonthlySimple() {
        final Credit credit = new Credit(this.creditId, this.creditName, this.creditDate, this.creditAmount, true);

        assertEquals(this.creditId, credit.id);
        assertEquals(this.creditName, credit.name);
        assertEquals(this.creditDate, credit.date);
        assertEquals(this.creditAmount, credit.amount);

        assertTrue(credit.isMonthly);

        List<Payment> expected = Collections.emptyList();
        assertThat(credit.payments, is(expected));
        assertThat(credit.payments.size(), is(expected.size()));
    }

    @Test
    public void constructWithPayments() {

        final Payment payment = new Payment("Payer", this.creditAmount);
        final List<Payment> payments = Collections.singletonList(payment);

        final Credit credit = new Credit(this.creditId, this.creditName, this.creditDate, this.creditAmount, payments);

        assertEquals(this.creditId, credit.id);
        assertEquals(this.creditName, credit.name);
        assertEquals(this.creditDate, credit.date);
        assertEquals(this.creditAmount, credit.amount);

        assertFalse(credit.isMonthly);

        assertThat(credit.payments, is(payments));
        assertThat(credit.payments.size(), is(payments.size()));
    }

    @Test
    public void constructMonthlyWithPayments() {

        final Payment payment = new Payment("Payer", this.creditAmount);
        final List<Payment> payments = Collections.singletonList(payment);

        final Credit credit = new Credit(this.creditId, this.creditName, this.creditDate, this.creditAmount, payments, true);

        assertEquals(this.creditId, credit.id);
        assertEquals(this.creditName, credit.name);
        assertEquals(this.creditDate, credit.date);
        assertEquals(this.creditAmount, credit.amount);

        assertTrue(credit.isMonthly);

        assertThat(credit.payments, is(payments));
        assertThat(credit.payments.size(), is(payments.size()));
    }


}