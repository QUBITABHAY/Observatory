document.addEventListener("DOMContentLoaded", async () => {
  const fetchJson = async (url) => {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Request failed: ${url}`);
    return res.json();
  };

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

  try {
    const metrics = await fetchJson("/api/metrics");
    const sorted = [...metrics].sort(
      (a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime(),
    );
    const recent = sorted.slice(-60);

    const heroCanvas = document.getElementById("heroMetricsChart");
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
          labels: recent.map((m) => new Date(m.timestamp).toLocaleTimeString("en-GB", { hour12: false })),
          datasets: [
            {
              label: "System Load",
              data: recent.map((m) => m.value),
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

    const grouped = [...groupByMetricName(recent).values()]
      .map((arr) => arr.slice(-20).map((m) => m.value))
      .filter((arr) => arr.length > 0)
      .slice(0, 6);

    const fallback = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    initSparkline("spark-1", "#38bdf8", grouped[0] || fallback);
    initSparkline("spark-2", "#818cf8", grouped[1] || fallback);
    initSparkline("spark-3", "#34d399", grouped[2] || fallback);
    initSparkline("spark-4", "#fbbf24", grouped[3] || fallback);
    initSparkline("spark-5", "#38bdf8", grouped[4] || fallback);
    initSparkline("spark-6", "#fb7185", grouped[5] || fallback);
  } catch (e) {
    console.error("Failed to load metrics", e);
  }
});
