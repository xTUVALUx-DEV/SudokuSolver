package SudokuSolver.SmartSolver;

public class SmartSolverConfig {
    private int threadedDepth = 1;
    private int maxThreadCount = 4;
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
    
    public static SmartSolverConfig getCustomConfig(int threadedDepth, boolean useTranspositionTable, int maxThreadCount) {
        SmartSolverConfig config = new SmartSolverConfig();
        config.threadedDepth = threadedDepth;
        config.useTranspositionTable = useTranspositionTable;
        config.maxThreadCount = maxThreadCount;
        return config;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }   

}
