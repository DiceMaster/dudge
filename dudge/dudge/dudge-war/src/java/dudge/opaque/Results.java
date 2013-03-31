package dudge.opaque;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Results complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Results">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actionSummary" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="answerLine" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attempts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="customResults" type="{http://opaque.dudge/}CustomResult" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="questionLine" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="scores" type="{http://opaque.dudge/}Score" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Results", propOrder = {
    "actionSummary",
    "answerLine",
    "attempts",
    "customResults",
    "questionLine",
    "scores"
})
public class Results {

    @XmlElement(required = true)
    protected String actionSummary;
    @XmlElement(required = true)
    protected String answerLine;
    protected int attempts;
    protected List<CustomResult> customResults;
    @XmlElement(required = true)
    protected String questionLine;
    protected List<Score> scores;

    public Results() {
        this.actionSummary=null;
        this.answerLine=null;
        this.questionLine=null;
        this.attempts=0;
    }
    
    /**
     * Gets the value of the actionSummary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionSummary() {
        return actionSummary;
    }

    /**
     * Sets the value of the actionSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionSummary(String value) {
        this.actionSummary = value;
    }

    /**
     * Gets the value of the answerLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnswerLine() {
        return answerLine;
    }

    /**
     * Sets the value of the answerLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnswerLine(String value) {
        this.answerLine = value;
    }

    /**
     * Gets the value of the attempts property.
     * 
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the value of the attempts property.
     * 
     */
    public void setAttempts(int value) {
        this.attempts = value;
    }

    /**
     * Gets the value of the customResults property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customResults property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomResult }
     * 
     * 
     */
    public List<CustomResult> getCustomResults() {
        if (customResults == null) {
            customResults = new ArrayList<CustomResult>();
        }
        return this.customResults;
    }

    /**
     * Gets the value of the questionLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestionLine() {
        return questionLine;
    }

    /**
     * Sets the value of the questionLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestionLine(String value) {
        this.questionLine = value;
    }

    /**
     * Gets the value of the scores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Score }
     * 
     * 
     */
    public List<Score> getScores() {
        if (scores == null) {
            scores = new ArrayList<Score>();
        }
        return this.scores;
    }

}

