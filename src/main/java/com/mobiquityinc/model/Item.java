package com.mobiquityinc.model;

import java.util.Objects;

import static java.lang.String.format;

/**
 * represents a package item
 */
public class Item {

    private static final double MAX_ITEM_WEIGHT = 100;
    private static final double MAX_ITEM_cost = 100;

    private final Integer index;
    private final Double weight;
    private final Double cost;
    private final Double ratio; // cost/weight ratio, the higher the better (items with less weight and higher cost have more probability to be in the package)


    public Item(Integer index, Double weight, Double cost) {
        this.index = index = Objects.requireNonNull(index, "Provide item index");
        this.weight = Objects.requireNonNull(weight, "Provide item weight");
        this.cost = Objects.requireNonNull(cost, "Provide item cost");

        this.validate(index, weight, cost);

        if (weight <= 0) { //avoid division by zero and set a high ratio because an item with zero weight should be added to the package
            ratio = 100d;
        } else {
            this.ratio = cost / weight;
        }
    }

    private void validate(int index, double weight, double cost) {
        if (index < 0) {
            throw new IllegalArgumentException(format("Invalid item index %s. Index must be a positive number.", index));
        }
        if (weight > MAX_ITEM_WEIGHT) {
            throw new IllegalArgumentException(format("Invalid item weight %s. Item should not weight more than %s.", weight, MAX_ITEM_WEIGHT));
        }
        if (cost > MAX_ITEM_cost) {
            throw new IllegalArgumentException(format("Invalid item cost %s. Item should not cost more than %s.", cost, MAX_ITEM_cost));
        }
    }

    public Integer getIndex() {
        return index;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getCost() {
        return cost;
    }

    public Double getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "("+index+","+weight+",€"+cost+")";
    }
}
