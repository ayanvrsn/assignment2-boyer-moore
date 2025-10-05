package cli;

import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Map<String, String> flags = parseFlags(args);
        int[] sizes = parseSizes(flags.getOrDefault("--sizes", "100,1000,10000,100000"));
        int trials = Integer.parseInt(flags.getOrDefault("--trials", "5"));
        Path outDir = Paths.get(flags.getOrDefault("--out", "docs/performance-plots"));
        Files.createDirectories(outDir);

        List<Integer> sizeList = new ArrayList<>();
        for (int s : sizes) sizeList.add(s);

        BenchmarkResult bm = benchmarkBoyerMoore(sizeList, trials, outDir);

        // Charts
        saveChart(outDir.resolve("bm_time.png"), "Boyer-Moore Time (ns)", sizeList, bm.timeNs);
        saveChart(outDir.resolve("bm_accesses.png"), "Boyer-Moore Array Accesses", sizeList, bm.arrayAccesses);
        saveChart(outDir.resolve("bm_comparisons.png"), "Boyer-Moore Comparisons", sizeList, bm.comparisons);

        System.out.println("Boyer–Moore benchmarking complete. Results in: " + outDir.toAbsolutePath());
    }

    private static class BenchmarkResult {
        List<Double> timeNs = new ArrayList<>();
        List<Double> arrayAccesses = new ArrayList<>();
        List<Double> comparisons = new ArrayList<>();
        List<Double> memory = new ArrayList<>();
    }

    private static BenchmarkResult benchmarkBoyerMoore(List<Integer> sizes, int trials, Path outDir) throws IOException {
        Path csv = outDir.resolve("boyer_moore.csv");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(csv.toFile()))) {
            w.write("n,trial,time_ns,array_accesses,comparisons,memory_bytes,found\n");
            BenchmarkResult agg = new BenchmarkResult();

            for (int n : sizes) {
                double tSum = 0, aSum = 0, cSum = 0, mSum = 0;
                for (int t = 0; t < trials; t++) {
                    int[] data = generateArrayForMajority(n);
                    PerformanceTracker tracker = PerformanceTracker.create();
                    Integer result = BoyerMooreMajorityVote.findMajorityElement(data, tracker);

                    w.write(n + "," + t + "," + tracker.getExecutionTimeNs() + "," +
                             tracker.getArrayAccesses() + "," + tracker.getComparisons() + "," +
                             tracker.getMemoryAllocatedBytes() + "," + (result != null) + "\n");

                    tSum += tracker.getExecutionTimeNs();
                    aSum += tracker.getArrayAccesses();
                    cSum += tracker.getComparisons();
                    mSum += tracker.getMemoryAllocatedBytes();
                }
                agg.timeNs.add(tSum / trials);
                agg.arrayAccesses.add(aSum / trials);
                agg.comparisons.add(cSum / trials);
                agg.memory.add(mSum / trials);
            }
            return agg;
        }
    }

    private static void saveChart(Path imagePath, String title, List<Integer> x, List<Double> y) throws IOException {
        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title(title)
                .xAxisTitle("n")
                .yAxisTitle("value")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.addSeries("avg", x, y);

        Files.createDirectories(imagePath.getParent());
        BitmapEncoder.saveBitmap(chart, imagePath.toString(), BitmapEncoder.BitmapFormat.PNG);
    }

    private static int[] parseSizes(String s) {
        String[] parts = s.split(",");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }
        return arr;
    }

    private static Map<String, String> parseFlags(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--")) {
                String val = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : "";
                map.put(a, val);
            }
        }
        return map;
    }

    private static int[] generateArrayForMajority(int n) {
        Random rnd = new Random(1234L + n);
        int[] arr = new int[n];
        int majority = rnd.nextInt(10);
        int majorityCount = n / 2 + (n % 2 == 0 ? 0 : 1);

        for (int i = 0; i < majorityCount && i < n; i++) {
            arr[i] = majority;
        }
        for (int i = majorityCount; i < n; i++) {
            arr[i] = rnd.nextInt(10);
        }
        shuffle(arr, rnd);
        return arr;
    }

    private static void shuffle(int[] arr, Random rnd) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
}
