package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Pack;
import com.mobiquityinc.packer.impl.DefaultPackProcessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Packer {

    private static Logger LOGGER = Logger.getLogger(Packer.class.getName());
    private static final String NEW_LINE = System.lineSeparator();

    private Packer() {
    }

    /**
     * Given a file containing a list of items and a package weight on each line
     * calculates the best package (higher cost) for each set of items
     *
     * @param filePath path to a file containing the packages
     * @return A string repreentation containg the indexes of the items that should be in the package for each line
     * @throws APIException if max weight that a package can take is greater than 100
     * @throws APIException if there are more than 15 items in a package
     * @throws APIException if max weight and cost of an item is greater than 100
     * @throws APIException if a package has no items
     * @throws APIException if there are items in invalid format '(index,weight,€cost)'
     */
    public static String pack(String filePath) throws APIException {

        Integer lineNumber = 1;
        try {
            StringBuilder packagesOutput = new StringBuilder();
            for (String line : Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"))) {
                Pack pack = createPackage(line);
                packagesOutput.append(pack.toString()).append(NEW_LINE);
                lineNumber ++;
            }
            return packagesOutput.toString();
        } catch (RuntimeException | IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage() + " (Last processed line: "+lineNumber+")", e);
            throw new APIException(e.getMessage() + " (Last processed line: "+lineNumber+")", e);
        }
    }


    /**
     * creates a package based on provided items
     *
     * @param line represents items that may go inside the package
     * @return a package containing items that do not overweight the package having the highest cost sum
     */
    private static Pack createPackage(String line) {
        List<Item> availableItems = PackParser.parseItems(line);
        Double supportedPackageWeight = PackParser.parsePackageWeight(line);
        Pack pack = new DefaultPackProcessor().process(availableItems, supportedPackageWeight);
        return pack;
    }

}
