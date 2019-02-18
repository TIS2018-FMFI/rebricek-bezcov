//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0-b170531.0717 
//         See <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
//         Any modifications to this file will be lost upon recompilation of the source schema. 
//         Generated on: 2019.01.03 at 06:02:46 PM CET 
//


package InputFileClasses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for ResultList complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Event" type="{http://www.orienteering.org/datastandard/3.0}Event"/&gt;
 *         &lt;element name="ClassResult" type="{http://www.orienteering.org/datastandard/3.0}ClassResult" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="iofVersion" type="{http://www.w3.org/2001/XMLSchema}float" /&gt;
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="creator" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultList", propOrder = {
    "event",
    "classResult"
})
@XmlRootElement(name = "ResultList")
public class ResultList {

    @XmlElement(name = "Event", required = true)
    protected Event event;
    @XmlElement(name = "ClassResult")
    protected List<ClassResult> classResult;
    @XmlAttribute(name = "iofVersion")
    protected Float iofVersion;
    @XmlAttribute(name = "status")
    protected String status;
    @XmlAttribute(name = "creator")
    protected String creator;

    /**
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link Event }
     *     
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event }
     *     
     */
    public void setEvent(Event value) {
        this.event = value;
    }

    /**
     * Gets the value of the classResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassResult }
     * 
     * 
     */
    public List<ClassResult> getClassResult() {
        if (classResult == null) {
            classResult = new ArrayList<ClassResult>();
        }
        return this.classResult;
    }

    /**
     * Gets the value of the iofVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getIofVersion() {
        return iofVersion;
    }

    /**
     * Sets the value of the iofVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setIofVersion(Float value) {
        this.iofVersion = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the creator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

}