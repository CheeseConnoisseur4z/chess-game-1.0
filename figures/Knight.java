package tom.chess;

import static tom.chess.ChessEnum.KNIGHT;

public class Knight extends Figure
{
    public Knight(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = new int[][]{{1, 2}, {-1, 2}, {2, 1}, {-2, 1}, {-1, -2}, {1, -2}, {-2, -1}, {2, -1}};
        type = KNIGHT;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♘" : "♞", player);

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
            if (gameBoard.inBounds(pos)) this.preMoves.add(new Position(pos.x, pos.y));
        }
        this.removeFriendlyFire();
    }
}
