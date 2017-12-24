/* 051030-1 Programmiersprachen und -konzepte WS 2017/18
 * Concurrent Programming
 * Hans Moritsch 2017-12-17
 */
// --------------------------------------------------------
public class Position {
// --------------------------------------------------------
    private int x,y;

    public Position(int x, int y) {
        set(x, y);
        }

    public Position(Position p) {
        set(p.getX(), p.getY());
        }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void set(int x, int y) { setX(x); setY(y); }

    }
