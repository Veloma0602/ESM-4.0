package edu.ucdenver.ccp.esm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.uci.ics.jung.graph.DirectedGraph;

public class ESM {
	DirectedGraph<Vertex, Edge> subgraph = null;
	DirectedGraph<Vertex, Edge> graph = null;
	private Vertex subgraphStartNode = null;
	private List<Vertex> graphStartNodes = null;
	private double similarityThreshold = 0.8; // Define a threshold for similarity

	public ESM(DirectedGraph<Vertex, Edge> subgraph, DirectedGraph<Vertex, Edge> graph, double similarityThreshold) {
		if (subgraph == null || subgraph.getVertexCount() == 0) {
			throw new IllegalArgumentException("Subgraph cannot be null or empty.");
		}
		if (graph == null || graph.getVertexCount() == 0) {
			throw new IllegalArgumentException("Graph cannot be null or empty.");
		}

		this.graph = graph;
		this.subgraph = subgraph;
		this.similarityThreshold = similarityThreshold;
		subgraphStartNode = getRandomStartNode(new ArrayList<>(subgraph.getVertices()));
		graphStartNodes = new ArrayList<>(graph.getVertices());
	}

	private Vertex getRandomStartNode(List<Vertex> subgraphNodes) {
		if (subgraphNodes.isEmpty()) {
			throw new IllegalArgumentException("subgraphNodes list cannot be empty.");
		}
		Random random = new Random();
		int randomNumber = random.nextInt(subgraphNodes.size());
		return subgraphNodes.get(randomNumber);
	}

	public void setSubgraphStartNode(Vertex vertex) {
		subgraphStartNode = vertex;
	}

	public void setGraphStartNodes(List<Vertex> vertices) {
		graphStartNodes = vertices;
	}

	public List<Map<Vertex, Vertex>> getSubgraphMatchingMatches() {
		List<Map<Vertex, Vertex>> matches = new ArrayList<>();
		if (!isSubgraphSmaller()) {
			System.err.println("The size of the subgraph: " +
					subgraph.getVertexCount() + " is bigger than the size of the graph " +
					graph.getVertexCount() + ". Please check.");
			return matches;
		}

		for (Vertex graphStartNode : graphStartNodes) {
			Map<Vertex, Vertex> subgraphToGraph = new HashMap<>();
			Map<Vertex, Vertex> graphToSubgraph = new HashMap<>();
			List<Map<Vertex, Vertex>> total = new ArrayList<>();
			List<Vertex> toMatch = Arrays.asList(subgraphStartNode, graphStartNode);

			if (matchNodeForAllMatches(toMatch, subgraphToGraph, graphToSubgraph, subgraph, graph, total)) {
				for (Map<Vertex, Vertex> m : total) {
					boolean flag = true;
					for (Map<Vertex, Vertex> n : matches) {
						if (equalMaps(m, n))
							flag = false;
					}
					if (flag) matches.add(m);
				}
			}
		}
		return matches;
	}

	private boolean equalMaps(Map<Vertex, Vertex> m1, Map<Vertex, Vertex> m2) {
		if (m1.size() != m2.size())
			return false;
		for (Vertex key : m1.keySet())
			if (!m1.get(key).equals(m2.get(key)))
				return false;
		return true;
	}

