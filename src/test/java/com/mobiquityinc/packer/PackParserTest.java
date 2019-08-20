package com.mobiquityinc.packer;

import com.mobiquityinc.model.Item;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.Assert.fail;

public class PackParserTest {


    @Test
    public void shouldReadItemsFromLine() {
        String line = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";

        List<Item> items = PackParser.parseItems(line);

        assertThat(items).isNotNull()
                .hasSize(5) //only 5 because item 2 has more weight than the package supports (88.63) hence it is ignored
                .extracting("index", "weight", "cost")
                .contains(
                        tuple(1, 53.38, 45d),
                        tuple(3, 78.48, 3d),
                        tuple(4, 72.30, 76d),
                        tuple(5, 30.18, 9d),
                        tuple(6, 46.34, 48d)
                )
                .doesNotContain(tuple(2, 88.62, 98));
    }

    @Test
    public void shouldNotReadItemsFromEmptyLine() {
        String line = " ";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because line is empty.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("No items found to read.");
        }
    }

    @Test
    public void shouldNotReadItemsWhenPackageWeightIsGreaterThanAllowed() {
        String line = "101 : (1,53.38,€45)";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because package weight is invalid.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Invalid package weight 101.0. Package max weight must not be greater than 100.");
        }
    }

    @Test
    public void shouldNotReadItemsWhenNumberOfItemsIsGreaterThanAllowed() {
        String line = "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78) " +
                "(10,76.25,€75) (11,60.02,€74) (12,93.18,€35) (13,89.95,€78) (14,76.25,€75) (15,60.02,€74) (16,93.18,€35) (17,89.95,€78)";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because number of packages is greater than allowed.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Invalid number of items, 17. Maximum number of items in package is 15.");
        }
    }

    @Test
    public void shouldNotReadItemsWithEmptyPackageWeight() {
        String line = "(14,76.25,€75) (15,60.02,€74) (16,93.18,€35) (17,89.95,€78)";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because package weight is missing.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Package weight not found.");
        }
    }

    @Test
    public void shouldNotReadItemsWithInvalidPackageWeight() {
        String line = "75a : (14,76.25,€75) (15,60.02,€74) (16,93.18,€35) (17,89.95,€78)";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because invalid package weight.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Invalid package weight.");
        }
    }

    @Test
    public void shouldNotReadItemsWithMissingMonetaryCurrency() {
        String line = "75 : (14,76.25,75)";
        try {
            PackParser.parseItems(line);
            fail("should not reach here because missing item monetary currency.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Item cost must have monetary currency.");
        }
    }

}
