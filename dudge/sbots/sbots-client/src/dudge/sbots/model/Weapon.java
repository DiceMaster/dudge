/*
 * Weapon.java
 *
 * Created on 27 Октябрь 2007 г., 1:39
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
abstract public class Weapon extends Unit{

	/**
	 * Время до самоуничтожения оружия
	 */	
	private TimeCounter time;
	
	/**
	 * Время жизни оружия
	 */	
	private int timeOut;
	
	public Weapon(Player player) {		
	}
	
	/**
	 * Нанесение урона
	 */	
	abstract public void useShipForDamage(Ship ship);
	
}
