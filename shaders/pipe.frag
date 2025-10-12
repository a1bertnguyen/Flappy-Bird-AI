#version 330 core

layout (location = 0) out vec4 color;

in DATA
{
    vec2 tc;
    vec3 position;
} fs_in;

uniform vec2 bird;
uniform sampler2D tex;
uniform int top;

void main()
{
    vec2 texCoord = fs_in.tc;   
    if (top == 1)
        texCoord.y = 1.0 - texCoord.y;

    color = texture(tex, texCoord);   
    if (color.w < 1.0)
        discard;

    color *= 2.0 / (length(bird - fs_in.position.xy) + 1.5) + 0.5;
    color.w = 1.0;
}