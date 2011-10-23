/*------------------------------------------------------
  ファイル名  Bunshi.java
  日付        2004/07/25
  作成者      Si_Densetsu
-------------------------------------------------------*/

import java.util.Random;

/**
 * 一回分のゲームを制御するメインクラス
 * @author  Si_Densetsu
 * @version 0.4.3-8/24
 */

public final class Bunshi {
    public static final byte ENDLESS    = 0;
    public static final byte TIMEATTACK = 1;
    public static final long GAMETIME   = 60000;
    
    // これらはsearchOHを呼ぶために使う。
    private static final int EMPTY = -1; // 空
    private static final int CENTER= 0;
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private static final int TOP   = 3;
    private static final int BOTTOM= 4;
    private static final int SUPPLIMENT = 5;
	
	private Random rnd = new Random(); // 乱数
	public Field field = new Field(8,5);
	
	/** 操作する元素情報 */
	public FallingAtom movingAtom;
	public FallingAtom nextAtom;
	
	/** ゲーム情報 */
	public GameResult gameResult;
	public int energy;
	public int fallwait;
	public ConfigValue configValue_;
	
	private boolean endFlag;
	private boolean generatingFlag;
	
	/** 連鎖回数 */
	public byte seriesCount;
	
	/** 指令 */
	private Mission mission;
	
	/** @param mode ゲームモード */
	public Bunshi(byte mode, ConfigValue configValue) {
	    gameResult = new GameResult(mode, System.currentTimeMillis(), configValue.getChoice());
	    configValue_ = configValue; 
	    
	    mission = new Mission(rnd, configValue);
	    endFlag = false;
	    nextAtom = createNext();
	    fallwait = 2000;
	    goNext();
	}
	
	/** 次の原子 */
	public void goNext() {
	    movingAtom = nextAtom;
	    nextAtom = createNext();
	}
	
	private FallingAtom createNext() {
	    return new FallingAtom((byte)(Math.abs(rnd.nextInt()) % Atom.SPECIES));
	}
	
	public boolean fall() {
    
	    if (isEnd()) return false;
	    
	    if (movingAtom.getY() > field.amount(movingAtom.getX())) {
	        movingAtom.fall();
	        return false;
	    }
	    else {
	        land();
	        return true;
	    }
	}
	
	public void moveLeft() {
	    if (!isEnd()
	            && movingAtom.getX() > 0
	            && movingAtom.getY() >= field.amount(movingAtom.getX() - 1)) {
	        movingAtom.moveLeft();
	    }
	}
	
	public void moveRight() {
	    if (!isEnd()
	            && movingAtom.getX() < field.getWidth() - 1
	            && movingAtom.getY() >= field.amount(movingAtom.getX() + 1) ) {
	        movingAtom.moveRight();
	    }
	}
	
	public void land() {
	    if (isEnd()) return;
	    field.land(movingAtom);
	    seriesCount = 0;
	    generatingFlag = true;
	}
	
	/** 着地チェック・着地し，反応していたらtrueを返し，反復処理してfalseを返すと共にgonextする。 */
	public Molecule check() {
	    Molecule m = null;
	    close();
	    if ((m = search()) != null) { // 検索・反応処理開始
	        generate(m);
		    for (int x = 0; x < field.getWidth(); x++) {
		        for (int y = 0; y < field.amount(x); y++) {
		            if (field.getMatter(x, y).isMarkedErace()) {
		                if (field.getMatter(x+1, y) != null) field.getMatter(x+1, y).energize_weak();
		                if (field.getMatter(x-1, y) != null) field.getMatter(x-1, y).energize_weak();
		                if (field.getMatter(x, y+1) != null) field.getMatter(x, y+1).energize_weak();
		                if (field.getMatter(x, y-1) != null) field.getMatter(x, y-1).energize_weak();
		            }
		        }
		    }
	        return m;
	    }
	    
	    generatingFlag = false;
	    endFlag = (field.getMatter(2,7) != null);
	    goNext();
	    return null;
	}
	
	public boolean isEnd() {
	    return (gameResult.getMode() == TIMEATTACK
	            && System.currentTimeMillis() - gameResult.getStartTime() > GAMETIME
	    	) || endFlag;
	}
	
	public boolean isGenerating() {
	    return generatingFlag;
	}
	
	public Molecule search() {
	    Molecule m = null;
	    if ((m = searchComplex()) != null
	            || (m = search5()) != null
	            || (m = search4()) != null
	            || (m = search3()) != null
	            || (m = search2()) != null)
	        return m;
	    else
	        return null;
	}
	
	public Molecule search2() {
	    Molecule m = null;
	    
	    // 横検索
	    for (int x = 0; x < field.getWidth() - 1; x++) {
	        for (int y = 0; y < field.amount(x); y++) {
	            m = search2(field.getMatter(x, y), field.getMatter(x + 1, y));
	            if (m != null) return m;
	        }
	    }
	    
	    // 縦検索
	    for (int x = 0; x < field.getWidth(); x++) {
	        for (int y = 0; y < field.amount(x) - 1; y++) {
	            m = search2(field.getMatter(x, y), field.getMatter(x, y + 1));
	            if (m != null) return m;
	        }
	    }
	    
	    return null;
	}
	
