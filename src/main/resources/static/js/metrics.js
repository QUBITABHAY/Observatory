document.addEventListener("DOMContentLoaded", () => {
  const createGradient = (ctx, colorStart, colorEnd) => {
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, colorStart);
    gradient.addColorStop(1, colorEnd);
    return gradient;
  };

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
        labels: Array.from({ length: 60 }, (_, i) => `${i}m ago`).reverse(),
        datasets: [
          {
            label: "System Load",
            data: Array.from({ length: 60 }, () =>
              Math.floor(Math.random() * 40 + 30),
            ),
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

  const initSparkline = (id, color) => {
    if (!document.getElementById(id)) return;
    createChart(
      id,
      "line",
      {
        labels: Array.from({ length: 20 }, (_, i) => i),
        datasets: [
          {
            data: Array.from({ length: 20 }, () =>
              Math.floor(Math.random() * 50 + 20),
            ),
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

  initSparkline("spark-1", "#38bdf8");
  initSparkline("spark-2", "#818cf8");
  initSparkline("spark-3", "#34d399");
  initSparkline("spark-4", "#fbbf24");
  initSparkline("spark-5", "#38bdf8");
  initSparkline("spark-6", "#fb7185");
});
