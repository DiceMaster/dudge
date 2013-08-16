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
        //logger.info("isFinish="+isFinish+" -finish='"+reqParam.get("-finish")+"'");
        return isFinish;
    }

    public Boolean isEnterAnswerButtonPressed() {
        return reqParam.containsKey("omact_enteranswer");
    }

    public Boolean isClearButtonPressed() {
        return reqParam.containsKey("omact_clear");
    }

    public Boolean isTryAgainButtonPressed() {
        return reqParam.containsKey("omact_tryagain");
    }

    public Boolean isComment() {
        return reqParam.containsKey("-comment");
    }

    // коментарий преподавателя к ответу
    public String comment() {
        String retval;
        if(this.isComment()) retval=reqParam.get("-comment");
        else retval="";
        
        return retval;
    }

    // оценка, выставленная преподавателем вручную
    // идет только вместе с комментарием преподавателя к ответу
    public int mark() {
        return Integer.parseInt(reqParam.get("-mark"));
    }

    // максимальная оценка которую можно выставить
    // идет только вместе с комментарием преподавателя к ответу
    public int maxmark() {
        return Integer.parseInt(reqParam.get("-maxmark"));
    }
}
