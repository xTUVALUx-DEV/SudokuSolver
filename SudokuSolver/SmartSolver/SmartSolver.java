package SudokuSolver.SmartSolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;

public class SmartSolver {

     public SmartSolver() {
     }

     public LinkedList<SudokuMove> sortMoves(LinkedList<SudokuMove> moves, SudokuBoard board) {
          // Maximise the number of moves

          moves.sort((a, b) -> {
               board.makeMove(a);
               int aMoves = board.getPossibleMoves().size();
               board.undoMove(a);
               
               board.makeMove(b);
               int bMoves = board.getPossibleMoves().size();
               board.undoMove(b);
          
               return bMoves - aMoves;
          });

          return moves;
     }

     public SudokuBoard solve(SudokuBoard board) {
          AtomicBoolean threadStopFlag = new AtomicBoolean(false);

          SudokuBoard newBoard = solve(board, threadStopFlag, 0);
          if (!newBoard.isSolved()) {
               System.out.println("Failed to solve!");
               return null;
          }
          System.out.println("Solved!");
          System.out.println(board);

          return newBoard;

     }
     
     public SudokuBoard solve(SudokuBoard board, AtomicBoolean threadStopFlag, int threadedDepth) {
          // Solves a generic sudoku board using a brute force algorithm while trying to maximise the number of possible moves
          
          if (board.isSolved()) {
               System.out.println("Solved!");
               return board;
          }

          LinkedList<SudokuMove> moves = board.getPossibleMoves();
          moves = sortMoves(moves, board);

          if (threadedDepth <= 0) 
               return solveStepSinglethreaded(board, threadStopFlag, moves, threadedDepth);
          else
               return solveStepMultithreaded(board, threadStopFlag, moves, threadedDepth - 1);

     }

     public SudokuBoard solveStepSinglethreaded(SudokuBoard board, AtomicBoolean threadStopFlag, LinkedList<SudokuMove> moves, int depth) {
          for (SudokuMove move : moves) {
               if (threadStopFlag.get()) return null; 

               board.makeMove(move);
               
               SudokuBoard solvedBoard = solve(board, threadStopFlag, depth);
               if (solvedBoard != null) {
                    return solvedBoard;
               }
               board.undoMove(move);
          }

          return null;
     }

     public SudokuBoard solveStepMultithreaded(SudokuBoard board, AtomicBoolean threadStopFlag, LinkedList<SudokuMove> moves, int depth) {
          if(1==1) 
               throw new RuntimeException("Not implemented");

          List<SudokuBoard> results = moves.parallelStream().map(move -> {
               if (threadStopFlag.get()) return null; 
               SudokuBoard newBoard = board.copy();
               newBoard.makeMove(move);
               SudokuBoard solvedBoard = solve(newBoard, threadStopFlag, depth);
               if (solvedBoard != null) {
                    threadStopFlag.set(true); // Stop all other threads
                    return solvedBoard;
               }
               return null;
          }).collect(Collectors.toList());
          
          for (SudokuBoard result : results) {
               if (result != null) {
                    return result;
               }
               // Todo: Check for multiple solutions
          }
          return null;
          
     }

}
