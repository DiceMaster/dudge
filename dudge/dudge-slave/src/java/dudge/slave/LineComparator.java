package dudge.slave;

import dudge.slave.dtest.OutputComparer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vladimir Shabanov
 */
public class LineComparator implements OutputComparer {

	private static final Logger logger = Logger.getLogger(LineComparator.class.toString());

        
        private String readLine(BufferedReader buf) throws IOException {
            String line=null;
            
            do {
                line=buf.readLine();
                if(line!=null) line=line.trim();
            } while(line!=null && line.length()==0);
            
            return line;
        }
        
	/**
	 * Метод сравнения вывода решения с эталонным.
	 *
	 * @param referenceOutput эталонный вывод
	 * @param solutionOutput фактический вывод решения задачи
	 * @return true если фактический вывод эквивалентен эталонному выводу (т.е. если тест пройдет), иначе false
	 */
	@Override
	public boolean compare(InputStream referenceOutput, InputStream solutionOutput) throws IOException {

		BufferedReader refs = new BufferedReader(new InputStreamReader(referenceOutput, "UTF-8"));
		BufferedReader sols = new BufferedReader(new InputStreamReader(solutionOutput, "UTF-8"));

		String sol;
		String ref;

		while ((sol = readLine(sols)) != null) {
			if ((ref = readLine(refs)) == null) {
				logger.log(Level.INFO, "extra line(s) in solution: [{0}]", new Object[]{sol});
				return false;
			}

			if (!ref.equals(sol)) {
				logger.log(Level.INFO, "mismatched: [{0}] vs [{1}]", new Object[]{ref, sol});
				return false;
			}
			logger.log(Level.INFO, "matched: [{0}] vs [{1}]", new Object[]{ref, sol});
		}

		if ((ref=readLine(refs)) != null) {
			logger.log(Level.INFO, "missed line(s) in solution: [{0}]", new Object[]{ref});
			return false;
		}

		return true;
	}
}
