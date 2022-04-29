package Graphs;

import Graphs.util.ConnectivityComponentsUtil;
import Graphs.util.RemoveUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebGoogle {
	private Map<Integer, List<Integer>> loadGraph(boolean loadAsOriented) throws IOException {
		Path path = Paths.get("src/Graphs/web-Google.txt");

		//Path path = Paths.get("src/Graphs/test.txt");
		List<String> input = Files.readAllLines(path);

		Map<Integer, List<Integer>> graph = new HashMap<>();
		for (String s : input) {
			if (!s.startsWith("#")) {
				String[] split = s.split("\t");
				int fromNode = Integer.parseInt(split[0]);
				int toNode = Integer.parseInt(split[1]);

				List<Integer> curEdges;
				if (!graph.containsKey(fromNode)) {
					curEdges = new ArrayList<>();
				} else {
					curEdges = graph.get(fromNode);
				}
				if (!curEdges.contains(toNode)) {
					curEdges.add(toNode);
				}
				if (!graph.containsKey(toNode)) {
					graph.put(toNode, new ArrayList<>());
				}
				graph.put(fromNode, curEdges);

				if (!loadAsOriented) {
					if (!graph.containsKey(toNode)) {
						curEdges = new ArrayList<>();
					} else {
						curEdges = graph.get(toNode);
					}
					if (!curEdges.contains(fromNode)) {
						curEdges.add(fromNode);
					}
					if (!graph.containsKey(fromNode)) {
						graph.put(fromNode, new ArrayList<>());
					}
					graph.put(toNode, curEdges);
				}
			}
		}
		return graph;
	}

	private int countEdges(Map<Integer, List<Integer>> graph) {
		return countEdgesOriented(graph) * 2;
	}

	private int countEdgesOriented(Map<Integer, List<Integer>> graph) {
		return graph.values().stream().mapToInt(Collection::size).sum();
	}

	private static List<Integer> findMaxComponent(List<List<Integer>> connectivityComponents) {
		return connectivityComponents.stream()
				.max(Comparator.comparing(List::size))
				.get();
	}

	private static Map<Integer, List<Integer>> createComponentGraph(List<Integer> maxComponent, Map<Integer, List<Integer>> graph) {


		Map<Integer, List<Integer>> result = new HashMap<>();

		for (int i = 0; i < maxComponent.size(); i++) {
			Integer vertex = maxComponent.get(i);
			List<Integer> oldLinks = graph.get(vertex);
			result.put(vertex, oldLinks);
		}
		return result;
	}

	static Map<Integer, List<Integer>> copy(Map<Integer, List<Integer>> graph) {
		Map<Integer, List<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			List<Integer> edges = graph.get(vertex);

			List<Integer> newEdges = new ArrayList<>(edges);
			newGraph.put(vertex, newEdges);
		}
		return newGraph;
	}

	static Map<Integer, List<Integer>> cast(Map<Integer, Set<Integer>> graph) {
		Map<Integer, List<Integer>> newGraph = new HashMap<>();
		for (Integer vertex : graph.keySet()) {
			Set<Integer> edges = graph.get(vertex);

			List<Integer> newEdges = new ArrayList<>(edges);
			newGraph.put(vertex, newEdges);
		}
		return newGraph;
	}

	static void testB(Map<Integer, List<Integer>> graph) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("src/Graphs/output.txt");

		for (int i = 0; i < 100; i += 10) {
			Map<Integer, List<Integer>> copy = copy(graph);
			Map<Integer, List<Integer>> removed = RemoveUtil.removeWithBiggestPower(copy, i);

			List<List<Integer>> connectivityComponents = ConnectivityComponentsUtil.countConnectivityComponents(removed);
			List<Integer> maxComponent = findMaxComponent(connectivityComponents);
			int maxComponentSize = maxComponent.size();
			double maxComponentPercent = 1. * maxComponentSize / graph.size();
			writer.println(maxComponentPercent);

			System.out.println(i);
		}

		writer.close();
	}

	public static void main(String[] args) throws IOException {
		boolean isOriented = false;

		WebGoogle webGoogle = new WebGoogle();
		Map<Integer, List<Integer>> graph = webGoogle.loadGraph(isOriented);

		int edgesAmount;
		if (isOriented) {
			edgesAmount = webGoogle.countEdgesOriented(graph);
		} else {
			edgesAmount = webGoogle.countEdges(graph);
		}

		PrintWriter writer = new PrintWriter("src/Graphs/output.txt");

		testB(graph);

//		if (isOriented) {
//			List<List<Integer>> strongConnectivityComponents = StrongConnectivityComponentsUtil.getSCComponents(graph);
//			Map<Integer, List<Integer>> metaGraph = cast(StrongConnectivityComponentsUtil.buildMetaGraph(graph, strongConnectivityComponents));
//
//			Map<Integer, List<Integer>> metaGraph2 = metaGraph;
//			for (int i = 0; i < 4; i++) {
//				List<List<Integer>> strongConnectivityComponents2 = StrongConnectivityComponentsUtil.getSCComponents(metaGraph2);
//				metaGraph2 = cast(StrongConnectivityComponentsUtil.buildMetaGraph(graph, strongConnectivityComponents2));
//			}
//
//			for (Integer vertex : metaGraph2.keySet()) {
//				List<Integer> edges = metaGraph2.get(vertex);
//				for (Integer edge : edges) {
//					writer.println(vertex + " " + edge);
//				}
//			}
//		}

//		writer.println("Число вершин: " + graph.size());
//		writer.println("Число ребер: " + edgesAmount);
//
//		BigDecimal graphSize1 = new BigDecimal(String.valueOf(graph.size()));
//		BigDecimal graphSize2 = new BigDecimal(String.valueOf(graph.size() - 1));
//		BigDecimal maxEdgesAmount = graphSize1.multiply(graphSize2).divide(new BigDecimal(String.valueOf(2)));
//		BigDecimal edgesAmount1 = new BigDecimal(String.valueOf(edgesAmount));
//		BigDecimal density = edgesAmount1.divide(maxEdgesAmount, 10, RoundingMode.HALF_UP);
//		writer.println("Плотность: " + density);
//
//		List<List<Integer>> connectivityComponents = ConnectivityComponentsUtil.countConnectivityComponents(graph);
//		writer.println("Число компонент слабой связности: " + connectivityComponents.size());
//
//		List<Integer> maxComponent = webGoogle.findMaxComponent(connectivityComponents);
//		int maxComponentSize = maxComponent.size();
//		double maxComponentPercent = 1. * maxComponentSize / graph.size();
////		writer.println("Максимальная по мощности компонента слабой связности: " + maxComponent);
//		writer.println("Вершин в максимальной компоненте слабой связности: " + maxComponentSize);
//		writer.println("Доля вершин в максимальной компоненте слабой связности: " + maxComponentPercent);
//
//		if (isOriented) {
//			List<List<Integer>> strongConnectivityComponents = StrongConnectivityComponentsUtil.getSCComponents(graph);
//			writer.println("Число компонент сильной связности: " + strongConnectivityComponents.size());
//
//			List<Integer> maxStrongComponent = webGoogle.findMaxComponent(strongConnectivityComponents);
//			int maxStrongComponentSize = maxStrongComponent.size();
//			double maxStrongComponentPercent = 1. * maxStrongComponentSize / graph.size();
////			writer.println("Максимальная по мощности компонента сильной связности: " + maxStrongComponent);
//			writer.println("Вершин в максимальной компоненте сильной связности: " + maxStrongComponentSize);
//			writer.println("Доля вершин в максимальной компоненте сильной связности: " + maxStrongComponentPercent);
//
//			Map<Integer, Set<Integer>> metaGraph = StrongConnectivityComponentsUtil.buildMetaGraph(graph, strongConnectivityComponents);
//			writer.println("Ребра мета-графа:");
//			writer.println(metaGraph);
//		}
//
//		Map<Integer, List<Integer>> maxComponentGraph = createComponentGraph(maxComponent, graph);
//		Map<Integer, List<Integer>> copy = copy(maxComponentGraph);
//		Map<Integer, List<Integer>> removed = RemoveUtil.removeRandomByCount(copy, 500);
//
//		DistanceUtil distanceUtil = new DistanceUtil(removed);
//		writer.println("Радиус максимальной по мощности компоненты связности: " + distanceUtil.getRadius());
//		writer.println("Диаметр максимальной по мощности компоненты связности: " + distanceUtil.getDiameter());
//		writer.println("90 процентиль расстояния максимальной по мощности компоненты связности: " + distanceUtil.countPercentile());
//
//		TriangleUtil triangleUtil = new TriangleUtil();
//		triangleUtil.setG(graph);
//
//		writer.println("Число треугольников: " + triangleUtil.countTriangles());
//		writer.println("Средний кластерный коэффициент: " + ClusterUtil.countMediumClusterCoefficient(graph));
//		writer.println("Глобальный кластерный коэффициент: " + ClusterUtil.countGlobalClusterCoefficient(graph));
//
//		writer.println("Минимальная степень узла: " + ClusterUtil.countMinPower(graph));
//		writer.println("Максимальная степень узла: " + ClusterUtil.countMaxPower(graph));
//		writer.println("Средняя степень узла: " + ClusterUtil.countAveragePower(graph));
//
//		writer.println("Функция распределения: ");
//		writer.println(ClusterUtil.countDistribution(graph));

		writer.close();

		//System.out.println(RemoveUtil.removeWithBiggestPower(graph, 20));
	}
}
