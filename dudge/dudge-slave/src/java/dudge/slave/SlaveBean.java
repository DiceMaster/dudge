/*
 * SlaveBean.java
 *
 * Created on September 15, 2007, 2:10 AM
 */
package dudge.slave;

import dudge.DudgeRemote;
import dudge.db.SolutionStatus;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.RunResultType;
import dudge.db.Run;
import dudge.db.Solution;
import dudge.db.Contest;
import dudge.logic.ContestTraits;
import dudge.util.Substitution;
import dudge.db.Test;
import dudge.slave.dtest.CheckingLimits;
import dudge.slave.dtest.CheckingResult;
import dudge.slave.dtest.OutputComparer;
import dudge.slave.dtest.SolutionLauncher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Session bean раба. Позволяет проверять решения и получать информацию о рабе.
 *
 * @author Vladimir Shabanov
 */
@Stateless
public class SlaveBean implements dudge.slave.SlaveLocal {

	private static final Logger logger = Logger.getLogger(SlaveBean.class.toString());
	private static final String solutionPrefix = "Solution";
	@EJB
	private DudgeRemote dudgeBean;
	@Resource
	boolean launcherUsePrivilegeDrop = false;
	@Resource
	String launcherUsername = "dudge";
	@Resource
	String launcherDomain = ".";
	@Resource
	String launcherPassword = "123";
	@Resource
	String slaveLanguages = "gcc fpc java";

	/**
	 * Creates a new instance of SlaveBean
	 */
	public SlaveBean() {
	}

