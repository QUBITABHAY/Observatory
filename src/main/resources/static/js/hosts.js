document.addEventListener("DOMContentLoaded", () => {

  const render = async () => {
    const [hosts, metrics] = await Promise.all([fetchJson("/api/hosts"), fetchJson("/api/metrics")]);
    const latestByHost = new Map();

    [...metrics]
      .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
      .forEach((m) => {
        if (m.host?.id && !latestByHost.has(m.host.id)) {
          latestByHost.set(m.host.id, m);
        }
      });

    const tbody = document.querySelector(".data-table tbody");
    if (!tbody) return;

    tbody.innerHTML = hosts
      .map((h) => {
        const metric = latestByHost.get(h.id);
        const value = metric ? Math.round(metric.value) : 0;
        const metricLabel = metric?.metricName || "load";
        const barColor = value >= 80 ? "#fb7185" : value >= 60 ? "#fbbf24" : "var(--accent-blue)";
        return `
          <tr style="border-bottom: 1px solid var(--card-border);">
            <td class="text-mono fw-semibold text-info" style="padding: 1.25rem 1.5rem;">${h.hostname}</td>
            <td class="text-mono text-secondary">${h.ipAddress}</td>
            <td><span class="badge" style="background: rgba(129, 140, 248, 0.1); color: #818cf8;">${h.environment || "n/a"}</span></td>
            <td><span class="status-indicator text-success"><i class="fas fa-circle"></i> Online</span></td>
            <td class="font-small">${h.environment || "n/a"}</td>
            <td>
              <div class="progress-bar-container">
                <div class="progress-bar" style="width: ${Math.min(100, value)}%; background: ${barColor};"></div>
              </div>
              <span class="text-mono text-secondary" style="font-size: 0.65rem;">${value}% ${metricLabel}</span>
            </td>
            <td class="text-right" style="padding-right: 2rem;">
              <button class="btn btn-outline btn-small"><i class="fas fa-external-link-alt"></i></button>
            </td>
          </tr>
        `;
      })
      .join("");

  };

  startLiveUpdates(render, 5000);
});
