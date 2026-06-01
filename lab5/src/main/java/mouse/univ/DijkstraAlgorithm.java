package mouse.univ;

import java.util.*;

public class DijkstraAlgorithm {

    public static List<Entry> shortestPath(Graph graph, int from) {
        List<Edge> edgeList = graph.getEdgeList();
        Map<Integer, List<Edge>> adj = new HashMap<>();

        for (Edge edge : edgeList) {
            adj.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge);
        }

        Map<Integer, Integer> distances = new HashMap<>();

        PriorityQueue<Entry> pq = new PriorityQueue<>(
                Comparator.comparingInt(Entry::getDistance)
        );

        distances.put(from, 0);
        pq.add(new Entry(from, 0));

        while (!pq.isEmpty()) {
            Entry current = pq.poll();

            int vertex = current.getVertex();
            int distance = current.getDistance();
            if (distance > distances.getOrDefault(vertex, Integer.MAX_VALUE)) {
                continue;
            }
            for (Edge edge : adj.getOrDefault(vertex, Collections.emptyList())) {
                int to = edge.getTo();
                int newDistance = distance + edge.getWeight();

                if (newDistance < distances.getOrDefault(to, Integer.MAX_VALUE)) {
                    distances.put(to, newDistance);
                    pq.add(new Entry(to, newDistance));
                }
            }
        }
        List<Entry> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            result.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
