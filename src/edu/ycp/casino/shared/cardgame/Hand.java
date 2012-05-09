package edu.ycp.casino.shared.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Hand implements Comparable<Hand>{
	ArrayList<Card> cards;
	//The owner field is used to keep track of the hand's owner during the process of comparing hands.
	int owner;
	
	public Hand(){
		cards = new ArrayList<Card>();
	}
	
	public Hand(ArrayList<Card> _cards){
		this.cards=new ArrayList<Card>();
		this.cards.addAll(_cards);
	}
	
	public Hand(int owner){
		this.owner = owner;
	}
	
	public Hand(ArrayList<Card> _cards,int owner){
		this.cards=new ArrayList<Card>();
		this.cards.addAll(_cards);
		this.owner = owner;
	}
	
    public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public ArrayList<Card> drawCards(int numCards){
    	ArrayList<Card> hand=new ArrayList<Card>();
    	for(int x=0; x<numCards; x++){
    		hand.add(drawCard());
    	}
    	return hand;
    }
    
    public int getNumCards() {
    	return this.cards.size();
    }
    
    public Card getCard(int i) {
            return this.cards.get(i);
    }
    
    public void addCard(Card c){
    		this.cards.add(c);
    }
    
    public Card drawCard() {
            Card temp=this.cards.get(this.cards.size()-1);
            this.cards.remove(this.cards.size()-1);
            return temp;
    }
    
    public void shuffle() {
            //Collections.shuffle(this.cards);
            shuffleCard(this.cards);
    }
	//swap method for shuffle
	public void shuffleswap(ArrayList<Card>cards, int x , int y){
		Card temp = cards.get(x);
		cards.set(x, cards.get(y));
		cards.set(y, temp);
	}
	public void shuffleCard(ArrayList<Card>cards){
		Random rand = new Random();
		for (int i = cards.size(); i>1 ;i--){
			shuffleswap(cards,i-1,rand.nextInt(i));
		}
	}
    public void sort(){
    	Collections.sort(this.cards);
    }
    
    public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public HandType parseHandType(){
    	if((getNumCards()<5) || (getNumCards()>5))
    		return HandType.INVALID;
		if (isFlush()){
			if(isRoyal())
				return HandType.ROYALFLUSH;
			else if(isStraight())
				return HandType.STRAIGHTFLUSH;
			else
				return HandType.FLUSH;
		}
		else if (isStraight()){
			return HandType.STRAIGHT;
		}
		else{
			int num=getNumOfAKind();
			if(num>1){
				if(num>2){
					if (num>3)
						return HandType.FOUROFAKIND;
					else if(isFullHouse())
						return HandType.FULLHOUSE;
					else
						return HandType.THREEOFAKIND;
				}
				else if (isTwoPair())
					return HandType.TWOPAIR;
				else
					return HandType.ONEPAIR;
			}
		else
			return HandType.HIGHCARD;
		}
	}
    
	private ArrayList<Rank> makeRankList(){
    	ArrayList<Rank> handRankList = new ArrayList<Rank>();
    	for(Card card : cards){
    		handRankList.add(card.getRank());
    	}
    	return handRankList;
	}
	
	private ArrayList<Integer> makeRankOrdList(){
    	ArrayList<Integer> handRankOrdList = new ArrayList<Integer>();
    	for(Card card : cards){
    		handRankOrdList.add(card.getRank().ordinal());
    	}
    	return handRankOrdList;
	}
	private ArrayList<Rank> makeRankList(ArrayList<Card> tmpCards){
    	ArrayList<Rank> handRankList = new ArrayList<Rank>();
    	if(tmpCards.size()>0){
    		for(Card card : tmpCards){
    			handRankList.add(card.getRank());
    		}
    	}
    	return handRankList;
	}
	
	private ArrayList<Integer> makeRankOrdList(ArrayList<Card> tmpCards){
    	ArrayList<Integer> handRankOrdList = new ArrayList<Integer>();
    	if(tmpCards.size()>0){
	    	for(Card card : tmpCards){
	    		handRankOrdList.add(card.getRank().ordinal());
	    	}
    	}
    	return handRankOrdList;
	}
	
    private int getNumOfAKind(){
    	ArrayList<Rank> rankList = makeRankList();
    	int x=0;
    	for (Card card : cards){
    		x=Collections.frequency(rankList,card.getRank());
    		if (x>1){
    			break;
    		}
    	}
    	return x;
    }
    
    private int getNumOfAKind(ArrayList<Card> tmpCards){
    	ArrayList<Rank> rankList = makeRankList(tmpCards);
    	int x=0;
    	if(tmpCards.size()>0){
    		for (Card card : tmpCards){
    			x=Collections.frequency(rankList,card.getRank());
    			if (x>1){
    				break;
    			}
    		}
    	}
    	return x;
    }
    
    private boolean isTwoPair() {
    	ArrayList<Rank> rankList = makeRankList();
    	int x=0;
    	Card card=null;
    	for (Card tmpCard : cards){
    		x=Collections.frequency(rankList,tmpCard.getRank());
    		if (x>1){
    			card=tmpCard;
    			break;
    		}
    	}
    	//Check if pair was found
    	if (x<=1)
    		return false;
    	else{
    		//Make a temporary duplicate.
    		ArrayList<Card> tempCards=new ArrayList<Card>(cards);
    		while(tempCards.contains(card))
    			tempCards.remove(card);
    		if (this.getNumOfAKind(tempCards)>1){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
	}
    
	private boolean isFullHouse() {
		int x=0;
    	Card card=null;
    	//Find one pair.
    	for (Card tmpCard : cards){
    		x=Collections.frequency(cards,tmpCard);
    		if (x>1){
    			card=tmpCard;
    			break;
    		}
    	}
    	//Check if pair was found
    	if (x<=1)
    		return false;
    	else{
    		//Make a temporary duplicate.
    		ArrayList<Card> tempCards=new ArrayList<Card>(cards);
    		while(tempCards.contains(card))
    			tempCards.remove(card);
    		if (this.getNumOfAKind()>2){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
	}
	
	private boolean isFlush(){
		Suit testedSuit = cards.get(0).getSuit();
		for(Card card : cards){
			if (!testedSuit.equals(card.getSuit()))
				return false;
		}
		return true;
	}
	
    private boolean isRoyal(){
    	ArrayList<Rank> royalList = new ArrayList<Rank>();
    	ArrayList<Rank> cardRankList = new ArrayList<Rank>();
    	int x=0;
    	royalList.add(Rank.TEN);
    	royalList.add(Rank.JACK);
    	royalList.add(Rank.QUEEN);
    	royalList.add(Rank.KING);
    	royalList.add(Rank.ACE);
    	for(Card card : cards){
    		cardRankList.add(card.getRank());
    	}
    	for(Rank royalRank : royalList){
    		x=Collections.frequency(cardRankList,royalRank);
    		if (x<1)
    			return false;
    	}
    	return true;
    }
    
    private boolean isStraight(){
    	//Make a list of rank ordinals
    	ArrayList<Integer> handRankOrdList = makeRankOrdList();
    	//find the highest rank ordinal
    	int topRank=Collections.max(handRankOrdList);
    	for(int x=1; x<handRankOrdList.size(); x++){
    		int found=Collections.frequency(handRankOrdList, topRank-x);
    		if (found<1)
    			return false;
    	}
    	return true;
    }
    
    public String toString(){
    	String str="";
    	for(Card card : this.cards){
    		str+=card.toString()+"  ";
    	}
    	try{
    		return str.substring(0,(str.length()-2));
    	}
    	catch(StringIndexOutOfBoundsException e){
    		return"";
    	}
    }

	@Override
	public int compareTo(Hand other) {
		return (this.parseHandType().ordinal() - other.parseHandType().ordinal());
	}
	public int getBJHandValue (){
		int value = 0;
		for (int i = 0; i < this.getNumCards();i++){
			value = value + this.getBJCardValue(this.getCard(i));
		}
		if (value<=11){
			for(Card card : this.getCards()){
				if (card.getRank()==Rank.ACE){
					value+=10;
					break;
				}
			}
		}
		return value;
	}

	//get card value and changing the ace value
	public int getBJCardValue(Card c){
		int cardNum=c.getRank().ordinal()+1;
		if (cardNum>10)
			return 10;
		return cardNum;
	}
	//get highest hand
	public int compareBJ(Hand other){
		//if handvalue are the same
		if (this.getBJHandValue() == other.getBJHandValue()){
			//compare number of cards in hand
			if (other.getNumCards() == this.getNumCards()){
				return 0;
			}
			//if less cards
			else if(this.getNumCards() < other.getNumCards()){
				return 1;
			}
			//if more cards
			else {
				return -1;
			}
		}
		// if handvalue is less
		else if (this.getBJHandValue() < other.getBJHandValue()){
			return -1;
		}
		// if handvalue is more
		else {
			return 1;
		}
	}
	
}
