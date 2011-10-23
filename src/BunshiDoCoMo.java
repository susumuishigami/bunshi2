/*------------------------------------------------------
  DoCoMo i-appli ���s�p�N���X�Q

  �t�@�C����     BunshiDoCoMo.java
  ���t           2004/07/18
  �쐬��         Si_Densetsu
-------------------------------------------------------*/

import com.nttdocomo.ui.*;
import com.nttdocomo.util.*;
import java.io.*;
import java.util.*;

import javax.microedition.io.Connector;



/**
 * ���q�쐬��i-mode�Ŏ��s���邽�߂̃N���X
 * @author  Si_Densetsu
 * @version 0.5.1-8/29
 */
public final class BunshiDoCoMo extends IApplication {
    
    public static final String VERSION = "0.5.1-8/29";
    
	public Bunshi bunshi;
	private TitleCanvas titleCanvas;
	private MainCanvas mainCanvas;
	private ScoreCanvas scoreCanvas;
	private ConfigForm configForm;
	public ScoreManager scoreManager;
	public ConfigValue configValue;
	
	public BunshiDoCoMo() {
	    scoreManager = new ScoreManager();
	    configValue  = new ConfigValue();
	    loadData();
	    scoreCanvas = new ScoreCanvas(this);
	    titleCanvas = new TitleCanvas(this);
	    configForm  = new ConfigForm(this); 
	}
	
	public void start() {
	    Display.setCurrent(titleCanvas);
	}
	
	public void newGame(byte mode) {
	    bunshi = new Bunshi(mode, configValue); // �V�K�Q�[���𐶐�����B
	    mainCanvas = new MainCanvas(this, bunshi);
	    Display.setCurrent(mainCanvas);
	}
	
	public void finishGame() {
	    if (bunshi != null) {
	        scoreManager.input(bunshi.gameResult);
	        saveData();
	        ScoreForm scoreForm = new ScoreForm(
    	            bunshi.gameResult,
    	            titleCanvas);
    	    Display.setCurrent(scoreForm);
	    }
	}
	
	public void goScores() {
	    Display.setCurrent(scoreCanvas);
	}
	
	public void goConfig() {
	    Display.setCurrent(configForm);
	}
	
	public void resetData() {
	    Dialog dlg = new Dialog(Dialog.DIALOG_YESNO, "��낵���ł����H");
		dlg.setText("�n�C�X�R�A�f�[�^���폜���܂��B�����͂ł��܂���B");
	    if (dlg.show() == Dialog.BUTTON_YES) {
	        scoreManager = new ScoreManager();
	        saveData();
	    }
	}
	
	public void saveData() {
	    DataOutputStream out = null;
	    try {
	        out = Connector.openDataOutputStream("scratchpad:///0;pos=0");
	        out.writeBoolean(true); // �X�N���b�`�p�b�h������������Ă��邩�B
	        scoreManager.write(out);
	    } catch (IOException e) {
	        System.err.println(e.getMessage());
	        e.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {
	                System.err.println(e.getMessage());
	    	        e.printStackTrace();
	            }
	        }
	    }
	}
	
	public void loadData() {
	    DataInputStream in = null;
	    try {
	        in = Connector.openDataInputStream("scratchpad:///0;pos=0");
	        
	        // �X�N���b�`�p�b�h������������Ă��邩�B
	        if (in.readBoolean()) {
	            scoreManager.read(in);
	        }
	    } catch (IOException e) {
	        System.err.println(e.getMessage());
	        e.printStackTrace();
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {
	                System.err.println(e.getMessage());
	    	        e.printStackTrace();
	            }
	        }
	    }
	}
}

/**
 * ���q�쐬�Q�[�����
 * @author  Si_Densetsu
 * @version 0.5.1-8/29
 */
final class MainCanvas extends Canvas implements TimerListener{
    private static final int height = Display.getHeight();
    private static final int width  = Display.getWidth();
    private BunshiDoCoMo listener_;
    private Bunshi game;
    private Timer dropTimer;	// �����p�^�C�}�[
    private Random rnd = new Random();
    private boolean allrepaint  = true; // ���ׂčĕ`�悷�邩�ۂ�
    private boolean isPausing   = false; // �|�[�Y���͗��B
    private boolean isGameOver  = false; // �Q�[���I�[�o�[�̎���m�点��B
    private boolean checkingFlag = false;
    private String message;
    
    public void delay(long n) {
        long fin = System.currentTimeMillis() + n; // �� + �ݒ�~���b
		while (System.currentTimeMillis() < fin) {}
    }
    
