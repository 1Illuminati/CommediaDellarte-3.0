'use strict';

// ── 상태 ──────────────────────────────────────────────────────────────────
let filePath = '';
let consolePaused = false;
let pendingAction = null; // { type: 'kick'|'ban', player: string }

// ── 포맷 유틸 ─────────────────────────────────────────────────────────────
function fmtUptime(sec) {
    const h = Math.floor(sec / 3600);
    const m = Math.floor((sec % 3600) / 60);
    const s = sec % 60;
    return `${h}h ${m}m ${s}s`;
}

function fmtBytes(bytes) {
    if (bytes < 1024)            return bytes + ' B';
    if (bytes < 1024 * 1024)     return (bytes / 1024).toFixed(1) + ' KB';
    if (bytes < 1024 * 1024 * 1024) return (bytes / 1024 / 1024).toFixed(1) + ' MB';
    return (bytes / 1024 / 1024 / 1024).toFixed(2) + ' GB';
}

function esc(s) {
    return String(s ?? '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function tpsClass(v) {
    if (v < 0)   return '';
    if (v >= 18) return 'tps-good';
    if (v >= 15) return 'tps-warn';
    return 'tps-bad';
}

// ── /api/stats 갱신 ───────────────────────────────────────────────────────
async function fetchStats() {
    try {
        const d = await fetch('/api/stats').then(r => r.json());

        document.getElementById('uptime-label').textContent = '⏱ ' + fmtUptime(d.uptime);
        document.getElementById('version-label').textContent = d.version || '';

        const setTps = (id, v) => {
            const el = document.getElementById(id);
            el.textContent = v < 0 ? 'N/A' : v.toFixed(2);
            el.className = 'stat-value ' + tpsClass(v);
        };
        setTps('v-tps',   d.tps.s10);
        setTps('v-tps5m', d.tps.m5);

        const msptColor = v => v < 0 ? '' : v < 40 ? 'tps-good' : v < 80 ? 'tps-warn' : 'tps-bad';
        const vm = document.getElementById('v-mspt');
        vm.textContent = d.mspt.mean < 0 ? 'N/A' : d.mspt.mean.toFixed(1) + 'ms';
        vm.className = 'stat-value ' + msptColor(d.mspt.mean);

        const vm95 = document.getElementById('v-mspt95');
        vm95.textContent = d.mspt.p95 < 0 ? 'N/A' : d.mspt.p95.toFixed(1) + 'ms';
        vm95.className = 'stat-value ' + msptColor(d.mspt.p95);

        document.getElementById('v-cpu').textContent    = d.cpu.process < 0 ? 'N/A' : d.cpu.process.toFixed(1) + '%';
        document.getElementById('v-cpusys').textContent = d.cpu.system  < 0 ? 'N/A' : d.cpu.system.toFixed(1) + '%';

        const pct = Math.round(d.memory.used / d.memory.max * 100);
        document.getElementById('mem-fill').style.width = pct + '%';
        document.getElementById('mem-label').textContent = `${d.memory.used} / ${d.memory.max} MB (${pct}%)`;

        const gcEl = document.getElementById('gc-list');
        if (d.gc && d.gc.length > 0) {
            gcEl.innerHTML = d.gc.map(g => `
                <div class="gc-row">
                  <span class="gc-name">${esc(g.name)}</span>
                  <span class="gc-meta">avg ${g.avgTime.toFixed(1)}ms · ${g.avgFrequency}s 주기</span>
                </div>`).join('');
        } else {
            gcEl.innerHTML = '<p class="empty">spark 미설치 또는 데이터 없음</p>';
        }

        document.getElementById('player-count').textContent = `${d.onlineCount}/${d.maxPlayers}`;
        const plEl = document.getElementById('player-list');
        if (d.players.length === 0) {
            plEl.innerHTML = '<p class="empty">접속자 없음</p>';
        } else {
            plEl.innerHTML = d.players.map(p => `
                <div class="player-row">
                  <span class="dot"></span>
                  <span class="player-name">${esc(p.name)}</span>
                  <span class="player-meta">${esc(p.world)} · ❤ ${p.health.toFixed(0)} · ${p.ping}ms</span>
                  <div class="player-action-wrap">
                    <button class="btn-player-action" data-player="${esc(p.name)}">⋮</button>
                    <div class="player-dropdown">
                      <button data-action="info" data-player="${esc(p.name)}">🔍 정보 보기</button>
                      <button data-action="kick" data-player="${esc(p.name)}">🚪 추방</button>
                      <button data-action="ban"  data-player="${esc(p.name)}" class="danger">🔨 밴</button>
                    </div>
                  </div>
                </div>`).join('');
        }

    } catch (e) {
        console.error('stats 오류:', e);
    }
}

// ── 플레이어 드롭다운 이벤트 ──────────────────────────────────────────────
document.addEventListener('click', e => {
    if (!e.target.closest('.player-action-wrap')) {
        document.querySelectorAll('.player-dropdown.open')
            .forEach(d => d.classList.remove('open'));
    }
});

document.getElementById('player-list').addEventListener('click', e => {
    const actionBtn = e.target.closest('.btn-player-action');
    if (actionBtn) {
        e.stopPropagation();
        const dropdown = actionBtn.nextElementSibling;
        const isOpen = dropdown.classList.contains('open');
        document.querySelectorAll('.player-dropdown.open').forEach(d => d.classList.remove('open'));
        if (!isOpen) dropdown.classList.add('open');
        return;
    }

    const menuBtn = e.target.closest('.player-dropdown button');
    if (menuBtn) {
        e.stopPropagation();
        const action = menuBtn.dataset.action;
        const player = menuBtn.dataset.player;
        menuBtn.closest('.player-dropdown').classList.remove('open');

        if (action === 'info')      showPlayerInfo(player);
        else if (action === 'kick') showActionModal(player, 'kick');
        else if (action === 'ban')  showActionModal(player, 'ban');
    }
});

// ── 플레이어 정보 모달 ────────────────────────────────────────────────────
async function showPlayerInfo(name) {
    document.getElementById('info-modal-title').textContent = name + ' — 정보';
    document.getElementById('info-modal-body').innerHTML = '<p class="empty">로딩 중...</p>';
    document.getElementById('info-modal').style.display = 'flex';

    try {
        const p = await fetch('/api/player/info?name=' + encodeURIComponent(name)).then(r => r.json());
        if (p.error) {
            document.getElementById('info-modal-body').innerHTML = `<p class="empty">${esc(p.error)}</p>`;
            return;
        }

        const sessionTime = p.sessionSeconds >= 0 ? fmtUptime(p.sessionSeconds) : '알 수 없음';
        const firstPlayed = p.firstPlayed > 0
            ? new Date(p.firstPlayed).toLocaleString('ko-KR')
            : '알 수 없음';

        document.getElementById('info-modal-body').innerHTML = `
            <div class="info-grid">
              <span class="info-key">닉네임</span>    <span class="info-val">${esc(p.name)}</span>
              <span class="info-key">UUID</span>       <span class="info-val" style="font-size:.75rem">${esc(p.uuid)}</span>
              <span class="info-key">IP 주소</span>    <span class="info-val">${esc(p.ip)}</span>
              <hr class="info-sep">
              <span class="info-key">접속 세계</span>  <span class="info-val">${esc(p.world)}</span>
              <span class="info-key">위치</span>        <span class="info-val">X ${p.x.toFixed(1)}, Y ${p.y.toFixed(1)}, Z ${p.z.toFixed(1)}</span>
              <span class="info-key">게임모드</span>   <span class="info-val">${esc(p.gameMode)}</span>
              <hr class="info-sep">
              <span class="info-key">체력</span>        <span class="info-val">${p.health.toFixed(1)} / ${p.maxHealth.toFixed(1)}</span>
              <span class="info-key">배고픔</span>     <span class="info-val">${p.foodLevel} / 20</span>
              <span class="info-key">레벨</span>        <span class="info-val">${p.level}</span>
              <span class="info-key">핑</span>           <span class="info-val">${p.ping}ms</span>
              <hr class="info-sep">
              <span class="info-key">이번 세션</span>  <span class="info-val">${sessionTime}</span>
              <span class="info-key">최초 접속</span>  <span class="info-val">${firstPlayed}</span>
            </div>`;
    } catch (err) {
        document.getElementById('info-modal-body').innerHTML = '<p class="empty">정보를 불러올 수 없습니다.</p>';
    }
}

function closeInfoModal() {
    document.getElementById('info-modal').style.display = 'none';
}

// ── 추방 / 밴 모달 ───────────────────────────────────────────────────────
function showActionModal(player, type) {
    pendingAction = { player, type };
    document.getElementById('action-modal-title').textContent =
        type === 'kick' ? `${player} 추방` : `${player} 밴`;
    document.getElementById('action-reason').value = '';
    document.getElementById('action-modal').style.display = 'flex';
    setTimeout(() => document.getElementById('action-reason').focus(), 50);
}

function closeActionModal() {
    document.getElementById('action-modal').style.display = 'none';
    pendingAction = null;
}

async function confirmPlayerAction() {
    if (!pendingAction) return;
    const { player, type } = pendingAction;
    const reason = document.getElementById('action-reason').value.trim() ||
        (type === 'kick' ? '웹 대시보드에서 추방되었습니다.' : '웹 대시보드에서 밴되었습니다.');
    closeActionModal();

    try {
        await fetch('/api/player/' + type, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: player, reason })
        });
    } catch (e) {
        console.error('player action 오류:', e);
    }
}

