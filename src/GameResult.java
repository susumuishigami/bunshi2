/*------------------------------------------------------
 ファイル名  GameResult.java
 日付        2004/07/25
 作成者      Susumu ISHIGAMI
 -------------------------------------------------------*/

import java.io.*;

/**
 * ゲーム結果を格納するクラス
 * @author Si
 * @version 0.2.2-8/17
 */
public final class GameResult {
    private int score_;
    private long startTime_;
    private byte mode_;
    private byte choice_; // 発生する元素。
    private byte maxSeries_;
    private short successCount_;
    public short[] madeMolecules = new short[Molecule.MOLECULES.length];
    
    public GameResult(byte mode, long startTime, byte choice) {
        for (int i = 0; i < madeMolecules.length; i++) {
            madeMolecules[i] = 0;
        }
        score_ = 0;
        mode_ = mode;
        startTime_ = startTime;
        successCount_ = 0;
    }
    
    public GameResult(DataInputStream in) throws IOException {
        read(in);
    }
    
    public short getMadeMolecules(int index) {
        return madeMolecules[index];
    }
    
    public byte getMode() {
        return mode_;
    }
    
    public int getScore() {
        return score_;
    }
    
    public long getStartTime() {
        return startTime_;
    }
    
    public byte getMaxSeries() {
        return maxSeries_;
    }
    
    public byte getChoice() {
        return choice_;
    }
    
    public void addScore(int a) {
        score_ += a;
    }
    
    public void success() {
        successCount_++;
    }
    
    public int getSuccessCount() {
        return successCount_;
    }
    
    public void generate(int kind) {
        madeMolecules[kind] ++;
    }
    
    public void inputMaxSeries(byte seriesCount) {
        if (maxSeries_ < seriesCount) {
            maxSeries_ = seriesCount;
        }
    }
    
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(score_);
        out.writeLong(startTime_);
        out.writeByte(mode_);
        out.writeByte(maxSeries_);
        out.writeByte(choice_);
        out.writeShort(successCount_);
        for (int i = 0; i < madeMolecules.length; i++) {
            out.writeShort(madeMolecules[i]);
        }
    }
    
    public void read(DataInputStream in) throws IOException {
        score_     = in.readInt();
        startTime_ = in.readLong();
        mode_      = in.readByte();
        maxSeries_ = in.readByte();
        choice_    = in.readByte();
        successCount_ = in.readShort();
        for (int i = 0; i < madeMolecules.length; i++) {
            madeMolecules[i] = in.readShort();
        }
    }
}
