/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import javax.ejb.Local;

/**
 *
 * @author duke
 */
@Local
public interface OpaqueBeanLocal {

    /**
     *
     * @return
     */
    int getSessionsCount();
    
    /**
     *
     * @return
     */
    String makeQuestionSession(String randomseed);


    /**
     *
     * @return
     */
    OpaqueSession getSession(String questionSession);

    /**
     *
     * @return
     */
    void updateSession(String questionSession, OpaqueSession val);
    
    /**
     *
     * @return
     */
    void dropSession(String questionSession);
}
