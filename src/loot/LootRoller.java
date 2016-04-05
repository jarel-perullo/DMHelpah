package loot;

import java.io.File;
import java.nio.file.Paths;
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
public class LootRoller {
	Table<Integer, Integer, PurseRoller> goldTable;
	Table<Integer, Integer, GoodsRoller> goodsTable;
//	Table<Integer, Integer, ItemRoller>  itemTable;
	
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
		
		goldTable = HashBasedTable.create();
		
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
			goldTable.put(level, upperRoll, pr);
		}
	}
	
	public CoinPurse rollCoins(int level) {
		int roll = DiceBag.rollPercent();
		Map<Integer, PurseRoller> row = goldTable.row(level);
		
		while( !row.containsKey(roll) && roll++ <= 100 );
		PurseRoller pr = goldTable.get(level, roll);
		
		return pr.rollPurse();
	}
	
	public void parseGoods(Element goods) {
		//TODO
	}
	
	public static void main (String args[]) {
		CoinPurse purse = new CoinPurse();
		for(int i=0; i<10000; i++){
			purse = instance.rollCoins(5);
			System.out.println(purse);
		}
	}
}
