package flappy.Screen;

public enum Difficulty {
	EASY(1, 120, 335),
    MEDIUM(2, 100, 300),
    HARD(3, 80, 270);

    public final int speed;
    public final int pipeGap;
    public final int backgroundLength;

    Difficulty(int speed, int pipeGap, int backgroundLength) {
        this.speed = speed;
        this.pipeGap = pipeGap;
        this.backgroundLength = backgroundLength;
    }
}
