package loot;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import util.CoinPurse;
import util.CoinType;
import util.DiceBag;
import util.DiceRoll;
import util.Hoard;
public class LootRoller {
	private Table<Integer, Integer, PurseRoller> goldTreasureTable;
	private Table<Integer, Integer, GoodsRoller> goodsTreasureTable;
//	private Table<Integer, Integer, ItemRoller>  itemTable;
	
	private static LootRoller instance = new LootRoller();
	
	private LootRoller() {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("config\\lootgen.xml"));
			
			Element loot = (Element) doc.getDocumentElement();
			
			NodeList coins = loot.getElementsByTagName("coins");
			if( coins.getLength() == 1 )
				parseCoins( (Element)coins.item(0));
			else
				System.out.println("Problem parsing coins: Found " + coins.getLength() + " nodes");
			
			NodeList goods = loot.getElementsByTagName("goods");
			if( goods.getLength() == 1 )
				parseGoods( (Element)goods.item(0));
			else
				System.out.println("Problem parsing goods: Found " + goods.getLength() + " nodes");
			
			
			//TODO do the same for items
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void parseCoins(Element coins) {
		PurseRoller pr;
		int level, upperRoll, numSides, numDice, modifier;
		CoinType coinType;
		Element currEntry;
		
		goldTreasureTable = HashBasedTable.create();
		
		NodeList entries = coins.getElementsByTagName("coin_entry");
			
		for(int i=0; i<entries.getLength(); i++) {
			currEntry = (Element) entries.item(i);
			
			level = Integer.parseInt(currEntry.getAttribute("level").trim());
			upperRoll = Integer.parseInt(currEntry.getAttribute("upper_roll").trim());
			numSides = Integer.parseInt(currEntry.getAttribute("num_sides").trim());
			numDice = Integer.parseInt(currEntry.getAttribute("num_dice").trim());
			modifier = Integer.parseInt(currEntry.getAttribute("modifier").trim());
			coinType = CoinType.valueOf( currEntry.getAttribute("coin_type").trim() );
			
			pr = new PurseRoller(new DiceRoll(numSides, numDice), modifier, coinType);
			goldTreasureTable.put(level, upperRoll, pr);
		}
	}
	
	public CoinPurse rollCoins(int level) {
		int roll = DiceBag.rollPercent();
		Map<Integer, PurseRoller> row = goldTreasureTable.row(level);
		
		while( !row.containsKey(roll) && roll++ <= 100 );
		PurseRoller pr = goldTreasureTable.get(level, roll);
		
		return pr.rollPurse();
	}
	
	public void parseGoods(Element goods) {
		GoodsRoller gr;
		int level, upperRoll, numSides, numDice, modifier;
		GoodsType goodsType;
		List<String> names;
		Element currEntry;
		NodeList nameList;
		String tempName;
		
		// Populate Gem portion of treasure table
		goodsTreasureTable = HashBasedTable.create();
		NodeList gemEntries = goods.getElementsByTagName("goods_entry");
		for(int i=0; i<gemEntries.getLength(); i++) {
			currEntry = (Element) gemEntries.item(i);
			level = Integer.parseInt(currEntry.getAttribute("level").trim());
			upperRoll = Integer.parseInt(currEntry.getAttribute("upper_roll").trim());
			numSides = Integer.parseInt(currEntry.getAttribute("num_sides").trim());
			numDice = Integer.parseInt(currEntry.getAttribute("num_dice").trim());
			goodsType = GoodsType.valueOf(currEntry.getAttribute("goods_type").trim());
			
			gr = new GoodsRoller(new DiceRoll(numSides, numDice),goodsType);
			goodsTreasureTable.put(level, upperRoll, gr);
//			System.out.println(level + "::" + goodsTreasureTable.get(level, upperRoll));
			
		}
		
		// Populate Gem table
		NodeList gems = ((Element)goods.getElementsByTagName("gems").item(0)).getElementsByTagName("gem");
		for(int i = 0; i < gems.getLength(); i++) {
			currEntry = (Element) gems.item(i);
			upperRoll = Integer.parseInt(currEntry.getAttribute("upper_roll").trim());
			numSides = Integer.parseInt(currEntry.getAttribute("num_sides").trim());
			numDice = Integer.parseInt(currEntry.getAttribute("num_dice").trim());
			modifier = Integer.parseInt(currEntry.getAttribute("modifier").trim());
			
			names = new LinkedList<String>();
			nameList = currEntry.getElementsByTagName("name");
			for(int j = 0; j < nameList.getLength(); j++) {
				names.add(((Element)nameList.item(j)).getAttribute("value").trim());
			}
			GoodsRoller.addEntry(GoodsType.GEM, new DiceRoll(numSides, numDice), modifier, upperRoll, names);
		}
		
		
		//TODO repeat for Art
	}
	
	public Hoard rollGoods(int level) {
		int roll = DiceBag.rollPercent();
		Map<Integer, GoodsRoller> row = goodsTreasureTable.row(level);
//		System.out.println(roll);
		while( !row.containsKey(roll) && roll++ <= 100 );
		GoodsRoller gr = goodsTreasureTable.get(level, roll);
//		System.out.println(roll);
//		System.out.println(gr);
		
		return gr.roll();
	}
	
	public static void main (String args[]) {
		CoinPurse purse = new CoinPurse();
		Item item;
//		for(int i=0; i<10000; i++){
//			purse = instance.rollCoins(5);
//			System.out.println(purse);
//			
//			item = instance.rollGoods(1);
//			System.out.println(item);
//		}
		
		for(int i = 0; i < 100; i++)
			System.out.println(instance.rollGoods(1));
//		instance.goodsTreasureTable.cellSet().forEach(thing -> {
//			System.out.println(thing);
//		});
	}
}
