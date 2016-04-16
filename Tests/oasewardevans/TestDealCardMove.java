package oasewardevans;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestDealCardMove extends TestCase {
	
	public void testSimple() {
		Deuces deuces = new Deuces();
		GameWindow gw  = Main.generateWindow(deuces, Deck.OrderBySuit);
		
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
}
