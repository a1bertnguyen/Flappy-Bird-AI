package flappy.graphics.VertexArray;

public interface IVertexArray {
	void bind();
    void unbind();
    int getCount();
    boolean hasIndices();
}
