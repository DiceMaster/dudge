/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.util.logging.Logger;

/**
 *
 * @author duke
 */
public class OpaqueSession {

    private static final Logger logger = Logger.getLogger("OpaqueSession");
    
    private String sessionid;
    private int problemid;
    private int solutionid;
    private String originalsession;
    private int steps;
    
    private String behaviour; // ожидаемое поведение
    private String locale;  // язык надписей
    private Boolean display_readonly; // формировать html-фрагмент для r/o-режима
    private Boolean display_correctness; // показывать правильность решения
    private Boolean display_feedback; // отзыв на конкретное решение
    private Boolean display_remark; // дополнительные пояснения к задаче
    private int precision; // точность - число цифр после запятой в оценке
    
    private String src;     // текст ответа
    private String langid;  // язык программирования
    
    
    public OpaqueSession(String sessionid,int problemid) {
        this.sessionid=sessionid;
        this.problemid = problemid;
        this.steps=1;
        this.solutionid=-1;
        this.behaviour="unknown";
        this.locale="ru";
        this.display_correctness=false;
        this.display_feedback=false;
        this.display_readonly=false;
        this.display_remark=false;        
        this.precision=0;        
        this.src="";
        this.langid="unknown";
    }
    
    public String getSessionId() {
        return this.sessionid;
    }
    
    public int getProblemId() {
        return problemid;
    }
    
    public Boolean isSolution() {
        return solutionid!=-1;
    }
    
    public int getSolutionId() {
        return solutionid;
    }
    
    public void setSolutionId(int solutionid) {
        this.solutionid=solutionid;
    }

    public String getOriginalSession() {
        return originalsession;
    }
    
    public void setOriginalSession(String originalSession) {
        this.originalsession=originalSession;
    }

    public int getSteps() {
        return steps;
    }
    
    public void nextStep() {
        this.steps++;
    }

    
    
    public String getBehaviour() {
        return behaviour;
    }
    
    public void setBehaviour(String behaviour) {
        this.behaviour=behaviour;
    }
    
    public Boolean isInteractiveBehaviour() {
        return ("adaptive".equalsIgnoreCase(behaviour) ||
                "adaptivenopenalty".equalsIgnoreCase(behaviour) ||
                "interactive".equalsIgnoreCase(behaviour) ||
                "immediatefeedback".equalsIgnoreCase(behaviour) ||
                "immediatecbm".equalsIgnoreCase(behaviour)
                );
    }

    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale=locale;
    }
    
    public Boolean isDisplayReadOnly() {
        return display_readonly;
    }
    
    public void setDisplayReadOnly(Boolean display_readonly) {
        this.display_readonly=display_readonly;
    }

    public Boolean isDisplayCorrectness() {
        return display_correctness;
    }
    
    public void setDisplayCorrectness(Boolean display_correctness) {
        this.display_correctness=display_correctness;
    }

    public Boolean isDisplayFeedback() {
        return display_feedback;
    }
    
    public void setDisplayFeedback(Boolean display_feedback) {
        this.display_feedback=display_feedback;
    }

    public Boolean isDisplayRemark() {
        return display_remark;
    }
    
    public void setDisplayRemark(Boolean display_remark) {
        this.display_remark=display_remark;
    }

    public void setResult(String result, String language) {
        this.logger.info("setResult()" + result);
        this.src=result;
        this.langid=language;
    }
    
    public String getSourceCode() {
        return src;
    }
    
    public String getLanguageId() {
        return langid;
    }
}
