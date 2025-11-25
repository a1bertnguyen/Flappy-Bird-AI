package flappy.Screen;
 

public enum Difficulty {
    EASY(0.12f, 120, 335),
    MEDIUM(0.16f, 100, 300),
    HARD(0.20f, 80, 270);

    public final float speed;     // <--- đổi int → float
    public final int pipeGap;
    public final int backgroundLength;

    Difficulty(float speed, int pipeGap, int backgroundLength) {
        this.speed = speed;
        this.pipeGap = pipeGap;
        this.backgroundLength = backgroundLength;
    }
}
