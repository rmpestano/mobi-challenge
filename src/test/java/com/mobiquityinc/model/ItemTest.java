package com.mobiquityinc.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ItemTest {

    private static final double MAX_WEIGHT = 100;
    private static final double MAX_COST = MAX_WEIGHT;


    @Test
    public void shouldCreateValidItem() {
        Item item = new Item(0, 1d, 1d);
        assertThat(item).isNotNull()
                .extracting("index", "weight", "cost")
                .contains(0, 1d, 1d);
    }

    @Test
    public void shouldNotCreateItemWithInvalidWeight() {
        try {
            Item item = new Item(0, (MAX_WEIGHT + 1), 1d);
            fail("should not reach here because max item weIntegeright is invalid.");
        } catch (IllegalArgumentException e) {
            assertThat(String.format("Invalid item weight %s. Item should not weight more than %s.", MAX_WEIGHT + 1, MAX_WEIGHT)).isEqualTo(e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateItemWithInvalidCost() {
        try {
            Item item = new Item(0, 1d, MAX_COST + 1);
            fail("should not reach here because max item cost is invalid.");
        } catch (IllegalArgumentException e) {
            assertThat(String.format("Invalid item cost %s. Item should not cost more than %s.", MAX_COST + 1, MAX_COST)).isEqualTo(e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateItemWithNegativeIndex() {
        try {
            Item item = new Item(-1, 1d, MAX_COST + 1);
            fail("should not reach here because item index must be positive.");
        } catch (IllegalArgumentException e) {
            assertThat(String.format("Invalid item index %s. Index must be a positive number.", -1)).isEqualTo(e.getMessage());
        }
    }

}
