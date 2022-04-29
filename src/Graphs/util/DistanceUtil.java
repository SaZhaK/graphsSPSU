package Graphs.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class DistanceUtil {
	private Map<Integer, Integer> eccentricities;

	private int radius;
	private int diameter;

	public DistanceUtil(Map<Integer, List<Integer>> graph) {
		eccentricities = new HashMap<>();
		radius = Integer.MAX_VALUE;
		diameter = Integer.MIN_VALUE;

		for (Integer vertex : graph.keySet()) {
			eccentricities.put(vertex, breadthFirstSearch(graph, vertex));

			if (eccentricities.get(vertex) < radius) {
				radius = eccentricities.get(vertex);
			}
			if (eccentricities.get(vertex) > diameter) {
				diameter = eccentricities.get(vertex);
			}
		}
	}

	private int breadthFirstSearch(Map<Integer, List<Integer>> graph, int sourceVertex) {
		Queue<Integer> queue = new PriorityQueue<>();
		Map<Integer, Integer> distanceToSource = new HashMap<>();

		Map<Integer, Boolean> isVisited = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			isVisited.put(vertex, false);
			distanceToSource.put(vertex, 0);
		}

		isVisited.put(sourceVertex, true);
		distanceToSource.put(sourceVertex, 0);
		queue.add(sourceVertex);

		int eccentricity = 0;
		while (!queue.isEmpty()) {
			int thisVertex = queue.remove();

			List<Integer> adjacent = graph.get(thisVertex);

			for (int adjacentVertex : adjacent) {
				if (!isVisited.get(adjacentVertex)) {
					distanceToSource.put(adjacentVertex, distanceToSource.get(thisVertex) + 1);
					isVisited.put(adjacentVertex, true);
					queue.add(adjacentVertex);

					if (distanceToSource.get(adjacentVertex) > eccentricity) {
						eccentricity = distanceToSource.get(adjacentVertex);
					}
				}
			}
		}

		return eccentricity;
	}

	public Map<Integer, Integer> getEccentricities() {
		return eccentricities;
	}

	public int getRadius() {
		return radius;
	}

	public int getDiameter() {
		return diameter;
	}

	public int countPercentile() {
		double percentile = diameter * 0.9;

		int result = -1;
		for (Integer vertex : eccentricities.keySet()) {
			if (eccentricities.get(vertex) <= percentile && eccentricities.get(vertex) >= result) {
				result = eccentricities.get(vertex);
			}
		}
		return result;
	}
}