package oasewardevans;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestDeucesWasteToFoundationMove extends TestCase  {
	public void testSimple() {
		Deuces deuces = new Deuces();
		GameWindow gw  = Main.generateWindow(deuces, Deck.OrderBySuit);
		
		Card topCard = deuces.stock.peek();
		DealCardMove dcm = new DealCardMove(deuces.stock, deuces.wasteColumn);
		
		// Assert that the game is valid:
		assertTrue(dcm.valid(deuces));
		assertEquals(86, deuces.stock.count());
		
		// Deal a card:
		dcm.doMove(deuces);
		
		assertEquals(85, deuces.stock.count());
		assertEquals(1, deuces.wasteColumn.count());
		
		topCard = deuces.wasteColumn.peek();
		
		WasteToFoundationMove w2fm = new WasteToFoundationMove(deuces.wasteColumn, deuces.foundation[8], topCard);
		
		// Assert that the game is valid:
		/* can you check if valid for this case? */
		// assertTrue(w2fm.valid(deuces));
	}
}
