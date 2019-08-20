package com.mobiquityinc.model;

import com.mobiquityinc.packer.PackParser;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PackTest {


    @Test
    public void shouldPrintPackageItems() {
        String line = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";

        List<Item> items = PackParser.parseItems(line);
        Double weight = PackParser.parsePackageWeight(line);

        Pack pack = new Pack(items, weight);

        assertThat(pack.toString()).isEqualTo("1,3,4,5,6");

    }

    @Test
    public void shouldGetItemsCost() {
        String line = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";

        List<Item> items = PackParser.parseItems(line);
        Double weight = PackParser.parsePackageWeight(line);

        Pack pack = new Pack(items, weight);

        assertThat(pack.getItemsCost()).isEqualTo(181d); //note that item with cost €98 is ignored (does not count) because it weights more than the package weight
    }


}
