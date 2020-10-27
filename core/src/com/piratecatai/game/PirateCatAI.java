package com.piratecatai.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.piratecatai.game.pathfinding.Graph;
import com.piratecatai.game.pathfinding.Node;


import static com.badlogic.gdx.math.MathUtils.atan2;


public class PirateCatAI implements ApplicationListener, InputProcessor {
	private static float time;
	public Environment environment;
	public PerspectiveCamera cam;
	public Shader shader;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	private ModelInstance playerInstance;
	private ModelInstance NPCinstance;
	private ModelInstance islandInstance;
	public static World world;
	protected static Player player;
	private Vector3 position;
	private Array<Player> players;
	private Array<Body> allBodiesInWorld;
	private Array<ModelInstance> water;
	protected static Array<CannonBall> cannonBalls;
	private Array<Island> islands;
	private Array<NPCship> npcships;
	protected static Array<ModelInstance> debugInstances;
	protected static BodyCreator bodycreator;
	protected Pixmap pixmap;
	private GraphPath<Node> nodePath;
	private Graph nodeGraph;

	@Override
	public void create () {
		pixmap = new Pixmap(Gdx.files.internal("pixMap.png"));

		world = new World(new Vector2(0, 0f),true);

		allBodiesInWorld = new Array<Body>();
		players = new Array<Player>();
		water = new Array<ModelInstance>();
		cannonBalls = new Array<CannonBall>();
		islands = new Array<Island>();
		npcships = new Array<NPCship>();
		debugInstances = new Array<ModelInstance>();
		//nodeGraph = NodeMapGenerator.generateGraph(pixmap);


		bodycreator = BodyCreator.getInstance(world);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 200f, 0f);
		//cam.rotate(new Vector3(0,1,0),90f);
		cam.lookAt(0,0,0);
		cam.near = 20f;
		cam.far = 1000f;
		cam.update();

		//separate 3D import function later
		ModelLoader modelLoader = new G3dModelLoader(new JsonReader());

		model = modelLoader.loadModel(Gdx.files.internal("Simple_Pirate_Ship_y_up_x_forw.g3dj"));
		playerInstance = new ModelInstance(model);
		playerInstance.transform.setTranslation(100,0,100);
		player = new Player(playerInstance, world);

		for (int x =0 ; x<=0 ; x+=40) {
			NPCinstance = new ModelInstance(model);
			NPCinstance.transform.setTranslation(x,-3f,0f);
			npcships.add(new NPCship(NPCinstance, world));
		}

		model = modelLoader.loadModel(Gdx.files.internal("island.g3dj"));
		islandInstance = new ModelInstance(model,80f,0f,230f);
		islands.add(new Island(islandInstance,world));


