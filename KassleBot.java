package put.ai.games.Rudowicz_Kierkosz;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.SkipMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rudowicz_Kierkosz extends Player {

    private static final int MAX_DEPTH = 4;
    private static final int TIME_MARGIN_MS = 50;

    private Random random = new Random();

    @Override
    public String getName() {
        return "Bartłomiej Rudowicz 155993 Paweł Kierkosz 155995";
    }

    @Override
    public Move nextMove(Board board) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + getTime() - TIME_MARGIN_MS;

        List<Move> moves = board.getMovesFor(getColor());
        if (moves.isEmpty()) {
            return skipOrNull();
        }

        Move immediateWin = findImmediateWinningMove(board, moves);
        if (immediateWin != null) {
            return immediateWin;
        }

        Move blockMove = findImmediateLosingMove(board, moves);
        if (blockMove != null) {
            return blockMove;
        }

        Move bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (Move move : moves) {
            board.doMove(move);
            double value = alphaBeta(board, MAX_DEPTH - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, endTime);
            board.undoMove(move);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        if (bestMove == null) {
            bestMove = moves.get(random.nextInt(moves.size()));
        }
        return bestMove;
    }

    private double alphaBeta(Board board, int depth, double alpha, double beta, boolean maximizingPlayer, long endTime) {
        if (System.currentTimeMillis() >= endTime) {
            return evaluate(board);
        }

        Player.Color winner = board.getWinner(currentPlayerColor(maximizingPlayer));
        if (winner != null) {
            if (winner == getColor()) {
                return Double.POSITIVE_INFINITY;
            } else if (winner == Player.Color.EMPTY) {
                return 0.0;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        if (depth <= 0) {
            return evaluate(board);
        }

        Player.Color mover = currentPlayerColor(maximizingPlayer);
        List<Move> moves = board.getMovesFor(mover);
        if (moves.isEmpty()) {
            return evaluate(board);
        }

        if (maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            for (Move m : moves) {
                board.doMove(m);
                double childValue = alphaBeta(board, depth - 1, alpha, beta, false, endTime);
                board.undoMove(m);

                if (childValue > value) {
                    value = childValue;
                }
                if (value > alpha) {
                    alpha = value;
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (Move m : moves) {
                board.doMove(m);
                double childValue = alphaBeta(board, depth - 1, alpha, beta, true, endTime);
                board.undoMove(m);

                if (childValue < value) {
                    value = childValue;
                }
                if (value < beta) {
                    beta = value;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }

    private Move findImmediateWinningMove(Board board, List<Move> moves) {
        for (Move m : moves) {
            board.doMove(m);
            Player.Color w = board.getWinner(getColor());
            board.undoMove(m);

            if (w == getColor()) {
                return m;
            }
        }
        return null;
    }

    private Move findImmediateLosingMove(Board board, List<Move> myMoves) {
        Player.Color opp = Player.getOpponent(getColor());
        List<Move> oppMoves = board.getMovesFor(opp);
        List<Move> oppWinningMoves = new ArrayList<>();
        for (Move om : oppMoves) {
            board.doMove(om);
            if (board.getWinner(opp) == opp) {
                oppWinningMoves.add(om);
            }
            board.undoMove(om);
        }
        if (oppWinningMoves.isEmpty()) {
            return null;
        }

        for (Move om : oppWinningMoves) {
            for (Move myM : myMoves) {
                board.doMove(myM);
                boolean stillWinning = false;
                List<Move> oppNext = board.getMovesFor(opp);
                if (oppNext.contains(om)) {
                    board.doMove(om);
                    if (board.getWinner(opp) == opp) {
                        stillWinning = true;
                    }
                    board.undoMove(om);
                }
                board.undoMove(myM);

                if (!stillWinning) {
                    return myM;
                }
            }
        }
        return null;
    }

    private double evaluate(Board board) {
        int myCount = countStones(board, getColor());
        int oppCount = countStones(board, Player.getOpponent(getColor()));
        return myCount - oppCount;
    }

    private int countStones(Board board, Player.Color color) {
        int count = 0;
        int size = board.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.getState(x, y) == color) {
                    count++;
                }
            }
        }
        return count;
    }

    private Player.Color currentPlayerColor(boolean maximizingPlayer) {
        return maximizingPlayer ? getColor() : Player.getOpponent(getColor());
    }

    private Move skipOrNull() {
        return new SkipMove() {
            @Override
            public Color getColor() {
                return Rudowicz_Kierkosz.this.getColor();
            }
        };
    }
}