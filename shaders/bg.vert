#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;   // ✅ thêm model matrix để MenuScreen dùng

out DATA
{
    vec2 tc;
    vec3 position;
} vs_out;

void main()
{
    // ✅ thêm ml_matrix vào chuỗi biến đổi
    gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
    vs_out.tc = tc;
    vs_out.position = vec3(vw_matrix * ml_matrix * position);
}
