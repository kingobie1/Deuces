package oasewardevans;

import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.launcher.Main;

public class TestDeuces extends TestCase {
	// the game:
	Deuces deuces;
	
	// the game window:
	GameWindow gw;
	
	@Override
	protected void setUp() {
		deuces = new Deuces();
		gw  = Main.generateWindow(deuces, Deck.OrderBySuit);	
	}
	
	// clean up properly
	@Override
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	public void testDealCardMove() {
		Card topCard = deuces.stock.peek();
		DeucesMoveDealCard dcm = new DeucesMoveDealCard(deuces.stock, deuces.wasteColumn);
		
		// Assert that the game is valid:
		assertTrue(dcm.valid(deuces));
		assertEquals(86, deuces.stock.count());
		
		// Deal a card:
		dcm.doMove(deuces);
		
		assertEquals(topCard, deuces.wasteColumn.peek());
		assertEquals(85, deuces.stock.count());
		
		int value = deuces.getNumLeft().getValue();
		assertEquals(85, value);
		
		dcm.undo(deuces);
		
		assertEquals(86, deuces.stock.count());
	}
	
	public void testFoundationMove() {
		Card topCard = deuces.stock.peek();
		DeucesMoveDealCard dcm = new DeucesMoveDealCard(deuces.stock, deuces.wasteColumn);
		dcm.doMove(deuces); 
		
		// have card in waste pile
	}
	
//	public void testMoves() {
//		// Ace to foundation
//		Column columnBeingDragged = new Column("columnBeingDragged");
//		//deuces.tableau[0].add(new Card(Card.ACE, Card.SPADES));
//		deuces.foundation[0].add(new Card(Card.KING, Card.SPADES));
//		Card ace = new Card(Card.ACE, Card.SPADES);
//		columnBeingDragged.add(ace);
//		
//		DeucesMoveTableauToFoundation ttf = new DeucesMoveTableauToFoundation( deuces.tableau[0], deuces.foundation[0], columnBeingDragged );
//		assertTrue (ttf.valid(deuces));
//		
//
//		//tableau[1] new Card(Card.TEN, Card.SPADES);
//	}
	
	
}
