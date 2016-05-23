package aima.core.environment.horsequeen;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;

public class Field implements Cloneable{

	public String string="";
	private int x;
	private int y;
	public Piece field[][];
	private int winner = -1;
	private char nextPlayer = 'W';
	public TextArea textArea;
	

	public Field(int x,int y, TextArea text) {
		this.textArea = text;
		this.x = x;
		this.y = y;
		field = new Piece[this.x][this.y];
		
		for (int i = 0; i < x; i++) {
			for (int a = 0; a < y; a++) {
				field[i][a] = null;
			}
		}
		
		
		
		field[0][field[1].length / 2]= new Queen('B',this,10);
		field[0][field[1].length / 2].setPosition(new Position(0,field[1].length / 2));
		
		field[field[0].length - 1][field[1].length / 2]= new Queen('W',this,10);
		field[field[0].length - 1][field[1].length / 2].setPosition(new Position(field[0].length - 1,field[1].length / 2));
	

	}
	
	public Piece[][] getField() {
		return field;

	}
	
	public int getSize() {
		return x;

	}

	public double getWinner() {
		return winner;
	}

	public char getNextPlayer() {
		return nextPlayer;

	}
	
	public void setWinner(int winner){
		this.winner = winner;
	}
	
	public void setNextPlayer(){
		nextPlayer = getOponent(nextPlayer);
	}
	
	public char getOponent(char player){
		
		if(player == 'W')
			return 'B';
		else
			return 'W';
	}

	public boolean isTaken(int x, int y) {
		if (field[x][y] != null)
			return true;
		return false;
	}

	public void addItem(Piece piece, Position position) {
		field[position.getX()][position.getY()] = piece;
	}

	public Position ourQueenPosition(){
		
		for( int i=0; i<field.length; i++){
			for( int j=0; j<field.length; j++){
				if(isTaken(i,j) && field[i][j] instanceof Queen && nextPlayer == field[i][j].getColor())
					return new Position(i,j);
			}
		}
		return null;
	}
	
	public Position getQueenPosition(char color){
		
		for( int i=0; i<field.length; i++){
			for( int j=0; j<field.length; j++){
				if(isTaken(i,j) && field[i][j] instanceof Queen && color == field[i][j].getColor())
					return new Position(i,j);
			}
		}
		return null;
	}

