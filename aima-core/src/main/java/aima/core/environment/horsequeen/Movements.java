package aima.core.environment.horsequeen;

public class Movements {
	private Position oldPosition;
	private Position newPosition;
	
	public Movements(Position oldPosition, Position newPosition){
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		
	}
	
	public Position getOldPosition(){
		return oldPosition;
	}
	
	public Position getNewPosition(){
		return newPosition;
	}
	
	public void setNewPosition(Position newPosition){
		this.newPosition = newPosition;
	}
	
	public void setOldPosition(Position oldPosition){
		this.oldPosition = oldPosition;
	}
}
