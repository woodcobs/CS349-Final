/**
 * 
 */
package hangman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.JApplication;
import gui.HangmanGame;
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
	
	protected JTextField letterField;
	private ResourceFinder jarFinder;
	private String word;
	private String gameWord;
	private List<String> words;

	/**
	 * @param args
	 * @param width
	 * @param height
	 */
	public HangmanApplication(String[] args) {
		super(args, WIDTH, HEIGHT);
	    
	    jarFinder = ResourceFinder.createInstance(new Marker());
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
	    
	    if (ac.equalsIgnoreCase(EASY)) handleGame("easy");
	    else if (ac.equalsIgnoreCase(MEDIUM)) handleGame("medium");
	    else if (ac.equalsIgnoreCase(HARD)) handleGame("hard");
	}
	
	/**
	* Handle the ABOUT button.
	*/
	protected void handleGame(final String difficulty)
	{
		String filename = difficulty + ".txt";
		InputStream is = jarFinder.findInputStream(filename);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		
		
		
		// Read the words of the difficulty
		words = new ArrayList<String>();
		String currWord = "";
		try
		{
			while ((currWord = in.readLine()) != null)
			{
				words.add(currWord);
			}
		} catch (IOException ioe)
		{
			words.add("error");
		}
		getWord();
		
		System.out.println(word);
		System.out.println(gameWord);

	}
	
	
	  

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
	    
	    
	    //JComponent hangmanComponent = getGUIComponent();
	    //hangmanComponent.setBounds(0, 0, WIDTH, HEIGHT);
	    //contentPane.add(hangmanComponent);
	}
	
	public void getWord()
	{
		Random rand = new Random();
		int rand_int = rand.nextInt(60);
		word = words.get(rand_int);
		String tempWord = "";
		// Add spaces
		
		gameWord = "";
		for (int i = 0; i < word.length(); i++)
		{
			tempWord += word.charAt(i);
			gameWord += "_";
			
			if (i < word.length())
			{
				tempWord += " ";
				gameWord += " ";
			}			
		}
		word = tempWord;
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