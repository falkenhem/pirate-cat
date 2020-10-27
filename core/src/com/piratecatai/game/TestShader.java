package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class TestShader implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    int u_projViewTrans;
    int u_worldTrans;
    int u_color;
    float time;

    @Override
    public void init() {
        String vert = Gdx.files.internal("Data/test.vertex.glsl").readString();
        String frag = Gdx.files.internal("Data/test.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());

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
        //context.setCullFace(GL20.GL_BACK);
        context.setBlending(true,1,1);
    }

    @Override
    public void render(Renderable renderable) {
        time = PirateCatAI.getTime();
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        program.setUniformf("u_time",time);
        program.setUniformi("uSurfaceTexture", 0);
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
