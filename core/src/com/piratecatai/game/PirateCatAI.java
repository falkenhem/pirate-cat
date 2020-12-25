package com.piratecatai.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.piratecatai.game.pathfinding.Graph;
import com.piratecatai.game.pathfinding.NodeMapGenerator;
import com.piratecatai.game.pathfinding.PixMapOfMap;

import static com.badlogic.gdx.math.MathUtils.atan2;


public class PirateCatAI implements ApplicationListener {
	private static float time;
	public Environment environment;
	private static PerspectiveCamera cam;
	public Shader shader;
	private ArrowShader arrowShader;
	public ModelBatch modelBatch;
	public Model model;
	private ModelInstance playerInstance;
	private ModelInstance NPCinstance;
	private ModelInstance islandInstance;
	public World world;
	protected Player player;
	private Vector3 position;
	private Array<Player> players;
	private Array<Body> allBodiesInWorld;
	private Array<ModelInstance> water;
	protected static Array<CannonBall> cannonBalls;
	private Array<Island> islands;
	private Array<NPCship> npcships;
	public static Array<ModelInstance> debugInstances;
	protected static BodyCreator bodycreator;
	protected static  Graph nodeGraph;
	private static DecalBatch decalBatch;
	private static int mapWidth;
	private static int mapHeight;
	private GameObjectManager gameObjectManager;
	private PixMapOfMap pixMapOfMap;

