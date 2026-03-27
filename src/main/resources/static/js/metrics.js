document.addEventListener("DOMContentLoaded", () => {

  const createGradient = (ctx, colorStart, colorEnd) => {
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, colorStart);
    gradient.addColorStop(1, colorEnd);
    return gradient;
  };

  const groupByMetricName = (metrics) => {
    const grouped = new Map();
    metrics.forEach((m) => {
      const key = m.metricName || "unknown";
      if (!grouped.has(key)) grouped.set(key, []);
      grouped.get(key).push(m);
    });
    return grouped;
  };

  const percentile = (values, p) => {
    if (!values.length) return 0;
    const sorted = [...values].sort((a, b) => a - b);
    const idx = Math.min(
      sorted.length - 1,
      Math.ceil((p / 100) * sorted.length) - 1,
    );
    return sorted[Math.max(0, idx)];
  };

  const median = (values) => {
    if (!values.length) return 0;
    const sorted = [...values].sort((a, b) => a - b);
    const mid = Math.floor(sorted.length / 2);
    return sorted.length % 2 !== 0
      ? sorted[mid]
      : (sorted[mid - 1] + sorted[mid]) / 2;
  };

  const formatBytes = (bytes) => {
    if (bytes >= 1073741824) return `${(bytes / 1073741824).toFixed(1)} GB`;
    if (bytes >= 1048576) return `${(bytes / 1048576).toFixed(1)} MB`;
    if (bytes >= 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${bytes} B`;
  };

  const initSparkline = (id, color, values) => {
    if (!document.getElementById(id)) return;
    createChart(
      id,
      "line",
      {
        labels: values.map((_, i) => i + 1),
        datasets: [
          {
            data: values,
            borderColor: color,
            borderWidth: 1.5,
            pointRadius: 0,
            tension: 0.4,
            fill: false,
          },
        ],
      },
      {
        scales: { y: { display: false }, x: { display: false } },
        plugins: { legend: { display: false } },
        animation: false,
      },
    );
  };

  const setText = (id, text) => {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  };

  // Find a metric group by exact name match or prefix match
  const findGroup = (grouped, ...candidates) => {
    for (const candidate of candidates) {
      const entry = [...grouped.entries()].find(([k]) => k === candidate);
      if (entry) return entry[1];
    }
    // Fallback: prefix match
    for (const candidate of candidates) {
      const entry = [...grouped.entries()].find(([k]) => k.startsWith(candidate));
      if (entry) return entry[1];
    }
    return [];
  };

  const avgOf = (arr) => {
    if (!arr.length) return 0;
    return arr.reduce((s, m) => s + (m.value || 0), 0) / arr.length;
  };

  const latestOf = (arr) => {
    if (!arr.length) return 0;
    return arr[arr.length - 1].value || 0;
  };

  const render = async () => {
    const metrics = await fetchJson("/api/metrics");
    const sorted = [...metrics].sort(
      (a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime(),
    );
    const recent = sorted.slice(-60);

    // --- Group metrics by name ---
    const grouped = groupByMetricName(recent);

    // --- Insight Cards ---
    // CPU: prefer cpu_usage_percent, fallback to cpu_usage
    const cpuMetrics = findGroup(grouped, "cpu_usage_percent", "cpu_usage");
    const cpuAvg = avgOf(cpuMetrics);
    const cpuCount = cpuMetrics.length;
    setText("insightCpuValue", cpuCount ? `${cpuAvg.toFixed(1)}%` : "--");
    setText("insightCpuStatus", cpuCount ? `${cpuCount} samples` : "No data");

    // Memory: prefer memory_usage_percent, show used bytes as context
    const memPercentMetrics = findGroup(grouped, "memory_usage_percent");
    const memBytesMetrics = findGroup(grouped, "memory_used_bytes");
    if (memPercentMetrics.length) {
      const memAvg = avgOf(memPercentMetrics);
      setText("insightMemValue", `${memAvg.toFixed(1)}%`);
      const latestBytes = memBytesMetrics.length ? latestOf(memBytesMetrics) : 0;
      setText("insightMemStatus", latestBytes ? formatBytes(latestBytes) + " used" : `${memPercentMetrics.length} samples`);
    } else if (memBytesMetrics.length) {
      setText("insightMemValue", formatBytes(avgOf(memBytesMetrics)));
      setText("insightMemStatus", `${memBytesMetrics.length} samples`);
    } else {
      setText("insightMemValue", "--");
      setText("insightMemStatus", "No data");
    }

    // Disk I/O: look for disk or io metrics
    const ioMetrics = findGroup(grouped, "disk_io_read", "disk_io_write", "disk_io", "io");
    const ioCount = ioMetrics.length;
    if (ioCount) {
      const ioAvg = avgOf(ioMetrics);
      setText("insightIoValue", ioAvg > 1048576 ? `${(ioAvg / 1048576).toFixed(1)} MB/s` : `${ioAvg.toFixed(1)}`);
    } else {
      setText("insightIoValue", "--");
    }
    setText("insightIoStatus", ioCount ? `${ioCount} samples` : "No data");

    // Threads: look for thread metrics, fallback to total metric count
    const threadMetrics = findGroup(grouped, "thread", "active_threads");
    const threadCount = threadMetrics.length;
    if (threadCount) {
      setText("insightThreadsValue", Math.round(latestOf(threadMetrics)).toLocaleString());
    } else {
      setText("insightThreadsValue", String(recent.length));
    }
    setText("insightThreadsStatus", threadCount ? `${threadCount} samples` : `${recent.length} total metrics`);

    // --- Hero Stats (use CPU-related % metrics for P99/Median) ---
    const percentMetrics = [...(findGroup(grouped, "cpu_usage_percent", "cpu_usage")),
                            ...(findGroup(grouped, "memory_usage_percent"))];
    const percentValues = percentMetrics.map((m) => m.value || 0);
    const allValues = recent.map((m) => m.value || 0); // for chart
    const p99 = percentValues.length ? percentile(percentValues, 99) : 0;
    const med = percentValues.length ? median(percentValues) : 0;
    setText("heroP99", percentValues.length ? `${p99.toFixed(1)}%` : "--");
    setText("heroMedian", percentValues.length ? `${med.toFixed(1)}%` : "--");
    setText("heroSamples", recent.length ? (recent.length >= 1000 ? `${(recent.length / 1000).toFixed(1)}K` : `${recent.length}`) : "--");

    // --- Hero Chart (use CPU metrics for a clean chart) ---
    const heroCanvas = document.getElementById("heroMetricsChart");
    const chartMetrics = cpuMetrics.length ? cpuMetrics : recent;
    if (heroCanvas) {
      const heroCtx = heroCanvas.getContext("2d");
      const heroGradient = createGradient(
        heroCtx,
        "rgba(56, 189, 248, 0.2)",
        "rgba(56, 189, 248, 0)",
      );

      createChart(
        "heroMetricsChart",
        "line",
        {
          labels: chartMetrics.map((m) => new Date(m.timestamp).toLocaleTimeString("en-GB", { hour12: false })),
          datasets: [
            {
              label: "CPU Usage %",
              data: chartMetrics.map((m) => m.value),
              borderColor: "#38bdf8",
              backgroundColor: heroGradient,
              fill: true,
              tension: 0.4,
              pointRadius: 0,
              borderWidth: 2,
            },
          ],
        },
        {
          scales: {
            y: {
              grid: { color: "rgba(255,255,255,0.03)" },
              border: { display: false },
              ticks: {
                color: "#64748b",
                font: { family: "JetBrains Mono", size: 10 },
                callback: (v) => `${v}%`,
              },
            },
            x: {
              grid: { display: false },
              border: { display: false },
              ticks: { display: false },
            },
          },
          plugins: { legend: { display: false } },
        },
      );
    }

    // --- Sparklines ---
    const sparkGroups = [...grouped.entries()].slice(0, 6);
    const fallback = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    const sparkColors = ["#38bdf8", "#818cf8", "#34d399", "#fbbf24", "#38bdf8", "#fb7185"];

    for (let i = 0; i < 6; i++) {
      const group = sparkGroups[i];
      const vals = group ? group[1].slice(-20).map((m) => m.value) : fallback;
      const name = group ? group[0] : "";
      initSparkline(`spark-${i + 1}`, sparkColors[i], vals.length ? vals : fallback);

      // Format value based on metric name
      const latest = vals.length ? vals[vals.length - 1] : null;
      let display = "--";
      if (latest !== null) {
        if (name.includes("bytes")) {
          display = formatBytes(latest);
        } else if (name.includes("percent") || name.includes("cpu_usage")) {
          display = `${Number(latest).toFixed(1)}%`;
        } else {
          display = `${Number(latest).toFixed(1)}`;
        }
      }
      setText(`sparkVal${i + 1}`, display);

      // Update sparkline title to show actual metric name
      const titleEl = document.querySelector(`#spark-${i + 1}`)?.closest(".mini-chart-card")?.querySelector(".mini-chart-title");
      if (titleEl && name) {
        titleEl.textContent = name.toUpperCase().replace(/_/g, " ");
      }
    }
  };

  startLiveUpdates(render, 5000);
});
