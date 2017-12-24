/* 051030-1 Programmiersprachen und -konzepte WS 2017/18
 * Concurrent Programming
 * Hans Moritsch 2017-12-17
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

// ------------------------------------------------------------------------
public class Clock {
// ------------------------------------------------------------------------
     DecimalFormat df  = (DecimalFormat)NumberFormat.getInstance(Locale.US);
     { 
        df.applyPattern("000,000"); 
        }

    private long    startTime;
    private int     length;
    private int     digits;

    public Clock(boolean start) {
        System.out.println("seconds");
        length = 1000;
        digits = 3;
        if (start) 
            setStartTime();
        }

    public Clock() {
        this(true);
        }

    public Clock(int length) {
        this(true);
        setLength(length);
        }

    public void setLength(int length) {
        this.length = length;
        }


    public void setDigits(int digits) {
        this.digits = digits;
        }

    public void setStartTime() {
        startTime = System.currentTimeMillis();
        }

    public void continueAfter(long ms) {
        if (ms > 0)
        try { Thread.sleep(ms); } 
            catch (InterruptedException e) { 
                System.out.println(this + "continueAfter: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
        }

    public void continueAfter(double units) {
        if (units > 0.0)
        try { Thread.sleep((long)(units*length)); } 
            catch (InterruptedException e) { 
                System.out.println(this + "continueAfter: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
        }

    public void continueAfterMax(long ms) {
        long msRand;
        if (ms > 0) {
          msRand = (long) ((double)ms * Math.random());
          try { Thread.sleep(msRand); } 
            catch (InterruptedException e) { 
                System.out.println(this + "continueAfterMax: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
          }
        }

    public void continueAfterMax(double units) {
        long msRand;
        if (units > 0.0) {
          msRand = (long)(units * Math.random());
          try { Thread.sleep(msRand); } 
            catch (InterruptedException e) { 
                System.out.println(this + "continueAfterMax: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
          }
        }

    public  void continueAt(long ms) {
        try { Thread.sleep(ms - System.currentTimeMillis() + startTime); } 
            catch (IllegalArgumentException e) {
                System.out.println(this + "continueAt: " + ms + " > aktuelle Zeit (\"" + e.getMessage() + "\")");
                }
            catch (InterruptedException e) { 
                System.out.println(this + "continueAt: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
        }

    public  void continueAt(double units) {
        long ms = (long)(units*length);
        try { Thread.sleep(ms - System.currentTimeMillis() + startTime); }
            catch (IllegalArgumentException e) {
                System.out.println(this + "continueAt: " + units + " > aktuelle Zeit (\"" + e.getMessage() + "\")");
                }
            catch (InterruptedException e) { 
                System.out.println(this + "continueAt: das sollte nicht auftreten (\"" + e.getMessage() + "\")");
                }
        }

    public long getTime() {
        return System.currentTimeMillis() - startTime;
        }

    public String toString() {
        long current = System.currentTimeMillis() - startTime;
        return "[" + df.format(current).replace(',', '.').substring(0, 4 + digits) + "] " + 
            "(" + Thread.currentThread().getName() + ") ";
        }

    public String thread() {
        return "(" + Thread.currentThread().getName() + ") ";
        }

    public void show(Thread t) {
        System.out.println(this + t.getName() + " is " + t.getState() + 
            ( t.isInterrupted() ? "-IR" : "" ) );
        }

    public void show(Thread t, String s) {
        System.out.println(this + t.getName() + " is " + t.getState() + 
            ( t.isInterrupted() ? "-IR" : "" ) + " // " + s);
        }

    public void show(Thread t, int k) {
        System.out.println(this + t.getName() + " is " + t.getState() +
            ( t.isInterrupted() ? "-IR" : "" ) + " // " + k);
        }

    public void show(String s) {
        System.out.println(this + "// " + s);
        }

    public void show(boolean withTime, String s) {
        if (withTime)
            show(s);
        else 
            System.out.println(thread() + "// " + s);
        }


    }

