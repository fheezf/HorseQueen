package aima.core.environment.horsequeen;

public abstract class Piece implements Cloneable {
	
	 	char color;
	    abstract public char getColor();
	    abstract public boolean unsetAlive();
	    abstract public Position getPosition();
	    abstract public Position calculatePosition(int num);
	    abstract public void setPosition(Position newPosition);
	    abstract public int reduceStack();
	    abstract public int getStack();
	    abstract public char getType();

}
