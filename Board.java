package tom.chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static tom.chess.ChessEnum.ROOK;

public class Board
{
    public FigureControls gameFigureControls;
    public JPanel[][] moveOverlay = Graphics.createMoveOverlay();

    GameInfo gameInfo;

    public Tile[][] board = createBoard();
    public King kingBlack;
    public King kingWhite;

    public Board(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }


    /*
    moves figure both on graphics and on board array;
     */
    public void moveFigure(Figure selected, Position pos) {
        selected.setDisplayPosition(pos);
        if (notNull(pos)) boardAt(pos).display.setVisible(false);
        this.moveOnBoard(selected, pos);
        selected.movedTimes++;
    }


    /*
    loops through board and generates moves for all figures ;
    withVirtualMove - to prevent recursion;
     */
    public void calculateAllMoves(boolean withinVirtualMove) {
        clearCastlingPartner(withinVirtualMove);
        ArrayList<Figure> rooks = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) if (notNull(x, y)) {
                Figure selected = board[x][y].figure;
                selected.calculateMoves();
                if (!withinVirtualMove) {
                    if (selected.type == ROOK) rooks.add(selected);
                    selected.copyMoves();
                    selected.moves.removeIf(to -> !virtualMove(selected, to));
                }
            }
        }
        checkCastling(withinVirtualMove, rooks);
    }

    public void clearCastlingPartner(boolean withinVirtualMove) {
        if (withinVirtualMove) return;
        for (int p = 0; p < 2; p++) {
            getKing(p).castlingPartner = new HashMap<>();
            getKing(p).castlingPartner = new HashMap<>();
        }
    }

    public void checkCastling(boolean withinVirtualMove, ArrayList<Figure> rooks) {
        if (withinVirtualMove) return;
        for (int p = 0; p < 2; p++) {
            for (Figure rook : rooks) {
                getKing(p).castling(rook);
            }
        }
    }


    /*
    checks if move doesn't end in check, done with moving figures and then returning them;
    doesn't move graphic components;
     */
    public boolean virtualMove(Figure selected, Position to) {
        Figure temporaryFigure = boardAt(to);
        Position temporaryPosition = new Position(selected.pos.x, selected.pos.y);

        moveOnBoard(selected, to);

        boolean check = callCheckCheck(getKing(selected.player));

        setBoard(selected, temporaryPosition);
        selected.pos = temporaryPosition;
        setBoard(temporaryFigure, to);

        return check;
    }

    /*
    checks if castling doesn't end in check (same as virtualMove);
    doesn't move graphic components;
     */
    public boolean virtualCastling(King king, Position kingTo, Figure rook, Position rookTo) {
        Position originalKingPos = new Position(king.pos.x, king.pos.y);
        Position originalRookPos = new Position(rook.pos.x, rook.pos.y);

        moveOnBoard(king, kingTo);
        moveOnBoard(rook, rookTo);

        boolean check = callCheckCheck(king);

        moveOnBoard(king, originalKingPos);
        moveOnBoard(rook, originalRookPos);

        return check;
    }

    public boolean callCheckCheck(King king) {
        calculateAllMoves(true);
        king.checkCheck();
        return !king.getCheck();
    }


    static Tile[][] createBoard() {
        Tile[][] board = new Tile[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = new Tile(x, y);
            }
        }
        return board;
    }

    /*
    put pieces on board;
    only 1 king per team can be present *(can be changed easily by adding an array of kings)*;
     */
    public void addFiguresToBoard() {
        gameFigureControls = new FigureControls(this, moveOverlay, gameInfo);

        board[0][1].figure = new Pawn(1, new Position(0, 1), this, gameFigureControls);
        board[1][1].figure = new Pawn(1, new Position(1, 1), this, gameFigureControls);
        board[2][1].figure = new Pawn(1, new Position(2, 1), this, gameFigureControls);
        board[3][1].figure = new Pawn(1, new Position(3, 1), this, gameFigureControls);
        board[4][1].figure = new Pawn(1, new Position(4, 1), this, gameFigureControls);
        board[5][1].figure = new Pawn(1, new Position(5, 1), this, gameFigureControls);
        board[6][1].figure = new Pawn(1, new Position(6, 1), this, gameFigureControls);
        board[7][1].figure = new Pawn(1, new Position(7, 1), this, gameFigureControls);

        board[0][0].figure = new Rook(1, new Position(0, 0), this, gameFigureControls);
        board[1][0].figure = new Knight(1, new Position(1, 0), this, gameFigureControls);
        board[2][0].figure = new Bishop(1, new Position(2, 0), this, gameFigureControls);
        board[3][0].figure = new Queen(1, new Position(3, 0), this, gameFigureControls);
        board[5][0].figure = new Bishop(1, new Position(5, 0), this, gameFigureControls);
        board[6][0].figure = new Knight(1, new Position(6, 0), this, gameFigureControls);
        board[7][0].figure = new Rook(1, new Position(7, 0), this, gameFigureControls);

        this.kingWhite = new King(1, new Position(4, 0), this, gameFigureControls);
        board[this.kingWhite.pos.x][this.kingWhite.pos.y].figure = this.kingWhite;


        board[0][6].figure = new Pawn(0, new Position(0, 6), this, gameFigureControls);
        board[1][6].figure = new Pawn(0, new Position(1, 6), this, gameFigureControls);
        board[2][6].figure = new Pawn(0, new Position(2, 6), this, gameFigureControls);
        board[3][6].figure = new Pawn(0, new Position(3, 6), this, gameFigureControls);
        board[4][6].figure = new Pawn(0, new Position(4, 6), this, gameFigureControls);
        board[5][6].figure = new Pawn(0, new Position(5, 6), this, gameFigureControls);
        board[6][6].figure = new Pawn(0, new Position(6, 6), this, gameFigureControls);
        board[7][6].figure = new Pawn(0, new Position(7, 6), this, gameFigureControls);


        board[0][7].figure = new Rook(0, new Position(0, 7), this, gameFigureControls);
        board[1][7].figure = new Knight(0, new Position(1, 7), this, gameFigureControls);
        board[2][7].figure = new Bishop(0, new Position(2, 7), this, gameFigureControls);
        board[3][7].figure = new Queen(0, new Position(3, 7), this, gameFigureControls);
        board[5][7].figure = new Bishop(0, new Position(5, 7), this, gameFigureControls);
        board[6][7].figure = new Knight(0, new Position(6, 7), this, gameFigureControls);
        board[7][7].figure = new Rook(0, new Position(7, 7), this, gameFigureControls);

        this.kingBlack = new King(0, new Position(4, 7), this, gameFigureControls);
        board[this.kingBlack.pos.x][this.kingBlack.pos.y].figure = this.kingBlack;


        //ads figures before tiles;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) if (board[x][y].figure != null) {
                Graphics.frame.add(board[x][y].figure.display);
            }
        }
        Graphics.addMoveOverlayToFrame(moveOverlay, true);
    }


    public void setBoard(Figure set, Position pos) {
        board[pos.x][pos.y].figure = set;
    }

    public void moveOnBoard(Figure selected, Position pos) {
        setBoard(null, selected.pos);
        selected.pos = pos;
        setBoard(selected, pos);
    }


    public King getKing(int player) {
        return (player == 0) ? kingBlack : kingWhite;
    }

    public boolean notNull(Position pos) {
        return boardAt(pos) != null;
    }

    public boolean notNull(int x, int y) {
        return board[x][y].figure != null;
    }

    public Figure boardAt(Position pos) {
        return board[pos.x][pos.y].figure;
    }

    public boolean inBounds(Position pos) {
        return pos.x >= 0 && pos.x < 8 && pos.y >= 0 && pos.y < 8;
    }

    public boolean differentPlayer(Figure selected, Position pos) {
        return !notNull(pos) || boardAt(pos).player != selected.player;
    }
}
