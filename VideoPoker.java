 /* //--VideoPoker.java--///////////////////////////////////////////////////////
 //  Jupiter Covarrubias
 //  CSCI 1125
 //  Spring '15
 //  Programming Assignment 3
 //  April 22, 2015
 //  
 //   Implements a simulation of a popular casino game usually called Video Poker
 //   The car deck contain 52 cards, 13 of each suit. Credits are paid and in 
 //   Java Dollars. Uses Card Class from Lewis/Loftus to create a deck.
 //   Copyrights: Images used in this program where downloaded jfitz.com/cards/
 //   Known Bug: Sometime when a radio button is selected, the card corresponding
 //   to it, changes its value.
 ------------------------------------------------------------------------------*/
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class VideoPoker extends JPanel{

	private JButton stand, draw, deal, payout;
	private JLabel[] cards = new JLabel[5];     //Container for Card image.
	private int[] hand = new int[5];            //Dealer hand by card index
	private int[] handbyRank = new int[5];      //Dealer hand by rank
	private Card[] deck = new Card[52];
	private Integer[] deckShuffled = new Integer[52]; //To shuffle deck 
	private JRadioButton[] crdsB = new JRadioButton[5];
	private Stack<Integer> dealerDeck = new Stack<Integer>();  //Deck to draw
	private double playerCredit = 0;
	private JLabel enterCredit = new JLabel("Enter Credit:");
	private JLabel payCredit = new JLabel("Your Credit is: $"
	                                  + String.format("%.2f", playerCredit));
	private JTextField credit = new JTextField(6); //Text field to enter credit
	private JPanel moneyZone, imageZone, selectZone, buttonZone, winZone;
	private JLabel handPay = new JLabel();        //Credit payed by hand.
	private double rylFlsh = 250, strFlsh = 50, fOaK = 25, fullHs = 6,
			       flsh = 5, strgt = 4,tOaK = 3, twoPr = 2, onePr = 1;
	private boolean pair, twoPair, treeOfKind, fullHouse, fourOkind, royal, 
	        straight, flush, strFlush, royalFlush;
	private ImageIcon icon;

	
	//======================= MAIN FUNCTION ===================================
	public VideoPoker()
	{			
		createDeck();	
		initDeck();
		buildGUI();
	}

	//Function name: createDeck
	//Creates an array of the 52 unique cards on a playing deck as objects
	// of the Card class.
	public void createDeck(){
		int cnt = 0;
		for(int j = 1; j < 5; j++)      // Loop to set suit value
			for(int k = 1; k < 14; k++){ // Loop to set face value
			  deck[cnt]= new Card(k,j);
			  cnt++;
			}
	}
	
	//Function name: initDeck
	//Initializes deckShuffled with card identifiers  implementing 
	//Collections.shuffle method. It then pushes those identifiers to the 
	//dealerDeck stack where the card are going to be drawn from.
	public void initDeck(){
		for(int i = 0; i < 52; i++)
			deckShuffled[i] = i+1;
		Collections.shuffle(Arrays.asList(deckShuffled));
		for(int i = 0; i < 52; i++){
			dealerDeck.push(deckShuffled[i]);
		}
	}
	
	//Function name: setHands
	//Set boolean values depending on the playing hand
	public void setHands(){
		Arrays.sort(hand);       //Array of playing hand sorted by suit
		Arrays.sort(handbyRank); //Array of playing hand sorted by rank
		int cnt1 = 0;
		
		for(int i = 0; i < 4; i++)   //Compares pair instances
			for(int j = i + 1; j < 5; j++)
				if(handbyRank[i] == handbyRank[j])
					cnt1++;
		
		pair = cnt1 == 1;
		twoPair = cnt1 == 2;
		treeOfKind = cnt1 == 3;
		fullHouse = cnt1 == 4;
		fourOkind = cnt1 == 6;
		royal = cnt1 == 0 && handbyRank[0] == 1 && handbyRank[1] == 10; 
		straight = (cnt1 == 0 &&
				   (handbyRank[4] - handbyRank[0] + 1) == 5) || royal;
		flush = (deck[hand[0]-1].getSuit() == deck[hand[4]-1].getSuit());
		strFlush = straight && flush;
		royalFlush = straight && flush && royal;
	}
	
	//Function name: calcHand
	//Uses boolean values from setHands function to update player's credit 
	//And display the winning hand won as a JText 
	public void calcHand(){
		
		setHands();
		if(royalFlush){
			playerCredit += rylFlsh;
			handPay.setText("Royal Flush!!"
					        + " Hand pays: $"+ String.format("%.2f", rylFlsh));
		}
		else if(strFlush){
			playerCredit += strFlsh;
			handPay.setText("Straight Flush!"
					         + " Hand pays: $"+ String.format("%.2f", strFlsh));
		}
		else if(flush){
			playerCredit += flsh;
			handPay.setText("Flush!"
					         + " Hand pays: $"+ String.format("%.2f", flsh));
		}
		else if(straight){
			playerCredit += strgt;
			handPay.setText("Straight!"
					        + " Hand pays: $"+ String.format("%.2f", strgt));
		}
		else if(fourOkind){
			playerCredit += fOaK;
			handPay.setText("Four of a kind!"
					        + " Hand pays: $"+ String.format("%.2f", fOaK));
		}
		else if(fullHouse){
			playerCredit += fullHs;
			handPay.setText("Full House!"
					        + " Hand pays: $"+ String.format("%.2f", fullHs));
		}
		else if(treeOfKind){
			playerCredit += tOaK;
			handPay.setText("Three of a Kind!"
					        + " Hand pays: $"+ String.format("%.2f", tOaK));
		}
		else if(twoPair){
			playerCredit += twoPr;
			handPay.setText("Two Pair!"
					        + " Hand pays: $"+ String.format("%.2f", twoPr));
		}
		else if(pair){
			playerCredit += onePr;
			handPay.setText("One Pair! "
					      + " Hand pays: $"+ String.format("%.2f", onePr));
		}
		else 
			handPay.setText("Keep playing to win!");
		handPay.setVisible(true);
		payCredit.setText("Your Credit is: $"+ String.format("%.2f", playerCredit));

	    if(playerCredit == 0)
	    	enableEnterCredit();
	    
	}
	
	//Function name: buildGUI
	//Builds a GUI implementing JPanels, JLabels, JButtons and layout managers
	public void buildGUI(){
		
		setLayout (new GridLayout (5,1));
		setPreferredSize(new Dimension(500, 500));
		setBackground(Color.WHITE);
		
		stand = new JButton("STAND");
		draw = new JButton("DRAW");
		deal = new JButton("DEAL");
		payout = new JButton("PAYOUT");
	
		for(int i = 0; i < 5; i++)
			crdsB[i] = new JRadioButton();
		
		stand.addActionListener(new standListener());
		draw.addActionListener(new drawListener());
		deal.addActionListener(new dealListener());
		credit.addActionListener(new creditListener());
		payout.addActionListener(new payListener());
		
			moneyZone = new JPanel();
		    imageZone = new JPanel();
		    buttonZone = new JPanel();
		    selectZone = new JPanel();
		    winZone = new JPanel();
		    
		    moneyZone.setPreferredSize(new Dimension(400, 50));
		    moneyZone.setBackground(Color.WHITE);
		    moneyZone.add(enterCredit);
		    moneyZone.add(credit);
		    moneyZone.add(payCredit);
		    
		    imageZone.setBackground(Color.WHITE);
		    imageZone.setPreferredSize(new Dimension(440, 100));
		    imageZone.setLayout (new GridLayout (1,5));
		    //Sets 5 panels that represent a hand of cards.
		    for(int i = 0; i < 5 ; i++){
		      icon = new ImageIcon(getClass().getResource("/Images/b2fv.gif"));
				cards[i] = new JLabel(icon);
				imageZone.add(cards[i]);
		    }
		    
		    selectZone.setPreferredSize(new Dimension(300, 50));
		    selectZone.setBackground(Color.WHITE);
		    selectZone.setLayout (new GridLayout (1,5));
		    for(int i = 0; i < 5; i++)
		    	selectZone.add(crdsB[i]);
		    
		    buttonZone.setPreferredSize(new Dimension(350, 50));
		    buttonZone.setBackground(Color.WHITE);
		    buttonZone.add(stand);
		    buttonZone.add(draw);
		    buttonZone.add(deal);
		    buttonZone.add(payout);
		    
		    winZone.setPreferredSize(new Dimension(350, 50));
		    winZone.setBackground(Color.WHITE);
		    winZone.add(handPay);
		    
		    
		    
		    buttonZone.setVisible(false);
		    selectZone.setVisible(false);
		    payout.setEnabled(false);
		    draw.setEnabled(false);
		    stand.setEnabled(false);
		 
		    add(moneyZone);
		    add(imageZone);
		    add(selectZone);
		    add(buttonZone);
		    add(winZone);
	}
	
	
	
	public void enableEnterCredit(){
		buttonZone.setVisible(false);
		credit.setEnabled(true);
		enterCredit.setEnabled(true);
		handPay.setText("Please enter more credit to keep playing!");
	}
	//===================== ACTION LISTENER FUNCTIONS =========================
    private class payListener implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{
			credit.setVisible(false);
			enterCredit.setVisible(false);
			buttonZone.setVisible(false);
			imageZone.setVisible(false);
			selectZone.setVisible(false);
			winZone.setVisible(false);
			payCredit.setText("Printing Ticket... Credit: $"
			                  + String.format("%.2f", playerCredit)
			                  + " Thank You for playing!!");
		}
	}
	private class standListener implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{
			selectZone.setVisible(false);
			stand.setEnabled(false);
			draw.setEnabled(false);
			deal.setEnabled(true);
			calcHand();
		}
	}
	private class creditListener implements ActionListener{
		public void actionPerformed (ActionEvent event)
		{
			String text = credit.getText();
			if(Double.parseDouble(text) > 0){
				playerCredit = Double.parseDouble(text);
				payCredit.setText("Your Credit is: $"
				                   + String.format("%.2f", playerCredit));
				credit.setEnabled(false);
				enterCredit.setEnabled(false);
				buttonZone.setVisible(true);
				payout.setEnabled(true);
				handPay.setVisible(false);
			}
		}
	}
	
	private class drawListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			stand.setEnabled(false);
			draw.setEnabled(false);
			for(int i = 0; i < 5; i++){
				if(crdsB[i].isSelected()){
					if(dealerDeck.isEmpty())
						initDeck();
					hand[i] = (int) dealerDeck.peek();
					handbyRank[i] = deck[hand[i]-1].getFace();
					dealerDeck.pop();
					icon = new ImageIcon(getClass().getResource("/Images/"
					                                             + hand[i] +".gif"));
					cards[i].setIcon(icon);	
				}
			}
			deal.setEnabled(true);
			selectZone.setVisible(false);
			calcHand();
		}
	}
	
	private class dealListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			handPay.setVisible(false);
			selectZone.setVisible(true);
			stand.setEnabled(true);
			draw.setEnabled(true);
			if(playerCredit > 0){
				playerCredit--;
			payCredit.setText("Your Credit is: $"
				               + String.format("%.2f", playerCredit));
			for(int i = 0; i < 5 ; i++){
				crdsB[i].setSelected(false);
				if(dealerDeck.isEmpty())
					initDeck();
				hand[i] = (int) dealerDeck.peek();
				handbyRank[i] = deck[hand[i]-1].getFace();
				dealerDeck.pop();
				icon = new ImageIcon(getClass().getResource("/Images/"
				                                             + hand[i] +".gif"));
				cards[i].setIcon(icon);	
			}
			deal.setEnabled(false);
			}
		 }//end action Performed
	}//end ButtonListener
}