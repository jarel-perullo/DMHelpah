package loot;

import util.CoinPurse;
import util.CoinType;
import util.DiceBag;
import util.DiceRoll;

public class PurseRoller {
	private DiceRoll roll;
	private int modifier;
	private CoinType type;
	
	public PurseRoller(DiceRoll roll, int modifier, CoinType type) {
		this.roll = roll;
		this.modifier = modifier;
		this.type = type;
	}
	
	public CoinPurse rollPurse() {
		CoinPurse p = new CoinPurse();
		p.add(modifier * DiceBag.roll(roll), type);
		return p;
	}
	
	public String toString() {
		String val = "";
		val += "{roll=" + roll;
		val += "; mod=" + modifier;
		val += "; type=" + type.toString();
		val += "}";
		return val;
	}
	
	public static void main(String args[]) {
		PurseRoller roller = new PurseRoller(
				new DiceRoll(8, 1),
				1000,
				CoinType.SP);
		
		for(int i=0; i<10; i++) {
			System.out.println(roller.rollPurse());
		}
	}
}
