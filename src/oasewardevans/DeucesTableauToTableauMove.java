package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;

public class DeucesTableauToTableauMove extends Move {

	Column tableauColumnFrom;
	Column tableauColumnTo;
	Column columnBeingDragged;
	private int count;
	
	DeucesTableauToTableauMove( Column source, Column destination, Column columnBeingDragged ){
		this.tableauColumnFrom = source;
		this.tableauColumnTo = destination;
		this.columnBeingDragged = columnBeingDragged;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		// if the column is not put next card into the tableau:
		while (!columnBeingDragged.empty()){
			tableauColumnTo.add(columnBeingDragged.get());
			count++;
		}
		
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		for (int i = 0; i < count; i++){
			tableauColumnFrom.add(tableauColumnTo.get());
		}
		
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		//if ( wasteColumn.empty() ) { return false; }
		
		// Make sure that the top card in the tableauColumn is one RANK less:
		if ( columnBeingDragged.peek(count).getRank() != tableauColumnTo.peek().getRank() - 1 ) { return false; }
		
		// Make sure that the top card in the tableauColumn is of the same SUIT:
		if ( columnBeingDragged.peek(count).getSuit() != tableauColumnTo.peek().getSuit()) { return false; }
		
		// Card being dragged is of the same suit and one rank higher than the one in the tableauColumn:
		return true;
	}

}
