/*
 * TokenizedComparator.java
 *
 * Created on June 3, 2007, 10:36 PM
 */

package dudge.slave;

import dudge.slave.dtest.OutputComparer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс компаратора, сравнивающего текстовые потоки по строкам и токенам
 * в них.
 * @author Vladimir Shabanov
 */
public class TokenizedComparator implements OutputComparer
{
	private static final Logger logger = Logger.getLogger(TokenizedComparator.class.toString());
	
	/**
	 * Метод сравнения вывода решения с эталонным.
	 * @param referenceOutput эталонный вывод
	 * @param solutionOutput фактический вывод решения задачи
	 * @return true если фактический вывод эквивалентен эталонному выводу
	 * (т.е. если тест пройдет), иначе false
	 */
	public boolean compare(InputStream referenceOutput, InputStream solutionOutput) throws IOException{
		BufferedReader sols = new BufferedReader(new InputStreamReader(solutionOutput));
		BufferedReader etas = new BufferedReader(new InputStreamReader(referenceOutput));
		
		String sol;
		String eta;
		
		while ((sol = sols.readLine()) != null) {
			if((eta = etas.readLine()) == null) {
				return false;
			}

			StringTokenizer solTokenizer = new StringTokenizer(sol, " ");
			StringTokenizer etaTokenizer = new StringTokenizer(eta, " ");

			if (solTokenizer.countTokens() < etaTokenizer.countTokens())
				return false;

			if (solTokenizer.countTokens() > etaTokenizer.countTokens())
				return false;

			while (solTokenizer.hasMoreTokens())
				if (!solTokenizer.nextElement().equals(etaTokenizer.nextElement()))
					return false;
		}

		if(etas.readLine() != null)
			return false;
		
		return true;
	}
}

