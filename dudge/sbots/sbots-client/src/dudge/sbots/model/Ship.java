/*
 * Ship.java
 *
 * Created on October 22, 2007, 3:05 PM
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Ship extends Unit{

	/**
	 * Собственный радиус корабля.
	 */		
	private double ownR = 30; 
	
	/**
	 * Количество рокет на корабле.
	 */		
	private int rocketCount = 0;
	
	/**
	 * Броня
	 */		
	private int life = 100;
	
	/**
	 * Время перезарядки ракеты
	 */	
	private int rocketTimeOut = 200;
	
	/**
	 * Время перерождения
	 */
	private int respawnTimeOut = 600;
	
	/**
	 *  Время перезарядки лазера
	 */	
	private int laserTimeOut = 50;
	
	/**
	 * Время оставшееся до заряда ракеты
	 */		
	private TimeCounter rocketTime = new TimeCounter();
	
	/**
	 * Время оставшееся до заряда лазера
	 */	
	private TimeCounter laserTime = new TimeCounter();
	
	/**
	 * Время оставшееся до возрождения
	 */		
	private TimeCounter respawnTime = new TimeCounter();
	
	/**
	 * Максимальный угол поворота за один тик
	 */	
	private double maxAngle = 0.3;
	
	/**
	 * Максимальная скорость
	 */	
	private double maxSpeed = 10;
	
	/**
	 * Количество регенерируемой энергии за один тик 
	 */	
	private int energyRegenerate = 1;
	
	/**
	 * Максимальная энергия хранящаяся на корабле,
	 * т.е. сумма по щиту, лазеру, движку.
	 */	
	private int maxEnergy = 100;
	
	/**
	 * Максимальный заряд лазера
	 */		
	private int maxLaser = 30;
	
	/**
	 * Максимальный заряд щита. 
	 */		
	private int maxShield = 100; 
	
	/**
	 * Максимальная мощность двигателя
	 */		
	private int maxEngine = 10;
	
	/**
	 * Реальный заряд лазера
	 */			
	private int laser = 0;
	
	/**
	 * Реальный заряд щита.
	 * Вся не рассходуемая энергия находится в
	 * щите. Либо переводиться туда
	 */			
	private int shield = maxEnergy;
	
	/**
	 * Реальный заряд движка
	 */		
	private int engine = 0;
	
	public Ship() {
		super.setR(ownR);
	}
	
	/**
	 * Получить максимальный угол поворота
	 */		
	public double getMaxAngle(){
		return this.maxAngle;
	}

	/**
	 * Получить максимальную мощность двигателя
	 */			
	public int getMaxEngine(){
		return this.maxEngine;
	}

	/**
	 * Получить максимальную мощность лазера
	 */			
	public int getMaxLaser(){
		return this.maxLaser;
	}

	/**
	 * Получиь максимальную мощность щита
	 */			
	public int getMaxShield(){
		return this.maxShield;
	}
	
	/**
	 * Задать мощность двигателя,
	 * уменьшая мощность щита.
	 */			
	public void setEngine(int power) {
		power = Math.min(this.maxEngine, power);
		power = Math.min(power, this.getShield());
		this.engine = power;
		this.setShield(this.getShield() - power);		
	}
	
	/**
	 * Задать мощность лазера,
	 * уменьшая мощность щита.
	 */		
	public void setLaser(int power) {
		power = Math.min(this.maxLaser, power);
		power = Math.min(power, this.getShield());
		this.laser = power;
		this.setShield(this.getShield() - power);
	}
	
	/**
	 * Задать мощность щита.
	 */			
	public void setShield(int power) {
		this.shield = power;
	}
	
	/**
	 * Получить мощность двигателя
	 */			
	public int getEngine() {
		return engine;
	}

	/**
	 * Получить мощность лазера
	 */			
	public int getLaser() {
		return this.laser;
	}
	
	/**
	 * Получиь мощность щита
	 */			
	public int getShield() {
		return shield;
	}
	
	/**
	 * Задать броню (количество жизней)
	 */			
	public void setLife(int life) {
		this.life = life;
	}
	
	/**
	 * Получить броню (количество жизней)
	 */			
	public int getLife() {
		return life;
	}
	
	/**
	 * Задать количество ракет
	 */			
	public void setRocketCount(int n) {
		this.rocketCount = n;
	}

	/**
	 * Получить количество ракет
	 */			
	public int getRocketCount() {
		return rocketCount;
	}
	
}
