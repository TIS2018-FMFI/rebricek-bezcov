package InputFileHandlingModule;
/**
 * @author TomasTakacs
 */


import DataVerificationModule.InputFileVerificator;
import InputFileClasses.ResultList;
import InteractionModule.InteractionModule;
import computingModule.RoundPointComputation;
import configurationModule.ConfigurationFile;
import ProgramConstraintsModule.ProgramConstraints;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class InputFileHandler {

    private final static String DATA_DIRECTORY_NAME = ProgramConstraints.DATA_DIRECTORY_NAME;
    // TODO na spajane ciest pouzit metody operacneho systemu
    // TODO nahradit printy za metody Interakcneho modulu

    public static void addInputFile(String fileAddress){
        ResultList inputResultList = null;
        try {
            inputResultList = InputFileHandler.unmarshalInputFile(fileAddress);
        }
        catch (JAXBException e) {
            InteractionModule.printMessage("An Error has occurred while reading file: " + fileAddress);
            InteractionModule.printMessage(e.toString());
            return;
        }

        // daj skontrolovat InputFileVerificator-u
        ResultList resultList = InputFileVerificator.verifyInputFile(inputResultList);

        //String season = "2018";         // placeholder, dummy value
        String season = Integer.toString(new ConfigurationFile().getSeasonYear());

        // get FileStorage // TODO

        String newFileName = "kolo"+resultList.getEvent().getRound() + ".xml";

        String savePath = DATA_DIRECTORY_NAME + '/'+season + '/' + newFileName;

        File fileStorageFolder = new File(DATA_DIRECTORY_NAME);
        if (fileStorageFolder.isDirectory() == false) {
            new File(DATA_DIRECTORY_NAME).mkdirs();
            InteractionModule.printMessage("created directory: "+fileStorageFolder.getPath());
            //System.out.println("created directory: "+DATA_DIRECTORY_NAME);
        }

        File seasonFolder = new File(DATA_DIRECTORY_NAME + '/'+season);
        if (seasonFolder.isDirectory() == false) {
            new File(DATA_DIRECTORY_NAME + '/'+ season).mkdirs();
            InteractionModule.printMessage("created directory: "+ DATA_DIRECTORY_NAME + '\\'+season);
        }

        try {
            InputFileHandler.marshalInputFile(resultList, savePath);
            InteractionModule.printMessage("created file: "+savePath);
        } catch (JAXBException e) {
            InteractionModule.printMessage("An Error has occurred while creating file: " + savePath);
            InteractionModule.printMessage(e.toString());
            return;
        }
        //if (success == false)
        // InteractionModule.printMessage(MESSAGE_MARSHALLING_FAILED)
        // nastala chyba pri ukladaní vstupného súboru

        loadInputFiles();
    }


    public static void loadInputFiles(){

        //String season = "2018";      // placeholder, dummy value
        String season = Integer.toString(new ConfigurationFile().getSeasonYear());
        String seasonDirectoryName = DATA_DIRECTORY_NAME +"/"+season;

        List<String> inputFilePaths = null;
        try {
            inputFilePaths = listFilesForFolder(seasonDirectoryName);
        } catch (FileNotFoundException e) {
            InteractionModule.printMessage("Directory "+seasonDirectoryName+" does not exist");
            //e.printStackTrace();
            return;
        }
        List<ResultList> loadedResultLists = new ArrayList<>();

        for (String fileAddress : inputFilePaths){
            ResultList resultList = null;
            try {
                resultList = InputFileHandler.unmarshalInputFile(fileAddress);
                ResultList verifiedResultList = InputFileVerificator.verifyInputFile(resultList);
                loadedResultLists.add(verifiedResultList);
            } catch (JAXBException e) {
                InteractionModule.printMessage("An Error has occurred while reading file: " + fileAddress);
                InteractionModule.printMessage(e.toString());
                e.printStackTrace();
                return;
            }
        }

        // <TEST>
//        System.out.println(loadedResultLists.get(0).getClassResult().size());
//        int person_count = 0;
//        for (ClassResult cr : loadedResultLists.get(0).getClassResult()){
//            person_count+=cr.getPersonResult().size();
//        }
//        System.out.println(person_count);
        // </TEST>


        // spusti dalsi MODUL
        new RoundPointComputation(loadedResultLists);
    }


    private static ResultList unmarshalInputFile(String sourceFileAddress) throws JAXBException {
        ResultList resultList = null;

        JAXBContext jc = JAXBContext.newInstance(ResultList.class);

        File sourceFile = new File(sourceFileAddress);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        resultList = (ResultList) unmarshaller.unmarshal(sourceFile);

        return resultList;
    }

    private static void marshalInputFile(ResultList resultList, String targetFileAdress) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ResultList.class);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File targetFile = new File(targetFileAdress);
        marshaller.marshal(resultList, targetFile);
    }


    // DATA_DIRECTORY_NAME + season + filename + extension
    private static List<String> listFilesForFolder(final String folderAddress) throws FileNotFoundException {
        final File folder = new File(folderAddress);
        if (folder.isDirectory() == false){
            throw new FileNotFoundException();
        }
        List<String> filePaths = new ArrayList<>();


//        System.out.println("-------------");
//        System.out.println("Files loaded:");

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if (fileEntry.getName().endsWith(".xml")){
//                    System.out.println(fileEntry.getName());
                    filePaths.add(fileEntry.getPath());
                }
            }
        }

//        System.out.println("-------------");

        return filePaths;
    }

}
