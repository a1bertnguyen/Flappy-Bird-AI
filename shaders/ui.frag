#version 330 core

in vec2 pass_texCoord;
out vec4 color;

uniform sampler2D tex;

void main() {
    color = texture(tex, pass_texCoord);
}
