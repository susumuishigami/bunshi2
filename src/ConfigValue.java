/*------------------------------------------------------
 ファイル名  ConfigValue.java
 日付        2004/08/18
 作成者      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * 設定を保持するクラス
 * @author Susumu ISHIGAMI
 * @version 0.3.2-8/23
 */
public final class ConfigValue {
    public static final byte ALL    = 0;
    public static final byte STABLE = 1;
    public static final byte JUNIOR = 2;
    public static final byte OVER3  = 3;
    public static final byte CUSTOM = 4;
    public static String[] LABEL = {
            "全て", "安定するもの", "中学生用", "３つ以上", "カスタム"
    };
    
    private byte choice_;
    private boolean[] generatable_ = new boolean[Molecule.MOLECULES.length];

    public ConfigValue() {
        choice_ = ALL;
        for (int i = 0; i < generatable_.length; i++) {
            generatable_[i] = true;
        }
    }
    
    public byte getChoice() {
        return choice_;
    }
    
    public void changeChoice(byte choice) {
        choice_ = choice;
    }
    
    public boolean isGeneratable(int index) {
        return generatable_[index];
    }
    
    public void markGeneratable(int index) {
        generatable_[index] = true;
    }
    
    public void unmarkGeneratable(int index) {
        generatable_[index] = false;
    }
}
