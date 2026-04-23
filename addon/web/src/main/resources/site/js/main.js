const REFRESH_INTERVAL = 5000; // 5초

let countdown = REFRESH_INTERVAL / 1000;
const timerEl = document.getElementById('refresh-timer');

// ── API 호출 ──────────────────────────────────────────────────

async function fetchStatus() {
    try {
        const res = await fetch('/api/status');
        if (!res.ok) throw new Error(res.status);
        const data = await res.json();

        document.getElementById('stat-version').textContent = data.version ?? '—';
        document.getElementById('stat-online').textContent  = data.online  ?? '—';
        document.getElementById('stat-max').textContent     = data.max     ?? '—';
        document.getElementById('stat-motd').textContent    = data.motd    ?? '—';
    } catch (e) {
        console.error('status fetch 실패:', e);
    }
}

async function fetchPlayers() {
    try {
        const res = await fetch('/api/players');
        if (!res.ok) throw new Error(res.status);
        const players = await res.json();

        const container = document.getElementById('player-list');

        if (players.length === 0) {
            container.innerHTML = '<p class="empty">접속 중인 플레이어가 없습니다.</p>';
            return;
        }

        container.innerHTML = players.map(p => `
      <div class="player-row">
        <span class="player-dot"></span>
        <span class="player-name">${escapeHtml(p.name)}</span>
        <span class="player-meta">${escapeHtml(p.world)} · ❤ ${p.health.toFixed(1)}</span>
      </div>
    `).join('');

    } catch (e) {
        console.error('players fetch 실패:', e);
    }
}

// ── 유틸 ──────────────────────────────────────────────────────

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

// ── 자동 갱신 루프 ─────────────────────────────────────────────

function refresh() {
    fetchStatus();
    fetchPlayers();
    countdown = REFRESH_INTERVAL / 1000;
}

setInterval(() => {
    countdown--;
    timerEl.textContent = `새로고침: ${countdown}s`;
    if (countdown <= 0) refresh();
}, 1000);

// 최초 로드
refresh();