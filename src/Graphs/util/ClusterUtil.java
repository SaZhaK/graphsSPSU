package Graphs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for counting cluster coefficients of graph
 */
public class ClusterUtil {
	/**
	 * Method for counting power of given vertex
	 *
	 * @param graph
	 * @param vertex
	 * @return power of vertex
	 */
	public static int countPower(Map<Integer, Set<Integer>> graph, int vertex) {
		return graph.get(vertex).size();
	}

	/**
	 * Method for counting local cluster coefficient of given vertex
	 *
	 * @param graph
	 * @param vertex
	 * @return local cluster coefficient
	 */
	public static double countLocalClusterCoefficient(Map<Integer, Set<Integer>> graph, int vertex) {
		int power = countPower(graph, vertex);
		if (power < 2) {
			return 0;
		} else {
			int neighboursEdges = countNeighboursEdges(graph, vertex);
			return 2. * neighboursEdges / (power * (power - 1));
		}
	}

	/**
	 * Method for counting medium cluster coefficient of given graph
	 *
	 * @param graph
	 * @return medium cluster coefficient
	 */
	public static double countMediumClusterCoefficient(Map<Integer, Set<Integer>> graph) {
		double result = 0;
		for (Integer vertex : graph.keySet()) {
			result += countLocalClusterCoefficient(graph, vertex);
		}
		return result / graph.size();
	}

	/**
	 * Method for counting global cluster coefficient of given graph
	 *
	 * @param graph
	 * @return global cluster coefficient
	 */
	public static double countGlobalClusterCoefficient(Map<Integer, Set<Integer>> graph) {
		double numerator = 0;
		double denominator = 0;
		for (Integer vertex : graph.keySet()) {
			double nv = countPower(graph, vertex);
			double cnv2 = nv * (nv - 1) / 2;
			numerator += (cnv2 * countLocalClusterCoefficient(graph, vertex));
			denominator += cnv2;
		}
		return numerator / denominator;
	}

	/**
	 * Method for counting minimum vertex power in a graph
	 *
	 * @param graph
	 * @return min vertex power in a graph
	 */
	public static int countMinPower(Map<Integer, Set<Integer>> graph) {
		int min = Integer.MAX_VALUE;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			min = Math.min(min, power);
		}
		return min;
	}

	/**
	 * Method for counting maximum vertex power in a graph
	 *
	 * @param graph
	 * @rerurn max vertex power in a graph
	 */
	public static int countMaxPower(Map<Integer, Set<Integer>> graph) {
		int max = Integer.MIN_VALUE;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			max = Math.max(max, power);
		}
		return max;
	}

	/**
	 * Method for counting average vertex power in a graph
	 *
	 * @param graph
	 * @return average vertex power in a graph
	 */
	public static int countAveragePower(Map<Integer, Set<Integer>> graph) {
		int sum = 0;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			sum += power;
		}
		return sum / graph.size();
	}

	/**
	 * Method for counting vertecies' powers distribution function
	 *
	 * @param graph
	 * @return distribution function of graph's vertecies
	 */
	public static Map<Integer, Double> countDistribution(Map<Integer, Set<Integer>> graph) {
		Map<Integer, Double> distribution = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);

			if (!distribution.containsKey(power)) {
				distribution.put(power, 1.);
			} else {
				double counter = distribution.get(power);
				distribution.put(power, counter + 1);
			}
		}

		for (Map.Entry<Integer, Double> entry : distribution.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue();

			distribution.put(key, value / graph.size());
		}
		return distribution;
	}

	private static int countNeighboursEdges(Map<Integer, Set<Integer>> graph, int vertex) {
		Set<Integer> neighbours = graph.get(vertex);
		int count = 0;
		for(Integer curNeighbour : neighbours){
			Set<Integer> neighboursOfCurNeighbour = graph.get(curNeighbour);

			for (Integer neighbour : neighboursOfCurNeighbour) {
				if (neighbours.contains(neighbour)) {
					++count;
				}
			}
		}
		return count / 2;
	}
}
