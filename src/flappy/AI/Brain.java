package flappy.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Brain {
    public List<Node> nodes;
    public List<Connection> connections;
    public int inputs;
    public int layers = 2;
    public List<Node> net;


    public Brain(int inputs) {
        this.inputs = inputs;
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();

        initializeNetwork();
    }

    private void initializeNetwork() {
        Random rand = new Random();

        // Tạo input nodes
        for (int i = 0; i < inputs; i++) {
            Node inputNode = new Node(i);
            inputNode.layer = 0;
            nodes.add(inputNode);
        }

        // Tạo bias node
        Node biasNode = new Node(3);
        biasNode.layer = 0;
        nodes.add(biasNode);

        // Tạo output node
        Node outputNode = new Node(4);
        outputNode.layer = 1;
        nodes.add(outputNode);

        // Tạo kết nối từ tất cả input + bias đến output
        for (int i = 0; i <= 4; i++) { // inputs + 1 (bias)
            Node from = nodes.get(i);
            Connection conn = new Connection(from, outputNode, rand.nextFloat() * 2 - 1); // [-1, 1]
            from.connections.add(conn);  // để node phát tín hiệu
            connections.add(conn);       // để theo dõi toàn mạng
        }

    }
    public void connectNodes() {
        // Bước 1: reset danh sách connections của từng node
        for (Node node : nodes) {
            node.connections.clear();
        }

        // Bước 2: gán các connection vào node xuất phát (fromNode)
        for (Connection conn : connections) {
            conn.fromNode.connections.add(conn);
        }
    }
    public void generateNet() {
        connectNodes();
        net = new ArrayList<>();

        for (int j = 0; j < layers; j++) {
            for (Node node : nodes) {
                if (node.layer == j) {
                    net.add(node);
                }
            }
        }
    }
    public float feedForward(float[] vision) {
        // 1. Gán đầu vào
        for (int i = 0; i < inputs; i++) {
            nodes.get(i).outputValue = vision[i];
        }

        // 2. Gán bias node
        nodes.get(3).outputValue = 1;

        // 3. Kích hoạt mạng theo net
        for (Node node : net) {
            node.activate();
        }

        // 4. Lấy output từ node ID 4 (output node)
        float outputValue = nodes.get(4).outputValue;

        // 5. Reset inputValue
        for (Node node : nodes) {
            node.inputValue = 0;
        }

        return outputValue;
    }



}