    public MainCanvas(BunshiDoCoMo listener, Bunshi bunshi) {
        listener_ = listener;
        game = bunshi;
        
        dropTimer = new Timer();
	    dropTimer.setListener(this);
	    dropTimer.setRepeat(true);
	    dropTimer.setTime(game.fallwait);
		dropTimer.start();
    }
    
	public void paint(Graphics g) {
	    g.lock();
	    
	    if (isGameOver) {
	        paintInformation(g);
	        
	        setSoftLabel(SOFT_KEY_1, "����");
	        g.setColor(Graphics.getColorOfName(Graphics.YELLOW));
	        g.setFont(Font.getFont(Font.SIZE_MEDIUM));
	        g.fillRect(width * 3/40, height * 4/10, width * 9/20, height * 5/40);
	        g.setColor(Graphics.getColorOfName(Graphics.BLACK));
	        g.drawString("Game Over", width * 3/40, height * 5/10);
	    }
	    
	    else if (isPausing) {
	        setSoftLabel(SOFT_KEY_1, "�J�n");
	        
	        g.setColor(Graphics.getColorOfRGB(160, 160, 160));
	        g.fillRect(width * 1/40 + 1, height * 5/40 + 1, width * 22/40 - 2, height * 34/40 - 2);
	        
	        g.setFont(Font.getFont(Font.SIZE_MEDIUM));
	        g.setColor(Graphics.getColorOfName(Graphics.BLACK));
	        g.drawString("PAUSE", width * 5/40, height * 3/10);
	        
	        g.setFont(Font.getFont(Font.SIZE_SMALL));
	        g.drawString("\ue6eb ��  �� ���_", width * 2/40, height * 19/40);
	        g.drawString("\ue6e2 C/��/���� N", width * 2/40, height * 22/40);
	        g.drawString("\ue6e3 N      �� O", width * 2/40, height * 25/40);
	        g.drawString("\ue6e4 S      ��Cl", width * 2/40, height * 28/40);
	        g.drawString("\ue6e5 C/��/���� O", width * 2/40, height * 31/40);
	        g.drawString("\ue6e6 ��/��  �� C", width * 2/40, height * 34/40);
	        g.drawString("\ue6e7 �}���x����", width * 2/40, height * 37/40);
	        
	    }
	    
	    else if (allrepaint) {
	        setSoftLabel(SOFT_KEY_1, "�x�e");
		    g.setColor(Graphics.getColorOfRGB(160, 160, 160));
			g.fillRect(0, 0, width, height);
			
			// �g�̕`��
			g.setColor(Graphics.getColorOfName(Graphics.BLACK));
			int[] px = {width * 1/40,  width * 1/40,   width * 23/40, width * 23/40, width * 26/40};
			int[] py = {height * 5/40, height * 39/40, height * 39/40, height * 8/40, height * 5/40};
			
			g.drawPolyline(px, py, 5);
			g.setColor(Graphics.getColorOfRGB(128, 128, 128));
			g.drawLine(width * 1/40,  height * 5/40, width * 26/40, height * 5/40);
			
			paintInformation(g);
			
			g.setFont(Font.getFont(Font.SIZE_MEDIUM));
			
			// �t�B�[���h��̌��q��\��
			if (!checkingFlag)
			    paintAtom(g, game.movingAtom, game.movingAtom.getX(), game.movingAtom.getY());
		    for (int x = 0; x < game.field.getWidth(); x++) {
		        for (int y = 0; y < game.field.amount(x); y++) {
		            paintAtom(g, game.field.getMatter(x,y), x, y);
		        }
		    }
		    
		    // ���̌��q��\��
		    g.setColor(Graphics.getColorOfName(Graphics.BLACK));
		    
		    g.setColor(getColor(game.nextAtom));
		    g.drawString(game.nextAtom.toString(), width * 33/40 , height * 5/20);
		}
		    
		// �ړ����̌��q�̂ݕ����ĕ`��B
		else {
		    paintInformation(g);
		    g.setFont(Font.getFont(Font.SIZE_MEDIUM));
		    eraceAtom(g, game.movingAtom, game.movingAtom.getPreX(), game.movingAtom.getPreY());
		    paintAtom(g, game.movingAtom, game.movingAtom.getX(), game.movingAtom.getY());
		}
	    
	    if (game.isGenerating()) {
	        g.setColor(Graphics.getColorOfName(Graphics.BLACK));
	        g.drawString(message, 0, height / 10);
	    }
	    
	    g.unlock(true);
	}
	
	public void paintAtom(Graphics g, PlacedAtom atom, int x, int y) {
	    if (atom.isMarkedErace()) {
	        g.setColor(Graphics.getColorOfName(Graphics.YELLOW));
	        g.fillRect((2 * x + 1) *  width / 20, (2 * (10 - y) - 3) * height / 20 + height/80, width / 10, height / 10);
	    }
	    paintAtom(g, (Atom)atom, x, y);
	}
	
