/**
 * 
 */
package hangman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import app.JApplication;

/**
 * @author Bradley Woodcock, Thomas Mandel, Hunter Bowles
 *
 */
public abstract class HangmanApplication extends JApplication implements ActionListener {

	
	public static final int WIDTH  = 1000;
	public static final int HEIGHT = 800;

	protected static final String ABOUT = "About";
	protected static final String LOAD = "Load";
	  
	private JButton aboutButton, loadButton;
	private JTextField fileField;
	private String aboutText;
	/**
	 * @param width
	 * @param height
	 */
	public HangmanApplication(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @param width
	 * @param height
	 */
	public HangmanApplication(String[] args, int width, int height) {
		super(args, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
