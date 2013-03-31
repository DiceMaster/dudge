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
 * <p>Java class for ProcessReturn complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcessReturn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CSS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XHTML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="progressInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="questionEnd" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="resources" type="{http://opaque.dudge/}Resource" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="results" type="{http://opaque.dudge/}Results"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessReturn", propOrder = {
    "css",
    "xhtml",
    "progressInfo",
    "questionEnd",
    "resources",
    "results"
})
public class ProcessReturn {

    @XmlElement(name = "CSS", required = true)
    protected String css;
    @XmlElement(name = "XHTML", required = true)
    protected String xhtml;
    @XmlElement(required = true)
    protected String progressInfo;
    protected boolean questionEnd;
    protected List<Resource> resources;
    @XmlElement(required = true)
    protected Results results;

    public ProcessReturn() {
        this.css = null;
        this.xhtml = null;
        this.progressInfo = null;
        this.questionEnd = false;
    }

    
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
     * Gets the value of the questionEnd property.
     * 
     */
    public boolean isQuestionEnd() {
        return questionEnd;
    }

    /**
     * Sets the value of the questionEnd property.
     * 
     */
    public void setQuestionEnd(boolean value) {
        this.questionEnd = value;
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
     * {@link Resource }
     * 
     * 
     */
    public List<Resource> getResources() {
        if (resources == null) {
            resources = new ArrayList<Resource>();
        }
        return this.resources;
    }

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link Results }
     *     
     */
    public Results getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link Results }
     *     
     */
    public void setResults(Results value) {
        this.results = value;
    }

}
