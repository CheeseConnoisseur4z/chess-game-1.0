package tom.chess;

import javax.swing.*;

class Tile
{
    Figure figure;
    JPanel display;

    public Tile(int x, int y) {
        this.display = Graphics.createTileDisplay(x, y);
    }
}
