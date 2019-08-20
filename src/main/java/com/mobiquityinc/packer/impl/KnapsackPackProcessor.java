package com.mobiquityinc.packer.impl;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Pack;
import com.mobiquityinc.packer.api.PackProcessor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


public class KnapsackPackProcessor implements PackProcessor {

    @Override
    public Pack process(List<Item> availableItems, Double packageWeight) {
        throw new NotImplementedException();
    }
}
