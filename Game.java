package tom.chess;

public class Game
{
    public GameInfo gameInfo = new GameInfo(this);
    public Board gameBoard = new Board(gameInfo);


    void start() {
        gameGraphics();
        gameBoard.calculateAllMoves(false);
    }


    /*
    called by dragEnd function;
    checks for game over or if the player can't make a move;
     */
    public void makeMove(Figure movedFigure, Position movedTo, boolean isCastling) {
        if (movedFigure != null && movedTo != null) {
            makeMoveCastling(movedFigure, movedTo, isCastling);
            gameBoard.moveFigure(movedFigure, movedTo);
            if (movedFigure.type == ChessEnum.PAWN) {
                movedFigure.promotion();
            }
        }
        turnChange();
        gameBoard.calculateAllMoves(false);
        checkForGameOver();
        skipTurnIfNeeded();
    }


    /*
    move the rook as well
     */
    public void makeMoveCastling(Figure king, Position to, boolean isCastling) {
        if (!isCastling) return;
        Figure rook = king.castlingPartner.get(to.x);
        Position rookTo = (king.pos.x > rook.pos.x) ? new Position(3, rook.pos.y) : new Position(5, rook.pos.y);
        gameBoard.moveFigure(rook, rookTo);
    }


    public void turnChange() {
        gameInfo.player = (gameInfo.player == 1) ? 0 : 1;
        gameInfo.turn++;
    }


    public void gameOver() {
        System.out.println("CHECK MATE");
        while (true) {}
    }


    public void checkForGameOver() {
        for (int p = 0; p < 2; p++) {
            gameBoard.getKing(p).checkCheck();
            gameBoard.getKing(p).checkCheckMate();
        }
        if (gameBoard.kingWhite.getCheckMate() || gameBoard.kingBlack.getCheckMate()) {
            gameOver();
        }
    }


    /*
    skips turn if player's figures are unable to move;
     */
    public void skipTurnIfNeeded() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++){
                if (gameBoard.board[x][y].figure != null && gameBoard.board[x][y].figure.player == gameInfo.player && gameBoard.board[x][y].figure.moves.size() > 0) {
                    return;
                }
            }
        }
        System.out.println("cant move, turn skipped");
        makeMove(null, null, false);
    }


    void gameGraphics() {
        Graphics.frame.setSize(414, 436);
        gameBoard.addFiguresToBoard();
        Graphics.addAllTileDisplay(gameBoard.board, true);
        Graphics.frame.setSize(413, 435);
    }
}
