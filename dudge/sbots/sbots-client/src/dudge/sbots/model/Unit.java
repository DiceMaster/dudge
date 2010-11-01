/*
 * Unit.java
 *
 * Created on 20 Октябрь 2007 г., 22:05
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class Unit {
	
	/**
	 * Координата Х в мире.
	 */	
	private double x; 
	
	/**
	 * Координата Y в мире.
	 */	
	private double y; 
	
	/**
	 * Угол на который направлен вектор объекта
	 */		
	private double angle;
	
	/**
	 * Абсолютная скорост
	 */		
	private double speed; 
	
	/**
	 * Радиус объекта
	 */		
	private double r;
	
	/**
	 * Угол вектора скорости
	 */		
	private double speedAngle;

	public Unit() {
	}

	/**
	 * Задать угол скорости
	 */		
	public void setSpeedAngel(double angel){
		this.speedAngle = angel;
	}

	/**
	 * Получить угол скорости
	 */	
	public double getSpeedAngel() {
		return this.speedAngle;
	}
	
	/**
	 * Задать радиус
	 */		
	public void setR(double r){
		this.r = r;
	}
	
	/**
	 * Задать X и Y
	 */		
	public void setXY(double x, double y) {
		this.setX(x);
		this.setY(y);
	}	

	/**
	 * Получить радиус
	 */		
	public double getR() {
		return r;
	}
	
	/**
	 * Задать X
	 */		
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Плучить X
	 */		
	public double getX() {
		return x;
	}
	
	/**
	 * Задать Y
	 */		
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Получить Y
	 */		
	public double getY() {
		return y;
	}
	
	/**
	 * Задать угол
	 */		
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Получить угол
	 */		
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Задать скорость
	 */		
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/**
	 * Получить скорость
	 */	
	public double getSpeed() {
		return speed;
	}
	
	/**
	 * Расстояние до объекта
	 */		
	public double getDistance(Unit unit) {
		return getDistance(unit.getX(), unit.getY());
	}
	
	/**
	 * Расстояние до точки
	 */		
	public double getDistance(double x, double y) {
		return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
	}
	
	/**
	 * Угол на котором находится объект.
	 * Не учитывет направленность объекта,
	 * (т.е. не зависит от направления куда смотрит нос корабля)
	 */		
	public double getUnitAngle(Unit unit) {
		return this.getUnitAngle(unit.x, unit.y);
	}
	
	/**
	 * Угол на котором находится точка
	 */	
	public double getUnitAngle(double x, double y) {
		return Math.atan2(y, x) + Math.PI;
	}
	
	/**
	 * Угол между двумя объектами
	 */		
	public double getAngleBetween(Unit unit1, Unit unit2) {
		return this.getAngleBetween(unit1.x, unit1.y, unit2.x, unit2.y);
	}
	
	/**
	 * Угол между двумя точками
	 */		
	public double getAngleBetween(double x1, double y1, double x2, double y2) {
		return this.getUnitAngle(x1, y1) - this.getUnitAngle(x2, y2);
	}
	
	/**
	 * Получить горизонтальную скорость
	 */	
	public double getHorizontalSpeed() {
		return this.speed * Math.cos(this.angle);
	}
	
	/**
	 * Получить вертикальную скорость
	 */		
	public double getVerticalSpeed(){
		return this.speed * Math.sin(this.angle);
	}	
	
}
