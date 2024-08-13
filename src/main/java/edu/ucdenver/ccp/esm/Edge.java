
package edu.ucdenver.ccp.esm;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Edge {
    private Vertex governor;
    private Vertex dependent;
    private String label;
    private final Map<String, Object> properties;

    public Edge(Vertex governor, Vertex dependent, String label) {
        this.governor = governor;
        this.dependent = dependent;
        this.label = label;
        this.properties = new HashMap<>();
    }

    public Vertex getGovernor() {
        return governor;
    }

    public Vertex getDependent() {
        return dependent;
    }

    public String getLabel() {
        return label;
    }
    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(governor, edge.governor) &&
                Objects.equals(dependent, edge.dependent) &&
                Objects.equals(label, edge.label) &&
                Objects.equals(properties, edge.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(governor, dependent, label, properties);
    }

    @Override
    public String toString() {
        return "Edge{" + "governor=" + governor + ", dependent=" + dependent + ", label='" + label + '\'' + '}';
    }

    public double calculateCosineSimilarity(Edge other) {
        if (!label.equals(other.label)) {
            return 0.0;
        }
        return 1.0; // 若标签匹配 算1
    }
}
