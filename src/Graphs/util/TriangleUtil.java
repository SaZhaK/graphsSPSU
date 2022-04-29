package Graphs.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TriangleUtil {

	private Map<Integer, List<Integer>> graph;

	public int countTriangles() {
		int count = 0;
		Collection<Integer> collection = graph.keySet();
		Collection<Integer> neighb;
		Collection<Integer> neighbNeighb;

		Collection<Integer> visitedVertices = new HashSet<>();
		Collection<Integer> tempVisitedVertex = new HashSet<>();

		for (Integer vertex : collection) {
			neighb = graph.get(vertex);
			neighb.removeAll(visitedVertices);

			for (Integer vertexN : neighb) {
				neighbNeighb = graph.get(vertexN);
				neighbNeighb.removeAll(tempVisitedVertex);
				neighbNeighb.removeAll(visitedVertices);
				neighbNeighb.retainAll(neighb);
				count += neighbNeighb.size();
				tempVisitedVertex.add(vertexN);
			}
			visitedVertices.add(vertex);
			tempVisitedVertex = new HashSet<>();
		}

		return count;
	}

	public Map<Integer, List<Integer>> getG() {
		return graph;
	}

	public void setG(Map<Integer, List<Integer>> g) {
		this.graph = g;
	}
}