	public void paintAtom(Graphics g, Atom atom, int x, int y) {
	    if (atom != null) {
		    // g.setColor(Graphics.getColorOfName(Graphics.BLACK));
	        g.setColor(getColor(atom));
		    g.drawString(
		        atom.toString(),
		        (2 * x + 1) *  width / 20,
		        (2 * (10 - y) - 1) * height / 20
		    );
	    }
	}
	
	public void eraceAtom(Graphics g, Atom atom, int x, int y) {
	    if (atom != null) {
	        g.setColor(Graphics.getColorOfRGB(160,160,160));
		    g.drawString(
		        atom.toString(),
		        (2 * x + 1) *  width / 20,
		        (2 * (10 - y) - 1) * height / 20
		    );
	    }
	}
	
	public void paintInformation(Graphics g) {
	    
	    String missionStr = game.showMission();
		int p =missionStr.indexOf("\n");
		
	    g.setColor(Graphics.getColorOfRGB(160, 160, 160));
        g.fillRect(width * 24/40 + 1, height * 15/40 + 1, width * 22/40 - 2, height * 34/40 - 2);
        g.fillRect(width * 27/40, 0, width * 10/40, height * 4/40);
	    g.setColor(Graphics.getColorOfName(Graphics.BLACK));
		g.setFont(Font.getFont(Font.SIZE_SMALL));
		
		if (game.gameResult.getMode() == Bunshi.TIMEATTACK) {
		    long rest = game.gameResult.getStartTime() - System.currentTimeMillis() + Bunshi.GAMETIME;
		    rest = rest / 1000;
		    g.drawString("����" + Long.toString(rest) + "�b", width * 28/40 , height * 2/20);
		}
		g.drawString("�w��:", width * 25/40 , height * 15/40);
		
		if (p != -1) {
		    g.drawString(missionStr.substring(0, p), width * 24/40 , height * 9/20);
		    g.drawString(missionStr.substring(p), width * 25/40 , height * 11/20);
		} else {
		    g.drawString(missionStr, width * 25/40 , height * 9/20);
		}
		
		g.drawString(Integer.toString(game.seriesCount) + "�A��", width * 31/40 , height * 14/20);
		g.drawString("��: " + Integer.toString(game.energy), width * 25/40 , height * 16/20);
		g.drawString("�}: " + Integer.toString(game.fallwait), width * 25/40 , height * 35/40);
		g.drawString("Sc: " + Integer.toString(game.gameResult.getScore()), width * 25/40 , height * 19/20);
		
		g.drawString("Next: ", width * 25/40 , height * 5/20);
	}
	
	public int getColor(Atom atom) {
	    switch (atom.getMatter()) {
	    	case Atom.HYDROGEN: return Graphics.getColorOfName(Graphics.WHITE);
	    	case Atom.CARBON:   return Graphics.getColorOfName(Graphics.BLACK);
	    	case Atom.NITROGEN: return Graphics.getColorOfName(Graphics.BLUE);
	    	case Atom.OXYGEN:   return Graphics.getColorOfName(Graphics.RED);
	    	case Atom.FLORIDE:  return Graphics.getColorOfName(Graphics.GREEN);
	    	case Atom.SULFUR:   return Graphics.getColorOfName(Graphics.OLIVE);
	    	case Atom.CHLORINE: return Graphics.getColorOfName(Graphics.LIME);
	    	case Atom.GRAPHITE: return Graphics.getColorOfName(Graphics.BLACK);
	    	case Atom.DIAMOND:  return Graphics.getColorOfName(Graphics.BLACK);
	    	default:            return Graphics.getColorOfName(Graphics.BLACK);
	    }
	}
	
