## Assignment 2 — Linear Array Algorithms (Pair 3)

This project implements and benchmarks linear-time array algorithm with full testing, metrics, CLI benchmarking, and PDF reports:

- Boyer–Moore Majority Vote

### Repository structure

```
assignment2-linear-algorithms/
├── src/
│   ├── main/java/algorithms/
│   │   ├── BoyerMooreMajorityVote.java
│   ├── main/java/metrics/PerformanceTracker.java
│   ├── main/java/cli/BenchmarkRunner.java
│   └── test/java/algorithms/
│       ├── BoyerMooreMajorityVoteTest.java
|
├── docs/
│   ├── analysis-report-A.pdf
│   └── performance-plots/
├── README.md
└── pom.xml
```

### Build and test

```bash
mvn clean test
```

### Run benchmarks

```bash
mvn clean package
java -jar target/assignment2-linear-algorithms-1.0.0-SNAPSHOT.jar \
  --sizes 100,1000,10000,100000 \
  --trials 5 \
  --out docs/performance-plots
```

This will generate CSVs and plots in `docs/performance-plots/`.

### Branching and releases

Branches:
- `main`
- `feature/boyer-moore`
- `feature/kadane`
- `feature/metrics`
- `feature/testing`
- `feature/cli`
- `feature/optimization`

Conventional commit examples:
- `init: project setup with maven and junit`
- `feat(algorithm): implement Boyer-Moore Majority Vote`
- `test(algorithm): add edge case tests`
- `feat(metrics): performance counters and CSV export`
- `feat(cli): add benchmark runner`
- `docs(report): add analysis-report.pdf`
- `release: v1.0 complete submission`
