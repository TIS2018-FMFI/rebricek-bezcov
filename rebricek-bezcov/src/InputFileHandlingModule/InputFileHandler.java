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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InputFileHandler {

    private final static String DATA_DIRECTORY_NAME = ProgramConstraints.DATA_DIRECTORY_NAME;

    public static void addInputFile(String fileAddress){
        saveInputFile(fileAddress);
        loadInputFiles();
    }

    public static void loadInputFiles(){
        List<ResultList> loadedResultLists = getInputFiles();
        new RoundPointComputation(loadedResultLists);
    }


    //================================================================

    public static void saveInputFile(String fileAddress){

        if (!fileAddress.contains(".xml")){
            InteractionModule.printMessage("File is not an XML file.");
            return;
        }

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


        ConfigurationFile configurationFile = new ConfigurationFile();

        String season = Integer.toString(configurationFile.getSeasonYear());
        String newFileName = "kolo"+resultList.getEvent().getRound() + ".xml";
        String savePath = DATA_DIRECTORY_NAME + '/'+season + '/' + newFileName;

        File fileStorageFolder = new File(DATA_DIRECTORY_NAME);
        if (fileStorageFolder.isDirectory() == false) {
            new File(DATA_DIRECTORY_NAME).mkdirs();
            InteractionModule.printMessage("created directory: "+fileStorageFolder.getPath());
        }

        File seasonFolder = new File(DATA_DIRECTORY_NAME + '/'+season);
        if (seasonFolder.isDirectory() == false) {
            new File(DATA_DIRECTORY_NAME + '/'+ season).mkdirs();
            InteractionModule.printMessage("created directory: "+ DATA_DIRECTORY_NAME + '/'+season);
        }

        try {
            InputFileHandler.marshalInputFile(resultList, savePath);
            InteractionModule.printMessage("created file: "+savePath);
        } catch (JAXBException e) {
            InteractionModule.printMessage("An Error has occurred while creating file: " + savePath);
            InteractionModule.printMessage(e.toString());
            return;
        }
    }


    public static List<ResultList> getInputFiles(){

        ConfigurationFile configurationFile = new ConfigurationFile();
        String season = Integer.toString(configurationFile.getSeasonYear());
        String seasonDirectoryName = DATA_DIRECTORY_NAME +"/"+season;

        List<String> inputFilePaths = null;
        try {
            inputFilePaths = listFilesForFolder(seasonDirectoryName);
        } catch (FileNotFoundException e) {
            InteractionModule.printMessage("Directory "+seasonDirectoryName+" does not exist");
            //e.printStackTrace();
            return new ArrayList<>();
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
                //e.printStackTrace();
                return new ArrayList<>();
            }
        }

        if (loadedResultLists.size() == 0) {
            InteractionModule.printMessage("Directory "+seasonDirectoryName+" is empty.");
            return new ArrayList<>();
        }

        return loadedResultLists;
    }


    //==================================================================================

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

    private static List<String> listFilesForFolder(final String folderAddress) throws FileNotFoundException {
        final File folder = new File(folderAddress);
        if (folder.isDirectory() == false){
            throw new FileNotFoundException();
        }
        List<String> filePaths = new ArrayList<>();


        InteractionModule.printMessage("-------------");
        InteractionModule.printMessage("Files loaded:");

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if (fileEntry.getName().endsWith(".xml")){
                    InteractionModule.printMessage(fileEntry.getName());
                    filePaths.add(fileEntry.getPath());
                }
            }
        }

        InteractionModule.printMessage("-------------");

        return filePaths;
    }

}
