package edu.ucdenver.ccp.esm;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ESMTest {

    private DirectedGraph<Vertex, Edge> graph;
    private DirectedGraph<Vertex, Edge> subgraph;

    @BeforeEach
    public void setUp() {
        graph = new DirectedSparseMultigraph<>();
        subgraph = new DirectedSparseMultigraph<>();

        // Create vertices
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");

        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");

        // Set properties
        v1.setProperty("name", "A");
        v2.setProperty("name", "B");
        v3.setProperty("name", "C");
        v4.setProperty("name", "D");

        sv1.setProperty("name", "A");
        sv2.setProperty("name", "B");



        // Add vertices to graphs
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        subgraph.addVertex(sv1);
        subgraph.addVertex(sv2);

        // Create edges
        Edge e1 = new Edge(v1, v2, "connects");
        Edge e2 = new Edge(v2, v3, "connects");
        Edge e3 = new Edge(v3, v4, "connects");

        Edge se1 = new Edge(sv1, sv2, "connects");

        // Add edges to graphs
        graph.addEdge(e1, v1, v2);
        graph.addEdge(e2, v2, v3);
        graph.addEdge(e3, v3, v4);

        subgraph.addEdge(se1, sv1, sv2);

        // Add edges to vertexs
        v1.addEdge(e1);
        v2.addEdge(e2);
        v3.addEdge(e3);

        sv1.addEdge(se1);


    }

//    @Test
//    public void testSubgraphMatching() {
//        ESM esm = new ESM(subgraph, graph);
//
//        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();
//
//        System.out.println("TestMatches found:");
//        for (Map<Vertex, Vertex> match0 : matches) {
//            for (Map.Entry<Vertex, Vertex> entry : match0.entrySet()) {
//                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
//            }
//        }
//
//        assertEquals(1, matches.size(), "Expected one match");
//
//        Map<Vertex, Vertex> match = matches.get(0);
//
//        Collection<Vertex> subgraphVertices = subgraph.getVertices();
//        assertNotNull(subgraphVertices, "Subgraph vertices should not be null");
//        assertFalse(subgraphVertices.isEmpty(), "Subgraph should have vertices");
//
//        Vertex firstSubgraphVertex = subgraphVertices.iterator().next();
//        assertNotNull(firstSubgraphVertex, "First subgraph vertex should not be null");
//
//        Collection<Edge> firstSubgraphVertexEdges = (Collection<Edge>) firstSubgraphVertex.getEdges();
//        assertNotNull(firstSubgraphVertexEdges, "Edges of first subgraph vertex should not be null");
//        assertFalse(firstSubgraphVertexEdges.isEmpty(), "First subgraph vertex should have edges");
//
//        Vertex matchedSV1 = match.get(firstSubgraphVertex);
//        assertNotNull(matchedSV1, "Matched subgraph vertex should not be null");
//
//        Edge firstEdge = firstSubgraphVertexEdges.iterator().next();
//        assertNotNull(firstEdge, "First edge should not be null");
//
//        Vertex matchedSV2 = match.get(firstEdge.getDependent());
//        assertNotNull(matchedSV2, "Matched target vertex should not be null");
//
//
//
//        assertEquals("1", matchedSV1.getId());
//        assertEquals("2", matchedSV2.getId());
//
//
//    }

    @Test
    public void testSubgraphNoMatch() {
        // Change subgraph to something that doesn't match
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");
        sv1.setProperty("name", "X"); // This property does not match any vertex in the graph
        sv2.setProperty("name", "Y");

        subgraph = new DirectedSparseMultigraph<>();
        subgraph.addVertex(sv1);
        subgraph.addVertex(sv2);
        Edge se1 = new Edge(sv1, sv2, "connects");
        subgraph.addEdge(se1, sv1, sv2);

        sv1.addEdge(se1);


        ESM esm = new ESM(subgraph, graph,1);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        System.out.println("NoMatches found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
        }

        assertTrue(matches.isEmpty(), "Expected no matches");


    }

//    @Test
//    public void testEmptyGraph() {
//        DirectedGraph<Vertex, Edge> emptyGraph = new DirectedSparseMultigraph<>();
//        DirectedGraph<Vertex, Edge> subgraph = new DirectedSparseMultigraph<>();
//
//        ESM esm = new ESM(subgraph, emptyGraph);
//
//        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();
//
//        assertTrue(matches.isEmpty(), "Expected no matches for empty graph");
//    }

    @Test
    public void testSubgraphLargerThanGraph() {
        // Create a subgraph that is larger than the graph
        DirectedGraph<Vertex, Edge> graph = new DirectedSparseMultigraph<>();
        DirectedGraph<Vertex, Edge> largerSubgraph = new DirectedSparseMultigraph<>();

        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        graph.addVertex(v1);
        graph.addVertex(v2);

        Vertex lv1 = new Vertex("lv1");
        Vertex lv2 = new Vertex("lv2");
        Vertex lv3 = new Vertex("lv3");
        largerSubgraph.addVertex(lv1);
        largerSubgraph.addVertex(lv2);
        largerSubgraph.addVertex(lv3);

        Edge e1 = new Edge(v1, v2, "connects");
        graph.addEdge(e1, v1, v2);

        Edge le1 = new Edge(lv1, lv2, "connects");
        Edge le2 = new Edge(lv2, lv3, "connects");
        largerSubgraph.addEdge(le1, lv1, lv2);
        largerSubgraph.addEdge(le2, lv2, lv3);

        ESM esm = new ESM(largerSubgraph, graph,1);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        assertTrue(matches.isEmpty(), "Expected no matches when subgraph is larger than graph");
    }

    @Test
    public void testComplexSubgraphMatching() {
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
        Vertex v7 = new Vertex("7");
        Vertex v8 = new Vertex("8");

        // Create vertices for the subgraph
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");
        Vertex sv3 = new Vertex("s3");

        // Set properties
        v1.setProperty("name", "A");
        v2.setProperty("name", "B");
        v3.setProperty("name", "C");
        v4.setProperty("name", "D");
        v5.setProperty("name", "C");
        v6.setProperty("name", "B");
        v7.setProperty("name", "A");
        v8.setProperty("name", "H");

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
        complexGraph.addVertex(v7);
        complexGraph.addVertex(v8);

        complexSubgraph.addVertex(sv1);
        complexSubgraph.addVertex(sv2);
        complexSubgraph.addVertex(sv3);

        // Create edges for the graph with varied properties
        Edge e1 = new Edge(v1, v2, "connects");
        Edge e2 = new Edge(v2, v3, "connects");
        Edge e3 = new Edge(v3, v4, "connects");
        Edge e4 = new Edge(v4, v5, "connects");
        Edge e5 = new Edge(v5, v6, "connects");
        Edge e6 = new Edge(v6, v7, "connects");
        Edge e7 = new Edge(v7, v8, "connects");
        Edge e8 = new Edge(v8, v1, "connects");
        Edge e9 = new Edge(v2, v5, "bypasses");
        Edge e10 = new Edge(v3, v6, "bypasses");

        // Create edges for the subgraph with varied properties
        Edge se1 = new Edge(sv1, sv2, "connects");
        Edge se2 = new Edge(sv2, sv3, "bypasses");

        // Add edges to graphs
        complexGraph.addEdge(e1, v1, v2);
        complexGraph.addEdge(e2, v2, v3);
        complexGraph.addEdge(e3, v3, v4);
        complexGraph.addEdge(e4, v4, v5);
        complexGraph.addEdge(e5, v5, v6);
        complexGraph.addEdge(e6, v6, v7);
        complexGraph.addEdge(e7, v7, v8);
        complexGraph.addEdge(e8, v8, v1);
        complexGraph.addEdge(e9, v2, v5);
        complexGraph.addEdge(e10, v3, v6);

        complexSubgraph.addEdge(se1, sv1, sv2);
        complexSubgraph.addEdge(se2, sv2, sv3);

        // Add edges to vertexs
        v1.addEdge(e1);
        v2.addEdge(e2);
        v2.addEdge(e9);
        v3.addEdge(e3);
        v3.addEdge(e10);
        v4.addEdge(e4);
        v5.addEdge(e5);
        v6.addEdge(e6);
        v7.addEdge(e7);
        v8.addEdge(e8);

        sv1.addEdge(se1);
        sv2.addEdge(se2);

        ESM esm = new ESM(complexSubgraph, complexGraph,1);

        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        // Output the matched results
        System.out.println("ComplexMatches found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
        }

        // Manually verify the matches
        for (Map<Vertex, Vertex> match : matches) {
            // Add assertions to verify the match is correct
            assertTrue(match.size() >= 2, "Match should have at least 2 vertices");
        }

        // Ensure the number of matches is as expected
        assertEquals(1, matches.size());
    }

    @Test
    public void testComplexSubgraphMatchingMultipleResults() {
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

        ESM esm = new ESM(complexSubgraph, complexGraph,1);

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
        assertEquals(2, matches.size()); // 假设有3个匹配
    }

    @Test
    public void testSubgraphMatchingWithNeo4j() {
        // Connect to Neo4j and get data
        Neo4jConnector neo4jConnector = new Neo4jConnector();
        List<Vertex> vertices = neo4jConnector.getVertices();
        List<Edge> edges = neo4jConnector.getEdges(vertices);
        neo4jConnector.close();

        // Create the graph from Neo4j data
        DirectedGraph<Vertex, Edge> graph = new DirectedSparseMultigraph<>();
        for (Vertex vertex : vertices) {
            graph.addVertex(vertex);
        }
        for (Edge edge : edges) {
            graph.addEdge(edge, edge.getGovernor(), edge.getDependent());
        }

        // Create a subgraph manually for testing
        DirectedGraph<Vertex, Edge> subgraph = new DirectedSparseMultigraph<>();
        Vertex sv1 = new Vertex("s1");
        Vertex sv2 = new Vertex("s2");
        Vertex sv3 = new Vertex("s3");

        sv1.setProperty("name", "A");
        sv2.setProperty("name", "B");
        sv3.setProperty("name", "C");

        subgraph.addVertex(sv1);
        subgraph.addVertex(sv2);
        subgraph.addVertex(sv3);

        Edge se1 = new Edge(sv1, sv2, "connects");
        Edge se2 = new Edge(sv2, sv3, "connects");

        subgraph.addEdge(se1, sv1, sv2);
        subgraph.addEdge(se2, sv2, sv3);

        // Perform subgraph matching
        ESM esm = new ESM(subgraph, graph,1);
        List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();

        // Output the matched results
        System.out.println("Neo4jMatches found:");
        for (Map<Vertex, Vertex> match : matches) {
            for (Map.Entry<Vertex, Vertex> entry : match.entrySet()) {
                System.out.println("Subgraph Vertex: " + entry.getKey().getId() + " -> Graph Vertex: " + entry.getValue().getId());
            }
            System.out.println("---");
        }

        // Add assertions to verify the matches
        for (Map<Vertex, Vertex> match : matches) {
            assertTrue(match.size() >= 2, "Match should have at least 2 vertices");
        }
    }

}
