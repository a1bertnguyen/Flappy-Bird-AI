package flappy.main;

public class GameLoop implements Runnable{
	private boolean running = false;
    private Thread thread;
    private final IGameLogic game;

    public GameLoop(IGameLogic game) {
        this.game = game;
    }

    public void start() {
        running = true;
        thread = new Thread(this, "GameLoop");
        thread.start();
    }

    @Override
    public void run() {
        game.init();

        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / 60.0; // 60 ups
        double delta = 0.0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1.0) {
                game.update();
                delta--;
            }
            game.render();

            if (game.shouldClose()) {
                running = false;
            }
        }

        game.cleanup();
    }
}
