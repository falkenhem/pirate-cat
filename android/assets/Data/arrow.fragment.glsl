#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D uSurfaceTexture;
uniform float u_time;

varying vec2 v_texCoord0;
varying vec3 ScreenPosition;
varying float v_z;
varying vec3 position;


void main() {

    float maxY;
    maxY = u_time;
    vec2 pos = v_texCoord0;

    float scalar = sign(position.y-maxY);
    vec4 color = vec4(1.0-scalar,0.0,0.0,1.0-scalar);
  /*  pos.y += u_time * 0.02;

    vec4 texture = texture2D(uSurfaceTexture,pos);

    color.rgba += texture.r;*/

    gl_FragColor = color;

}


