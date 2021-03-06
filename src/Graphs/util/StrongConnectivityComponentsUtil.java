package Graphs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

/**
 * Utility class for finding strong connectivity components util
 */
public class StrongConnectivityComponentsUtil {
	static class Recursive<I> {
		I func;
	}

	/**
	 * Method to get a list of strong connectivity components of a graph via Kosaraju method
	 *
	 * @param graph
	 * @return list of sets - each set contains vertices from a separate connectivity component
	 */
	public static List<Set<Integer>> getScComponents(Map<Integer, Set<Integer>> graph) {
		int size = graph.size();
		Map<Integer, Boolean> vis = new HashMap<>();
		for (Integer key : graph.keySet()) {
			vis.put(key, false);
		}
		Map<Integer, Integer> l = new HashMap<>();
		for (Integer key : graph.keySet()) {
			l.put(key, 0);
		}
		AtomicInteger x = new AtomicInteger(size);

		Map<Integer, Set<Integer>> t = new HashMap<>();
		for (Integer key : graph.keySet()) {
			t.put(key, new HashSet<>());
		}

		Recursive<IntConsumer> visit = new Recursive<>();
		visit.func = (int u) -> {
			if (!vis.get(u)) {
				vis.put(u, true);
				for (Integer v : graph.get(u)) {
					visit.func.accept(v);
					t.get(v).add(u);
				}
				int xval = x.decrementAndGet();
				l.put(xval, u);
			}
		};

		for (Integer key : graph.keySet()) {
			visit.func.accept(key);

		}
		Map<Integer, Integer> c = new HashMap<>();

		Recursive<BiConsumer<Integer, Integer>> assign = new Recursive<>();
		assign.func = (Integer u, Integer root) -> {
			if (vis.get(u)) {
				vis.put(u, false);
				c.put(u, root);
				for (Integer v : t.get(u)) {
					assign.func.accept(v, root);
				}
			}
		};

		for (int u : l.values()) {
			assign.func.accept(u, u);
		}

		Map<Integer, Set<Integer>> result = new HashMap<>();

		for (Integer key : c.keySet()) {
			int value = c.get(key);

			Set<Integer> list;
			if (result.get(value) == null) {
				list = new HashSet<>();
			} else {
				list = result.get(value);
			}
			list.add(key);
			result.put(value, list);
		}

		List<Set<Integer>> result2 = new ArrayList<>();
		for (Integer key : result.keySet()) {
			result2.add(result.get(key));
		}

		return result2;
	}

	/**
	 * Method to get a meta-graph from strong connectivity components of a given graph
	 *
	 * @param graph
	 * @param scComponents - list of strong connectivity components of a graph
	 * @return meta-graph
	 */
	public static Map<Integer, Set<Integer>> buildMetaGraph(Map<Integer, Set<Integer>> graph, List<Set<Integer>> scComponents) {
		Map<Integer, Integer> extended = new HashMap<>();
		for (int i = 0; i < scComponents.size(); i++) {
			Set<Integer> component = scComponents.get(i);
			for (Integer vertex : component) {
				extended.put(vertex, i);
			}
		}

		Map<Integer, Set<Integer>> metaGraph = new HashMap<>();

		for (Integer vertex : graph.keySet()) {
			Integer component1 = extended.get(vertex);

			Set<Integer> edges = graph.get(vertex);
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
}