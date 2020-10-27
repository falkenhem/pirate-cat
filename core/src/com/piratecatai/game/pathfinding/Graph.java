package com.piratecatai.game.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class Graph implements IndexedGraph<Node> {
    private Array<Node> nodes;
    NodeHeuristic nodeHeuristic = new NodeHeuristic();

    Graph(Array<Node> nodes) {
        this.nodes = nodes;
    }

    public Node getNodeByCoordinates(float x, float y) {
        return nodes.get(1);
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

    public GraphPath<Node> findPath(Node startNode, Node goalNode){
        GraphPath<Node> nodePath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalNode, nodeHeuristic, nodePath);
        return nodePath;
    }
}
