/*------------------------------------------------------
 �t�@�C����  Field.java
 ���t        2004/07/25
 �쐬��      Si_Densetsu
 -------------------------------------------------------*/

/**
 * ���q������������Ԃ�\���N���X
 * @author Si_Densetsu
 * @version 0.4.0-8/23
 */
public final class Field {
    private int height_, width_;
    private PlacedAtom[][] placedAtom;
    private int[] amount_;
    
    public Field(int height, int width) {
        height_ = height;
        width_  = width;
        amount_ = new int[width];
        placedAtom = new PlacedAtom[width][height + 1]; // ���Ƀ_�~�[�����
    }
    
    public int getHeight() {
        return height_;
    }
    
    public int getWidth() {
        return width_;
    }
    
    public int amount(int x) {
        return amount_[x];
    }
    
    public PlacedAtom getMatter(int x, int y) {
        if (x < 0 || x >= width_ || y < 0 || y >= height_) return null;
        return placedAtom[x][y];
    }
    
    public void land(FallingAtom a) {
        int x = a.getX();
        int y = amount_[x]++;
        placedAtom[x][y] = new PlacedAtom(a);
    }
    
    public void close(int x, int y) {
        for (int i = y; i < height_ && placedAtom[x][i] != null; i++) {
            placedAtom[x][i] = placedAtom[x][i + 1]; // �_�~�[�̂��A�ōŏ㕔�ł��o�O��Ȃ��B
        }
        amount_[x]--;
    }
    
    public void energize(byte param) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < amount(x); y++) {
                getMatter(x, y).energize(param);
            }
        }
    }
}