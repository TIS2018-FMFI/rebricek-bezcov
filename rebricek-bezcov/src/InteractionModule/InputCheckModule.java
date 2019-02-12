package InteractionModule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class InputCheckModule {

    public static String checkInputAddress(String inputAddress) {
        // metoda vracia spravnu variantu vstupnej adresy - minimalne pridanu o koncovku (ak nebola zadana).
        // takisto sa pouziva pri volbe varianty A, ak je zadana adresa chybna.
        if (inputAddress.length() >= 4) {
            if (!inputAddress.substring(inputAddress.length() - 4, inputAddress.length()).equals(".xml")) {
                inputAddress += ".xml";
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputAddress));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return null;
        }
        return inputAddress;
    }
}