	public void processEvent(int type, int param) {
	    if (checkingFlag) {
	        allrepaint = true;
	        repaint();
	        return;
	    }
	        
	    if (isGameOver) {
	        if (type == Display.KEY_PRESSED_EVENT && param == Display.KEY_SOFT1) {
	            listener_.finishGame();
		    }
	    } else if (isPausing) { 
	        if (type == Display.KEY_PRESSED_EVENT && param == Display.KEY_SOFT1) {    
	            pauseEnd();
		    }
	    } else {
		    if (type == Display.KEY_PRESSED_EVENT) {
		        allrepaint = false;
		        switch (param) {
		        	case Display.KEY_LEFT:  game.moveLeft();  break;
		        	case Display.KEY_RIGHT: game.moveRight(); break;
		        	case Display.KEY_DOWN:
		        	    game.gameResult.addScore(2);
		        	    fall();
		        		break;
		        	
		        	case Display.KEY_SELECT: land();           break;
		        	case Display.KEY_SOFT1:  pauseStart();     break;
		        	
		        	case Display.KEY_0: game.useEnergy((byte) 0); allrepaint = true; break;
		        	case Display.KEY_1: game.useEnergy((byte) 1); allrepaint = true; break;
		        	case Display.KEY_2: game.useEnergy((byte) 2); allrepaint = true; break;
		        	case Display.KEY_3: game.useEnergy((byte) 3); allrepaint = true; break;
		        	case Display.KEY_4: game.useEnergy((byte) 4); allrepaint = true; break;
		        	case Display.KEY_5: game.useEnergy((byte) 5); allrepaint = true; break;
		        	case Display.KEY_6: game.useEnergy((byte) 6); allrepaint = true; break;
		        }
		        
		        repaint();
		    } // of KEY_PRESSED_EVENT
	    }
	}
	
	public void timerExpired(Timer tm) {
	    fall();
	    repaint();
	}
	
	public void fall() {
	    synchronized (game) {
	        allrepaint = game.fall();
	        endCheck();
	    }
	}
	
	public void land() {
	    game.gameResult.addScore(30 + (1000 - game.fallwait) / 500);
	    game.fallwait++;
	    game.land();
	    allrepaint = true;
	    endCheck();
	}
	
	public void endCheck() {
	    checkingFlag = true;
	    if (game.isGenerating()) {
	        Molecule m = null;
	        dropTimer.stop();
	        while ((m = game.check()) != null) {
	            if (m != Molecule.NUCLEAR)
	                message = m.getName() + "����";
	            else
	                message = m.getName();
	            allrepaint = true;
	            paint(this.getGraphics());
	            delay(1000);
	        }
	        dropTimer.start();
	        allrepaint = true;
            paint(this.getGraphics());
		}
	    
	    else if (game.isEnd()) {
	        dropTimer.stop();
	        isGameOver = true;
	    }
	    checkingFlag = false;
	    
	    dropTimer.stop();
	    dropTimer.setTime(game.fallwait);
	    dropTimer.start();
	}
	
	public void pauseStart() {
	    isPausing = true;
	    dropTimer.stop();
	    repaint();
	}
	
	public void pauseEnd() {
	    isPausing = false;
	    dropTimer.start();
	    allrepaint = true;
	    repaint();
	}
}

/**
 * ���q�쐬�^�C�g���E�o�[�W����
 * @author  Si_Densetsu
 * @version 0.3.1-8/23
 */
final class TitleCanvas extends Canvas {
    public static final byte TITLE = 0;
    public static final byte ABOUT = 1;
    
    private BunshiDoCoMo listener_;
    private TitleCanvas titleCanvas;
    private byte flag;
    
    public TitleCanvas(BunshiDoCoMo listener) {
        changeFlag(TITLE);
        listener_ = listener;
    }
    
	public void paint(Graphics g) {
	    int height = Display.getHeight();
	    int width = Display.getWidth();
	    
	    g.lock();
	    g.setFont(Font.getFont(Font.SIZE_MEDIUM));
	    
		switch (flag) {
		case TITLE:
		    //g.setColor(Graphics.getColorOfRGB(128,128,255));
		    g.setColor(Graphics.getColorOfName(Graphics.WHITE));
		    g.fillRect(0, 0, width, height);
		
			g.setColor(Graphics.getColorOfName(Graphics.BLACK));
			g.drawString("�����쐬 mobile",      width * 1/20, height * 1/10);
			g.drawString("-���q���ł����-", width * 1/10,  height * 2/10);
			
			g.setFont(Font.getFont(Font.SIZE_SMALL));

			g.drawString("(c)2004 Si_Densetsu",   width * 12/40, height * 39/40);
			

			//g.drawScaledImage(img, 88, 68, 64, 64, 0, 0, 32, 32);
			g.drawString("\ue6e2 ��-Ӱ��", width * 1/20, height * 4/10);
			g.drawString("\ue6e3 ��ѱ���", width * 1/20, height * 5/10);
			g.drawString("\ue6e4 ���������", width * 1/20, height * 6/10);
			g.drawString("\ue6eb About&Help", width * 1/20, height * 8/10);
			break;

		case ABOUT:
		    g.setColor(Graphics.getColorOfName(Graphics.WHITE));
		    g.fillRect(0, 0, width, height);
			g.setColor(Graphics.getColorOfName(Graphics.BLACK));
			g.drawString("�����쐬 mobile\ue688", 0,  height * 1/10);
			g.setFont(Font.getFont(Font.SIZE_SMALL));
			g.drawString("for \ue6d2   Ver." + BunshiDoCoMo.VERSION.toString(), width * 1/20,  height * 2/10);
			
//			g.drawScaledImage(img, 88, 68, 64, 64, 0, 0, 32, 32);

			g.drawString("Push Soft_1 key !", width * 2/20, height * 15/20);
			
			g.drawString("���� Si_Densetsu", width * 1/20, height * 35/40);
			g.drawString("���� ��������\u301c��\u301c", width * 1/20, height * 39/40);
			break;
		}
		g.unlock(true);
	}
	
