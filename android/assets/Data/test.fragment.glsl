#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D uSurfaceTexture;
uniform sampler2D u_DepthMap;
uniform vec4 u_ScreenSize;
uniform mat4 matrix_view;
uniform float u_camera_params;
uniform vec3 camera_params;
uniform float u_time;

varying vec2 v_texCoord0;
varying vec3 ScreenPosition;
varying float v_z;
varying vec3 WorldPosition;

#ifdef GL2
    float linearizeDepth(float z) {
        z = z * 2.0 - 1.0;
        return 1.0 / (camera_params.z * z + u_camera_params);
    }
#else
    #ifndef UNPACKFLOAT
    #define UNPACKFLOAT
    float unpackFloat(vec4 rgbaDepth) {
        const vec4 bitShift = vec4(1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0);
        return dot(rgbaDepth, bitShift);
    }
    #endif
#endif

float getLinearScreenDepth(vec2 uv) {
    #ifdef GL2
        return linearizeDepth(texture2D(u_DepthMap, uv).r) * camera_params.y;
    #else
        return unpackFloat(texture2D(u_DepthMap, uv)) * camera_params.y;
    #endif
}

float getLinearDepth(vec3 pos) {
    return -(matrix_view * vec4(pos, 1.0)).z;
}

float getLinearScreenDepth() {
    vec2 uv = gl_FragCoord.xy * u_ScreenSize.zw;
    return getLinearScreenDepth(uv);
}

void main() {

    vec4 color = vec4(0.0,0.7,1.0,0.5);

    vec2 pos = v_texCoord0;

    pos.y += u_time * 0.02;

    vec4 WaterLines = texture2D(uSurfaceTexture,pos);

    color.rgba += (WaterLines.r * 0.2);

    gl_FragColor = color;

    /*float worldDepth = getLinearDepth(WorldPosition);
    float screenDepth = getLinearScreenDepth();
    color = vec4(vec3(screenDepth - worldDepth),1.0);

    gl_FragColor = color;*/
}


