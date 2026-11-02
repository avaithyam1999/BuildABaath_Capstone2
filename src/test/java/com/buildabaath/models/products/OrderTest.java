package com.buildabaath.models.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private static final double DELTA = 0.001;
    private Order order;

    @BeforeEach
    void setUp() {
        // Subtotal: large drink (4.20) + side (1.50) = 5.70
        order = new Order();
        order.addItem(new Drink("Mango Lassi", "Large"));
        order.addItem(new Side("Papadum"));
        order.updateTotalPrice();
    }

    @Test
    void updateTotalPrice_sumsAllItems() {
        assertEquals(5.70, order.getTotalPrice(), DELTA);
    }

    @Test
    void setTipPercentage_chargesPercentOfSubtotal_notFlatDollars() {
        order.setTipPercentage(20);

        assertEquals(20, order.getTipPercentage(), DELTA);
        assertEquals(1.14, order.getTipAmount(), DELTA);   // 20% of 5.70, not $20
        assertEquals(6.84, order.getFinalTotal(), DELTA);
    }

    @Test
    void setTipPercentage_zeroMeansNoTip() {
        order.setTipPercentage(0);

        assertEquals(0, order.getTipAmount(), DELTA);
        assertEquals(order.getTotalPrice(), order.getFinalTotal(), DELTA);
    }

    @Test
    void setTipPercentage_rejectsNegativeOrOver100() {
        assertThrows(IllegalArgumentException.class, () -> order.setTipPercentage(-5));
        assertThrows(IllegalArgumentException.class, () -> order.setTipPercentage(150));
    }

    @Test
    void drink_pricesBySize() {
        assertEquals(3.00, new Drink("Chai", "Small").calculatePrice(), DELTA);
        assertEquals(3.75, new Drink("Chai", "Medium").calculatePrice(), DELTA);
        assertEquals(4.20, new Drink("Chai", "Large").calculatePrice(), DELTA);
    }
}
