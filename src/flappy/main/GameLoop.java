package flappy.main;

import flappy.input.input; 

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
    
    
// fix tiếng vỗ chim bay

//    @Override
//    public void run() {
//        game.init();
//
//        long lastTime = System.nanoTime();
//        double ns = 1000000000.0 / 60.0; // 60 FPS
//        double delta = 0.0;
//
//        while (running) {
//            long now = System.nanoTime();
//            delta += (now - lastTime) / ns;
//            lastTime = now;
//
//            while (delta >= 1.0) {
//                game.update();  // 🧠 xử lý logic game & đọc input
//                delta--;
//            }
//
//            game.render();      // 🎨 vẽ khung hình
//            input.update();     // ⌨️ cập nhật trạng thái phím SAU CÙNG mỗi frame ✅
//
//            if (game.shouldClose()) {
//                running = false;
//            }
//        }
//
//        game.cleanup();
//    }
    
    @Override
    public void run() {
        game.init();

        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / 60.0; // 60 FPS
        double delta = 0.0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1.0) {
                input.update();   // 👈 cập nhật input TRƯỚC KHI update game
                game.update();    // 🧠 xử lý logic game
                delta--;
            }

            game.render();        // 🎨 vẽ khung hình

            if (game.shouldClose()) {
                running = false;
            }
        }

        game.cleanup();
    }

    
}
