package TestsInputFileHandling;

import InputFileClasses.ClassResult;
import InputFileClasses.ResultList;
import InputFileHandlingModule.InputFileHandler;
import ProgramConstraintsModule.ProgramConstraints;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestInputFileHandling {

    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);


    private void deleteFolder(File folder){
        File[] contents = folder.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteFolder(file);
            }
        }
        folder.delete();
    }

    private void deleteDataFolder(){
        deleteFolder(new File(ProgramConstraints.DATA_DIRECTORY_NAME));
    }

    /**
     * req. [1.12.2.2]
     */
    @Test
    public void testAddNotXML(){
        System.setOut(printStream);

        InputFileHandler.addInputFile("TestsInputFiles/notxmlfile.txt");
        assertTrue(outputStream.toString().contains("File is not an XML file."));
    }

    /**
     * req. ?
     */
    @Test
    public void testLoadNonexDir(){
        System.setOut(printStream);
        deleteDataFolder();

        InputFileHandler.loadInputFiles();
        assertTrue(outputStream.toString().contains("Directory DATA_STORAGE") && outputStream.toString().contains("does not exist"));
    }


    /**
     * req. [1.11.1], [1.11.2.5]
     */
    @Test
    public void testCreateCopyAndFolder(){
        deleteDataFolder();

        InputFileHandler.addInputFile("TestsInputFiles/kolo1-completedEvent.xml");
        List<ResultList> loadedResultLists = InputFileHandler.getInputFiles();

        assertEquals(1, loadedResultLists.size());

        ResultList resultList = loadedResultLists.get(0);
    }

    /**
     * req. [1.12.2.1]
     */
    @Test
    public void testLoadSingleInputFile(){
        deleteDataFolder();

        InputFileHandler.addInputFile("TestsInputFiles/kolo1-completedEvent.xml");
        List<ResultList> loadedResultLists = InputFileHandler.getInputFiles();

        assertEquals(loadedResultLists.size(), 1);

        ResultList resultList = loadedResultLists.get(0);
        // 19 categories
        assertEquals(resultList.getClassResult().size(), 19);

        int countPersonResult = 0;
        for (ClassResult cr : resultList.getClassResult()){
            countPersonResult += cr.getPersonResult().size();
        }
        // 188 records of person result
        assertEquals(188, countPersonResult);
    }

    /**
     * req. [1.11.2.4], [1.12.1]
     */
    @Test
    public void testCreateCopies(){
        deleteDataFolder();

        InputFileHandler.addInputFile("TestsInputFiles/kolo1-completedEvent.xml");
        InputFileHandler.addInputFile("TestsInputFiles/kolo2-completedEvent.xml");
        InputFileHandler.addInputFile("TestsInputFiles/kolo3-completedEvent.xml");

        List<ResultList> loadedResultLists = InputFileHandler.getInputFiles();

        assertEquals(3, loadedResultLists.size());
    }

    /**
     * req. [1.11.2.3], [3.1.1.1]
     */
    @Test
    public void testFillEvent(){
        // https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    }


}
