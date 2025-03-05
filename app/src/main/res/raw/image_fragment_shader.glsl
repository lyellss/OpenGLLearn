#version 100

// 精度 必要，否则编译不通过
precision mediump float;

varying vec2 v_TextureCoordinate;

uniform sampler2D u_TextureUnit;

void main() {
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinate);
}