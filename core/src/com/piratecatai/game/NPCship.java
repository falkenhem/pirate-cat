package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.piratecatai.game.pathfinding.Node;
import com.badlogic.gdx.math.GeometryUtils;


public class NPCship extends Ship{
    float distanceToPlayer;
    float speed;
    Player player;
    final float interceptScalar = 0.1f;
    protected StateMachine<NPCship, NPCshipState> stateMachine;
    SteeringAcceleration<Vector2> steering;
    CentralRayWithWhiskersConfiguration<Vector2> rayConfig;
    Pursue<Vector2> pursue;
    RaycastCollisionDetector<Vector2> raycastCollisionDetector;
    RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance;
    PrioritySteering<Vector2> prioritySteeringPursue;
    PrioritySteering<Vector2> prioritySteeringEngage;
    MatchVelocity<Vector2> matchVelocity;
    FollowPath followPath;
    SteeringBehavior steeringBehavior;
    LinePath pathToPlayer;
    CoolDownManager coolDownManager;
    int gunsCD;


    public NPCship(ModelInstance instance, World world, float health, Player player,
                   EffectsManager effectsManager, GameAssetManager gameAssetManager){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody, health, effectsManager, gameAssetManager);
        this.speed = 10f;
        this.pitch = 0;
        this.player = player;
        gunsCD = 5000;

        coolDownManager = new CoolDownManager();
        coolDownManager.put("shootingCD",0f);

        cannonsLocalPosition = new Vector2[2];
        cannonsLocalPosition[LEFT] = new Vector2(0,-20f);
        cannonsLocalPosition[RIGHT] = new Vector2(0,20f);

        generateNewPathToPlayer();

        steering= new SteeringAcceleration<Vector2>(new Vector2());
        matchVelocity = new MatchVelocity<Vector2>(steerable,player.getSteerable());
        followPath = new FollowPath(steerable,pathToPlayer,50);
        followPath.setArriveEnabled(false);
        rayConfig = new CentralRayWithWhiskersConfiguration<Vector2>(steerable, 60,
                55, 45 * MathUtils.degreesToRadians);
        raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(steerable,rayConfig,raycastCollisionDetector,12f);
        pursue = new Pursue<Vector2>(steerable,player.getSteerable());

        prioritySteeringPursue = new PrioritySteering<Vector2>(steerable, 0.001f)
                .add(followPath);
        prioritySteeringEngage = new PrioritySteering<Vector2>(steerable, 0.01f)
                .add(matchVelocity);
        steeringBehavior = prioritySteeringPursue;
        stateMachine = new DefaultStateMachine<NPCship, NPCshipState>(this, NPCshipState.PURSUE_PLAYER, NPCshipState.GLOBAL_STATE);

    }

    public void update(float time){
        super.update();
        distanceToPlayer = body.getPosition().dst(player.body.getPosition());
        stateMachine.update();
        instance.transform.setFromEulerAnglesRad(body.getAngle() - MathUtils.PI/2,pitch, PirateCatAI.getAngleFromBodyOnWave(body));
        instance.transform.setTranslation(body.getPosition().x,
                100f*(MathUtils.cos(-body.getPosition().y/100 * 3.0f * 3.1415f + time) * 0.05f *
                        MathUtils.sin(body.getPosition().x/100 * 3.0f * 3.1415f + time))-3f, body.getPosition().y);
    }

    public float getTimeUntilInterceptPlayer(){
        float timeFromPlayer;

        timeFromPlayer = Gdx.graphics.getDeltaTime() * distanceToPlayer/speed;

        return timeFromPlayer;
    }

    protected void turnAndSetPitch(){
        Vector2 direction;
        float angle;
        float totalRotation;

        direction = steering.linear;
        direction.nor();

        angle = steerable.vectorToAngle(direction);

        totalRotation = body.getAngle() + angle;
        if (totalRotation < -MathUtils.PI) totalRotation += 2*MathUtils.PI;
        if (totalRotation > MathUtils.PI) totalRotation -= 2*MathUtils.PI;

        if (totalRotation>0) body.setAngularVelocity(-0.8f);
        if (totalRotation<0) body.setAngularVelocity(0.8f);


        if (body.getAngularVelocity() < 0)
            if (pitch < 0.3f) pitch += 0.01f;

        if (body.getAngularVelocity() > 0)
            if (pitch > -0.3f) pitch -= 0.01f;

        if (body.getAngularVelocity() == 0) {
            if (pitch < 0f) pitch += 0.01;
            if (pitch > 0f) pitch -= 0.01f;
        }
    }

    protected void generateNewPathToPlayer(){
        Node closestNodeToNPCship;
        Node closestNodeToPlayer;
        Vector2 futurePositionPlayer;

        futurePositionPlayer = player.getFuturePositionWithTime(getTimeUntilInterceptPlayer() * interceptScalar);

        closestNodeToNPCship = PirateCatAI.nodeGraph.getNodeByCoordinates(body.getPosition());
        closestNodeToPlayer = PirateCatAI.nodeGraph.getNodeByCoordinates(futurePositionPlayer);

        if (closestNodeToNPCship != closestNodeToPlayer){
            pathToPlayer = PirateCatAI.nodeGraph.findPath(closestNodeToNPCship,closestNodeToPlayer);
        }

    }

    private void updateCoolDowns(){

    }

    protected boolean checkIfPlayerInShootingRange(int directionToCheck){
        Vector2[] lineOfSightTriangle = new Vector2[3];
        Vector2[] lineOfSightTriangleWorld = new Vector2[3];
        Vector2 barycoord;

        lineOfSightTriangle[0] = new Vector2(0f, 0f);

        switch(directionToCheck) {
            case LEFT:
                lineOfSightTriangle[1] = new Vector2(-20f, -100f);
                lineOfSightTriangle[2] = new Vector2(20f, -100f);
                break;
            case RIGHT:
                lineOfSightTriangle[1] = new Vector2(-20f, 100f);
                lineOfSightTriangle[2] = new Vector2(20f, 100f);
                break;
        }

        for (int i = 0; i<=2; i++) {
            lineOfSightTriangleWorld[i] = getWorldPointFromLocalBodyVector(lineOfSightTriangle[i], body);
            PirateCatAI.addDebugInstance(lineOfSightTriangleWorld[i]);
        }

        barycoord = new Vector2();

        GeometryUtils.toBarycoord(player.getPosition(),lineOfSightTriangleWorld[0],lineOfSightTriangleWorld[1],lineOfSightTriangleWorld[2],barycoord);

        if (GeometryUtils.barycoordInsideTriangle(barycoord)) return true;
        else return false;

    }


    @Override
    protected void setDestroyed() {
        effectsManager.addStationaryParticleEffect(gameAssetManager.getEffectByName("blastWave"),getPos(4f),
                new Vector3(10f,10f,10f));
        effectsManager.addStationaryParticleEffect(gameAssetManager.getEffectByName("betterExplosion"),getPos(4f),
                new Vector3(6f,6f,6f));
        super.setDestroyed();
    }
}


