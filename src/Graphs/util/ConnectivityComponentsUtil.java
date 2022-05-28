package Graphs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for finding weak connectivity components of graph
 */
public class ConnectivityComponentsUtil {
	private static List<Set<Integer>> connectivityComponents;
	private static Map<Integer, Boolean> visited;
	private static int idx;

	/**
	 * Method for finding list of weak connectivity components of a given graph
	 *
	 * @param graph
	 * @return list of sets - each set contains vertices from a separate connectivity component
	 */
	public static List<Set<Integer>> countConnectivityComponents(Map<Integer, Set<Integer>> graph) {
		idx = 0;
		connectivityComponents = new ArrayList<>();
		visited = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			visited.put(vertex, false);
		}

		for (Map.Entry<Integer, Boolean> entry : visited.entrySet()) {
			Integer vertex = entry.getKey();
			Boolean visited = entry.getValue();

			if (!visited) {
				connectivityComponents.add(new HashSet<>());
				DFS(graph, vertex);
				++idx;
			}
		}
		return connectivityComponents;
	}

	private static void DFS(Map<Integer, Set<Integer>> graph, int vertex) {
		visited.put(vertex, true);
		connectivityComponents.get(idx).add(vertex);

		Set<Integer> neighbours = graph.get(vertex);

		for (Integer neighbour : neighbours) {
			if (visited.get(neighbour) != null && !visited.get(neighbour)) {
				DFS(graph, neighbour);
			}
		}
	}
}
