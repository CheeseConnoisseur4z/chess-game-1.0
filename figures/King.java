package tom.chess;

import static tom.chess.ChessEnum.KING;

public class King extends Figure
{
    private boolean check;
    private boolean checkMate;


    public King(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        type = KING;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♚" : "♔", player);
        check = false;
        checkMate = false;

        this.gameBoard = gameBoard;
        this.gameFigureControls = gameFigureControls;
        this.addMouseFigureMovement();
    }

    /*
    doesn't declare a new ArrayList to moves due to it overriding castling;
    */
    @Override
    public void calculateMoves() {
        Position pos = this.beforeCalculation();
        for (int[] move : moveSet) {
            pos.x = this.pos.x + move[0];
            pos.y = this.pos.y + move[1];
            if (gameBoard.inBounds(pos)) this.preMoves.add(new Position(pos.x, pos.y));
        }
        this.removeFriendlyFire();
    }


    /*
    loops through tiles and checks if king is being attacked by enemy figure;
    called for every move validation;

    A BETTER WAY TO DO THIS:
        - delete generate all moves in validation (reduces total calculation needed by ? 60-80%)
        - make Figure abstract -> abstract void getMove()
        - 2 move functions (general move function, pawn move function)
        - getMove calls move function with figure subclass's moveSet
        - make king move from his position with every figure's move set
        - if it can eat the enemy's figure (with its move set), then the figure can eat him -> check
    WHY NOT DO THIS?:
        - I don't feel like doing it
        - it's better for the "AI" algorithm to know more information for further evaluation
     */
    public void checkCheck() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) if (gameBoard.notNull(x, y) && gameBoard.board[x][y].figure.player != this.player) {
                for (Position to : gameBoard.board[x][y].figure.preMoves) if (to.x == this.pos.x && to.y == this.pos.y) {
                    this.check = true;
                    return;
                }
            }
        }

        this.check = false;
    }


    /*
    checks only if king is in check;
    loops through board and looks any of the figures can move --> if it can this means it can save the king;
     */
    public void checkCheckMate() {
        if (!this.moves.isEmpty() || !this.check) return;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (gameBoard.notNull(x, y) && gameBoard.board[x][y].figure.player == this.player && gameBoard.board[x][y].figure.moves.size() > 0) {
                    this.checkMate = false;
                    return;
                }
            }
        }

        this.checkMate = true;
    }


    /*
    loops until it checks if king's line is empty;
    puts castling to list of moves and remembers with which rook the move is preformed;
     */
    public void castling(Figure rook) {
        if (rook.player != this.player || this.movedTimes != 0 || rook.movedTimes != 0) return;

        Position rookPosition = rook.pos;
        int r = (this.pos.x > rookPosition.x) ? -1 : 1;
        int y = this.pos.y;
        int x = this.pos.x + r;

        while (x != rookPosition.x) {
            if (gameBoard.notNull(x, y)) return;
            x += r;
        }

        if (r == -1) {
            if (gameBoard.virtualCastling(this, new Position(2, y), rook, new Position(3, y))) this.canCastle(new Position(2, y), rook);
        } else {
            if (gameBoard.virtualCastling(this, new Position(6, y), rook, new Position(5, y))) this.canCastle(new Position(6, y), rook);
        }
    }

    public void canCastle(Position kingMove, Figure rook) {
        this.castlingPartner.put(kingMove.x, rook);
        this.moves.add(kingMove);
    }


    public boolean getCheck() {
        return this.check;
    }

    public boolean getCheckMate() {
        return this.checkMate;
    }
}
