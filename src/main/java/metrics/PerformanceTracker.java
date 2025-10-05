package metrics;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceTracker {
    private final AtomicLong comparisons = new AtomicLong();
    private final AtomicLong arrayAccesses = new AtomicLong();
    private final AtomicLong executionTimeNs = new AtomicLong();
    private final AtomicLong memoryAllocatedBytes = new AtomicLong();

    private static final PerformanceTracker NOOP = new NoopPerformanceTracker();

    public static PerformanceTracker create() {
        return new PerformanceTracker();
    }

    public static PerformanceTracker noop() {
        return NOOP;
    }

    public void incrementComparisons(long delta) { comparisons.addAndGet(delta); }
    public void incrementArrayAccesses(long delta) { arrayAccesses.addAndGet(delta); }
    public void addExecutionTimeNs(long delta) { executionTimeNs.addAndGet(delta); }
    public void addMemoryAllocatedBytes(long delta) { memoryAllocatedBytes.addAndGet(delta); }

    public long getComparisons() { return comparisons.get(); }
    public long getArrayAccesses() { return arrayAccesses.get(); }
    public long getExecutionTimeNs() { return executionTimeNs.get(); }
    public long getMemoryAllocatedBytes() { return memoryAllocatedBytes.get(); }

    public void reset() {
        comparisons.set(0);
        arrayAccesses.set(0);
        executionTimeNs.set(0);
        memoryAllocatedBytes.set(0);
    }

    private static final class NoopPerformanceTracker extends PerformanceTracker {
        @Override public void incrementComparisons(long delta) {}
        @Override public void incrementArrayAccesses(long delta) {}
        @Override public void addExecutionTimeNs(long delta) {}
        @Override public void addMemoryAllocatedBytes(long delta) {}
        @Override public void reset() {}
    }
}


