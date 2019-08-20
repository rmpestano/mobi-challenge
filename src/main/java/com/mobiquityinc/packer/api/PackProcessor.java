package com.mobiquityinc.packer.api;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Pack;

import java.util.List;

public interface PackProcessor {


    /**
     * Finds best combination of items to create a package
     *
     * @param availableItems possible items to include in the package
     * @param packageWeight supported weight
     * @return package with best combinations of items
     */
   Pack process(List<Item> availableItems, Double packageWeight);
}
