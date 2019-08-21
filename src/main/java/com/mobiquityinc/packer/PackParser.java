package com.mobiquityinc.packer;

import com.mobiquityinc.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Parses package items and weight from a String line
 */
public class PackParser {

    private static final int MAX_PACKAGE_ITEMS = 15;
    private static final int MAX_PACKAGE_WEIGHT = 100;


    private PackParser() {

    }

    /**
     * Reads package items in a line <b>ignoring</b> items which weight more than the package supports
     *
     * @param line containing package items
     * @return list of items
     * @throws IllegalArgumentException if no items are found
     * @throws IllegalArgumentException if number of items are greater than {@value #MAX_PACKAGE_ITEMS}
     * @throws IllegalArgumentException if package weight is greater than {@value #MAX_PACKAGE_WEIGHT}
     * @throws IllegalArgumentException if there are items in invalid format: '(index,weight,€cost)'
     */
    public static List<Item> parseItems(String line) {
        if (line == null || line.isEmpty() || line.trim().isEmpty()) {
            throw new IllegalArgumentException("No items found to read.");
        }
        Double packageSupportedWeight = readSupportedWeight(line);
        if (packageSupportedWeight > MAX_PACKAGE_WEIGHT) {
            throw new IllegalArgumentException(format("Invalid package weight %s. Package max weight must not be greater than %s.", packageSupportedWeight, MAX_PACKAGE_WEIGHT));
        }
        List<String> lineItems = extractLineItems(line);
        if (lineItems == null || lineItems.isEmpty()) {
            return Collections.emptyList(); //no items found
        }

        if (lineItems.size() > MAX_PACKAGE_ITEMS) {
            throw new IllegalArgumentException(format("Invalid number of items, %s. Maximum number of items in package is %s.", lineItems.size(), MAX_PACKAGE_ITEMS));
        }
        List<Item> items = lineItems.stream() //read each line item
                .map(item -> item.split(",")) //split item attributes (index, weight and cost) inside parenteses
                .map(PackParser::createItem)
                .filter(createdItem -> createdItem.getWeight() < packageSupportedWeight) //do not add items that overweight the package
                .collect(Collectors.toList());

        return items;

    }

    /**
     * Reads the package weight from a String.
     *
     * @param line a String containing the package weight
     * @return the package weight
     */
    public static Double parsePackageWeight(String line) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("No package weight found.");
        }
        Double packageWeight = readSupportedWeight(line);
        return packageWeight;
    }

    private static List<String> extractLineItems(String line) {
        String lineWithoutPackageWeight = line.substring(line.indexOf(':') + 1);
        return Stream.of(lineWithoutPackageWeight.split("[()]"))//extract all items inside parentheses
                .filter(extractedLine -> !extractedLine.trim().isEmpty()) //the split by '[()]' leaves empty string on the resulting array so we remove them
                .collect(Collectors.toList());
    }

    private static Item createItem(String[] lineItem) {
        if (lineItem.length != 3) {
            throw new IllegalArgumentException("Invalid item format. Items must have 'index', 'weight' and 'cost'.");
        }
        try {
            Integer index = Integer.parseInt(lineItem[0]);
            Double weight = Double.parseDouble(lineItem[1]);
            Double cost = extractCost(lineItem[2]);
            return new Item(index, weight, cost);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid item attribute format.", ex);
        }
    }

    private static Double extractCost(String rawCost) {
        if (!rawCost.contains("€")) {
            throw new IllegalArgumentException("Item cost must have monetary currency.");
        }
        return Double.parseDouble(rawCost.substring(1));
    }

    private static Double readSupportedWeight(String line) {

        if (!line.contains(":")) {
            throw new IllegalArgumentException("Package weight not found.");
        }
        try {
            Double weight = Double.parseDouble(line.substring(0, line.indexOf(':')).trim());
            return weight;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Invalid package weight.");
        }

    }
}
