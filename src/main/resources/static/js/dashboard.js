document.addEventListener("DOMContentLoaded", () => {
  const latencyData = {
    labels: ["12:00", "12:05", "12:10", "12:15", "12:20", "12:25", "12:30"],
    datasets: [
      {
        label: "p99 Latency (ms)",
        data: [120, 145, 130, 210, 160, 140, 135],
        borderColor: "#38bdf8",
        backgroundColor: "rgba(56, 189, 248, 0.05)",
        fill: true,
        tension: 0.4,
        pointRadius: 4,
        pointHoverRadius: 6,
      },
      {
        label: "p95 Latency (ms)",
        data: [80, 95, 88, 110, 92, 85, 82],
        borderColor: "#34d399",
        backgroundColor: "transparent",
        fill: false,
        tension: 0.4,
        pointRadius: 4,
        pointHoverRadius: 6,
      },
    ],
  };

  const serviceData = {
    labels: ["Auth API", "Payment SVC", "Asset CDN"],
    datasets: [
      {
        data: [42, 28, 30],
        backgroundColor: [
          "rgba(56, 189, 248, 0.8)",
          "rgba(129, 140, 248, 0.8)",
          "rgba(52, 211, 153, 0.8)",
        ],
        hoverOffset: 10,
        borderWidth: 0,
      },
    ],
  };

  if (document.getElementById("latencyAnalysisChart")) {
    createChart("latencyAnalysisChart", "line", latencyData, {
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
    });
  }

  if (document.getElementById("servicePieChart")) {
    createChart("servicePieChart", "doughnut", serviceData, {
      plugins: {
        legend: { display: false },
      },
      cutout: "70%",
    });
  }

  // Auto-scroll logic for logic console
  const consoleEl = document.getElementById("logicConsole");
  if (consoleEl) {
    setInterval(() => {
      const logs = consoleEl.querySelectorAll("div");
      if (logs.length > 10) logs[0].remove();

      const newLog = document.createElement("div");
      const time = new Date().toLocaleTimeString("en-GB", { hour12: false });
      newLog.innerHTML = `[${time}] INFO: Automated health check pass on worker-${Math.floor(Math.random() * 20)}`;
      consoleEl.appendChild(newLog);
    }, 3000);
  }
});
