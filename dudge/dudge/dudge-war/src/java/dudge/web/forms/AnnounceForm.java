/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.forms;

import dudge.DudgeLocal;
import dudge.db.Contest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mikhail_Ivanov
 */
public class AnnounceForm extends ActionForm {
    public static final long serialVersionUID = 1L;
    
    private DudgeLocal dudgeBean;

    private List<Contest> activeContests = Collections.synchronizedList(new ArrayList<Contest>());
    private List<Contest> pendingContests = Collections.synchronizedList(new ArrayList<Contest>());
    private List<Contest> recentlyFinishedContests = Collections.synchronizedList(new ArrayList<Contest>());

    public AnnounceForm() {
    }

    public DudgeLocal getDudgeBean() {
        return dudgeBean;
    }

    public void setDudgeBean(DudgeLocal dudgeBean) {
        this.dudgeBean = dudgeBean;
    }

    public List<Contest> getActiveContests() {
        return activeContests;
    }

    public void setActiveContests(List<Contest> activeContests) {
        this.activeContests = activeContests;
    }

    public List<Contest> getPendingContests() {
        return pendingContests;
    }

    public void setPendingContests(List<Contest> pendingContests) {
        this.pendingContests = pendingContests;
    }

    public List<Contest> getRecentlyFinishedContests() {
        return recentlyFinishedContests;
    }

    public void setRecentlyFinishedContests(List<Contest> recentlyFinishedContests) {
        this.recentlyFinishedContests = recentlyFinishedContests;
    }
}
