document.addEventListener("DOMContentLoaded", () => {
  const spanRows = document.querySelectorAll(".span-row");

  spanRows.forEach((row) => {
    row.addEventListener("click", () => {
      spanRows.forEach((r) => r.classList.remove("selected"));
      row.classList.add("selected");

      console.log(
        "Selected span:",
        row.querySelector(".span-label")?.textContent,
      );
    });
  });
});
