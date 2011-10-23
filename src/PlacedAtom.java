/*------------------------------------------------------
�t�@�C����  PlacedAtom.java
���t        2004/07/25
�쐬��      Susumu ISHIGAMI
-------------------------------------------------------*/

/**
 * �u���ꂽ���q��\���N���X
 * @author Si
 * @version 0.2-8/15
 */
public final class PlacedAtom extends Atom {
    private boolean eraceFlag_ = false;
    
    public PlacedAtom(FallingAtom kind) {
        super(kind.getMatter());
    }
    
    public boolean isMarkedErace() {
        return eraceFlag_;
    }
    
    public void markErace() {
        eraceFlag_ = true;
    }
    
    public void unmarkErace() {
        eraceFlag_ = false;
    }
    
}