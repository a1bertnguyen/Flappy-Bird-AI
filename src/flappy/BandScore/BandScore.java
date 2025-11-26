 


package flappy.BandScore;

import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.score.ScoreManager;
import flappy.Screen.Difficulty;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class BandScore {

    private final ScoreManager scoreManager;
    private final Difficulty difficulty;

    private final VertexArray panelVAO;
    private final Texture panelTexture;

    // Difficulty textures (easy.png, medium.png, hard.png trong res)
    private final Texture diffTexture;

    // Bird skin texture
    private final Texture birdSkin;

    // Medal textures
    private final Texture copperMedal;
    private final Texture silverMedal;
    private final Texture goldMedal;
    private final Texture blankMedal;

    public BandScore(ScoreManager scoreManager, Difficulty difficulty, Texture birdSkin) {
        this.scoreManager = scoreManager;
        this.difficulty = difficulty;
        this.birdSkin = birdSkin;

        panelTexture = TextureLoader.load("res/bandscore.png");
        panelVAO = new VertexArray(
                new float[]{0,0,0, 1,0,0, 1,1,0, 0,1,0},
                new byte[]{0,1,2, 2,3,0},
                new float[]{0,0, 1,0, 1,1, 0,1}
        );

        // Load difficulty texture
        switch(difficulty) {
            case EASY -> diffTexture = TextureLoader.load("res/easy.png");
            case MEDIUM -> diffTexture = TextureLoader.load("res/medium.png");
            case HARD -> diffTexture = TextureLoader.load("res/hard.png");
            default -> diffTexture = TextureLoader.load("res/easy.png");
        }

        // Load medals
        copperMedal = TextureLoader.load("res/CopperMedal.png");
        silverMedal = TextureLoader.load("res/SilverMedal.png");
        goldMedal = TextureLoader.load("res/GoldMedal.png");
        blankMedal = TextureLoader.load("res/BlankMedal.png");
    }

    public void render() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderManager.UI.enable();
        ShaderManager.UI.setUniformMat4f("pr_matrix",
                Matrix4f.orthographic(0, 1280, 0, 720, -1, 1));

        // 1️⃣ Vẽ panel
        glActiveTexture(GL_TEXTURE0);
        panelTexture.bind();
        ShaderManager.UI.setUniform1i("tex", 0);
//        Matrix4f panelModel = Matrix4f.translate(400, 250, 0)
//                .multiply(Matrix4f.scale(480, -220, 1));
        
   // căn giữa ban score
        
        float panelW = 580;
        float panelH = 350;
        float panelX = 640 - panelW/2;  // center horizontally
        float panelY = 320;             // lower than gameover

        Matrix4f panelModel = Matrix4f.translate(panelX, panelY+100, 0)
                .multiply(Matrix4f.scale(panelW, -panelH, 1));

        
        
        ShaderManager.UI.setUniformMat4f("ml_matrix", panelModel);
        Renderer.draw(panelVAO);
        panelTexture.unbind();

        // 2️⃣ Vẽ score
//        renderScore(scoreManager.getScore(), 550, 350);
        
        
//        renderScore(scoreManager.getScore(), panelX + 180, panelY + 130);
        
        renderScore(scoreManager.getScore(), panelX + 380, panelY  );


        // 3️⃣ Vẽ difficulty bên trái
        diffTexture.bind();
//        Matrix4f diffModel = Matrix4f.translate(450, 420, 0)
//                .multiply(Matrix4f.scale(100, -50, 1));
        
        
        
        Matrix4f diffModel = Matrix4f.translate(panelX + 460, panelY -5, 0)
                .multiply(Matrix4f.scale(100, -50, 1));

        
        ShaderManager.UI.setUniformMat4f("ml_matrix", diffModel);
        Renderer.draw(panelVAO);
        diffTexture.unbind();

        // 4️⃣ Vẽ bird skin
        birdSkin.bind();
//        Matrix4f birdModel = Matrix4f.translate(650, 420, 0)
//                .multiply(Matrix4f.scale(64, -64, 1));
        
//        Matrix4f birdModel = Matrix4f.translate(panelX + 340, panelY + 160, 0)
        
        Matrix4f birdModel = Matrix4f.translate(panelX + 340, panelY -65 , 0)
                .multiply(Matrix4f.scale(64, -64, 1));

        
        ShaderManager.UI.setUniformMat4f("ml_matrix", birdModel);
        Renderer.draw(panelVAO);
        birdSkin.unbind();

        // 5️⃣ Vẽ medal theo score
        Texture medal;
        int score = scoreManager.getScore();
        if(score >= 50) medal = goldMedal;
        else if(score >= 40) medal = silverMedal;
        else if(score >= 30) medal = copperMedal;
        else medal = blankMedal;

        medal.bind();
//        Matrix4f medalModel = Matrix4f.translate(700, 350, 0)
//                .multiply(Matrix4f.scale(64, -64, 1));
        
//        
//        Matrix4f medalModel = Matrix4f.translate(panelX + 360, panelY + 80, 0)
        
        Matrix4f medalModel = Matrix4f.translate(panelX + 40, panelY + 25, 0)
                .multiply(Matrix4f.scale(125, -125, 1));

        
        ShaderManager.UI.setUniformMat4f("ml_matrix", medalModel);
        Renderer.draw(panelVAO);
        medal.unbind();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    private void renderScore(int score, float startX, float startY) {
        String scoreText = String.valueOf(score);
        float digitWidth = 32;
        for(int i = 0; i < scoreText.length(); i++) {
            char c = scoreText.charAt(i);
            if(Character.isDigit(c)) {
                int d = c - '0';
                Texture digitTexture = scoreManager.getDigit(d);
                digitTexture.bind();
                Matrix4f model = Matrix4f.translate(startX + i*(digitWidth+4), startY + digitWidth, 0)
                        .multiply(Matrix4f.scale(digitWidth, -digitWidth, 1));
                ShaderManager.UI.setUniformMat4f("ml_matrix", model);
                Renderer.draw(scoreManager.getVao());
                digitTexture.unbind();
            }
        }
    }
}
