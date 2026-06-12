/* ===================================================================
   APEX RACING — Gestão de Equipe
   static/js/team.js
=================================================================== */

/* ── Relógio ────────────────────────────────────────────────── */
(function initClock() {
    const el = document.getElementById('clock');
    if (!el) return;
    function tick() {
        el.textContent = new Date().toLocaleTimeString('pt-BR', {
            hour: '2-digit', minute: '2-digit', second: '2-digit'
        });
    }
    tick();
    setInterval(tick, 1000);
})();

/* ── Scroll do calendário para a corrida atual ──────────────── */
(function scrollCalendar() {
    const current = document.querySelector('.cal-event.current');
    if (current) {
        current.scrollIntoView({ behavior: 'smooth', inline: 'center', block: 'nearest' });
    }
})();

/* ===================================================================
   KANBAN
   Os dados são lidos do atributo data-items do elemento #kanbanData,
   injetado pelo Thymeleaf como JSON. Caso o atributo não exista
   (p.ex. desenvolvimento sem backend), usa dados de demonstração.

   Formato esperado do JSON (List<ItemDesenvolvimento>):
   [
     { "id": 1, "area": "Aerodinâmica", "titulo": "Pacote B-Spec",
       "prioridade": "ALTA", "engenheiro": "K. Chen", "status": "DESENVOLVIMENTO" },
     ...
   ]
=================================================================== */
(function initKanban() {
    const dataEl   = document.getElementById('kanbanData');
    const boardEl  = document.getElementById('kanbanBoard');
    if (!boardEl) return;

    /* ── Dados de fallback (desenvolvimento local) ── */
    const fallback = [
        { id: 1,  area: 'Aerodinâmica', titulo: 'Pacote Aero B-Spec',        prioridade: 'ALTA',  engenheiro: 'K. Chen',    status: 'DESENVOLVIMENTO' },
        { id: 2,  area: 'Aerodinâmica', titulo: 'DRS Otimizado Monaco',       prioridade: 'ALTA',  engenheiro: 'K. Chen',    status: 'TESTE' },
        { id: 3,  area: 'Aerodinâmica', titulo: 'Barge Boards V3',            prioridade: 'MEDIA', engenheiro: 'A. Rossi',   status: 'BACKLOG' },
        { id: 4,  area: 'Motor',        titulo: 'ERS Deploy Calibração',      prioridade: 'ALTA',  engenheiro: 'T. Müller',  status: 'HOMOLOGADO' },
        { id: 5,  area: 'Suspensão',    titulo: 'Suspensão Dianteira +Rigidez', prioridade: 'MEDIA', engenheiro: 'S. Park',  status: 'DESENVOLVIMENTO' },
        { id: 6,  area: 'Transmissão',  titulo: 'Câmbio Sequencial V4',       prioridade: 'BAIXA', engenheiro: 'T. Müller',  status: 'BACKLOG' },
        { id: 7,  area: 'Aerodinâmica', titulo: 'Piso Atualizado',            prioridade: 'ALTA',  engenheiro: 'K. Chen',    status: 'HOMOLOGADO' },
        { id: 8,  area: 'Suspensão',    titulo: 'Anti-Roll Bar Traseiro',     prioridade: 'MEDIA', engenheiro: 'S. Park',    status: 'TESTE' },
        { id: 9,  area: 'Motor',        titulo: 'Mapa de Motor Qualificação', prioridade: 'ALTA',  engenheiro: 'T. Müller',  status: 'BACKLOG' },
        { id: 10, area: 'Aerodinâmica', titulo: 'Asa Traseira High-Down',     prioridade: 'MEDIA', engenheiro: 'A. Rossi',   status: 'DESENVOLVIMENTO' },
    ];

    let items;
    try {
        const raw = dataEl?.dataset?.items;
        items = raw ? JSON.parse(raw) : fallback;
    } catch (e) {
        items = fallback;
    }

    /* ── Estado ── */
    let draggedId = null;

    /* ── Renderiza todos os cards ── */
    function render() {
        const cols = boardEl.querySelectorAll('[data-dropzone]');
        cols.forEach(col => { col.innerHTML = ''; });

        const cols_status = ['BACKLOG', 'DESENVOLVIMENTO', 'TESTE', 'HOMOLOGADO'];

        items.forEach(item => {
            const col = boardEl.querySelector(`[data-dropzone="${item.status}"]`);
            if (!col) return;

            const prioClass = { ALTA: 'prio-alta', MEDIA: 'prio-media', BAIXA: 'prio-baixa' }[item.prioridade] || 'prio-baixa';
            const prioLabel = { ALTA: 'Alta', MEDIA: 'Média', BAIXA: 'Baixa' }[item.prioridade] || item.prioridade;

            const card = document.createElement('div');
            card.className = 'kanban-card';
            card.draggable  = true;
            card.dataset.id = item.id;
            card.innerHTML = `
                <div class="kcard-area">${item.area}</div>
                <div class="kcard-title">${item.titulo}</div>
                <div class="kcard-footer">
                    <span class="kcard-prio ${prioClass}">${prioLabel}</span>
                    <span class="kcard-eng">${item.engenheiro}</span>
                </div>`;

            card.addEventListener('dragstart', e => {
                draggedId = item.id;
                card.classList.add('dragging');
                e.dataTransfer.effectAllowed = 'move';
            });

            card.addEventListener('dragend', () => {
                card.classList.remove('dragging');
                draggedId = null;
            });

            col.appendChild(card);
        });

        updateCounts();
    }

    /* ── Atualiza contadores de coluna ── */
    function updateCounts() {
        ['BACKLOG', 'DESENVOLVIMENTO', 'TESTE', 'HOMOLOGADO'].forEach(status => {
            const el = document.getElementById(`cnt-${status}`);
            if (el) el.textContent = items.filter(i => i.status === status).length;
        });
    }

    /* ── Drop zones ── */
    boardEl.querySelectorAll('[data-dropzone]').forEach(zone => {
        zone.addEventListener('dragover', e => {
            e.preventDefault();
            zone.classList.add('drag-over');
        });

        zone.addEventListener('dragleave', () => {
            zone.classList.remove('drag-over');
        });

        zone.addEventListener('drop', e => {
            e.preventDefault();
            zone.classList.remove('drag-over');

            if (draggedId == null) return;
            const newStatus = zone.dataset.dropzone;
            const item = items.find(i => i.id == draggedId);
            if (item && item.status !== newStatus) {
                item.status = newStatus;
                render();

                /*
                    Em produção, persista a mudança via fetch:
                    fetch(`/api/desenvolvimento/${draggedId}/status`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ status: newStatus })
                    });
                */
            }
        });
    });

    render();
})();
