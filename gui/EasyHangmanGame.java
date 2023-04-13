package gui;

import javax.swing.*;

import app.JApplication;

public class EasyHangmanGame extends JApplication {
	
	protected JTextField letterField;
	private JButton aboutButton;
	
	public EasyHangmanGame(String[] args, int width, int height) {
		super(args, width, height);
		
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
