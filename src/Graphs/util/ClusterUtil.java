package Graphs.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterUtil {
	public static int countPower(Map<Integer, List<Integer>> graph, int vertex) {
		return graph.get(vertex).size();
	}

	private static int countNeighboursEdges(Map<Integer, List<Integer>> graph, int vertex) {
		List<Integer> neighbours = graph.get(vertex);
		int count = 0;
		for (int i = 0; i < neighbours.size(); i++) {
			int curNeighbour = neighbours.get(i);
			List<Integer> neighboursOfCurNeighbour = graph.get(curNeighbour);

			for (Integer neighbour : neighboursOfCurNeighbour) {
				if (neighbours.contains(neighbour)) {
					++count;
				}
			}
		}
		return count / 2;
	}

	public static double countLocalClusterCoefficient(Map<Integer, List<Integer>> graph, int vertex) {
		int power = countPower(graph, vertex);
		if (power < 2) {
			return 0;
		} else {
			int neighboursEdges = countNeighboursEdges(graph, vertex);
			return 2. * neighboursEdges / (power * (power - 1));
		}
	}

	public static double countMediumClusterCoefficient(Map<Integer, List<Integer>> graph) {
		double result = 0;
		for (Integer vertex : graph.keySet()) {
			result += countLocalClusterCoefficient(graph, vertex);
		}
		return result / graph.size();
	}

	public static double countGlobalClusterCoefficient(Map<Integer, List<Integer>> graph) {
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

	public static int countMinPower(Map<Integer, List<Integer>> graph) {
		int min = Integer.MAX_VALUE;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			min = Math.min(min, power);
		}
		return min;
	}

	public static int countMaxPower(Map<Integer, List<Integer>> graph) {
		int max = Integer.MIN_VALUE;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			max = Math.max(max, power);
		}
		return max;
	}

	public static int countAveragePower(Map<Integer, List<Integer>> graph) {
		int sum = 0;
		for (Integer vertex : graph.keySet()) {
			int power = countPower(graph, vertex);
			sum += power;
		}
		return sum / graph.size();
	}

	public static Map<Integer, Double> countDistribution(Map<Integer, List<Integer>> graph) {
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
}
