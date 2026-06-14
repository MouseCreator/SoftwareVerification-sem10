package mouse.univ;

import mouse.univ.exception.InfinitelyLowWeightException;
import mouse.univ.exception.InvalidGraphException;
import mouse.univ.exception.InvalidVertexException;
import mouse.univ.exception.VerticesNotConnectedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphTest {

    private final ShortestPathFinder finder = new ShortestPathFinderImpl();

    @Test
    void testFindsShortestPath() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 2)
                .edge("B", "E", 5)
                .edge("B", "B", 1)
                .edge("A", "C", 4)
                .edge("C", "D", 3)
                .edge("D", "E", -2).build();

        int shortestPath = finder.findShortestPath(graph, "A", "E");
        Assertions.assertEquals(5, shortestPath);
    }

    @Test
    void testThrowsNotConnectedException() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 7)
                .edge("B", "C", 3)
                .edge("C", "A", -1)
                .edge("D", "E", 5)
                .edge("E","D", -2)
                .build();

        Assertions.assertThrows(
                VerticesNotConnectedException.class,
                ()->finder.findShortestPath(graph, "A", "D")
        );
    }

    @Test
    void testThrowsInfinitelyLowWeightException() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 7)
                .edge("B", "C", 2)
                .edge("C", "D", 3)
                .edge("D", "B", -8)
                .edge("C", "A", 1)
                .edge("C", "E", 5)
                .build();

        Assertions.assertThrows(
                InfinitelyLowWeightException.class,
                ()->finder.findShortestPath(graph, "A", "D")
        );
    }

    @Test
    void testThrowsInvalidVertexException() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 5)
                .edge("B", "C", 7)
                .edge("B", "C", 2)
                .edge("B", "D", -4)
                .build();

        Assertions.assertEquals(1, finder.findShortestPath(graph, "A", "D"));
        Assertions.assertThrows(InvalidVertexException.class, () -> finder.findShortestPath(graph, "X", "A"));
        Assertions.assertThrows(InvalidVertexException.class, () -> finder.findShortestPath(graph, "A", "X"));
        Assertions.assertThrows(InvalidVertexException.class, () -> finder.findShortestPath(graph, "X", "X"));
    }

    @Test
    void testMinimumNumberOfVerticesWorks() {
        Graph graph = GraphBuilder.init().edge("A", "A", 5).build();
        Assertions.assertEquals(0, finder.findShortestPath(graph, "A", "A"));
    }

    @Test
    void testMaximumNumberOfVerticesWorks() {
        Graph graph = GraphBuilder.loop(100, 2);
        Assertions.assertEquals(198, finder.findShortestPath(graph, "V0", "V99"));
    }

    @Test
    void testNoVerticesThrows() {
        Graph graph = GraphBuilder.init().build();
        Assertions.assertThrows(InvalidGraphException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

    @Test
    void test101VerticesThrows() {
        Graph graph = GraphBuilder.loop(101, 1);
        Assertions.assertThrows(InvalidGraphException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

    @Test
    void testMinEdgeWeightWorks() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 100)
                .edge("B", "C", -400)
                .edge("B", "D", -500)
                .build();

        Assertions.assertEquals(-400, finder.findShortestPath(graph, "A", "D"));
    }

    @Test
    void testMaxEdgeWeightWorks() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 100)
                .edge("B", "C", 200)
                .edge("C", "D", -300)
                .edge("B", "E", 400)
                .edge("E", "F", 500)
                .build();

        Assertions.assertEquals(1000, finder.findShortestPath(graph, "A", "F"));
    }

    @Test
    void testThrowsOnGraphIsNull() {
        Graph graph = null;
        Assertions.assertThrows(InvalidGraphException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

    @Test
    void testThrowsOnTooLowEdgeWeight() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", -501)
                .build();

        Assertions.assertThrows(InvalidGraphException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

    @Test
    void testThrowsOnTooHighEdgeWeight() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 501)
                .build();
        Assertions.assertThrows(InvalidGraphException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

    @Test
    void testHooksWorks() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 3)
                .edge("A", "C", -5)
                .edge("A", "D", 4)
                .build();
        Assertions.assertEquals(0, finder.findShortestPath(graph, "A", "A"));
    }

    @Test
    void testHookThrowsOnNegativeLoop() {
        Graph graph = GraphBuilder.init()
                .edge("A", "B", 8)
                .edge("B", "C", -4)
                .edge("C", "A", -5)
                .build();
        Assertions.assertThrows(InfinitelyLowWeightException.class, ()->finder.findShortestPath(graph, "A", "B"));
    }

}