package SudokuSolver.SmartSolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;

public class GenericSmartSolver {
     
     static TranspositionTable transpositionTable = new TranspositionTable();
     SmartSolverConfig config;

     public GenericSmartSolver() {
          this(new SmartSolverConfig());
     }

     public GenericSmartSolver(SmartSolverConfig config) {
          this.config = config;
     }

     public SudokuBoard solve(SudokuBoard board) {
          System.out.println("Solving with SmartSolver...");
          long startTime = System.currentTimeMillis();

          AtomicBoolean threadStopFlag = new AtomicBoolean(false);  // To stop all the threads once a solution is found
          OptionBoard optionBoard = new OptionBoard(board);  // To keep track of possible moves

          SudokuBoard newBoard = solve(board, optionBoard, threadStopFlag, config.getThreadedDepth());
          if (newBoard == null || !newBoard.isSolved()) {
               System.out.println("Failed to solve!");
               return null;
          }
          System.out.printf("Solved in %dms!%n", System.currentTimeMillis() - startTime);
          System.out.println(newBoard);

          return newBoard;

     }

     public SudokuBoard solve(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, int threadedDepth) {
          // Solves a generic sudoku board using a smart brute force algorithm which always picks the move with the least possible options
          // May have hash collisions if a transposition table is used
          // Uses multithreading to solve the board faster
          // ~30ms for the test board


          if (board.isSolved()) {
               return board;
          }

          LinkedList<SudokuMove> moves = board.getPossibleMoves();
          OptionsMoveWrapper[] sortedMoves = optionBoard.sortMoves(moves);
          
          if (sortedMoves.length == 0) {
               return null;
          }

          if (sortedMoves[0].getOptionCount() == 1) { 
               // If there is only one option, make the move and continue
               sortedMoves = new OptionsMoveWrapper[] {sortedMoves[0]};
               
               return solveStepSinglethreaded(board, optionBoard, threadStopFlag, sortedMoves, threadedDepth);
          }

          if (threadedDepth <= 0) 
               return solveStepSinglethreaded(board, optionBoard, threadStopFlag, sortedMoves, threadedDepth);
          else
               return solveStepMultithreaded(board, optionBoard, threadStopFlag, sortedMoves, threadedDepth - 1);

     }

     public SudokuBoard solveStepSinglethreaded(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, OptionsMoveWrapper[] moves, int depth) {
          for (OptionsMoveWrapper optionMove : moves) {
               if (threadStopFlag.get()) return null;

               SudokuMove move = optionMove.getMove();
               board.makeMove(move);
               
               if (config.getUseTranspositionTable() && TranspositionTable.checkOrAdd(board)) {
                    board.undoMove(move);
                    return null;
               }

               OptionBoardMove optMove = optionBoard.makeMove(move);
               
               SudokuBoard solvedBoard = solve(board, optionBoard, threadStopFlag, depth);
               if (solvedBoard != null) {
                    return solvedBoard;
               }
               board.undoMove(move);
               optionBoard.undoMove(optMove);
          }

          return null;
     }

     public SudokuBoard solveStepMultithreaded(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, OptionsMoveWrapper[] moves, int depth) {
          List<SudokuBoard> results = new ArrayList<>();

          ForkJoinPool forkJoinPool = null;
          try {
               forkJoinPool = new ForkJoinPool(config.getMaxThreadCount()); // Always use 10 threads
                    results = forkJoinPool.submit(() ->
                         Stream.of(moves).parallel().map(optionsMove -> {
                              if (threadStopFlag.get()) return null;  // Solution found in another thread

                              SudokuMove move = optionsMove.getMove();

                              // Copy the board and make the move without affecting other threads
                              SudokuBoard newBoard = board.copy();
                              OptionBoard newOptionBoard = optionBoard.copy();
                              newOptionBoard.makeMove(move);
                              newBoard.makeMove(move);

                              if (config.getUseTranspositionTable() && TranspositionTable.checkOrAdd(newBoard)) return null;

                              SudokuBoard solvedBoard = solve(newBoard, newOptionBoard, threadStopFlag, depth);
                              if (solvedBoard != null) {
                                   threadStopFlag.set(true); // Stop all other threads
                                   return solvedBoard;
                              }
                              return null;
                         }).collect(Collectors.toList())
               ).get();
               
          } catch (InterruptedException | ExecutionException e) {
               throw new RuntimeException(e);
          } finally {
               if (forkJoinPool != null) {
                    forkJoinPool.shutdown();
                    forkJoinPool.close();
               }
          }

          for (SudokuBoard result : results) {
               if (result != null) {
                    return result;
               }
               // Todo: Check for multiple solutions
          }
          return null;
          
     }

}
