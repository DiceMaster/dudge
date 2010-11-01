/*
 * Laser.java
 *
 * Created on 27 Октябрь 2007 г., 1:42
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Laser extends Weapon{

	/**
	 * Урон наносимый лазером, зависит от уровня заряда.
	 * Наносится по щиту, если щита нет, то по броне.
	 */
	private int damage = 0; 
	
	/**
	 * Собственная скорость, т.е. скорость
	 * при выстреле со стоячего корабля,
	 * у лазера относительная и своя скорость
	 * одинакова
	 */
	private double ownSpeed = 30;
	
	/**
	 * Радиус
	 */
	private double ownR = 10;
	
	/**
	 * Время жизни
	 */	
	private int timeOut = 200;

	public Laser(Player player) {
		super(player);
		this.setR(ownR);
		this.setSpeed(ownSpeed);
	}
	
	/**
	 * Получить урон
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Задать урон
	 */	
	public void setDamage(int damage){
		this.damage = damage;
	}

	/**
	 * Нанести урон по щиту или броне
	 */
	public void useShipForDamage(Ship ship) {
		ship.setShield(ship.getShield() - damage);
		if(ship.getShield() < 0){
			ship.setLife(ship.getLife() + ship.getShield());
			ship.setShield(0);
		}
	}
	
}
