package edu.ucdenver.ccp.esm;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author fwt
 * @since  2024/7/29
 */

public class Neo4jConnector {
    private Driver driver;

    public Neo4jConnector() {
        Properties props = new Properties();
        try {
            // Assuming the config file is in src/main/resources/
            props.load(new FileInputStream("src/main/resources/Neo4jConfig.properties"));
            String uri = props.getProperty("neo4j.uri");
            String username = props.getProperty("neo4j.username");
            String password = props.getProperty("neo4j.password");
            driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error scenario here, could throw a runtime exception or log error
        }
    }

    public void close() {
        driver.close();
    }

    public List<Vertex> getVertices() {
        List<Vertex> vertices = new ArrayList<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n) RETURN n");
            while (result.hasNext()) {
                Record record = result.next();
                org.neo4j.driver.types.Node node = record.get("n").asNode();
                Vertex vertex = new Vertex(String.valueOf(node.id()));
                node.labels().forEach(label -> vertex.setProperty("label", label));
                node.asMap().forEach((key, value) -> vertex.setProperty(key, value.toString()));
                vertices.add(vertex);
            }
        }
        return vertices;
    }

    public List<Edge> getEdges(List<Vertex> vertices) {
        List<Edge> edges = new ArrayList<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n)-[r]->(m) RETURN n, r, m");
            while (result.hasNext()) {
                Record record = result.next();
                org.neo4j.driver.types.Node startNode = record.get("n").asNode();
                org.neo4j.driver.types.Node endNode = record.get("m").asNode();
                org.neo4j.driver.types.Relationship relationship = record.get("r").asRelationship();
                Vertex source = findVertexById(vertices, String.valueOf(startNode.id()));
                Vertex target = findVertexById(vertices, String.valueOf(endNode.id()));
                Edge edge = new Edge(source, target, relationship.type());
                relationship.asMap().forEach((key, value) -> edge.setProperty(key, value.toString()));
                edges.add(edge);
            }
        }
        return edges;
    }

    private Vertex findVertexById(List<Vertex> vertices, String id) {
        return vertices.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }
}

