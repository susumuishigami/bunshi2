/*------------------------------------------------------
 ファイル名  ScoreManager.java
 日付        2004/07/25
 作成者      Si_Densetsu
 -------------------------------------------------------*/

import java.io.*;

/**
 * ハイスコアを管理するクラス
 * @author Si_Densetsu
 * @version 0.2.2-8/17
 */
public final class ScoreManager {
    public static final int SCORES = 5;
    public static final int MODES  = 2;
    
    private GameResult[][] ranking_;
    private GameResult prevResult_;
    
    public ScoreManager() {
        ranking_ = new GameResult[MODES][SCORES];
    }
    
    public void input(GameResult result) {
        prevResult_ = result;
        
        byte mode = result.getMode();
        for (int i = 0; i < ranking_[mode].length; i++) {
            if (ranking_[mode][i] == null || result.getScore() > ranking_[mode][i].getScore()) {
                Rankin(i, result);
                break;
            }
        }       
    }
    
    public GameResult getPrevResult() {
        return prevResult_;
    }
    
    public GameResult getRankingData(byte mode, int num) {
        
        GameResult result;
        if ((result = ranking_[mode][num]) != null) 
            return result;
        else {
            return (new GameResult(mode, GameUtil.getSimpleTime(2004, 8, 1), ConfigValue.CUSTOM ));
        }
    }
    
    private void Rankin(int num, GameResult result) {
        byte mode = result.getMode();
        for (int i = ranking_[mode].length - 2; i >= num;  i--) {
            ranking_[mode][i+1] = ranking_[mode][i];
        }
        ranking_[mode][num] = result;
    }
    
    public void write(DataOutputStream out) throws IOException {
        for (byte i = 0; i < ranking_.length; i++) {
            for (int j = 0; j < ranking_[0].length; j++) {
                getRankingData(i, j).write(out);
            }
        }
    }
    
    public void read(DataInputStream in) throws IOException {
        for (int i = 0; i < ranking_.length; i++) {
            for (int j = 0; j < ranking_[0].length; j++) {
                ranking_[i][j] = new GameResult(in);
            }
        }
    }

}
