package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

public class DeucesMoveTableauToFoundation extends Move {
	
	Column tempColumn = new Column("temp");
	Column tableauColumnFrom;
	Pile foundationPile;
	Column columnBeingDragged;
	private int count;
	
	DeucesMoveTableauToFoundation( Column source, Pile destination, Column columnBeingDragged ){
		this.tableauColumnFrom = source;
		this.foundationPile = destination;
		this.columnBeingDragged = columnBeingDragged;
	}
	
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		// if the column is not put next card into the tableau:
		while (!columnBeingDragged.empty()){
			foundationPile.add(columnBeingDragged.get());
			count++;
		}
		
		return true;
	}

	public boolean undo(Solitaire game) {
		tempColumn.removeAll();
		
		for (int i = 0; i < count; i++){
			tableauColumnFrom.add(foundationPile.get());
		}
		
		return true;
	}

	public boolean valid(Solitaire game) {
		
		// Make sure that the top card in the foundationPile is one RANK less:
		if ( columnBeingDragged.peek().getRank() != foundationPile.peek().getRank() + 1 ) { return false; }
		
		// Make sure that the top card in the foundationPile is of the same SUIT:
		if ( columnBeingDragged.peek().getSuit() != foundationPile.peek().getSuit()) { return false; }
		
		// Card being dragged is of the same suit and one rank higher than the one in the foundation:
		return true;
	}

}
