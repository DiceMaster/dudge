/*
 * Player.java
 *
 * Created on 27 Октябрь 2007 г., 1:06
 *
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
abstract public class Player {
	
	/**
	 * Имя игрока.
	 */
	private String name;

	/**
	 * Контроль над кораблем.
	 */
	private SelfControl sc = new SelfControl(this);
	
	/**
	 * Мир.
	 */	
	private World world = new World(this);
	
	public Player() {		
	}
	
	/**
	 * Инициализация, нужно только для задания игроком начальных настроек корабля (названия) .
	 */	
	abstract public void init();
	
	/**
	 * Задание названия корабля (игрока).
	 */
	public void setName(String s){
		this.name = s;
	}
	
	/**
	 * Возвращает название корабля (игрока).
	 * @param foo фу.
	 * @return ашвыганрышпа
	 * @throws эксепшен
	 */	
	public String getName() {
		return name;
	}
	
	/**
	 * Возвращает структуру управления кораблем.
	 */	
	public SelfControl SelfControl() {
		return sc;
	}

	/**
	 * Возвращает структуру "Мир".
	 */	
	public World getWorld() {
		return world;
	}
	
	/**
	 * Функция, которую делает игрок.
	 * Отвечает за действия корабля в игре.
	 */
	abstract public void move();
}
