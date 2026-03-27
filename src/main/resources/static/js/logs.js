document.addEventListener("DOMContentLoaded", async () => {
  const fetchJson = async (url) => {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Request failed: ${url}`);
    return res.json();
  };

  const liveToggle = document.getElementById("liveToggle");
  const logContainer = document.getElementById("logContainer");

  if (liveToggle) {
    liveToggle.addEventListener("click", function () {
      this.classList.toggle("active");
      const dot = this.querySelector("i");
      if (this.classList.contains("active")) {
        dot.style.color = "var(--accent-green)";
        this.innerHTML =
          '<i class="fas fa-circle" style="color: var(--accent-green); font-size: 0.5rem; margin-right: 0.5rem;"></i> Live';
      } else {
        dot.style.color = "var(--accent-red)";
        this.innerHTML =
          '<i class="fas fa-circle" style="color: var(--accent-red); font-size: 0.5rem; margin-right: 0.5rem;"></i> Paused';
      }
    });
  }

  if (logContainer) {
    try {
      const logs = await fetchJson("/api/logs");
      const levelColors = {
        ERROR: "#fb7185",
        WARN: "#fbbf24",
        INFO: "#34d399",
        DEBUG: "#38bdf8",
      };

      const rows = [...logs]
        .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
        .slice(0, 200)
        .map((l) => {
          const ts = new Date(l.timestamp).toLocaleString("en-GB", { hour12: false });
          const level = (l.level || "INFO").toUpperCase();
          const color = levelColors[level] || "#94a3b8";
          return `
            <div class="log-row">
              <span class="log-ts">${ts}</span>
              <span class="log-level" style="color: ${color};">${level}</span>
              <span class="log-service">svc-${l.serviceId}</span>
              <span class="log-msg">${l.message}</span>
            </div>
          `;
        })
        .join("");

      logContainer.innerHTML = rows || '<div class="log-row"><span class="log-msg">No logs yet.</span></div>';
    } catch (e) {
      console.error("Failed to load logs", e);
    }

    logContainer.scrollTop = 0;
  }
});
