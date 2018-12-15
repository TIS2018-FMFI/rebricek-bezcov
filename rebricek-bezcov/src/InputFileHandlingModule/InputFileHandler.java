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

    public static void addInputFile(String fileAddress){
        ResultList inputResultList = InputFileHandler.unmarshalInputFile(fileAddress);

        // daj skontrolovat InputFileVerificator-u
        ResultList resultList = InputFileVerificator.verifyInputFile(inputResultList);

        //Configuration config = ConfigurationModule.getConfiguration();
        //String season = config.getSeason()
        String season = "2018";         // placeholder, dummy value

        // get FileStorage
        String fileStorage = "Data";   // placeholder, dummy value

        String newFileName = "kolo"+resultList.getEvent().getRank() + ".xml";

        String savePath = fileStorage + '/'+season + '/' + newFileName;

        boolean success = InputFileHandler.marshalInputFile(resultList, savePath);

        // if (success == false) -> message?
        // InteractionModule.printMessage(MESSAGE_MARSHALLING_FAILED)
        // nastala chyba pri ukladaní vstupného súboru


        // spusti dalsi MODUL -> nacitaj vsetky vstupne subory

        loadInputFiles();
    }


    public static void loadInputFiles(){
        // Configuration config = ConfigurationModule.getConfiguration();
        // get fileStorage, season
        // get all files in [fileStorage]/[season]/

        String season = "2018";      // placeholder, dummy value
        String fileStorage = "Data"; // placeholder, dummy value

        //listFilesForFolder(folder);

//        List<String> folderContent = List.of("a","b","...");  // by OS
//        List<ResultList> loaded = new ArrayList<>();

        List<String> inputFilePaths = listFilesForFolder(fileStorage+"/"+season);
        List<ResultList> loadedResultLists = new ArrayList<>();

        for (String fileAddress : inputFilePaths){
            ResultList resultList = InputFileHandler.unmarshalInputFile(fileAddress);
            ResultList verifiedResultList = InputFileVerificator.verifyInputFile(resultList);
            loadedResultLists.add(verifiedResultList);
        }

        // spusti dalsi MODUL

        // RoundPointComputation.compute(loadedResultLists) // new(?)
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


    private static List<String> listFilesForFolder(final String folderAddress) {
        final File folder = new File(folderAddress);
        List<String> filePaths = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if (fileEntry.getName().endsWith(".xml")){
                    System.out.println(fileEntry.getName());
                    filePaths.add(fileEntry.getPath());
                }
            }
        }
        return filePaths;
    }


}
