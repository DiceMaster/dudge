package dudge;

import dudge.web.User;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Vladimir Shabanov
 */
public class DudgeWeb {
	
	@EJB
	private UserLocal userBean;

	@WebMethod(operationName = "authenticate")
	public boolean authenticate(@WebParam(name = "login") String login, @WebParam(name = "password") String password) {
		return userBean.authenticate(login, password);
	}

	@WebMethod(operationName = "getUser")
	public User getUser(@WebParam(name = "login") String login) {
		return new User(userBean.getUser(login));
	}

	/*	@WebMethod(operationName = "registerUser")
	 public User registerUser(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "email") String email) {
	 return dudgeBean.registerUser(login, password, email);
	 }

	 @WebMethod(operationName = "modifyUser")
	 @Oneway
	 public void modifyUser(@WebParam(name = "user") User user) {
	 dudgeBean.modifyUser(user);
	 }

	 @WebMethod(operationName = "deleteUser")
	 @Oneway
	 public void deleteUser(@WebParam(name = "login") String login) {
	 dudgeBean.deleteUser(login);
	 }

	 @WebMethod(operationName = "getLanguage")
	 public Language getLanguage(@WebParam(name = "languageId") String languageId) {
	 return dudgeBean.getLanguage(languageId);
	 }

	 @WebMethod(operationName = "getLanguages")
	 public Language[] getLanguages() {
	 return (Language[]) dudgeBean.getLanguages().toArray();
	 }

	 @WebMethod(operationName = "addLanguage")
	 public Language addLanguage(@WebParam(name = "language") Language language) {
	 return dudgeBean.addLanguage(language);
	 }

	 @WebMethod(operationName = "modifyLanguage")
	 @Oneway
	 public void modifyLanguage(@WebParam(name = "language") Language language) {
	 dudgeBean.modifyLanguage(language);
	 }

	 @WebMethod(operationName = "deleteLanguage")
	 @Oneway
	 public void deleteLanguage(@WebParam(name = "languageId") String languageId) {
	 dudgeBean.deleteLanguage(languageId);
	 }
	
	 @WebMethod(operationName = "getDefaultContest")
	 public Contest getDefaultContest() {
	 return dudgeBean.getDefaultContest();
	 }

	 @WebMethod(operationName = "getContest")
	 public Contest getContest(@WebParam(name = "contestId") int contestId) {
	 return dudgeBean.getContest(contestId);
	 }

	 @WebMethod(operationName = "getContests")
	 public Contest[] getContests() {
	 return (Contest[]) dudgeBean.getContests().toArray();
	 }

	 @WebMethod(operationName = "getActiveContests")
	 public Contest[] getActiveContests() {
	 return (Contest[]) dudgeBean.getActiveContests().toArray();
	 }

	 @WebMethod(operationName = "getPendingContests")
	 public Contest[] getPendingContests() {
	 return (Contest[]) dudgeBean.getPendingContests().toArray();
	 }

	 @WebMethod(operationName = "getRecentlyFinishedContests")
	 public Contest[] getRecentlyFinishedContests() {
	 return (Contest[]) dudgeBean.getRecentlyFinishedContests().toArray();
	 }

	 @WebMethod(operationName = "addContest")
	 public Contest addContest(@WebParam(name = "contest") Contest contest) {
	 return dudgeBean.addContest(contest);
	 }

	 @WebMethod(operationName = "modifyContest")
	 @Oneway
	 public void modifyContest(@WebParam(name = "contest") Contest contest) {
	 dudgeBean.modifyContest(contest);
	 }

	 @WebMethod(operationName = "deleteContest")
	 @Oneway
	 public void deleteContest(@WebParam(name = "contestId") int contestId) {
	 dudgeBean.deleteContest(contestId);
	 }

	 @WebMethod(operationName = "getProblem")
	 public Problem getProblem(@WebParam(name = "problemId") int problemId) {
	 return dudgeBean.getProblem(problemId);
	 }

	 @WebMethod(operationName = "addProblem")
	 public Problem addProblem(@WebParam(name = "problem") Problem problem) {
	 return dudgeBean.addProblem(problem);
	 }

	 @WebMethod(operationName = "modifyProblem")
	 @Oneway
	 public void modifyProblem(@WebParam(name = "problem") Problem problem) {
	 dudgeBean.modifyProblem(problem);
	 }

	 @WebMethod(operationName = "deleteProblem")
	 @Oneway
	 public void deleteProblem(@WebParam(name = "problemId") int problemId) {
	 dudgeBean.deleteProblem(problemId);
	 }

	 @WebMethod(operationName = "getSolution")
	 public Solution getSolution(@WebParam(name = "solutionId") int solutionId) {
	 return dudgeBean.getSolution(solutionId);
	 }

	 @WebMethod(operationName = "getSolutions")
	 public Solution[] getSolutions(@WebParam(name = "login") String login, @WebParam(name = "contestId") int contestId, @WebParam(name = "problemId") int problemId) {
	 return (Solution[]) dudgeBean.getSolutions(login, contestId, problemId).toArray();
	 }

	 @WebMethod(operationName = "submitSolution")
	 public Solution submitSolution(@WebParam(name = "solution") Solution solution) {
	 return dudgeBean.submitSolution(solution);
	 }

	 @WebMethod(operationName = "modifySolution")
	 @Oneway
	 public void modifySolution(@WebParam(name = "solution") Solution solution) {
	 dudgeBean.modifySolution(solution);
	 }

	 @WebMethod(operationName = "getAcmMonitorRecords")
	 public AcmMonitorRecord[] getAcmMonitorRecords(@WebParam(name = "contestId") Contest contest, @WebParam(name = "date") Date when) {
	 return (AcmMonitorRecord[]) dudgeBean.getAcmMonitorRecords(contest, when).toArray();
	 }
	 */
}
