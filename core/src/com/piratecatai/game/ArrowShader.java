package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class ArrowShader implements Shader {
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
        texture = new Texture(Gdx.files.internal("erikf.png"));
        //texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        String vert = Gdx.files.internal("Data/arrow.vertex.glsl").readString();
        String frag = Gdx.files.internal("Data/arrow.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        //ShaderProgram.pedantic = false;
        if (!program.isCompiled()) {
            throw new GdxRuntimeException(program.getLog());
        }

        u_projViewTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
        time = 0;
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
        context.setBlending(true, GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render(Renderable renderable) {
        time += Gdx.graphics.getDeltaTime();
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        program.setUniformf("u_time",time);
        //program.setUniformi("uSurfaceTexture", context.textureBinder.bind(texture));
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    public void reset(){
        time = 0;
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