	public void changeFlag(byte f) {
	    switch (flag = f) {
		    case TITLE:
				setSoftLabel(SOFT_KEY_1, "�ݒ�");
				setSoftLabel(SOFT_KEY_2, "�I��");
				break;
			case ABOUT:
				setSoftLabel(SOFT_KEY_1, "�߂�");
				setSoftLabel(SOFT_KEY_2, null);
				break;
		}
		
		repaint();
	}
	
	public void processEvent(int type, int param) {
	    if (type == Display.KEY_PRESSED_EVENT) {
	        if (flag == TITLE) {
		        switch (param) {
		        	case Display.KEY_1:
		        	    listener_.newGame(Bunshi.ENDLESS);
		        		break;
		        	case Display.KEY_2:
		        	    listener_.newGame(Bunshi.TIMEATTACK);
		        		break;
		        	case Display.KEY_3:
		        	    listener_.goScores();
		        		break;	        	
		        	case Display.KEY_0:
		        	    changeFlag(ABOUT);
		        		break;
		        	case Display.KEY_SOFT1:
		        	    listener_.goConfig();
		        		break;
		        	case Display.KEY_SOFT2:
		        	    IApplication.getCurrentApp().terminate(); // �I��
		        		break;
		        }
	        } // of if flag == TITLE
	        
	        else {
	            switch (param) {
		            case Display.KEY_SOFT1:
		        	    changeFlag(TITLE);
		        	    break;
	            }
	            
	        } // of if flag != TITLE
	    } // of if type == KEY_PRESSED_EVENT
	}
		
}

/**
 * ���q�쐬�X�R�A���
 * @author  Si_Densetsu
 * @version 0.5.1-8/29
 */
final class ScoreCanvas extends Canvas {
    
    private BunshiDoCoMo listener_;
    
    public ScoreCanvas(BunshiDoCoMo listener) {
        listener_ = listener;
        setSoftLabel(SOFT_KEY_1, "�߂�");
		setSoftLabel(SOFT_KEY_2, "ؾ��");
    }
    
    public void paint(Graphics g) {
        g.lock();
        
        int height = Display.getHeight();
        int width  = Display.getWidth();

        g.clearRect(0, 0, height, width);
        g.setFont(Font.getFont(Font.SIZE_MEDIUM));
        g.drawString("ENDLESS", width * 1/40, height * 1/10);
        g.drawString("TIMEATTACK", width * 1/40, height * 6/10);
        
        for (byte mode = Bunshi.ENDLESS; mode <= Bunshi.TIMEATTACK; mode++) {
	        for (int i = 0; i < ScoreManager.SCORES; i++) {
	            int y = height * i/14 + height * 2/10 - height / 40;
	            
	            if (mode == Bunshi.TIMEATTACK) {
	                y += height / 2;
	            }
	           
	            g.setFont(Font.getFont(Font.SIZE_SMALL));
	            if (mode == Bunshi.ENDLESS) {
	                g.drawString(
	                    new String(new char[] {(char)(0xe6e2 + i)}), width * 1/10, y);
	            } else if (mode == Bunshi.TIMEATTACK) {
	                g.drawString(
	                    new String(new char[] {(char)(0xe6e2 + i + 5)}), width * 1/10, y);
	            }
	            
	            g.drawString(
	                Integer.toString(listener_.scoreManager.getRankingData(mode, i).getScore()),
	                width * 2/10, y
	            );
	            
	            g.drawString(
	                GameUtil.showSimpleDate(listener_.scoreManager.getRankingData(mode, i).getStartTime()
                        + (60000L * 60L * 9L)),
	                width * 6/10, y
	            );
	            
	        }
        }
        g.unlock(true);
    }
    
	public void processEvent(int type, int param) {
	    if (type == Display.KEY_PRESSED_EVENT) {
	        switch (param) {
            case Display.KEY_SOFT1:
                listener_.start();
                break;
            case Display.KEY_SOFT2:
                listener_.resetData();
                repaint();
                break;
            case Display.KEY_1:
                showScore(0);
                break;
            case Display.KEY_2:
                showScore(1);
                break;
            case Display.KEY_3:
                showScore(2);
                break;
            case Display.KEY_4:
                showScore(3);
                break;
            case Display.KEY_5:
                showScore(4);
            	break;
            case Display.KEY_6:
                showScore(5);
                break;
            case Display.KEY_7:
                showScore(6);
                break;
            case Display.KEY_8:
                showScore(7);
                break;
            case Display.KEY_9:
                showScore(8);
                break;
            case Display.KEY_0:
                showScore(9);
                break;
            }
	    }
    }
	
