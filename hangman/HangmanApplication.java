/**
 * 
 */
package hangman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;

/**
 * @author Bradley Woodcock, Thomas Mandel, Hunter Bowles
 *
 */
public class HangmanApplication extends JApplication implements ActionListener {

	
	public static final int WIDTH  = 1000;
	public static final int HEIGHT = 700;

	
	protected static final String EASY = "Easy";
	protected static final String MEDIUM = "Medium";
	protected static final String HARD = "Hard";
	  
	private JLabel title;
	private JButton easyButton, mediumButton, hardButton;
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
	public HangmanApplication(String[] args) {
		super(args, WIDTH, HEIGHT);
	    
	    ResourceFinder rf = ResourceFinder.createInstance(new Marker());
	}

	/**
	 * Handle actionPerformed messages (required by ActionListener).
	 * In particular, get the input, perform the requested conversion,
	 * and display the result.
	 * 
	 * @param evt  The ActionEvent that generated the actionPerformed message
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		String ac = evt.getActionCommand();
	    
	    if (ac.equalsIgnoreCase(EASY)) handleGame(EASY);
	    else if (ac.equalsIgnoreCase(MEDIUM)) handleGame(MEDIUM);
	    else if (ac.equalsIgnoreCase(HARD)) handleGame(HARD);
	}
	
	/**
	* Handle the ABOUT button.
	*/
	protected void handleGame(final String difficulty)
	{
		//JOptionPane.showMessageDialog(getGUIComponent(), 
		//difficulty, difficulty, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	* Get the GUI components to use to display the weather information.
	* 
	* @return The WeatherObserverPanel
	*/
	//protected abstract JComponent getGUIComponent();
	 

	@Override
	public void init() {
		// TODO Auto-generated method stub
		 // Setup the content pane
	    JPanel contentPane = (JPanel)getContentPane();
	    contentPane.setLayout(null);

	    title = new JLabel("HangBuzz!");
	    title.setBounds(WIDTH/2 - 60, 30, 100, 50);
	    contentPane.add(title);
	    
	    easyButton = new JButton(EASY);
	    easyButton.setBounds(WIDTH/2 - 100, 200, 200, 50);
	    easyButton.addActionListener(this);
	    contentPane.add(easyButton);
	    
	    mediumButton = new JButton(MEDIUM);
	    mediumButton.setBounds(WIDTH/2 - 100, 300, 200, 50);
	    mediumButton.addActionListener(this);
	    contentPane.add(mediumButton);
	    
	    hardButton = new JButton(HARD);
	    hardButton.setBounds(WIDTH/2 - 100, 400, 200, 50);
	    hardButton.addActionListener(this);
	    contentPane.add(hardButton);
	    
	    
	    //JComponent weatherObserverComponent = getGUIComponent();
	    //weatherObserverComponent.setBounds(0, 60, WIDTH, HEIGHT-60);
	    //contentPane.add(weatherObserverComponent);
	}

	  /**
	  * Construct and invoke  (in the event dispatch thread) 
	  * an instance of this JApplication.
	  * 
	  * @param args The command line arguments
	  */
	  public static void main(final String[] args)
	  {
	    JApplication app = new HangmanApplication(args);
	    invokeInEventDispatchThread(app);
	  }
}
