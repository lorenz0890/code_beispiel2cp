/* 051030-1 Programmiersprachen und -konzepte WS 2017/18
 * Concurrent Programming
 * Hans Moritsch 2017-12-17
 */
import java.util.concurrent.locks.*;

// ------------------------------------------------------------------------
public class FigurePositionImpl implements FigurePosition {
    // ------------------------------------------------------------------------
    private Lock lock;
    private Condition waitMove;
    private Condition waitConfirm;
    private Condition waitRead;
    private Clock clock;
    private Position position;
    private int length;
    boolean confirmed;
    // --------------------------------------------------------
    public FigurePositionImpl (Clock clock) {
        // this is the constructor - unfortunately i havent figured out yet why we need the clock.
        position = new Position(8,8);
        this.clock = clock;
        lock = new ReentrantLock(false);
        waitMove = lock.newCondition();
        waitConfirm = lock.newCondition();
        waitRead = lock.newCondition();
        length = 0;
        confirmed = true;
    }

    // --------------------------------------------------------
    public Position getPosition() {
        // acquire lock and then return a copy of the current position of the figure. when finished unlock lock.
        while (!lock.tryLock());
        try{
            return new Position(position);
        } finally
        {
            lock.unlock();
        }

    }

    // --------------------------------------------------------
    public Position move(int dX, int dY) throws InterruptedException {
        // acquire lock with spinlock, then stay in the next while loop until the position allows the move to be executed without leaving the field.
        // while inside the loop, each time someone calls waitMove.signal() there is a chance that the condition in while (condition) is checked.
        // as soon as the move is executed, waitMove.signal() is called to wake a thread up so it can check if it can execute its move without leaving the field.
        // unlock when everything is finished and return position. also in this method length is calculated.
        while (!lock.tryLock());
        try {
            while (dX + position.getX() > 64 || dY + position.getY() > 64 || dX + position.getX() < 0 || dY + position.getY() < 0)
                waitMove.await();

            position.set(position.getX() + dX, position.getY() + dY);
            confirmed = false;

            length += (dX < 0 ? -dX : dX) + (dY < 0 ? -dY : dY);
            //System.out.println("length: " + length);

            waitMove.signalAll();
            waitConfirm.signalAll();
            return getPosition();
        } finally {
            lock.unlock();
        }
    }

    // --------------------------------------------------------
    public Position confirm() throws InterruptedException {
        // acquire lock with spinlock, then stay in the next while loop until lenght >= 8.
        // each time waitConfirm.singal() is called, there is a chance the condition inside the while loop is checked.
        // once it got past the while loop, length is set to 0, waitRead.signal() is called, the lock is unlocked and position is returned.
        while (!lock.tryLock());
        try {
            while (length < 8)
                waitConfirm.await();

            length = 0;
            confirmed = true;
            waitRead.signalAll();

            return getPosition();
        }
        finally {
            lock.unlock();
        }
    }

    // --------------------------------------------------------
    public Position read() throws InterruptedException {
        // store current position in local variable, acquire lock with spinlock and wait until someone calls waitRead.signal();
        Position returnPos = new Position(position);
        while (!lock.tryLock());
        try {
            while (!confirmed)
                waitRead.await();
            return returnPos;
        }
        finally {
            lock.unlock();
        }
    }
}
