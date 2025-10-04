package flappy.main;

public interface IGameLogic {
    void init();
    void update();
    void render();
    void cleanup();
    boolean shouldClose();
}
