import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class OthelloSwing extends JFrame {
    OthelloSwing()
    {
        OthelloPanel othelloPanel = new OthelloPanel();
        JPanel top = new JPanel();
        JLabel turn, black, white, blackScore, whiteScore;

        turn = new JLabel("Turn", SwingConstants.CENTER);
        turn.setOpaque(true);
        turn.setPreferredSize(new Dimension(100,20));
        black = new JLabel("Black:");
        blackScore = new JLabel("0");
        blackScore.setPreferredSize(new Dimension(100,20));
        white = new JLabel("White:");
        whiteScore = new JLabel("0");
        whiteScore.setPreferredSize(new Dimension(100, 20));
        top.add(turn);
        JLabel dummy = new JLabel("");
        dummy.setPreferredSize(new Dimension(50, 20));
        top.add(dummy);
        top.add(black);
        top.add(blackScore);
        top.add(white);
        top.add(whiteScore);
        othelloPanel.registerScoreBoards(blackScore, whiteScore);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        othelloPanel.setPreferredSize(new Dimension(600, 600));
        othelloPanel.setBackground(Color.LIGHT_GRAY);
        othelloPanel.registerTurnLabel(turn);

        add(top, BorderLayout.PAGE_START);
        add(othelloPanel, BorderLayout.CENTER);
    
        setSize(800, 700);
        setVisible(true);
    }

    public static void consoleStart()
    {
        Othello othello = new Othello();
        //othello.randomPut(3);
        othello.show();

        Scanner sinput = new Scanner(System.in);
        int turn = 0;
        while(othello.nextTurn()) {
            int ro, co;
            char ch;

            //ch = sinput.next().charAt(0);
            if ( turn == 0 )
                ch = 'X';
            else
                ch = 'O';
            int[] nch = othello.countCell();
            System.out.printf("[X:%d, O:%d] Enter row(int), col(int) (-1,-1 quit): (%c) ==> ", nch[0], nch[1], ch);
            try {
                ro = sinput.nextInt();
                co = sinput.nextInt();
            }
            catch(Exception e) {
                System.out.println("Incorrect input, try again!!");
                sinput.nextLine();
                sinput.reset();
                continue;
            }

            if( ro == -1 && co == -1 ) break;
            if( ro < 0 || ro >= othello.height || co < 0 || co >= othello.width )
                continue;

            if( othello.putStone(ro, co, (ch == 'O' ? CellType.WHITE: ch == 'X' ? CellType.BLACK : CellType.NONE)) ) {
                othello.show();
                turn = (turn + 1) % 2;
            }
            else {
                System.out.println("Incorrect place, try again!!");
            }
        }
    }

    public static void main(String[] args) {
        // Graphic Othello
        OthelloSwing othelloswing = new OthelloSwing();

        // console Othello
        // consoleStart();
    }
}
