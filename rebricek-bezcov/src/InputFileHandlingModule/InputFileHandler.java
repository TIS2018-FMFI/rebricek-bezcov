package InputFileHandlingModule;
/**
 * @author TomasTakacs
 */



import InputFileClasses.ResultList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class InputFileHandler {

    public static ResultList unmarshallInputFile(String sourceFileAddress){

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

    public static boolean marshallInputFile(ResultList resultList, String targetFileAdress){
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