	public void showScore(int number) {
	    ScoreForm scoreForm = new ScoreForm(
	            listener_.scoreManager.getRankingData((byte) (number / 5), number % 5),
	            this);
	    Display.setCurrent(scoreForm);
	    
	    /*
	    GameResult gr = listener_.scoreManager.getRankingData((byte) (number / 5), number % 5);
	    
	    Dialog dlg = new Dialog(Dialog.BUTTON_OK, "�X�R�A�ڍ�");
		dlg.setText("Score     : " + gr.getScore()
		        + "\nStartTime : " + GameUtil.showSimpleDate(gr.getStartTime() + (60000L * 60L * 9L))
		        + "\n��������� : " + ConfigValue.LABEL[gr.getChoice()]
		        + "\n�w�ߒB��  : " + gr.getSuccessCount() + "��"
		        + "\n�ő�A����: " + gr.getMaxSeries() + "��");
		dlg.show();
		
		dlg = new Dialog(Dialog.BUTTON_OK, "��������������");
		String s = "";
		for (int i = 0; i < Molecule.MOLECULES.length; i++) {
		    s = s + Molecule.MOLECULES[i].getName() + " " + gr.getMadeMolecules(i) + "��\n";
		}
		dlg.setText(s);
		dlg.show();
		*/
	}
}

/**
 * �X�R�A�̌ʉ��
 * @author  Si_Densetsu
 * @version 0.5.0-8/26
 */

final class ScoreForm extends Panel implements SoftKeyListener {
    
    private Canvas listener_;
    private int score;
    private int maxSeries;
    private int mCount = 0;
    private int gamer;
    private int mannia;
    private int danger;
    private int over4 = 0;
    
    public ScoreForm(GameResult gr, Canvas listener) {
        HTMLLayout layout = new HTMLLayout();
        
        score = gr.getScore();
        maxSeries = gr.getMaxSeries();
        for (int i = 0; i < Molecule.MOLECULES.length; i++) {
            mCount += gr.getMadeMolecules(i);
        }        
        for (int i = Molecule.AMMONIA.getId(); i < Molecule.MOLECULES.length; i++) {
            over4 += gr.getMadeMolecules(i);
        }
        gamer = score * maxSeries * maxSeries / 1000000 + 50 * over4 / (mCount + 1) + gr.getSuccessCount(); 
        mannia = gr.getMadeMolecules(Molecule.FLORIDE.getId())
        		+ gr.getMadeMolecules(Molecule.HCN.getId())
        		+ gr.getMadeMolecules(Molecule.CNCL.getId())
        		+ gr.getMadeMolecules(Molecule.CS2.getId())
        		+ gr.getMadeMolecules(Molecule.NCL3.getId())
        		+ gr.getMadeMolecules(Molecule.C3METHANE.getId())
        		+ gr.getMadeMolecules(Molecule.C4METHANE.getId())
        		+ gr.getMadeMolecules(Molecule.FRON11.getId())
        		+ gr.getMadeMolecules(Molecule.FRON12.getId())
        		+ gr.getMadeMolecules(Molecule.FRON13.getId())
        		+ gr.getMadeMolecules(Molecule.FRON14.getId())
        		+ gr.getMadeMolecules(Molecule.FRON21.getId())
        		+ gr.getMadeMolecules(Molecule.FRON22.getId())
        		+ gr.getMadeMolecules(Molecule.FRON23.getId())
        		+ gr.getMadeMolecules(Molecule.FRON31.getId())
        		+ gr.getMadeMolecules(Molecule.FRON32.getId())
        		+ gr.getMadeMolecules(Molecule.FRON41.getId());
        danger = gr.getMadeMolecules(Molecule.HYDROGEN.getId())
        		+ gr.getMadeMolecules(Molecule.CHLORINE.getId())
        		+ gr.getMadeMolecules(Molecule.FLORIDE.getId()) * 2
        		+ gr.getMadeMolecules(Molecule.NO.getId())
        		+ gr.getMadeMolecules(Molecule.CO.getId())
        		+ gr.getMadeMolecules(Molecule.HCN.getId()) * 2
        		+ gr.getMadeMolecules(Molecule.OZONE.getId())
        		+ gr.getMadeMolecules(Molecule.SO2.getId())
        		+ gr.getMadeMolecules(Molecule.AMMONIA.getId())
        		+ gr.getMadeMolecules(Molecule.CYAN.getId())
        		+ gr.getMadeMolecules(Molecule.NO3.getId())
        		+ gr.getMadeMolecules(Molecule.C3METHANE.getId()) * 2
        		+ gr.getMadeMolecules(Molecule.NUCLEAR.getId())
        		- gr.getMadeMolecules(Molecule.NITROGEN.getId())
        		- gr.getMadeMolecules(Molecule.OXYGEN.getId());
        		
        mannia *= 400 / (mCount + 1);
        danger *= 400 / (mCount + 1);
        
        listener_ = listener;
        setSoftLabel(Frame.SOFT_KEY_1, "�߂�");
        setSoftKeyListener(this);
        setLayoutManager(layout);
        
        layout.begin(HTMLLayout.CENTER);
	        add(new Label("�Q�[������"));
		layout.end();
        
        
        layout.begin(HTMLLayout.LEFT);
        
	        add(new Label("Score     : " + gr.getScore()));
	        layout.br();
	        add(new Label("StartTime : " + GameUtil.showSimpleDate(gr.getStartTime() + (60000L * 60L * 9L))));
	        layout.br();
	        add(new Label("��������� : " + ConfigValue.LABEL[gr.getChoice()]));
	        layout.br();
	        add(new Label("�w�ߒB��  : " + gr.getSuccessCount() + "��"));
	        layout.br();
	        add(new Label("�ő�A����: " + gr.getMaxSeries() + "��"));
	        layout.br();
	        add(new Label("����������: " + mCount + "��"));
	        layout.br();
	        add(new Label("�Q�[�}�[�x: " + gamer));
	        layout.br();
	        add(new Label("�}�j�A�x  : " + mannia));
	        layout.br();
	        add(new Label("�댯�x    : " + danger));
	        layout.br();
	        add(new Label(""));
	        layout.br();
    		add(new Label(getComment(gr)));
    		layout.br();
	        add(new Label(""));
		layout.end();
        
	    layout.begin(HTMLLayout.CENTER);
	        add(new Label("�����������f"));
	    layout.end();
	        
	    layout.begin(HTMLLayout.LEFT);
	        for (int i = 0; i < Molecule.MOLECULES.length; i++) {
	            add(new Label(Molecule.MOLECULES[i].getName() + " " + gr.getMadeMolecules(i) + "��"));
	            layout.br();
	        }
	        layout.br();
	        add(new Label(""));
        layout.end();
        
    }
    
