package loot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import util.CoinPurse;
import util.DiceBag;
import util.DiceRoll;
import util.Hoard;

public class GoodsRoller {
	private DiceRoll roll;
	private GoodsType type;
	private static Table<GoodsType, Integer, GoodsEntry> goodsTable = HashBasedTable.create();
	
	public GoodsRoller(DiceRoll roll, GoodsType type) {
		this.roll = roll;
		this.type = type;
	}
	
	public Hoard roll() {
		GoodsEntry entry;
		Hoard hoard = new Hoard();
		int numGoods = DiceBag.roll(roll);
		int rollVal;
		Map<Integer, GoodsEntry> row = goodsTable.rowMap().get(type);
		
		//TODO remove "if" statement once ART objects are loaded
		if(type==GoodsType.ART){
			hoard.add(new Item("ART!", "TBD", new CoinPurse(999999)));
			return hoard;
		}
		
		for(int i = 0; i < numGoods; i++) {
			rollVal = DiceBag.rollPercent();
			while( !row.containsKey(rollVal) && rollVal++ <= 100 );
			entry = goodsTable.get(type, rollVal);
			hoard.add(entry.roll());
		}
		
		return hoard;
	}
	
	public static void addEntry(GoodsType type, DiceRoll roll, int modifier, int upperRoll, List<String> names) {
		goodsTable.put(type, upperRoll, new GoodsEntry(roll, modifier, names));
	}
	
	public String toString() {
		String val = "{";
		val += "roll=" + roll + "; ";
		val += "type=" + type;
		val += "}";
		return val;
	}
}

class GoodsEntry {
	DiceRoll roll;
	int modifier;
	List<String> names;
	
	public GoodsEntry(DiceRoll roll, int modifier, List<String> names) {
		this.roll = roll;
		this.modifier = modifier;
		this.names = names;
	}
	
	public Item roll() {
		String name = names.get(DiceBag.roll(names.size(), 1)-1);
		String description = "TBD";
		CoinPurse value = new CoinPurse(0,DiceBag.roll(roll) * modifier,0,0);
		
		return new Item(name, description, value);
	}
	
	public String toString() {
		String val = "{";
		val += "roll=" + roll + "; ";
		val += "modifier=" + modifier + "; ";
		val += "names=" + names + "}";
		return val;
	}
}