/*
 * SourceCompiler.java
 *
 * Created on 14 Май 2007 г., 17:49
 */
package dudge.slave;

import dudge.util.Substitution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для компилирования исходных кодов решения. В процессе своей работы вызывает компилятор и передает ему пути и ключи для компиляции.
 *
 * @author Vladimir Shabanov
 */
public class SourceCompiler {

	private final static Logger logger = Logger.getLogger(SourceCompiler.class.toString());
	private final static long compilationTimeout = 30000; // milliseconds
	private String sourceCode;
	private File dstDir;
	private String solutionPrefix;
	private File sourceFile = null;
	private File compiledFile = null;
	private String compilationCommand;
	private StringBuffer outputBuffer = new StringBuffer();
	private int compilationTime = 0; //milliseconds
        
        private int problemId =-1; // FIXME: temporary hack for DB olymp 2013

	/**
	 *
	 */
	private class ProcessWaiterThread implements Runnable {

		private Process process;

		public ProcessWaiterThread(Process process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				process.waitFor();
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * Класс потока, принимающий вывод комплятора, в котором могут содержаться предупреждения и ошибки компиляции.
	 */
	private class OutputReaderThread implements Runnable {

		private InputStream inputStream;
		private StringBuffer outputBuffer;

		/**
		 * Конструктор класса.
		 */
		public OutputReaderThread(InputStream inputStream, StringBuffer outBuffer) {
			this.inputStream = inputStream;
			this.outputBuffer = outBuffer;
		}

		@Override
		public void run() {
			try {
				BufferedReader compilerOutput = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String s;
				while ((s = compilerOutput.readLine()) != null) {
					outputBuffer.append(s);
					outputBuffer.append("\n");
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Error occured", ex.getMessage());
			}
		}
	}

	/**
	 *
	 * @param sourceCode
	 * @param dstDir
	 * @param solutionPrefix
	 * @param solutionExtension
	 * @param compilationCommand
	 */
	public SourceCompiler(
			String sourceCode,
			File dstDir,
			String solutionPrefix,
			String solutionExtension,
			String compilationCommand) {
		this.sourceCode = sourceCode;
		this.dstDir = dstDir;
		this.solutionPrefix = solutionPrefix;
		this.compilationCommand = compilationCommand;

		this.compiledFile = new File(
				dstDir.getPath()
				+ File.separator
				+ solutionPrefix //+ executableExtension
				).getAbsoluteFile();

		this.sourceFile = new File(
				dstDir.getPath()
				+ File.separator
				+ solutionPrefix
				+ solutionExtension).getAbsoluteFile();

		compilationTime = 0;
	}

	/**
	 * Метод компиляции решения задачи.
	 *
	 * @return true если компиляция прошла успешно, false - если в исходном коде решения были ошибки.
	 * @throw IOException при невозможности найти или создать один из необходимых файлов.
	 * @throw CompilerHangedException если компилятор повис.
	 */
	public synchronized boolean compile() throws IOException, CompilerHangedException {

		Substitution sub = new Substitution();

		try {
			try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(sourceFile), "UTF-8")) {
				osw.write(sourceCode + "\n");
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to create file with source code.\n{0}", ex.getMessage());
			throw ex;
		}

		sub.set("PROG.NAME", solutionPrefix);
		sub.set("PROG.EXENAME", getCompiledFile().getCanonicalPath());
		sub.set("PROG.SRCNAME", getSourceFile().getCanonicalPath());
		sub.set("PROG.TESTDIR", dstDir.getCanonicalPath());
		sub.set("PATH.SEPAR", File.separator);
                sub.set("PROBLEM.ID", String.valueOf(getProblemId())); // FIXME: temporary hack for DB olymp 2013


		String decodedCommand = sub.decodeString(compilationCommand);
		logger.log(Level.FINEST, "Compilation command: {0}", decodedCommand);

		long compilerStarted = System.currentTimeMillis();
		compilationTime = 0;

		Process compilerProc = Runtime.getRuntime().exec(decodedCommand, null, dstDir);

		outputBuffer.setLength(0);
		new Thread(new OutputReaderThread(compilerProc.getErrorStream(), outputBuffer)).start();
		new Thread(new OutputReaderThread(compilerProc.getInputStream(), outputBuffer)).start();

		int compilerExitCode;
		try {
			// Запускаем поток ожидания процесса компилятора.
			Thread waiter = new Thread(new ProcessWaiterThread(compilerProc));
			waiter.start();
			waiter.join(compilationTimeout);
			if (waiter.isAlive()) {
				// Если поток все еще жив после таймаута,
				// то это значит, что компилятор повис.
				logger.severe("Compiler hanged.");

				// Убиваем процесс компилятора.
				compilerProc.destroy();

				// Кидаем исключение о повисании компилятора.
				throw new CompilerHangedException();
			}

			compilerExitCode = compilerProc.exitValue();
		} catch (InterruptedException ex) {
			return false;
		}

		compilationTime = (int) (System.currentTimeMillis() - compilerStarted);

		if (compilerExitCode != 0) {
			logger.log(Level.FINER, "Compilation failed due to errors in source code. Error:\n{0}", outputBuffer.toString());
			return false;
		}

		logger.finest("Compilation successful.");
		return true;
	}

	/**
	 *
	 * @return
	 */
	public int getCompilationTime() {
		return compilationTime;
	}

	/**
	 *
	 * @return
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 *
	 * @return
	 */
	public File getCompiledFile() {
		return compiledFile;
	}

	/**
	 *
	 * @return
	 */
	public String getOutput() {
		String outs = outputBuffer.toString();
		return outs;
	}

        public void SetProblemId(int problemId) { // FIXME: temporary hack for DB olymp 2013
            this.problemId=problemId;
        }
        
        public int getProblemId() { // FIXME: temporary hack for DB olymp 2013
            return problemId;
        }
}