		model = modelLoader.loadModel(Gdx.files.internal("flat_water_5.g3dj"));
		model.getMaterial("Material").set(new BlendingAttribute(1f));
		for (int x=100 ; x<=1100 ; x+=200) {
			for (int z = 100; z <=1100 ; z+=200) {
				water.add(new ModelInstance(model, x,0f,z));
			}

		}
		shader = new TestShader();
		shader.init();

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA().getBody().getUserData() instanceof CannonBall
						&& contact.getFixtureB().getBody().getUserData() instanceof NPCship) {
					((NPCship) contact.getFixtureB().getBody().getUserData()).destroyed = true;
					((CannonBall) contact.getFixtureA().getBody().getUserData()).destroyed = true;
				}

				if (contact.getFixtureB().getBody().getUserData() instanceof CannonBall
						&& contact.getFixtureA().getBody().getUserData() instanceof NPCship) {
					((NPCship) contact.getFixtureA().getBody().getUserData()).destroyed = true;
					((CannonBall) contact.getFixtureB().getBody().getUserData()).destroyed = true;
				}
			}
			@Override
			public void endContact(Contact contact) {
			}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}


		});

		//nodePath = nodeGraph.findPath(NodeMapGenerator.nodes.get(0),NodeMapGenerator.nodes.get(3599));
		//System.out.println(nodePath);

	}

	@Override
	public void render () {
		time += Gdx.graphics.getDeltaTime();
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);


		for (Body body:allBodiesInWorld) {
			if (body.getUserData() instanceof NPCship) {
				if (((NPCship) body.getUserData()).destroyed) {
					npcships.removeValue(((NPCship) body.getUserData()), true);
					world.destroyBody(body);
				}
			}

			if (body.getUserData() instanceof CannonBall) {
				if (((CannonBall) body.getUserData()).destroyed) {
					cannonBalls.removeValue(((CannonBall) body.getUserData()),true);
					world.destroyBody(body);
				}
			}
		}

		world.step(1f/60f, 6, 2);

		world.getBodies(allBodiesInWorld);

		for (Body body:allBodiesInWorld) {

			if (body.getUserData() instanceof Player) player.update(time);

			if (body.getUserData() instanceof NPCship) ((NPCship) body.getUserData()).update(time);

			if (body.getUserData() instanceof CannonBall) {
				((CannonBall) body.getUserData()).instance.transform.setTranslation(body.getPosition().x,3,body.getPosition().y);
			}
			if (body.getUserData() instanceof Island) ((Island) body.getUserData()).instance.transform.setTranslation(body.getPosition().x,-10f,
					body.getPosition().y);
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.position.set(player.instance.transform.getTranslation(new Vector3()).add(0f, 150f, 150f));
		cam.lookAt(player.instance.transform.getTranslation(new Vector3()));
		cam.update();

		modelBatch.begin(cam);
		modelBatch.render(playerInstance, environment);
		for (Island island : islands)
			modelBatch.render(island.instance, environment);
		for (CannonBall cannonBall : cannonBalls)
			modelBatch.render(cannonBall.instance, environment);
		for (NPCship npcship : npcships)
			modelBatch.render(npcship.instance,environment);
		for (ModelInstance instance : debugInstances)
			modelBatch.render(instance,environment);
		for (ModelInstance instance : water)
			modelBatch.render(instance,environment,shader);
		modelBatch.end();



	}

	@Override
	public void dispose () {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.UP) {
			player.moving = true;
		}
		if(keycode == Input.Keys.LEFT) {
			player.turnLeft = true;
		}
		if(keycode == Input.Keys.RIGHT) {
			player.turnRight = true;
		}
		if(keycode == Input.Keys.Q) {
			player.shootingFromSide = 0;
			player.shooting = true;
		}
		if(keycode == Input.Keys.E) {
			player.shootingFromSide = 1;
			player.shooting = true;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.RIGHT) {
			player.turnRight = false;

		}

		if(keycode == Input.Keys.LEFT) {
			player.turnLeft = false;

		}

		if(keycode == Input.Keys.UP) {
			player.moving = false;

		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/*
		Vector2 point;
		Vector2 origin;
		Ray ray = cam.getPickRay(screenX, screenY);
		position = new Vector3();
		final float distance = -ray.origin.y / ray.direction.y;
		position.set(ray.direction).scl(distance).add(ray.origin);
		System.out.println(position);

		point = new Vector2(position.x,position.z);
		shoot(player.body.getPosition(),point);

		 */
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return true;
	}
	public static float getTime(){return time;}
	public static float getAngleFromBodyOnWave(Body body){
		float angle;
		Vector2 direction;
		float currentPosY;
		float futurePosY;

		direction = body.getLinearVelocity();
		direction = direction.nor();

		currentPosY = 100f*(MathUtils.cos(-body.getPosition().y/100 * 3.0f * 3.1415f + time) * 0.05f *
				MathUtils.sin(body.getPosition().x/100 * 3.0f * 3.1415f + time));

		futurePosY = 100f*(MathUtils.cos(-(body.getPosition().y + direction.y)/100 * 3.0f * 3.1415f + time) * 0.05f *
				MathUtils.sin((body.getPosition().x + direction.x)/100 * 3.0f * 3.1415f + time));

		angle = atan2(futurePosY-currentPosY,1)/2;

		return angle;
	}

	public static void shoot(Vector2 origin, Vector2 direction, Vector2 inertia){
		Model model;
		ModelBuilder modelBuilder;
		ModelInstance instance;
		modelBuilder = new ModelBuilder();
		//Vector2 direction;

		model = modelBuilder.createSphere(4f,4f,4f,4,4,
				new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);

		//direction = destination.sub(origin);
		//direction.nor();

		origin = new Vector2(origin.x + direction.x*15f,origin.y + direction.y*15f);

		direction = new Vector2(direction.x, direction.y);

		instance.transform.setTranslation(origin.x,3,origin.y);

		PirateCatAI.cannonBalls.add(new CannonBall(instance, world, direction,inertia));


	}


}
