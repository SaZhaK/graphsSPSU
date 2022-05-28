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
	 * Method to get a given amount of random vertecies
	 *
	 * @param graph
	 * @param amount - amount of vertecies to be selected
	 * @return list of selected vertecies
	 */
	public static List<Integer> selectRandom(Map<Integer, List<Integer>> graph, int amount) {
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
	 * Method to remove all vertecies except given ones from a given graph
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param toBeLeft - set of vertecies to be left after remove
	 * @return graph with removed vertecies
	 */
	public static Map<Integer, List<Integer>> removeExcept(Map<Integer, List<Integer>> graph, Set<Integer> toBeLeft) {
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

	/**
	 * Method to remove given percent of vertecies
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param percentToBeRemoved
	 * @return graph with removed vertecies
	 */
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

	/**
	 * Method to remove given percent of vertecies with biggest powers
	 * Important! graph will be modified
	 *
	 * @param graph
	 * @param percentToBeRemoved
	 * @return graph with removed vertecies
	 */
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
}