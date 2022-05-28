package Graphs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for finding weak connectivity components of graph
 */
public class ConnectivityComponentsUtil {
	private static List<List<Integer>> connectivityComponents;
	private static Map<Integer, Boolean> visited;
	private static int idx;

	/**
	 * Method for finding list of weak connectivity components of a given graph
	 *
	 * @param graph
	 * @return list of lists - each list is a separate connectivity component
	 */
	public static List<List<Integer>> countConnectivityComponents(Map<Integer, List<Integer>> graph) {
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
				connectivityComponents.add(new ArrayList<>());
				DFS(graph, vertex);
				++idx;
			}
		}
		return connectivityComponents;
	}

	private static void DFS(Map<Integer, List<Integer>> graph, int vertex) {
		visited.put(vertex, true);
		connectivityComponents.get(idx).add(vertex);

		for (int i = 0; i < graph.get(vertex).size(); i++) {
			int a = graph.get(vertex).get(i);
			if (visited.get(a) != null && !visited.get(a)) {
				DFS(graph, a);
			}
		}
	}
}
