package tom.chess;

import static tom.chess.ChessEnum.BISHOP;

public class Bishop extends Figure
{
    public Bishop(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        type = BISHOP;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♗" : "♝", player);

        this.gameBoard = gameBoard;
        this.gameFigureControls = gameFigureControls;
        this.addMouseFigureMovement();
    }
}
