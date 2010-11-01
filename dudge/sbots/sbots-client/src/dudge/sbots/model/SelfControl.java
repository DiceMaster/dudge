/*
 * SelfControl.java
 *
 * Created on 27 Октябрь 2007 г., 1:14
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class SelfControl extends Ship{
	
	private Player player = null;
	
	public SelfControl(Player player){
		this.player = player;
	}
	
	/**
	 * Отклониться на заданный угол
	 */
	public void turnOn(double angle) {
		if(angle > super.getMaxAngle()) {
			super.setAngle(super.getAngle() + (angle / Math.abs(angle))* super.getMaxAngle());
		} else {
			super.setAngle(super.getAngle() + angle);
		}
	}
	
	/**
	 * Повернуться на заданный угол
	 */	
	public void turnTo(double angle) {
		if(Math.abs(angle - super.getAngle()) > super.getMaxAngle()) {
			if (Math.abs(super.getAngle()) - Math.abs(angle) > 0){
				super.setAngle(super.getAngle() + super.getMaxAngle());
			} else {
				super.setAngle(super.getAngle() - super.getMaxAngle());				
			}
		} else {
			super.setAngle(angle);
		}
	}
	
	/**
	 * Повернуться на объект
	 */	
	public void turnTo(Unit unit){
		this.turnTo(unit.getX(), unit.getY());
	}
	
	/**
	 * Повернуть на точку
	 */	
	public void turnTo(double x, double y) {
		double angle = super.getAngleBetween(this.getX(), this.getY(), x, y);
		this.turnTo(angle);		
	}
	
	/**
	 * Задать мощность движка, если мощность уменьшилась, 
	 * то энергия выкачивается в щит.
	 */	
	public void setPowerEngine(int power) {
		super.setEngine(Math.min(power, super.getMaxEngine()));
	}
	
	/**
	 * Задать мощность лазера, то энергия выкачивается
	 * в щит. 
	 */	
	public void setPowerLaser(int power) {
		super.setLaser(Math.min(power, super.getMaxLaser()));
	}
	
	/**
	 * Выстрел оружием
	 */	
	private void fire(Weapon weapon){
		
	}
	
	/**
	 * Выстрел лазером
	 */
	public void fireLaser() {
		//Weapon weapon = new Laser();
		//weapon.setSpeed(this.get);
		
	}
	
	/**
	 * выстрел ракетой
	 */	
	public void fireRocket(){
		
	}
	
}
