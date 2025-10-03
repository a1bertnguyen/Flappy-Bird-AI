package com.thecherno.flappy.graphics;

public interface IVertexArray {
	void bind();
    void unbind();
    int getCount();
    boolean hasIndices();
}
