package util;

public class DiceBag {
	
	public static int roll(int numSides, int numDice) {
		int sum = 0;
		for(int i=0; i<numDice; i++) {
			sum += (int)(Math.random()*numSides)+1;
		}
		return sum;
	}
	
	public static int roll(DiceRoll roll) {
		return roll(roll.numSides, roll.numDice);
	}
	
	public static int check() {
		return roll(20,1);
	}
	
	public static int rollPercent() {
		return roll(100,1);
	}
}

