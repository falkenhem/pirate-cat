package com.piratecatai.game.pathfinding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.piratecatai.game.PirateCatAI;

public class NodeMapGenerator {
    public static Array<Node> nodes;

    public static Graph generateGraph(Pixmap pixmap){
        nodes = createArray();
        addConnections(pixmap, nodes);
        return new Graph(nodes);
    }

    private static Array<Node> createArray() {
        float xWorld;
        float yWorld;
        Model model;
        ModelBuilder modelBuilder;
        ModelInstance instance;
        modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(4f,4f,4f,4,4,
                new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Array<Node> nodes = new Array<Node>();
        int index = 0;
        for (int y = 0; y < 60; y++) {
            yWorld = y * 20 + 10;
            for (int x = 0; x < 60; x++) {
                nodes.add(new Node(x, y, index++));
                xWorld = x * 20 + 10;
            }
        }
        return nodes;
    }

    private static void addConnections(Pixmap pixmap, Array<Node> nodes) {

        for (int y = 0; y < 60; y++) {
            for (int x = 0; x < 60; x++) {
                if (pixmap.getPixel(20 * x + 10,20 * y + 10) < 1) {
                    Node currentNode = nodes.get(x + 60 * y);
                    if (pixmap.getPixel(nodeToPixmap(x), nodeToPixmap(y) + 20) < 1 && y != 60 - 1) {// N
                        currentNode.addConnection(nodes.get(x + 60 * (y + 1)));
                        //drawConnection(currentNode, nodes.get(x + 60 * (y + 1)));
                    }
                    if (pixmap.getPixel(nodeToPixmap(x) + 20, nodeToPixmap(y)) < 1 && x != 0) {// W
                        currentNode.addConnection(nodes.get((x - 1) + 60 * y));
                        //drawConnection(currentNode, nodes.get((x - 1) + 60 * y));
                    }
                    if (pixmap.getPixel(nodeToPixmap(x), nodeToPixmap(y) + 20) < 1 && y != 0) {// S
                        currentNode.addConnection(nodes.get(x + 60 * (y - 1)));
                        //drawConnection(currentNode, nodes.get(x + 60 * (y - 1)));
                        }
                    if (pixmap.getPixel(nodeToPixmap(x) + 20, nodeToPixmap(y)) < 1 && x != 60 - 1) {// E
                        currentNode.addConnection(nodes.get((x + 1) + 60 * y));
                        //drawConnection(currentNode, nodes.get((x + 1) + 60 * y));
                    }
                }
            }
        }
    }

    private static int nodeToPixmap(int coord){
        coord = 20 * coord + 10;

        return coord;
    }

    private static void drawConnection(Node node1, Node node2){
        float xWorld;
        float yWorld;

        xWorld = (node1.getX() + node2.getX())*10f;
        yWorld = (node1.getY() + node2.getY())*10f;
        Model model;
        ModelBuilder modelBuilder;
        modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(2f,2f,2f,4,4,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }
}
