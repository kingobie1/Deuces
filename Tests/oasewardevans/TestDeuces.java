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
		//gw.setVisible(false);
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
	
	public void testTrueWasteToFoundationMove() {
		assertTrue(!deuces.stock.empty());
		Card topCard = deuces.stock.peek();
		Card drag = deuces.stock.get();
	 
		// Move 3 of spades into foundation of 2 of spades
		DeucesMoveWasteToFoundation wtf = new DeucesMoveWasteToFoundation(deuces.wasteColumn, deuces.foundation[1], drag);
		assertTrue (wtf.valid(deuces));
		
		wtf.doMove(deuces);
		assertEquals (topCard, deuces.foundation[1].peek());
	}
	
//	public void testFalseWasteToFoundationMove() {
//		Card topCard = deuces.stock.peek();
//		Card drag = deuces.stock.get();
//		
//		// Move 3 of spades into foundation of 2 of spades
//		DeucesMoveWasteToFoundation wtf = new DeucesMoveWasteToFoundation(deuces.wasteColumn, deuces.foundation[2], drag);
////		assertTrue (!wtf.valid(deuces));
////		
////		wtf.doMove(deuces);
////		assertTrue (topCard != deuces.foundation[2].peek());
//	}
	
//	public void testWasteToTableauMove() {
//		Card topCard = deuces.stock.peek();
//		Card drag = deuces.stock.get();
//		
//		// Move 3 of spades into foundation of 2 of spades
//		DeucesMoveWasteToTableau wtt = new DeucesMoveWasteToTableau(deuces.wasteColumn, deuces.tableau[10], drag);
//		assertTrue (wtt.valid(deuces));
//		
//		wtt.doMove(deuces);
//		//assertEquals (topCard, deuces.foundation[10].peek());
//	}
	
}
