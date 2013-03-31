/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import javax.xml.ws.WebFault;

/**
 *
 * @author duke
 */

@WebFault(name = "OmException", targetNamespace = "http://opaque.dudge/")
public class OmException
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private OmExceptionType faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public OmException(String message, OmExceptionType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public OmException(String message, OmExceptionType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dudge.opaque.OmExceptionType
     */
    public OmExceptionType getFaultInfo() {
        return faultInfo;
    }

}