	@Override
	public void create () {
		mapWidth = 1200;
		mapHeight = 1200;

		world = new World(new Vector2(0, 0f),true);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

		allBodiesInWorld = new Array<Body>();
		players = new Array<Player>();
		water = new Array<ModelInstance>();
		cannonBalls = new Array<CannonBall>();
		islands = new Array<Island>();
		npcships = new Array<NPCship>();
		debugInstances = new Array<ModelInstance>();
		pixMapOfMap = new PixMapOfMap();
		gameObjectManager = new GameObjectManager();

		bodycreator = BodyCreator.getInstance(world);
		GameAssetManager.getInstance().loadModels(world, pixMapOfMap);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.3f, 1f));
		environment.add(new DirectionalLight().set(0.9f, 0.9f, 0.7f, -0.2f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 200f, 0f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 500f;
		cam.update();


		GameAssetManager.getInstance().loadParticleEffects(cam);
		//effectsManager = new EffectsManager(GameAssetManager.getInstance().getParticleSystem());
		shader = new TestShader();
		shader.init();

		//separate 3D import function later
		ModelLoader modelLoader = new G3dModelLoader(new JsonReader());

		model = modelLoader.loadModel(Gdx.files.internal("playership.g3dj"));
		for (Material mat : model.materials) { mat.set(new IntAttribute(IntAttribute.CullFace, 0)); }
		playerInstance = new ModelInstance(model);
		playerInstance.transform.setTranslation(400,0,400);
		player = new Player(playerInstance, world, 300f, EffectsManager.getInstance(), GameAssetManager.getInstance());

		model = modelLoader.loadModel(Gdx.files.internal("Simple_Pirate_Ship_y_up_x_forw.g3dj"));
		nodeGraph = NodeMapGenerator.generateGraph(pixMapOfMap.getMap());

		for (int x =100 ; x<=1000 ; x+=500) {
			for (int z = 100; z <= 1000; z+=500) {
				NPCinstance = new ModelInstance(model);
				NPCinstance.transform.setTranslation(x, -3f, z);
				npcships.add(new NPCship(NPCinstance, world, 100f, player, EffectsManager.getInstance(), GameAssetManager.getInstance()));
			}
		}


		model = modelLoader.loadModel(Gdx.files.internal("flat_water_5.g3dj"));
		model.getMaterial("Material").set(new BlendingAttribute(1f));
		for (int x=100 ; x<=mapWidth ; x+=200) {
			for (int z = 100; z <=mapHeight ; z+=200) {
				water.add(new ModelInstance(model, x,0f,z));
			}

		}

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA().getBody().getUserData() instanceof DynamicGameObject
						&& contact.getFixtureB().getBody().getUserData() instanceof DynamicGameObject) {
					((DynamicGameObject) contact.getFixtureA().getBody().getUserData()).gotHit(1);
					((DynamicGameObject) contact.getFixtureB().getBody().getUserData()).gotHit(1);
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

		decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

	}

	@Override
	public void render () {
		debugInstances.clear();
		time += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);

		for (Body body:allBodiesInWorld) {
			if (body.getUserData() instanceof NPCship) {
				if (((NPCship) body.getUserData()).isDestroyed()) {
					npcships.removeValue(((NPCship) body.getUserData()), true);
					world.destroyBody(body);
				}
			}

			if (body.getUserData() instanceof CannonBall) {
				if (((CannonBall) body.getUserData()).isDestroyed()) {
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

			if (body.getUserData() instanceof CannonBall) ((CannonBall) body.getUserData()).update();

			if (body.getUserData() instanceof Island) ((Island) body.getUserData()).update();
		}

		EffectsManager.getInstance().update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cam.position.set(player.instance.transform.getTranslation(new Vector3()).add(0f, 140f, 130f));
		cam.lookAt(player.instance.transform.getTranslation(new Vector3()));
		cam.update();

		modelBatch.begin(cam);
		modelBatch.render(playerInstance, environment);
		for (GameObject island : GameAssetManager.getInstance().getGameObjects())
			modelBatch.render(island.instance, environment);
		for (CannonBall cannonBall : cannonBalls)
			modelBatch.render(cannonBall.instance, environment);
		for (NPCship npcship : npcships)
			modelBatch.render(npcship.instance,environment);
		/*for (ModelInstance instance : debugInstances)
			modelBatch.render(instance,environment);*/
		for (ModelInstance instance : water)
			modelBatch.render(instance,environment,shader);
		modelBatch.end();

		modelBatch.begin(cam);
		for (ChargingArrow arrow : player.getRenderableAccessories()){
			if (arrow.shouldRender()) {
				modelBatch.render(arrow.getInstance(),arrow.getArrowShader());
			}
		}
		GameAssetManager.getInstance().getParticleSystem().update();
		GameAssetManager.getInstance().getParticleSystem().begin();
		GameAssetManager.getInstance().getParticleSystem().draw();
		modelBatch.render(GameAssetManager.getInstance().getParticleSystem());
		modelBatch.end();
		GameAssetManager.getInstance().getParticleSystem().end();

		EffectsManager.getInstance().stopEmissionStationaryEffects();

		decalBatch.flush();

	}

	@Override
	public void dispose () {
		decalBatch.dispose();
		modelBatch.dispose();
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

	public static void shoot(Vector2 origin, Vector2 direction, Vector2 inertia, World world, float velocity, float size){
		//replace this with a pool set arguments in cannon and send in cannon as argument

		Model model;
		ModelBuilder modelBuilder;
		ModelInstance instance;
		modelBuilder = new ModelBuilder();

		float strength = size;

		model = modelBuilder.createSphere(2,2,2,20,20,
				new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		origin = new Vector2(origin.x,origin.y);

		direction = new Vector2(direction.x, direction.y);
		Vector2 scalar = inertia.nor();

		for (int i = 0 ; i <= size ; i++) {
			float offset = -1.25f*size + i*2.5f;
			Vector2 pos = new Vector2();
			pos.x = origin.x + scalar.x*offset;
			pos.y = origin.y + scalar.y*offset;

			instance = new ModelInstance(model);
			instance.transform.setTranslation(pos.x, 3, pos.y);

			PirateCatAI.cannonBalls.add(new CannonBall(instance, world, direction, inertia, velocity, strength));
		}

	}

	public static void addDebugInstance(Vector2 pos){
		Model model;
		ModelInstance modelInstance;
		ModelBuilder modelBuilder;
		modelBuilder = new ModelBuilder();
		model = modelBuilder.createSphere(4f,4f,4f,4,4,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		modelInstance = new ModelInstance(model,pos.x,0,pos.y);
		PirateCatAI.debugInstances.add(modelInstance);
	}

	public static DecalBatch getDecalBatch(){
		return decalBatch;
	}

	public static Camera getCam(){
		return cam;
	}

	public static int getMapHeight() {
		return mapHeight;
	}

	public static int getMapWidth() {
		return mapWidth;
	}

	public ArrowShader getArrowShader() {
		return arrowShader;
	}
}