document.getElementById('action-reason').addEventListener('keydown', e => {
    if (e.key === 'Enter')  confirmPlayerAction();
    if (e.key === 'Escape') closeActionModal();
});

// ── /api/console 갱신 ─────────────────────────────────────────────────────
async function fetchConsole() {
    if (consolePaused) return;
    try {
        const lines = await fetch('/api/console').then(r => r.json());
        const el = document.getElementById('console-output');
        const atBottom = el.scrollHeight - el.scrollTop - el.clientHeight < 40;

        el.innerHTML = lines.map(l =>
            '<div>' + esc(l.replace(/\x1B\[[0-9;]*m/g, '')) + '</div>'
        ).join('');

        if (atBottom) el.scrollTop = el.scrollHeight;
    } catch (e) {
        console.error('console 오류:', e);
    }
}

function toggleConsolePause() {
    consolePaused = !consolePaused;
    const btn = document.getElementById('console-pause-btn');
    btn.textContent = consolePaused ? '▶ 재개' : '일시정지';
    btn.className   = consolePaused ? 'btn btn-warn' : 'btn btn-muted';
}

// ── 명령어 전송 ───────────────────────────────────────────────────────────
async function sendCommand() {
    const input = document.getElementById('cmd-input');
    const cmd = input.value.trim();
    if (!cmd) return;
    try {
        await fetch('/api/command', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ command: cmd })
        });
        input.value = '';
    } catch (e) {
        console.error('command 오류:', e);
    }
}

