/*
 * Listener.java
 *
 * Created on November 10, 2007, 4:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.sbots.client;

import dudge.sbots.model.World;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author stswoon
 */
public class Listener implements ActionListener{
	
	private Game game;
	
	public Listener(Game game) {
		this.game = game;
	}

	public void actionPerformed(ActionEvent e) {
		game.updateWorld();
	}
	
}
