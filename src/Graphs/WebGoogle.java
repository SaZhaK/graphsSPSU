package Graphs;

import Graphs.util.ClusterUtil;
import Graphs.util.ConnectivityComponentsUtil;
import Graphs.util.DistanceUtil;
import Graphs.util.StrongConnectivityComponentsUtil;
import Graphs.util.RemoveUtil;
import Graphs.util.TriangleUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebGoogle {
	private Map<Integer, Set<Integer>> loadGraph(boolean loadAsOriented) throws IOException {
		Path path = Paths.get("src/Graphs/email-Eu-core.txt");

		//Path path = Paths.get("src/Graphs/test.txt");
		List<String> input = Files.readAllLines(path);

		Map<Integer, Set<Integer>> graph = new HashMap<>();
		for (String s : input) {
			if (!s.startsWith("#")) {
				String[] split = s.split(" ");
				int fromNode = Integer.parseInt(split[0]);
				int toNode = Integer.parseInt(split[1]);

				Set<Integer> curEdges;
				if (!graph.containsKey(fromNode)) {
					curEdges = new HashSet<>();
				} else {
					curEdges = graph.get(fromNode);
				}
				if (!curEdges.contains(toNode)) {
					curEdges.add(toNode);
				}
				if (!graph.containsKey(toNode)) {
					graph.put(toNode, new HashSet<>());
				}
				graph.put(fromNode, curEdges);

				if (!loadAsOriented) {
					if (!graph.containsKey(toNode)) {
						curEdges = new HashSet<>();
					} else {
						curEdges = graph.get(toNode);
					}
					if (!curEdges.contains(fromNode)) {
						curEdges.add(fromNode);
					}
					if (!graph.containsKey(fromNode)) {
						graph.put(fromNode, new HashSet<>());
					}
					graph.put(toNode, curEdges);
				}
			}
		}
		return graph;
	}

	private int countEdges(Map<Integer, Set<Integer>> graph) {
		return countEdgesOriented(graph) * 2;
	}

	private int countEdgesOriented(Map<Integer, Set<Integer>> graph) {
		return graph.values().stream().mapToInt(Collection::size).sum();
	}

	private static Set<Integer> findMaxComponent(List<Set<Integer>> connectivityComponents) {
		return connectivityComponents.stream()
				.max(Comparator.comparing(Set::size))
				.get();
	}

	private static Map<Integer, Set<Integer>> createComponentGraph(Set<Integer> maxComponent, Map<Integer, Set<Integer>> graph) {
		Map<Integer, Set<Integer>> result = new HashMap<>();
		for (Integer vertex : maxComponent) {
			Set<Integer> oldLinks = graph.get(vertex);
			result.put(vertex, oldLinks);
		}
		return result;
	}

	static Map<Integer, Set<Integer>> copy(Map<Integer, Set<Integer>> graph) {
		Map<Integer, Set<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			Set<Integer> edges = graph.get(vertex);

			Set<Integer> newEdges = new HashSet<>(edges);
			newGraph.put(vertex, newEdges);
		}
		return newGraph;
	}

	static Map<Integer, Set<Integer>> cast(Map<Integer, Set<Integer>> graph) {
		Map<Integer, Set<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			Set<Integer> edges = graph.get(vertex);

			Set<Integer> newEdges = new HashSet<>(edges);
			newGraph.put(vertex, newEdges);
		}
		return newGraph;
	}

	static void testB(Map<Integer, Set<Integer>> graph) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("src/Graphs/output.txt");

		for (int i = 0; i < 100; i += 10) {
			Map<Integer, Set<Integer>> copy = copy(graph);
			Map<Integer, Set<Integer>> removed = RemoveUtil.removeWithBiggestPower(copy, i);

			List<Set<Integer>> connectivityComponents = ConnectivityComponentsUtil.countConnectivityComponents(removed);
			Set<Integer> maxComponent = findMaxComponent(connectivityComponents);
			int maxComponentSize = maxComponent.size();
			double maxComponentPercent = 1. * maxComponentSize / graph.size();
			writer.println(maxComponentPercent);
		}
		writer.close();
	}

	public static void analyseMetaGraph(Map<Integer, Set<Integer>> mgList, PrintWriter writer) {
		writer.println(ClusterUtil.countDistribution(mgList));
		int moreCounter = 0;
		int lessCounter = 0;
		Set<Integer> toBeLeft = new HashSet<>();
		for (Integer key : mgList.keySet()) {
			Set<Integer> edges = mgList.get(key);
			if (edges.size() > 10) {
				moreCounter++;
				toBeLeft.add(key);
			} else {
				lessCounter++;
			}
		}
		writer.println("More: " + moreCounter);
		writer.println("Less: " + lessCounter);

		Map<Integer, Set<Integer>> mgListRemoved = RemoveUtil.removeExcept(mgList, toBeLeft);
		writer.println(mgListRemoved);
	}

	public static void main(String[] args) throws IOException {
		boolean isOriented = false;

		WebGoogle webGoogle = new WebGoogle();
		Map<Integer, Set<Integer>> graph = webGoogle.loadGraph(isOriented);

		int edgesAmount;
		if (isOriented) {
			edgesAmount = webGoogle.countEdgesOriented(graph);
		} else {
			edgesAmount = webGoogle.countEdges(graph);
		}

		PrintWriter writer = new PrintWriter("src/Graphs/output.txt");

		writer.println("Число вершин: " + graph.size());
		writer.println("Число ребер: " + edgesAmount);

		BigDecimal graphSize1 = new BigDecimal(String.valueOf(graph.size()));
		BigDecimal graphSize2 = new BigDecimal(String.valueOf(graph.size() - 1));
		BigDecimal maxEdgesAmount = graphSize1.multiply(graphSize2).divide(new BigDecimal(String.valueOf(2)));
		BigDecimal edgesAmount1 = new BigDecimal(String.valueOf(edgesAmount));
		BigDecimal density = edgesAmount1.divide(maxEdgesAmount, 10, RoundingMode.HALF_UP);
		writer.println("Плотность: " + density);

		List<Set<Integer>> connectivityComponents = ConnectivityComponentsUtil.countConnectivityComponents(graph);
		writer.println("Число компонент слабой связности: " + connectivityComponents.size());

		Set<Integer> maxComponent = webGoogle.findMaxComponent(connectivityComponents);
		int maxComponentSize = maxComponent.size();
		double maxComponentPercent = 1. * maxComponentSize / graph.size();
		//writer.println("Максимальная по мощности компонента слабой связности: " + maxComponent);
		writer.println("Вершин в максимальной компоненте слабой связности: " + maxComponentSize);
		writer.println("Доля вершин в максимальной компоненте слабой связности: " + maxComponentPercent);

		if (isOriented) {
			List<Set<Integer>> strongConnectivityComponents = StrongConnectivityComponentsUtil.getScComponents(graph);
			writer.println("Число компонент сильной связности: " + strongConnectivityComponents.size());

			Set<Integer> maxStrongComponent = webGoogle.findMaxComponent(strongConnectivityComponents);
			int maxStrongComponentSize = maxStrongComponent.size();
			double maxStrongComponentPercent = 1. * maxStrongComponentSize / graph.size();
			//writer.println("Максимальная по мощности компонента сильной связности: " + maxStrongComponent);
			writer.println("Вершин в максимальной компоненте сильной связности: " + maxStrongComponentSize);
			writer.println("Доля вершин в максимальной компоненте сильной связности: " + maxStrongComponentPercent);

			Map<Integer, Set<Integer>> metaGraph = StrongConnectivityComponentsUtil.buildMetaGraph(graph, strongConnectivityComponents);
			writer.println("Ребра мета-графа:");
			//writer.println(metaGraph);
		}

		Map<Integer, Set<Integer>> maxComponentGraph = createComponentGraph(maxComponent, graph);
		Map<Integer, Set<Integer>> copy = copy(maxComponentGraph);
		List<Integer> selected = RemoveUtil.selectRandom(copy, 500);

		DistanceUtil distanceUtil = new DistanceUtil(graph, selected);
		writer.println("Радиус максимальной по мощности компоненты связности: " + distanceUtil.getRadius());
		writer.println("Диаметр максимальной по мощности компоненты связности: " + distanceUtil.getDiameter());
		writer.println("90 процентиль расстояния максимальной по мощности компоненты связности: " + distanceUtil.countPercentile());

		TriangleUtil triangleUtil = new TriangleUtil(graph);
		writer.println("Число треугольников: " + triangleUtil.numberOfTriangles());
		writer.println("Средний кластерный коэффициент: " + ClusterUtil.countMediumClusterCoefficient(graph));
		writer.println("Глобальный кластерный коэффициент: " + ClusterUtil.countGlobalClusterCoefficient(graph));

		writer.println("Минимальная степень узла: " + ClusterUtil.countMinPower(graph));
		writer.println("Максимальная степень узла: " + ClusterUtil.countMaxPower(graph));
		writer.println("Средняя степень узла: " + ClusterUtil.countAveragePower(graph));

		writer.println("Функция распределения: ");
		writer.println(ClusterUtil.countDistribution(graph));

		writer.close();
	}
}
