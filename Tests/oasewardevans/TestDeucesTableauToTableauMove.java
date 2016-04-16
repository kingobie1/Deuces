package oasewardevans;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestDeucesTableauToTableauMove extends TestCase {
	Deuces deuces = new Deuces();
	GameWindow gw  = Main.generateWindow(deuces, Deck.OrderBySuit);
	
	
}
