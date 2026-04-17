const percentile = (values, p) => {
    if (!values.length) return 0;
    const sorted = [...values].sort((a, b) => a - b);
    const idx = Math.min(
      sorted.length - 1,
      Math.ceil((p / 100) * sorted.length) - 1,
    );
    return Math.round(sorted[Math.max(0, idx)]);
  };

const groupIntoWindows = (items, windows) => {
    if (!items.length) return [];
    const size = Math.max(1, Math.ceil(items.length / windows));
    const out = [];
    for (let i = 0; i < items.length; i += size) {
      out.push(items.slice(i, i + size));
    }
    return out;
  };
const traces = [{"id":1,"traceId":"trace-check-1773573163","duration":321,"serviceId":1,"startedAt":"2026-03-15T16:42:43.495216"}];
const tracesSorted = [...traces].sort((a, b) => new Date(a.startedAt).getTime() - new Date(b.startedAt).getTime());
const windows = groupIntoWindows(tracesSorted, 8);
const latencyData = {
      labels: windows.map((_, i) => `w${i + 1}`),
      datasets: [
        { data: windows.map((w) => percentile(w.map((t) => t.duration || 0), 99)) }
      ]
};
console.log(latencyData);
