package loot;

import util.CoinPurse;

public class Item {
	private String name;
	private String description;
	private CoinPurse value;

	public Item(String name, String description, CoinPurse value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public CoinPurse getValue() {
		return value;
	}
	
	public String toString() {
		String val = "";
		val += "{Name=" + this.name;
		val += "; description=" + this.description;
		val += "; value=" + this.value;
		val += "}";
		return val;
	}
}
