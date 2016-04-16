//package oasewardevans;
//
//import junit.framework.TestCase;
//import ks.client.gamefactory.GameWindow;
//import ks.common.model.Deck;
//import ks.launcher.Main;
//import ks.tests.KSTestCase;
//
//public class TestKDeuces extends KSTestCase {
//	// the game:
//	Deuces deuces;
//	
//	// the game window:
//	GameWindow gw;
//	  
//	@Override
//	protected void setUp() {
//		deuces = new Deuces();
//		gw  = Main.generateWindow(deuces, Deck.OrderBySuit);	
//	}
//
//	@Override
//	protected void tearDown() { 
//		gw.dispose();
//	}
//
//	public void testPressLogic() {
//		// create mouse press at (0,0) within the deckview; should deal card
//		MouseEvent press = this.createPressed(redBlack, redBlack.deckView, 0, 0);
//		redBlack.deckView.getMouseManager().handleMouseEvent(press);
//
//		// what do we know about the game after press on deck? Card dealt!
//		assertEquals ("KS", redBlack.wastePile.peek().toString()); 
//	}
//
//}
