package edu.ycp.casino.shared.cardgame.poker;

import java.util.ArrayList;

import edu.ycp.casino.shared.Game;
import edu.ycp.casino.shared.Player;
import edu.ycp.casino.shared.cardgame.Dealer;
import edu.ycp.casino.shared.cardgame.Hand;

public class Table extends Game{
	
	private final int ANTI=5;
	private HandComperator comperator;
	private ArrayList<Player> players;
	private Dealer dealer;
	private Hand community;
	private Pot pot;
	private int currentPlayer=-1;
	
	private ArrayList<Player> makePlayers(int numPlayers){
		ArrayList<Player> players=new ArrayList<Player>();
		for(int pNum=0; pNum<numPlayers; pNum++){
			players.add(new Player(50,pNum));
		}
		return players;
	}
	
	public Table(){
		//Instantiate objects;
		this.comperator = new HandComperator();
		this.pot = new Pot();
		this.community = new Hand();
		this.dealer = new Dealer();
		this.players = makePlayers(5);
		dealer.newDeck();
		dealer.dealNext(players, community);
		iterateCurrentPlayer();
	}
	public Table(ArrayList<Player> _players){
		//Instantiate objects;
		this.comperator = new HandComperator();
		this.pot = new Pot();
		this.community = new Hand();
		this.dealer = new Dealer();
		this.players = _players;
		dealer.newDeck();
		dealer.dealNext(players, community);
		iterateCurrentPlayer();
	}
	public void newGame(){
		this.pot = new Pot();
		this.community = new Hand();
		dealer.setBettingRound(0);
		dealer.newDeck();
		dealer.dealNext(players, community);
		iterateCurrentPlayer();
	}
	
	public void takeBets(){
		for (Player player : players){
			if(player.takeHoldingBet())
				pot.add(player.getHoldingBet());
		}
	}
	
	public void iterateCurrentPlayer(){
		if(currentPlayer<players.size()-1)
			currentPlayer++;
		else{
			currentPlayer=0;
			pot.resetMinBet();
			dealer.dealNext(players,community);
		}
		if(players.get(currentPlayer).isFolded())
			iterateCurrentPlayer();
	}
	
	public void takeAnti(){
		for (Player player : players){
			pot.add(player.getAnti(ANTI));
		}
	}

	public Player getCurrentPlayer(){
		return players.get(currentPlayer);
	}
	public void setCurrentPlayer(int playerNum){
		currentPlayer=playerNum;
	}
	public int getCurrentPlayerNum(){
		return currentPlayer;
	}
	
	public void play(){
		takeAnti();
		dealer.deal(players);
		takeBets();
		dealer.dealFlop(this.community);
		takeBets();
		dealer.dealTurn(this.community);
		takeBets();
		dealer.dealRiver(this.community);
		takeBets();
		getWinner().getWallet().addFunds(this.pot.takeAll());
	}
	
	public boolean gameIsOver(){
		return (dealer.getBettingRound()==-1);
	}
	
	public void fillEmptySeats(){
		int emptySeats=5-this.players.size();
		for (int x=0; x<emptySeats; x++)
			this.players.add(new Player());
	}
	
	public int nonFoldedPlayers(){
		int x=0;
		for(Player player : players){
			if(!player.isFolded()){
				x++;
			}
		}
		return x;
	}
	
	public Player getWinner(){
		ArrayList<Player> playersIn=new ArrayList<Player>();
		for(Player player : players)
			if(!player.isFolded())
				playersIn.add(player);
		return this.comperator.getWinner(this.community,playersIn);
	}
	
	public Dealer getDealer() {
		return dealer;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public Hand getCommunity() {
		return community;
	}

	public void setCommunity(Hand community) {
		this.community = community;
	}

	public void addPlayer(Player player){
		this.players.add(player);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public int getAnti() {
		return ANTI;
	}

	public Pot getPot() {
		return pot;
	}

	public void setPot(Pot pot) {
		this.pot = pot;
	}
	
	
	
}
