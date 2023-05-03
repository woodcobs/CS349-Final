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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;
import visual.ScaledVisualizationRenderer;
import visual.VisualizationView;
import visual.dynamic.sampled.Screen;
import visual.statik.SimpleContent;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;

/**
 * @author Bradley Woodcock, Thomas Mandel, Hunter Bowles
 *
 */
public class HangmanApplication extends JApplication implements ActionListener {

	private static final Color BACKGROUND_COLOR = new Color(204, 204, 255);
	private static final Color GAME_COLOR = new Color(155, 155, 250);

	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;

	protected static final String EASY = "Easy";
	protected static final String MEDIUM = "Medium";
	protected static final String HARD = "Hard";
	protected static final String CUSTOM = "Custom";

	protected JTextField letterField;

	private int lives = 6;

	private Container cont;
	private JLabel bgLabel, guyLabel, sunLabel, exeIdleLabel, exeSwingLabel, wordBoxLabel, wordBubbleLabel;
	private JPanel titlePanel, gamePanel, informationPanel;
	private JLabel wordProgress, usedLetters, crowd1Label, crowd2Label, nameLabel;
	private JButton easyButton, mediumButton, hardButton, customButton;
	private ResourceFinder jarFinder;
	private String word;
	private String gameWord;
	private List<String> words;
	private List<Character> guessedLetters = new ArrayList<Character>();
	private String currDifficulty;
	private String name;
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
	}

	/**
	 * Handle actionPerformed messages (required by ActionListener). In particular,
	 * get the input, perform the requested conversion, and display the result.
	 * 
	 * @param evt The ActionEvent that generated the actionPerformed message
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		String ac = evt.getActionCommand();

		if (ac.equalsIgnoreCase(EASY))
			handleGame("easy");
		else if (ac.equalsIgnoreCase(MEDIUM))
			handleGame("medium");
		else if (ac.equalsIgnoreCase(HARD))
			handleGame("hard");
		else if (ac.equalsIgnoreCase(CUSTOM))
			handleGame("custom");
	}

	/**
	 * Handle the ABOUT button.
	 */
	protected void handleGame(final String difficulty) {
		if (difficulty.equals("custom")) {
			String text = JOptionPane.showInputDialog("Enter a word:");
			System.out.println(text);
			word = "";
			gameWord = "";
			currDifficulty = difficulty;

			for (int i = 0; i < text.length(); i++) {
				word += text.charAt(i);
				gameWord += "_";

				if (i < word.length()) {
					word += " ";
					gameWord += " ";
				}
			}
		} else {
			name = JOptionPane.showInputDialog("Enter your name:");
			currDifficulty = difficulty;
			String filename = difficulty + ".txt";
			InputStream is = jarFinder.findInputStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));

			// Read the words of the difficulty
			words = new ArrayList<String>();
			String currWord = "";
			try {
				while ((currWord = in.readLine()) != null) {
					words.add(currWord);
				}
			} catch (IOException ioe) {
				words.add("error");
			}
			getWord();
		}

		System.out.println(word);
		System.out.println(gameWord);

		cont.removeAll();
		cont.revalidate();
		cont.repaint();
		newGame();
	}

	public void newGame() {
		ResourceFinder rf = ResourceFinder.createInstance(new Marker());
		gamePanel = new JPanel();
		gamePanel.setLayout(null);
		gamePanel.setBorder(BorderFactory.createLineBorder(GAME_COLOR, 2));
		gamePanel.setBackground(GAME_COLOR);

		usedLetters = new JLabel("");
		usedLetters.setBounds(WIDTH / 2 + 50, HEIGHT / 2 - 100, 500, 75);
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
		InputStream background = jarFinder.findInputStream("monkeyMusic2.wav");
		BufferedInputStream bis1 = new BufferedInputStream(winSound);
		BufferedInputStream bis2 = new BufferedInputStream(loseSound);
		BufferedInputStream bis3 = new BufferedInputStream(background);

		try {
			aisWin = AudioSystem.getAudioInputStream(new BufferedInputStream(bis1));
			aisLose = AudioSystem.getAudioInputStream(new BufferedInputStream(bis2));
			aisBackground = AudioSystem.getAudioInputStream(new BufferedInputStream(bis3));
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
		tfield.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent keyevent) {
				char c = keyevent.getKeyChar();

				if (lives == 1) {
					System.out.println("out of lives");
					tfield.removeKeyListener(this);

					gamePanel.remove(guyLabel);
					gamePanel.remove(wordBubbleLabel);
					gamePanel.remove(wordProgress);
					gamePanel.remove(usedLetters);
					gamePanel.remove(wordBoxLabel);
					gamePanel.remove(sunLabel);
					BufferedImage guyPicture, sunPicture;

					ImageFactory factory = new ImageFactory(rf);

					guyPicture = factory.createBufferedImage("stickman_6.png", 4);
					sunPicture = factory.createBufferedImage("sun6.png", 4);

					guyLabel = new JLabel(new ImageIcon(guyPicture));
					guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
					sunLabel = new JLabel(new ImageIcon(sunPicture));
					sunLabel.setBounds(0, 0, WIDTH, HEIGHT);

					gamePanel.add(guyLabel, 0);
					gamePanel.add(wordBubbleLabel, 0);
					gamePanel.add(wordProgress, 0);
					gamePanel.add(sunLabel, 0);
					gamePanel.add(wordBoxLabel, 0);
					gamePanel.add(usedLetters, 0);
					gamePanel.revalidate();
					gamePanel.repaint();

					Object[] options = { "Menu", "Play Again" };

					String loseText = "You Died!";
					if (name != null) {
						loseText = name + " Has Died!";
					}
					int n = JOptionPane.showOptionDialog(cont, "The word was: " + word.replace(" ", ""), loseText,
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);

					if (n == 1) {
						// Play Again
						backgroundSound.stop();
						handleGame(currDifficulty);
					} else {
						// Back to Menu
						cont.removeAll();
						cont.revalidate();
						cont.repaint();
						backgroundSound.stop();
						mainMenu();
					}

					return;
				}

				if (c >= 'a' && c <= 'z' && !guessedLetters.contains(c)) {
					System.out.println(c + " Typed!");
					checkLetter(c);
					guessedLetters.add(c);

					System.out.println(word);
					if (word.equals(gameWord)) {
						System.out.println("YOU WIN");
						tfield.removeKeyListener(this);

						Object[] options = { "Menu", "Play Again" };

						String winText = "YOU SURVIVED!";
						if (name != null) {
							winText = name.toUpperCase() + " HAS SURVIVED!";
						}

						int n = JOptionPane.showOptionDialog(cont, "NICE WORK", winText, JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, options, null);

						if (n == 1) {
							// Play Again
							backgroundSound.stop();
							handleGame(currDifficulty);
						} else {
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
				} else {
					keyevent.consume();
				}
			}
		});

		BufferedImage background1, guyPicture, sunPicture, executionerIdle, executionerSwing, wordBoxImage,
				wordBubbleImage;
		ImageFactory factory = new ImageFactory(rf);
		background1 = factory.createBufferedImage("scene.png", 4);
		guyPicture = factory.createBufferedImage("stickman_0.png", 4);
		sunPicture = factory.createBufferedImage("sun0.png", 4);

		wordBubbleImage = factory.createBufferedImage("wordBubble.png", 4);
		executionerIdle = factory.createBufferedImage("executioner_idle.png", 4);
		executionerSwing = factory.createBufferedImage("executioner_slash.png", 4);
		wordBoxImage = factory.createBufferedImage("wordBox.png", 4);
		BufferedImage crowd1Image = factory.createBufferedImage("crows1.png", 4);
		BufferedImage crowd2Image = factory.createBufferedImage("crowd2.png", 4);

		ImageIcon bg = new ImageIcon(background1);
		ImageIcon guy = new ImageIcon(guyPicture);
		ImageIcon sunIcon = new ImageIcon(sunPicture);
		ImageIcon exeIdle = new ImageIcon(executionerIdle);
		ImageIcon exeSwing = new ImageIcon(executionerSwing);
		ImageIcon wordBoxIcon = new ImageIcon(wordBoxImage);
		ImageIcon wordBubbleIcon = new ImageIcon(wordBubbleImage);
		ImageIcon crowd1Icon = new ImageIcon(crowd1Image);
		ImageIcon crowd2Icon = new ImageIcon(crowd2Image);

		bgLabel = new JLabel(bg);
		guyLabel = new JLabel(guy);
		exeIdleLabel = new JLabel(exeIdle);
		exeSwingLabel = new JLabel(exeSwing);
		wordBoxLabel = new JLabel(wordBoxIcon);
		wordBubbleLabel = new JLabel(wordBubbleIcon);
		crowd1Label = new JLabel(crowd1Icon);
		crowd2Label = new JLabel(crowd2Icon);
		sunLabel = new JLabel(sunIcon);

		bgLabel.setBounds(0, 0, WIDTH, HEIGHT);
		guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
		exeIdleLabel.setBounds(WIDTH / 2 - 250, HEIGHT / 2 - 275, 500, 500);
		exeSwingLabel.setBounds(WIDTH / 2 - 100, HEIGHT / 2 - 285, 500, 500);
		wordBoxLabel.setBounds(WIDTH / 2 + 100, HEIGHT / 2 - 215, 400, 375);
		wordBubbleLabel.setBounds(WIDTH / 2 - 410, HEIGHT / 2 - 325, 600, 300);
		crowd1Label.setBounds(0, 0, WIDTH, HEIGHT);
		crowd2Label.setBounds(0, 0, WIDTH, HEIGHT);
		sunLabel.setBounds(0, 0, WIDTH, HEIGHT);

		gamePanel.add(usedLetters);
		gamePanel.add(wordProgress);
		gamePanel.add(wordBubbleLabel);
		gamePanel.add(exeIdleLabel);
		gamePanel.add(wordBoxLabel);
		gamePanel.add(sunLabel);
		gamePanel.add(guyLabel);
		gamePanel.add(crowd1Label);
		gamePanel.add(bgLabel);

		if (name != null) {
			System.out.println("Name is " + name);
			nameLabel = new JLabel(name);
			nameLabel.setBounds(275, 35, 200, 325);
			nameLabel.setVerticalAlignment(SwingConstants.CENTER);
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			nameLabel.setFont(new Font("Serif", Font.PLAIN, 30));
			nameLabel.setOpaque(false);
			nameLabel.setBackground(Color.LIGHT_GRAY);
			gamePanel.add(nameLabel, 0);
		}

		new Thread(new Runnable() {
			@Override

			public void run() {
				JLabel currLabel = crowd1Label;
				Boolean h = true;
				while (true) {
					try {
						gamePanel.remove(currLabel);
						gamePanel.revalidate();
						gamePanel.repaint();
						if (h) {
							h = !h;
							currLabel = crowd2Label;
						} else {
							h = !h;
							currLabel = crowd1Label;
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

		gamePanel.add(tfield);
		cont.add(gamePanel);

		lives = 6;

	}

	public void checkLetter(char c) {
		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == c) {
				indices.add(i);
			}
		}

		if (indices.isEmpty()) {
			incorrectSound.setFramePosition(0);
			incorrectSound.start();

			usedLetters.setText(usedLetters.getText() + " " + c);
			lives--;
			BufferedImage guyPicture, sunImage;

			int currGuy = 6 - lives;
			if (currGuy == 0) {
				gamePanel.remove(guyLabel);
				gamePanel.remove(wordBubbleLabel);
				gamePanel.remove(wordProgress);
				gamePanel.remove(usedLetters);
				gamePanel.remove(wordBoxLabel);
				gamePanel.remove(sunLabel);
			}
			ResourceFinder rf = ResourceFinder.createInstance(new Marker());
			ImageFactory factory = new ImageFactory(rf);
			guyPicture = factory.createBufferedImage("stickman_" + currGuy + ".png", 4);
			sunImage = factory.createBufferedImage("sun" + currGuy + ".png", 4);

			guyLabel = new JLabel(new ImageIcon(guyPicture));
			guyLabel.setBounds(WIDTH / 2 - 360, HEIGHT / 2 - 125, 300, 300);
			sunLabel = new JLabel(new ImageIcon(sunImage));
			sunLabel.setBounds(0, 0, WIDTH, HEIGHT);

			gamePanel.add(guyLabel, 0);
			gamePanel.add(wordBubbleLabel, 0);
			gamePanel.add(wordProgress, 0);
			gamePanel.add(sunLabel, 0);
			gamePanel.add(wordBoxLabel, 0);
			gamePanel.add(usedLetters, 0);
			gamePanel.revalidate();
			gamePanel.repaint();
			new Thread(new Runnable() {
				@Override
				public void run() {
					JLabel currLabel = exeIdleLabel;
					Boolean h = true;
					for (int i = 1; i < 3; i++) {
						try {
							gamePanel.remove(currLabel);
							gamePanel.revalidate();
							gamePanel.repaint();
							if (h) {
								h = !h;
								currLabel = exeSwingLabel;
							} else {
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

		} else {
			correctSound.setFramePosition(0);
			correctSound.start();

			for (int index : indices) {
				gameWord = gameWord.substring(0, index) + c + gameWord.substring(index + 1);
				wordProgress.setText(gameWord);
			}
		}
	}

	public void mainMenu() {

		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		titlePanel.setBackground(Color.LIGHT_GRAY);

		informationPanel = new JPanel();
		informationPanel.setLayout(null);
		informationPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		informationPanel.setBackground(Color.RED);

		easyButton = new JButton(EASY);
		easyButton.setBounds(WIDTH / 2 - 100, 300, 200, 50);
		easyButton.setFont(new Font("Serif", Font.PLAIN, 20));
		easyButton.addActionListener(this);
		titlePanel.add(easyButton);

		mediumButton = new JButton(MEDIUM);
		mediumButton.setBounds(WIDTH / 2 - 100, 400, 200, 50);
		mediumButton.setFont(new Font("Serif", Font.PLAIN, 20));
		mediumButton.addActionListener(this);
		titlePanel.add(mediumButton);

		hardButton = new JButton(HARD);
		hardButton.setBounds(WIDTH / 2 - 100, 500, 200, 50);
		hardButton.setFont(new Font("Serif", Font.PLAIN, 20));
		hardButton.addActionListener(this);
		titlePanel.add(hardButton);

		customButton = new JButton(CUSTOM);
		customButton.setBounds(WIDTH / 2 - 100, 600, 200, 50);
		customButton.setFont(new Font("Serif", Font.PLAIN, 20));
		customButton.addActionListener(this);
		titlePanel.add(customButton);

		Screen screen1 = new Screen(6);
		screen1.setRepeating(true);
		VisualizationView view1 = screen1.getView();
		view1.setRenderer(new ScaledVisualizationRenderer(view1.getRenderer(), WIDTH, HEIGHT));
		view1.setBounds(0, 0, WIDTH, HEIGHT);
		view1.setSize(WIDTH, HEIGHT);

		ResourceFinder rf = ResourceFinder.createInstance(new Marker());
		String[] frames = rf.loadResourceNames("logo.txt");
		ContentFactory factory = new ContentFactory(rf);
		SimpleContent[] frames1 = factory.createContents(frames, 4);
		for (int i = 0; i < frames1.length; i++) {
			screen1.add(frames1[i]);
		}
		for (int i = frames1.length - 1; i >= 0; i--) {
			screen1.add(frames1[i]);
		}

		titlePanel.add(screen1.getView(), -1);
		screen1.start();
		cont.add(titlePanel);
	}

	@Override
	public void init() {

		cont = getContentPane();
		cont.setLayout(new BorderLayout(5, 10));
		cont.setBackground(BACKGROUND_COLOR);
		mainMenu();
	}

	public void getWord() {
		Random rand = new Random();
		int rand_int = rand.nextInt(words.size() - 1);
		word = words.get(rand_int);
		String tempWord = "";
		// Add spaces

		gameWord = "";
		for (int i = 0; i < word.length(); i++) {
			tempWord += word.charAt(i);
			gameWord += "_";

			if (i < word.length()) {
				tempWord += " ";
				gameWord += " ";
			}
		}
		word = tempWord;
	}

	/**
	 * Construct and invoke (in the event dispatch thread) an instance of this
	 * JApplication.
	 * 
	 * @param args The command line arguments
	 */
	public static void main(final String[] args) {
		JApplication app = new HangmanApplication(args);
		invokeInEventDispatchThread(app);
	}
}