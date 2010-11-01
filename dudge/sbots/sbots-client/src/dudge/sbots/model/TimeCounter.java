/*
 * Time.java
 *
 * Created on 21 Октябрь 2007 г., 21:55
 */

package dudge.sbots.model;

/**
 *
 * @author stswoon
 */
public class TimeCounter {
	
	/**
	 * Оставщееся время, измеряется в тиках
	 */		
	private int time = 0;
	
	/**
	 * Создание счетчика с нулевым временем
	 */		
	public TimeCounter() {
	}

	/**
	 * Создание счетчика с заданным временем
	 */		
	public TimeCounter(int time) {
		this.setTime(time);
	}
	
	/**
	 * Задание времени
	 */		
	public void setTime(int time){
		this.time = time;
	}
	
	/**
	 * Получить время
	 */		
	public int getTime() {
		return time;
	}
	
	/**
	 * Увеличить время на один тик
	 */		
	public void incTime() {
		time++;
	}
	
	/**
	 * Уменьшить время на один тик
	 */		
	public void decTime(){
		time--;
		if(time < 0){
			time = 0;
		}
	}
	
	/**
	 * True, если время истекло.
	 */
	public boolean isFinish(){
		return (time <= 0);
	}
	
}
