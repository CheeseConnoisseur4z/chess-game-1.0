package tom.chess;

import static tom.chess.ChessEnum.ROOK;

class Rook extends Figure
{
    public Rook(int p, Position position, Board gameBoard, FigureControls gameFigureControls) {
        player = p;
        pos = position;
        moveSet = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        type = ROOK;
        display = Graphics.createFigureDisplay(pos.x, pos.y, (player == 1) ? "♖" : "♜", player);

        this.gameBoard = gameBoard;
        this.gameFigureControls = gameFigureControls;
        this.addMouseFigureMovement();
    }
}
