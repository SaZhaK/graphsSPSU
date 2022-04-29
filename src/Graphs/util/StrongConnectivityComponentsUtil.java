package Graphs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StrongConnectivityComponentsUtil {
	private static Map<Integer, Boolean> visited;

	public static List<List<Integer>> getSCComponents(Map<Integer, List<Integer>> graph) {
		int V = graph.size();

		visited = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			visited.put(vertex, false);
		}

		List<Integer> order = fillOrder(graph);
		Map<Integer, List<Integer>> reverseGraph = getTranspose(graph);

		visited = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			visited.put(vertex, false);
		}

		Collections.reverse(order);

		List<List<Integer>> SCComp = new ArrayList<>();
		for (int v : order) {
			if (!visited.get(v)) {
				List<Integer> comp = new ArrayList<>();
				DFS(reverseGraph, v, comp);
				SCComp.add(comp);
			}
		}
		return SCComp;
	}

	public static Map<Integer, Set<Integer>> buildMetaGraph(Map<Integer, List<Integer>> graph, List<List<Integer>> scComponents) {
		Map<Integer, Integer> extended = new HashMap<>();
		for (int i = 0; i < scComponents.size(); i++) {
			List<Integer> component = scComponents.get(i);
			for (int j = 0; j < component.size(); j++) {
				extended.put(component.get(j), i);
			}
		}

		Map<Integer, Set<Integer>> metaGraph = new HashMap<>();

		for (Integer vertex : graph.keySet()) {
			Integer component1 = extended.get(vertex);

			List<Integer> edges = graph.get(vertex);
			for (Integer edge : edges) {
				Integer component2 = extended.get(edge);

				if (component1 != null && component2 != null && !component1.equals(component2)) {
					Set<Integer> list = metaGraph.get(component1);
					if (list == null) {
						list = new HashSet<>();
					}
					list.add(component2);
					metaGraph.put(component1, list);
				}
			}
		}

		return metaGraph;
	}

	private static void DFS(Map<Integer, List<Integer>> graph, int vertex, List<Integer> component) {
		visited.put(vertex, true);

		List<Integer> vertecies = graph.get(vertex);
		if (vertecies != null) {
			for (int i = 0; i < graph.get(vertex).size(); i++) {
				if (visited.get(graph.get(vertex).get(i)) != null && !visited.get(graph.get(vertex).get(i))) {
					DFS(graph, graph.get(vertex).get(i), component);
				}
			}
		}
		component.add(vertex);
	}

	private static List<Integer> fillOrder(Map<Integer, List<Integer>> graph) {
		List<Integer> order = new ArrayList<>();
		for (Integer vertex : graph.keySet()) {
			if (!visited.get(vertex)) {
				DFS(graph, vertex, order);
			}
		}
		return order;
	}

	private static Map<Integer, List<Integer>> getTranspose(Map<Integer, List<Integer>> graph) {
		Map<Integer, List<Integer>> g = new HashMap<>();

		for (Integer vertex : graph.keySet()) {
			for (int i = 0; i < graph.get(vertex).size(); i++) {

				List<Integer> list;
				if (!g.containsKey(graph.get(vertex).get(i))) {
					list = new ArrayList<>();
				} else {
					list = g.get(graph.get(vertex).get(i));
				}
				list.add(vertex);
				g.put(graph.get(vertex).get(i), list);
			}
		}
		return g;
	}
}
