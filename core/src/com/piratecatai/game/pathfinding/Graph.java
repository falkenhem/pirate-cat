package com.piratecatai.game.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.piratecatai.game.PirateCatAI;

public class Graph implements IndexedGraph<Node> {
    protected Array<Node> nodes;
    NodeHeuristic nodeHeuristic = new NodeHeuristic();

    Graph(Array<Node> nodes) {
        this.nodes = nodes;
    }

    public Node getNodeByCoordinates(Vector2 pos) {
        float distance;
        int index;

        index = 0;
        distance = nodes.get(0).getWorldPosition().dst2(pos);

        for (Node node : nodes){
            if (node.getConnections().size > 0){
                if (node.getWorldPosition().dst2(pos) <= distance){
                    distance = node.getWorldPosition().dst2(pos);
                    index = node.getIndex();
                }
            }

        }



        return nodes.get(index);
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    public LinePath findPath(Node startNode, Node goalNode){
        GraphPath<Node> nodePath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalNode, nodeHeuristic, nodePath);
        return graphPathToLinePath((DefaultGraphPath)nodePath);
    }

    private LinePath graphPathToLinePath(DefaultGraphPath graphPath){
        LinePath linePath;
        Array linePathPoints = new Array<>();

        Model model;
        ModelBuilder modelBuilder;
        ModelInstance instance;
        modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(4f,4f,4f,4,4,
                new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        for(int i = 0; i < graphPath.getCount(); i++){
            Node node = (Node)graphPath.get(i);
            linePathPoints.add(new Vector2(node.getX()* 20f + 10f,node.getY()* 20f + 10f));
            instance = new ModelInstance(model,new Vector3(node.getX()*20f + 10f,0,node.getY() * 20f + 10f));
            PirateCatAI.debugInstances.add(instance);
        }

        linePath = new LinePath(linePathPoints, true);

        return linePath;

    }
}
