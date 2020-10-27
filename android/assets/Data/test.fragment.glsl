#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D uSurfaceTexture;
uniform float u_time;

varying vec2 v_texCoord0;
varying vec3 ScreenPosition;
varying float v_z;

void main() {

    vec4 color = vec4(0.0,0.7,1.0,0.5);

    vec2 pos = v_texCoord0;
    //pos *= 2.0;

    pos.y += u_time * 0.02;

    //color = texture2D(uSurfaceTexture,v_texCoord0);
    vec4 WaterLines = texture2D(uSurfaceTexture,pos);
    color.rgba += WaterLines.r;

    gl_FragColor = color;
}


