package oasewardevans;

import oasewardevans.DeucesControllerDeck;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.FanPileView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Deuces extends Solitaire {
	/* initialize model */
	
	private static final int COL_HEIGHT = 340;

	// attributes
	MultiDeck stock;
	 
	Column wasteColumn;
	Pile foundation[] = new Pile[12]; // add four Piles as foundation. Extra size is to enable easier algorithms.
	Column tableau[] = new Column[14]; // add four Columns as tableau. Extra size is to enable easier algorithms.
	
	// views
	DeckView stockView;
	FanPileView wasteView;
	PileView foundationView[] = new PileView[10]; // add 2 more for algorithms
	ColumnView tableauView[] = new ColumnView[12]; // add 2 more for algorithms
	
	IntegerView scoreView;
	IntegerView numLeftView;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "oasewardevans Deuces";
	}

	@Override
	public boolean hasWon() {
		// win when the score is 104
		if (getScoreValue() == 104){
			return true;
		}
		
		return false;
	}

	private void initializeControllers() {
		// Initialize Controllers for DeckView
		stockView.setMouseAdapter(new DeucesControllerDeck (this, stock, wasteColumn));
		stockView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		stockView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		wasteView.setMouseAdapter(new DeucesControllerWasteColumn (this, wasteView));
		wasteView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		wasteView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		// Now for each Foundation.
		for (int i = 1; i <= 8; i++) {
			foundationView[i].setMouseAdapter (new DeucesControllerFoundation (this, foundationView[i]));
			foundationView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			foundationView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Now for each Foundation.
		for (int i = 1; i <= 10; i++){
			tableauView[i].setMouseAdapter (new DeucesControllerTableauView (this, tableauView[i]));
			tableauView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			tableauView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
	}

	private void initializeView() {
		// initialize views
		CardImages ci = getCardImages(); // get card images
		
		for (int i = 1; i <= 8; i++){ // add all foundation widgets.
			foundationView[i] = new PileView(foundation[i]);
			foundationView[i].setBounds(40 + i*20 + i*ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
			container.addWidget(foundationView[i]);
		}
		
		for (int i = 1; i <= 10; i++){ // add all tableau widgets.
			tableauView[i] = new ColumnView(tableau[i]);
			tableauView[i].setBounds(40 + i*20 + i*ci.getWidth() - ci.getWidth(), ci.getHeight() + 60, ci.getWidth(), ci.getHeight());
			tableauView[i].setHeight(COL_HEIGHT);
			container.addWidget(tableauView[i]);
		}
		
		// add stock view
		stockView = new DeckView(stock);
		stockView.setBounds(40 + 200 + 9*ci.getWidth(), 635 - 40 - ci.getHeight(), ci.getWidth(), ci.getHeight());
		container.addWidget(stockView);
		
		// add waste column view
		wasteView = new FanPileView(1 ,wasteColumn);
		wasteView.setBounds(40 + 180 + 8*ci.getWidth(), 635 - 40 - ci.getHeight(), ci.getWidth() + 2, ci.getHeight());
		wasteView.setHeight(COL_HEIGHT);
		container.addWidget(wasteView);
		
		// add score statistic
		scoreView = new IntegerView(getScore());
		scoreView.setFontSize(14);
		scoreView.setBounds(202 + 9*ci.getWidth(), 20, 100, 60);
		container.addWidget(scoreView);
			
		// add number of cards left counter
		numLeftView = new IntegerView(getNumLeft());
		numLeftView.setFontSize(14);
		numLeftView.setBounds(202 + 9*ci.getWidth(), 80, 100, 60);
		container.addWidget(numLeftView);
	}

	private void initializeModel(int seed) {
		// initialize deck:
		stock = new MultiDeck(2);
		stock.create(seed);
		model.addElement(stock); // add to model as defined within the superclass
		
		wasteColumn = new Column("waste");
		model.addElement(wasteColumn);
		
		// initialize foundation piles 1-8:
		for (int i = 1; i <= 8; i++){
			foundation[i] = new Pile("foundation" + i);
			model.addElement(foundation[i]);
		}
		
		// initialize columns columns 1-10:
		for (int i = 1; i <= 10; i++){
			tableau[i] = new Column("tableau" + i);
			model.addElement(tableau[i]);
		}
				
		updateNumberCardsLeft(86);
		updateScore(8);
	}
	
	@Override
	public void initialize() {
		
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		
		Card currentCard;
		int foundCount = 1;
		
		// get two's into foundation piles:
		for (int i = 0; i < 104; i++) {
			currentCard = stock.get();
			
			// if the card is a two give it to the foundation piles:
			if (currentCard.getRank() == 2) {
				foundation[foundCount].add(currentCard);
				foundCount++; // add until foundCount is 8
			} else { // card is not a two
				tableau[1].add(currentCard); 
			}
		}
		
		// return other cards to deck:
		for (int i = 0; i < 96; i++) {
			stock.add(tableau[1].get());
		}
		
		// give a card to the 10 tableau columns
		for (int i = 1; i <= 10; i++) {
			tableau[i].add(stock.get());
		}
		
		// prepare game by dealing facedown cards to all columns, then one face up
		updateScore(0);
	}
	
	// Code to launch Solitaire variation
	public static void main(String[] args){
//		Main.generateWindow(new Deuces(), 123);
		Main.generateWindow(new Deuces(), Deck.OrderBySuit);
	}


}
