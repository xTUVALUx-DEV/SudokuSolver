package SudokuSolver.SmartSolver;

public class SmartSolverConfig {
    private int threadedDepth = 1;
    private boolean useTranspositionTable = true;

    SmartSolverConfig() {}        

    public int getThreadedDepth() {
        return threadedDepth;
    }

    public boolean getUseTranspositionTable() {
        return useTranspositionTable;
    }

    public static SmartSolverConfig getDefaultConfig() {
        return new SmartSolverConfig();
    }
    
    public static SmartSolverConfig getCustomConfig(int threadedDepth, boolean useTranspositionTable) {
        SmartSolverConfig config = new SmartSolverConfig();
        config.threadedDepth = threadedDepth;
        config.useTranspositionTable = useTranspositionTable;
        return config;
    }

}
