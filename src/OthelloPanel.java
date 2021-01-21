import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OthelloPanel extends JPanel implements MouseListener, MouseMotionListener {
    Othello othello;
    int cellw, cellh;
    int nrow, ncol;
    int turn = 0;
    Cursor blackStoneCursor;

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if( cellh > 0 && cellw > 0 ) {
            int ro = y / cellh, co = x / cellw;
            if (othello.putCheck(ro, co, (turn == 0 ? CellType.BLACK : CellType.WHITE))) {
                setStoneCursor();
            } else
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    Cursor whiteStoneCursor;
    BufferedImage blackStoneImg, whiteStoneImg;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int ro = y/cellh, co = x/cellw;
        //System.out.printf("y: %d, x: %d, row: %d, col: %d\n", y, x, ro, co);
        if( othello.putStone(ro, co, (turn == 0 ? CellType.BLACK: CellType.WHITE ))) {
            turn = (turn + 1) % 2;
            setStoneCursor();
            updateScores();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    OthelloPanel()
    {
        othello = new Othello();
        requestFocus();
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
            
    }
   

    void createStoneCursor()
    {
        blackStoneImg = new BufferedImage(cellw/2, cellh/2, BufferedImage.TYPE_INT_ARGB);
        whiteStoneImg = new BufferedImage(cellw/2, cellh/2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D bg, wg;
        bg = blackStoneImg.createGraphics();
        bg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        bg.fillRect(0, 0, blackStoneImg.getWidth(), blackStoneImg.getHeight());

        bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        bg.setColor(Color.BLACK);
        bg.fillOval(0, 0, blackStoneImg.getWidth(), blackStoneImg.getHeight());
        bg.setColor(Color.WHITE);
        bg.drawOval(0, 0, blackStoneImg.getWidth(), blackStoneImg.getHeight());

        wg = whiteStoneImg.createGraphics();
        wg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        wg.fillRect(0, 0, whiteStoneImg.getWidth(), whiteStoneImg.getHeight());

        wg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        wg.setColor(Color.WHITE);
        wg.fillOval(0, 0, whiteStoneImg.getWidth(), whiteStoneImg.getHeight());
        wg.setColor(Color.BLACK);
        wg.drawOval(0, 0, whiteStoneImg.getWidth(), whiteStoneImg.getHeight());

        blackStoneCursor = Toolkit.getDefaultToolkit().createCustomCursor(blackStoneImg,
            new Point(blackStoneImg.getWidth()/2, blackStoneImg.getHeight()/2),
            "blackStoneCursor");
        whiteStoneCursor = Toolkit.getDefaultToolkit().createCustomCursor(whiteStoneImg,
            new Point(whiteStoneImg.getWidth()/2, whiteStoneImg.getHeight()/2),
            "whiteStoneCursor");
    }

    JLabel blackScore, whiteScore;
    JLabel turnLabel;
    void registerTurnLabel(JLabel turn)
    {
       turnLabel = turn;

       turnLabel.setBackground(Color.BLACK);
       turnLabel.setForeground(Color.WHITE);
    }

    void registerScoreBoards(JLabel bscore, JLabel wscore )
    {
        blackScore = bscore;
        whiteScore = wscore;
    }

    void updateScores()
    {
        int[] count = othello.countCell();
        blackScore.setText(""+count[0]);
        whiteScore.setText(""+count[1]);
    }

    void setStoneCursor()
    {
        if( blackStoneCursor == null || whiteStoneCursor == null )
            createStoneCursor();
        setCursor(turn==0?blackStoneCursor:whiteStoneCursor);

        turnLabel.setForeground(turn==0?Color.WHITE:Color.BLACK);
        turnLabel.setBackground(turn==0?Color.BLACK:Color.WHITE);
    }

    public void drawStone(Graphics g, int cx, int cy, int w, int h, CellType ct)
    {
        if( ct == CellType.BLACK ) {
            g.setColor(Color.BLACK);
            g.fillOval(cx, cy, w, h);
        }
        else {
            g.setColor(Color.WHITE);
            g.fillOval(cx, cy, w, h);
        }
    }
    
    public void drawPrev(Graphics g, int cx, int cy, int w, int h, CellType ct) {
        if (ct == CellType.BLACK) {
           g.setColor(Color.BLACK);
           g.drawOval(cx, cy, w, h);
        } else {
           g.setColor(Color.WHITE);
           g.drawOval(cx, cy, w, h);
        }
     }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension cs = this.getSize();

        cellw = cs.width / (ncol=othello.getWidth());
        cellh = cs.height / (nrow=othello.getHeight());

        //setStoneCursor();
        g.setColor(Color.BLACK);
        for(int r = 0; r < nrow ; r ++ ) {
            g.drawLine(0, r*cellh, cs.width, r*cellh);
        }
        for(int c = 0; c < ncol ; c ++ ) {
            g.drawLine(c*cellw, 0, c*cellw, cs.height);
        }
        for(int r = 0; r < nrow ; r ++ ) {
            for (int c = 0; c < ncol; c++) {
                CellType ce = othello.getCell(r, c);
                if( ce != CellType.NONE )
                    drawStone( g, c*cellw, r*cellh, cellw, cellh, ce );
            }
        }
        for (int r = 0; r < nrow; r++) {
            for (int c = 0; c < ncol; c++) {
               if (turn == 0 && othello.putCheck(r, c, CellType.BLACK)) {
                  CellType ce = othello.getCell(r, c);
                  if (ce == CellType.NONE)
                     drawPrev(g, c * cellw, r * cellh, cellw, cellh, CellType.BLACK);
               } else if (turn == 1 && othello.putCheck(r, c, CellType.WHITE)) {
                  CellType ce = othello.getCell(r, c);
                  if (ce == CellType.NONE)
                     drawPrev(g, c * cellw, r * cellh, cellw, cellh, CellType.WHITE);
               }
            }
         }
        
        /*
        ArrayList<Integer> nl;
        nl = othello.checkNeighbor(4, 4, ct );
        if( nl.size() == 0 )
            return;
        for( int i = 0; i < nl.size() ; i ++ ) {
            if(othello.checkDirection(4, 4, ct, nl.get(i) ) ) {
                //System.out.println("Available: (" + nbr[nl.get(i)][0] + "," + nbr[nl.get(i)][1] + ")");
            	g.drawOval(4*cellw, 4*cellh, cellw, cellh);
            	othello.changeDirection(cx, cy, ct, nl.get(i));
            }
        }*/
    }
}
