package oasewardevans;

import java.awt.event.MouseEvent;

import heineman.klondike.MoveCardToFoundationMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.view.Container;
import ks.common.view.FanPileView;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Widget;


public class DeucesTableauViewController extends SolitaireReleasedAdapter {

	// The Deuces Game.
	protected Deuces theGame;

	// The specific Foundation ColumnView being controlled.
	protected ColumnView src;

	// FoundationController constructor comment.	
	public DeucesTableauViewController (Deuces theGame, ColumnView tableauView) {
		super(theGame);
		this.theGame = theGame;
		this.src = tableauView;
	}
	
	public void mousePressed(MouseEvent me) {
		 
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a ColumnView
		// Widget managing the column we are trying to drag between two other columns.
		Container c = theGame.getContainer();
		
		/** Return if there is no card to be chosen. */
		Column tableauColumn = (Column) src.getModelElement();
		if (tableauColumn.count() == 0) {
			c.releaseDraggingObject();
			return;
		}
	
		// Get a column (sequence of cards) to move from columnView. Note: this returns a columnView.
		// Note that this method will alter the model for columnView if the condition is met.
		ColumnView columnView = src.getColumnView(me);
		
		// an invalid selection of some sort.
		if (columnView == null) {
			c.releaseDraggingObject();
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the columnView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// columnView (sequence of cards) widget reflect the original column's (sequence of cards) location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("DeucesTableauViewController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (columnView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
	}
	
	
	/**
	 * quick change
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
			System.err.println ("DeucesTableauViewController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("DeucesTableauViewController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		Column tableau = (Column) src.getModelElement();
		
		if ( fromWidget instanceof FanPileView ) { 		// From wasteColumn
			// Determine the To Pile
			Column wasteColumn = (Column) fromWidget.getModelElement();
			
			CardView cardView = (CardView) draggingWidget;
			Card theCard = (Card) cardView.getModelElement();
		
			Move move = new DeucesWasteToTableauMove(wasteColumn, tableau, theCard);
			if (move.doMove(theGame)) {
				theGame.pushMove(move);
				theGame.refreshWidgets(); // success move has been made.
			} else {
				// if the move was not successful return the widgets
				fromWidget.returnWidget (draggingWidget);
			}
		} else if ( fromWidget instanceof ColumnView ) { // From Tableau column
			
			// coming from a Tableau column [user may be trying to move multiple cards]
			Column fromColumn = (Column) fromWidget.getModelElement();
			
			ColumnView columnView = (ColumnView) draggingWidget;
			Column col = (Column) columnView.getModelElement();
		
			Move move = new DeucesTableauToTableauMove (fromColumn, tableau, col);
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
