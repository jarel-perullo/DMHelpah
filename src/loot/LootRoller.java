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
		
		NodeList entries = coins.getElementsByTagName("treasure_coins");
			
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
		NodeList gemEntries = goods.getElementsByTagName("treasure_goods");
		for(int i=0; i<gemEntries.getLength(); i++) {
			currEntry = (Element) gemEntries.item(i);
			level = Integer.parseInt(currEntry.getAttribute("level").trim());
			upperRoll = Integer.parseInt(currEntry.getAttribute("upper_roll").trim());
			numSides = Integer.parseInt(currEntry.getAttribute("num_sides").trim());
			numDice = Integer.parseInt(currEntry.getAttribute("num_dice").trim());
			goodsType = GoodsType.valueOf(currEntry.getAttribute("goods_type").trim());
			
			gr = new GoodsRoller(new DiceRoll(numSides, numDice),goodsType);
			goodsTreasureTable.put(level, upperRoll, gr);
		}
		
		// Populate Gem table
		NodeList goodsList = goods.getElementsByTagName("goods_entry");
		for(int i = 0; i < goodsList.getLength(); i++) {
			currEntry = (Element) goodsList.item(i);
			upperRoll = Integer.parseInt(currEntry.getAttribute("upper_roll").trim());
			numSides = Integer.parseInt(currEntry.getAttribute("num_sides").trim());
			numDice = Integer.parseInt(currEntry.getAttribute("num_dice").trim());
			modifier = Integer.parseInt(currEntry.getAttribute("modifier").trim());
			goodsType = GoodsType.valueOf(currEntry.getAttribute("goods_type").trim());
			
			names = new LinkedList<String>();
			nameList = currEntry.getElementsByTagName("name");
			for(int j = 0; j < nameList.getLength(); j++) {
				names.add(((Element)nameList.item(j)).getAttribute("value").trim());
			}
			GoodsRoller.addEntry(goodsType, new DiceRoll(numSides, numDice), modifier, upperRoll, names);
		}
		
		
		//TODO repeat for Art
	}
	
	public CoinPurse rollCoins(int level) {
		int roll = DiceBag.rollPercent();
		Map<Integer, PurseRoller> row = goldTreasureTable.row(level);
		while( !row.containsKey(roll) && roll++ <= 100 );
		PurseRoller pr = goldTreasureTable.get(level, roll);
		return pr.rollPurse();
	}
	
	public List<Item> rollGoods(int level) {
		int roll = DiceBag.rollPercent();
		Map<Integer, GoodsRoller> row = goodsTreasureTable.row(level);
		while( !row.containsKey(roll) && roll++ <= 100 );
		GoodsRoller gr = goodsTreasureTable.get(level, roll);
		return gr.roll();
	}
	
	public List<Item> rollItems(int level){
		//TODO
		return null;
	}
	
	public Hoard rollHoard(int level){
		Hoard hoard = new Hoard();
		
		hoard.add(rollCoins(level));
		hoard.add(rollGoods(level));
		hoard.add(rollItems(level));
		
		return hoard;
	}
	
	public static void main (String args[]) {
		CoinPurse purse = new CoinPurse();
		Item item;
		
		for(int i = 0; i < 100; i++)
			System.out.println(instance.rollHoard(1));
	}
}
