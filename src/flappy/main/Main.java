package flappy.main;

import audio.SoundManager;

public class Main {
    public static void main(String[] args) {
        GameLoop loop = new GameLoop(new GameLogic());
        loop.start();
        
        

        // Khi loop kết thúc, thoát game
        SoundManager.cleanup(); // ✅ giải phóng âm thanh
    }
}
