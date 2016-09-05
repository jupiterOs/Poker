 /*////--VideoPokerDriver.java --/////////////////////////////////////////////////////
 //  Jupiter Covarrubias
 //  CSCI 1125
 //  Spring '15
 //  Programming Assignment 3
 //  April 22, 2015
 //
 //  Driver program of the Video Poker Simulation Class
 ------------------------------------------------------------------------------*/

import javax.swing.*; 


public class VideoPokerDriver {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Video Poker");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		VideoPoker panel = new VideoPoker();
		
		
		frame.getContentPane().add(panel);
	    frame.pack();
	    frame.setVisible(true);
	}

}
