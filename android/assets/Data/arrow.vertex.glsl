attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
attribute vec2 a_uv0;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform float u_time;

varying vec3 ScreenPosition;
varying vec2 v_texCoord0;
varying float v_z;
varying vec3 position;


void main() {
    v_texCoord0 = a_texCoord0;

    vec3 pos = a_position;
    position = a_position;

    gl_Position = u_projViewTrans * u_worldTrans * vec4(pos, 1.0);

}


