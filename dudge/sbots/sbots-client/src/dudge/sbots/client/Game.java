/*
 * Game.java
 *
 * Created on 27 Октябрь 2007 г., 1:50
 *
 */

package dudge.sbots.client;

import dudge.sbots.model.TimeCounter;
import dudge.sbots.model.World;
import javax.swing.Timer;

/**
 *
 * @author stswoon
 */
public class Game {
	
	private int timeOut = 10000;
	private TimeCounter time = new TimeCounter(timeOut);
	private World world = new World(null);
	
	public Game() {
	}
	
	public void updateWorld(){
		time.incTime();
		if (time.isFinish()){
			finish();
		}
		
	}
	
	public void start(){		
		Listener listener = new Listener(this);
		Timer timer = new Timer(10, listener);
	}
	
	public void finish(){
		System.out.println("Yahoo!");
	}
	
}
