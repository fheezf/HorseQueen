package aima.core.environment.horsequeen;

import java.awt.TextArea;
import java.util.List;
import aima.core.search.adversarial.Game;

public class HorseQueenGame implements Game <Field, Movements, String>{
	
	TextArea text;
	Field field;

	
	public HorseQueenGame(TextArea text){
		this.text = text;
		field = new Field(12,12, text);
		
	}
	
	public HorseQueenGame(TextArea text, int x, int y){
		this.text = text;
		field = new Field(x,y, text);
		
	}
	
	public Field getInitialState() {
		return field;
		
	}

	@Override
	public String[] getPlayers() {
		return new String[] {"W","B"};
	}

	@Override
	public String getPlayer(Field field) {
		
		return String.valueOf(field.getNextPlayer());
		
	}

	@Override
	public List<Movements> getActions(Field field) {
		
		Field newField = field;
		return newField.getFieldMovementsAvailable();
	}

	@Override
	public Field getResult(Field field, Movements action) {
		
		Movements movement = action;
		Field cloneField = (Field) field.clone();
		cloneField.movePiece(cloneField.getField()[movement.getOldPosition().getX()][movement.getOldPosition().getY()],movement.getNewPosition());

		return cloneField;
	}

	@Override
	public boolean isTerminal(Field field) {
		return field.getWinner() != -1;
	}

	@Override
	public double getUtility(Field field, String player) { 
		double result = field.getWinner();
		if (result != -1)
			return result;
		
		else
			throw new IllegalArgumentException("State is not terminal.");
		
		
	}

}



