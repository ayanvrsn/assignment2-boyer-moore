package algorithms;

import metrics.PerformanceTracker;

public final class BoyerMooreMajorityVote {

    private BoyerMooreMajorityVote() {}

    public static Integer findMajorityElement(int[] array, PerformanceTracker tracker) {
        if (tracker == null) {
            tracker = PerformanceTracker.noop();
        }
        long startNs = System.nanoTime();
        try {
            if (array.length == 0) {
                return null;
            }

            int candidate = 0;
            int count = 0;
            for (int i = 0; i < array.length; i++) {
                tracker.incrementArrayAccesses(1);
                int value = array[i];

                if (count < 0) {
                  count = 0; 
                }

                if (count == 0) {
                    candidate = value;
                    count = 1;
                } else {
                    tracker.incrementComparisons(1);
                    if (value == candidate) {
                        count++;
                    } else {
                        count--;
                    }
                }
            }

            int occurrences = 0;
            for (int i = 0; i < array.length; i++) {
                tracker.incrementArrayAccesses(1);
                tracker.incrementComparisons(1);
                if (array[i] == candidate) {
                    occurrences++;
                }
            }

            tracker.incrementComparisons(1);
            if (occurrences > array.length / 2) {
                return candidate;
            }
            return null;
        } finally {
            tracker.addExecutionTimeNs(System.nanoTime() - startNs);
        }
    }
}


