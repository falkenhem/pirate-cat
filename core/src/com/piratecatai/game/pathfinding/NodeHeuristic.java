package com.piratecatai.game.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class NodeHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node goalNode) {
        return Vector2.dst(startNode.x, startNode.y, goalNode.x, goalNode.y);
    }
}
