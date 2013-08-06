package dudge;
/*
 * DudgeBeanTest.java
 * JUnit based test
 *
 * Created on April 14, 2007, 4:30 PM
 */

import dudge.db.Contest;
import dudge.db.ContestType;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.User;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Vladimir Shabanov
 */
public class DudgeBeanTest extends TestCase {

	private static final Logger logger = Logger.getLogger(DudgeBeanTest.class.toString());
	DudgeLocal dudgeBean = null;
	UserLocal userBean = null;
	ContestLocal contestBean = null;
	LanguageLocal languageBean = null;
	ProblemLocal problemBean = null;

	private static DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:global/dudge/dudge-ejb/DudgeLocal");//java:comp/env/ejb/DudgeLocal
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "Exception caught.", ne);
			throw new RuntimeException(ne);
		}
	}

	private static UserLocal lookupUserBean() {
		try {
			Context c = new InitialContext();
			return (UserLocal) c.lookup("java:global/dudge/dudge-ejb/UserLocal");//java:comp/env/ejb/UserLocal
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "Exception caught.", ne);
			throw new RuntimeException(ne);
		}
	}

	private static ContestLocal lookupContestBean() {
		try {
			Context c = new InitialContext();
			return (ContestLocal) c.lookup("java:global/dudge/dudge-ejb/ContestLocal");//java:comp/env/ejb/ContestLocal
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "Exception caught.", ne);
			throw new RuntimeException(ne);
		}
	}

	private static LanguageLocal lookupLanguageBean() {
		try {
			Context c = new InitialContext();
			return (LanguageLocal) c.lookup("java:global/dudge/dudge-ejb/LanguageLocal");//java:comp/env/ejb/LanguageLocal
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "Exception caught.", ne);
			throw new RuntimeException(ne);
		}
	}

	private static ProblemLocal lookupProblemBean() {
		try {
			Context c = new InitialContext();
			return (ProblemLocal) c.lookup("java:global/dudge/dudge-ejb/ProblemLocal");//java:comp/env/ejb/ProblemLocal
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "Exception caught.", ne);
			throw new RuntimeException(ne);
		}
	}

	/**
	 * Для запуска JUnit4 тестов ant'ом.
	 */
	/*	public static junit.framework.Test suite() {
	 return new JUnit4TestAdapter(DudgeBeanTest.class);
	 }*/
	public DudgeBeanTest() {
	}

	@Before
	@Override
	public void setUp() throws Exception {
		dudgeBean = lookupDudgeBean();
		userBean = lookupUserBean();
		contestBean = lookupContestBean();
		languageBean = lookupLanguageBean();
		problemBean = lookupProblemBean();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		dudgeBean = null;
		userBean = null;
		contestBean = null;
		problemBean = null;
	}

	@org.junit.Test
	public void testUserRegisterGetModify() {
		try {
			String login = Integer.toString(new java.util.Random(new java.util.Date().getTime()).nextInt(100000));
			String password = "pawz0r";
			String email = "jrandom@microsoft.com";

			User nuser = userBean.registerUser(login, password, email);

			assertEquals("login", login, nuser.getLogin());
			assertEquals("email", email, nuser.getEmail());

			nuser.setEmail("none@none.no");
			userBean.modifyUser(nuser);
			nuser = userBean.getUser(login);
			assertEquals("Modification failed.", email, nuser.getEmail());
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}

	@org.junit.Test
	public void testLanguageAddGet() {
		try {

			int languageID = new java.util.Random(new java.util.Date().getTime()).nextInt(100000);

			Language lang = new Language(
					Integer.toString(languageID), // id
					"Foo Language", // name
					"Super Foo Language", // description
					".foo", // file extension
					"compile", // compilation command
					"exec" // execution command
					);

			Language nlang = languageBean.addLanguage(lang);

			assertEquals("languageID", languageID, nlang.getLanguageId());
			assertEquals("name", lang.getName(), nlang.getName());
			assertEquals("description", lang.getDescription(), nlang.getDescription());
			assertEquals("fileExtension", lang.getFileExtension(), nlang.getFileExtension());

			// Проверяем что список всех языков содержит добавленный.
			assertTrue(languageBean.getLanguages().contains(nlang));
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}

	@org.junit.Test
	public void testContestAddGetModify() {
		try {
			Contest contest = new Contest(
					"New Contest", // caption
					"It will be great", // description
					"No rules", // rules
					ContestType.ACM, // contest type
					new Date(), // startTime
					3600 // duration
					);

			Contest ncontest = contestBean.addContest(contest);

			assertEquals(contest.getType(), ncontest.getType());
			assertEquals(contest.getRules(), ncontest.getRules());
			assertEquals(contest.getContestProblems(), ncontest.getContestProblems());
			assertEquals(contest.getStartTime(), ncontest.getStartTime());
			assertEquals(contest.getDuration(), ncontest.getDuration());

			// Проверяем что список всех соревнований содержит новосозданное.
			assertTrue("Created contest doesn't exist in contest list.", contestBean.getContests().contains(ncontest));
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}

	@org.junit.Test
	public void testProblemAddGetModify() {
		try {
			Problem prob = new Problem();
			prob.setTitle("foo");

			Problem nprob = problemBean.addProblem(prob);
			nprob.setTitle("bar");
			problemBean.modifyProblem(nprob);
			prob = problemBean.getProblem(nprob.getProblemId());
			assertEquals("Modification failed.", nprob.getTitle(), prob.getTitle());
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}
	/*	@org.junit.Test
	 public void testGetSolution() {
	 fail("Not yet implemented");
	 }
 
	 @org.junit.Test
	 public void testAddSolution() {
	 fail("Not yet implemented");
	 }*/
}
