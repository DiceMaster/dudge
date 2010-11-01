package dudge;
/*
 * DudgeBeanTest.java
 * JUnit based test
 *
 * Created on April 14, 2007, 4:30 PM
 */

import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.ContestType;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.Role;
import dudge.db.User;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Vladimir Shabanov
 */
public class DudgeBeanTest extends TestCase {
	
	DudgeLocal dudge = null;

	private static DudgeLocal lookupDudgeBean() {
		try {
			/*Hashtable<String, String> icargs = new Hashtable<String, String>();
			icargs.put("org.omg.CORBA.ORBInitialHost", "localhost");
			icargs.put("org.omg.CORBA.ORBInitialPort", "8686");
			icargs.put("java.naming.factory.initial",
				"com.sun.enterprise.naming.SerialInitContextFactory");*/
			
			Context c = new InitialContext();//icargs);
			return (DudgeLocal) c.lookup("java:comp/env/ejb/DudgeLocal");
		} catch(NamingException ne) {
			Logger.getLogger(DudgeBeanTest.class.getName()).log(Level.SEVERE, "Exception caught.", ne);
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
	public void setUp() throws Exception {
		dudge = lookupDudgeBean();
	}
	
	@After
	public void tearDown() throws Exception {
		dudge = null;
	}
	
	@org.junit.Test
	public void testUserRegisterGetModify() {
		try {
			String login = Integer.toString( new java.util.Random(
					new java.util.Date().getTime()).nextInt(100000) );
			String password = "pawz0r";
			String email = "jrandom@microsoft.com";
			
			User nuser = dudge.registerUser(
					login,
					password,
					email
					);
			
			assertEquals("login", login, nuser.getLogin());
			assertEquals("email", email, nuser.getEmail());
			
			nuser.setEmail("none@none.no");
			dudge.modifyUser(nuser);
			nuser = dudge.getUser(login);
			assertEquals("Modification failed.", email, nuser.getEmail());
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}
	
	@org.junit.Test
	public void testLanguageAddGet() {
		try {
			Language lang = new Language(
					Integer.toString( new java.util.Random(
					new java.util.Date().getTime()).nextInt(100000) ),
					"Foo Language",
					"Super Foo Language",
					".foo"
					);
			
			Language nlang = dudge.addLanguage(lang);
			
			assertEquals("languageID", languageID, nlang.getLanguageId());
			assertEquals("name", lang.getName(), nlang.getName());
			assertEquals("description", lang.getDescription(), nlang.getDescription());
			assertEquals("fileExtension", lang.getFileExtension(), nlang.getFileExtension());
			
			// Проверяем что список всех языков содержит добавленный.
			assertTrue(dudge.getLanguages().contains(nlang));
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}
	
	@org.junit.Test
	public void testContestAddGetModify() {
		try {
			Contest contest = new Contest(
					0,
					ContestType.ACM,
					new Date(),
					3600,
					new ArrayList<String>(),
					new ArrayList<Role>(),
					new ArrayList<ContestProblem>()
					);
			
			Contest ncontest = dudge.addContest(contest);
			
			assertEquals(contestID, ncontest.getContestId());
			assertEquals(contest.getType(),
					ncontest.getType());
			assertEquals(contest.getLanguageIds(),
					ncontest.getLanguageIds());
			
			assertEquals(contest.getContestProblems(),
					ncontest.getContestProblems());
			
			//assertEquals(contest.getStartTime(),
			//ncontest.getStartTime());
			assertEquals(contest.getDuration(),
					ncontest.getDuration());
			
			// Проверяем что список всех соревнований содержит новосозданное.
			assertTrue("Created contest doesn't exist in contest list.",
					dudge.getContests().contains(ncontest));
		} catch (Exception ex) {
			fail("Caught exception: " + ex + ": \n" + ex.getMessage());
		}
	}
	
	@org.junit.Test
	public void testProblemAddGetModify() {
		try {
			Problem prob = new Problem(0);
			prob.setTitle("foo");
			
			Problem nprob = dudge.addProblem(prob);
			
			nprob.setTitle("bar");
			dudge.modifyProblem(nprob);
			prob = dudge.getProblem(id);
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
