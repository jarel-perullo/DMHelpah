package util;

public class DiceRoll {
	int numSides;
	int numDice;
	
	public DiceRoll(int numSides, int numDice) {
		this.numSides = numSides;
		this.numDice = numDice;
	}
	
	public DiceRoll(int numSides) {
		this(numSides, 1);
	}
	
	public String toString() {
		String val = "";
		val += "{sides=" + numSides;
		val += "; dice=" + numDice;
		val += "}";
		return val;
	}
	
	public int roll() {
		return DiceBag.roll(this);
	}
}
