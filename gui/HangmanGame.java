package gui;

import javax.swing.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
  
import io.ResourceFinder;
import visual.Visualization;
import visual.VisualizationView;

public class HangmanGame extends Visualization {
	
	private static final Color BACKGROUND_COLOR = new Color(204, 204, 255);
	
	protected JTextField letterField;
	private ResourceFinder jarFinder;
	private String word;
	private String gameWord;
	private List<String> words;
	
	public HangmanGame(int width, int height, String difficulty) {
		super();
		jarFinder = ResourceFinder.createInstance(resources.Marker.class);
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
		
		VisualizationView view = getView();
	    view.setBounds(0, 0, width, height);
	    view.setSize(width, height);
	    view.setBackground(BACKGROUND_COLOR);
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
			if (i % 2 == 0)
			{
				tempWord += word.charAt(i);
				gameWord += "_";
			} else
			{
				tempWord += " " + word.charAt(i);
				gameWord += " _";
			}
		}
		word = tempWord;
	}
	
	/**
	 * Reset this WeatherObserver.
	 */
	public void reset()
	{
	    clear();
	}

	
	
	

}