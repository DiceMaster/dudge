/*
 * Sun.java
 *
 * Created on October 22, 2007, 2:42 PM
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Sun extends Unit {
	
	/**
	 * Собственный радиус
	 */		
	private double ownR = 50;
	
	/**
	 * Собственная атака
	 */		
	private double ownDamage = 1;
	
	/**
	 * Масса, влияет на притяжение (вместо G в физической формуле) 
	 */		
	private double m; 

	/**
	 * Создание солнца, ownR записывается в Unir.r
	 */		
	public Sun() {
		this.setR(ownR);
	}
	
	/**
	 * Получить массу
	 */		
	public double getM() {
		return m;
	}
	
	/**
	 * Задать массу
	 */			
	public void setM(double m) {
		this.m = m;
	}
	
	/**
	 * Возвращет урон, который наносится
	 * кораблю на расстоянии от солнца
	 */			
	public int getDistanceDamage(Ship ship) {
		return this.getDistanceDamage(ship.getDistance(this));
	}
		
	/**
	 * Возвращет урон, который наносится
	 * на данном расстоянии от солнца
	 */			
	public int getDistanceDamage(double distance) {
		double x = this.ownDamage * distance / this.ownR;
		if(x > Math.pow(10, 9)){
			x = Math.pow(10, 9);
		}
		return (int)Math.round(x);
	}

	/**
	 * Наносит урон броне корабля
	 */
	public void useShipForDamage(Ship ship) {
		ship.setLife(ship.getLife() - this.getDistanceDamage(ship));	
	}
	
	/**
	 * Притяжение. Меняет вектор скорости объекта,
	 * в зависимости от его положения в мире. Солнце - центр мира.
	 */	
	public void useUnitForAttraction(Unit unit){
		double dV = (m / Math.pow(unit.getDistance(this) , 2)) * 1; // dt = 1 тик
		double b = Math.atan2(Math.abs(unit.getX()), Math.abs(unit.getY())) + Math.PI;
		
		double Vx = unit.getHorizontalSpeed() + dV * Math.cos(b);
		double Vy = unit.getVerticalSpeed() + dV * Math.sin(b);
		double V = Math.pow(Vx, 2);
		V += Math.pow(Vy, 2);
		V = Math.sqrt(V);
		
		unit.setSpeed(V);
		unit.setSpeedAngel(Math.atan2(Vy, Vx) + Math.PI);
	}
}
