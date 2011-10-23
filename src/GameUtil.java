/*------------------------------------------------------
 ファイル名  GameUtil.java
 日付        2004/08/06
 作成者      Susumu ISHIGAMI
 -------------------------------------------------------*/

/**
 * シンプル日付などゲームで必要になる関数群。
 * @author Susumu ISHIGAMI
 * @version 0.3.2-8/23
 */

public final class GameUtil {
    private static final long MS_OF_MINUTE = 1000L * 60L;
    private static final long MS_OF_HOUR   = MS_OF_MINUTE * 60L;
    private static final long MS_OF_DAY    = MS_OF_HOUR * 24L;
    private static final long MS_OF_y2k    = getMSofY2K();
    
    private static final int[] DAYS_OF_MONTH  = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    
    private static long getMSofY2K() {
        return 365 * (2000 - 1970) + getLeapYears(2000);
    }
    
    private static long getLeapYears(int year) {
        // 1970年からの閏年の回数を数える。ただし，1972～2099年までしか対応しない。
        return (year - 1972) / 4L + 1;
    }
    
    private static int getLeapYearsByTime(long time) {
        int days = (int) (time / MS_OF_DAY);
        int result = 0;
        
        days -= 365 * 2; // 最初の閏年は1972年
        
        while (days > 0) {
            result++;
            days -= 365L * 4L;
        }
        
        return result;
    }
    
    // 閏年かどうか。(1999-2099までしか対応していない。)
    private static boolean isLeapYear(int year) {
        return year % 4 == 0;
    }
    
    public static byte getSimpleYear(long time) {
        long result = (time / MS_OF_DAY - getLeapYearsByTime(time) + 1) / 365 + 1970;
        
        if (time >= MS_OF_y2k) {
            result -= 2000L;
        } else {
            result -= 1900L;
        }
        
        return (byte)result;
    }
    
    public static byte getSimpleMonth(long time) {
        long day = (time / MS_OF_DAY - getLeapYearsByTime(time) + 1) % 365L;

        byte i;
        for (i = 0; i < 12; i++) {
            
            day -= DAYS_OF_MONTH[i];
            if (i == 1 && isLeapYear(getSimpleYear(time))) {
                day--;
            }
            
            if (day < 0) {
                break;
            }
        }
        
        return (byte)(i + 1);
    }
    
    public static byte getSimpleDate(long time) {
        long day = ( time / MS_OF_DAY - getLeapYearsByTime(time) + 1) % 365L;
        for (int i = 0; i < 12; i++) {
            
            long tmp = day - DAYS_OF_MONTH[i];
            
            if (i == 1 && isLeapYear(getSimpleYear(time))) {
                tmp--;
            }
            
            if (tmp < 0) {
                break;
            } else {
                day = tmp;
            }
        }
        
        return (byte) (day + 1);
    }
    
    public static long getSimpleTime(int year, int month, int date) {
        long days = 0;
        days += (year - 1970) * 365 + getLeapYears(year) - 1;

        for (int i = 0; i < month - 1; i++) {
            days += DAYS_OF_MONTH[i];
            if (i == 1 && isLeapYear(year)) {
                days++;
            }
        }
        
        days += date - 1;
        
        return days * MS_OF_DAY;
    }
    
    public static String showSimpleDate(long time) {
        byte year  = getSimpleYear(time);
		byte month = getSimpleMonth(time);
		byte date  = getSimpleDate(time);
		String yearStr  = Integer.toString(year);
        String monthStr = Integer.toString(month);
        String dateStr  = Integer.toString(date);
        
        // 年・月・日付が1桁のとき頭に0をつける。
        String zeroY = "";
        String zeroM = "";
        String zeroD = "";
        
        if (year <= 10) {
            zeroY = "0";
        }
        if (month <= 10) {
            zeroM = "0";
        }
        if (date <= 10) {
            zeroD = "0";
        }
        
        return zeroY + yearStr + "/" + zeroM + monthStr + "/" + zeroD + dateStr;
    }
    
    public static int max3(int x, int y, int z) {
        return Math.max(Math.max(x,y), z);
    }
    
    public static int max4(int w, int x, int y, int z) {
        return Math.max(Math.max(Math.max(w,x), y), z);
    }
    
    /*
    public static void main(String[] args) {
		System.out.println(getSimpleTime(2004,8,7));
		System.out.println(getSimpleMonth(getSimpleTime(2004,8,7)));
		System.out.println(getSimpleDate(getSimpleTime(2004,8,7)));
        System.out.println(System.currentTimeMillis());
		System.out.println(getSimpleMonth(System.currentTimeMillis()));
		System.out.println(getSimpleDate(System.currentTimeMillis()));
    }
	*/
}
