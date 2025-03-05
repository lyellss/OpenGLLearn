#version 100

uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinate;
varying vec2 v_TextureCoordinate;

void main() {
    gl_Position = u_Matrix * a_Position;
    // 传递纹理坐标
    // 也可以使用 v_TextureCoordinate = a_TextureCoordinate.xy;
    // 顶点中定义 和 着色器中定义 一致，即可直接传值
    v_TextureCoordinate = a_TextureCoordinate;
}