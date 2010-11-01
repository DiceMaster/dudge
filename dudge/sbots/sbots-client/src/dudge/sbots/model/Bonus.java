/*
 * Bonus.java
 *
 * Created on 27 Октябрь 2007 г., 0:47
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Bonus extends Unit {
	
	/**
	 * Время жизни бонуса
	 */
	private int timeOut = 1000;

	/**
	 * Счетчик времени, после истечения 
	 * которого  он изчезает
	 */
	private TimeCounter time;

	/**
	 * Радиус
	 */
	private double ownR = 10;
	
	/**
	 * Количество ракет в бонусе
	 */	
	private int rocketCount = 1; 

	public Bonus() {
		this.setR(ownR);
	}

	/**
	 * Увеличение ракет на корабле,
	 * который подобрал бонус 
	 */
	public void useShip(Ship ship){
		ship.setRocketCount(ship.getRocketCount() + rocketCount);
	}

}
