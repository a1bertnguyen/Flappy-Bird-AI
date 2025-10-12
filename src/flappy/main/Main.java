package flappy.main;
public class Main {
    public static void main(String[] args) {
        GameLoop loop = new GameLoop(new GameLogic());
        loop.start();
    }
}
