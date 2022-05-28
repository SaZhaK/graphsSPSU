package Graphs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Utility class for getting subgraphs of a given graph
 */
public class RemoveUtil {
	/**
	 * Method to get a given amount of random vertices
	 *
	 * @param graph
	 * @param amount - amount of vertices to be selected
	 * @return list of selected vertices
	 */
	public static List<Integer> selectRandom(Map<Integer, Set<Integer>> graph, int amount) {
		Random random = new Random();
		Set<Integer> selected = new HashSet<>();
		while (selected.size() < amount) {
			for (Integer vertex : graph.keySet()) {
				if (random.nextBoolean()) {
					selected.add(vertex);
				}
			}
		}
		List<Integer> result = new ArrayList<>();
		for (Integer i : selected) {
			result.add(i);
		}
		return result;
	}

	/**
	 * Method to remove all vertices except given ones from a given graph
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param toBeLeft - set of vertices to be left after remove
	 * @return graph with removed vertices
	 */
	public static Map<Integer, Set<Integer>> removeExcept(Map<Integer, Set<Integer>> graph, Set<Integer> toBeLeft) {
		Map<Integer, Set<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (toBeLeft.contains(vertex)) {
				Set<Integer> edges = graph.get(vertex);
				Set<Integer> newEdges = new HashSet<>();
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

	/**
	 * Method to remove given percent of vertices
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param percentToBeRemoved
	 * @return graph with removed vertices
	 */
	public static Map<Integer, Set<Integer>> removeRandom(Map<Integer, Set<Integer>> graph, int percentToBeRemoved) {
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

		Map<Integer, Set<Integer>> result = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (!toBeRemoved.contains(vertex)) {
				Set<Integer> edges = graph.get(vertex);
				Set<Integer> newEdges = new HashSet<>();
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

	/**
	 * Method to remove given percent of vertecies with biggest powers
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param percentToBeRemoved
	 * @return graph with removed vertecies
	 */
	public static Map<Integer, Set<Integer>> removeWithBiggestPower(Map<Integer, Set<Integer>> graph, int percentToBeRemoved) {
		int size = graph.size();
		int amountToBeRemoved = size * percentToBeRemoved / 100;

		Set<Integer> toBeRemoved = new HashSet<>();
		Map<Integer, Set<Integer>> sorted = sortByValue(graph);

		int removed = 0;
		for (Integer vertex : sorted.keySet()) {
			if (removed >= amountToBeRemoved) {
				break;
			} else {
				toBeRemoved.add(vertex);
				++removed;
			}
		}

		Map<Integer, Set<Integer>> result = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			if (!toBeRemoved.contains(vertex)) {
				Set<Integer> edges = graph.get(vertex);
				Set<Integer> newEdges = new HashSet<>();
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

	private static Map<Integer, Set<Integer>> sortByValue(Map<Integer, Set<Integer>> graph) {
		List<Map.Entry<Integer, Set<Integer>>> list = new ArrayList<>(graph.entrySet());
		list.sort(Comparator.comparing(it -> it.getValue().size()));
		Collections.reverse(list);

		Map<Integer, Set<Integer>> result = new LinkedHashMap<>();
		for (Map.Entry<Integer, Set<Integer>> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
}