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
  const ctx = document.getElementById(canvasId).getContext("2d");
  return new Chart(ctx, {
    type: type,
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      ...options,
    },
  });
};
