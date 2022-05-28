package Graphs.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Utility class for counting distance parameters of a graph
 */
public class DistanceUtil {
	private Map<Integer, Integer> eccentricities;
	Map<Integer, Boolean> visited = new HashMap<>();

	private int radius;
	private int diameter;

	/**
	 * Constructor
	 *
	 * @param graph
	 * @param selected - sample list of vertecies to count distances
	 */
	public DistanceUtil(Map<Integer, List<Integer>> graph, List<Integer> selected) {
		eccentricities = new HashMap<>();
		radius = Integer.MAX_VALUE;
		diameter = Integer.MIN_VALUE;

		for (Integer vertex : selected) {
			eccentricities.put(vertex, breadthFirstSearch(graph, vertex));

			if (eccentricities.get(vertex) < radius) {
				radius = eccentricities.get(vertex);
			}
			if (eccentricities.get(vertex) > diameter) {
				diameter = eccentricities.get(vertex);
			}
		}
	}

	/**
	 * Method for counting path between two vertecies using breadth first search method
	 *
	 * @param graph
	 * @param startVertex
	 * @param endVertex
	 * @return distance
	 */
	public int BFS(Map<Integer, List<Integer>> graph, int startVertex, int endVertex) {
		int counter = 0;
		Queue<Integer> q1 = new PriorityQueue<>();
		Queue<Integer> q2 = new PriorityQueue<>();

		q1.add(startVertex);
		visited.put(startVertex, true);

		while (true) {
			if (q1.isEmpty() && q2.isEmpty()) {
				return -1;
			}
			if (counter % 2 == 0) {
				int vertex = q1.remove();
				if (vertex == endVertex) {
					return counter;
				}

				List<Integer> neighbours = graph.get(vertex);
				for (int i = 0; i < neighbours.size(); i++) {
					q2.add(neighbours.get(i));
					visited.put(neighbours.get(i), true);
				}

				if (q1.isEmpty()) {
					counter++;
				}
			}
			if (counter % 2 != 0) {
				int vertex = q2.remove();
				if (vertex == endVertex) {
					return counter;
				}

				List<Integer> neighbours = graph.get(vertex);
				for (int i = 0; i < neighbours.size(); i++) {
					q1.add(neighbours.get(i));
					visited.put(neighbours.get(i), true);
				}

				if (q2.isEmpty()) {
					counter++;
				}
			}
		}
	}

	/**
	 * Method to get vertecies with their eccentricities
	 *
	 * @return map - keys are vertecies, values are eccentricities
	 */
	public Map<Integer, Integer> getEccentricities() {
		return eccentricities;
	}

	/**
	 * Method to get radius of a given graph
	 *
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Method to get diameter of a given graph
	 *
	 * @return diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Method to get 90-percentile of a given graph
	 *
	 * @rerturn 90-percentile
	 */
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
}