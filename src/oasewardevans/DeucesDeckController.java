package oasewardevans;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.MultiDeck;
import ks.common.model.Column;
import ks.common.model.Move;

/**
 */
public class DeucesDeckController extends SolitaireReleasedAdapter {
	/** The game. */
	protected Deuces theGame;

	/** The WasteColumn of interest. */
	protected Column wasteColumn;

	/** The Deck of interest. */
	protected MultiDeck stock;

	/**
	 * DeucesDeckController constructor comment.
	 */
	public DeucesDeckController(Deuces theGame, MultiDeck s, Column wasteColumn) {
		super(theGame);

		this.theGame = theGame;
		this.wasteColumn = wasteColumn;
		this.stock = s;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the press.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealFourCardMove
		Move m = new DealCardMove (stock, wasteColumn);
		if (m.doMove(theGame)) {
			theGame.pushMove (m);     // Successful DealFour Move
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}
}
 