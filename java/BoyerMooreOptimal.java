package algorithms;

import metrics.PerformanceTracker;

public final class BoyerMooreMajorityVote {

    private BoyerMooreMajorityVote() {}

    public static Integer findMajorityElement(int[] array, PerformanceTracker tracker) {
        if (array == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }

        if (tracker == null) {
            tracker = PerformanceTracker.noop();
        }

        long startNs = System.nanoTime();
        try {
            int n = array.length;
            if (n == 0) return null;

            int candidate = array[0];
            int count = 1;
            tracker.incrementArrayAccesses(1);

            for (int i = 1; i < n; i++) {
                tracker.incrementArrayAccesses(1);
                int value = array[i];

                if (value == candidate) {
                    tracker.incrementComparisons(1);
                    count++;
                } else {
                    tracker.incrementComparisons(1);
                    count--;
                    if (count == 0 && i + 1 < n) {
                        candidate = array[++i];
                        tracker.incrementArrayAccesses(1);
                        count = 1;
                    }
                }
            }

            int occurrences = 0;
            for (int i = 0; i < n; i++) {
                tracker.incrementArrayAccesses(1);
                if (array[i] == candidate) {
                    tracker.incrementComparisons(1);
                    occurrences++;
                } else {
                    tracker.incrementComparisons(1);
                }
            }

            tracker.incrementComparisons(1);
            return (occurrences > n / 2) ? candidate : null;

        } finally {
            tracker.addExecutionTimeNs(System.nanoTime() - startNs);
        }
    }
}
