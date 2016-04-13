package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;

public class DeucesTableauToTableauMove extends Move {

	Column tableauColumnFrom;
	Column tableauColumnTo;
	Card cardBeingDragged;
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		tableauColumnTo.add(cardBeingDragged);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		tableauColumnFrom.add(tableauColumnTo.get());
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		//if ( wasteColumn.empty() ) { return false; }
		
		// Make sure that the top card in the tableauColumn is one RANK less:
		if ( cardBeingDragged.getRank() != tableauColumnTo.peek().getRank() - 1 ) { return false; }
		
		// Make sure that the top card in the tableauColumn is of the same SUIT:
		if ( cardBeingDragged.getSuit() != tableauColumnTo.peek().getSuit()) { return false; }
		
		// Card being dragged is of the same suit and one rank higher than the one in the tableauColumn:
		return true;
	}

}
