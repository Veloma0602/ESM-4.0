package edu.ucdenver.ccp.esm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;



import java.util.*;



public class VertexTest {

    @Test
    public void testCosineSimilarityIdenticalVertices() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");

        v1.setProperty("name", "A");
        v1.setProperty("type", "B");
        v1.setProperty("value", "C");

        v2.setProperty("name", "A");
        v2.setProperty("type", "B");
        v2.setProperty("value", "C");

        assertEquals(1.0, v1.calculateCosineSimilarity(v2), 0.001);
    }

    @Test
    public void testCosineSimilarityCompletelyDifferentVertices() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");

        v1.setProperty("name", "A");
        v1.setProperty("type", "B");

        v2.setProperty("name", "X");
        v2.setProperty("type", "Y");

        assertEquals(0.0, v1.calculateCosineSimilarity(v2), 0.001);
    }

    @Test
    public void testCosineSimilarityPartiallySimilarVertices() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");

        v1.setProperty("name", "A");
        v1.setProperty("type", "B");

        v2.setProperty("name", "A");
        v2.setProperty("type", "Y");

        assertTrue(v1.calculateCosineSimilarity(v2) > 0.0);
        assertTrue(v1.calculateCosineSimilarity(v2) < 1.0);
    }

    @Test
    public void testCosineSimilarityEmptyVertices() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");

        assertEquals(0.0, v1.calculateCosineSimilarity(v2), 0.001);
    }

    @Test
    public void testCosineSimilaritySingleProperty() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");

        v1.setProperty("name", "A");
        v2.setProperty("name", "A");

        assertEquals(1.0, v1.calculateCosineSimilarity(v2), 0.001);
    }

    @Test
    public void testComplexSubgraphMatchingWithSimilarity() {
        // Create a more complex graph and subgraph
        DirectedGraph<Vertex, Edge> complexGraph = new DirectedSparseMultigraph<>();
        DirectedGraph<Vertex, Edge> complexSubgraph = new DirectedSparseMultigraph<>();

        // Create vertices for the graph
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");
        Vertex v5 = new Vertex("5");
        Vertex v6 = new Vertex("6");

        // Create vertices for the subgraph
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");
        Vertex sv3 = new Vertex("s3");

        // Set properties
        v1.setProperty("name", "A");
        v2.setProperty("name", "B");
        v3.setProperty("name", "C");
        v4.setProperty("name", "B");
        v5.setProperty("name", "C");
        v6.setProperty("name", "D");

        sv1.setProperty("name", "A");
        sv2.setProperty("name", "B");
        sv3.setProperty("name", "C");

        // Add vertices to graphs
        complexGraph.addVertex(v1);
        complexGraph.addVertex(v2);
        complexGraph.addVertex(v3);
        complexGraph.addVertex(v4);
        complexGraph.addVertex(v5);
        complexGraph.addVertex(v6);

        complexSubgraph.addVertex(sv1);
        complexSubgraph.addVertex(sv2);
        complexSubgraph.addVertex(sv3);

        // Create edges for the graph with varied types
        Edge e1 = new Edge(v1, v2, "connects");
        Edge e2 = new Edge(v2, v3, "connects");
        Edge e3 = new Edge(v3, v4, "connects");
        Edge e4 = new Edge(v1, v4, "connects");
        Edge e5 = new Edge(v5, v1, "connects");
        Edge e6 = new Edge(v4, v5, "connects");
        Edge e7 = new Edge(v5, v6, "connects");
        Edge e8 = new Edge(v3, v1, "connects");
        Edge e9 = new Edge(v6, v4, "connects");

        // Create edges for the subgraph with varied types
        Edge se1 = new Edge(sv1, sv2, "connects");
        Edge se2 = new Edge(sv2, sv3, "connects");
        Edge se3 = new Edge(sv3, sv1, "connects");

        // Add edges to graphs
        complexGraph.addEdge(e1, v1, v2);
        complexGraph.addEdge(e2, v2, v3);
        complexGraph.addEdge(e3, v3, v4);
        complexGraph.addEdge(e4, v1, v4);
        complexGraph.addEdge(e5, v5, v1);
        complexGraph.addEdge(e6, v4, v5);
        complexGraph.addEdge(e7, v5, v6);
        complexGraph.addEdge(e8, v3, v1);
        complexGraph.addEdge(e9, v6, v4);

        complexSubgraph.addEdge(se1, sv1, sv2);
        complexSubgraph.addEdge(se2, sv2, sv3);
        complexSubgraph.addEdge(se3, sv3, sv1);

        // Add edges to vertexs
        v1.addEdge(e1);
        v1.addEdge(e4);
        v2.addEdge(e2);
        v3.addEdge(e3);
        v5.addEdge(e5);
        v4.addEdge(e6);
        v5.addEdge(e7);
        v3.addEdge(e8);
        v6.addEdge(e9);

        sv1.addEdge(se1);
        sv2.addEdge(se2);
        sv3.addEdge(se3);

        // Set similarity threshold
        double similarityThreshold = 0.8;

        ESM esm = new ESM(complexSubgraph, complexGraph, similarityThreshold);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        // Output the matched results
        System.out.println("MultipleResults found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
            System.out.println("---");
        }

        // Manually verify the matches
        for (Map<Vertex, Vertex> match : matches) {
            // Add assertions to verify the match is correct
            assertTrue(match.size() >= 2, "Match should have at least 2 vertices");
        }

        // Ensure the number of matches is as expected
        assertEquals(2, matches.size()); // 假设有2个匹配
    }

    @Test
    public void testMatchingAtThreshold() {
        // Create a more complex graph and subgraph
        DirectedGraph<Vertex, Edge> complexGraph = new DirectedSparseMultigraph<>();
        DirectedGraph<Vertex, Edge> complexSubgraph = new DirectedSparseMultigraph<>();

        // Create vertices for the graph
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");

        // Create vertices for the subgraph
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");

        // Set properties
        v1.setProperty("name", "节点A");
        v2.setProperty("name", "节点B");
        v3.setProperty("name", "节点C");
        v4.setProperty("name", "节点D");

        sv1.setProperty("name", "节点A");
        sv2.setProperty("name", "节点B");

        // Add vertices to graphs
        complexGraph.addVertex(v1);
        complexGraph.addVertex(v2);
        complexGraph.addVertex(v3);
        complexGraph.addVertex(v4);

        complexSubgraph.addVertex(sv1);
        complexSubgraph.addVertex(sv2);

        // Create edges for the graph with varied types
        Edge e1 = new Edge(v1, v2, "连接");
        Edge e2 = new Edge(v2, v3, "连接");
        Edge e3 = new Edge(v3, v4, "连接");

        // Create edges for the subgraph with varied types
        Edge se1 = new Edge(sv1, sv2, "连接");

        // Add edges to graphs
        complexGraph.addEdge(e1, v1, v2);
        complexGraph.addEdge(e2, v2, v3);
        complexGraph.addEdge(e3, v3, v4);

        complexSubgraph.addEdge(se1, sv1, sv2);

        // Add edges to vertices
        v1.addEdge(e1);
        v2.addEdge(e2);
        v3.addEdge(e3);

        sv1.addEdge(se1);
        sv2.addEdge(se1);

        // Set similarity threshold
        double similarityThreshold = 0.8;

        ESM esm = new ESM(complexSubgraph, complexGraph, similarityThreshold);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        // Output the matched results
        System.out.println("MatchingAtThreshold found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
            System.out.println("---");
        }

        // Manually verify the matches
        for (Map<Vertex, Vertex> match : matches) {
            // Add assertions to verify the match is correct
            assertTrue(match.size() >= 2, "Match should have at least 2 vertices");
        }

        // Ensure the number of matches is as expected
        assertEquals(1, matches.size()); // 假设有1个匹配
    }

    @Test
    public void testMatchingBelowThreshold() {
        // Create a more complex graph and subgraph
        DirectedGraph<Vertex, Edge> complexGraph = new DirectedSparseMultigraph<>();
        DirectedGraph<Vertex, Edge> complexSubgraph = new DirectedSparseMultigraph<>();

        // Create vertices for the graph
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");

        // Create vertices for the subgraph
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");

        // Set properties
        v1.setProperty("name", "节点A");
        v2.setProperty("name", "节点B");
        v3.setProperty("name", "节点C");
        v4.setProperty("name", "节点D");

        sv1.setProperty("name", "节点E");
        sv2.setProperty("name", "节点F");

        // Add vertices to graphs
        complexGraph.addVertex(v1);
        complexGraph.addVertex(v2);
        complexGraph.addVertex(v3);
        complexGraph.addVertex(v4);

        complexSubgraph.addVertex(sv1);
        complexSubgraph.addVertex(sv2);

        // Create edges for the graph with varied types
        Edge e1 = new Edge(v1, v2, "连接");
        Edge e2 = new Edge(v2, v3, "连接");
        Edge e3 = new Edge(v3, v4, "连接");

        // Create edges for the subgraph with varied types
        Edge se1 = new Edge(sv1, sv2, "连接");

        // Add edges to graphs
        complexGraph.addEdge(e1, v1, v2);
        complexGraph.addEdge(e2, v2, v3);
        complexGraph.addEdge(e3, v3, v4);

        complexSubgraph.addEdge(se1, sv1, sv2);

        // Add edges to vertices
        v1.addEdge(e1);
        v2.addEdge(e2);
        v3.addEdge(e3);

        sv1.addEdge(se1);
        sv2.addEdge(se1);

        // Set similarity threshold
        double similarityThreshold = 0.8;

        ESM esm = new ESM(complexSubgraph, complexGraph, similarityThreshold);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        // Output the matched results
        System.out.println("MatchingBelowThreshold found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
            System.out.println("---");
        }

        // Ensure no matches are found
        assertEquals(0, matches.size()); // 假设没有匹配
    }
}
