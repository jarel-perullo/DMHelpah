package loot;

import java.util.List;

import util.CoinPurse;
import util.DiceBag;
import util.DiceRoll;

public class GoodsRoller {
	private DiceRoll roll;
	private GoodsType type;
	
	public GoodsRoller(DiceRoll roll, GoodsType type) {
		this.roll = roll;
		this.type = type;
	}
	
	public Item roll() {
		//TODO
		
		return null;
	}
}

class GemRoller {
	private DiceRoll roll;
	private int modifier;
	private List<String> names;
	
	public GemRoller(DiceRoll roll, int modifier, List<String> names) {
		this.roll = roll;
		this.modifier = modifier;
		this.names = names;
	}
	
	public Item roll() {
		int nameIdx = DiceBag.roll(names.size(), 1) - 1;
		String name = names.get(nameIdx);
		
		//TODO update descriptions
		String description = "TBD";
		
		int temp = DiceBag.roll(roll) * modifier;
		CoinPurse value = new CoinPurse(0, temp, 0, 0);
		
		return new Item(name, description, value);
	}
}
