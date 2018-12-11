package InputFileHandlingModule;
/**
 * @author TomasTakacs
 */



import DataVerificationModule.InputFileVerificator;
import InputFileClasses.ResultList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputFileHandler {


    public static boolean addInputFile(String fileAddress){
        ResultList resultList = InputFileHandler.unmarshalInputFile(fileAddress);

        // daj skontrolovat InputFIleVerificator-u

        // Configuration config = ConfigurationModule.getConfiguration();
        // String season = config.getSeason()

        String season = "2018";         // placeholder, dummy value
        String fileStorage = "Data";   // placeholder, dummy value

        String newFileName = "kolo"+resultList.getEvent().getRank() + ".xml";

        String savePath = fileStorage + '/'+season + '/' + newFileName;

        boolean success = InputFileHandler.marshalInputFile(resultList, savePath);

        // if (success == false) -> message?

        // spusti dalsi MODUL

        return success;
    }


    public static List<ResultList> loadInputFiles(){
        // Configuration config = ConfigurationModule.getConfiguration();
        // get fileStorage, season
        // get all files in [fileStorage]/[season]/

        List<String> folderContent = List.of("a","b","...");  // by OS
        List<ResultList> loaded = new ArrayList<>();

        for (String fileAddress : folderContent){
            ResultList resultList = InputFileHandler.unmarshalInputFile(fileAddress);
            ResultList verifiedResultList = InputFileVerificator.verifyInputFile(resultList);
            loaded.add(verifiedResultList);
        }


        // spusti dalsi MODUL

        return loaded;
    }


    private static ResultList unmarshalInputFile(String sourceFileAddress){

        ResultList resultList = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(ResultList.class);

            File sourceFile = new File(sourceFileAddress);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            resultList = (ResultList) unmarshaller.unmarshal(sourceFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private static boolean marshalInputFile(ResultList resultList, String targetFileAdress){
        boolean success = false;
        try {
            JAXBContext jc = JAXBContext.newInstance(ResultList.class);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File targetFile = new File(targetFileAdress);
            marshaller.marshal(resultList, targetFile);

            success = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return success;
    }
}
