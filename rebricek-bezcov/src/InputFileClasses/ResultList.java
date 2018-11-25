package InputFileClasses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author TomasTakacs
 */

@XmlRootElement(name = "ResultList")
public class ResultList {

    @XmlAttribute(name = "iofVersion")
    private String iofVersion;
    @XmlAttribute(name = "status")
    private String status;
    @XmlAttribute(name = "creator")
    private String creator;

    @XmlElement(name = "Event")
    private Event event;

    @XmlElement(name = "ClassResult")
    private List<ClassResult> classResultList;

    public ResultList(){
    }
}
