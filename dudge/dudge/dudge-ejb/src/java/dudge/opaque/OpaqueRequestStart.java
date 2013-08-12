/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.util.Map;

/**
 *
 * @author duke
 */
public class OpaqueRequestStart extends OpaqueRequestBase {
    
    public OpaqueRequestStart(Map<String,String> param) {
            super(param);
    }
    
    public String seed() {
        return reqParam.get("randomseed");
    }

    public String preferredBehaviour() {
        return reqParam.get("preferredbehaviour");
    }
    
    public String language() {
        return reqParam.get("language"); // язык надписей
    }
    
    public Boolean displayCorrectness() {
        String pval=reqParam.get("display_correctness");
        return "1".equals(pval);
    }
    
    public Boolean displayFeedback() {
        String pval=reqParam.get("display_feedback");
        return "1".equals(pval);
    }

    public Boolean displayGeneralFeedback() {
        String pval=reqParam.get("display_generalfeedback");
        return "1".equals(pval);
    }
    
    public void initializeSession(OpaqueSession S) {
        String pval;
        
        pval=preferredBehaviour();
        if(pval!=null) S.setBehaviour(pval);

        pval=language();
        if(pval!=null) S.setLocale(pval);

        S.setDisplayReadOnly(displayReadOnly());        
        S.setDisplayCorrectness(displayCorrectness());
        S.setDisplayFeedback(displayFeedback());
        S.setDisplayRemark(displayGeneralFeedback());
    }
}
