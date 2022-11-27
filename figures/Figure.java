package tom.chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Figure
{
    Board gameBoard;
    FigureControls gameFigureControls;

    int player;
    int[][] moveSet;
    int movedTimes = 0;

    ChessEnum type;
    JLabel display;

    ArrayList<Position> preMoves;
    ArrayList<Position> moves;
    Position pos;

    HashMap<Integer, Figure> castlingPartner;


    /*
    calculate moves for most figures (Queen, Rook, Bishop) - other figures @override it with their own function;
    ends on collision (still counts it as movable);
    puts them into preMoves (before move validation);
    2 arrays needed, otherwise recursion occurs in validation;
     */
    public void calculateMoves() {
        Position pos = this.beforeCalculation();
        boolean l;
        for (int[] move : moveSet) {
            pos.x = this.pos.x;
            pos.y = this.pos.y;
            do {
                pos.x += move[0];
                pos.y += move[1];
                if (gameBoard.inBounds(pos)) {
                    this.preMoves.add(new Position(pos.x, pos.y));
                    l = !gameBoard.notNull(pos);
                } else {
                    l = false;
                }
            } while (l);
        }
        this.removeFriendlyFire();
    }

    public Position beforeCalculation() {
        this.preMoves = new ArrayList<>();
        return new Position(0, 0);
    }

    public void removeFriendlyFire() {
        this.preMoves.removeIf(to -> gameBoard.notNull(to) && !gameBoard.differentPlayer(this, to));
    }


    /*
    new Position(0, 0) == new Position(0, 0) -> false, so it needs costume compare;
     */
    public boolean canMoveTo(Position to) {
        for (Position pos : this.moves) {
            if (pos.x == to.x && pos.y == to.y) {
                return true;
            }
        }
        return false;
    }


    /*
    copies preMoves into the moves ArrayList;
     */
    public void copyMoves() {
        moves = new ArrayList<>();
        for (Position pos : this.preMoves) {
            moves.add(new Position(pos.x, pos.y));
        }
    }


    /*
    sets position of figure's Graphics;
     */
    public void setDisplayPosition(Position pos) {
        this.display.setBounds(pos.x * 50, pos.y * 50, this.display.getWidth(), this.display.getHeight());
    }


    /*
    called on pawn promotion;
    only turns figure into the queen;
     */
    public void promotion() {
        if (this.pos.y == 0 || this.pos.y == 7) {
            Figure queen = new Queen(this.player, this.pos, gameBoard, this.gameFigureControls);
            queen.movedTimes = this.movedTimes;
            gameBoard.moveOnBoard(queen, this.pos);
            Graphics.replaceFigureGraphics(this, queen, gameBoard);
        }
    }


    /*
    adds mouse control interface;
    same object for every figure;
     */
    public void addMouseFigureMovement(){
        gameFigureControls.onDragMoveStart(this);
        gameFigureControls.dragMove(this);
        gameFigureControls.onDragMoveEnd(this);
    }
}