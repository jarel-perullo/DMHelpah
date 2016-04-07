package util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import loot.Item;

public class Hoard {
	private CoinPurse coins;
	private List<Item> items;
	
	public Hoard() {
		coins = new CoinPurse();
		items = new LinkedList<Item>();
	}
	
	public Hoard(CoinPurse coins, List<Item> items) {
		this.coins = coins;
		this.items = items;
	}
	
	public void add(CoinPurse coins) {
		this.coins.add(coins);
	}
	
	public void add(Item item) {
		items.add(item);
	}
	
	public void add(List<Item> items) {
		this.items.addAll(items);
	}
	
	public void add(Hoard h) {
		add(h.coins);
		add(h.items);
	}
	
	public String toString() {
		String val = "{";
		val += "coins=" + coins + ";";
		val += "Items=" + items;
		val += "}";
		return val;
	}
}
