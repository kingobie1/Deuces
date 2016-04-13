package oasewardevans;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.MultiDeck;

public class DealCardMove extends Move {
	MultiDeck stock;
	Column waste;
	
	// Constructor
	public DealCardMove(MultiDeck stock, Column waste){
		this.stock = stock;
		this.waste = waste;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)){ return false; }
		
		Card card = stock.get();
		waste.add(card);
		game.updateNumberCardsLeft(-1);
		return true;
		
	}
	
	@Override
	public boolean undo(Solitaire game) {
		Card c = waste.get();
		stock.add(c);
		game.updateNumberCardsLeft(+1);
		return true;
	}
	
	@Override
	public boolean valid(Solitaire game) {
		return !stock.empty();
	}
	
	
}
