import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by MengqinGong on 9/29/16.
 */
public class Solver {

    private int solvable;
    private SearchNode goal;
    private int moves;

    private class SearchNode {
        Board board;
        int numMoves;
        int priority;
        SearchNode previous;

        SearchNode(Board board, int numMoves, SearchNode previous) {
            this.board = board;
            this.numMoves = numMoves;
            this.previous = previous;
            priority = this.board.manhattan() + this.numMoves;
        }
    }

    private class ByManhattan implements Comparator<SearchNode> {
        public int compare(SearchNode o1, SearchNode o2) {
            if (o1.priority < o2.priority) return -1;
            if (o1.priority > o2.priority) return 1;
            if (o1.board.manhattan() > o2.board.manhattan()) return 1;
            if (o1.board.manhattan() < o2.board.manhattan()) return -1;
            return 0;
        }
    }

    public Solver(Board initial) {

        if (initial == null) throw new NullPointerException("Initial board can not be NULL");

        MinPQ<SearchNode> pq = new MinPQ<>(new ByManhattan());
        MinPQ<SearchNode> pqTwin = new MinPQ<>(new ByManhattan());

        SearchNode init = new SearchNode(initial, 0, null);
        SearchNode initTwin = new SearchNode(initial.twin(), 0, null);

        pq.insert(init);
        pqTwin.insert(initTwin);

        solvable = 0;
        SearchNode dequeued;
        SearchNode dequeuedTwin;

        while (true) {
            dequeued = pq.delMin();
            dequeuedTwin = pqTwin.delMin();

            if (dequeued.board.isGoal()) {
                solvable = 1;
                goal = dequeued;
                moves = dequeued.numMoves;
                break;
            }
            if (dequeuedTwin.board.isGoal()) break;

            for (Board neighbor : dequeued.board.neighbors()) {
                if (dequeued.previous == null || !neighbor.equals(dequeued.previous.board)) {
                    pq.insert(new SearchNode(neighbor, dequeued.numMoves + 1, dequeued));
                }
            }

            for (Board neighbor : dequeuedTwin.board.neighbors()) {
                if (dequeuedTwin.previous == null || !neighbor.equals(dequeuedTwin.previous.board)) {
                    pqTwin.insert(new SearchNode(neighbor, dequeuedTwin.numMoves + 1, dequeuedTwin));
                }
            }
        }

    }

    public boolean isSolvable() {
        return solvable == 1;
    }

    public int moves() {
        if (solvable == 0) return -1;
        return moves;
    }

    public Iterable<Board> solution() {
        if (solvable == 0) return null;
        SearchNode goalCopy = new SearchNode(goal.board, goal.numMoves, goal.previous);

        int capacity = goalCopy.numMoves + 1;
        Board[] steps = new Board[capacity];

        for (int i = 1; i < capacity; i++) {
            steps[capacity - i] = goalCopy.board;
            goalCopy = goalCopy.previous;
        }
        steps[0] = goalCopy.board;

        ArrayList<Board> stepsList = new ArrayList<>();
        for (Board step : steps) stepsList.add(step);

        return stepsList;
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

