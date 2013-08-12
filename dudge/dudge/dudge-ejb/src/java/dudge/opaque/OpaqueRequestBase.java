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
public class OpaqueRequestBase {
    
    protected Map<String,String> reqParam;
    
    OpaqueRequestBase(Map<String,String> param) {
        reqParam=param;
    }
    
    public String programLanguage() {
        return reqParam.get("prglang");
    }

    public String result() {
        String ret=reqParam.get("result");
        if(ret==null) ret="";
        return ret;
    }
    
    public Boolean displayReadOnly() {
        String pval=reqParam.get("display_readonly");
        return "1".equals(pval);
    }

}
