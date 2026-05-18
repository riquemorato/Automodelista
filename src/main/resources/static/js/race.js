/* ===================================================================
   APEX RACING — Race Engineering
   static/js/race.js

   Contém:
   1. Relógio em tempo real
   2. Estado de corrida simulado (substitua por fetch/SSE em produção)
   3. Simulação do live timing (atualiza a cada ~3s)
   4. Gráfico de evolução de tempos de volta (Chart.js)
   5. Renderização do grid virtual
=================================================================== */

/* ── Relógio ────────────────────────────────────────────────── */
(function initClock() {
    const el = document.getElementById('clock');
    if (!el) return;
    const tick = () => el.textContent = new Date().toLocaleTimeString('pt-BR',
        { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    tick();
    setInterval(tick, 1000);
})();

/* ── Timer de corrida ───────────────────────────────────────── */
(function initRaceTimer() {
    const el = document.getElementById('raceTimer');
    if (!el) return;
    let seconds = 32 * 60 + 17; // 00:32:17 inicial
    setInterval(() => {
        seconds++;
        const h = String(Math.floor(seconds / 3600)).padStart(2, '0');
        const m = String(Math.floor((seconds % 3600) / 60)).padStart(2, '0');
        const s = String(seconds % 60).padStart(2, '0');
        el.textContent = `${h}:${m}:${s}`;
    }, 1000);
})();

/* ===================================================================
   ESTADO DA CORRIDA
   Em produção: popule raceState a partir de dados do controller ou
   via fetch periódico para um endpoint REST como /api/corrida/estado.
   Os campos th:data-* nos elementos HTML podem ser usados como seed.
=================================================================== */
const raceState = {
    lap: 14,
    totalLaps: 78,
    flag: 'VERDE',

    /* Nossos pilotos (índices 0 e 1) */
    ourNums: [14, 22],

    cars: [
        { pos: 1,  num: 44, driver: 'Hamilton',    team: 'Mercedes', gap: 'LDR',      interval: '—',       last: '1:14.851', best: '1:14.604', s1: '27.411', s2: '25.812', s3: '21.631', s1c: 'pb',      s2c: 'neutral', s3c: 'neutral', tyre: 'M', tyreLaps: 14, pits: 0 },
        { pos: 2,  num: 1,  driver: 'Verstappen',  team: 'Red Bull', gap: '+0.842',   interval: '+0.842',  last: '1:14.693', best: '1:14.520', s1: '27.201', s2: '25.961', s3: '21.531', s1c: 'fastest', s2c: 'neutral', s3c: 'pb',      tyre: 'M', tyreLaps: 14, pits: 0 },
        { pos: 3,  num: 14, driver: 'Hartmann',    team: 'Apex',     gap: '+2.841',   interval: '+1.999',  last: '1:15.122', best: '1:14.881', s1: '27.841', s2: '26.220', s3: '21.061', s1c: 'fastest', s2c: 'pb',      s3c: 'neutral', tyre: 'M', tyreLaps: 8,  pits: 0 },
        { pos: 4,  num: 16, driver: 'Leclerc',     team: 'Ferrari',  gap: '+4.103',   interval: '+1.262',  last: '1:15.240', best: '1:14.990', s1: '27.920', s2: '26.190', s3: '21.130', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'M', tyreLaps: 14, pits: 0 },
        { pos: 5,  num: 55, driver: 'Sainz',       team: 'Ferrari',  gap: '+6.218',   interval: '+2.115',  last: '1:15.402', best: '1:15.102', s1: '28.050', s2: '26.110', s3: '21.242', s1c: 'neutral', s2c: 'pb',      s3c: 'neutral', tyre: 'H', tyreLaps: 14, pits: 0 },
        { pos: 6,  num: 22, driver: 'DeLuca',      team: 'Apex',     gap: '+7.103',   interval: '+0.885',  last: '1:15.561', best: '1:15.301', s1: '28.312', s2: '26.108', s3: '21.141', s1c: 'slow',    s2c: 'pb',      s3c: 'neutral', tyre: 'S', tyreLaps: 4,  pits: 0 },
        { pos: 7,  num: 4,  driver: 'Norris',      team: 'McLaren',  gap: '+9.441',   interval: '+2.338',  last: '1:15.802', best: '1:15.441', s1: '28.111', s2: '26.450', s3: '21.241', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'M', tyreLaps: 14, pits: 0 },
        { pos: 8,  num: 81, driver: 'Piastri',     team: 'McLaren',  gap: '+11.200',  interval: '+1.759',  last: '1:15.920', best: '1:15.600', s1: '28.220', s2: '26.560', s3: '21.140', s1c: 'neutral', s2c: 'neutral', s3c: 'pb',      tyre: 'H', tyreLaps: 14, pits: 0 },
        { pos: 9,  num: 63, driver: 'Russell',     team: 'Mercedes', gap: '+13.850',  interval: '+2.650',  last: '1:16.102', best: '1:15.800', s1: '28.402', s2: '26.560', s3: '21.140', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'M', tyreLaps: 14, pits: 0 },
        { pos: 10, num: 11, driver: 'Perez',       team: 'Red Bull', gap: '+15.200',  interval: '+1.350',  last: '1:16.250', best: '1:15.940', s1: '28.530', s2: '26.580', s3: '21.140', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'H', tyreLaps: 14, pits: 0 },
        { pos: 11, num: 14, driver: 'Alonso',      team: 'Aston',    gap: '+18.440',  interval: '+3.240',  last: '1:16.510', best: '1:16.200', s1: '28.750', s2: '26.610', s3: '21.150', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'S', tyreLaps: 4,  pits: 0 },
        { pos: 12, num: 18, driver: 'Stroll',      team: 'Aston',    gap: '+21.100',  interval: '+2.660',  last: '1:16.750', best: '1:16.400', s1: '28.900', s2: '26.700', s3: '21.150', s1c: 'neutral', s2c: 'neutral', s3c: 'neutral', tyre: 'M', tyreLaps: 14, pits: 0 },
    ]
};

/* ===================================================================
   TORRE DE TIMING
=================================================================== */
function renderTimingTable() {
    const tbody = document.getElementById('timingBody');
    if (!tbody) return;

    tbody.innerHTML = raceState.cars.map(car => {
        const isOurs   = raceState.ourNums.includes(car.num);
        const rowClass = isOurs ? 'our-car' : '';
        const numClass = isOurs ? '' : 'other';
        const posClass = car.pos === 1 ? 'p1' : car.pos === 2 ? 'p2' : car.pos === 3 ? 'p3' : '';

        const tyreClass = { S: 'tyre-S', M: 'tyre-M', H: 'tyre-H', I: 'tyre-I', W: 'tyre-W' }[car.tyre] || 'tyre-H';

        return `<tr class="${rowClass}">
            <td><span class="t-pos ${posClass}">${car.pos}</span></td>
            <td><span class="t-num ${numClass}">${car.num}</span></td>
            <td>
                <div class="t-driver">${car.driver}</div>
                <div class="t-team-name">${car.team}</div>
            </td>
            <td><span class="t-time ${car.pos === 1 ? 'leader' : ''}">${car.gap}</span></td>
            <td><span class="t-time">${car.interval}</span></td>
            <td><span class="t-time">${car.last}</span></td>
            <td><span class="t-time">${car.best}</span></td>
            <td><span class="t-sector ${car.s1c}">${car.s1}</span></td>
            <td><span class="t-sector ${car.s2c}">${car.s2}</span></td>
            <td><span class="t-sector ${car.s3c}">${car.s3}</span></td>
            <td>
                <div class="t-tyre-cell">
                    <div class="tyre ${tyreClass}">${car.tyre}</div>
                    <span class="tyre-laps">${car.tyreLaps}v</span>
                </div>
            </td>
            <td class="t-status-ok">${car.pits}</td>
        </tr>`;
    }).join('');
}

/* ===================================================================
   SIMULAÇÃO DE TIMING
   Atualiza setores e tempos de volta periodicamente.
   Em produção: substitua por fetch para /api/corrida/timing ou SSE.
=================================================================== */
(function initTimingSimulation() {
    function randMs(base, spread) {
        return (base + (Math.random() - 0.5) * spread).toFixed(3);
    }

    function formatLap(s1, s2, s3) {
        const total = parseFloat(s1) + parseFloat(s2) + parseFloat(s3);
        const m = Math.floor(total / 60);
        const s = (total % 60).toFixed(3).padStart(6, '0');
        return `${m}:${s}`;
    }

    const baseTimes = {
        s1: { 44: 27.4, 1: 27.2, 14: 27.8, 16: 27.9, 55: 28.0, 22: 28.3, 4: 28.1, 81: 28.2, 63: 28.4, 11: 28.5, 14: 28.7, 18: 28.9 },
        s2: { 44: 25.8, 1: 26.0, 14: 26.2, 16: 26.2, 55: 26.1, 22: 26.1, 4: 26.4, 81: 26.5, 63: 26.5, 11: 26.6, 14: 26.6, 18: 26.7 },
        s3: { 44: 21.6, 1: 21.5, 14: 21.1, 16: 21.1, 55: 21.2, 22: 21.1, 4: 21.2, 81: 21.1, 63: 21.1, 11: 21.1, 14: 21.1, 18: 21.1 },
    };

    function assignSectorColor(val, all, carNum) {
        const fastest = Math.min(...all);
        if (Math.abs(parseFloat(val) - fastest) < 0.05) return 'fastest';
        const pb = Math.min(...raceState.cars
            .filter(c => c.num === carNum)
            .map(() => parseFloat(val))) ;
        if (parseFloat(val) <= pb + 0.1) return 'pb';
        if (parseFloat(val) > fastest + 0.8) return 'slow';
        return 'neutral';
    }

    setInterval(() => {
        raceState.cars.forEach(car => {
            const s1 = randMs(baseTimes.s1[car.num] || 28.0, 0.6);
            const s2 = randMs(baseTimes.s2[car.num] || 26.2, 0.4);
            const s3 = randMs(baseTimes.s3[car.num] || 21.2, 0.3);
            car.s1 = s1; car.s2 = s2; car.s3 = s3;
            car.last = formatLap(s1, s2, s3);
            car.tyreLaps++;
        });

        const allS1 = raceState.cars.map(c => parseFloat(c.s1));
        const allS2 = raceState.cars.map(c => parseFloat(c.s2));
        const allS3 = raceState.cars.map(c => parseFloat(c.s3));

        raceState.cars.forEach(car => {
            const fastestS1 = Math.min(...allS1);
            const fastestS2 = Math.min(...allS2);
            const fastestS3 = Math.min(...allS3);
            car.s1c = Math.abs(parseFloat(car.s1) - fastestS1) < 0.05 ? 'fastest' : parseFloat(car.s1) > fastestS1 + 0.7 ? 'slow' : 'neutral';
            car.s2c = Math.abs(parseFloat(car.s2) - fastestS2) < 0.05 ? 'fastest' : parseFloat(car.s2) > fastestS2 + 0.7 ? 'slow' : 'neutral';
            car.s3c = Math.abs(parseFloat(car.s3) - fastestS3) < 0.05 ? 'fastest' : parseFloat(car.s3) > fastestS3 + 0.7 ? 'slow' : 'neutral';
        });

        raceState.lap++;
        renderTimingTable();
        updateDriverCards();
        updateGrid();
        updateLapChart();
    }, 3500);

    renderTimingTable();
})();

/* ── Atualiza driver cards ──────────────────────────────────── */
function updateDriverCards() {
    const p1 = raceState.cars.find(c => c.num === raceState.ourNums[0]);
    const p2 = raceState.cars.find(c => c.num === raceState.ourNums[1]);

    const set = (id, val) => { const el = document.getElementById(id); if (el) el.textContent = val; };
    const setClass = (id, cls) => {
        const el = document.getElementById(id);
        if (!el) return;
        el.className = el.className.replace(/\bfastest\b|\bpb\b|\bslow\b|\bneutral\b/g, '').trim() + ' ' + cls;
    };

    if (p1) {
        set('p1pos',   `P${p1.pos}`);
        set('p1gap',   p1.gap);
        set('p1s1',    p1.s1);   setClass('p1s1', p1.s1c + ' dlc-s-val');
        set('p1s2',    p1.s2);   setClass('p1s2', p1.s2c + ' dlc-s-val');
        set('p1s3',    p1.s3);   setClass('p1s3', p1.s3c + ' dlc-s-val');
        set('p1tyreLaps', `${p1.tyreLaps} voltas`);
    }

    if (p2) {
        set('p2pos',   `P${p2.pos}`);
        set('p2gap',   p2.gap);
        set('p2s1',    p2.s1);   setClass('p2s1', p2.s1c + ' dlc-s-val');
        set('p2s2',    p2.s2);   setClass('p2s2', p2.s2c + ' dlc-s-val');
        set('p2s3',    p2.s3);   setClass('p2s3', p2.s3c + ' dlc-s-val');
        set('p2tyreLaps', `${p2.tyreLaps} voltas`);
    }
}

/* ===================================================================
   GRID VIRTUAL
=================================================================== */
function updateGrid() {
    const container = document.getElementById('gridContainer');
    if (!container) return;

    container.innerHTML = raceState.cars.map(car => {
        const isOurs = raceState.ourNums.includes(car.num);
        return `<div class="grid-slot ${isOurs ? 'our-slot' : ''}">
            <span class="grid-pos-num">${car.pos}</span>
            <span class="grid-car-num ${isOurs ? 'ours' : 'other'}">${car.num}</span>
            <span class="grid-driver-name">${car.driver}</span>
            <span class="grid-gap-time">${car.gap === 'LDR' ? '—' : car.gap}</span>
        </div>`;
    }).join('');
}

updateGrid();

/* ===================================================================
   GRÁFICO DE EVOLUÇÃO DE TEMPOS DE VOLTA
=================================================================== */
(function initLapChart() {
    const canvas = document.getElementById('lapChart');
    if (!canvas) return;

    Chart.defaults.color       = '#878fa8';
    Chart.defaults.borderColor = 'rgba(255,255,255,0.07)';
    Chart.defaults.font.family = "'IBM Plex Sans', sans-serif";
    Chart.defaults.font.size   = 10;

    /* Gera histórico simulado de voltas */
    function genLapHistory(baseS, spread, pitLap) {
        const laps = [];
        for (let v = 1; v <= raceState.lap; v++) {
            const degradation = (v % pitLap) * 0.04; // degradação de pneu
            const raw = baseS + degradation + (Math.random() - 0.5) * spread;
            const isPitLap = v === pitLap;
            laps.push(isPitLap ? raw + 22 : parseFloat(raw.toFixed(3)));
        }
        return laps;
    }

    const labels  = Array.from({ length: raceState.lap }, (_, i) => i + 1);
    const p1Laps  = genLapHistory(74.8, 0.4, 28);
    const p2Laps  = genLapHistory(75.3, 0.5, 22);

    /* Ponto de pit stop */
    const pitPlugin = {
        id: 'pitLines',
        afterDraw(chart) {
            const { ctx, scales: { x, y } } = chart;
            [{ lap: 28, color: '#e8002d' }, { lap: 22, color: '#e8002d' }].forEach(pit => {
                if (pit.lap > raceState.lap) return;
                const xPos = x.getPixelForValue(pit.lap - 1);
                ctx.save();
                ctx.strokeStyle = 'rgba(232,0,45,0.4)';
                ctx.lineWidth   = 1;
                ctx.setLineDash([3, 3]);
                ctx.beginPath();
                ctx.moveTo(xPos, y.top);
                ctx.lineTo(xPos, y.bottom);
                ctx.stroke();
                ctx.restore();
            });
        }
    };

    const lapChart = new Chart(canvas, {
        type: 'line',
        plugins: [pitPlugin],
        data: {
            labels,
            datasets: [
                {
                    label: `#${raceState.ourNums[0]} Hartmann`,
                    data:  p1Laps,
                    borderColor:     '#e8002d',
                    backgroundColor: 'rgba(232,0,45,0.06)',
                    borderWidth:     1.5,
                    pointRadius:     0,
                    pointHoverRadius: 4,
                    fill:    false,
                    tension: 0.3
                },
                {
                    label: `#${raceState.ourNums[1]} DeLuca`,
                    data:  p2Laps,
                    borderColor:     '#4a9eff',
                    backgroundColor: 'rgba(74,158,255,0.06)',
                    borderWidth:     1.5,
                    pointRadius:     0,
                    pointHoverRadius: 4,
                    fill:    false,
                    tension: 0.3,
                    borderDash: [4, 2]
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: { duration: 300 },
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: ctx => {
                            const v = ctx.raw;
                            const m = Math.floor(v / 60);
                            const s = (v % 60).toFixed(3).padStart(6, '0');
                            return `${ctx.dataset.label}: ${m}:${s}`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid:  { color: 'rgba(255,255,255,0.04)' },
                    ticks: { color: '#454d64', maxTicksLimit: 12 }
                },
                y: {
                    grid:  { color: 'rgba(255,255,255,0.04)' },
                    ticks: {
                        color: '#454d64',
                        callback: v => {
                            const m = Math.floor(v / 60);
                            const s = (v % 60).toFixed(1).padStart(4, '0');
                            return `${m}:${s}`;
                        }
                    }
                }
            }
        }
    });

    /* Legenda manual */
    const legendContainer = canvas.parentElement;
    const legendEl = document.createElement('div');
    legendEl.style.cssText = 'display:flex;gap:16px;font-size:11px;color:var(--text-secondary);margin-bottom:6px;';
    legendEl.innerHTML = `
        <span style="display:flex;align-items:center;gap:5px;">
            <span style="width:18px;height:2px;background:#e8002d;display:inline-block;border-radius:1px;"></span>
            #${raceState.ourNums[0]} Hartmann
        </span>
        <span style="display:flex;align-items:center;gap:5px;">
            <span style="width:18px;height:2px;background:#4a9eff;display:inline-block;border-radius:1px;"></span>
            #${raceState.ourNums[1]} DeLuca
        </span>
        <span style="margin-left:auto;display:flex;align-items:center;gap:5px;color:var(--text-muted);">
            <span style="width:18px;height:1px;border-top:1px dashed rgba(232,0,45,0.5);display:inline-block;"></span>
            Pit stop
        </span>`;
    legendContainer.insertBefore(legendEl, canvas);

    /* Adiciona ponto a cada tick do timing */
    window.updateLapChart = function () {
        const newP1 = p1Laps[p1Laps.length - 1] + (Math.random() - 0.5) * 0.3;
        const newP2 = p2Laps[p2Laps.length - 1] + (Math.random() - 0.5) * 0.3;
        lapChart.data.labels.push(raceState.lap);
        lapChart.data.datasets[0].data.push(parseFloat(newP1.toFixed(3)));
        lapChart.data.datasets[1].data.push(parseFloat(newP2.toFixed(3)));
        lapChart.update('none');
    };
})();
