package com.mobiquityinc.packer.impl;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Pack;
import com.mobiquityinc.packer.api.PackProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Process package items and find 'best package' by permuting all items in the array
 * and trying to put them in the resulting package.
 *
 * The permutation is done first with the items with highest 'ration' (cost/weight)
 * So we choice an item to be fixed and then combine it with all other items until the package is fulfilled or there are no more items to combine
 */
public class DefaultPackProcessor implements PackProcessor {

    @Override
    public Pack process(List<Item> availableItems, Double packageWeight) {

        Pack bestPack = new Pack(new ArrayList<>(), packageWeight);

        availableItems.sort((item, other) -> other.getRatio().compareTo(item.getRatio())); //order items by best ratio (cost/weight) descending
        for (int i = 0; i < availableItems.size(); i++) {
            List<Item> currentPermutationItems = new ArrayList<>();
            currentPermutationItems.add(availableItems.get(i));//fix an item and permute with all other items
            permute(availableItems, currentPermutationItems, 0, i, packageWeight);
            Pack candidatePack = new Pack(currentPermutationItems, packageWeight);
            if (bestPack.getItemsCost() < candidatePack.getItemsCost()) {
                bestPack = candidatePack;
            } else if (bestPack.getItemsCost().equals(candidatePack.getItemsCost()) && bestPack.getItemsWeight() > candidatePack.getItemsWeight()) {
                bestPack = candidatePack; //You would prefer to send a package which weighs less in case there is more than one package with the same price
            }
        }
        return bestPack;
    }


    /**
     *
     * @param availableItems
     * @param currentPermutation
     * @param startIndex
     * @param currentIndex index of the fixed item
     * @param maxWeight
     */
    private void permute(List<Item> availableItems, List<Item> currentPermutation, int startIndex, int currentIndex,
                         Double maxWeight) {

        if (startIndex > availableItems.size() || permutationWeight(currentPermutation).equals(maxWeight)) {
            return;//stop permutations if there is no more available items or package is already filled with maxWeight
        }

        for (int i = startIndex; i < availableItems.size(); i++) {
            if(startIndex == currentIndex) {
                continue;//skip current index because it is already in the package
            }
            Item item = availableItems.get(i);
            if (!currentPermutation.contains(item) && permutationWeight(currentPermutation) + item.getWeight() <= maxWeight) { //if current item doesn't overweight the package then add it to current permutation
                currentPermutation.add(item);
            }
            permute(availableItems, currentPermutation, i + 1, currentIndex, maxWeight);
        }
    }

    private Double permutationWeight(List<Item> permutation) {
        if (permutation == null || permutation.isEmpty()) {
            return 0d;
        }
        return permutation.stream()
                .mapToDouble(Item::getWeight)
                .sum();
    }
}
