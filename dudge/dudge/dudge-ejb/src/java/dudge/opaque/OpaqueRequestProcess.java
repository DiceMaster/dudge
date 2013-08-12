/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author duke
 */
public class OpaqueRequestProcess extends OpaqueRequestBase {
    
    private static final Logger logger = Logger.getLogger("OpaqueRequestProcess");
    
    public OpaqueRequestProcess(Map<String,String> param) {
            super(param);
    }
    
    public String originalSession() {
        return reqParam.get("originalsession");
    }
    
    public Boolean finish() {
        Boolean isFinish="1".equals(reqParam.get("-finish"));
        logger.info("isFinish="+isFinish+" -finish='"+reqParam.get("-finish")+"'");
        return isFinish;
    }
}
