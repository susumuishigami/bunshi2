/*------------------------------------------------------
 �t�@�C����  Atom.java
 ���t        2004/07/25
 �쐬��      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * ���q��\���N���X
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
        "�g", "�b", "�m", "�n", "�e", "�r", "Cl", "��", "��" 
    };

    private byte kind_;
    
    /** @param kind ���f�̎�� */
    public Atom(byte kind) {
        kind_ = kind;        
    }
    
    /** �\���p�e�L�X�g�𓾂� */
    public String toString() {
        return AtomString[kind_];
    }
    
    /** ���f�̎�ނ𓾂� */
    public byte getMatter() {
        return kind_;
    }
    
    /** �G�l���M�[��^����B */
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
    
    /** �G�l���M�[��^����B */
    public void energize_weak() {
        switch (kind_) {
        	case GRAPHITE: kind_ = CARBON;    break;
        	case DIAMOND  : kind_ = GRAPHITE; break;
        }
    }
}