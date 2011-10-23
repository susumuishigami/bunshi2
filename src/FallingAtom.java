/*------------------------------------------------------
ファイル名  FallingAtom.java
日付        2004/07/25
作成者      Susumu ISHIGAMI
-------------------------------------------------------*/

/**
 * 落下する原子を表すクラス
 * @author Susumu ISHIGAMI
 * @version 0.1.4-7/27
 */
public final class FallingAtom extends Atom {
    private int x_, y_, preX_, preY_;
    
    public FallingAtom(byte kind) {
        super(kind);   
        x_ = 2;
        y_ = 7;
        preX_ = -100;
        preY_ = -100;
    }
    
    public int getX() {
        return x_;
    }
 
    public int getY() {
        return y_;
    }
    
    public void setX(int x) {
        preX_ = x_;
        preY_ = y_;
        x_ = x;
    }
    
    public void setY(int y) {
        preX_ = x_;
        preY_ = y_;
        y_ = y;
    }

    public int getPreX() {
        return preX_;
    }

    public int getPreY() {
        return preY_;
    }
    
    public void moveLeft() {
        setX(x_ - 1); 
    }
    
    public void moveRight() {
        setX(x_ + 1);
    }
    
    public void fall() {
        setY(y_ - 1);
    }
}