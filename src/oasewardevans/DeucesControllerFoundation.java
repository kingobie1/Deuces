package oasewardevans;

import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.view.Container;
import ks.common.view.FanPileView;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.PileView;
import ks.common.view.Widget;


public class DeucesControllerFoundation extends SolitaireReleasedAdapter {

	// The Deuces Game.
	protected Deuces theGame;

	// The specific Foundation pileView being controlled.
	protected PileView src;

	// FoundationController constructor comment.	
	public DeucesControllerFoundation (Deuces theGame, PileView foundationView) {
		super(theGame);
		this.theGame = theGame;
		this.src = foundationView;
	}
	
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView object) and the 
	 * BuildablePileView (as a ColumnView).
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}
		
		Pile foundation = (Pile) src.getModelElement();
		
		if ( fromWidget instanceof FanPileView ) { // From WasteColumn
			// Determine the To Pile
			Column wasteColumn = (Column) fromWidget.getModelElement();
			
			CardView cardView = (CardView) draggingWidget;
			Card theCard = (Card) cardView.getModelElement();
			
			Move move = new DeucesMoveWasteToFoundation(wasteColumn, foundation, theCard);
			
			if (move.doMove(theGame)) {
				theGame.pushMove(move);
				theGame.refreshWidgets(); // success move has been made.
			} else {
				// if the move was not successful return the widgets
				fromWidget.returnWidget (draggingWidget);
			}
		} else if ( fromWidget instanceof ColumnView ){ // From Tableau column
			// coming from a Tableau column [user may be trying to move multiple cards]
			Column fromColumn = (Column) fromWidget.getModelElement();
			
			ColumnView columnView = (ColumnView) draggingWidget;
			Column col = (Column) columnView.getModelElement();
		
			Move move = new DeucesMoveTableauToFoundation(fromColumn, foundation, col);
			if (move.doMove(theGame)) {
				theGame.pushMove(move);
				theGame.refreshWidgets(); // success move has been made.
			} else {
				// if the move was not successful return the widgets
				fromWidget.returnWidget (draggingWidget);
			}
		}
		
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();
		
		// finally repaint
		c.repaint();
	}
}
