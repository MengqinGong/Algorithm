import edu.princeton.cs.algs4.In;

import java.util.Stack;

/**
 * Created by MengqinGong on 9/29/16.
 */
public class Board {

    private int[] board;
    private int n;

    public Board(int[][] blocks) {

        n = blocks.length;
        board = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[n * i + j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n * n; i++) {
            if (board[i] != 0) {
                if (board[i] != i + 1) hamming++;
            }
        }
        return hamming;
    }

    private int absNum(int num) {
        if (num > 0) return num;
        else return -num;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n * n; i++) {
            if (board[i] != 0) {
                manhattan += absNum(i / n - (board[i] - 1) / n) + absNum(i % n - (board[i] - 1) % n);
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        if (board[0] == 0) {
            return swap(board, 1, 2);
        } else if (board[1] == 0) {
            return swap(board, 0, 3);
        } else {
            return swap(board, 0, 1);
        }
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < n * n; i++) {
            if (this.board[i] != that.board[i]) return false;
        }
        return true;
    }

    private Board swap(int[] board, int i, int j) {
        if (i >= j) throw new IllegalArgumentException("i must be smaller than j");
        int[][] newBoard = new int[n][n];
        for (int k = 0; k < i; k++) newBoard[k / n][k % n] = board[k];
        newBoard[i / n][i % n] = board[j];
        for (int k = i + 1; k < j; k++) newBoard[k / n][k % n] = board[k];
        newBoard[j / n][j % n] = board[i];
        for (int k = j + 1; k < n * n; k++) newBoard[k / n][k % n] = board[k];
        return new Board(newBoard);
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int blank = 0;
        for (int i = 0; i < n * n; i++) {
            if (board[i] == 0) {
                blank = i;
                break;
            }
        }
        int row = blank / n;
        int col = blank % n;

        if (row > 0) neighbors.push(swap(board, (row - 1) * n + col, row * n + col));
        if (row < n - 1) neighbors.push(swap(board, row * n + col, (row + 1) * n + col));
        if (col > 0) neighbors.push(swap(board, row * n + col - 1, row * n + col));
        if (col < n - 1) neighbors.push(swap(board, row * n + col, row * n + col + 1));

        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i * n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            System.out.println(initial);

            // for (Board neighbor : initial.neighbors()) System.out.println(neighbor);

            System.out.println(initial.dimension());
            System.out.println(initial.hamming());
            System.out.println(initial.manhattan());


        }

    }
}