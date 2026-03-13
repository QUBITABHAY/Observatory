document.addEventListener("DOMContentLoaded", () => {
  const alertsTrendChart = document.getElementById("alertsTrendChart");
  if (alertsTrendChart) {
    createChart(
      "alertsTrendChart",
      "line",
      {
        labels: ["00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "23:59"],
        datasets: [
          {
            label: "Triggered Alerts",
            data: [5, 2, 8, 15, 12, 6, 4],
            borderColor: "#fb7185",
            backgroundColor: "rgba(251, 113, 133, 0.05)",
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: "#fb7185",
          },
        ],
      },
      {
        scales: {
          y: {
            beginAtZero: true,
            grid: { color: "rgba(255, 255, 255, 0.03)" },
            border: { display: false },
            ticks: {
              color: "#64748b",
              font: { family: "JetBrains Mono", size: 10 },
            },
          },
          x: {
            grid: { display: false },
            border: { display: false },
            ticks: { color: "#64748b", font: { size: 11 } },
          },
        },
        plugins: {
          legend: { display: false },
        },
      },
    );
  }
});
