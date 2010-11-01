/*
 * Rocket.java
 *
 * Created on 27 Октябрь 2007 г., 1:47
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Rocket extends Weapon{

	/**
	 * Урон наносимый ракетой (по броне)
	 */	
	private int damage = 15;
	
	/**
	 * Собственная скорость. Скорость относительно
	 * солнца зависит от скорости корабля.
	 */	
	private double ownSpeed = 10;
	
	/**
	 * Радиус
	 */
	private double ownR = 10;
	
	/**
	 * Время жизни
	 */	
	private int timeOut = 300;

	public Rocket(Player player) {
		super(player);
		super.setR(ownR);
		super.setSpeed(ownSpeed);
	}

	/**
	 * Нанести урон по броне
	 */
	public void useShipForDamage(Ship ship) {
		ship.setLife(ship.getLife() - damage);
	}
	
}
