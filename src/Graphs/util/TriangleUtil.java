package Graphs.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class for counting number of triangles in a given graph
 */
public class TriangleUtil {
	private Map<Integer, List<Integer>> graph;

	/**
	 * Constructor
	 *
	 * @param graph
	 */
	public TriangleUtil(Map<Integer, List<Integer>> graph) {
		this.graph = graph;
	}

	/**
	 * Method to get a number of triangles in a given graph
	 *
	 * @return number of triangles
	 */
	public long numberOfTriangles() {
		Map<Integer, Boolean> marked_a = new HashMap<>();
		for (Integer a : graph.keySet()) {
			marked_a.put(a, false);
		}

		long triangles = 0;

		for (Integer a : graph.keySet()) {
			Map<Integer, Boolean> marked_b = new HashMap<>();
			for (Integer c : graph.get(a)) {
				marked_b.put(c, false);
			}
			marked_b.put(a, true);
			for (Integer b : graph.get(a)) {
				if (!marked_a.get(b)) {
					for (Integer c : graph.get(b)) {
						if (!marked_a.get(c) && marked_b.containsKey(c) && !marked_b.get(c)) {
							triangles++;
						}
					}
					marked_b.put(b, true);
				}
			}
			marked_a.put(a, true);
		}
		return triangles;
	}
}