	private boolean matchNodeForAllMatches(List<Vertex> toMatchs, Map<Vertex, Vertex> subgraphToGraphs, Map<Vertex, Vertex> graphToSubgraphs,
										   DirectedGraph<Vertex, Edge> subgraph, DirectedGraph<Vertex, Edge> graph, List<Map<Vertex, Vertex>> total) {
		List<Vertex> toMatch = new ArrayList<>(toMatchs);
		Map<Vertex, Vertex> subgraphToGraph = new HashMap<>(subgraphToGraphs);
		Map<Vertex, Vertex> graphToSubgraph = new HashMap<>(graphToSubgraphs);

		while (!toMatch.isEmpty()) {
			Vertex noder = toMatch.remove(0);
			Vertex nodes = toMatch.remove(0);

			if (subgraphToGraph.containsKey(noder) && !graphToSubgraph.containsKey(nodes))
				return false;
			if (!subgraphToGraph.containsKey(noder) && graphToSubgraph.containsKey(nodes))
				return false;
			if (subgraphToGraph.containsKey(noder) && graphToSubgraph.containsKey(nodes)) {
				if (subgraphToGraph.get(noder).equals(nodes) && graphToSubgraph.get(nodes).equals(noder)) {
					// Do nothing
				} else {
					return false;
				}
			}

			if (!matchNodeContent(noder, nodes))
				return false;

			subgraphToGraph.put(noder, nodes);
			graphToSubgraph.put(nodes, noder);

			for (Edge e : subgraph.getOutEdges(noder)) {
				Vertex r = e.getDependent();
				List<Vertex> candidateNodes = new ArrayList<>();
				for (Edge s : graph.getOutEdges(nodes)) {
					if (e.calculateCosineSimilarity(s) >= similarityThreshold)
						candidateNodes.add(s.getDependent());
				}
				boolean flag = false;
				boolean terminate = false;
				for (Vertex s : candidateNodes) {
					if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s))
						continue;
					if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s))
						continue;
					if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
						if (subgraphToGraph.get(r).equals(s) && graphToSubgraph.get(s).equals(r)) {
							terminate = true;
							break;
						} else {
							continue;
						}
					}
					List<Vertex> toMatchTemp = new ArrayList<>(toMatch);
					toMatchTemp.add(noder);
					toMatchTemp.add(nodes);
					toMatchTemp.add(r);
					toMatchTemp.add(s);
					if (matchNodeForAllMatches(toMatchTemp, subgraphToGraph, graphToSubgraph, subgraph, graph, total))
						flag = true;
				}
				if (terminate) continue;
				if (flag) return true;

				return false;
			}

			for (Edge e : subgraph.getInEdges(noder)) {
				Vertex r = e.getGovernor();
				List<Vertex> candidateNodes = new ArrayList<>();
				for (Edge s : graph.getInEdges(nodes)) {
					if (e.calculateCosineSimilarity(s) >= similarityThreshold)
						candidateNodes.add(s.getGovernor());
				}
				boolean flag = false;
				boolean terminate = false;
				for (Vertex s : candidateNodes) {
					if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s))
						continue;
					if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s))
						continue;
					if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
						if (subgraphToGraph.get(r).equals(s) && graphToSubgraph.get(s).equals(r)) {
							terminate = true;
							break;
						} else {
							continue;
						}
					}
					List<Vertex> toMatchTemp = new ArrayList<>(toMatch);
					toMatchTemp.add(noder);
					toMatchTemp.add(nodes);
					toMatchTemp.add(r);
					toMatchTemp.add(s);
					if (matchNodeForAllMatches(toMatchTemp, subgraphToGraph, graphToSubgraph, subgraph, graph, total))
						flag = true;
				}
				if (terminate) continue;
				if (flag) return true;

				return false;
			}
		}

		total.add(subgraphToGraph);
		return true;
	}

	public boolean isSubgraphIsomorphism() {
		boolean isSubgraphIsomorphism = false;
		if (!isSubgraphSmaller()) {
			System.err.println("The size of the subgraph: " +
					subgraph.getVertexCount() + " is bigger the size of the graph " +
					graph.getVertexCount() + ". Please check.");
			return isSubgraphIsomorphism;
		}
		for (int i = 0; i < graphStartNodes.size(); i++) {
			Map<Vertex, Vertex> subgraphToGraph = new HashMap<>();
			Map<Vertex, Vertex> graphToSubgraph = new HashMap<>();
			List<Vertex> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));

			if (matchNodeForSingleMatch(toMatch, subgraphToGraph, graphToSubgraph, subgraph, graph)) {
				isSubgraphIsomorphism = true;
				break;
			}
		}
		return isSubgraphIsomorphism;
	}

	public boolean isGraphIsomorphism() {
		boolean isGraphIsomorphism = false;
		boolean isSubgraphIsomorphicToGraph = false;
		boolean isgraphIsomorphicToSubgraph = false;

		if (!isGraphSizeSame()) {
			System.err.println("The size of the subgraph: " +
					subgraph.getVertexCount() + " is not same as the size of the graph " +
					graph.getVertexCount() + ". Please check.");
			return isGraphIsomorphism;
		}
		for (int i = 0; i < graphStartNodes.size(); i++) {
			Map<Vertex, Vertex> subgraphToGraph = new HashMap<>();
			Map<Vertex, Vertex> graphToSubgraph = new HashMap<>();
			List<Vertex> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));
			if (matchNodeForSingleMatch(toMatch, subgraphToGraph, graphToSubgraph, subgraph, graph)) {
				isSubgraphIsomorphicToGraph = true;
				break;
			}
		}

		subgraphStartNode = getRandomStartNode(new ArrayList<>(graph.getVertices()));
		graphStartNodes = new ArrayList<>(subgraph.getVertices());
		for (int i = 0; i < graphStartNodes.size(); i++) {
			Map<Vertex, Vertex> subgraphToGraph = new HashMap<>();
			Map<Vertex, Vertex> graphToSubgraph = new HashMap<>();

			List<Vertex> toMatch = Arrays.asList(subgraphStartNode, graphStartNodes.get(i));
			if (matchNodeForSingleMatch(toMatch, subgraphToGraph, graphToSubgraph, graph, subgraph)) {
				isgraphIsomorphicToSubgraph = true;
				break;
			}
		}

		if (isSubgraphIsomorphicToGraph && isgraphIsomorphicToSubgraph)
			isGraphIsomorphism = true;

		subgraphStartNode = getRandomStartNode(new ArrayList<>(subgraph.getVertices()));
		graphStartNodes = new ArrayList<>(graph.getVertices());

		return isGraphIsomorphism;
	}

	private boolean matchNodeForSingleMatch(List<Vertex> toMatchs, Map<Vertex, Vertex> subgraphToGraphs, Map<Vertex, Vertex> graphToSubgraphs,
											DirectedGraph<Vertex, Edge> subgraph, DirectedGraph<Vertex, Edge> graph) {
		List<Vertex> toMatch = new ArrayList<>(toMatchs);
		Map<Vertex, Vertex> subgraphToGraph = new HashMap<>(subgraphToGraphs);
		Map<Vertex, Vertex> graphToSubgraph = new HashMap<>(graphToSubgraphs);

		while (!toMatch.isEmpty()) {
			Vertex noder = toMatch.remove(0);
			Vertex nodes = toMatch.remove(0);

			if (subgraphToGraph.containsKey(noder) && !graphToSubgraph.containsKey(nodes))
				return false;
			if (!subgraphToGraph.containsKey(noder) && graphToSubgraph.containsKey(nodes))
				return false;
			if (subgraphToGraph.containsKey(noder) && graphToSubgraph.containsKey(nodes)) {
				if (subgraphToGraph.get(noder).equals(nodes) && graphToSubgraph.get(nodes).equals(noder)) {
					// Do nothing
				} else {
					return false;
				}
			}

			if (!matchNodeContent(noder, nodes))
				return false;

			subgraphToGraph.put(noder, nodes);
			graphToSubgraph.put(nodes, noder);

			for (Edge e : subgraph.getOutEdges(noder)) {
				Vertex r = e.getDependent();
				List<Vertex> candidateNodes = new ArrayList<>();
				for (Edge s : graph.getOutEdges(nodes)) {
					if (e.calculateCosineSimilarity(s) >= similarityThreshold)
						candidateNodes.add(s.getDependent());
				}
				boolean terminate = false;
				for (Vertex s : candidateNodes) {
					if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s))
						continue;
					if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s))
						continue;
					if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
						if (subgraphToGraph.get(r).equals(s) && graphToSubgraph.get(s).equals(r)) {
							terminate = true;
							break;
						} else {
							continue;
						}
					}
					List<Vertex> toMatchTemp = new ArrayList<>(toMatch);
					toMatchTemp.add(noder);
					toMatchTemp.add(nodes);
					toMatchTemp.add(r);
					toMatchTemp.add(s);
					if (matchNodeForSingleMatch(toMatchTemp, subgraphToGraph, graphToSubgraph, subgraph, graph)) {
						subgraphToGraphs.putAll(subgraphToGraph);
						graphToSubgraphs.putAll(graphToSubgraph);
						return true;
					}
				}
				if (terminate) continue;

				return false;
			}

			for (Edge e : subgraph.getInEdges(noder)) {
				Vertex r = e.getGovernor();
				List<Vertex> candidateNodes = new ArrayList<>();
				for (Edge s : graph.getInEdges(nodes)) {
					if (e.calculateCosineSimilarity(s) >= similarityThreshold)
						candidateNodes.add(s.getGovernor());
				}
				boolean terminate = false;
				for (Vertex s : candidateNodes) {
					if (subgraphToGraph.containsKey(r) && !graphToSubgraph.containsKey(s))
						continue;
					if (!subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s))
						continue;
					if (subgraphToGraph.containsKey(r) && graphToSubgraph.containsKey(s)) {
						if (subgraphToGraph.get(r).equals(s) && graphToSubgraph.get(s).equals(r)) {
							terminate = true;
							break;
						} else {
							continue;
						}
					}
					List<Vertex> toMatchTemp = new ArrayList<>(toMatch);
					toMatchTemp.add(noder);
					toMatchTemp.add(nodes);
					toMatchTemp.add(r);
					toMatchTemp.add(s);
					if (matchNodeForSingleMatch(toMatchTemp, subgraphToGraph, graphToSubgraph, subgraph, graph)) {
						subgraphToGraphs.putAll(subgraphToGraph);
						graphToSubgraphs.putAll(graphToSubgraph);
						return true;
					}
				}
				if (terminate) continue;

				return false;
			}
		}

		subgraphToGraphs.putAll(subgraphToGraph);
		graphToSubgraphs.putAll(graphToSubgraph);
		return true;
	}

	private boolean matchNodeContent(Vertex noder, Vertex nodes) {
		return noder.calculateCosineSimilarity(nodes) >= similarityThreshold;
	}

	private Boolean isSubgraphSmaller() {
		return subgraph.getVertexCount() <= graph.getVertexCount();
	}

	private Boolean isGraphSizeSame() {
		return subgraph.getVertexCount() == graph.getVertexCount();
	}
}
