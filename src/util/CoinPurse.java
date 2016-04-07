package util;

public class CoinPurse {
	private int cp;
	private int sp;
	private int gp;
	private int pp;
	
	public CoinPurse(int pp, int gp, int sp, int cp){
		this.cp = cp;
		this.sp = sp;
		this.gp = gp;
		this.pp = pp;
	}
	
	public CoinPurse() {
		this(0,0,0,0);
	}
	
	public CoinPurse(int gp) {
		this(0,gp,0,0);
	}
	
	public int getCp() {
		return this.cp;
	}
	
	public int getSp() {
		return this.sp;
	}
	
	public int getGp() {
		return this.gp;
	}
	
	public int getPp() {
		return this.pp;
	}
	
	public void add(int val, CoinType type) {
		switch(type) {
			case PP:
				pp += val;
				break;
			case GP:
				gp += val;
				break;
			case SP:
				sp += val;
				break;
			case CP:
				cp += val;
				break;
			default:
				break;
		}
	}
	
	public void addCp(int val){
		this.cp += val;
	}
	
	public void addSp(int val){
		this.sp += val;
	}
	
	public void addGp(int val){
		this.gp += val;
	}
	
	public void addPp(int val){
		this.pp += val;
	}
	
	public void add(CoinPurse purse) {
		this.cp += purse.cp;
		this.sp += purse.sp;
		this.gp += purse.gp;
		this.pp += purse.pp;
	}
	
	public void consolodate() {
		sp += cp / 10;
		cp %= 10;
		
		gp += sp / 10;
		sp %= 10;
		
		pp += gp / 10;
		gp %= 10;
	}
	
	public String toString() {
		String val = "";
		val += (pp>0) ? pp + "PP" : "";
		val += (gp>0) ? (val.length()>0 ? ", " : "") + gp + "GP" : "";
		val += (sp>0) ? (val.length()>0 ? ", " : "") + sp + "SP" : "";
		val += (cp>0) ? (val.length()>0 ? ", " : "") + cp + "CP" : "";

		return (val.equals("")) ? "0CP" : val;
	}
	
	public static void main(String args[]) {
		CoinPurse purse = new CoinPurse(0,2,3,0);
		purse.consolodate();
		System.out.println(purse);
	}
}
