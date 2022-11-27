package tom.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static tom.chess.ChessEnum.KING;

public class FigureControls
{
    Board gameBoard;
    GameInfo gameInfo;
    JPanel[][] moveOverlay;

    Position mousePosition;


    /*
    constructor needed for turn changing and move overlay;
     */
    public FigureControls(Board gameBoard, JPanel[][] moveOverlay, GameInfo gameInfo) {
        this.gameBoard = gameBoard;
        this.moveOverlay = moveOverlay;
        this.gameInfo = gameInfo;
    }


    /*
    lights move overlay Graphics;
     */
    public void onDragMoveStart(Figure selected) {
        selected.display.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lightMoveOverlay(selected, selected.player == gameInfo.player);
            }
        });
    }


    /*
    while mouse button is held, figure will follow it;
     */
    public void dragMove(Figure selected) {
        selected.display.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePosition = getMousePosition();
                selected.display.setBounds(mousePosition.x - 25, mousePosition.y - 50, selected.display.getWidth(), selected.display.getHeight());
            }
        });
    }


    /*
    signals the game to make move if move is valid,
    otherwise figure is returned to starting position;
    ending with right click (sometimes) breaks the game;
     */
    public void onDragMoveEnd(Figure selected) {
        selected.display.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < 8; i++) {
                    for (int i2 = 0; i2 < 8; i2++) {
                        moveOverlay[i][i2].setVisible(false);
                        moveOverlay[i][i2].setBackground(new Color(0xB514FF00, true));
                    }
                }

                boolean isCastling = false;
                mousePosition.x /= 50;
                mousePosition.y = (short) ((mousePosition.y - 25) / 50);

                if (selected.player == gameInfo.player && selected.canMoveTo(mousePosition)) {
                    if (selected.type == KING && selected.castlingPartner.size() > 0 && (mousePosition.x == selected.pos.x + 2 || mousePosition.x == selected.pos.x - 2)) {
                        isCastling = true;
                    }
                    gameInfo.game.makeMove(selected, mousePosition, isCastling);
                } else {
                    selected.setDisplayPosition(selected.pos);
                }
            }
        });
    }


    /*
    sets move overlay to visible;
    colors overlay according to, if figure belongs to this turn's player;
     */
    public void lightMoveOverlay(Figure selected, boolean rightPlayer) {
        for (Position pos : selected.moves) {
            moveOverlay[pos.x][pos.y].setVisible(true);
            if (gameBoard.notNull(pos) && gameBoard.differentPlayer(selected, pos)) {
                if (rightPlayer) {
                    moveOverlay[pos.x][pos.y].setBackground(new Color(0xB5FF0000, true));
                } else {
                    moveOverlay[pos.x][pos.y].setBackground(new Color(0xB58D8D8D, true));
                }
            } else {
                if (rightPlayer) {
                    moveOverlay[pos.x][pos.y].setBackground(new Color(0xB514FF00, true));
                } else {
                    moveOverlay[pos.x][pos.y].setBackground(new Color(0xB58D8D8D, true));
                }
            }
        }
        moveOverlay[selected.pos.x][selected.pos.y].setVisible(true);
        moveOverlay[selected.pos.x][selected.pos.y].setBackground(new Color(0xB5004CFF, true));
    }


    /*
    get location of mouse and return it as Position object;
    frame.getLocation - accurate mouse positions if frame is moved;
     */
    public Position getMousePosition() {
        int mouseX = (int)(MouseInfo.getPointerInfo().getLocation().getX() - Graphics.frame.getLocation().getX());
        int mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY() - Graphics.frame.getLocation().getY());
        return new Position(mouseX, mouseY);
    }
}
