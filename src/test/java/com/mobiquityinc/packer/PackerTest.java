package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.fail;

public class PackerTest {

    private static final String NEW_LINE = System.lineSeparator();

    @Test
    public void shouldPackPackagesInSampleFile() throws APIException {
        String pack = Packer.pack(getClass().getResource("/input.txt").getPath());
        assertThat(pack)
                .isEqualTo("4" + NEW_LINE +
                        "-" + NEW_LINE +
                        "2,7" + NEW_LINE +
                        "8,9" + NEW_LINE +
                        "8,9,10,12,13,14,15" + NEW_LINE);
    }

    @Test
    public void shouldNotPackEmptyItems() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-empty-package.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("No items found to read. (Last processed line: 2)");
        }

    }

    @Test
    public void shouldNotPackItemsInInvalidFormat() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-invalid-package.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid item format. Items must have 'index', 'weight' and 'cost'. (Last processed line: 2)");
        }
    }

    @Test
    public void shouldNotPackItemsWithInvalidPackageWeightFormat() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-invalid-package-weight-format.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid package weight. (Last processed line: 2)");
        }
    }

    @Test
    public void shouldNotPackItemsWithInvalidMaxPackageWeight() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-invalid-max-package-weight.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid package weight 101.0. Package max weight must not be greater than 100. (Last processed line: 1)");
        }
    }

    @Test
    public void shouldNotPackItemsWithoutWeight() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-without-package-weight.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Package weight not found. (Last processed line: 2)");
        }
    }

    @Test
    public void shouldNotPackItemsWithInvalidMonetaryCurrency() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-invalid-item-currency.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Item cost must have monetary currency. (Last processed line: 1)");
        }
    }

    @Test
    public void shouldNotPackTooManyItems() {
        try {
            String pack = Packer.pack(getClass().getResource("/file-with-too-many-packages.txt").getPath());
            fail("Should not pack items but returned the following pack: " + pack);
        } catch (APIException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid number of items, 16. Maximum number of items in package is 15. (Last processed line: 1)");
        }
    }



}