	public Molecule search3() {
	    Molecule m = null;
	    
	    // 横一列検索
	    for (int x = 0; x < field.getWidth() - 2; x++) {
	        for (int y = 0; y < field.amount(x); y++) {
	            m = search3Straight(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 2, y));
	            if (m != null) return m;
	            
	            m = search3Folded(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 2, y));
	            if (m != null) return m;
	        }
	    }
	    
	    // 縦一列検索
	    for (int x = 0; x < field.getWidth(); x++) {
	        for (int y = 0; y < field.amount(x) - 2; y++) {
	            m = search3Straight(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x, y + 2));
	            if (m != null) return m;
	            
	            m = search3Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x, y + 2));
	            if (m != null) return m;
	        }
	    }
	    
	    // L字型検索
	    for (int x = 0; x < field.getWidth() - 1; x++) {
	        for (int y = 0; y < Math.max(field.amount(x), field.amount(x+1)) - 1; y++) {
	            m = search3Folded(
	                    field.getMatter(x + 1, y), field.getMatter(x, y),
	                    field.getMatter(x, y + 1));
	            if (m != null) return m;
	            
	            m = search3Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x + 1, y + 1));
	            if (m != null) return m;
	            
	            m = search3Folded(
	                    field.getMatter(x, y + 1), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x + 1, y));
	            if (m != null) return m;
	            
	            m = search3Folded(
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 1, y),
	                    field.getMatter(x, y));
	            if (m != null) return m;
	        }
	    }	    
	    
	    return null;
	}
	
	public Molecule search4() {
	    Molecule m = null;
	    
	    // 横一列検索
	    for (int x = 0; x < field.getWidth() - 3; x++) {
	        for (int y = 0; y < field.amount(x); y++) {
	            m = search4Straight(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 2, y), field.getMatter(x + 3, y));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 2, y), field.getMatter(x + 3, y));
	            if (m != null) return m;
	        }
	    }
	    
	    // 縦一列検索
	    for (int x = 0; x < field.getWidth(); x++) {
	        for (int y = 0; y < field.amount(x) - 3; y++) {
	            m = search4Straight(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x, y + 2), field.getMatter(x, y + 3));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x, y + 2), field.getMatter(x, y + 3));
	            if (m != null) return m;
	        }
	    }
	    
	    // 横L/S/T字型検索
	    for (int x = 0; x < field.getWidth() - 2; x++) {
            for (int y = 0; y < GameUtil.max3(field.amount(x),
                    			    field.amount(x + 1), field.amount(x + 2)) - 1; y++) {
                // L字
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 2, y + 1));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 1), field.getMatter(x, y),
	                    field.getMatter(x + 1, y), field.getMatter(x + 2, y));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 1), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x + 2, y + 1), field.getMatter(x + 2, y));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 2, y), field.getMatter(x + 2, y + 1));
	            if (m != null) return m;
	            
	            // S字	            
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 2, y + 1));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 1), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x + 1, y), field.getMatter(x + 2, y));
	            if (m != null) return m;
	            
	            // T字
	            
	            m = search4T(
	                    field.getMatter(x + 1, y + 1), field.getMatter(x, y + 1),
	                    field.getMatter(x + 1, y), field.getMatter(x + 2, y + 1));
	            if (m != null) return m;
	            
	            m = search4T(
	                    field.getMatter(x + 1, y), field.getMatter(x, y),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 2, y));
	            if (m != null) return m;
	            
	        }
	    }
	    
	    // 縦L/S/ト字型検索
	    for (int x = 0; x < field.getWidth() - 1; x++) {
	        for (int y = 0; y < Math.max(field.amount(x), field.amount(x + 1)) - 2; y++) {
	            // L字
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x, y + 2), field.getMatter(x + 1, y + 2));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 2), field.getMatter(x, y + 1),
	                    field.getMatter(x, y), field.getMatter(x + 1, y));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x + 1, y), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x + 1, y + 2), field.getMatter(x, y + 2));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x + 1, y + 2), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x + 1, y), field.getMatter(x, y));
	            if (m != null) return m;
	            
	            // S字
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x, y + 1),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 1, y + 2));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 2), field.getMatter(x, y + 1),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 1, y));
	            if (m != null) return m;
	            
	            // ト字
	            
	            m = search4T(
	                    field.getMatter(x, y + 1), field.getMatter(x, y),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x, y + 2));
	            if (m != null) return m;
	            
	            m = search4T(
	                    field.getMatter(x + 1, y + 1), field.getMatter(x + 1, y),
	                    field.getMatter(x, y + 1), field.getMatter(x + 1, y + 2));
	            if (m != null) return m;
	        }
	    }
	    
	    // C字型検索
	    for (int x = 0; x < field.getWidth() - 1; x++) {
	        for (int y = 0; y < Math.max(field.amount(x), field.amount(x + 1)) - 1; y++) {
	            // ついでに水素核融合のロ型検索もここでやってしまう。
	            if ((m = searchNuclearR(x, y)) != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x + 1, y + 1), field.getMatter(x, y + 1),
	                    field.getMatter(x, y), field.getMatter(x, y + 1));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y + 1), field.getMatter(x, y),
	                    field.getMatter(x + 1, y), field.getMatter(x + 1, y + 1));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x, y), field.getMatter(x + 1, y),
	                    field.getMatter(x + 1, y + 1), field.getMatter(x, y + 1));
	            if (m != null) return m;
	            
	            m = search4Folded(
	                    field.getMatter(x + 1, y), field.getMatter(x + 1, y + 1),
	                    field.getMatter(x, y + 1), field.getMatter(x, y));
	            if (m != null) return m;
	        }
	    }
	    return null;
	}
	
	public Molecule search5() {
	    Molecule m = null;
	    
	    for (int x = 1; x < field.getWidth() - 1; x++) {
	        for (int y = 1; y < GameUtil.max3(field.amount(x - 1),
	                				field.amount(x) - 1, field.amount(x + 1)); y++) {
                m = search5(x, y);
                if (m != null) return m;
            }
	    }
	    
	    return null;
	}
	
	public Molecule searchComplex() {
	    Molecule m = null;
	    
	    for (int y = 1; y < field.amount(0); y++) {
	        m = searchAceticAcid(y);
	        if (m != null) return m;
	    }
	    
	    for (int x = 1; x < field.getWidth() - 1; x++) {
	        for (int y = 1; y < GameUtil.max3(field.amount(x - 1),
	                				field.amount(x) - 1, field.amount(x + 1)); y++) {
                m = searchMethanol(x, y);
                if (m != null) return m;
                m = searchFormicAcid(x, y);
                if (m != null) return m;
                m = searchNitricAcid(x, y);
                if (m != null) return m;
            }
	    }
	    
	    return null;
	}
	
	public Molecule search2(PlacedAtom p1, PlacedAtom p2) {
	    if (p1 == null || p2 == null) return null;
	    
	    byte m1 = p1.getMatter();
	    byte m2 = p2.getMatter();
	    
	    // 仮に削除マークをつけてしまう。
	    p1.markErace();
	    p2.markErace();
	   
	    // H H
	    if (configValue_.isGeneratable(Molecule.HYDROGEN.getId())
	            && m1 == Atom.HYDROGEN && m2 == Atom.HYDROGEN) {
	        return Molecule.HYDROGEN;
	    }
	    
	    // N N
	    else if (configValue_.isGeneratable(Molecule.NITROGEN.getId())
	            && m1 == Atom.NITROGEN && m2 == Atom.NITROGEN) {
	        return Molecule.NITROGEN;
	    }
	    
	    // O O
	    else if (configValue_.isGeneratable(Molecule.OXYGEN.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.OXYGEN) {
	        return Molecule.OXYGEN;
	    }
	    
	    // F F
	    else if (configValue_.isGeneratable(Molecule.FLORIDE.getId())
	            && m1 == Atom.FLORIDE && m2 == Atom.FLORIDE) {
	        return Molecule.FLORIDE;
	    }
	    
	    // ClCl
	    else if (configValue_.isGeneratable(Molecule.CHLORINE.getId())
	            && m1 == Atom.CHLORINE && m2 == Atom.CHLORINE) {
	        return Molecule.CHLORINE;
	    }
	    	    
	    // CO
	    else if (configValue_.isGeneratable(Molecule.CO.getId())
	            && m1 + m2 == Atom.CARBON + Atom.OXYGEN
	            && m1 * m2 == Atom.CARBON * Atom.OXYGEN) {
	        return Molecule.CO;
	    }

	    // NO
	    else if (configValue_.isGeneratable(Molecule.NO.getId())
	            && m1 + m2 == Atom.NITROGEN + Atom.OXYGEN
	            && m1 * m2 == Atom.NITROGEN * Atom.OXYGEN) {
	        return Molecule.NO;
	    }	    

	    // SO
	    else if (configValue_.isGeneratable(Molecule.SO.getId())
	            && m1 + m2 == Atom.SULFUR + Atom.OXYGEN
	            && m1 * m2 == Atom.SULFUR * Atom.OXYGEN) {
	        return Molecule.SO;
	    }
	    
	    // ClO
	    else if (configValue_.isGeneratable(Molecule.CLO.getId())
	            && m1 + m2 == Atom.CHLORINE + Atom.OXYGEN
	            && m1 * m2 == Atom.CHLORINE * Atom.OXYGEN) {
	        return Molecule.CLO;
	    }

	    // HF
	    else if (configValue_.isGeneratable(Molecule.HF.getId())
	            && m1 + m2 == Atom.HYDROGEN + Atom.FLORIDE
	            && m1 * m2 == Atom.HYDROGEN * Atom.FLORIDE) {
	        return Molecule.HF;
	    }
	    
	    // ClF
	    else if (configValue_.isGeneratable(Molecule.CLF.getId())
	            && m1 + m2 == Atom.CHLORINE + Atom.FLORIDE
	            && m1 * m2 == Atom.CHLORINE * Atom.FLORIDE) {
	        return Molecule.CLF;
	    }
	    
	    // CS
	    else if (configValue_.isGeneratable(Molecule.CS.getId())
	            && m1 + m2 == Atom.CARBON + Atom.SULFUR
	            && m1 * m2 == Atom.CARBON * Atom.SULFUR) {
	        return Molecule.CS;
	    }
	    
	    // HCl
	    else if (configValue_.isGeneratable(Molecule.HCL.getId())
	            && m1 + m2 == Atom.HYDROGEN + Atom.CHLORINE
	            && m1 * m2 == Atom.HYDROGEN * Atom.CHLORINE) {
	        return Molecule.HCL;
	    }

	    else {
	        p1.unmarkErace();
	        p2.unmarkErace();
	        return null;
	    }
	}
	
	public Molecule search3Straight(PlacedAtom p1, PlacedAtom p2, PlacedAtom p3) {
	    if (p1 == null || p2 == null || p3 == null) return null;
	    
	    byte m1 = p1.getMatter();
	    byte m2 = p2.getMatter();
	    byte m3 = p3.getMatter();
	    
	    // 仮に削除マークをつけてしまう。
	    p1.markErace();
	    p2.markErace();
	    p3.markErace();
	    
	    // CO2
	    if (configValue_.isGeneratable(Molecule.CO2.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.CARBON && m3 == Atom.OXYGEN) {
	        return Molecule.CO2;
	    }

	    // CNF
	    else if (configValue_.isGeneratable(Molecule.CNF.getId())
	            && m2 == Atom.CARBON
	            && m1 + m3 == Atom.NITROGEN + Atom.FLORIDE
	            && m1 * m3 == Atom.NITROGEN * Atom.FLORIDE) {
	        return Molecule.CNF;
	    }

	    // CNCl
	    else if (configValue_.isGeneratable(Molecule.CNCL.getId())
	            && m2 == Atom.CARBON
	            && m1 + m3 == Atom.NITROGEN + Atom.CHLORINE
	            && m1 * m3 == Atom.NITROGEN * Atom.CHLORINE) {
	        return Molecule.CNCL;
	    }
	    
	    // CS2
	    else if (configValue_.isGeneratable(Molecule.NO2.getId())
	            && m2 == Atom.CARBON
	            && m1 + m3 == Atom.SULFUR + Atom.SULFUR
	            && m1 * m3 == Atom.SULFUR * Atom.SULFUR) {
	        return Molecule.CS2;
	    }
	    
	    // HCN
	    else if (configValue_.isGeneratable(Molecule.HCN.getId())
	            && m2 == Atom.CARBON
	            && m1 + m3 == Atom.HYDROGEN + Atom.NITROGEN
	            && m1 * m3 == Atom.HYDROGEN * Atom.NITROGEN) {
	        return Molecule.HCN;
	    }
	    
	    else {
	        p1.unmarkErace();
	        p2.unmarkErace();
	        p3.unmarkErace();
	        return null;
	    }
	}
	
	public Molecule search3Folded(PlacedAtom p1, PlacedAtom p2, PlacedAtom p3) {
	    if (p1 == null || p2 == null || p3 == null) return null;
	    
	    byte m1 = p1.getMatter();
	    byte m2 = p2.getMatter();
	    byte m3 = p3.getMatter();
	    
	    // 仮に削除マークをつけてしまう。
	    p1.markErace();
	    p2.markErace();
	    p3.markErace();
	    
	    // NO2
	    if (configValue_.isGeneratable(Molecule.NO2.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.NITROGEN && m3 == Atom.OXYGEN) {
	        return Molecule.NO2;
	    }

	    // SO2
	    else if (configValue_.isGeneratable(Molecule.SO2.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.SULFUR && m3 == Atom.OXYGEN) {
	        return Molecule.SO2;
	    }
	    
	    // ClO2
	    else if (configValue_.isGeneratable(Molecule.CLO2.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.CHLORINE && m3 == Atom.OXYGEN) {
	        return Molecule.CLO2;
	    }
	    
	    // O3
	    else if (configValue_.isGeneratable(Molecule.OZONE.getId())
	            && m1 == Atom.OXYGEN && m2 == Atom.OXYGEN && m3 == Atom.OXYGEN) {
	        return Molecule.OZONE;
	    }
	    
	    // H2O
	    else if (configValue_.isGeneratable(Molecule.WATER.getId())
	            && m1 == Atom.HYDROGEN && m2 == Atom.OXYGEN && m3 == Atom.HYDROGEN) {
	        return Molecule.WATER;
	    }
	    
	    // F2O
	    else if (configValue_.isGeneratable(Molecule.F2O.getId())
	            && m1 == Atom.FLORIDE && m2 == Atom.OXYGEN && m3 == Atom.FLORIDE) {
	        return Molecule.F2O;
	    }
	    
	    // Cl2O
	    else if (configValue_.isGeneratable(Molecule.CL2O.getId())
	            && m1 == Atom.CHLORINE && m2 == Atom.OXYGEN && m3 == Atom.CHLORINE) {
	        return Molecule.CL2O;
	    }
	    
	    // SCl2
	    else if (configValue_.isGeneratable(Molecule.SCL2.getId())
	            && m1 == Atom.CHLORINE && m2 == Atom.SULFUR && m3 == Atom.CHLORINE) {
	        return Molecule.SCL2;
	    }
	    
	    // H2O
	    else if (configValue_.isGeneratable(Molecule.H2S.getId())
	            && m1 == Atom.HYDROGEN && m2 == Atom.SULFUR && m3 == Atom.HYDROGEN) {
	        return Molecule.H2S;
	    }
	    
	    // HClO
	    else if (configValue_.isGeneratable(Molecule.HCLO.getId())
	            && m2 == Atom.OXYGEN
	            && m1 + m3 == Atom.HYDROGEN + Atom.CHLORINE
	            && m1 * m3 == Atom.HYDROGEN * Atom.CHLORINE) {
	        return Molecule.HCLO;
	    }
	    	    
	    // HCN
	    else if (configValue_.isGeneratable(Molecule.HCN.getId())
	            && m2 == Atom.NITROGEN
	            && m1 + m3 == Atom.HYDROGEN + Atom.CARBON
	            && m1 * m3 == Atom.HYDROGEN * Atom.CARBON) {
	        return Molecule.HCN;
	    }
	    
	    else {
	        p1.unmarkErace();
	        p2.unmarkErace();
	        p3.unmarkErace();
	        return null;
	    }
	}
	
	public Molecule search4T(PlacedAtom p1, PlacedAtom p2, PlacedAtom p3, PlacedAtom p4) {
	    if (p1 == null || p2 == null || p3 == null || p4 == null) return null;
	    
	    byte m1 = p1.getMatter(); // 中心
	    byte m2 = p2.getMatter();
	    byte m3 = p3.getMatter();
	    byte m4 = p4.getMatter();
	    Molecule result = null;
	    
	    if (m2 == m3 && m3 == m4) {
	        
	        // 水素核融合
	        if (configValue_.isGeneratable(Molecule.NUCLEAR.getId())
	                && m1 == Atom.HYDROGEN && m2 == Atom.HYDROGEN) {
	            result = Molecule.NUCLEAR;
	        }	        
	        
	        // NH3
	        if (configValue_.isGeneratable(Molecule.AMMONIA.getId())
	                && m1 == Atom.NITROGEN && m2 == Atom.HYDROGEN) {
	            result = Molecule.AMMONIA;
	        }
	        
	        // SO3
	        else if (configValue_.isGeneratable(Molecule.SO3.getId())
	                && m1 == Atom.SULFUR && m2 == Atom.OXYGEN) {
	            result = Molecule.SO3;
	        }
	        
	        // NO3
	        else if (configValue_.isGeneratable(Molecule.NO3.getId())
	                && m1 == Atom.NITROGEN && m2 == Atom.OXYGEN) {
	            result = Molecule.NO3;
	        }
	        
	        // NCl3
	        else if (configValue_.isGeneratable(Molecule.NCL3.getId())
	                && m1 == Atom.NITROGEN && m2 == Atom.CHLORINE) {
	            result = Molecule.NCL3;
	        }
	    }
	    
	    // ホルムアルデヒド検索
	    else if (configValue_.isGeneratable(Molecule.METHANAL.getId())
                && (m1 == Atom.CARBON)
                && (
	                (m2 == Atom.OXYGEN && m3 == Atom.HYDROGEN && m4 == Atom.HYDROGEN)
	                || (m2 == Atom.HYDROGEN
	                		&& m3 + m4 == Atom.HYDROGEN + Atom.OXYGEN
	                		&& m3 * m4 == Atom.HYDROGEN * Atom.OXYGEN)
	        		)
	            ) {
	        result = Molecule.METHANAL;
	    }

	    if (result != null) {
		    p1.markErace();
		    p2.markErace();
		    p3.markErace();
		    p4.markErace();
	    }
	    return result;
	}
	
	public Molecule search4Folded(PlacedAtom p1, PlacedAtom p2, PlacedAtom p3, PlacedAtom p4) {
	    if (p1 == null || p2 == null || p3 == null || p4 == null) return null;
	    
	    byte m1 = p1.getMatter();
	    byte m2 = p2.getMatter();
	    byte m3 = p3.getMatter();
	    byte m4 = p4.getMatter();
	    Molecule result = null;
	    
	    if (m1 == m4 && m2 == m3) {
	        
	        // HOOH
	        if (configValue_.isGeneratable(Molecule.H2O2.getId())
	                && m1 == Atom.HYDROGEN && m2 == Atom.OXYGEN) {
	            result = Molecule.H2O2;
	        }
	        
	        // ClSSCl
	        else if (configValue_.isGeneratable(Molecule.S2CL2.getId())
	                && m1 == Atom.CHLORINE && m2 == Atom.SULFUR) {
	            result = Molecule.S2CL2;
	        }
	    }
	    
	    // HONO
	    else if (configValue_.isGeneratable(Molecule.HNO2.getId())
                && (
                    (
		            	m1 == Atom.HYDROGEN
		            	&& m2 == Atom.OXYGEN
		            	&& m3 == Atom.NITROGEN
		            	&& m4 == Atom.OXYGEN
		            ) || (
	                    m4 == Atom.HYDROGEN
	                    && m3 == Atom.OXYGEN
		            	&& m2 == Atom.NITROGEN
		            	&& m1 == Atom.OXYGEN
		            )
	            )) {
	        result = Molecule.HNO2;
	    }
	    
	    if (result != null) {
		    p1.markErace();
		    p2.markErace();
		    p3.markErace();
		    p4.markErace();
	    }
	    return result;
	}
	
	public Molecule search4Straight(PlacedAtom p1, PlacedAtom p2, PlacedAtom p3, PlacedAtom p4) {
	    if (p1 == null || p2 == null || p3 == null | p4 == null) return null;
	    
	    byte m1 = p1.getMatter();
	    byte m2 = p2.getMatter();
	    byte m3 = p3.getMatter();
	    byte m4 = p4.getMatter();
	    Molecule result = null;
	    
	    if (m1 == m4 && m2 == m3) {
	        
	        // HCCH
	        if (configValue_.isGeneratable(Molecule.ETHINE.getId())
	                && m1 == Atom.HYDROGEN && m2 == Atom.CARBON) {
	            result = Molecule.ETHINE;
	        }
	        
	        // NCCN
	        else if (configValue_.isGeneratable(Molecule.CYAN.getId())
	                && m1 == Atom.NITROGEN && m2 == Atom.CARBON) {
	            result = Molecule.CYAN;
	        }
	    }
	    
	    if (result != null) {
		    p1.markErace();
		    p2.markErace();
		    p3.markErace();
		    p4.markErace();
	    }
	    return result;
	}
	
	public Molecule search5(int x, int y) {
	    PlacedAtom p0 = field.getMatter(x, y);
	    PlacedAtom p1 = field.getMatter(x - 1, y);
	    PlacedAtom p2 = field.getMatter(x + 1, y);
	    PlacedAtom p3 = field.getMatter(x, y - 1);
	    PlacedAtom p4 = field.getMatter(x, y + 1);
	    
	    if (p0 == null || p1 == null || p2 == null || p3 == null | p4 == null)
	        return null;
	    
	    
	    byte[] m = { p0.getMatter(), p1.getMatter(), p2.getMatter(),
                p3.getMatter(), p4.getMatter() };
	    
	    Molecule result = null;
	    
	    if (m[0] == Atom.CARBON) {
	        int h_count  = 0;
	        int f_count  = 0;
	        int cl_count = 0;
	        
	        // H, F, Clの数
	        for (int i = 1; i < m.length; i++) {
	            if (m[i] == Atom.HYDROGEN)
                    h_count++;
                else if (m[i] == Atom.FLORIDE)
                    f_count++;
                else if (m[i] == Atom.CHLORINE)
                    cl_count++;
	        }
	        
	        // メタン類
	        if (configValue_.isGeneratable(Molecule.METHANE.getId())
	                && h_count == 4)
	            result = Molecule.METHANE;
	        
	        else if (configValue_.isGeneratable(Molecule.CMETHANE.getId())
	                && h_count == 3 && cl_count == 1)
	        	result = Molecule.CMETHANE;
	        
	        else if (configValue_.isGeneratable(Molecule.C2METHANE.getId())
	                && h_count == 2 && cl_count == 2)
	            result = Molecule.C2METHANE;
	        
	        else if (configValue_.isGeneratable(Molecule.C3METHANE.getId())
	                && h_count == 1 && cl_count == 3)
	            result = Molecule.C3METHANE;
	        
	        else if (configValue_.isGeneratable(Molecule.C4METHANE.getId())
	                && cl_count == 4)
	            result = Molecule.C4METHANE;
	        
	        // フロンガス類
	        else if (configValue_.isGeneratable(Molecule.FRON11.getId())
	                && f_count == 1 && cl_count == 3)
	            result = Molecule.FRON11;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON12.getId())
	                && f_count == 2 && cl_count == 2)
	            result = Molecule.FRON12;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON13.getId())
	                && f_count == 3 && cl_count == 1)
	            result = Molecule.FRON13;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON14.getId())
	                && f_count == 4)
	            result = Molecule.FRON14;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON21.getId())
	                && h_count == 1 && f_count == 1 && cl_count == 2)
	            result = Molecule.FRON21;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON22.getId())
	                && h_count == 1 && f_count == 2 && cl_count == 1)
	            result = Molecule.FRON22;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON23.getId())
	                && h_count == 1 && f_count == 3)
	            result = Molecule.FRON23;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON31.getId())
	                && h_count == 2 && f_count == 1 && cl_count == 1)
	            result = Molecule.FRON31;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON32.getId())
	                && h_count == 2 && f_count == 2)
	            result = Molecule.FRON32;
	        
	        else if (configValue_.isGeneratable(Molecule.FRON41.getId())
	                && h_count == 3 && f_count == 1)
	            result = Molecule.FRON41;
	    }
	    
	    if (result != null) {
	        p0.markErace();
		    p1.markErace();
		    p2.markErace();
		    p3.markErace();
		    p4.markErace();
	    }
	    return result;
	}
	
	// メタノール検索
	public Molecule searchMethanol(int x, int y) {
	    
	    if (!configValue_.isGeneratable(Molecule.METHANOL.getId())) return null;
	    
	    PlacedAtom[] p = new PlacedAtom[6];
	    p[0]      = field.getMatter(x, y);
	    p[LEFT]   = field.getMatter(x - 1, y);
	    p[RIGHT]  = field.getMatter(x + 1, y);
	    p[TOP]    = field.getMatter(x, y + 1);
	    p[BOTTOM] = field.getMatter(x, y - 1);
	    p[SUPPLIMENT] = null;
	    
	    byte[] m = new byte[5];
	    for (int i = 0; i < m.length; i++) {
	        if (p[i] != null) m[i] = p[i].getMatter();
	        else return null; // nullが一個でもあったらおしまい。
	    }
	    
	    if (m[0] == Atom.CARBON) {
	        int h_count  = 0;
	        int oh_count  = 0;
	        
	        // H, Oの数
	        for (int i = 1; i < 5; i++) {
	            if (m[i] == Atom.HYDROGEN)
                    h_count++;
                else if (m[i] == Atom.OXYGEN) {
                    if ((p[SUPPLIMENT] = searchOH(x, y, i)) != null) {
                        oh_count++;
                    } else {
                        return null; // OH基がないので不可。
                    }

                }
	        }
	        
	        if (h_count == 3 && oh_count == 1) {
	            for (int i = 0; i < p.length; i++) {
	                p[i].markErace();
	            }
	            return Molecule.METHANOL;
	        }
	    }
	    
	    return null;
	}
	
	// 蟻酸検索
	public Molecule searchFormicAcid(int x, int y) {
	    
	    if (!configValue_.isGeneratable(Molecule.FORMICACID.getId())) return null;
	    
	    PlacedAtom[] p = new PlacedAtom[6];
	    p[0]      = field.getMatter(x, y);
	    p[LEFT]   = field.getMatter(x - 1, y);
	    p[RIGHT]  = field.getMatter(x + 1, y);
	    p[TOP]    = field.getMatter(x, y + 1);
	    p[BOTTOM] = field.getMatter(x, y - 1);
	    p[SUPPLIMENT] = null;
	    
	    byte[] m = new byte[5];
	    for (int i = 0; i < m.length; i++) {
	        if (p[i] != null) m[i] = p[i].getMatter();
	        else m[i] = EMPTY;
	    }
	    
	    if (m[0] == Atom.CARBON) {
	        int h_count  = 0;
	        int o_count  = 0;
	        int oh_count  = 0;
	        
	        // H, Oの数
	        for (int i = 1; i < 5; i++) {
	            if (m[i] == Atom.HYDROGEN && h_count < 1)
                    h_count++;
                else if (m[i] == Atom.OXYGEN) {
                    if (p[SUPPLIMENT] == null && (p[SUPPLIMENT] = searchOH(x, y, i)) != null) {
                        oh_count++;
                    } else if (o_count < 1) {
                        o_count++;
                    } else {
                        p[i] = null; // 消える対象から外す。
                    }
                } else {
                    p[i] = null; // 消える対象から外す。
                }
                
                if (h_count == 1 && o_count == 1 && oh_count == 1) {
    	            for (int j = 0; j <= i; j++) {
    	                if (p[j] != null) p[j].markErace();
    	            }
    	            p[SUPPLIMENT].markErace();
    	            return Molecule.FORMICACID;
    	        }
	        }
	        
	        
	    }
	    
	    return null;
	}
	
	// 硝酸検索
	public Molecule searchNitricAcid(int x, int y) {
	    
	    if (!configValue_.isGeneratable(Molecule.NITRICACID.getId())) return null;
	    
	    
	    
	    PlacedAtom[] p = new PlacedAtom[6];
	    p[0]      = field.getMatter(x, y);
	    p[LEFT]   = field.getMatter(x - 1, y);
	    p[RIGHT]  = field.getMatter(x + 1, y);
	    p[TOP]    = field.getMatter(x, y + 1);
	    p[BOTTOM] = field.getMatter(x, y - 1);
	    p[SUPPLIMENT] = null;
	    
	    byte[] m = new byte[5];
	    for (int i = 0; i < m.length; i++) {
	        if (p[i] != null) m[i] = p[i].getMatter();
	        else m[i] = EMPTY;
	    }
	    
	    if (m[0] == Atom.NITROGEN) {
	        int o_count  = 0;
	        int oh_count  = 0;
	        
	        // H, Oの数
	        for (int i = 1; i < 5; i++) {
                if (m[i] == Atom.OXYGEN) {
                    if (p[SUPPLIMENT] == null && (p[SUPPLIMENT] = searchOH(x, y, i)) != null) {
                        oh_count++;
                    } else if (o_count < 2) {
                        o_count++;
                    } else {
                        p[i] = null; // 消える対象から外す。
                    }
                } else {
                    p[i] = null; // 消える対象から外す。
                }
                
                if (o_count == 2 && oh_count == 1) {
    	            for (int j = 0; j <= i; j++) {
    	                if (p[j] != null) p[j].markErace();
    	            }
    	            p[SUPPLIMENT].markErace();
    	            return Molecule.NITRICACID;
    	        }
	        }
	    }
	    
	    return null;
	}
	
	// 硝酸検索
	public Molecule searchAceticAcid(int y) {
	    
	    if (!configValue_.isGeneratable(Molecule.ACETICACID.getId())) return null;
	    
	    PlacedAtom[] p = new PlacedAtom[8];
	    
	    p[0] = field.getMatter(0, y);
	    p[1] = field.getMatter(1, y);
	    p[2] = field.getMatter(2, y);
	    p[3] = field.getMatter(3, y);
	    p[4] = field.getMatter(4, y);
	    p[5] = field.getMatter(1, y + 1);
	    p[6] = field.getMatter(1, y - 1);
	    p[7] = field.getMatter(2, y - 1);
	    
	    if (searchAceticAcid(p))
	        return Molecule.ACETICACID;
	    
	    p[5] = field.getMatter(1, y - 1);
	    p[6] = field.getMatter(1, y + 1);
	    p[7] = field.getMatter(2, y + 1);
	    
	    if (searchAceticAcid(p))
	        return Molecule.ACETICACID;
	    
	    p[0] = field.getMatter(4, y);
	    p[1] = field.getMatter(3, y);
	    p[2] = field.getMatter(2, y);
	    p[3] = field.getMatter(1, y);
	    p[4] = field.getMatter(0, y);
	    p[5] = field.getMatter(3, y + 1);
	    p[6] = field.getMatter(3, y - 1);
	    p[7] = field.getMatter(2, y - 1);
	    
	    if (searchAceticAcid(p))
	        return Molecule.ACETICACID;
	    
	    p[5] = field.getMatter(3, y - 1);
	    p[6] = field.getMatter(3, y + 1);
	    p[7] = field.getMatter(2, y + 1);
	    
	    if (searchAceticAcid(p))
	        return Molecule.ACETICACID;
	    
	    // どれにも該当しない場合ここへ到達。
	    return null;
	}
	
	private boolean searchAceticAcid(PlacedAtom[] p) {
	    if (getAtomMatter(p[0]) == Atom.HYDROGEN
	            && getAtomMatter(p[1]) == Atom.CARBON
	            && getAtomMatter(p[2]) == Atom.CARBON
	            && getAtomMatter(p[3]) == Atom.OXYGEN
	            && getAtomMatter(p[4]) == Atom.HYDROGEN
	        	&& getAtomMatter(p[5]) == Atom.HYDROGEN
		        && getAtomMatter(p[6]) == Atom.HYDROGEN
		        && getAtomMatter(p[7]) == Atom.OXYGEN) {
	        for (int i = 0; i < p.length; i++) {
	            p[i].markErace();
	        }
		    return true;
		} else {
		    return false;
		}
	}
	
	private byte getAtomMatter(Atom atom) {
	    return atom == null ? -1 : atom.getMatter();
	}
	
	private PlacedAtom searchOH(int x, int y, int direction) {
	    PlacedAtom tmp;
	    switch (direction) {
	    
	    case LEFT:
            if (
                    ((tmp = field.getMatter(x - 1, y + 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x - 2, y)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x - 1, y - 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    ) {
                return tmp;
            }
            
            
        case RIGHT:
            if (
                    ((tmp = field.getMatter(x + 1, y + 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x + 2, y)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x + 1, y - 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    ) {
                return tmp;
            }
            break;
            
	    case TOP:
	        if (((tmp = field.getMatter(x - 1, y + 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
	                || ((tmp = field.getMatter(x, y + 2)) != null && tmp.getMatter() == Atom.HYDROGEN)
	                || ((tmp = field.getMatter(x + 1, y + 1)) != null && tmp.getMatter() == Atom.HYDROGEN)) {
	            return tmp;
	        }
            break;
            
        case BOTTOM:
            if (((tmp = field.getMatter(x - 1, y - 1)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x, y - 2)) != null && tmp.getMatter() == Atom.HYDROGEN)
                    || ((tmp = field.getMatter(x + 1, y - 1)) != null && tmp.getMatter() == Atom.HYDROGEN)) {
                return tmp;
            }
            break;
	    }
	    return null;
	}
	
	// ロ型水素核融合検索
	public Molecule searchNuclearR(int x, int y) {
	    
	    if (!configValue_.isGeneratable(Molecule.NUCLEAR.getId())) return null;
	    
	    
	    PlacedAtom[] p = {
	            field.getMatter(x, y),
	            field.getMatter(x + 1, y),
	            field.getMatter(x, y + 1),
	            field.getMatter(x + 1, y + 1)
	    };
	    
	    if (p[0] == null || p[1] == null || p[2] == null || p[3] == null) {
	        return null;
	    }
	    
	    if (p[0].getMatter() == Atom.HYDROGEN
	            && p[1].getMatter() == Atom.HYDROGEN
	            && p[2].getMatter() == Atom.HYDROGEN
	            && p[3].getMatter() == Atom.HYDROGEN) {
	        p[0].markErace();
	        p[1].markErace();
	        p[2].markErace();
	        p[3].markErace();
	        return Molecule.NUCLEAR;
	    }
	    
	    return null;
	}
	
	public void generate(Molecule molecule) {
	    gameResult.generate(molecule.getId());
	    
	    fallwait += molecule.getSpeedEffect();
	    if (fallwait > 3000) {
	        gameResult.addScore((fallwait - 3000) * 10);
	        fallwait = 3000;
	    } else if (fallwait < 100) {
	        gameResult.addScore((fallwait - 100) / 10);
	        fallwait = 100;
	    }
	    
	    // 水素核融合ではエネルギーがたまる。
	    if (molecule == Molecule.NUCLEAR) {
	        energy += 100;
	    }
	    
	    seriesCount++;
	    gameResult.inputMaxSeries(seriesCount);
	    
	    if (seriesCount >= 5) {
	        energy += 10;
	    } else if (seriesCount >= 10) {
	        energy += 20;
	    }
	    
	    if (mission.getId() == Mission.SERIES && seriesCount >= mission.getParam()) {
	        success();
	    } else if (mission.getId() == Mission.GENERATE) {
	        if (mission.getParam() >= Molecule.CMETHANE.getId()
	                && mission.getParam() <= Molecule.C4METHANE.getId()
	                && molecule.getId() >= Molecule.CMETHANE.getId()
	                && molecule.getId() <= Molecule.C4METHANE.getId())
	            success();
	        else if (mission.getParam() >= Molecule.FRON11.getId()
	                && mission.getParam() <= Molecule.FRON41.getId()
	                && molecule.getId() >= Molecule.FRON11.getId()
	                && molecule.getId() <= Molecule.FRON41.getId())
	            success();
	        else if (molecule.getId() == mission.getParam()) {
	            success();
	        }
	    }
	    
	    gameResult.addScore((20 * (3001 - fallwait) / 1500  + molecule.getBonus()) * seriesCount);
	}
	
	public void close() {
	    for (int x = 0; x < field.getWidth(); x++) {
	        for (int y = 0; y < field.amount(x); y++) {
	            if (field.getMatter(x, y).isMarkedErace()) {
	                field.close(x, y);
	                y--;
	            }
	        }
	    }
	    
	    // 全消し判定
	    if (mission.getId() == Mission.ALLELACE) {
		    boolean flag = true;
		    for (int x = 0; x < field.getWidth(); x++) {
		        flag = flag && field.amount(x) == 0;
		    }
		    if (flag) {
		        success();
		    }
	    }
	}
	
	// 指令完了
	private void success() {
	    gameResult.addScore(10000);
	    gameResult.success();
	    mission.success();
	}
	
	public String showMission() {
	    return mission.toString();
	}
	
	// エネルギーを使う
	public void useEnergy(byte param) {
	    if (param == 0) {
	        gameResult.addScore(energy * energy);
	        energy = 0;
	    }
	    
	    // 炭素を窒素に
	    else if (param == 1 && energy >= 100) {
	        energy -= 50;
	        field.energize(param);
	        movingAtom.energize(param);
	        
	    }
	    
	    // 窒素を酸素に
	    else if (param == 2 && energy >= 100) {
	        energy -= 50;
	        field.energize(param);
	        movingAtom.energize(param);
	    }
	    
	    // 硫黄を塩素に
	    else if (param == 3 && energy >= 100) {
	        energy -= 50;
	        field.energize(param);
	        movingAtom.energize(param);
	    }
	    
	    // 炭素を酸素に（大損コマンド）
	    else if (param == 4 && energy >= 300) {
	        energy -= 150;
	        field.energize(param);
	        movingAtom.energize(param);
	    }
	    
	    // 黒鉛・ダイヤモンドを炭素原子に。
	    else if (param == 5 && energy >= 10) {
	        energy -= 5;
	        field.energize(param);
	        movingAtom.energize(param);
	    }
	    
	    // 落下抑制度の回復
	    else if (param == 6 && energy >= 1) {
	        energy--;
	        fallwait += 10;
	    }
	}
}