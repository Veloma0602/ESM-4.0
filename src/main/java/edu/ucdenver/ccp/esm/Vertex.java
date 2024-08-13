package edu.ucdenver.ccp.esm;

import java.util.HashMap;
import java.util.Map;

public class Vertex {
    private String id;
    private Map<String, String> properties;
    private Map<String, Edge> edges;

    public Vertex(String id) {
        this.id = id;
        this.properties = new HashMap<>();
        this.edges = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void addEdge(Edge edge) {
        edges.put(edge.getLabel(), edge);
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    // 相似度余弦计算
    public double calculateCosineSimilarity(Vertex other) {
        Map<String, Integer> vector1 = getVectorRepresentation();
        Map<String, Integer> vector2 = other.getVectorRepresentation();

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String key : vector1.keySet()) {
            dotProduct += vector1.get(key) * vector2.getOrDefault(key, 0);
            norm1 += Math.pow(vector1.get(key), 2);
        }

        for (int value : vector2.values()) { // Change from String to Integer
            norm2 += Math.pow(value, 2);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0; // To avoid division by zero
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private Map<String, Integer> getVectorRepresentation() {
        Map<String, Integer> vector = new HashMap<>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            vector.put(entry.getKey() + "=" + entry.getValue(), 1);
        }
        return vector;
    }
}
