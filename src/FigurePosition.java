/* 051030-1 Programmiersprachen und -konzepte WS 2017/18
 * Concurrent Programming
 * Hans Moritsch 2017-12-17
 */
import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;

// ------------------------------------------------------------------------
interface FigurePosition {
// ------------------------------------------------------------------------
    static int SIZE   = 64;
    static int LENGTH = 8;
    static int INIT   = 8;

    public Position move(int dX, int dY) throws InterruptedException;
    public Position confirm() throws InterruptedException;
    public Position read() throws InterruptedException;

    public Position getPosition();
    }
