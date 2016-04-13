package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;

public class DeucesWasteToTableauMove extends Move {
	
	Column wasteColumn;
	Column tableauColumn;
	Card cardBeingDragged;
	
	DeucesWasteToTableauMove( Column source, Column destination, Card cardBeingDragged ){
		this.wasteColumn = source;
		this.tableauColumn = destination;
		this.cardBeingDragged = cardBeingDragged;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		tableauColumn.add(cardBeingDragged);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		wasteColumn.add(tableauColumn.get());
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		//if ( wasteColumn.empty() ) { return false; }
		
		// do the following if the tableau Column is not empty:
		if (!tableauColumn.empty()){
			// Make sure that the top card in the foundationPile is one RANK less:
			if ( cardBeingDragged.getRank() != tableauColumn.peek().getRank() - 1) { return false; }
			
			// Make sure that the top card in the foundationPile is of the same SUIT:
			if ( cardBeingDragged.getSuit() != tableauColumn.peek().getSuit()) { return false; }
		}
			
		// Card being dragged is of the same suit and one rank higher than the one in the foundation:
		return true;
	}

}
