package aima.core.environment.horsequeen;

import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.framework.Metrics;

public class SecondHeuristic extends AlphaBetaSearch<Field, Movements, String>{
	HorseQueenGame game;
	private int expandedNodes;
	private int maxDepth;
	private String currentPlayer;
	public SecondHeuristic (HorseQueenGame game){
		super(game);
		this.game=game;
	}
	
	@Override
	public Movements makeDecision(Field state) {
		maxDepth = HorseQueen.maxDepth;
		expandedNodes = 0;
		Movements result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		String player = this.game.getPlayer(state);
		currentPlayer=player;

		for (Movements action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxDepth);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}if(result==null) result=action;
		}
			
		HorseQueen.HorseQueenPanel.setStatusBar(resultValue);
		
		System.out.println(resultValue);
		
		return result;
	}
	
	public double maxValue(Field state, String player, double alpha, double beta, int maxDepth) {
		expandedNodes++;
		if (game.isTerminal(state) || maxDepth<=0)
			return eval(state,player);
		double value = Double.NEGATIVE_INFINITY;
		for (Movements action : game.getActions(state)) {
			value = Math.max(value, minValue(game.getResult(state, action), player, alpha, beta, maxDepth-1));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(Field state, String player, double alpha, double beta, int maxDepth) {
		expandedNodes++;
		if (game.isTerminal(state) || maxDepth<=0)
			return eval(state,player);
		double value = Double.POSITIVE_INFINITY;
		for (Movements action : game.getActions(state)) {
			value = Math.min(value, maxValue( game.getResult(state, action), player, alpha, beta, maxDepth-1));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}
	
	public double eval(Field state, String player){
		boolean myQueenIsAlive=false;
		boolean enemyQueenIsAlive=false;
		Position pos;
				
		for(int i=0; i<state.getField().length;i++){
			for(int j=0;j<state.getField().length;j++){
				pos = new Position(i,j);
				if(state.isTaken(pos.getX(),pos.getY())){
					if(state.getField()[pos.getX()][pos.getY()] instanceof Queen &&  (String.valueOf(state.getField()[pos.getX()][pos.getY()].getColor()).equals(player)) == false){
						enemyQueenIsAlive = true;
						
						break;
					}
				}
			}
		}
		if(!enemyQueenIsAlive){ 
			if (player.equals(currentPlayer)) return 100; // 100 es el valor máximo heuristico que doy, que significa la victoria
		}
		
		for(int i=0; i<state.getField().length;i++){
			for(int j=0;j<state.getField().length;j++){
				pos = new Position(i,j);
				if(state.isTaken(pos.getX(),pos.getY())){
					if(state.getField()[pos.getX()][pos.getY()] instanceof Queen &&  String.valueOf(state.getField()[pos.getX()][pos.getY()].getColor()).equals(player)){
						myQueenIsAlive = true;
						break;
					}
				}
			}
		}
		
		
		if (!myQueenIsAlive){
			if (player.equals(currentPlayer)) return -100;
		}
		
		if(state.noMovementsAvailable(player.charAt(0))){
			if (player.equals(currentPlayer)) return -100;
		}
		
		if(state.noMovementsAvailable(state.getOponent(player.charAt(0)))){
			if (player.equals(currentPlayer)) return 100;
		}
		
        Position[] jaque = state.getMovementsAvailableTo(state.getQueenPosition(player.charAt(0))); // hay que utilizar el dle otro porque al mover se cambia el jugador actual
        for(int i=0;i<jaque.length;i++){
            if (state.isTaken(jaque[i].getX(),jaque[i].getY()) && String.valueOf(state.getField()[jaque[i].getX()][jaque[i].getY()].getColor())!=player){
                if (player.equals(currentPlayer)) return -10;
            }
        }
        
        Position[] jaque2 = state.getMovementsAvailableTo(state.getQueenPosition(state.getOponent(player.charAt(0))));
		for(int i=0;i<jaque2.length;i++){
			if (state.isTaken(jaque2[i].getX(),jaque2[i].getY()) && String.valueOf(state.getField()[jaque2[i].getX()][jaque2[i].getY()].getColor())==player){
				if (player.equals(currentPlayer)) return 10;
			}
		}
        
    	double defensive = 0;
		double offensive = 0;
		
		Position[] positions;
		
		for(int i=0; i<state.getField().length;i++){
			for(int j=0;j<state.getField().length;j++){
				if(state.isTaken(i, j)){ 
					if(String.valueOf(state.getField()[i][j].getColor()) == player){
						positions = state.getMovementsAvailableTo(state.getField()[i][j].getPosition());
						for(int h=0; h<positions.length; h++){
							if(String.valueOf(state.getField()[positions[h].getX()][positions[h].getY()].getColor()) == player){
								defensive++;
							}
							else
								offensive++;
						}
					}
					
					
				}
			}
		}
		
		if(player.equals(currentPlayer))
			return defensive;
		else
			return offensive;

		
	}
	@Override
	public Metrics getMetrics() {
		Metrics result = new Metrics();
		result.set("Nodos Expandidos", expandedNodes);
		return result;
	}
}
