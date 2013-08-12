/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author duke
 */
@Singleton
public class OpaqueBean implements OpaqueBeanLocal {

    private Map<String,OpaqueSession> session=new HashMap<String,OpaqueSession>();
    @PersistenceContext(unitName = "dudge-ejbPU")
    private EntityManager em;
    protected static final Logger logger = Logger.getLogger(OpaqueBean.class.toString());
    
    @Override
    public int getSessionsCount() {
        return session.size();
    }

    @Override
    public String makeQuestionSession(String randomseed) {
        String questionSession;
        
        // for store as original session in database, it must be a number and database wide unique
        questionSession=Long.toString(Calendar.getInstance().getTimeInMillis()); 

        return questionSession;
    }
    
    @Override
    public OpaqueSession getSession(String questionSession) {
        return session.get(questionSession);
    }

    @Override
    public void updateSession(OpaqueSession one) {
        session.put(one.getSessionId(),one);
    }

    @Override
    public void dropSession(String questionSession) {
        session.remove(questionSession);        
    }

    @Override
    public OpaqueOriginalSession getOriginalSession(String questionSession) {
        Long qs;
        try { qs=Long.parseLong(questionSession); }
        catch(Exception e){
            this.logger.warning("Invalid questionSession: "+questionSession);
            return null;
            }
        OpaqueOriginalSession session = (OpaqueOriginalSession) em.find(OpaqueOriginalSession.class, qs);
        
        return session;        
    }

    @Override
    public void saveAsOriginalSession(String questionSession) {
        OpaqueSession val=this.getSession(questionSession);
        if(val==null) {
            this.logger.warning("Unknown questionSession");
            throw new IllegalArgumentException("questionSession is invalid");
        }
        
        if(val.getSolutionId()>0) {
            OpaqueOriginalSession session=new OpaqueOriginalSession();
        
            session.setSessionId(Long.parseLong(questionSession));
            session.setSolutionId(val.getSolutionId());
            session.setSteps(val.getSteps());

            em.persist(session);
            em.flush();
        }
        else this.logger.warning("Session without solution id, saving skipped");
    }

}