    public void softKeyPressed(int softkey) {
        if (softkey == Frame.SOFT_KEY_1) {
            Display.setCurrent(listener_);
        }

    }
    public void softKeyReleased(int softkey) {
    }
    
    private String getComment(GameResult gr) {
        String yourname;
        String yourproperty;
        
        if (score > 9999999) {
            yourname = "�搶�́C";
        } else if (score > 100000) {
            yourname = "���Ȃ��́C";
        } else if (score > 30000) {
            yourname = "�N�́C";
        } else if (score > 10000) {
            yourname = "���O�́C";
        } else {
            yourname = "���񂽂́C";
        }
        
        if (gamer > 15) {
            yourproperty = "�Q�[�}�[";
        } else if (maxSeries > 10) {
            yourproperty = "�A���̃X�y�V�����X�g";
        } else if (mannia > 200) {
            yourproperty = "���^�N";
        } else if (mannia > 150) {
            yourproperty = "�I�^�N";
        } else if (mannia > 100) {
            yourproperty = "�}�j�A";
        } else if (danger > 300) {
            yourproperty = "�댯�l��";
        } else if (score < 5000) {
            yourproperty = "���n��";
        } else {
            yourproperty = "�}�l";
        }
        
        return yourname + yourproperty + "�ł��B";
    }
}
/**
 * ���q�쐬�ݒ���
 * @author  Si_Densetsu
 * @version 0.5.0-8/18
 */

final class ConfigForm extends Panel implements ComponentListener, SoftKeyListener {

    BunshiDoCoMo listener_;
    ListBox moleculeList;
    ListBox customMoleculeList  = new ListBox(ListBox.CHECK_BOX);
    ListBox choiceList = new ListBox(ListBox.CHOICE);
    Label customLabel;
    
    String[] molecules = new String[Molecule.MOLECULES.length];
    
