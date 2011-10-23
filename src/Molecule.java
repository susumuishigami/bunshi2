/*------------------------------------------------------
 ÉtÉ@ÉCÉãñº  Molecule.java
 ì˙ït        2004/08/15
 çÏê¨é“      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * ï™éqå^
 * @author Susumu ISHIGAMI
 * @version 0.2.5-8/21
 */
public final class Molecule {
    public static final Molecule HYDROGEN = new Molecule(0,  "êÖëf", 0, 20);
    public static final Molecule NITROGEN = new Molecule(1,  "íÇëf", 0, 80);
    public static final Molecule OXYGEN   = new Molecule(2,  "é_ëf", 0, 300);
    public static final Molecule FLORIDE  = new Molecule(3,  "ï§ëf", 0, -50);
    public static final Molecule CHLORINE = new Molecule(4,  "âñëf", 0, -20);
    
    public static final Molecule CO       = new Molecule(5,  "àÍé_âªíYëf", 0, -70);
    public static final Molecule NO       = new Molecule(6,  "àÍé_âªíÇëf", 0, -50);
    public static final Molecule SO       = new Molecule(7,  "àÍé_âªó∞â©", 0, -30);
    public static final Molecule CLO      = new Molecule(8,  "àÍé_âªâñëf", 0, -50);
    public static final Molecule HF       = new Molecule(9,  "ï§âªêÖëf", 0, -40);
    
    public static final Molecule CLF      = new Molecule(10, "ï§âªâñëf", 0, -40);
    public static final Molecule CS       = new Molecule(11, "àÍó∞âªíYëf", 0, -20);
    public static final Molecule HCL      = new Molecule(12, "âñâªêÖëf", 0, -40);
    // 3Ç¬à»è„---------------------------------------------------------------------
    public static final Molecule CO2      = new Molecule(13, "ìÒé_âªíYëf", 100, -5);
    public static final Molecule NO2      = new Molecule(14, "ìÒé_âªíÇëf", 100, -60);
    
    public static final Molecule SO2      = new Molecule(15, "ìÒé_âªó∞â©", 100, -50);
    public static final Molecule CLO2     = new Molecule(16, "ìÒé_âªâñëf", 100, -50);
    public static final Molecule OZONE    = new Molecule(17, "ÉIÉ]Éì", 100, -40);
    public static final Molecule WATER    = new Molecule(18, "êÖèˆãC", 100, 400);
    public static final Molecule F2O      = new Molecule(19, "é_âªï§ëf", 100, -60);
    
    public static final Molecule CL2O     = new Molecule(20, "àÍé_âªìÒâñëf", 100, -20);
    public static final Molecule SCL2     = new Molecule(21, "ìÒâñâªó∞â©", 100, -40);
    public static final Molecule H2S      = new Molecule(22, "ó∞âªêÖëf", 100, -100);
    public static final Molecule CS2      = new Molecule(23, "ìÒó∞âªíYëf", 100, -40);
    public static final Molecule HCN      = new Molecule(24, "º±›âªêÖëf", 100, -100);
    
    public static final Molecule CNF      = new Molecule(25, "ï§âªÉVÉAÉì", 100, -100);
    public static final Molecule CNCL     = new Molecule(26, "âñâªÉVÉAÉì", 100, -100);
    public static final Molecule HCLO     = new Molecule(27, "éüàüâñëfé_", 100, -30);
    // 4Ç¬à»è„---------------------------------------------------------------------
    public static final Molecule AMMONIA  = new Molecule(28, "ÉAÉìÉÇÉjÉA", 300, -30);
    public static final Molecule SO3      = new Molecule(29, "éOé_âªó∞â©", 300, -30);
    
    public static final Molecule NO3      = new Molecule(30, "éOé_âªíÇëf", 300, -10);
    public static final Molecule NCL3     = new Molecule(31, "âñâªíÇëf", 300, -20);
    public static final Molecule METHANAL = new Molecule(32, "ŒŸ—±Ÿ√ﬁÀƒﬁ", 300, -20);
    public static final Molecule H2O2     = new Molecule(33, "âﬂé_âªêÖëf", 300, -10);
    public static final Molecule S2CL2    = new Molecule(34, "àÍâñâªó∞â©", 300, -30);
    
    public static final Molecule HNO2     = new Molecule(35, "àüè…é_", 300, -20);
    public static final Molecule ETHINE   = new Molecule(36, "ÉAÉZÉ`ÉåÉì", 300, 0);
    public static final Molecule CYAN     = new Molecule(37, "ÉVÉAÉì", 300, -50);
    // 5Ç¬à»è„---------------------------------------------------------------------
    public static final Molecule METHANE  = new Molecule(38, "ÉÅÉ^Éì", 1000, -10);
    public static final Molecule CMETHANE = new Molecule(39, "∏€€“¿›", 1000, -10);
    
    public static final Molecule C2METHANE= new Molecule(40, "ºﬁ∏€€“¿›", 1000, -20);
    public static final Molecule C3METHANE= new Molecule(41, "ƒÿ∏€€“¿›", 1000, -20);
    public static final Molecule C4METHANE= new Molecule(42, "√ƒ◊∏€€“¿›", 1000, -20);
    public static final Molecule FRON11   = new Molecule(43, "ÉtÉçÉì11", 1000, -20);
    public static final Molecule FRON12   = new Molecule(44, "ÉtÉçÉì12", 1000, -20);
    
    public static final Molecule FRON13   = new Molecule(45, "ÉtÉçÉì13", 1000, -20);
    public static final Molecule FRON14   = new Molecule(46, "ÉtÉçÉì14", 1000, -20);
    public static final Molecule FRON21   = new Molecule(47, "ÉtÉçÉì21", 1000, -20);
    public static final Molecule FRON22   = new Molecule(48, "ÉtÉçÉì22", 1000, -20);
    public static final Molecule FRON23   = new Molecule(49, "ÉtÉçÉì23", 1000, -20);
    
    public static final Molecule FRON31   = new Molecule(50, "ÉtÉçÉì31", 1000, -20);
    public static final Molecule FRON32   = new Molecule(51, "ÉtÉçÉì32", 1000, -20);
    public static final Molecule FRON41   = new Molecule(52, "ÉtÉçÉì41", 1000, -20);
    // 6Ç¬à»è„---------------------------------------------------------------------
    public static final Molecule METHANOL = new Molecule(53, "ÉÅÉ^ÉmÅ[Éã", 3000, -20);
    public static final Molecule FORMICACID = new Molecule(54, "ãaé_", 3000, -20);
    
    public static final Molecule NITRICACID = new Molecule(55, "è…é_", 3000, -20);
    public static final Molecule ACETICACID = new Molecule(56, "ê|é_", 5000, -20);
    public static final Molecule NUCLEAR    = new Molecule(57, "êÖëfäjóZçá", 300, 0);
    
    
    
    
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
