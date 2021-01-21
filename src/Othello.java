import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Othello {
    CellType[][] board;
    int width=10;
    int height=10;
    int[][] nbr = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1,1}};

    Othello()
    {
        board = new CellType[height][width];
        init();
    }

    void init()
    {
        for(int i = 0 ; i < board.length ; i ++ )
            Arrays.fill(board[i], CellType.NONE);
        int mr= height/2, mc=width/2;
        put(mr-1, mc-1, CellType.WHITE);
        put(mr-1, mc, CellType.BLACK);
        put(mr, mc, CellType.WHITE);
        put(mr, mc-1, CellType.BLACK);
    }

    public CellType getCell(int r, int c) {
        if( r>=0 && r < height && c>=0 && c < width)
            return board[r][c];
        else
            return CellType.NONE;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    void randomPut(int count) {
        Random r = new Random();
        for (int s = 0; s < 2; s++) {
            for (int i = 0; i < 3; i++) {
                int ro, co;
                do {
                    ro = r.nextInt(10);
                    co = r.nextInt(10);
                } while (put(ro, co, CellType.values()[s + 1]) == false);
            }
        }
    }

    int[] countCell()
    {
        int[] cnt = {0, 0};
        for(int r = 0; r < board.length ; r ++ ) {
            for( int c = 0; c < board[r].length; c ++ )
                if( board[r][c] == CellType.BLACK )
                    cnt[0] ++;
                else if( board[r][c] == CellType.WHITE )
                    cnt[1] ++;
        }
        return cnt;
    }

    boolean put(int row, int col, CellType type ) {
        if( board[row][col] == CellType.NONE ) {
            board[row][col] = type;
            return true;
        }
        else
            return false;
    }
    ArrayList<Integer> checkNeighbor(int row, int col, CellType type )
    {
        ArrayList<Integer> nl = new ArrayList<>();
        for(int i = 0; i < nbr.length ; i ++ ) {
            int r, c;
            r = row + nbr[i][0];
            c = col + nbr[i][1];
            if( r >= 0 && r < height && c >= 0 && c < width &&
                board[r][c] != CellType.NONE && board[r][c] != type ) {
                nl.add( i );
            }
        }
        return nl;
    }

    void changeDirection(int row, int col, CellType type, int dir )
    {
        int dr = nbr[dir][0], dc = nbr[dir][1];
        CellType nt=CellType.NONE;
        int nr=row, nc=col;
        do {
            nr = nr + dr;
            nc = nc + dc;
            if( nr >= 0 && nr < height && nc >= 0 && nc < width ) {
                nt = board[nr][nc];
                assert( nt != CellType.NONE );
                board[nr][nc] = type;
            }
            else
                break;
        } while( nt != type );
    }
    boolean checkDirection(int row, int col, CellType type, int dir )
    {
        int dr = nbr[dir][0], dc = nbr[dir][1];
        //System.out.printf("Delta direction: %d %d%n", dr, dc);
        CellType nt=CellType.NONE;
        int nr=row, nc=col;
        do {
            nr = nr + dr;
            nc = nc + dc;
            if( nr >= 0 && nr < height && nc >= 0 && nc < width )
                nt = board[nr][nc];
            else
                break;
        } while( nt != type && nt != CellType.NONE );
        if( nt == type )
            return true;
        else
            return false;
    }

    boolean putStone(int row, int col, CellType type) {
        ArrayList<Integer> nl;
        nl = checkNeighbor( row, col, type );
        if( nl.size() == 0 )
            return false;
        boolean result = false;
        for( int i = 0; i < nl.size() ; i ++ ) {
            if( checkDirection( row, col, type, nl.get(i) ) ) {
                //System.out.println("Available: (" + nbr[nl.get(i)][0] + "," + nbr[nl.get(i)][1] + ")");
                changeDirection( row, col, type, nl.get(i));
                result = true;
            }
        }
        if( result )
            put(row, col, type);
        return result;
    }
    

    boolean putCheck(int row, int col, CellType type) {
        ArrayList<Integer> nl;
        nl = checkNeighbor( row, col, type );
        if( nl.size() == 0 || board[row][col] != CellType.NONE )
            return false;
        boolean result = false;
        for( int i = 0; i < nl.size() ; i ++ ) {
            if( checkDirection( row, col, type, nl.get(i) ) ) {
                result = true;
                break;
            }
        }
        return result;
    }

    CellType get(int row, int col) {
        if( row < height && col < width )
            return board[row][col];
        else
            return CellType.NONE;
    }

    boolean nextTurn()
    {
        for(int i = 0 ; i < board.length ; i ++ ) {
            for (int j = 0; j < board[i].length; j++) {
                if( board[i][j] == CellType.NONE )
                    return true;
            }
        }
        return false;
    }

    void show()
    {
        for(int i = 0; i < height; i ++ ) {
            if (i == 0)
                System.out.print("     ");
            System.out.print(i + "   ");
        }
        System.out.println("\n   +---+---+---+---+---+---+---+---+---+---+");
        for(int i = 0; i < height; i ++ ) {
            for( int j = 0; j < width; j ++ ) {
                char c = ' ';
                switch( board[i][j ]) {
                    case NONE:
                        break;
                    case BLACK:
                        c = 'X';
                        break;
                    case WHITE:
                        c = 'O';
                        break;
                }
                if( j == 0 )
                    System.out.print(" " + i + " | ");
                System.out.print(c + " | ");
            }
            System.out.println("\n   +---+---+---+---+---+---+---+---+---+---+");
        }
    }
}

