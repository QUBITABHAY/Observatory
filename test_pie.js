const services = [{"id":1,"name":"node-agent","hostId":1,"createdAt":"2026-03-14T01:40:06.3506"},{"id":2,"name":"observatory","hostId":2,"createdAt":"2026-03-15T16:18:33.860647"}];
const traces = [{"id":1,"traceId":"trace-check-1773573163","duration":321,"serviceId":1,"startedAt":"2026-03-15T16:42:43.495216"}];
const serviceById = new Map(services.map((s) => [s.id, s.name]));
const traceCounts = new Map();
traces.forEach((t) => {
  const key = serviceById.get(t.serviceId) || `svc-${t.serviceId}`;
  traceCounts.set(key, (traceCounts.get(key) || 0) + 1);
});
const top = [...traceCounts.entries()]
  .sort((a, b) => b[1] - a[1])
  .slice(0, 5);
const totalTop = top.reduce((acc, [, count]) => acc + count, 0);
console.log(top, totalTop);
