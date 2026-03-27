document.addEventListener("DOMContentLoaded", () => {

  let selectedSpan = null;
  let servicesLoaded = false;
  let serviceMap = new Map();

  const safeFetch = async (url) => {
    try {
      return await fetchJson(url);
    } catch (e) {
      console.error(`Failed to fetch ${url}`, e);
      return [];
    }
  };

  const loadServices = async () => {
    if (servicesLoaded) return;
    const services = await safeFetch("/api/services");
    serviceMap = new Map(services.map((s) => [s.id, s.name]));

    const select = document.getElementById("serviceFilter");
    if (select && services.length) {
      select.innerHTML = '<option>All Services</option>' +
        services.map((s) => `<option value="${s.id}">${s.name}</option>`).join("");
    }
    servicesLoaded = true;
  };

  const getServiceName = (serviceId) => serviceMap.get(serviceId) || `svc-${serviceId}`;

  const updateMetadataPanel = (span) => {
    const nameEl = document.getElementById("metaSpanName");
    const durEl = document.getElementById("metaDuration");
    const statusEl = document.getElementById("metaStatus");
    const attrEl = document.getElementById("metaAttributes");

    if (nameEl) nameEl.textContent = span ? `${getServiceName(span.serviceId)}: ${span.spanId}` : "Select a span";
    if (durEl) durEl.textContent = span ? `${span.duration}ms` : "--";
    if (statusEl) statusEl.textContent = span ? "OK" : "--";
    if (attrEl) {
      if (!span) {
        attrEl.innerHTML = '<div class="text-secondary font-small">No span selected</div>';
        return;
      }
      const attrs = [
        ["span.id", span.spanId],
        ["parent.span.id", span.parentSpanId || "root"],
        ["service.name", getServiceName(span.serviceId)],
        ["trace.id", span.traceId],
      ];
      attrEl.innerHTML = attrs
        .map(([key, val]) => `
          <div style="display: flex; justify-content: space-between; font-size: 0.75rem; padding-bottom: 0.5rem; border-bottom: 1px solid var(--card-border);">
            <span class="text-secondary">${key}</span>
            <span class="text-mono">${val}</span>
          </div>
        `).join("");
    }
  };

  const registerRowSelection = () => {
    const spanRows = document.querySelectorAll(".span-row");
    spanRows.forEach((row) => {
      row.addEventListener("click", () => {
        spanRows.forEach((r) => r.classList.remove("selected"));
        row.classList.add("selected");
        if (row._spanData) {
          updateMetadataPanel(row._spanData);
        }
      });
    });
  };

  const render = async () => {
    await loadServices();

    const traces = await safeFetch("/api/traces");
    if (!traces.length) {
      const titleEl = document.getElementById("traceIdDisplay");
      if (titleEl) titleEl.textContent = "No traces";
      const recentList = document.getElementById("recentTracesList");
      if (recentList) recentList.innerHTML = '<div class="text-secondary font-small">No traces yet.</div>';
      registerRowSelection();
      return;
    }

    const sortedTraces = [...traces].sort(
      (a, b) => new Date(b.startedAt).getTime() - new Date(a.startedAt).getTime(),
    );

    // --- Recent Traces Sidebar ---
    const recentList = document.getElementById("recentTracesList");
    if (recentList) {
      const recent = sortedTraces.slice(0, 5);
      recentList.innerHTML = recent
        .map((t) => `
          <div class="glass trace-list-item" style="padding: 0.75rem; border-radius: 8px; font-size: 0.75rem; cursor: pointer;" data-trace-id="${t.traceId}">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.25rem;">
              <span class="text-mono text-info">${t.traceId.substring(0, 10)}...</span>
              <span style="color: ${t.duration > 500 ? 'var(--accent-red)' : 'var(--accent-green)'};">${t.duration}ms</span>
            </div>
            <div class="text-secondary">${getServiceName(t.serviceId)}</div>
          </div>
        `).join("");
    }

    const trace = sortedTraces[0];
    const spans = await safeFetch(`/api/spans?traceId=${encodeURIComponent(trace.traceId)}`);

    // --- Trace Header ---
    const titleEl = document.getElementById("traceIdDisplay");
    if (titleEl) titleEl.textContent = trace.traceId;

    const durEl = document.getElementById("traceDuration");
    if (durEl) durEl.textContent = `${trace.duration}ms`;

    const spanCountEl = document.getElementById("traceSpanCount");
    if (spanCountEl) spanCountEl.textContent = String(spans.length);

    // --- Timeline Header ---
    const timelineHeader = document.getElementById("timelineHeader");
    if (timelineHeader && trace.duration) {
      const step = Math.ceil(trace.duration / 5);
      const labels = [];
      for (let i = 0; i <= 5; i++) {
        labels.push(`${i * step}ms`);
      }
      timelineHeader.innerHTML = labels
        .map((l) => `<div style="flex: 1;">${l}</div>`)
        .join("");
    }

    // --- Waterfall ---
    const waterfall = document.querySelector(".waterfall-container");
    if (waterfall) {
      const max = Math.max(trace.duration || 1, ...spans.map((s) => s.duration || 0));
      const sortedSpans = [...spans].sort((a, b) => (b.duration || 0) - (a.duration || 0));
      waterfall.innerHTML = sortedSpans
        .map((s, idx) => {
          const width = Math.max(5, Math.round(((s.duration || 0) / max) * 100));
          const left = Math.min(90, (idx * 7) % 70);
          const svcName = getServiceName(s.serviceId);
          return `
            <div class="span-row" data-span-index="${idx}">
              <span class="service-name">${svcName}</span>
              <div style="flex: 1; position: relative;">
                <div class="span-bar" style="width: ${width}%; left: ${left}%;">
                  <span class="span-label">${s.spanId} (${s.duration}ms)</span>
                </div>
              </div>
            </div>
          `;
        })
        .join("");

      // Attach span data to rows for metadata panel
      const rows = waterfall.querySelectorAll(".span-row");
      rows.forEach((row, idx) => {
        row._spanData = sortedSpans[idx];
      });

      // Auto-select first span
      if (sortedSpans.length) {
        updateMetadataPanel(sortedSpans[0]);
        if (rows[0]) rows[0].classList.add("selected");
      }
    }

    registerRowSelection();
  };

  startLiveUpdates(render, 5000);
});
