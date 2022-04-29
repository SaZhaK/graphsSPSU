package Graphs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class RemoveUtil {
	public static Map<Integer, List<Integer>> removeRandomByCount(Map<Integer, List<Integer>> graph, int amountToBeLeft) {
		int idx = 0;
		Set toBeLeft = breadthFirstSearch(graph, idx);
		while (toBeLeft == null) {
			toBeLeft = breadthFirstSearch(graph, ++idx);
		}

		Map<Integer, List<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (toBeLeft.contains(vertex)) {
				List<Integer> edges = graph.get(vertex);
				List<Integer> newEdges = new ArrayList<>();
				for (Integer edge : edges) {
					if (toBeLeft.contains(edge)) {
						newEdges.add(edge);
					}
				}
				newGraph.put(vertex, newEdges);
			}
		}

		return newGraph;
	}

	public static Map<Integer, List<Integer>> removeRandom(Map<Integer, List<Integer>> graph, int percentToBeRemoved) {
		Random random = new Random();
		int size = graph.size();
		int amountToBeRemoved = size * percentToBeRemoved / 100;

		Set<Integer> toBeRemoved = new HashSet<>();

		int removed = 0;
		while (removed < amountToBeRemoved) {
			for (Integer vertex : graph.keySet()) {
				if (random.nextBoolean()) {
					toBeRemoved.add(vertex);
					++removed;
				}

				if (removed >= amountToBeRemoved) {
					break;
				}
			}
		}

		Map<Integer, List<Integer>> result = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (!toBeRemoved.contains(vertex)) {
				List<Integer> edges = graph.get(vertex);
				List<Integer> newEdges = new ArrayList<>();
				for (Integer edge : edges) {
					if (!toBeRemoved.contains(edge)) {
						newEdges.add(edge);
					}
				}
				result.put(vertex, newEdges);
			}
		}

		return result;
	}

	public static Map<Integer, List<Integer>> removeWithBiggestPower(Map<Integer, List<Integer>> graph, int percentToBeRemoved) {
		int size = graph.size();
		int amountToBeRemoved = size * percentToBeRemoved / 100;

		Set<Integer> toBeRemoved = new HashSet<>();
		Map<Integer, List<Integer>> sorted = sortByValue(graph);

		int removed = 0;
		for (Integer vertex : sorted.keySet()) {
			if (removed >= amountToBeRemoved) {
				break;
			} else {
				toBeRemoved.add(vertex);
				++removed;
			}
		}

		Map<Integer, List<Integer>> result = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (!toBeRemoved.contains(vertex)) {
				List<Integer> edges = graph.get(vertex);
				List<Integer> newEdges = new ArrayList<>();
				for (Integer edge : edges) {
					if (!toBeRemoved.contains(edge)) {
						newEdges.add(edge);
					}
				}
				result.put(vertex, newEdges);
			}
		}

		return result;
	}

	private static Map<Integer, List<Integer>> sortByValue(Map<Integer, List<Integer>> graph) {
		List<Map.Entry<Integer, List<Integer>>> list = new ArrayList<>(graph.entrySet());
		list.sort(Comparator.comparing(it -> it.getValue().size()));
		Collections.reverse(list);

		Map<Integer, List<Integer>> result = new LinkedHashMap<>();
		for (Map.Entry<Integer, List<Integer>> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	private static Set breadthFirstSearch(Map<Integer, List<Integer>> graph, int sourceVertex) {
		Queue<Integer> queue = new PriorityQueue<>();
		Map<Integer, Integer> distanceToSource = new HashMap<>();

		Set<Integer> result = new HashSet<>();
		Map<Integer, Boolean> isVisited = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			isVisited.put(vertex, false);
			distanceToSource.put(vertex, 0);
		}

		result.add(sourceVertex);
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
					result.add(adjacentVertex);

					queue.add(adjacentVertex);

					if (distanceToSource.get(adjacentVertex) > eccentricity) {
						eccentricity = distanceToSource.get(adjacentVertex);
					}
				}

				if (result.size() >= 500) {
					break;
				}
			}
			if (result.size() >= 500) {
				return result;
			}
		}
		return null;
	}
}