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

void main() {
    v_texCoord0 = a_texCoord0;

    vec3 pos = a_position;

    pos.z = cos(pos.y * 3.0 * 3.1415 + u_time) * 0.05 * sin(pos.x * 3.0 * 3.1415 + u_time);
    //pos.z = cos(pos.y * 1.0 * 3.1415 + u_time) * 0.1;
    //pos.z = sin(u_time);
    v_z = pos.z;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(pos, 1.0);
    //gl_Position = u_projViewTrans * u_worldTrans * vec4(pos, 1.0);
    //ScreenPosition = gl_Position.xyz;
}