	protected void saveSolution(Solution solution) {
		dudgeBean.saveSolution(solution);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	protected void testSolutionInternal(Solution solution) throws SlaveException {
		logger.log(Level.FINE, "Testing Solution {0}", solution.getSolutionId());

		Properties props = new Properties();
		props.setProperty("dtest.usePrivelegeDrop", Boolean.toString(launcherUsePrivilegeDrop));
		props.setProperty("dtest.username", launcherUsername);
		props.setProperty("dtest.domain", launcherDomain);
		props.setProperty("dtest.password", launcherPassword);

		solution.getRuns().clear();
		Contest contest = solution.getContest();
		ContestTraits traits = contest.getTraits();
		Language lang = solution.getLanguage();
		Problem problem = solution.getProblem();
		List<Test> tests = new ArrayList<>(problem.getTests());
		Collections.sort(tests);

		// Компилируем решение.
		SolutionLauncher launcher = new SolutionLauncher(props);

		SourceCompiler compiler = new SourceCompiler(
				solution.getSourceCode(),
				new File(launcher.getTemporaryDirectory()),
				solutionPrefix,
				lang.getFileExtension(),
				lang.getCompilationCommand());

		solution.setStatus(SolutionStatus.COMPILING);

		this.saveSolution(solution);

		boolean isCompiled;

		try {
			isCompiled = compiler.compile();
		} catch (IOException ex) {
			String message = "Running of compiler failed.";
			logger.log(Level.SEVERE, message, ex);
			throw new SlaveException(message, ex);
		} catch (CompilerHangedException ex) {
			String message = "Compiler hanged.";
			logger.log(Level.SEVERE, message, ex);
			throw new SlaveException(message, ex);
		}

		solution.setCompilationTime(compiler.getCompilationTime());

		if (!isCompiled) {
			solution.setStatus(SolutionStatus.COMPILATION_ERROR);
			solution.setStatusMessage(compiler.getOutput());
			this.saveSolution(solution);

			return;
		}

		solution.setStatus(SolutionStatus.RUNNING);
		this.saveSolution(solution);

		Substitution sub = new Substitution();
		sub.set("PROG.NAME", solutionPrefix);
		sub.set("PROG.EXENAME", compiler.getCompiledFile().getName());
		sub.set("PROG.TESTDIR", launcher.getTemporaryDirectory());
		sub.set("PATH.SEPAR", File.separator);

		String solutionExecutionCommand = sub.decodeString(lang.getExecutionCommand());
		logger.log(Level.FINER, "Execution command: {0}", solutionExecutionCommand);

		// Тестируем решение.
		CheckingLimits limits = new CheckingLimits();
		limits.memoryLimit = problem.getMemoryLimit();
		limits.cpuTimeLimit = problem.getCpuTimeLimit();
		limits.realTimeLimit = problem.getRealTimeLimit();
		limits.outputLimit = problem.getOutputLimit();

		int i;
		for (i = 0; i < tests.size(); ++i) {
			Test test = tests.get(i);
			Run run = new Run(solution, test);
			run.setResultType(RunResultType.SUCCESS);
			run.setRunNumber(i + 1);

			ByteArrayOutputStream outs = new ByteArrayOutputStream();
			StringWriter errorStream = new StringWriter();

			// Фикс проблемы с зависанием при запуске теста
			String testInputData = test.getInputData();
			if (!testInputData.endsWith("\n")) {
				testInputData += "\n";
			}

			CheckingResult res = launcher.checkSolution(
					limits,
					solutionExecutionCommand,
					new ByteArrayInputStream(testInputData.getBytes()),
					outs,
					errorStream);

			boolean limit_reached = false;
			switch (res.resultType) {
				case 1:
					run.setResultType(RunResultType.TIME_LIMIT);
					limit_reached = true;
					break;
				case 2:
					run.setResultType(RunResultType.MEMORY_LIMIT);
					limit_reached = true;
					break;
				case 3:
					run.setResultType(RunResultType.OUTPUT_LIMIT);
					limit_reached = true;
					break;
				case 4:
					run.setResultType(RunResultType.RUNTIME_ERROR);
					limit_reached = true;
					break;
				case 13:
					throw new SlaveException("Dynamic Library error.\n\n --DTEST OUTPUT START--\n" + errorStream.toString() + "\n--DTEST OUTPUT END--\n");
			}

			if (!limit_reached) {
				OutputComparer comparator = new LineComparator();
				//TODO: Брать компаратор от сервера в зависимости от задачи.

				try {
					// Сравниваем ответ от решения и эталонный.
					boolean comparationResult = comparator.compare(
							new ByteArrayInputStream(test.getOutputData().getBytes()),
							new ByteArrayInputStream(outs.toByteArray()));

					if (!comparationResult) {
						run.setResultType(RunResultType.WRONG_ANSWER);
					}
				} catch (IOException ex) {
					throw new SlaveException("Exception during output comparation.", ex);
				}

			} // if !limit

			solution.getRuns().add(run);

			logger.log(Level.FINEST, "Result for test #{0}{1}:\n{2}", new Object[]{i, 1, run});

			if (run.getResultType() != RunResultType.SUCCESS && !traits.isRunAllTests()) {
				break;
			}
		} // for

		solution.setStatus(SolutionStatus.PROCESSED);
		this.saveSolution(solution);
		logger.log(Level.FINEST, "Solution {0} processed.", solution.getSolutionId());
	} // testSolutionInternal()

	/**
	 * Запускает на рабе проверку решения.
	 *
	 * @param solution решение на проверку.
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public void testSolution(Solution solution) throws SlaveException {
		try {
			testSolutionInternal(solution);
		} catch (NoClassDefFoundError ex) {
			logger.log(Level.SEVERE, "Class is not defined. Most likely, it is SolutionLauncher in dtest-lib. "
					+ "Try placing dtest-lib.jar into application server's 'lib' folder."
					+ "\n\nClasspath: " + System.getProperty("java.class.path")
					+ "\n\nJava library path: " + System.getProperty("java.library.path"), ex);
			if (solution != null) {
				solution.setStatus(SolutionStatus.INTERNAL_ERROR);

				StringWriter sw = new StringWriter();
				sw.write("Class is not defined. Most likely, it is SolutionLauncher in dtest-lib. "
						+ "Try placing dtest-lib.jar into application server's 'lib' folder.\n\n");
				ex.printStackTrace(new PrintWriter(sw));
				solution.setStatusMessage(sw.getBuffer().toString());
				dudgeBean.saveSolution(solution);
			}
			throw new SlaveException("Class is not defined. Most likely, it is SolutionLauncher in dtest-lib. "
					+ "Try placing dtest-lib.jar into application server's 'lib' folder.", ex);
		} catch (UnsatisfiedLinkError ex) {
			logger.log(Level.SEVERE, "dtest.dll/libdtest.so is not found. Please place it in "
					+ "java.library.path: " + System.getProperty("java.library.path"), ex);
			if (solution != null) {
				solution.setStatus(SolutionStatus.INTERNAL_ERROR);

				StringWriter sw = new StringWriter();
				sw.write("dtest.dll/libdtest.so is not found.\n Please place it in "
						+ "java.library.path: " + System.getProperty("java.library.path") + "\n\n");
				ex.printStackTrace(new PrintWriter(sw));
				solution.setStatusMessage(sw.getBuffer().toString());
				dudgeBean.saveSolution(solution);
			}
			throw new SlaveException("dtest.dll/libdtest.so is not found.\n Please place it in "
					+ "java.library.path: " + System.getProperty("java.library.path"), ex);
		} catch (Throwable ex) {
			logger.log(Level.SEVERE, "Internal slave error occured on solution "
					+ (solution != null ? solution.getSolutionId() : "") + " Exception " + ex.getClass().getName(), ex);
			if (solution != null) {
				solution.setStatus(SolutionStatus.INTERNAL_ERROR);

				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				solution.setStatusMessage(sw.getBuffer().toString());
				dudgeBean.saveSolution(solution);
			}
			throw new SlaveException("Internal slave error occured on solution " + (solution != null ? solution.getSolutionId() : ""), ex);
		}
	} // testSolution()
}
