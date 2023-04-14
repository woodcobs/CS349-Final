/**
 * 
 */
package hangman;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;

/**
 * @author Bradley Woodcock, Thomas Mandel, Hunter Bowles
 *
 */
public class HangmanApplication extends JApplication implements ActionListener {

	private static final Color BACKGROUND_COLOR = new Color(204, 204, 255);
	private static final Color GAME_COLOR = new Color(155, 155, 250);

	public static final int WIDTH  = 1000;
	public static final int HEIGHT = 700;

	
	protected static final String EASY = "Easy";
	protected static final String MEDIUM = "Medium";
	protected static final String HARD = "Hard";
	  
	private Container cont;
	private JPanel titlePanel, gamePanel, informationPanel;
	private JFrame display;
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
	    display = new JFrame();
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
		cont.removeAll();
	    cont.revalidate();
	    cont.repaint();
	    newGame();
	}
	
	public void newGame() {
		gamePanel = new JPanel();
		gamePanel.setLayout(null);
		gamePanel.setBorder(BorderFactory.createLineBorder(GAME_COLOR, 2));
		gamePanel.setBackground(GAME_COLOR);
		
		JLabel usedLetters = new JLabel("Used Letters: ");
		usedLetters.setBounds(0, 0, WIDTH, 75);
		usedLetters.setVerticalAlignment(SwingConstants.CENTER);
		usedLetters.setHorizontalAlignment(SwingConstants.CENTER);
		usedLetters.setFont(new Font("Serif", Font.PLAIN, 25));
		usedLetters.setOpaque(true);
		usedLetters.setBackground(Color.WHITE);
		
		
		JLabel wordProgress = new JLabel(gameWord);
		wordProgress.setBounds(0, 550, WIDTH, 150);
		wordProgress.setVerticalAlignment(SwingConstants.CENTER);
		wordProgress.setHorizontalAlignment(SwingConstants.CENTER);
		wordProgress.setFont(new Font("Serif", Font.PLAIN, 50));
		wordProgress.setOpaque(true);
		wordProgress.setBackground(Color.LIGHT_GRAY);

		gamePanel.add(usedLetters);
		gamePanel.add(wordProgress);
	    cont.add(gamePanel);
	}
	

	@Override
	public void init() { 
		
		cont = getContentPane();
	    cont.setLayout(new BorderLayout(5, 10));
	    cont.setBackground(BACKGROUND_COLOR);
	    
	    titlePanel = new JPanel();
	    titlePanel.setLayout(null);
	    titlePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    titlePanel.setBackground(Color.LIGHT_GRAY);
	    
	    informationPanel = new JPanel();
	    informationPanel.setLayout(null);
	    informationPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    informationPanel.setBackground(Color.RED);
		
	    title = new JLabel("HangBuzz!");
	    title.setBounds(WIDTH/2 - 60, 30, 100, 50);
	    titlePanel.add(title);
	    
	    easyButton = new JButton(EASY);
	    easyButton.setBounds(WIDTH/2 - 100, 200, 200, 50);
	    easyButton.addActionListener(this);
	    titlePanel.add(easyButton);
	    
	    mediumButton = new JButton(MEDIUM);
	    mediumButton.setBounds(WIDTH/2 - 100, 300, 200, 50);
	    mediumButton.addActionListener(this);
	    titlePanel.add(mediumButton);
	    
	    hardButton = new JButton(HARD);
	    hardButton.setBounds(WIDTH/2 - 100, 400, 200, 50);
	    hardButton.addActionListener(this);
	    titlePanel.add(hardButton);
	    
	    
	    //JComponent hangmanComponent = getGUIComponent();
	    //hangmanComponent.setBounds(0, 0, WIDTH, HEIGHT);
	    cont.add(titlePanel);
	    
	    

	    
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