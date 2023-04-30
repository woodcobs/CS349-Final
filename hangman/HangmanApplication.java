/**
 * 
 */
package hangman;


import java.awt.event.*;
import java.awt.*;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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

	public static final int WIDTH  = 1366;
	public static final int HEIGHT = 768;

	
	protected static final String EASY = "Easy";
	protected static final String MEDIUM = "Medium";
	protected static final String HARD = "Hard";
	  
	
	
	
	protected JTextField letterField;
	
	private int lives = 6;

	private Container cont;
    private JLabel bgLabel, guyLabel, exeIdleLabel, exeSwingLabel,  wordBoxLabel, wordBubbleLabel;
	private JPanel titlePanel, gamePanel, informationPanel;
	private JFrame display;
	private JLabel title, wordProgress, usedLetters;
	private JButton easyButton, mediumButton, hardButton;
	private ResourceFinder jarFinder;
	private String word;
	private String gameWord;
	private List<String> words;
	private List<Character> guessedLetters = new ArrayList<Character>();
	private String currDifficulty;
	private Clip correctSound, incorrectSound, backgroundSound;
	private AudioInputStream aisWin, aisLose, aisBackground;

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
		currDifficulty = difficulty;
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
		
		usedLetters = new JLabel("");
		usedLetters.setBounds(WIDTH/2 + 50, HEIGHT/2 - 100, 500, 75);
		usedLetters.setVerticalAlignment(SwingConstants.CENTER);
		usedLetters.setHorizontalAlignment(SwingConstants.CENTER);
		usedLetters.setFont(new Font("Serif", Font.PLAIN, 50));
		usedLetters.setOpaque(false);
		usedLetters.setBackground(Color.WHITE);
		
		
		wordProgress = new JLabel(gameWord);
		wordProgress.setBounds(0, 35, 1150, 150);
		wordProgress.setVerticalAlignment(SwingConstants.CENTER);
		wordProgress.setHorizontalAlignment(SwingConstants.CENTER);
		wordProgress.setFont(new Font("Serif", Font.PLAIN, 40));
		wordProgress.setOpaque(false);
		wordProgress.setBackground(Color.LIGHT_GRAY);
		
	    JTextField tfield = new JTextField(25);
	    tfield.setHorizontalAlignment(SwingConstants.RIGHT);
	    tfield.setBackground(new Color(203, 230, 245));
	    tfield.setBorder(BorderFactory.createLineBorder(new Color(203, 230, 245), 5));
	    guessedLetters.clear();
	    
	    
	    // Sampled Sound Area
	    InputStream winSound = jarFinder.findInputStream("mixkit-football-team-applause-509.wav");
	    InputStream loseSound = jarFinder.findInputStream("mixkit-wood-hard-hit-2182.wav");
	    InputStream background = jarFinder.findInputStream("Monkeys-Spinning-Monkeys.wav");
	    BufferedInputStream bis1 = new BufferedInputStream(winSound);
	    BufferedInputStream bis2 = new BufferedInputStream(loseSound);
	    BufferedInputStream bis3 = new BufferedInputStream(background);
	    
	    try {
	    	aisWin = AudioSystem.getAudioInputStream(bis1);
			aisLose = AudioSystem.getAudioInputStream(bis2);
			aisBackground = AudioSystem.getAudioInputStream(bis3);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    try {
			correctSound = AudioSystem.getClip();
			incorrectSound = AudioSystem.getClip();
			backgroundSound = AudioSystem.getClip();
			correctSound.open(aisWin);
			incorrectSound.open(aisLose);
			backgroundSound.open(aisBackground);
			
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    backgroundSound.setFramePosition(0);
	    backgroundSound.start();
	    
	    // allows for keyboard input
	    tfield.addKeyListener(new KeyAdapter()
	    {
	      public void keyTyped(KeyEvent keyevent)
	      {
	        char c = keyevent.getKeyChar();

	        if (lives == 1)
	        {
	        	System.out.println("out of lives");
	        	tfield.removeKeyListener(this);

	        	gamePanel.remove(guyLabel);
	        	gamePanel.remove(wordBubbleLabel);
			    gamePanel.remove(wordProgress);
	    	    BufferedImage guyPicture;

				try {
					guyPicture = ImageIO.read(new File("./resources/stickman_6.png"));
					guyLabel = new JLabel(new ImageIcon(guyPicture));
					guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
					gamePanel.add(guyLabel, 0);
					gamePanel.add(wordBubbleLabel, 0);
					gamePanel.add(wordProgress, 0);
					gamePanel.revalidate();
					gamePanel.repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	Object[] options = {"Menu", "Play Again"};

	        	
	        	int n = JOptionPane.showOptionDialog(cont, "The word was: " + word.replace(" ", ""), "Game Over!",
	        			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
	        			null, options, null);
	        	
	        	if (n == 1) 
	        	{
	        		// Play Again
	        		backgroundSound.stop();
	        	    handleGame(currDifficulty);
	        	} else
	        	{
	        		// Back to Menu
	        		cont.removeAll();
	        	    cont.revalidate();
	        	    cont.repaint();
	        	    backgroundSound.stop();
	        		mainMenu();
	        	}
	        	
	        	return;
	        }
	        
	        if (c >= 'a' && c <= 'z' && !guessedLetters.contains(c))
	        {
	        	System.out.println(c + " Typed!");
	        	checkLetter(c);
	        	guessedLetters.add(c);
	        	
	        	System.out.println(word);
	        	if (word.equals(gameWord))
	        	{
	        		System.out.println("YOU WIN");
		        	tfield.removeKeyListener(this);
		        	
		        	Object[] options = {"Menu", "Play Again"};

		        	
		        	int n = JOptionPane.showOptionDialog(cont, "NICE WORK", "YOU WIN!",
		        			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
		        			null, options, null);
		        	
		        	if (n == 1) 
		        	{
		        		// Play Again
		        		backgroundSound.stop();
		        	    handleGame(currDifficulty);
		        	} else
		        	{
		        		// Back to Menu
		        		backgroundSound.stop();
		        		cont.removeAll();
		        	    cont.revalidate();
		        	    cont.repaint();
		        		mainMenu();
		        	}
		        	
		        	return;
	        	}
	        	System.out.println("Lives Remaining: " + lives);
	        }
	        else
	        {
	          keyevent.consume();
	        }
	      }
	    });
	    
	    BufferedImage background1, guyPicture, executionerIdle, executionerSwing, wordBoxImage, wordBubbleImage;
		try {
			background1 = ImageIO.read(new File("./resources/scene.png"));
			guyPicture = ImageIO.read(new File("./resources/stickman_0.png"));
			wordBubbleImage = ImageIO.read(new File("./resources/wordBubble.png"));
			executionerIdle = ImageIO.read(new File("./resources/executioner_idle.png"));
			executionerSwing = ImageIO.read(new File("./resources/executioner_slash.png"));
			wordBoxImage = ImageIO.read(new File("./resources/wordBox.png"));
			
			
			ImageIcon bg = new ImageIcon(background1);
			ImageIcon guy = new ImageIcon(guyPicture);
			ImageIcon exeIdle = new ImageIcon(executionerIdle);
			ImageIcon exeSwing = new ImageIcon(executionerSwing);
			ImageIcon wordBoxIcon = new ImageIcon(wordBoxImage);
			ImageIcon wordBubbleIcon = new ImageIcon(wordBubbleImage);
			
			bgLabel = new JLabel(bg);
			guyLabel = new JLabel(guy);
			exeIdleLabel = new JLabel(exeIdle);
			exeSwingLabel = new JLabel(exeSwing);
			wordBoxLabel = new JLabel(wordBoxIcon);
			wordBubbleLabel = new JLabel(wordBubbleIcon);
			
			bgLabel.setBounds(0,0, WIDTH, HEIGHT);
			guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
			exeIdleLabel.setBounds(WIDTH / 2 - 250, HEIGHT / 2 - 275, 500, 500);
			exeSwingLabel.setBounds(WIDTH / 2 - 100, HEIGHT / 2 - 285, 500, 500);
			wordBoxLabel.setBounds(WIDTH / 2 + 100 , HEIGHT / 2 - 215, 400, 375);
			wordBubbleLabel.setBounds(WIDTH / 2 - 410 , HEIGHT / 2 - 325, 600, 300);

			gamePanel.add(usedLetters);
			gamePanel.add(wordProgress);
			gamePanel.add(wordBubbleLabel);
			gamePanel.add(exeIdleLabel);
			gamePanel.add(wordBoxLabel);
			gamePanel.add(guyLabel);
			gamePanel.add(bgLabel);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    
		gamePanel.add(tfield);
	    cont.add(gamePanel);
	    
	    lives = 6;
	    
	}
	
	public void checkLetter(char c)
	{
		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) == c)
			{
				indices.add(i);
			}
		}
		
		if (indices.isEmpty())
		{
			incorrectSound.setFramePosition(0);
			incorrectSound.start();
			
			usedLetters.setText(usedLetters.getText() + " " + c);
			lives--;
		    BufferedImage guyPicture;
		    
		    int currGuy = 6 - lives;
		    if (currGuy == 0)
		    {
			    gamePanel.remove(guyLabel);
			    gamePanel.remove(wordBubbleLabel);
			    gamePanel.remove(wordProgress);
		    }
			try {
				guyPicture = ImageIO.read(new File("./resources/stickman_" + currGuy + ".png"));
				guyLabel = new JLabel(new ImageIcon(guyPicture));
				guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
				gamePanel.add(guyLabel, 0);
				gamePanel.add(wordBubbleLabel, 0);
				gamePanel.add(wordProgress, 0);
				gamePanel.revalidate();
				gamePanel.repaint();
				new Thread(new Runnable() {
				     @Override
				     public void run() {
				          JLabel currLabel = exeIdleLabel;
				          Boolean h = true;
				          for (int i = 1; i < 3; i++)
				          {
				        	  try {
								gamePanel.remove(currLabel);
								gamePanel.revalidate();
								gamePanel.repaint();
								if (h)
								{
									h = !h;
									currLabel = exeSwingLabel;
								} else
								{
									h = !h;
									currLabel = exeIdleLabel;
								}
								gamePanel.add(currLabel, 0);
								gamePanel.revalidate();
								gamePanel.repaint();
								Thread.sleep(500);
								
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				          }
				     }
				}).start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else
		{
			correctSound.setFramePosition(0);
			correctSound.start();
			
			for (int index : indices)
			{
				gameWord = gameWord.substring(0, index) + c
	              + gameWord.substring(index + 1);
				wordProgress.setText(gameWord);
			}
		}
	}
	
	
	public void mainMenu()
	{
		
		
			
		titlePanel = new JPanel();
	    titlePanel.setLayout(null);
	    titlePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    titlePanel.setBackground(Color.LIGHT_GRAY);
	    
	    
	    informationPanel = new JPanel();
	    informationPanel.setLayout(null);
	    informationPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    informationPanel.setBackground(Color.RED);
		
	    title = new JLabel("Hangman!");
	    title.setBounds(0, 25, WIDTH, 100);
	    title.setHorizontalAlignment(SwingConstants.CENTER);
	    title.setFont(new Font("Serif", Font.PLAIN, 50));
	    titlePanel.add(title);
	    
	    easyButton = new JButton(EASY);
	    easyButton.setBounds(WIDTH/2 - 100, 200, 200, 50);
	    easyButton.setFont(new Font("Serif", Font.PLAIN, 20));
	    easyButton.addActionListener(this);
	    titlePanel.add(easyButton);
	    
	    mediumButton = new JButton(MEDIUM);
	    mediumButton.setBounds(WIDTH/2 - 100, 300, 200, 50);
	    mediumButton.setFont(new Font("Serif", Font.PLAIN, 20));
	    mediumButton.addActionListener(this);
	    titlePanel.add(mediumButton);
	    
	    hardButton = new JButton(HARD);
	    hardButton.setBounds(WIDTH/2 - 100, 400, 200, 50);
	    hardButton.setFont(new Font("Serif", Font.PLAIN, 20));
	    hardButton.addActionListener(this);
	    titlePanel.add(hardButton);
	    
	    
	    //JComponent hangmanComponent = getGUIComponent();
	    //hangmanComponent.setBounds(0, 0, WIDTH, HEIGHT);
	    BufferedImage background;
		try {
			background = ImageIO.read(new File("./resources/sceneBlur.png"));
			ImageIcon bg = new ImageIcon(background);
			bgLabel = new JLabel(bg);
			bgLabel.setBounds(0,0, WIDTH, HEIGHT);
			titlePanel.add(bgLabel, -1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    cont.add(titlePanel);
	}

	@Override
	public void init() { 
		
		cont = getContentPane();
	    cont.setLayout(new BorderLayout(5, 10));
	    cont.setBackground(BACKGROUND_COLOR);
	    mainMenu();
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