/*
 * World.java
 *
 * Created on 27 Октябрь 2007 г., 1:52
 *
 */

package dudge.sbots.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stswoon
 */
public class World {
	
	/**
	 * Высота мира
	 */
	private double height = 1536;
	
	/**
	 * Ширина мира
	 */
	private double weight = 2048;
	
	/**
	 * Все объекты существующие в мире
	 */
	private ArrayList<Unit> units = new ArrayList();
	private int timeOut = 10000;
	private TimeCounter time;
	
	private Player player = null;
	
	public World(Player player){
		this.player = player;
	}
	
	/**
	 * Возвращает солнце
	 */	
	public Sun getSun() {
		Sun sun = new Sun();
		for(int i = 0; i < units.size(); ++i){
			if (units.get(i) instanceof Sun){
				sun = (Sun) units.get(i);
			}
		}
		return sun;
	}
	
	/**
	 * Возвращает врагов
	 */	
	public List<Ship> getEnemies() {
		ArrayList<Ship> enemies = new ArrayList<Ship>();
		for(int i = 0; i < units.size(); ++i){
			if ((units.get(i) instanceof Ship) 
				//&& ( (Ship)units.get(i)) != null )
				)
				{
				enemies.add((Ship)units.get(i));
			}
		}
		return enemies;
	}
	
	/**
	 * Возвращает корабль игрока
	 */		
	public Ship getPlayerShip() {
		Ship ship = new Ship();
		return ship;
	}

	/**
	 * Возвращает все объекты мира
	 */		
	public List<Unit> getUnits() {
		return units;
		
	}
	
	/**
	 * Задать все объекты мира
	 */		
	public void setUnits(ArrayList units){
		this.units = units;
	}
	
}
