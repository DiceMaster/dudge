/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;

/**
 *
 * @author duke
 */
@Singleton
public class OpaqueBean implements OpaqueBeanLocal {

    private Map<String,OpaqueSession> session=new HashMap<String,OpaqueSession>();
    
    @Override
    public int getSessionsCount() {
        return session.size();
    }

    @Override
    public String makeQuestionSession(String randomseed) {
        String questionSession;
        
        if(session.get(randomseed)==null) {
            questionSession=randomseed;
        }
        else {
            questionSession="-1"; // FIXME: генерировать уникальный id
        }
        return questionSession;
    }
    
    @Override
    public OpaqueSession getSession(String questionSession) {
        return session.get(questionSession);
    }

    @Override
    public void updateSession(String questionSession, OpaqueSession one) {
        session.put(questionSession,one);
    }

    @Override
    public void dropSession(String questionSession) {
        session.remove(questionSession);        
    }

}
