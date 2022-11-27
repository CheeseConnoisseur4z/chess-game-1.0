package tom.chess;

import static tom.chess.ChessEnum.PAWN;

class Pawn extends Figure
{
    public Pawn(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = (player == 1) ? new int[][]{{0, 1}, {1, 1}, {-1, 1}} : new int[][]{{0, -1}, {1, -1}, {-1, -1}};
        type = PAWN;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♙" : "♟", player);

        this.gameBoard = gameBoard;
        this.gameFigureControls = gameFigureControls;
        this.addMouseFigureMovement();
    }

    /*
    figure specific movement calculation;
    */
    @Override
    public void calculateMoves() {
        Position pos = this.beforeCalculation();
        for (int[] move : moveSet) {
            pos.x = this.pos.x + move[0];
            pos.y = this.pos.y + move[1];
            if (gameBoard.inBounds(pos)) {
                if (move[0] == 0 && !gameBoard.notNull(pos)) {
                    this.firstMove(move[1]);
                    this.preMoves.add(new Position(pos.x, pos.y));
                } else if (move[0] != 0 && gameBoard.notNull(pos)){
                    this.preMoves.add(new Position(pos.x, pos.y));
                }
            }
        }
        this.removeFriendlyFire();
    }


    /*
    move 2 tiles instead of 1 of not moved before;
     */
    public void firstMove(int y) {
        if (this.movedTimes == 0 && !gameBoard.notNull(new Position(this.pos.x, this.pos.y + 2 * y))) {
            this.preMoves.add(new Position(this.pos.x, pos.y + 2 * y));
        }
    }
}
