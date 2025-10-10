//package flappy.graphics.Shader;
//
//import flappy.graphics.Shader.Shader;
//
//public class ShaderManager {
//	public static Shader BG;
//    public static Shader BIRD;
//    public static Shader PIPE;
//    public static Shader FADE;
//
//    public static void loadAll() {
//        BG   = new Shader("shaders/bg.vert", "shaders/bg.frag");
//        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
//        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
//        FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
//    }
//}

package flappy.graphics.Shader;

public class ShaderManager {
    public static Shader BG;
    public static Shader BIRD;
    public static Shader PIPE;
    public static Shader FADE;
    public static Shader UI; // ✅ thêm shader cho menu

    public static void loadAll() {
        BG   = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
        FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
        UI   = new Shader("shaders/ui.vert", "shaders/ui.frag"); // ✅ shader dùng cho MenuScreen
    }
}
