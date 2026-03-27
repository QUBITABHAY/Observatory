document.addEventListener("DOMContentLoaded", async () => {
  const fetchJson = async (url) => {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Request failed: ${url}`);
    return res.json();
  };

  const registerRowSelection = () => {
    const spanRows = document.querySelectorAll(".span-row");
    spanRows.forEach((row) => {
      row.addEventListener("click", () => {
        spanRows.forEach((r) => r.classList.remove("selected"));
        row.classList.add("selected");
      });
    });
  };

  try {
    const traces = await fetchJson("/api/traces");
    if (!traces.length) {
      registerRowSelection();
      return;
    }

    const trace = [...traces].sort(
      (a, b) => new Date(b.startedAt).getTime() - new Date(a.startedAt).getTime(),
    )[0];
    const spans = await fetchJson(`/api/spans?traceId=${encodeURIComponent(trace.traceId)}`);

    const titleEl = document.querySelector("h2.main-title");
    if (titleEl) {
      titleEl.innerHTML = `Trace: <span class="text-mono text-info">${trace.traceId}</span>`;
    }

    const descEl = document.querySelector("p.main-description");
    if (descEl) {
      descEl.innerHTML = `Total Duration: <span class="text-mono text-primary">${trace.duration}ms</span> | Spans: <span class="text-mono text-primary">${spans.length}</span>`;
    }

    const waterfall = document.querySelector(".waterfall-container");
    if (waterfall) {
      const max = Math.max(trace.duration || 1, ...spans.map((s) => s.duration || 0));
      waterfall.innerHTML = spans
        .sort((a, b) => (b.duration || 0) - (a.duration || 0))
        .map((s, idx) => {
          const width = Math.max(5, Math.round(((s.duration || 0) / max) * 100));
          const left = Math.min(90, (idx * 7) % 70);
          return `
            <div class="span-row">
              <span class="service-name">svc-${s.serviceId}</span>
              <div style="flex: 1; position: relative;">
                <div class="span-bar" style="width: ${width}%; left: ${left}%;">
                  <span class="span-label">${s.spanId} (${s.duration}ms)</span>
                </div>
              </div>
            </div>
          `;
        })
        .join("");
    }
  } catch (e) {
    console.error("Failed to load traces", e);
  }

  registerRowSelection();
});
