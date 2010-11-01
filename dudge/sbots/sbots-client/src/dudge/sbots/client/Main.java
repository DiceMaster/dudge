/*
 * Main.java
 *
 * Created on October 19, 2007, 5:02 PM

 */

package dudge.sbots.client;

import dudge.sbots.draw.SBotsFrame;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author stswoon
 */
public class Main {
    
    public Main() {		
    }
    
	/**
	 * @param args the command line arguments
	 */	
	public static void main(String args[]) {
	
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SBotsFrame().setVisible(true);
			}
		});
	}
    
}
