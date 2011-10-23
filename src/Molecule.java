/*------------------------------------------------------
 �t�@�C����  Molecule.java
 ���t        2004/08/15
 �쐬��      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * ���q�^
 * @author Susumu ISHIGAMI
 * @version 0.2.5-8/21
 */
public final class Molecule {
    public static final Molecule HYDROGEN = new Molecule(0,  "���f", 0, 20);
    public static final Molecule NITROGEN = new Molecule(1,  "���f", 0, 80);
    public static final Molecule OXYGEN   = new Molecule(2,  "�_�f", 0, 300);
    public static final Molecule FLORIDE  = new Molecule(3,  "���f", 0, -50);
    public static final Molecule CHLORINE = new Molecule(4,  "���f", 0, -20);
    
    public static final Molecule CO       = new Molecule(5,  "��_���Y�f", 0, -70);
    public static final Molecule NO       = new Molecule(6,  "��_�����f", 0, -50);
    public static final Molecule SO       = new Molecule(7,  "��_������", 0, -30);
    public static final Molecule CLO      = new Molecule(8,  "��_�����f", 0, -50);
    public static final Molecule HF       = new Molecule(9,  "�������f", 0, -40);
    
    public static final Molecule CLF      = new Molecule(10, "�������f", 0, -40);
    public static final Molecule CS       = new Molecule(11, "�ꗰ���Y�f", 0, -20);
    public static final Molecule HCL      = new Molecule(12, "�������f", 0, -40);
    // 3�ȏ�---------------------------------------------------------------------
    public static final Molecule CO2      = new Molecule(13, "��_���Y�f", 100, -5);
    public static final Molecule NO2      = new Molecule(14, "��_�����f", 100, -60);
    
    public static final Molecule SO2      = new Molecule(15, "��_������", 100, -50);
    public static final Molecule CLO2     = new Molecule(16, "��_�����f", 100, -50);
    public static final Molecule OZONE    = new Molecule(17, "�I�]��", 100, -40);
    public static final Molecule WATER    = new Molecule(18, "�����C", 100, 400);
    public static final Molecule F2O      = new Molecule(19, "�_�����f", 100, -60);
    
    public static final Molecule CL2O     = new Molecule(20, "��_���񉖑f", 100, -20);
    public static final Molecule SCL2     = new Molecule(21, "�񉖉�����", 100, -40);
    public static final Molecule H2S      = new Molecule(22, "�������f", 100, -100);
    public static final Molecule CS2      = new Molecule(23, "�񗰉��Y�f", 100, -40);
    public static final Molecule HCN      = new Molecule(24, "��݉����f", 100, -100);
    
    public static final Molecule CNF      = new Molecule(25, "�����V�A��", 100, -100);
    public static final Molecule CNCL     = new Molecule(26, "�����V�A��", 100, -100);
    public static final Molecule HCLO     = new Molecule(27, "�������f�_", 100, -30);
    // 4�ȏ�---------------------------------------------------------------------
    public static final Molecule AMMONIA  = new Molecule(28, "�A�����j�A", 300, -30);
    public static final Molecule SO3      = new Molecule(29, "�O�_������", 300, -30);
    
    public static final Molecule NO3      = new Molecule(30, "�O�_�����f", 300, -10);
    public static final Molecule NCL3     = new Molecule(31, "�������f", 300, -20);
    public static final Molecule METHANAL = new Molecule(32, "��ѱ������", 300, -20);
    public static final Molecule H2O2     = new Molecule(33, "�ߎ_�����f", 300, -10);
    public static final Molecule S2CL2    = new Molecule(34, "�ꉖ������", 300, -30);
    
    public static final Molecule HNO2     = new Molecule(35, "���Ɏ_", 300, -20);
    public static final Molecule ETHINE   = new Molecule(36, "�A�Z�`����", 300, 0);
    public static final Molecule CYAN     = new Molecule(37, "�V�A��", 300, -50);
    // 5�ȏ�---------------------------------------------------------------------
    public static final Molecule METHANE  = new Molecule(38, "���^��", 1000, -10);
    public static final Molecule CMETHANE = new Molecule(39, "������", 1000, -10);
    
    public static final Molecule C2METHANE= new Molecule(40, "�޸�����", 1000, -20);
    public static final Molecule C3METHANE= new Molecule(41, "�ظ�����", 1000, -20);
    public static final Molecule C4METHANE= new Molecule(42, "��׸�����", 1000, -20);
    public static final Molecule FRON11   = new Molecule(43, "�t����11", 1000, -20);
    public static final Molecule FRON12   = new Molecule(44, "�t����12", 1000, -20);
    
