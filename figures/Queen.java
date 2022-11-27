package tom.chess;

import static tom.chess.ChessEnum.QUEEN;

public class Queen extends Figure
{
    public Queen(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        type = QUEEN;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♕" : "♛", player);

        this.gameBoard = gameBoard;
        this.gameFigureControls = gameFigureControls;
        this.addMouseFigureMovement();
    }
}