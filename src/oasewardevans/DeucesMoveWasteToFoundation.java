package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

public class DeucesMoveWasteToFoundation extends Move {

	Column wasteColumn;
	Pile foundationPile;
	Card cardBeingDragged;
	
	DeucesMoveWasteToFoundation( Column source, Pile destination, Card cardBeingDragged ){
		this.wasteColumn = source;
		this.foundationPile = destination;
		this.cardBeingDragged = cardBeingDragged;
	}
	
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		foundationPile.add(cardBeingDragged);
		game.updateScore(+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {

		wasteColumn.add(foundationPile.get());
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		//if ( wasteColumn.empty() ) { return false; }
		
		// Make sure that the top card in the foundationPile is one RANK less:
		if ( cardBeingDragged.getRank() != foundationPile.peek().getRank() + 1 ) { return false; }
		
		// Make sure that the top card in the foundationPile is of the same SUIT:
		if ( cardBeingDragged.getSuit() != foundationPile.peek().getSuit()) { return false; }
		
		// Card being dragged is of the same suit and one rank higher than the one in the foundation:
		return true;
	}

}