	public Position enemyQueenPosition() {

		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field.length; j++) {
				if (isTaken(i, j) && field[i][j] instanceof Queen
						&& nextPlayer != field[i][j].getColor()) {
					return new Position(i, j);
				}
			}
		}
		return null;
	}

	public double euclideanDistance(Position currentPosition,Position nextPosition) {
		
		
		int resta1 = currentPosition.getX() - nextPosition.getX();
		int resta2 = currentPosition.getY() - nextPosition.getY();
		
		double distance = 0;
		
		distance = Math.sqrt(Math.pow(resta1, 2) + Math.pow(resta2, 2));

		return distance;
	}

	public boolean noMovementsAvailable (char player){
		
		if(!getFieldMovementsAvailable(player).isEmpty()){
			return false;
		}
		return true;
	}

	public boolean removeItem(Piece piece, Position position) {
		if (field[position.getX()][position.getY()] == piece) {
			field[position.getX()][position.getY()].unsetAlive();
			field[position.getX()][position.getY()] = null;
			return true;
		}
		return false;
	}
	
	public List<ArrayList<Movements>> movementsAvailable(Piece piece) {

		Movements queenMovements;
		Movements horseMovements;
		
		
		ArrayList<Movements> listQueenMovements = new ArrayList<Movements>();
		ArrayList<Movements> listHorseMovements = new ArrayList<Movements>();
		List<ArrayList<Movements>> listMovementsAvailable = new ArrayList<ArrayList<Movements>>(
				2);

		if (piece instanceof Queen) {
			
			
			if (piece.getStack() <= 2) { //la reina no se puede mover

				
				
				for (int i = 0; i < 8; i++) {

					if (correctPosition(piece.calculatePosition(i)) == true
							&& isTaken(piece.calculatePosition(i).getX(),
									piece.calculatePosition(i).getY()) == true
							&& diferentColor(piece, piece.calculatePosition(i)) == true) {
						queenMovements = new Movements(
								piece.getPosition(), piece.calculatePosition(i));
						listQueenMovements.add(queenMovements);
					}

				}

			} else { // si se puede mover

				for (int i = 0; i < 8; i++) {

					// caso para cuando la posicion no es vacia
					if (correctPosition(piece.calculatePosition(i)) == true
							&& isTaken(piece.calculatePosition(i).getX(),
									piece.calculatePosition(i).getY()) == true
							&& diferentColor(piece, piece.calculatePosition(i)) == true) {
						queenMovements = new Movements(
								piece.getPosition(), piece.calculatePosition(i));
						listQueenMovements.add(queenMovements);
					}

					// caso para cuando la posicion es vacia
					else if (correctPosition(piece.calculatePosition(i)) == true
							&& isTaken(piece.calculatePosition(i).getX(),
									piece.calculatePosition(i).getY()) == false) {

						queenMovements = new Movements(
								piece.getPosition(), piece.calculatePosition(i));
						listQueenMovements.add(queenMovements);

					}

				}
			}
		} else {
			
			
			//cuando es un caballo
			for (int i = 0; i < 8; i++) {
				
				double distance1 = euclideanDistance(piece.getPosition(),
						enemyQueenPosition());
				double distance2 = euclideanDistance(
						piece.calculatePosition(i), enemyQueenPosition());
				
				
				if (correctPosition(piece.calculatePosition(i)) == true // posicion ocupada
						&& isTaken(piece.calculatePosition(i).getX(), piece
								.calculatePosition(i).getY()) == true
						&& diferentColor(piece, piece.calculatePosition(i)) == true) {

					if (distance2 <= distance1) {
						horseMovements = new Movements(
								piece.getPosition(), piece.calculatePosition(i));
						listHorseMovements.add(horseMovements);
					} else {
						Position[] jaque = getMovementsAvailableTo(piece.calculatePosition(i));
						
						for (int j = 0; j < jaque.length; j++) {

			
							if (correctPosition(jaque[j]) == true
									&& isTaken(jaque[j].getX(),
											jaque[j].getY()) == true
									&& diferentColor(piece, jaque[j]) == true
									&& field[jaque[j].getX()][jaque[j].getY()] instanceof Queen) {
								horseMovements = new Movements(
										piece.getPosition(),
										piece.calculatePosition(i));
								listHorseMovements.add(horseMovements);
							}

						}
					}
			
				} else if (correctPosition(piece.calculatePosition(i)) // posicion no ocupada
						&& isTaken(piece.calculatePosition(i).getX(), piece
								.calculatePosition(i).getY()) == false) {
					if (distance2 <= distance1) {
						
						
						horseMovements = new Movements(
								piece.getPosition(), piece.calculatePosition(i));
						listHorseMovements.add(horseMovements);
					} else {
						
						Position[] jaque = getMovementsAvailableTo(piece.calculatePosition(i));
				
						for (int j = 0; j < jaque.length; j++) {
							if (correctPosition(jaque[j])
									&& field[jaque[j].getX()][jaque[j].getY()] instanceof Queen
									&& diferentColor(piece, jaque[j]) == true) {
						
								horseMovements = new Movements(
										piece.getPosition(),
										piece.calculatePosition(i));
								listHorseMovements.add(horseMovements);
							}
						}
					}
				}
			}

		}
		
		listMovementsAvailable.add(0, listQueenMovements);
		listMovementsAvailable.add(1, listHorseMovements);

		return listMovementsAvailable;

	}

	
	
	public Position[] getMovementsAvailableTo(Position position){
		
		ArrayList<Position> result = new ArrayList<Position>();
		
		
			Position[] possibleMoves = new Position[8];
			
			int x=position.getX();
			int y=position.getY();
			possibleMoves[0] = new Position(x - 2, y - 1);
			possibleMoves[1] = new Position(x - 2, y + 1);
			
			possibleMoves[2] = new Position(x - 1, y - 2);
			possibleMoves[3] = new Position(x - 1, y + 2);

			possibleMoves[4] = new Position(x + 2, y - 1);
			possibleMoves[5] = new Position(x + 2, y + 1);

			possibleMoves[6] = new Position(x + 1, y - 2);
			possibleMoves[7] = new Position(x + 1, y + 2);
			
			for (Position current : possibleMoves){
				if(correctPosition(current)){
					result.add(current);
				}
			}
				
		
		return result.toArray(new Position[result.size()]);
	}

	public List<Movements> getFieldMovementsAvailable() {
        
		List<Movements> movementsAvailable = new ArrayList<Movements>();
		List<Movements> queenMovementsAvailable = new ArrayList<Movements>();
		List<Movements> horseMovementsAvailable = new ArrayList<Movements>();
		List<ArrayList<Movements>> movements;
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (field[i][j] != null && Character.toUpperCase(field[i][j].getColor()) == nextPlayer) {
					
					movements = movementsAvailable(field[i][j]);
				
			
					if (field[i][j]instanceof Queen) {
						
						queenMovementsAvailable.addAll(movements.get(0));
					} else {
						horseMovementsAvailable.addAll(movements.get(1));
					}
				}
			}
		}
		movementsAvailable.addAll(queenMovementsAvailable);
		movementsAvailable.addAll(horseMovementsAvailable);		
		
		return movementsAvailable;
	}
	
	
	public List<Movements> getFieldMovementsAvailable(char player) {
        
		List<Movements> movementsAvailable = new ArrayList<Movements>();
		List<Movements> queenMovementsAvailable = new ArrayList<Movements>();
		List<Movements> horseMovementsAvailable = new ArrayList<Movements>();
		List<ArrayList<Movements>> movements;
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (field[i][j] != null && Character.toUpperCase(field[i][j].getColor()) == player) {
					movements = movementsAvailable(field[i][j]);
					if (field[i][j]instanceof Queen) {
						queenMovementsAvailable.addAll(movements.get(0));
					} else {
						horseMovementsAvailable.addAll(movements.get(1));
					}
				}
			}
		}
		movementsAvailable.addAll(queenMovementsAvailable);
		movementsAvailable.addAll(horseMovementsAvailable);
		
		return movementsAvailable;
	}
	
	public void movePiece(Piece piece, Position newPosition) {
		

		string +=( nextPlayer+" mueve desde "+ piece.getPosition().getX()+","+piece.getPosition().getY()+"hasta "+newPosition.getX()+","+newPosition.getY()+"\n");
		textArea.setText(string);

		analizeField();
		

			if(getNextPlayer() == Character.toUpperCase(piece.getColor())){
			
			if (piece instanceof Queen) {
				if (piece.getStack() > 2) { 
					
					if (isTaken(newPosition.getX(),newPosition.getY()) == false) { 
	
							field[newPosition.getX()][newPosition.getY()] = piece;
							field[piece.getPosition().getX()][piece.getPosition().getY()] = new Horse(piece.getColor(),piece.getPosition(), this);
							piece.setPosition(newPosition);
							piece.reduceStack();

					}
					
					else{
						if(Character.toUpperCase(field[newPosition.getX()][newPosition.getY()].getColor()) != Character.toUpperCase(piece.getColor())){
							
	
							
							removeItem(field[newPosition.getX()][newPosition.getY()], newPosition);
							field[newPosition.getX()][newPosition.getY()] = piece;
							removeItem(piece, piece.getPosition());
							piece.setPosition(newPosition);						
						}
					}

				}
				else{
					if(isTaken(newPosition.getX(),newPosition.getY()) == true && Character.toUpperCase(field[newPosition.getX()][newPosition.getY()].getColor()) != Character.toUpperCase(piece.getColor())){
						
						removeItem(field[newPosition.getX()][newPosition.getY()], newPosition);
						field[newPosition.getX()][newPosition.getY()] = piece;
						removeItem(piece, piece.getPosition());
						piece.setPosition(newPosition);
					}
					
				}
			}
			else{ //si es horse
				
				
				
				double distance1 = euclideanDistance(piece.getPosition(),enemyQueenPosition());
				double distance2 = euclideanDistance(newPosition,enemyQueenPosition());
	
				if (isTaken(newPosition.getX(),newPosition.getY()) == false) {
					if(distance2 <= distance1){
												
						field[newPosition.getX()][newPosition.getY()] = piece;
						removeItem(piece, piece.getPosition());
						piece.setPosition(newPosition);
						
					}
					else{
						
						Position[] jaque = getMovementsAvailableTo(newPosition);						
						for(int j=0;j<jaque.length;j++){
							
							if(isTaken(jaque[j].getX(),jaque[j].getY()) == true && field[jaque[j].getX()] [jaque[j].getY()] instanceof Queen && field[jaque[j].getX()] [jaque[j].getY()].getColor() != Character.toUpperCase(piece.getColor())){
								
								field[newPosition.getX()][newPosition.getY()] = piece;
								removeItem(piece, piece.getPosition());
								piece.setPosition(newPosition);
							}
						}
					}


				}
				else{
					if(isTaken(newPosition.getX(),newPosition.getY()) == true && Character.toUpperCase(field[newPosition.getX()][newPosition.getY()].getColor()) != Character.toUpperCase(piece.getColor())){
						if(distance2<=distance1){
								removeItem(field[newPosition.getX()][newPosition.getY()], newPosition);
				
								field[newPosition.getX()][newPosition.getY()] = piece;
								removeItem(piece, piece.getPosition());
								piece.setPosition(newPosition);
						}
						else{
							
							Position[] jaque = getMovementsAvailableTo(newPosition);
							for(int j=0;j<jaque.length;j++){
								
								if(isTaken(jaque[j].getX(),jaque[j].getY()) == true && field[jaque[j].getX()] [jaque[j].getY()] instanceof Queen && Character.toUpperCase(field[jaque[j].getX()] [jaque[j].getY()].getColor()) != Character.toUpperCase(piece.getColor())){
									removeItem(field[newPosition.getX()][newPosition.getY()], newPosition);
									field[newPosition.getX()][newPosition.getY()] = piece;
									removeItem(piece, piece.getPosition());
									piece.setPosition(newPosition);
								}
							}
						}
					}
				}
				
			}
		}
		
		analizeField();
		
		if(nextPlayer == 'W')
			
			nextPlayer = 'B';
		else
			nextPlayer = 'W';
					
		}
	

	public int countPieces(){
		
		int count = 0;

		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(field[i][j] != null)
					count ++;
			}
		}
		
		return count;
	}
	
	public int countHorses(){
		
		int count = 0;

		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(field[i][j] != null && field[i][j] instanceof Horse)
					count ++;
			}
		}
		
		return count;
	}
	
	public int countHorses(char color){
		
		int count = 0;

		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(field[i][j] != null && field[i][j] instanceof Horse && field[i][j].getColor() == Character.toLowerCase(color))
					count ++;
			}
		}
		
		return count;
	}
	
	public boolean correctPosition(Position position) {
		if ((position.getX() < x) && (position.getY() < y)
				&& (position.getX() >= 0) && (position.getY() >= 0)) {
			return true;

		}
		return false;
	}
	
	public boolean diferentColor(Piece piece, Position position) {
		if ((position.getX() < x) && (position.getY() < y) && (position.getX() >= 0)
				&& (position.getY() >= 0)) {
			if (field[position.getX()][position.getY()] != null) {
				if (Character.toUpperCase(field[position.getX()][position.getY()]
						.getColor()) == Character.toUpperCase(piece.getColor())) {
					return false;

				}
			}
		}
		return true;
	}


	public void analizeField(){ 
		
		if(enemyQueenPosition() == null){
			if(getNextPlayer() == 'W'){
				winner = 1;
			}
			else{
				winner = 0;
			}
		}
		else if(noMovementsAvailable(getOponent(nextPlayer))){
			if(getNextPlayer() == 'W')
				winner = 1;
			else
				winner = 0;
		}
	}
	
	public Object clone(){
		Field newField = null;
		try {
			newField = (Field) super.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		
		newField.field = new Piece[x][y];
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(field[i][j] != null){
					if(field[i][j] instanceof Queen){
												
						Queen queen = (Queen) field[i][j];
						newField.field[i][j] = (Queen) queen.clone();
						
					}else{
						Horse horse= (Horse) field[i][j];
						newField.field[i][j]= (Horse) horse.clone();
						}
					}
				}
			}
		
		return newField;
	}
	
	public boolean equals(Object object){
		if (object instanceof Field){
			Field auxField= (Field) object;
			for(int i=0;i<x;i++){
				for(int j=0;j<y;j++){
					if(field[i][j] != auxField.field[i][j]){
						return false;
					}
				}
			}
			return true;
			
		}
		return false;
	}
	
	public String toString() {
		String string = "";
		char character;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				try {
					character = field[i][j].getColor();
				} catch (NullPointerException e) {
					character = ' ';
				}
				string += character;
			}
			string += "\n";

		}
		return string;
	}
}
