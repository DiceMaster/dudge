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
    
    public Boolean displayReadOnly() {
        String pval=reqParam.get("display_readonly");
        return "1".equals(pval);
    }
    
    // результаты могут присутствовать как в запросах типа process, 
    // так и в запросах типа start (в случае, когда новый ответ основан на старом ответе)
    public Boolean isResultExist() {
        return reqParam.containsKey("result") && reqParam.containsKey("prglang");
    }

    public String programLanguage() {
        String ret=reqParam.get("prglang");
        if(ret==null) ret="";
        return ret;
    }

    public String result() {
        String ret=reqParam.get("result");
        if(ret==null) ret="";
        return ret;
    }

}
