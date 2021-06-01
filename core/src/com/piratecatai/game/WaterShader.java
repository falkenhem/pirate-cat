package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class WaterShader implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    int u_projViewTrans;
    int u_worldTrans;
    int u_color;
    Texture texture;
    float time;

    @Override
    public void init() {
        texture = new Texture(Gdx.files.internal("waves.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        String vert = Gdx.files.internal("Data/test.vertex.glsl").readString();
        String frag = Gdx.files.internal("Data/test.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        //ShaderProgram.pedantic = false;
        if (!program.isCompiled()) {
            throw new GdxRuntimeException(program.getLog());
        }

        u_projViewTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
    }

    @Override
    public void dispose() {
        program.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.bind();
        program.setUniformMatrix(u_projViewTrans, camera.combined);
        context.setBlending(true,GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render(Renderable renderable) {
        time = PirateCatAI.getTime();
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        /*program.setUniformf("u_time",time);
        program.setUniformi("uSurfaceTexture", context.textureBinder.bind(texture));*/
        /*float f = camera.far;
        float n = camera.near;
        program.setUniformf("u_camera_params", 1f);
        program.setUniformf("camera_params", new Vector3(1/f,
                f,
                (1-f / n) / 2));
        float camera_paramsW = (1 + f / n) / 2;*/

        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }
    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

}
