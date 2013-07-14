/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
	private static final Logger logger = Logger.getLogger(TokenizedComparator.class.toString());

	/**
	 * Метод сравнения вывода решения с эталонным.
	 * @param referenceOutput эталонный вывод
	 * @param solutionOutput фактический вывод решения задачи
	 * @return true если фактический вывод эквивалентен эталонному выводу
	 * (т.е. если тест пройдет), иначе false
	 */
        @Override
	public boolean compare(InputStream referenceOutput, InputStream solutionOutput) throws IOException{
		
		BufferedReader refs = new BufferedReader(new InputStreamReader(referenceOutput));
		BufferedReader sols = new BufferedReader(new InputStreamReader(solutionOutput));

		String sol;
		String ref;

		while ((sol = sols.readLine()) != null) {
			if((ref = refs.readLine()) == null) {
				return false;
			}

			if(sol.length() >0 && sol.charAt(sol.length()-1) == '\r')
				sol = sol.substring(0, sol.length() - 1);

			if(!ref.equals(sol))
			{
				logger.log(Level.INFO, "{0} || {1}", new Object[]{ref, sol});
				return false;
			}
		}

		if(refs.readLine() != null)
			return false;


		return true;
	}
}
