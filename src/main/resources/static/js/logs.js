document.addEventListener("DOMContentLoaded", () => {
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
    logContainer.scrollTop = logContainer.scrollHeight;
  }
});
