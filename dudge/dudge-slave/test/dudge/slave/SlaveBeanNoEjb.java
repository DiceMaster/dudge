/*
 * SlaveBeanNoEjb.java
 *
 * Created on October 18, 2007, 7:13 PM
 */
package dudge.slave;

import dudge.db.Solution;

/**
 * Класс-наследник SlaveBean'а, в котором отключено общение с сервером и перегружен метод saveSolution() на пустой.
 *
 * @author Vladimir Shabanov
 */
public class SlaveBeanNoEjb extends SlaveBean {

	private Solution savedSolution = null;

	/**
	 * Creates a new instance of SlaveBeanNoEjb
	 */
	public SlaveBeanNoEjb() {
		super();
	}

	@Override
	protected void saveSolution(Solution solution) {
		try {
			savedSolution = (Solution) solution.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Возвращает сохраненное решение.
	 *
	 * @return сохраненное рабом решение.
	 */
	public Solution getSavedSolution() {
		return savedSolution;
	}

	@Override
	public void testSolution(Solution solution) throws SlaveException {
		this.saveSolution(solution);
		super.testSolution(solution);
	}
}