    ConfigForm(BunshiDoCoMo listener) {
        
        listener_ = listener;
        
        for (int i = 0; i < Molecule.MOLECULES.length; i++) {
            molecules[i] = Molecule.MOLECULES[i].getName();
        }
        
        choiceList.setItems(ConfigValue.LABEL);
        choiceList.setSize(100, 18);
        
        customMoleculeList.setItems(molecules);
        customMoleculeList.setSize(100,Display.getHeight() - 24);

        add(new Label("�����p�^�[��"));
        add(choiceList);
        add(customLabel = new Label("�������錳�f"));
        add(customMoleculeList);
        
        
        setComponentListener(this);
        setSoftKeyListener(this);
        
        setSoftLabel(Frame.SOFT_KEY_1, "����");
        setSoftLabel(Frame.SOFT_KEY_2, "�߂�");
        
        choiceList.select(listener_.configValue.getChoice());
        setChoice(listener_.configValue.getChoice());
        
        if (listener_.configValue.getChoice() == ConfigValue.CUSTOM) {
            for (int i = 0; i < Molecule.MOLECULES.length; i++) {
                if (listener_.configValue.isGeneratable(i))
                    customMoleculeList.select(i);
                else
                    customMoleculeList.deselect(i);
            }
        }
    }
    
    public void componentAction(Component source, int type, int param) {
        if (type == ComponentListener.SELECTION_CHANGED) {
            if (source == choiceList) {
                setChoice((byte) choiceList.getSelectedIndex());
            }
        }
    }
    
    public void softKeyPressed(int softkey) {
        if (softkey == Frame.SOFT_KEY_1) {
            listener_.configValue.changeChoice((byte) choiceList.getSelectedIndex());
            
            if (choiceList.getSelectedIndex() == ConfigValue.CUSTOM) {
                // �J�X�^���ŉ����\������Ă��Ȃ��̂͂܂����B
                for (int i = 0; i < Molecule.MOLECULES.length; i++) {
                    if (customMoleculeList.isIndexSelected(i))
                        break;
                    else if (i == Molecule.MOLECULES.length - 1) {
                        Dialog dlg = new Dialog(Dialog.BUTTON_OK, "�ݒ�K���s�\�ł��B");
                        dlg.setText("�����\�ȕ��q������I������Ă��܂���B");
                        dlg.show();
                    
                        return;
                    }
                }
                
                for (int i = 0; i < Molecule.MOLECULES.length; i++) {
	                if (customMoleculeList.isIndexSelected(i))
	                    listener_.configValue.markGeneratable(i);
	                else
	                    listener_.configValue.unmarkGeneratable(i);
	            }
            } else {
                Molecule[] selected;
                switch (choiceList.getSelectedIndex()) {
                case ConfigValue.ALL:
            	    selected = Molecule.MOLECULES;
                	break;
                case ConfigValue.STABLE:
            	    selected = Molecule.STABLES;
                	break;
            	case ConfigValue.JUNIOR:
            	    selected = Molecule.JUNIORS;
            		break;
            	case ConfigValue.OVER3:
            	    selected = Molecule.OVER3S;
            		break;
            	default:
            		System.out.println("SelectionError");
            		listener_.terminate();
            		return;
                }
                
                int i;
                for (i = 0; i < selected.length; i++) {
                    if (i > 0) {
                        for (int j = selected[i-1].getId() + 1; j < selected[i].getId(); j++) {
                            listener_.configValue.unmarkGeneratable(j);
                            System.out.println("-" + Molecule.MOLECULES[j].getName());
                        }
                    }
	                listener_.configValue.markGeneratable(selected[i].getId());
	                System.out.println("+"+ selected[i].getName());
	            }
                for (; i < Molecule.MOLECULES.length; i++) {
                    listener_.configValue.unmarkGeneratable(i);
                }
            }
            listener_.start();
        } else if (softkey == Frame.SOFT_KEY_2) {
            // ���ɖ߂��B
            listener_.start();
            choiceList.select(listener_.configValue.getChoice());
            if (listener_.configValue.getChoice() == ConfigValue.CUSTOM) {
                for (int i = 0; i < Molecule.MOLECULES.length; i++) {
                    if (listener_.configValue.isGeneratable(i))
                        customMoleculeList.select(i);
                    else
                        customMoleculeList.deselect(i);
                }
            }
        }
    }
    
    public void softKeyReleased(int softkey) {

    }
    
    public void setChoice(byte choice) {
        
        switch (choice) {

        case ConfigValue.ALL:    // �S��
        case ConfigValue.STABLE: // ���肷�����
        case ConfigValue.JUNIOR: // ���w���p
        case ConfigValue.OVER3:  // 3�ȏ�
            customLabel.setVisible(false);
        	customMoleculeList.setVisible(false);
            break;

        // �J�X�^��
        case ConfigValue.CUSTOM:
            customLabel.setVisible(true);
    		customMoleculeList.setVisible(true);
            break;
        }
    }

}