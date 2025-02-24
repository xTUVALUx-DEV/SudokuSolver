package SudokuSolver.SmartSolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;

public class SmartSolver {

     public SudokuBoard solve(SudokuBoard board) {
          AtomicBoolean threadStopFlag = new AtomicBoolean(false);
          OptionBoard optionBoard = new OptionBoard(board);

          SudokuBoard newBoard = solve(board, optionBoard, threadStopFlag, 0);
          if (!newBoard.isSolved()) {
               System.out.println("Failed to solve!");
               return null;
          }
          System.out.println("Solved!");
          System.out.println(board);

          return newBoard;

     }
     
     public SudokuBoard solve(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, int threadedDepth) {
          // Solves a generic sudoku board using a smart brute force algorithm
          
          if (board.isSolved()) {
               System.out.println("Solved!");
               return board;
          }

          LinkedList<SudokuMove> moves = board.getPossibleMoves();
          SudokuMove[] sortedMoves = optionBoard.sortMoves(moves);

          

          if (threadedDepth <= 0) 
               return solveStepSinglethreaded(board, optionBoard, threadStopFlag, sortedMoves, threadedDepth);
          else
               return solveStepMultithreaded(board, optionBoard, threadStopFlag, sortedMoves, threadedDepth - 1);

     }

     public SudokuBoard solveStepSinglethreaded(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, SudokuMove[] moves, int depth) {
          for (SudokuMove move : moves) {
               if (threadStopFlag.get()) return null; 

               board.makeMove(move);
               // Print board and wait for a second
               //System.out.println(board);
               //try {
               //     Thread.sleep(200);
               //} catch (InterruptedException e) {
               //     e.printStackTrace();
               //}
               
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

     public SudokuBoard solveStepMultithreaded(SudokuBoard board, OptionBoard optionBoard, AtomicBoolean threadStopFlag, SudokuMove[] moves, int depth) {
          
          List<SudokuBoard> results = Stream.of(moves).parallel().map(move -> {
               if (threadStopFlag.get()) return null; 
               SudokuBoard newBoard = board.copy();
               OptionBoard newOptionBoard = optionBoard.copy();
               newOptionBoard.makeMove(move);
               newBoard.makeMove(move);
               SudokuBoard solvedBoard = solve(newBoard, newOptionBoard, threadStopFlag, depth);
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
