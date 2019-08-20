package com.mobiquityinc.packer;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Pack;
import com.mobiquityinc.packer.impl.DefaultPackProcessor;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.extractor.Extractors.toStringMethod;

public class PackProcessorTest {

    @Test
    public void shouldFindBestPackageUsingBruteForceProcessor() {
        String line = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";

        List<Item> items = PackParser.parseItems(line);

        Double weight = PackParser.parsePackageWeight(line);

        Pack packageCreated = new DefaultPackProcessor()
                .process(items, weight);

        assertThat(packageCreated)
                .isNotNull()
                .extracting(toStringMethod())
                .isEqualTo("4");

    }

    @Test
    public void shouldNotFindBestPackageUsingBruteForceProcessor() {
        String line = "8 : (1,15.3,€34)";

        List<Item> items = PackParser.parseItems(line);

        Double weight = PackParser.parsePackageWeight(line);

        Pack packageCreated = new DefaultPackProcessor()
                .process(items, weight);

        assertThat(packageCreated)
                .isNotNull()
                .extracting(toStringMethod())
                .isEqualTo("-");

    }

    @Test
    public void shouldFindBestPackageInSample2() {
        String line = "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)\n" +
                "(6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)";

        List<Item> items = PackParser.parseItems(line);

        Double weight = PackParser.parsePackageWeight(line);

        Pack packageCreated = new DefaultPackProcessor()
                .process(items, weight);

        assertThat(packageCreated)
                .isNotNull()
                .extracting(toStringMethod())
                .isEqualTo("2,7");

    }

    @Test
    public void shouldFindBestPackageInSample3() {
        String line = "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36)\n" +
                "(6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        List<Item> items = PackParser.parseItems(line);

        Double weight = PackParser.parsePackageWeight(line);

        Pack packageCreated = new DefaultPackProcessor()
                .process(items, weight);

        assertThat(packageCreated)
                .isNotNull()
                .extracting(toStringMethod())
                .isEqualTo("8,9");

    }

    @Test
    public void shouldFindBestPackageInLargePackage() {
        String line = "94 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36)\n" +
                "(6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64) (10,23.26,€52) (11,35.71,€58)\n" +
                "(12,5.33,€11) (13,2.76,€4) (14,12.23,€94) (15,23.26,€44)";

        List<Item> items = PackParser.parseItems(line);

        Double weight = PackParser.parsePackageWeight(line);

        Pack packageCreated = new DefaultPackProcessor()
                .process(items, weight);

        System.out.println(packageCreated.getItemsCost());
        System.out.println(packageCreated.getItemsWeight());

        assertThat(packageCreated)
                .isNotNull()
                .extracting(toStringMethod())
                .isEqualTo("8,9,10,12,13,14,15");

        assertThat(packageCreated)
                .extracting("itemsCost", "itemsWeight")
                .contains(348.0, 92.96000000000001);

    }



}