    public static final Molecule FRON13   = new Molecule(45, "�t����13", 1000, -20);
    public static final Molecule FRON14   = new Molecule(46, "�t����14", 1000, -20);
    public static final Molecule FRON21   = new Molecule(47, "�t����21", 1000, -20);
    public static final Molecule FRON22   = new Molecule(48, "�t����22", 1000, -20);
    public static final Molecule FRON23   = new Molecule(49, "�t����23", 1000, -20);
    
    public static final Molecule FRON31   = new Molecule(50, "�t����31", 1000, -20);
    public static final Molecule FRON32   = new Molecule(51, "�t����32", 1000, -20);
    public static final Molecule FRON41   = new Molecule(52, "�t����41", 1000, -20);
    // 6�ȏ�---------------------------------------------------------------------
    public static final Molecule METHANOL = new Molecule(53, "���^�m�[��", 3000, -20);
    public static final Molecule FORMICACID = new Molecule(54, "�a�_", 3000, -20);
    
    public static final Molecule NITRICACID = new Molecule(55, "�Ɏ_", 3000, -20);
    public static final Molecule ACETICACID = new Molecule(56, "�|�_", 5000, -20);
    public static final Molecule NUCLEAR    = new Molecule(57, "���f�j�Z��", 300, 0);
    
    
    
    
    public static final Molecule[] MOLECULES = {
        HYDROGEN, NITROGEN, OXYGEN, FLORIDE, CHLORINE,
        CO, NO, SO, CLO, HF, CLF, CS, HCL,
        
        CO2, NO2, SO2, CLO2, OZONE, WATER,
        F2O, CL2O, SCL2, H2S, CS2, HCN, CNF, CNCL, HCLO,
        
        AMMONIA, SO3, NO3, NCL3, METHANAL, H2O2,
        S2CL2, HNO2, ETHINE, CYAN,
        
        METHANE, CMETHANE, C2METHANE, C3METHANE, C4METHANE,
        FRON11, FRON12, FRON13, FRON14, FRON21, FRON22,
        FRON23, FRON31, FRON32, FRON41,
        
        METHANOL, FORMICACID, NITRICACID, ACETICACID, NUCLEAR
    };
    
    public static final Molecule[] STABLES = {
        HYDROGEN, NITROGEN, OXYGEN, FLORIDE, CHLORINE,
        SO, HF, CLF, HCL,
        
        CO2, NO2, SO2, WATER,
        F2O, CL2O, SCL2, H2S, CS2, HCN, CNCL, HCLO,
        
        AMMONIA, NCL3, METHANAL,
        HNO2, ETHINE, CYAN,
        
        METHANE, CMETHANE, C2METHANE, C3METHANE, C4METHANE,
        FRON11, FRON12, FRON13, FRON14, FRON21, FRON22,
        FRON23, FRON31, FRON32, FRON41,
        
        METHANOL, FORMICACID, NITRICACID, ACETICACID
    };
    
    public static final Molecule[] JUNIORS = {
        HYDROGEN, NITROGEN, OXYGEN, FLORIDE, CHLORINE,
        CO, HCL,
        
        CO2, NO2, SO2, OZONE, WATER,
        H2S,
        
        AMMONIA, H2O2
    };
    
    public static final Molecule[] OVER3S = {
        CO2, NO2, SO2, CLO2, OZONE, WATER,
        F2O, CL2O, SCL2, H2S, CS2, HCN, CNF, CNCL, HCLO,
        
        AMMONIA, SO3, NO3, NCL3, METHANAL, H2O2,
        S2CL2, HNO2, ETHINE, CYAN,
        
        METHANE, CMETHANE, C2METHANE, C3METHANE, C4METHANE,
        FRON11, FRON12, FRON13, FRON14, FRON21, FRON22,
        FRON23, FRON31, FRON32, FRON41,
        
        METHANOL, FORMICACID, NITRICACID, ACETICACID, NUCLEAR
    };
    
    private String name_;
    private int id_;
    private int bonus_;
    private int speedEffect_;
    
    public Molecule(int id, String name, int bonus, int speedEffect) {
        id_ = id;
        name_ = name;
        bonus_ = bonus;
        speedEffect_ = speedEffect;
    }
    
    public int getSpeedEffect() {
        return speedEffect_;
    }

    public int getId() {
        return id_;
    }
    
    public String getName() {
        return name_;
    }
    
    public int getBonus() {
        return bonus_;
    }
    
    
}
