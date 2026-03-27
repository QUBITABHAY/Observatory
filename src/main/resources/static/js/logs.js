document.addEventListener("DOMContentLoaded", () => {

  const liveToggle = document.getElementById("liveToggle");
  const logContainer = document.getElementById("logContainer");

  let stopUpdates = null;

  const render = async () => {
    if (!logContainer) return;

    const [logs, services] = await Promise.all([
      fetchJson("/api/logs"),
      fetchJson("/api/services"),
    ]);

    const serviceMap = new Map(services.map((s) => [s.id, s.name]));

    const levelColors = {
      ERROR: "#fb7185",
      WARN: "#fbbf24",
      INFO: "#34d399",
      DEBUG: "#38bdf8",
    };

    const displayed = [...logs]
      .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
      .slice(0, 200);

    const rows = displayed
      .map((l) => {
        const ts = new Date(l.timestamp).toLocaleString("en-GB", { hour12: false });
        const level = (l.level || "INFO").toUpperCase();
        const color = levelColors[level] || "#94a3b8";
        const serviceName = serviceMap.get(l.serviceId) || `svc-${l.serviceId}`;
        return `
          <div class="log-row">
            <span class="log-ts">${ts}</span>
            <span class="log-level" style="color: ${color};">${level}</span>
            <span class="log-service">${serviceName}</span>
            <span class="log-msg">${l.message}</span>
          </div>
        `;
      })
      .join("");

    logContainer.innerHTML = rows || '<div class="log-row"><span class="log-msg">No logs yet.</span></div>';
    logContainer.scrollTop = 0;

    // Update streaming stats
    const streamMetric = document.getElementById("streamMetric");
    const bufferMetric = document.getElementById("bufferMetric");
    if (streamMetric) streamMetric.textContent = `Streaming: ${logs.length} events`;
    if (bufferMetric) bufferMetric.textContent = `Buffer: ${displayed.length} lines`;
  };

  if (liveToggle) {
    liveToggle.addEventListener("click", function () {
      this.classList.toggle("active");
      if (this.classList.contains("active")) {
        this.innerHTML =
          '<i class="fas fa-circle" style="color: var(--accent-green); font-size: 0.5rem; margin-right: 0.5rem;"></i> Live';
        if (!stopUpdates) {
          stopUpdates = startLiveUpdates(render, 2000);
        }
      } else {
        this.innerHTML =
          '<i class="fas fa-circle" style="color: var(--accent-red); font-size: 0.5rem; margin-right: 0.5rem;"></i> Paused';
        if (stopUpdates) {
          stopUpdates();
          stopUpdates = null;
        }
      }
    });
  }

  if (logContainer) {
    stopUpdates = startLiveUpdates(render, 2000);
  }
});
