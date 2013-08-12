/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import dudge.ContestLocal;
import dudge.DudgeLocal;
import dudge.LanguageLocal;
import dudge.ProblemLocal;
import dudge.SolutionLocal;
import dudge.UserLocal;
import dudge.db.Contest;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import dudge.db.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author duke
 * TODO: всь логику приложения унести в opaqueBean
 */
@WebService(serviceName = "OpaqueQuestionEngine", targetNamespace = "http://opaque.dudge/")
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public class OpaqueQuestionEngine {
    
    private static final Logger logger = Logger.getLogger("OpaqueQuestionEngine");
    
    // special values for moodle opaque question module
    private static final String ANSWER_SAVED="Answer saved";
    private static final String ANSWER_GRADED="Answer graded";
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    /**
     *.
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "getEngineInfo")
    @WebResult(name = "EngineInfoReturn", targetNamespace = "http://opaque.dudge/", partName = "getEngineInfoReturn")
    public String getEngineInfo() {
        
        OpaqueBeanLocal opaqueBean=lookupOpaqueBean();

        return "<engineinfo>"+
                     "<name>DudgeQuestionEngine</name>"+
                     "<usedmemory>0 bytes</usedmemory>"+
                     "<activesessions>"+opaqueBean.getSessionsCount()+"</activesessions>"+
               "</engineinfo>";
    }

    /**
     *.
     * @param questionVersion
     * @param questionBaseURL
     * @param questionID
     * @return
     *     returns java.lang.String
     * @throws OmException
     */
    @WebMethod(operationName = "getQuestionMetadata")
    @WebResult(name = "QuestionMetadataReturn", targetNamespace = "http://opaque.dudge/", partName = "getQuestionMetadata")
    public String getQuestionMetadata(
        @WebParam(name = "questionID", targetNamespace = "http://opaque.dudge/", partName = "questionID")
        String questionID,
        @WebParam(name = "questionVersion", targetNamespace = "http://opaque.dudge/", partName = "questionVersion")
        String questionVersion,
        @WebParam(name = "questionBaseURL", targetNamespace = "http://opaque.dudge/", partName = "questionBaseURL")
        String questionBaseURL)
        throws OmException
    {
        // FIXME: число баллов marks должно совпадать с баллами положительной оценки в process()
        return "<questionmetadata>"+
                  "<scoring><marks>1</marks></scoring>"+
                  "<plainmode>yes</plainmode>"+
               "</questionmetadata>";
    }

     /**
     * 
     * @param questionSession
     * @throws OmException
     */
    @WebMethod(operationName = "stop")
    public void stop(
        @WebParam(name = "questionSession", targetNamespace = "http://opaque.dudge/", partName = "questionSession")
        String questionSession)
        throws OmException
    {
        logger.info("Enter stop() questionSession="+questionSession);
        
        OpaqueBeanLocal opaqueBean=lookupOpaqueBean();
        opaqueBean.dropSession(questionSession);
    }

    /**
     * 
     * @param initialParamNames
     * @param questionVersion
     * @param questionBaseURL
     * @param questionID
     * @param initialParamValues
     * @param cachedResources
     * @return
     *     returns dudge.opaque.StartReturn
     * @throws OmException
     */
    @WebMethod(operationName = "start")
    @WebResult(name = "StartReturn", targetNamespace = "http://opaque.dudge/", partName = "startReturn")
    public StartReturn start(
        @WebParam(name = "questionID", targetNamespace = "http://opaque.dudge/", partName = "questionID")
        String questionID,
        @WebParam(name = "questionVersion", targetNamespace = "http://opaque.dudge/", partName = "questionVersion")
        String questionVersion,
        @WebParam(name = "questionBaseURL", targetNamespace = "http://opaque.dudge/", partName = "questionBaseURL")
        String questionBaseURL,
        @WebParam(name = "initialParamNames", targetNamespace = "http://opaque.dudge/", partName = "initialParamNames")
        ArrayOfStrings initialParamNames,
        @WebParam(name = "initialParamValues", targetNamespace = "http://opaque.dudge/", partName = "initialParamValues")
        ArrayOfStrings initialParamValues,
        @WebParam(name = "cachedResources", targetNamespace = "http://opaque.dudge/", partName = "cachedResources")
        ArrayOfStrings cachedResources)
        throws OmException
    {
        StartReturn val = new StartReturn();   

        logger.info("Enter start()");
        Map<String,String> param=params2map(initialParamNames,initialParamValues);
        
        int problemid=question2ProblemId(questionID,questionVersion); 
        
        ProblemLocal problemBean=lookupProblemBean();
        Problem problem = problemBean.getProblem(problemid);
        
        OpaqueBeanLocal opaqueBean=lookupOpaqueBean();
        OpaqueRequestStart req = new OpaqueRequestStart(param);
        
        String sessionid=opaqueBean.makeQuestionSession(req.seed());
        logger.info("sessionid="+sessionid+" problemid="+problemid);
        OpaqueSession S=new OpaqueSession(sessionid,problemid);        
        req.initializeSession(S);
        opaqueBean.updateSession(S);
        
        val.setProgressInfo("Question loaded"); // FIXME: возможно здесь должно быть более внятное сообщение
        val.setQuestionSession(sessionid);        

        String resultHtml=makeXHTML(problem,S,
                req.result(),
                req.programLanguage(),
                req.displayReadOnly(),
                sessionid);
        val.setXHTML(resultHtml);
      
        logger.info("progressInfo: "+val.getProgressInfo());
        return val;
    }

    /**
     *
     * @param values
     * @param names
     * @param questionSession
     * @return
     *     returns dudge.opaque.ProcessReturn
     * @throws OmException
     */
    @WebMethod(operationName = "process")
    @WebResult(name = "ProcessReturnElement", targetNamespace = "http://opaque.dudge/", partName = "processReturn")
    public ProcessReturn process(
        @WebParam(name = "questionSession", targetNamespace = "http://opaque.dudge/", partName = "questionSession")
        String questionSession,
        @WebParam(name = "names", targetNamespace = "http://opaque.dudge/", partName = "names")
        ArrayOfStrings names,
        @WebParam(name = "values", targetNamespace = "http://opaque.dudge/", partName = "values")
        ArrayOfStrings values)
        throws OmException
    {
        logger.info("Enter process() questionSession="+questionSession);
        Map<String,String> param=params2map(names,values);
        
        ProcessReturn val = new ProcessReturn();
        
        OpaqueBeanLocal opaqueBean=lookupOpaqueBean();
        OpaqueRequestProcess req = new OpaqueRequestProcess(param);
        
        OpaqueSession session=opaqueBean.getSession(questionSession);
        if(session==null) {
            logger.warning("Unknown questionSession detected, aborted");
            // FIXME: нужно заполнить ответ подобающим образом
            return val;
        }
        session.nextStep();

        String langid=req.programLanguage();
        String originalsessionid=req.originalSession();        
        String src=req.result();
        Boolean isFinish=req.finish();        
        Boolean isReadOnly=(isFinish || session.isDisplayReadOnly());
                
        // Финишный запрос не содержит никаких пользовательских параметров 
        // и по нему нельзя определить - slave mode у нас или нет
        // но на этот момент нужная информация уже точно есть в сессии
        if(isFinish)
            originalsessionid=session.getOriginalSession();
        
        if(originalsessionid==null) 
            originalsessionid=questionSession;
        
        // Если originalsessionid не совпадает с sessionid то это означает,
        // что идет повтор запросов (slave mode) и нужно возвращать ровно те ответы,
        // которые были возвращены при первичных запросах
        // и следовательно не требуется запускать проверку решения т.к. она будет повторной
        boolean isSlaveMode=!questionSession.equals(originalsessionid);
        if(isSlaveMode)
            logger.info("Slave mode detected: cur="+questionSession+" orig="+originalsessionid);

        int problemid=session.getProblemId();
        logger.info("problemid="+problemid);
        ProblemLocal problemBean=lookupProblemBean();
        Problem problem = problemBean.getProblem(problemid);
        
        if(isSlaveMode)
             { processSlave(opaqueBean,session,val,req,
                     originalsessionid); }
        else { processMaster(opaqueBean,session,val,req,
                     src,langid,questionSession,problem); }
                
        String resultHtml=makeXHTML(problem,session,src,langid,isReadOnly,originalsessionid);
        val.setXHTML(resultHtml);

        logger.info("progressInfo: "+val.getProgressInfo());
        return val;
    }

    private void processSlave(
            OpaqueBeanLocal opaqueBean,
            OpaqueSession session,
            ProcessReturn val,
            OpaqueRequestProcess req,
            String originalsessionid) 
    {
        Boolean isIntermediaStep=false;
        
        if(req.finish()) {
            // TODO: вернуть ответ
            if(session.isSolution()) {
                String emptysrc="";
                SolutionLocal solutionBean=lookupSolutionBean();
                Solution solution=solutionBean.getSolution(session.getSolutionId());
                checkSolutionStatus(solution,emptysrc,val);
            }
            else {
                logger.info("Finish w/o solution in slave mode");
                val.setProgressInfo(ANSWER_SAVED);
            }
        }
        else {
            int originalsolutionid;
            OpaqueSession originalsession2=opaqueBean.getSession(originalsessionid);
            
            if(originalsession2==null) {
                OpaqueOriginalSession originalsession=opaqueBean.getOriginalSession(originalsessionid);
                if(originalsession==null) {
                    logger.warning("Undefined original session in slave mode, aborted");
                    // FIXME: нужно заполнить ответ подобающим образом
                    return;
                }
                originalsolutionid=originalsession.getSolutionId();
                isIntermediaStep=session.getSteps()<originalsession.getSteps();
            }
            else {
                originalsolutionid=originalsession2.getSolutionId();
                isIntermediaStep=session.getSteps()<originalsession2.getSteps();                
            }

            if(originalsolutionid==-1) {
                    logger.warning("Undefined original solutionid in slave mode, aborted");
                    // FIXME: нужно заполнить ответ подобающим образом
                    return;
            }            
            session.setSolutionId(originalsolutionid);
            if(isIntermediaStep) {
                logger.info("Intermedia step in slave mode");
                val.setProgressInfo(ANSWER_SAVED);
            }
        }
    }
    
    private void processMaster(
            OpaqueBeanLocal opaqueBean,
            OpaqueSession session,
            ProcessReturn val,
            OpaqueRequestProcess req,
            String src,
            String langid,
            String questionSession,
            Problem problem
            ) 
    {                
        logger.info("isSolution="+session.isSolution());
        Boolean needNewSolution;
        if(session.isSolution()) {

            SolutionLocal solutionBean=lookupSolutionBean();
            Solution solution=solutionBean.getSolution(session.getSolutionId());
            needNewSolution=checkSolutionStatus(solution,src,val);

            if(!needNewSolution && src.isEmpty()) { // restore source code of solution
                src=solution.getSourceCode();
                langid=solution.getLanguage().getLanguageId();
                } 
        }
        else {
            // solution will be created only at finish stage
            needNewSolution=req.finish();
        }
        
        if (needNewSolution) {
            Boolean created=makeNewSolution(src, langid, problem, session, val);
            if(created)
                { opaqueBean.updateSession(session); }
        }

        // FIXME: если финишных пакетов будет несколько, поведение будет неадекватным
        // т.к. в базу буде записано несколько экземпляров
        if(req.finish()) 
            { opaqueBean.saveAsOriginalSession(questionSession); }
        
    }
    
    private String makeXHTML(
                Problem problem,
                OpaqueSession session,
                String src,
                String langid,
                boolean isReadOnly,
                String originalsessionid) {
        
        LanguageLocal languageBean=lookupLanguageBean();
        List<Language> llist=languageBean.getLanguages();
        String langHtml="";
        String def;
        for(Language l : llist) {
            def = (l.getLanguageId().equals(langid)) ? "selected":"";
            //logger.info("default="+langid+" curr="+l.getLanguageId()+" res="+def);
            langHtml+="<option "+def+" value='"+l.getLanguageId()+"'>"+
                    l.getName()+"</option>";
        }

        String title=problem.getTitle();
        String desc=problem.getDescription();
       	//problem.getCpuTimeLimit();
	//problem.getRealTimeLimit();
	//problem.getMemoryLimit();
	//problem.getOutputLimit();
		
	//problem.getOwner().getLogin();
        //problem.getAuthor();
	//problem.getCreateTime();

        String roHtml;
        String disHtml;
        
        if(isReadOnly) { roHtml="readonly='readonly'"; disHtml="disabled='disabled'"; }
        else { roHtml=""; disHtml=""; }
        
        src=stringToHTMLString(src);
        String resultHtml;
        resultHtml="<div class='qtext'>"+
                "<h1>"+title+"</h1>"+desc+"</div>"+
                "<div class='ablock'><div class='answer'>"+
                "<br/><select "+disHtml+" name='%%IDPREFIX%%prglang'>"+langHtml+"</select>"+
                "<br/><textarea class='qtype_opaque_monospaced qtype_opaque_response' "+roHtml+
                        " rows='30' cols='60' name='%%IDPREFIX%%result'>"+
                        src+"</textarea>";
        if(session.getSolutionId()!=-1)
            { resultHtml+="<input type='hidden' name='%%IDPREFIX%%solutionid' value='"+session.getSolutionId()+"' />"; }
        if(originalsessionid!=null)
            { resultHtml+="<input type='hidden' name='%%IDPREFIX%%originalsession' value='"+originalsessionid+"' />"; }
        if(!isReadOnly && session.isInteractiveBehaviour()) {
            // FIXME: в зависимости от ожидаемого поведения можно показывать разный набор кнопок
            // например:
            // enteranswer - узнать результаты проверки ранее отправленного решения
            // tryagain - отправить новое решение на проверку, если старое уже оценено (как неверное)
            // clear - очистить поле для вставки нового решения методом copy-paste
            resultHtml+="\n<br/>"+
                //"<input type='submit' name='%%IDPREFIX%%omact_tryagain' value='%%lTRYAGAIN%%'/>"+
                "<input type='submit' name='%%IDPREFIX%%omact_enteranswer' value='%%lENTERANSWER%%'/>"+
                //"<input type='button' onClick='' name='%%IDPREFIX%%omact_clear' value='%%lCLEAR%%'/>"+
                "";            
        }
        resultHtml+="</div></div>"; // end of answer&ablock
                
        return resultHtml;
    }
    
    private boolean checkSolutionStatus(
                                Solution solution,
                                String src,
                                ProcessReturn val) {
            String oldSrc=solution.getSourceCode();
            //String oldLangid=solution.getLanguage().getLanguageId();
            boolean needNewSolution;

            logger.info("Old source code: "+oldSrc);
            logger.info("New source code: "+src);
            
            needNewSolution = !src.equals(oldSrc) && !src.isEmpty();
            logger.info("Source code is changed ? "+needNewSolution);
            
            if(!needNewSolution) {
                String status;
                                
                if(solution.getStatus() != SolutionStatus.PROCESSED
				|| solution.getContest().getTraits().isRunAllTests()
				|| solution.getLastRunResult() == null) {
                    status=solution.getStatus().toString();
		} else { 
                    status = solution.getLastRunResult().toString();
                }

                // итоговое состояние
                if(solution.getStatus() == SolutionStatus.PROCESSED ||
                   solution.getStatus() == SolutionStatus.DISQUALIFIED ||
                   solution.getStatus() == SolutionStatus.COMPILATION_ERROR) {
                    
                    Results res= new Results();
                    Score score=new Score();
                    val.setQuestionEnd(true);
                    if(status.equals("SUCCESS"))
                        { score.setMarks(1); } // FIXME: максимальный балл должен совпадать с getQuestionMetadata
                    else{ score.setMarks(0); }
                    res.getScores().add(score);
                    val.setProgressInfo(ANSWER_GRADED); // must be
                    res.setActionSummary(status+" "+solution.getStatusMessage());

                    val.setResults(res); // результаты должны быть только в итоговом ответе
                    logger.info("Solution "+solution.getSolutionId()+" finished, status= "+status);
                } else { // Solution в процессе обработки
                    // пока нет оценки, отображается только progressInfo
                    //res.setActionSummary(solution.getStatusMessage());
                    //status+"<br/>"+solution.getStatusMessage()
                    val.setProgressInfo(ANSWER_SAVED);
                    logger.info("Solution "+solution.getSolutionId()+" in progress, status="+status);
                }
            }
            
        return needNewSolution;
    }

    
    private int question2ProblemId(String questionId, String questionVersion) {
        int pid;
        // questionId должно быть в формате idNNN
        Pattern p = Pattern.compile("id(\\d+)");
        Matcher m = p.matcher(questionId);
        if(m.matches()) {
            pid=Integer.parseInt(m.group(1));
        }
        else {
            NamingException ne= new NamingException("Invalid question ID");
            throw new RuntimeException(ne);
        }
        logger.info("question2ProblemId() "+questionId+"/"+questionVersion+" --> "+pid);
        
        return pid; // TODO: брать из questionID+questionVersion
    }
    
    private Map<String,String> params2map(ArrayOfStrings names, ArrayOfStrings values) {
        Map<String,String> ret=new HashMap<>();
        
        List<String> n=names.getItem();
        List<String> v=values.getItem();
        int k;
        
        for(k=0;k<n.size();k++) {
            ret.put(n.get(k),v.get(k));
            logger.info("param["+n.get(k)+"]='"+v.get(k)+"'");
        }
        
        return ret;
    }
    
    private static String stringToHTMLString(String string) {
    StringBuilder sb = new StringBuilder(string.length());
    // true if last char was blank
    boolean lastWasBlankChar = false;
    int len = string.length();
    char c;

    for (int i = 0; i < len; i++)
        {
        c = string.charAt(i);
        if (c == ' ') {
            // blank gets extra work,
            // this solves the problem you get if you replace all
            // blanks with &nbsp;, if you do that you loss 
            // word breaking
            if (lastWasBlankChar) {
                lastWasBlankChar = false;
                sb.append("&nbsp;");
                }
            else {
                lastWasBlankChar = true;
                sb.append(' ');
                }
            }
        else {
            lastWasBlankChar = false;
            //
            // HTML Special Chars
            if (c == '"')      { sb.append("&quot;");}
            else if (c == '&') { sb.append("&amp;");}
            else if (c == '<') { sb.append("&lt;");}
            else if (c == '>') { sb.append("&gt;");}
            //else if (c == '\n')sb.append("&lt;br/&gt;");
            else {
                int ci = 0xffff & c;
                if (ci < 160 )
                    // nothing special only 7 Bit
                     { sb.append(c); }
                else {
                    // Not 7 Bit use the unicode system
                    sb.append("&#");
                    sb.append(new Integer(ci).toString());
                    sb.append(';');
                    }
                }
            }
        }
    return sb.toString();
}

    private Object lookupBean(String beanName) {
	try {
		Context c = new InitialContext();
		return c.lookup(beanName);
	}
	catch(NamingException ne) {
		Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
		throw new RuntimeException(ne);
	}        
    }
    
    private OpaqueBeanLocal lookupOpaqueBean() {
        return (OpaqueBeanLocal)lookupBean("java:global/dudge/dudge-ejb/OpaqueBean!dudge.opaque.OpaqueBeanLocal");
    }
    
    private DudgeLocal lookupDudgeBean() {
        return (DudgeLocal)lookupBean("java:global/dudge/dudge-ejb/DudgeBean!dudge.DudgeLocal");
    }
    
    private ProblemLocal lookupProblemBean() {
        return (ProblemLocal)lookupBean("java:global/dudge/dudge-ejb/ProblemBean!dudge.ProblemLocal");
    }

    private LanguageLocal lookupLanguageBean() {
        return (LanguageLocal)lookupBean("java:global/dudge/dudge-ejb/LanguageBean!dudge.LanguageLocal");
    }

    private UserLocal lookupUserBean() {
        return (UserLocal)lookupBean("java:global/dudge/dudge-ejb/UserBean!dudge.UserLocal");
    }

    private ContestLocal lookupContestBean() {
        return (ContestLocal)lookupBean("java:global/dudge/dudge-ejb/ContestBean!dudge.ContestLocal");
    }

    private SolutionLocal lookupSolutionBean() {
        return (SolutionLocal)lookupBean("java:global/dudge/dudge-ejb/SolutionBean!dudge.SolutionLocal");
    }

    private Boolean makeNewSolution(String src, 
            String langid, 
            Problem problem,
            OpaqueSession session, 
            ProcessReturn val) 
    {    
     Boolean solutionCreated;
     int contestId=2; // Global contest TODO: брать из настроек
     String username="admin"; // TODO: брать из настроек

        if (!src.isEmpty()) {
            DudgeLocal dudgeBean = lookupDudgeBean();              
            ContestLocal contestBean=lookupContestBean();
            Contest contest = contestBean.getContest(contestId);
            UserLocal userBean=lookupUserBean();
            User user = userBean.getUser(username);
            LanguageLocal languageBean=lookupLanguageBean();
            Language language = languageBean.getLanguage(langid);

            Solution solution = new Solution();
            solution.setUser(user);
            solution.setContest(contest);
            solution.setLanguage(language);
            solution.setProblem(problem);
            solution.setSourceCode(src);

            solution = dudgeBean.submitSolution(solution);
            session.setSolutionId(solution.getSolutionId());
            val.setProgressInfo(ANSWER_SAVED);
            logger.info("New solution");
            solutionCreated=true;
        } else {
            val.setProgressInfo("Empty answer, none todo.");
            logger.info("Skip solution creation - empty answer received");
            solutionCreated=false;
        }
        
        return solutionCreated;
    }
    
}
// EOF