// ── 서버 제어 ─────────────────────────────────────────────────────────────
async function serverAction(action) {
    const label = action === 'shutdown' ? '서버를 종료' : '서버를 재시작';
    if (!confirm(`정말 ${label}하시겠습니까?`)) return;
    await fetch('/api/' + action, { method: 'POST' });
}

// ── /api/plugins 플러그인 목록 ─────────────────────────────────────────────
async function fetchPlugins() {
    try {
        const plugins = await fetch('/api/plugins').then(r => r.json());
        document.getElementById('plugin-count').textContent = plugins.length;

        const el = document.getElementById('plugin-list');
        if (plugins.length === 0) {
            el.innerHTML = '<p class="empty">플러그인 없음</p>';
            return;
        }
        el.innerHTML = plugins
            .sort((a, b) => a.name.localeCompare(b.name))
            .map(p => `
                <div class="plugin-row">
                  <span class="plugin-dot ${p.enabled ? 'enabled' : 'disabled'}"></span>
                  <span class="plugin-name">${esc(p.name)}</span>
                  <span class="plugin-ver">${esc(p.version)}</span>
                </div>`).join('');
    } catch (e) {
        console.error('plugins 오류:', e);
    }
}

// ── 파일 매니저 ───────────────────────────────────────────────────────────
function downloadFile(path, filename) {
    const a = document.createElement('a');
    a.href = '/api/files?path=' + encodeURIComponent(path);
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}

async function loadFiles(path) {
    if (path !== undefined) filePath = path;

    const parts = filePath.split('/').filter(Boolean);
    let bcHtml = '<span class="bc-part" data-path="">루트</span>';
    parts.forEach((p, i) => {
        const sub = parts.slice(0, i + 1).join('/');
        bcHtml += ` / <span class="bc-part" data-path="${esc(sub)}">${esc(p)}</span>`;
    });
    document.getElementById('breadcrumb').innerHTML = bcHtml;

    try {
        const url = '/api/files' + (filePath ? '?path=' + encodeURIComponent(filePath) : '');
        const data = await fetch(url).then(r => r.json());
        const el = document.getElementById('file-list');

        if (!data.items || data.items.length === 0) {
            el.innerHTML = '<p class="empty">빈 폴더</p>';
            return;
        }

        el.innerHTML = data.items.map(f => {
            const icon    = f.isDir ? '📁' : fileIcon(f.name);
            const subPath = (filePath ? filePath + '/' : '') + f.name;
            const date    = new Date(f.modified).toLocaleDateString('ko-KR');
            return `<div class="file-row" data-path="${esc(subPath)}" data-dir="${f.isDir}" data-name="${esc(f.name)}">
                  <span class="file-icon">${icon}</span>
                  <span class="file-name">${esc(f.name)}</span>
                  <span class="file-size">${fmtBytes(f.size)}</span>
                  <span class="file-date">${date}</span>
                </div>`;
        }).join('');

    } catch (e) {
        console.error('files 오류:', e);
    }
}

// 파일 목록 클릭 (이벤트 위임)
document.getElementById('file-list').addEventListener('click', e => {
    const row = e.target.closest('.file-row');
    if (!row) return;
    if (row.dataset.dir === 'true') loadFiles(row.dataset.path);
    else                            downloadFile(row.dataset.path, row.dataset.name);
});

// 브레드크럼 클릭
document.getElementById('breadcrumb').addEventListener('click', e => {
    const part = e.target.closest('.bc-part');
    if (part) loadFiles(part.dataset.path);
});

function fileIcon(name) {
    const ext = name.split('.').pop().toLowerCase();
    const map = { jar:'📦', yml:'⚙️', yaml:'⚙️', json:'📋', log:'📄',
                  txt:'📄', png:'🖼️', jpg:'🖼️', zip:'🗜️', sh:'💻', bat:'💻' };
    return map[ext] ?? '📄';
}

// ── 초기 로드 & 폴링 루프 ─────────────────────────────────────────────────
fetchStats();
fetchConsole();
loadFiles('');
fetchPlugins();

setInterval(fetchStats,   3000);
setInterval(fetchConsole, 1500);
setInterval(() => loadFiles(), 10000);
setInterval(fetchPlugins,  30000);