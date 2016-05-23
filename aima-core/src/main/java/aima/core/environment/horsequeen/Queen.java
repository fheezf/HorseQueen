package aima.core.environment.horsequeen;

public class Queen extends Piece implements Cloneable{

	private char color;
    private Position position;
    private Boolean alive;
    private Field field;
    private int stack;

    public Queen(char color, Field field,int stack) {
        this.color = color;
        this.field = field;
        alive = true;
        this.stack=stack;
        

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

    
    public Position calculatePosition(int num) {
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

  
    public int reduceStack() {
        stack--;
        return stack;
    }


    public int getStack() {
        return stack;
    }


	@Override
	public char getType() {
		
		return 'q';
	}
	
	public Queen clone(){
		
		Queen queen = null;
		try{
			queen = (Queen) super.clone();
		}
		catch(CloneNotSupportedException e){
			
			
		}
		
		return queen;
	}
   

}
