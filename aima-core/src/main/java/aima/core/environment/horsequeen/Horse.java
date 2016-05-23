package aima.core.environment.horsequeen;

public class Horse extends Piece implements Cloneable{
	private char color;
    private Position position;
    private Boolean alive;
    private Field field;

    public Horse(char color, Position position, Field field) {
        this.color = Character.toLowerCase(color);
        this.position = position;
        this.field = field;
        alive = true;
    }

    
    public char getColor() {
        return color;
    }

    
    public Boolean isAlive(){
        
        return alive;
            
    }
    
    public Position getPosition(){
        return position;
    }
    
    public boolean unsetAlive() {
       if (alive){ 
      	 alive=false;
          return true;
       }
       return false;
    }

    
    public Position calculatePosition(int num) {   //
    	
    	switch(num){
        
        case 1: 
            return new Position(position.getX()+2,position.getY()+1);
        case 2: 
            
            return new Position(position.getX()+2,position.getY()-1);
        case 3: 
            
            return new Position(position.getX()+1,position.getY()+2);
        case 4: 
            
            return new Position(position.getX()-1,position.getY()+2);
       case 5: 
            
            return new Position(position.getX()-1,position.getY()-2);
        case 6: 
            
            return new Position(position.getX()-2,position.getY()-1);
       case 7: 
            
            return new Position(position.getX()-2,position.getY()+1);
       default: 
            
            return new Position(position.getX()+1,position.getY()-2);
        }
    }



    public void setPosition(Position newPosition) {
        position = newPosition;
    }



    @Override
    public char getType() {
        return 'h';
    }

    @Override
    public int reduceStack() {
       return 0;
    }

    @Override
    public int getStack() {
        return 1;
    }
    
	public Horse clone(){
		
		Horse horse = null;
		try{
			horse = (Horse) super.clone();
		}
		catch(CloneNotSupportedException e){
			
			
		}
		
		return horse;
	}
    
}


