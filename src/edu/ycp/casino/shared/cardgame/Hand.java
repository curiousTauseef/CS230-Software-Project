package edu.ycp.casino.shared.cardgame;

import java.util.ArrayList;
import java.util.Collections;


public class Hand implements Comparable<Hand>{
	ArrayList<Card> cards;
	public Hand(){
		cards = new ArrayList<Card>();
	}
	
	public Hand(ArrayList<Card> cards){
		this.cards = cards;
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
            Collections.shuffle(this.cards);
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
    	if(getNumCards()<5)
    		return HandType.INVALID;
    	this.sort();
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
			if(num==2){
				if(num==3){
					if (num==4)
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
    
    private int getNumOfAKind(){
    	int x=0;
    	for (Card card : cards){
    		x=Collections.frequency(cards,card);
    		if (x>1)
    			break;
    	}
    	return x;
    }
    
    private boolean isTwoPair() {
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
    		ArrayList<Card> tempCards=new ArrayList<Card>();
    		Collections.copy(tempCards,cards);
    		Collections.replaceAll(cards, card, null);
    		if (this.getNumOfAKind()>1)
    			return true;
    		else
    			return false;
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
    		ArrayList<Card> tempCards=new ArrayList<Card>();
    		Collections.copy(tempCards,cards);
    		Collections.replaceAll(cards, card, null);
    		if (this.getNumOfAKind()>2)
    			return true;
    		else
    			return false;
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
    	ArrayList<Integer> handRankOrdList = new ArrayList<Integer>();
    	for(Card card : cards){
    		handRankOrdList.add(card.getRank().ordinal());
    	}
    	int topRank=Collections.max(handRankOrdList);
    	for(int x=0; x<handRankOrdList.size(); x++){
    		int found=Collections.frequency(handRankOrdList, topRank-1);
    		if (found<1)
    			return false;
    	}
    	return true;
    }

	@Override
	public int compareTo(Hand other) {
		return (this.parseHandType().ordinal() - other.parseHandType().ordinal());
	}
}