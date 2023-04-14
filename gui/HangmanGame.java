package gui;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
  
import app.JApplication;
import io.ResourceFinder;

public class HangmanGame extends JApplication {
	
	protected JTextField letterField;
	private JButton aboutButton;
	private String word;
	private String gameWord;
	private List<String> words;
	
	public HangmanGame(String[] args, int width, int height, String difficulty) {
		super(args, width, height);
		
		ResourceFinder finder = ResourceFinder.createInstance(resources.Marker.class);
		String filename = difficulty + ".txt";
		InputStream is = finder.findInputStream(filename);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		
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
			if (i < word.length() - 1)
			{
				tempWord += " ";
				gameWord += " ";
			} 
		}
		word = tempWord;
		
	}

	@Override
	public void init() {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);

		JLabel label = new JLabel("Enter Letters: ");
		label.setBounds(30, 30, 40, 30);
		contentPane.add(label);
		
		letterField = new JTextField();
		letterField.setBounds(80, 30, 200, 30);
		contentPane.add(letterField);
		
		
		
	}
	
	

}
