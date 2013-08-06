/*
 * SlaveBeanTest.java
 * JUnit based test
 *
 * Created on June 2, 2007, 9:46 PM
 */
package dudge.slave;

import dudge.db.Contest;
import dudge.db.ContestType;
import dudge.db.Run;
import dudge.db.Test;
import dudge.db.RunResultType;
import dudge.db.SolutionStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import dudge.db.Solution;

import dudge.db.Language;
import dudge.db.Problem;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Vladimir Shabanov
 * @author Zaur Muhametgaleev
 */
public class SlaveBeanTest extends TestCase {

	SlaveBeanNoEjb slave;
	Contest cont;
	Language lang;
	Problem prob;
	List<Test> tests;
	Solution sol;
	int memoryLimit = 64 * 1024 * 1024;
	String quote;

	public SlaveBeanTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		// Включаем самый детальный уровень лога у хандлеров корневого логгера.
		for (Handler hand : Logger.getLogger("").getHandlers()) {
			hand.setLevel(Level.FINEST);
		}

		Logger.getLogger(SlaveBean.class.toString()).setLevel(Level.FINEST);
		Logger.getLogger(SourceCompiler.class.toString()).setLevel(Level.FINEST);
		Logger.getLogger(TokenizedComparator.class.toString()).setLevel(Level.FINEST);

		slave = new SlaveBeanNoEjb();

		cont = new Contest();
		cont.setType(ContestType.ACM);

		String os_version = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		if (os_version.contains("windows")) {
			quote = "\"";
		} else {
			quote = "";
		}

		lang = new Language();
		lang.setLanguageId("gcc");
		lang.setFileExtension(".cpp");
		lang.setCompilationCommand(
				"g++ -static -static-libgcc -pipe -O0 -o "
				+ quote + "${PROG.EXENAME}" + quote + " "
				+ quote + "${PROG.SRCNAME}" + quote);
		lang.setExecutionCommand(quote + "${PROG.TESTDIR}/${PROG.EXENAME}" + quote);

		tests = new ArrayList<>();
		tests.add(new Test("2 3\n", "5\n"));

		prob = new Problem("A + B", "Description", 10 * 1024 * 1024, 1000, 5000, 1024 * 1024);
		prob.setTests(tests);
		prob.setCpuTimeLimit(1000);
		prob.setMemoryLimit(memoryLimit);
		prob.setOutputLimit(1 * 1024 * 1024);
		prob.setRealTimeLimit(10000);


		sol = new Solution();
		sol.setContest(cont);
		sol.setLanguage(lang);
		sol.setProblem(prob);
	}

	@Override
	protected void tearDown() throws Exception {
		cont = null;
		lang = null;
		prob = null;
		tests = null;
		sol = null;
	}

	/**
	 * Проверка выполнения верного решения на наборе тестов.
	 */
	public void testTestSolutionSuccess() throws SlaveException {
		sol.setSourceCode(
				"#include <iostream>\n"
				+ "using namespace std;\n"
				+ "int main(){\n"
				+ "	int a, b;\n"
				+ "	cin >> a >> b;\n"
				+ "	cout << a+b << endl;\n"
				+ "	return 0;\n"
				+ "}\n");

		List<Run> runs = new ArrayList<>();
		runs.add(new Run(sol, tests.get(0), RunResultType.SUCCESS));

		slave.testSolution(sol);

		assertEquals(runs, slave.getSavedSolution().getRuns());
	}

	/**
	 * Проверка выполнения решения на наборе тестов. Решение на первом тесте должно быть верным, а на втором - нет.
	 */
	public void testTestSolutionWrongAnswer() throws SlaveException {
		sol.setSourceCode(
				"#include <iostream>\n"
				+ "using namespace std;\n"
				+ "int main(){\n"
				+ "	int a, b;\n"
				+ "	cin >> a >> b;\n"
				+ "	cout << 5 << endl;\n"
				+ "	return 0;\n"
				+ "}\n");

		/* Второй тест. */
		tests.add(new Test("3 4\n", "7\n"));

		List<Run> runs = new ArrayList<>();
		runs.add(new Run(sol, tests.get(0), RunResultType.SUCCESS));
		runs.add(new Run(sol, tests.get(1), RunResultType.WRONG_ANSWER));

		slave.testSolution(sol);

		assertEquals(runs, slave.getSavedSolution().getRuns());
	}

	/**
	 * Проверяет статус выполнения теста при попытке выполнения некомпилируемой задачи.
	 */
	public void testTestSolutionCompilationError()
			throws SlaveException, CloneNotSupportedException {
		sol.setSourceCode(
				"int main(){\n"
				+ "	return"
				+ "}\n");

		sol.setStatus(SolutionStatus.COMPILATION_ERROR);

		slave.testSolution((Solution) sol.clone());

		assertEquals(sol, slave.getSavedSolution());
	}

	/**
	 * Проверка работы слейва при превышении лимита по времени.
	 */
	public void testTestSolutionTimeLimit() throws SlaveException {
		sol.setSourceCode(
				"int main(){\n"
				+ "	while(true){}\n"
				+ "	return 0\n;"
				+ "}\n");

		List<Run> runs = new ArrayList<>();
		runs.add(new Run(sol, tests.get(0), RunResultType.TIME_LIMIT));

		slave.testSolution(sol);

		assertEquals(runs, slave.getSavedSolution().getRuns());
	}

	/**
	 * Проверка работы ограничения по объему выводимых данных.
	 */
	public void testTestSolutionOutputLimit() throws SlaveException {
		sol.setSourceCode(
				"#include<iostream>\n"
				+ "using namespace std;\n"
				+ "int main(){\n"
				+ "    while(true){\n"
				+ "        cout << \"GaRbAgE\";\n"
				+ "    }\n"
				+ "    return 0;\n"
				+ "}\n");

		List<Run> runs = new ArrayList<>();
		runs.add(new Run(sol, tests.get(0), RunResultType.OUTPUT_LIMIT));

		slave.testSolution(sol);

		assertEquals(runs, slave.getSavedSolution().getRuns());

	}

	/**
	 * Проверка работы ограничения по памяти для выполняемого решения.
	 */
	public void testTestSolutionMemoryLimit() throws SlaveException {
		sol.setSourceCode(
				"#include<iostream>\n"
				+ "using namespace std;\n"
				+ "int main(){\n"
				+ /*"	int a, b;\n" +
				 "	cin >> a >> b;\n" +
				 "	cout << a+b << endl;\n\n" +*/ "	long size = 1024 * 64;\n"
				+ "	double array[size];\n"
				+ "	return 0;\n"
				+ "}\n");

		/* Уменьшение значения лимита памяти для выполняемой задачи. */
		prob.setMemoryLimit(1024);

		List<Run> runs = new ArrayList<>();
		runs.add(new Run(sol, tests.get(0), RunResultType.MEMORY_LIMIT));

		slave.testSolution(sol);

		assertEquals(runs, slave.getSavedSolution().getRuns());
	}
}
