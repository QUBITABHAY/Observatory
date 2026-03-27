document.addEventListener("DOMContentLoaded", async () => {
  const fetchJson = async (url) => {
    const res = await fetch(url);
    if (!res.ok) {
      throw new Error(`Request failed: ${url}`);
    }
    return res.json();
  };

  const percentile = (values, p) => {
    if (!values.length) return 0;
    const sorted = [...values].sort((a, b) => a - b);
    const idx = Math.min(sorted.length - 1, Math.ceil((p / 100) * sorted.length) - 1);
    return Math.round(sorted[Math.max(0, idx)]);
  };

  const groupIntoWindows = (items, windows) => {
    if (!items.length) return [];
    const size = Math.max(1, Math.ceil(items.length / windows));
    const out = [];
    for (let i = 0; i < items.length; i += size) {
      out.push(items.slice(i, i + size));
    }
    return out;
  };

  try {
    const [traces, services, logs, alerts] = await Promise.all([
      fetchJson("/api/traces"),
      fetchJson("/api/services"),
      fetchJson("/api/logs"),
      fetchJson("/api/alerts"),
    ]);

    const tracesSorted = [...traces].sort(
      (a, b) => new Date(a.startedAt).getTime() - new Date(b.startedAt).getTime(),
    );
    const windows = groupIntoWindows(tracesSorted, 8);
    const latencyData = {
      labels: windows.map((_, i) => `w${i + 1}`),
      datasets: [
        {
          label: "p99 Latency (ms)",
          data: windows.map((w) => percentile(w.map((t) => t.duration || 0), 99)),
          borderColor: "#38bdf8",
          backgroundColor: "rgba(56, 189, 248, 0.05)",
          fill: true,
          tension: 0.4,
          pointRadius: 4,
          pointHoverRadius: 6,
        },
        {
          label: "p95 Latency (ms)",
          data: windows.map((w) => percentile(w.map((t) => t.duration || 0), 95)),
          borderColor: "#34d399",
          backgroundColor: "transparent",
          fill: false,
          tension: 0.4,
          pointRadius: 4,
          pointHoverRadius: 6,
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

    const serviceById = new Map(services.map((s) => [s.id, s.name]));
    const traceCounts = new Map();
    traces.forEach((t) => {
      const key = serviceById.get(t.serviceId) || `svc-${t.serviceId}`;
      traceCounts.set(key, (traceCounts.get(key) || 0) + 1);
    });
    const top = [...traceCounts.entries()].sort((a, b) => b[1] - a[1]).slice(0, 5);
    const serviceData = {
      labels: top.map((x) => x[0]),
      datasets: [
        {
          data: top.map((x) => x[1]),
          backgroundColor: [
            "rgba(56, 189, 248, 0.8)",
            "rgba(129, 140, 248, 0.8)",
            "rgba(52, 211, 153, 0.8)",
            "rgba(251, 191, 36, 0.8)",
            "rgba(251, 113, 133, 0.8)",
          ],
          hoverOffset: 10,
          borderWidth: 0,
        },
      ],
    };

    if (document.getElementById("servicePieChart")) {
      createChart("servicePieChart", "doughnut", serviceData, {
        plugins: {
          legend: { display: false },
        },
        cutout: "70%",
      });
    }

    const consoleEl = document.getElementById("logicConsole");
    if (consoleEl) {
      const recentLogs = [...logs]
        .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
        .slice(0, 12);
      consoleEl.innerHTML = recentLogs
        .map((l) => {
          const time = new Date(l.timestamp).toLocaleTimeString("en-GB", { hour12: false });
          return `<div>[${time}] ${l.level}: ${l.message}</div>`;
        })
        .join("");
    }

    const tbody = document.querySelector(".data-table tbody");
    if (tbody) {
      const recentAlerts = [...alerts]
        .sort((a, b) => new Date(b.triggeredAt).getTime() - new Date(a.triggeredAt).getTime())
        .slice(0, 5);
      tbody.innerHTML = recentAlerts
        .map((a) => {
          const when = new Date(a.triggeredAt).toLocaleString();
          const status = a.resolved ? "Resolved" : a.silenced ? "Silenced" : "Open";
          return `
            <tr>
              <td class="text-secondary">${when}</td>
              <td><span class="badge badge-${(a.severity || "info").toLowerCase()}">${a.severity}</span></td>
              <td>${a.message}</td>
              <td>${a.serviceId}</td>
              <td>${status}</td>
            </tr>
          `;
        })
        .join("");
    }
  } catch (e) {
    console.error("Failed to load dashboard data", e);
  }
});
