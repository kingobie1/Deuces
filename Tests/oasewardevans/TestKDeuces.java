package oasewardevans;

import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class TestKDeuces extends KSTestCase {
	// the game:
	Deuces deuces;
	
	// the game window:
	GameWindow gw;
	  
	@Override
	protected void setUp() {
		deuces = new Deuces();
		gw  = Main.generateWindow(deuces, Deck.OrderBySuit);	
	}

	@Override
	protected void tearDown() { 
		gw.dispose();
	}

	public void testPressStockLogic() {
		Card topCard = deuces.stock.peek();
		
		// create mouse press at (0,0) within the deckview; should deal card
		MouseEvent press = this.createPressed(deuces, deuces.stockView, 0, 0);
		deuces.stockView.getMouseManager().handleMouseEvent(press);

		// what do we know about the game after press on deck? Card dealt!
		assertEquals (topCard, deuces.wasteColumn.peek()); 
	}
	
	public void testTableauController() {

		// first create a mouse event
		MouseEvent pr = createPressed (deuces, deuces.tableauView[10], 0, 0);
		deuces.tableauView[10].getMouseManager().handleMouseEvent(pr);

		// drop on the first column
		MouseEvent rel = createReleased (deuces, deuces.tableauView[9], 0, 0);
		deuces.tableauView[9].getMouseManager().handleMouseEvent(rel);

		assertEquals (2, deuces.tableau[9].count());

		// undo twice.
		assertTrue (deuces.undoMove());
		assertEquals (1, deuces.tableau[9].count());
		
	}
	
	public void testFoundationTabController() {

		// first create a mouse event
		MouseEvent pr = createPressed (deuces, deuces.stockView, 0, 0);
		deuces.stockView.getMouseManager().handleMouseEvent(pr);

		MouseEvent rel = createReleased (deuces, deuces.stockView, 0, 0);
		deuces.stockView.getMouseManager().handleMouseEvent(rel);
		
		
		MouseEvent pr1 = createPressed (deuces, deuces.wasteView, 0, 0);
		deuces.wasteView.getMouseManager().handleMouseEvent(pr1);
		
		// drop on the first column
		MouseEvent rel1 = createReleased (deuces, deuces.tableauView[10], 0, 0);
		deuces.tableauView[10].getMouseManager().handleMouseEvent(rel1);

		assertEquals (2, deuces.tableau[10].count());

		MouseEvent pr2 = createPressed (deuces, deuces.tableauView[10], 0, 0);
		deuces.tableauView[10].getMouseManager().handleMouseEvent(pr2);
		
		MouseEvent rel2 = createReleased (deuces, deuces.foundationView[1], 0, 0);
		deuces.foundationView[1].getMouseManager().handleMouseEvent(rel2);

	}
	
	public void testPressTableauLogic() {
		Card topCard = deuces.tableau[10].peek();
		
		// create mouse press at (0,0) within the deckview; should deal card
		MouseEvent press = this.createPressed(deuces, deuces.tableauView[10], 0, 0);
		deuces.stockView.getMouseManager().handleMouseEvent(press);

		// what do we know about the game after press on deck? Card dealt!
		assertEquals (topCard, deuces.tableau[10].peek()); 
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
		int score = deuces.getScoreValue();
	 
		// Move 3 of spades into foundation of 2 of spades
		DeucesMoveWasteToFoundation wtf = new DeucesMoveWasteToFoundation(deuces.wasteColumn, deuces.foundation[1], drag);
		assertTrue (wtf.valid(deuces));
		
		wtf.doMove(deuces);
		assertEquals (topCard, deuces.foundation[1].peek());
		assertEquals (score + 1, deuces.getScoreValue());
	}
	
	
	public void testFalseWasteToFoundationMove() {
		Card drag = deuces.stock.get();
		
		// Move 3 of spades into foundation of 2 of spades
		DeucesMoveWasteToFoundation wtf = new DeucesMoveWasteToFoundation(deuces.wasteColumn, deuces.foundation[2], drag);
		assertEquals (false, wtf.doMove(deuces));
		
		wtf.undo(deuces);
	}
	
	
	public void testWasteToTableauMove() {
		Card topCard = deuces.stock.peek();
		Card drag = deuces.stock.get();
		
		// Move 3 of spades into foundation of 2 of spades
		DeucesMoveWasteToTableau wtt = new DeucesMoveWasteToTableau(deuces.wasteColumn, deuces.tableau[10], drag);
		assertTrue (wtt.valid(deuces));
		
		wtt.doMove(deuces);
		assertEquals (topCard, deuces.tableau[10].peek());
	}
	
	public void testTableauToTableauMove() {
		Column draggedColumn = new Column("draggedColumn");
		Card topCard = deuces.tableau[10].peek();
		draggedColumn.add(deuces.tableau[10].get());
		
		
		DeucesMoveTableauToTableau ttt = new DeucesMoveTableauToTableau(deuces.tableau[10], deuces.tableau[9], draggedColumn);
		assertTrue (ttt.valid(deuces));
		
		ttt.doMove(deuces);
		
		assertEquals (deuces.tableau[9].peek(), topCard);
		
		ttt.undo(deuces);
		assertEquals (deuces.tableau[10].peek(), topCard);
	}
	
	public void testTableauToFoundationMove() {
		Column draggedColumn = new Column("draggedColumn");
		deuces.tableau[10].add(deuces.stock.get()); // 3 of spade
		Card topCard = deuces.tableau[10].peek();
		draggedColumn.add(deuces.tableau[10].get());
		
		DeucesMoveTableauToFoundation ttf = new DeucesMoveTableauToFoundation(deuces.tableau[10], deuces.foundation[1], draggedColumn);
		
		assertTrue (ttf.valid(deuces));
		
		ttf.doMove(deuces);
		
		assertEquals (deuces.foundation[1].peek(), topCard);
		
		ttf.undo(deuces);
		assertEquals (deuces.tableau[10].peek(), topCard);
	}
}
