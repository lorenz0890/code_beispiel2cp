/* 051030-1 Programmiersprachen und -konzepte WS 2017/18
 * Concurrent Programming
 * Hans Moritsch 2017-12-17
 */
import java.util.*;

// ------------------------------------------------------------------------
public class TestBeispiel2Cp {
// ------------------------------------------------------------------------

    static int RSTART = 0;
    static int NUM    = 32;

// --------------------------------------------------------
    public static void main(String[] args) {
// --------------------------------------------------------
        Clock clock = new Clock(500);

        FigurePosition fp = new FigurePositionImpl(clock);

        ArrayList<Thread> commands = new ArrayList<Thread>();

        int j = RSTART;       
        for (int i = 0; i < NUM && j < 1000 - 2; i++) {
            int n = (int)(Rand.numbers[j++]*10);
            if (n < 5) {
                clock.continueAfter(1.0);
                new Move(fp, (int)(Rand.numbers[j++] * 20) - 10, 
                    (int)(Rand.numbers[j++] * 20) - 10, clock, commands).start();
                }
            else if (n < 7) {
                clock.continueAfter(1.0);
                new Confirm(fp, clock, commands).start();
                }
            else {
                clock.continueAfter(1.0);
                new Read(fp, clock, commands).start();
                }
            } 

        clock.continueAt((double)NUM*1.3);

        // clock.show(false, "kill " + commands.size() + " pending commands");
        for (Thread t: commands)
            t.interrupt();

        Position p = fp.getPosition();
        clock.show(false, "final position: (Impl " + p.getX() + "," + p.getY() + ")");
        }
    }

// --------------------------------------------------------
abstract class Command extends Thread {
// --------------------------------------------------------
    static int count = 0;
    int nr;
    Clock clock;
    FigurePosition fp;
    ArrayList<Thread> commands;
    Command(FigurePosition fp, Clock clock, ArrayList<Thread> commands) {
        this.fp = fp;
        this.clock = clock;
        nr = count++;
        this.commands = commands;
        this.commands.add(this);
        }
    }

// --------------------------------------------------------
class Move extends Command {
// --------------------------------------------------------
    int dX;
    int dY;
    public Move(FigurePosition fp, int dX, int dY, Clock clock, 
        ArrayList<Thread> commands) {
        super(fp, clock, commands);
        this.dX = dX;
        this.dY = dY;
        }
    public void run() { 
        Position p = null;
        try {
            clock.show(false, nr + ": move (" + dX + "," + dY + ")");
            p = fp.move(dX, dY); 
        } catch (InterruptedException ie) { 
            clock.show(false, nr + ": move interrupted"); return;
            }
        clock.show(false, nr + ": move -> (" + p.getX() + "," + p.getY() +")");
        commands.remove(this);
        }
    }

// --------------------------------------------------------
class Confirm extends Command {
// --------------------------------------------------------
    public Confirm(FigurePosition fp, Clock clock, ArrayList<Thread> commands) {
        super(fp, clock, commands);
        }
    public void run() { 
        Position p = null;
        try {
            clock.show(false, nr + ": conf");
            p = fp.confirm(); 
        } catch (InterruptedException ie) { 
            clock.show(false, nr + ": conf interrupted"); return; 
            }
        clock.show(false, nr + ": conf -> (" + p.getX() + "," + p.getY() +")");
        commands.remove(this);
        }
    }

// --------------------------------------------------------
class Read extends Command {
// --------------------------------------------------------
    public Read(FigurePosition fp, Clock clock, ArrayList<Thread> commands) {
        super(fp, clock, commands);
        }
    public void run() { 
        Position p = null;
        try {
            clock.show(false, nr + ": read");
            p = fp.read(); 
        } catch (InterruptedException ie) {
            clock.show(false, nr + ": read interrupted"); return; 
            }
        clock.show(false, nr + ": read -> (" + p.getX() + "," + p.getY() +")");
        commands.remove(this);
        }
    }

