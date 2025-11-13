package flappy.AI;

import java.util.Random;

public class Connection {
    public Node from;
    public Node to;
    public float weight;

    public Connection(Node from, Node to, float weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }


    public void mutateWeight() {
        Random rand = new Random();
        if (rand.nextFloat() < 0.1f) {
            // 10% cơ hội gán ngẫu nhiên hoàn toàn
            weight = rand.nextFloat() * 2 - 1; // [-1, 1]
        } else {
            // 90% còn lại: thay đổi nhẹ bằng Gaussian noise
            weight += rand.nextGaussian() / 10.0;
            // Giới hạn trong [-1, 1]
            if (weight > 1) weight = 1;
            if (weight < -1) weight = -1;
        }
    }


    // Tạo bản sao của kết nối
    public Connection cloneConnection(Node fromClone, Node toClone) {
        return new Connection(fromClone, toClone, this.weight);
    }
}
