package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoyerMooreMajorityVoteTest {

    @Test
    void emptyArrayReturnsNull() {
        Integer res = BoyerMooreMajorityVote.findMajorityElement(new int[]{}, PerformanceTracker.create());
        assertNull(res);
    }

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override public void execute() { BoyerMooreMajorityVote.findMajorityElement(null, PerformanceTracker.create()); }
        });
    }

    @Test
    void singleElementIsMajority() {
        Integer res = BoyerMooreMajorityVote.findMajorityElement(new int[]{7}, PerformanceTracker.create());
        assertEquals(7, res);
    }

    @Test
    void majorityDetected() {
        Integer res = BoyerMooreMajorityVote.findMajorityElement(new int[]{2,2,1,2,3,2,2}, PerformanceTracker.create());
        assertEquals(2, res);
    }

    @Test
    void noMajorityReturnsNull() {
        Integer res = BoyerMooreMajorityVote.findMajorityElement(new int[]{1,2,3,4}, PerformanceTracker.create());
        assertNull(res);
    }

    @Test
    void randomizedProperty() {
        Random rnd = new Random(0);
        for (int n = 1; n <= 200; n++) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(5);
            PerformanceTracker tracker = PerformanceTracker.create();
            Integer res = BoyerMooreMajorityVote.findMajorityElement(arr, tracker);
            // Verify by counting
            if (res != null) {
                int count = 0;
                for (int v : arr) if (v == res) count++;
                assertTrue(count > n / 2);
            }
        }
    }
}


