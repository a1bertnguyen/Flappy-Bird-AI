package flappy.level.pipe;

import java.util.Random;

import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.bird.Bird;
 

import static org.lwjgl.opengl.GL11.*; 
import static org.lwjgl.opengl.GL13.*;  
 

public class PipeManager {
	private Pipe[] pipes = new Pipe[10];
    private int index = 0;
 
    
    
    private float OFFSET = 3.0f;
    
    private Random random = new Random();
    private int lastPassedPipe = -1;
    private int gap=120;

    public PipeManager(int gap) {
    	this.gap = gap;
        Pipe.create();
        createPipes();
    }


    private void createPipes() {
        for (int i = 0; i < 10; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
 
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.0f);  
            index += 2;
        }
    }

    public void updatePipes() {
        pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
 
        
        pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.0f);
        index += 2;
    }

   
  
    public void render(float birdY, float xScroll) {
        ShaderManager.PIPE.enable();
        ShaderManager.PIPE.setUniform2f("bird", 0, birdY);
       
        ShaderManager.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
 
        glActiveTexture(GL_TEXTURE1);  
        ShaderManager.PIPE.setUniform1i("tex", 1);  
        
        Pipe.getTexture().bind();  
        Pipe.getMesh().bind();

        for (int i = 0; i < 10; i++) {
            ShaderManager.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            ShaderManager.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            Renderer.draw(Pipe.getMesh());
        }

        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
        glActiveTexture(GL_TEXTURE0);  
        ShaderManager.PIPE.disable();
    }
    
    
 
    
    
public boolean checkCollision(Bird bird, float xScroll) {
        
        final float SCROLL_OFFSET = xScroll * 0.05f; 
        float birdX = bird.getX();
        float birdY = bird.getY();
        float halfSize = bird.getSize() / 2.0f;
        
   
        final float MARGIN = 0.1f; 
        float adjustedHalfSize = halfSize + MARGIN; 

  
        float bx0 = birdX - adjustedHalfSize; 
        float bx1 = birdX + adjustedHalfSize; 
        float by0 = birdY - adjustedHalfSize; 
        float by1 = birdY + adjustedHalfSize; 

        for (int i = 0; i < 10; i++) {
       
            float pipeInitialX = pipes[i].getX();
            float pipeY = pipes[i].getY();
            float pipeWorldX = pipeInitialX + SCROLL_OFFSET; 
            float px0 = pipeWorldX;
            float px1 = pipeWorldX + Pipe.getWidth();
            float py0 = pipeY;
            float py1 = pipeY + Pipe.getHeight();

            // KIỂM TRA VA CHẠM CHÍNH (AABB)
            if (bx1 > px0 && bx0 < px1 && by1 > py0 && by0 < py1) {
                System.out.println("!!! VA CHẠM THÀNH CÔNG !!!");
                return true;
            }
        }
        return false;
    }
    
    
 
 public boolean checkPass(Bird bird, float xScroll) { 
   
     float birdX = bird.getX(); 
   
     float scrollOffset = xScroll * 0.05f; 

     for (int i = 0; i < pipes.length; i += 2) { 
         Pipe pipe = pipes[i];
       
         float pipeWorldX = pipe.getX() + scrollOffset;
   
         float pipeRightEdge = pipeWorldX + Pipe.getWidth();
     
         if (!pipe.isPassed() && birdX > pipeRightEdge) { 
              pipe.setPassed(true);
              return true;
         }
     }
     return false;
 }	
    
    
 
    public void reset() {
    	index = 0;
        lastPassedPipe = -1;
        
        for (int i = 0; i < pipes.length; i++) {
            pipes[i] = null;
        }
        createPipes();  
       
        for (int i = 0; i < pipes.length; i++) {
            if (pipes[i] != null) pipes[i].setPassed(false);
        }
    }

}
