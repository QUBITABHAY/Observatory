document.addEventListener("DOMContentLoaded", () => {

  const postAction = async (url) => {
    const res = await fetch(url, { method: "POST" });
    if (!res.ok) throw new Error(`Action failed: ${url}`);
    return res.json();
  };

  const hourBuckets = () => {
    const labels = [];
    for (let i = 0; i < 8; i += 1) {
      labels.push(`${String(i * 3).padStart(2, "0")}:00`);
    }
    return labels;
  };

  const render = async () => {
    const alerts = await fetchJson("/api/alerts");

    const labels = hourBuckets();
    const counts = Array(labels.length).fill(0);
    alerts.forEach((a) => {
      const hour = new Date(a.triggeredAt).getHours();
      const bucket = Math.min(7, Math.floor(hour / 3));
      counts[bucket] += 1;
    });

    const alertsTrendChart = document.getElementById("alertsTrendChart");
    if (alertsTrendChart) {
      createChart(
        "alertsTrendChart",
        "line",
        {
          labels,
          datasets: [
            {
              label: "Triggered Alerts",
              data: counts,
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

    const tbody = document.querySelector(".data-table tbody");
    if (tbody) {
      const rows = [...alerts]
        .sort((a, b) => new Date(b.triggeredAt).getTime() - new Date(a.triggeredAt).getTime())
        .map((a) => {
          const sev = (a.severity || "info").toLowerCase();
          const actions = a.resolved
            ? "<span class=\"text-success\">Resolved</span>"
            : `<button class=\"btn btn-outline btn-small action-resolve\" data-id=\"${a.id}\">Resolve</button>
               <button class=\"btn btn-primary btn-small action-silence\" data-id=\"${a.id}\">Silence</button>`;

          return `
            <tr style="border-bottom: 1px solid var(--card-border);">
              <td class="text-mono font-small text-secondary" style="padding: 1.25rem 1.5rem;">${new Date(a.triggeredAt).toLocaleString()}</td>
              <td><span class="badge badge-${sev}">${a.severity}</span></td>
              <td class="fw-medium font-outfit">${a.message}</td>
              <td class="text-mono font-small"><span class="text-info">service</span> / ${a.serviceId}</td>
              <td class="text-right" style="padding-right: 2rem;"><div class="fw-flex-center" style="gap: 0.5rem; justify-content: flex-end;">${actions}</div></td>
            </tr>
          `;
        })
        .join("");
      tbody.innerHTML = rows || "";
    }

    document.querySelectorAll(".action-resolve").forEach((btn) => {
      btn.addEventListener("click", async () => {
        await postAction(`/api/alerts/${btn.dataset.id}/resolve`);
        await render();
      });
    });

    document.querySelectorAll(".action-silence").forEach((btn) => {
      btn.addEventListener("click", async () => {
        await postAction(`/api/alerts/${btn.dataset.id}/silence`);
        await render();
      });
    });
  };

  startLiveUpdates(render, 5000);
});
