/*------------------------------------------------------
 ファイル名  Atom.java
 日付        2004/07/25
 作成者      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * 原子を表すクラス
 * @author Susumu ISHIGAMI
 * @version 0.4-7/25
 */
public abstract class Atom {
    public static final byte HYDROGEN  = 0;
    public static final byte CARBON    = 1;
    public static final byte NITROGEN  = 2;
    public static final byte OXYGEN    = 3;
    public static final byte FLORIDE   = 4;
    public static final byte SULFUR    = 5;
    public static final byte CHLORINE  = 6;
    public static final byte GRAPHITE  = 7;
    public static final byte DIAMOND   = 8;
    public static final byte SPECIES   = 9;
    
    private static final String[] AtomString = {
        "Ｈ", "Ｃ", "Ｎ", "Ｏ", "Ｆ", "Ｓ", "Cl", "◆", "◇" 
    };

    private byte kind_;
    
    /** @param kind 元素の種類 */
    public Atom(byte kind) {
        kind_ = kind;        
    }
    
    /** 表示用テキストを得る */
    public String toString() {
        return AtomString[kind_];
    }
    
    /** 元素の種類を得る */
    public byte getMatter() {
        return kind_;
    }
    
    /** エネルギーを与える。 */
    public void energize(int param) {
        switch (param) {
        case 1:
            if (kind_ == CARBON || kind_ == GRAPHITE || kind_ == DIAMOND) {
                kind_ = NITROGEN;
            }
            break;
        case 2:
            if (kind_ == NITROGEN) {
	            kind_ = OXYGEN;
            }
            break;
        case 3:
            if (kind_ == SULFUR) {
	            kind_ = CHLORINE;
            }
            break;
        case 4:
            if (kind_ == CARBON || kind_ == GRAPHITE || kind_ == DIAMOND) {
                kind_ = OXYGEN;
            }
            break;
        case 5:
            if (kind_ == GRAPHITE || kind_ == DIAMOND) {
                kind_ = CARBON;
            }
            break;
        }
    }
    
    /** エネルギーを与える。 */
    public void energize_weak() {
        switch (kind_) {
        	case GRAPHITE: kind_ = CARBON;    break;
        	case DIAMOND  : kind_ = GRAPHITE; break;
        }
    }
}