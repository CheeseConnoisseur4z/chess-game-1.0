package tom.chess;

/*
will hold more information once playing algorithm and status screen are added;
 */
public class GameInfo
{
    int player;
    int turn = 0;
    Game game;

    public GameInfo(Game game) {
        this.game = game;
    }
}
