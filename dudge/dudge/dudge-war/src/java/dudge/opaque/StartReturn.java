/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StartReturn complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StartReturn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CSS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XHTML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="progressInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="questionSession" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resources" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StartReturn", propOrder = {
    "css",
    "xhtml",
    "progressInfo",
    "questionSession",
    "resources"
})
public class StartReturn {

    @XmlElement(name = "CSS", required = true)
    protected String css;
    @XmlElement(name = "XHTML", required = true)
    protected String xhtml;
    @XmlElement(required = true)
    protected String progressInfo;
    @XmlElement(required = true)
    protected String questionSession;
    protected List<String> resources;

    /**
     * Gets the value of the css property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSS() {
        return css;
    }

    /**
     * Sets the value of the css property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSS(String value) {
        this.css = value;
    }

    /**
     * Gets the value of the xhtml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXHTML() {
        return xhtml;
    }

    /**
     * Sets the value of the xhtml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXHTML(String value) {
        this.xhtml = value;
    }

    /**
     * Gets the value of the progressInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgressInfo() {
        return progressInfo;
    }

    /**
     * Sets the value of the progressInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgressInfo(String value) {
        this.progressInfo = value;
    }

    /**
     * Gets the value of the questionSession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestionSession() {
        return questionSession;
    }

    /**
     * Sets the value of the questionSession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestionSession(String value) {
        this.questionSession = value;
    }

    /**
     * Gets the value of the resources property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resources property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResources().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResources() {
        if (resources == null) {
            resources = new ArrayList<String>();
        }
        return this.resources;
    }

}
