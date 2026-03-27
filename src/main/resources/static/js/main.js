document.addEventListener("DOMContentLoaded", () => {
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll(".nav-item");
  navLinks.forEach((link) => {
    if (link.getAttribute("href") === currentPath) {
      link.classList.add("active");
    }
  });

  const sidebar = document.querySelector(".sidebar");
  const toggleBtn = document.getElementById("sidebarToggle");

  if (localStorage.getItem("sidebarCollapsed") === "true") {
    sidebar.classList.add("collapsed");
  }

  if (toggleBtn) {
    toggleBtn.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");
      localStorage.setItem(
        "sidebarCollapsed",
        sidebar.classList.contains("collapsed"),
      );

      window.dispatchEvent(new Event("resize"));
    });
  }

  const cards = document.querySelectorAll(".glass");
  cards.forEach((card, index) => {
    card.style.opacity = "0";
    card.style.transform = "translateY(10px)";
    setTimeout(() => {
      card.style.transition = "all 0.4s ease-out";
      card.style.opacity = "1";
      card.style.transform = "translateY(0)";
    }, index * 50);
  });
});

const createChart = (canvasId, type, data, options) => {
  if (!window.Chart) return null;
  const canvas = document.getElementById(canvasId);
  if (!canvas) return null;

  window.__chartRegistry = window.__chartRegistry || {};
  if (window.__chartRegistry[canvasId]) {
    window.__chartRegistry[canvasId].destroy();
  }

  const ctx = canvas.getContext("2d");
  const chart = new Chart(ctx, {
    type: type,
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      ...options,
    },
  });

  window.__chartRegistry[canvasId] = chart;
  return chart;
};

const fetchJson = async (url) => {
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`Request failed: ${url}`);
  }
  return res.json();
};

const startLiveUpdates = (renderFn, intervalMs = 5000) => {
  let timer = null;

  const run = async () => {
    try {
      await renderFn();
    } catch (e) {
      console.error("Live update failed", e);
    }
  };

  run();
  timer = setInterval(run, intervalMs);

  document.addEventListener("visibilitychange", () => {
    if (document.hidden) {
      if (timer) {
        clearInterval(timer);
        timer = null;
      }
      return;
    }

    if (!timer) {
      run();
      timer = setInterval(run, intervalMs);
    }
  });

  return () => {
    if (timer) {
      clearInterval(timer);
      timer = null;
    }
  };
};

// Make shared helpers accessible to page-level scripts in all browsers.
window.createChart = createChart;
window.fetchJson = fetchJson;
window.startLiveUpdates = startLiveUpdates;
