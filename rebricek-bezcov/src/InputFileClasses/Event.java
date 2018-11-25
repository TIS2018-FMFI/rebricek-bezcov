package InputFileClasses;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author TomasTakacs
 */

public class Event {

    @XmlElement(name = "Id")
    private long id;
    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "StartTime")
    private StartTime startTime;

    @XmlElement(name="Rank")
    private Integer rank;

    public Event(){
    }

}
