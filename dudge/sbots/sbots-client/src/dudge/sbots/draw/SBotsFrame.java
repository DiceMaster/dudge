/*
 * SBotsFrame.java
 *
 * Created on November 10, 2007, 2:48 PM
 */

package dudge.sbots.draw;

import dudge.sbots.model.Ship;
import dudge.sbots.model.Unit;
import dudge.sbots.model.World;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author  stswoon
 */
public class SBotsFrame extends javax.swing.JFrame {
	
	/**
	 * Creates new form SBotsFrame
	 */
	public SBotsFrame() {
		initComponents();
	}
	
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//g2.draw(new Line2D.Double(100, 100, 200, 200));				
		
		World w = new World(null);
		Ship ship1 = new Ship();
		ship1.setXY(100,100);
		Ship ship2 = new Ship();
		ship2.setXY(50,50);
		ArrayList<Unit> units = (ArrayList) w.getUnits();
		units.add(ship2);
		units.add(ship1);
		w.setUnits(units);	
		
		for(int i = 0; i < w.getUnits().size() ; ++i){
			drawUnit(w.getUnits().get(i), g2);			
		}
		
	}
	
	public void drawUnit(Unit unit, Graphics2D g2){
		if(unit instanceof Ship){
			g2.draw(new Ellipse2D.Double(unit.getX(), unit.getY(), unit.getR(), unit.getR()));
		}
	}

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Spase bots");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
		
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
