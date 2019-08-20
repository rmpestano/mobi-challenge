package com.mobiquityinc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Pack {

    private final Double supportedWeight;
    private final List<Item> items;
    private final Double itemsCost;
    private final Double itemsWeight;

    public Pack(List<Item> items, Double supportedWeight) {
        this.supportedWeight = supportedWeight;
        this.items = Collections.unmodifiableList(items == null ? new ArrayList<>() : items);
        this.itemsCost = calculateItemsCost();
        this.itemsWeight = calculateItemsWeight();
    }

    private Double calculateItemsCost() {
        if(items == null || items.isEmpty()) {
            return 0d;
        }
        return items.stream()
                .mapToDouble(Item::getCost)
                .sum();
    }

     private Double calculateItemsWeight() {
        if(items == null || items.isEmpty()) {
            return 0d;
        }
        return items.stream()
                .mapToDouble(Item::getWeight)
                .sum();
    }

    public String toString() {
        if(items.isEmpty()) {
            return "-";
        }
        StringBuilder packageDescription = new StringBuilder();
        List<Item> sortedItems = new ArrayList<>(items);//need to copy because item list is immutable
        sortedItems.sort(Comparator.comparingInt(Item::getIndex));
        for (Item item : sortedItems) {
            packageDescription.append(item.getIndex()).append(",");
        }
        int indexOfLastComma = packageDescription.lastIndexOf(",");
        if(indexOfLastComma != -1) {
            packageDescription.deleteCharAt(indexOfLastComma);
        }
        return packageDescription.toString();
    }

    public Double getItemsCost() {
        return itemsCost;
    }

    public Double getItemsWeight() {
        return itemsWeight;
    }
}
