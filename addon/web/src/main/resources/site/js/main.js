'use strict';

// ── 인증 ──────────────────────────────────────────────────────────────────
const TOKEN_KEY = 'mc_dash_token';
function getToken()   { return localStorage.getItem(TOKEN_KEY); }
function setToken(t)  { localStorage.setItem(TOKEN_KEY, t); }
function clearToken() { localStorage.removeItem(TOKEN_KEY); }

async function apiFetch(url, options = {}) {
    const token = getToken();
    if (token) {
        options.headers = Object.assign({ 'Authorization': 'Bearer ' + token }, options.headers || {});
    }
    const res = await fetch(url, options);
    if (res.status === 401) { showLoginOverlay(); throw new Error('unauthorized'); }
    return res;
}

// ── 탭 전환 ──────────────────────────────────────────────────────────────
function switchTab(tab) {
    document.querySelectorAll('.tab-nav-btn').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.tab-panel').forEach(p => p.style.display = 'none');
    document.querySelector(`.tab-nav-btn[data-tab="${tab}"]`).classList.add('active');
    document.getElementById('tab-' + tab).style.display = '';
}

// ── 토스트 ───────────────────────────────────────────────────────────────
function showToast(msg, type = 'ok') {
    const el = document.getElementById('toast');
    el.textContent = msg;
    el.className = 'toast toast-' + type;
    el.style.display = 'block';
    clearTimeout(el._t);
    el._t = setTimeout(() => { el.style.display = 'none'; }, 2800);
}

// ── 로그인 / 로그아웃 ─────────────────────────────────────────────────────
function showLoginOverlay() {
    document.getElementById('login-overlay').style.display = 'flex';
    document.getElementById('logout-btn').style.display    = 'none';
    document.getElementById('login-error').style.display   = 'none';
    document.getElementById('login-user').value = '';
    document.getElementById('login-pass').value = '';
    setTimeout(() => document.getElementById('login-user').focus(), 50);
}

function hideLoginOverlay() {
    document.getElementById('login-overlay').style.display = 'none';
}

async function doLogin() {
    const username = document.getElementById('login-user').value.trim();
    const password = document.getElementById('login-pass').value;
    const errEl    = document.getElementById('login-error');
    const btn      = document.getElementById('login-btn');

    if (!username || !password) {
        errEl.textContent = '아이디와 비밀번호를 입력하세요.';
        errEl.style.display = 'block';
        return;
    }
    btn.disabled = true;
    btn.textContent = '로그인 중...';
    errEl.style.display = 'none';

    try {
        const res  = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (data.ok) {
            if (data.token) setToken(data.token);
            hideLoginOverlay();
            document.getElementById('logout-btn').style.display = 'inline-flex';
            startDashboard();
        } else {
            errEl.textContent = data.error || '로그인 실패';
            errEl.style.display = 'block';
        }
    } catch (e) {
        errEl.textContent = '서버에 연결할 수 없습니다.';
        errEl.style.display = 'block';
    } finally {
        btn.disabled = false;
        btn.textContent = '로그인';
    }
}

async function doLogout() {
    try { await apiFetch('/api/auth/logout', { method: 'POST' }); } catch (_) {}
    clearToken();
    document.getElementById('logout-btn').style.display = 'none';
    showLoginOverlay();
}

async function initAuth() {
    try {
        const token = getToken();
        const headers = token ? { 'Authorization': 'Bearer ' + token } : {};
        const data = await fetch('/api/auth/status', { headers }).then(r => r.json());
        if (!data.enabled) {
            hideLoginOverlay();
            startDashboard();
        } else if (data.authenticated) {
            hideLoginOverlay();
            document.getElementById('logout-btn').style.display = 'inline-flex';
            startDashboard();
        } else {
            showLoginOverlay();
        }
    } catch (e) {
        showLoginOverlay();
    }
}

function startDashboard() {
    fetchStats();
    fetchConsole();
    loadFiles('');
    fetchPlugins();
    setInterval(fetchStats,   3000);
    setInterval(fetchConsole, 1500);
    setInterval(() => loadFiles(), 10000);
    setInterval(fetchPlugins,  30000);
}

// ── 상태 ──────────────────────────────────────────────────────────────────
let filePath = '';
let consolePaused = false;
let pendingAction = null;

// ── 포맷 유틸 ─────────────────────────────────────────────────────────────
function fmtUptime(sec) {
    const h = Math.floor(sec / 3600);
    const m = Math.floor((sec % 3600) / 60);
    const s = sec % 60;
    return `${h}h ${m}m ${s}s`;
}
function fmtBytes(bytes) {
    if (bytes < 1024)               return bytes + ' B';
    if (bytes < 1024 * 1024)        return (bytes / 1024).toFixed(1) + ' KB';
    if (bytes < 1024 * 1024 * 1024) return (bytes / 1024 / 1024).toFixed(1) + ' MB';
    return (bytes / 1024 / 1024 / 1024).toFixed(2) + ' GB';
}
function esc(s) {
    return String(s ?? '')
        .replace(/&/g, '&amp;').replace(/</g, '&lt;')
        .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
function tpsClass(v) {
    if (v < 0)   return '';
    if (v >= 18) return 'tps-good';
    if (v >= 15) return 'tps-warn';
    return 'tps-bad';
}

// ── /api/stats ────────────────────────────────────────────────────────────
async function fetchStats() {
    try {
        const d = await apiFetch('/api/stats').then(r => r.json());
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
    } catch (e) { console.error('stats 오류:', e); }
}

// ── 플레이어 드롭다운 ──────────────────────────────────────────────────────
document.addEventListener('click', e => {
    if (!e.target.closest('.player-action-wrap')) {
        document.querySelectorAll('.player-dropdown.open').forEach(d => d.classList.remove('open'));
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
        const p = await apiFetch('/api/player/info?name=' + encodeURIComponent(name)).then(r => r.json());
        if (p.error) {
            document.getElementById('info-modal-body').innerHTML = `<p class="empty">${esc(p.error)}</p>`;
            return;
        }
        const sessionTime = p.sessionSeconds >= 0 ? fmtUptime(p.sessionSeconds) : '알 수 없음';
        const firstPlayed = p.firstPlayed > 0 ? new Date(p.firstPlayed).toLocaleString('ko-KR') : '알 수 없음';
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
function closeInfoModal() { document.getElementById('info-modal').style.display = 'none'; }

// ── 추방 / 밴 모달 ───────────────────────────────────────────────────────
function showActionModal(player, type) {
    pendingAction = { player, type };
    document.getElementById('action-modal-title').textContent = type === 'kick' ? `${player} 추방` : `${player} 밴`;
    document.getElementById('action-reason').value = '';
    document.getElementById('action-modal').style.display = 'flex';
    setTimeout(() => document.getElementById('action-reason').focus(), 50);
}
function closeActionModal() { document.getElementById('action-modal').style.display = 'none'; pendingAction = null; }

async function confirmPlayerAction() {
    if (!pendingAction) return;
    const { player, type } = pendingAction;
    const reason = document.getElementById('action-reason').value.trim() ||
        (type === 'kick' ? '웹 대시보드에서 추방되었습니다.' : '웹 대시보드에서 밴되었습니다.');
    closeActionModal();
    try {
        await apiFetch('/api/player/' + type, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: player, reason })
        });
    } catch (e) { console.error('player action 오류:', e); }
}

document.getElementById('action-reason').addEventListener('keydown', e => {
    if (e.key === 'Enter')  confirmPlayerAction();
    if (e.key === 'Escape') closeActionModal();
});

// ── /api/console ──────────────────────────────────────────────────────────
async function fetchConsole() {
    if (consolePaused) return;
    try {
        const lines = await apiFetch('/api/console').then(r => r.json());
        const el = document.getElementById('console-output');
        const atBottom = el.scrollHeight - el.scrollTop - el.clientHeight < 40;
        el.innerHTML = lines.map(l => '<div>' + esc(l.replace(/\x1B\[[0-9;]*m/g, '')) + '</div>').join('');
        if (atBottom) el.scrollTop = el.scrollHeight;
    } catch (e) { console.error('console 오류:', e); }
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
        await apiFetch('/api/command', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ command: cmd })
        });
        input.value = '';
    } catch (e) { console.error('command 오류:', e); }
}

// ── 서버 제어 ─────────────────────────────────────────────────────────────
async function serverAction(action) {
    const label = action === 'shutdown' ? '서버를 종료' : '서버를 재시작';
    if (!confirm(`정말 ${label}하시겠습니까?`)) return;
    await apiFetch('/api/' + action, { method: 'POST' });
}

// ── /api/plugins ──────────────────────────────────────────────────────────
async function fetchPlugins() {
    try {
        const plugins = await apiFetch('/api/plugins').then(r => r.json());
        document.getElementById('plugin-count').textContent = plugins.length;
        const el = document.getElementById('plugin-list');
        if (plugins.length === 0) { el.innerHTML = '<p class="empty">플러그인 없음</p>'; return; }
        el.innerHTML = plugins
            .sort((a, b) => a.name.localeCompare(b.name))
            .map(p => `
                <div class="plugin-row">
                  <span class="plugin-dot ${p.enabled ? 'enabled' : 'disabled'}"></span>
                  <span class="plugin-name">${esc(p.name)}</span>
                  <span class="plugin-ver">${esc(p.version)}</span>
                </div>`).join('');
    } catch (e) { console.error('plugins 오류:', e); }
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
        const data = await apiFetch(url).then(r => r.json());
        const el = document.getElementById('file-list');
        if (!data.items || data.items.length === 0) { el.innerHTML = '<p class="empty">빈 폴더</p>'; return; }
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
    } catch (e) { console.error('files 오류:', e); }
}

document.getElementById('file-list').addEventListener('click', e => {
    const row = e.target.closest('.file-row');
    if (!row) return;
    if (row.dataset.dir === 'true') loadFiles(row.dataset.path);
    else                            downloadFile(row.dataset.path, row.dataset.name);
});
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

// ════════════════════════════════════════════════════════════════════════════
// ── 스토리지 탭 ──────────────────────────────────────────────────────────
// ════════════════════════════════════════════════════════════════════════════

/** 웹에서 로드한 스토리지 목록: [{ key, entries }] */
let storageList = [];

// ── 스토리지 로드 모달 ────────────────────────────────────────────────────
function openStorageLoadModal() {
    document.getElementById('sl-namespace').value = '';
    document.getElementById('sl-name').value = '';
    document.getElementById('sl-error').style.display = 'none';
    document.getElementById('storage-load-modal').style.display = 'flex';
    setTimeout(() => document.getElementById('sl-namespace').focus(), 50);
}
function closeStorageLoadModal() {
    document.getElementById('storage-load-modal').style.display = 'none';
}

async function doStorageLoad() {
    const ns   = document.getElementById('sl-namespace').value.trim().toLowerCase();
    const name = document.getElementById('sl-name').value.trim().toLowerCase();
    const errEl = document.getElementById('sl-error');
    const btn   = document.getElementById('sl-btn');

    if (!ns || !name) {
        errEl.textContent = '네임스페이스와 타입을 모두 입력하세요.';
        errEl.style.display = 'block';
        return;
    }

    const storageKey = ns + ':' + name;

    // 이미 로드된 스토리지인지 확인
    if (storageList.some(s => s.key === storageKey)) {
        errEl.textContent = '이미 로드된 스토리지입니다.';
        errEl.style.display = 'block';
        return;
    }

    btn.disabled = true;
    btn.textContent = '로드 중...';
    errEl.style.display = 'none';

    try {
        const res  = await apiFetch('/api/storage/load?storage=' + encodeURIComponent(storageKey));
        const data = await res.json();

        if (data.error) {
            errEl.textContent = data.error;
            errEl.style.display = 'block';
            return;
        }

        storageList.push({ key: data.storage, entries: data.entries });
        closeStorageLoadModal();
        renderStorageTree();
        showToast('스토리지 로드 완료: ' + data.storage, 'ok');
    } catch (e) {
        errEl.textContent = '요청 실패: ' + e.message;
        errEl.style.display = 'block';
    } finally {
        btn.disabled = false;
        btn.textContent = '로드';
    }
}

// ── 스토리지 트리 렌더링 ──────────────────────────────────────────────────
const EDITABLE_TYPES = ['int','long','float','double','boolean','String','UUID','A_DataMap','List'];

/** 펼쳐진 노드 상태 저장 */
function saveExpandState() {
    const state = { snodes: {}, enodes: {}, fnested: {} };

    document.querySelectorAll('[id^="snode-body-"]').forEach(body => {
        const si = body.id.replace('snode-body-', '');
        state.snodes[si] = body.classList.contains('open');
    });

    document.querySelectorAll('.enode').forEach(enode => {
        const addBtn = enode.querySelector('.enode-header .btn-add-field');
        const body   = enode.querySelector('.enode-body');
        if (!addBtn || !body) return;
        const k = `${addBtn.dataset.si}:${addBtn.dataset.datakey}`;
        state.enodes[k] = body.classList.contains('open');
    });

    document.querySelectorAll('.fnode-nested').forEach(fnode => {
        const addBtn = fnode.querySelector('.fnode-nested-header .btn-add-field');
        const body   = fnode.querySelector('.fnested-body');
        if (!addBtn || !body) return;
        const k = `${addBtn.dataset.si}:${addBtn.dataset.datakey}:${addBtn.dataset.path}`;
        state.fnested[k] = body.classList.contains('open');
    });

    return state;
}

/** 펼쳐진 노드 상태 복원 */
function restoreExpandState(state) {
    if (!state) return;

    document.querySelectorAll('[id^="snode-body-"]').forEach(body => {
        const si = body.id.replace('snode-body-', '');
        if (state.snodes[si] === undefined) return;
        const toggle = document.querySelector(`#snode-${si} .snode-toggle`);
        if (state.snodes[si]) {
            body.classList.add('open');
            if (toggle) toggle.textContent = '▼';
        } else {
            body.classList.remove('open');
            if (toggle) toggle.textContent = '▶';
        }
    });

    document.querySelectorAll('.enode').forEach(enode => {
        const addBtn = enode.querySelector('.enode-header .btn-add-field');
        const body   = enode.querySelector('.enode-body');
        if (!addBtn || !body) return;
        const k = `${addBtn.dataset.si}:${addBtn.dataset.datakey}`;
        if (!state.enodes[k]) return;
        const toggle = enode.querySelector('.enode-toggle');
        body.classList.add('open');
        if (toggle) toggle.textContent = '▼';
    });

    document.querySelectorAll('.fnode-nested').forEach(fnode => {
        const addBtn = fnode.querySelector('.fnode-nested-header .btn-add-field');
        const body   = fnode.querySelector('.fnested-body');
        if (!addBtn || !body) return;
        const k = `${addBtn.dataset.si}:${addBtn.dataset.datakey}:${addBtn.dataset.path}`;
        if (!state.fnested[k]) return;
        const toggle = fnode.querySelector('.fnested-toggle');
        body.classList.add('open');
        if (toggle) toggle.textContent = '▼';
    });
}

function renderStorageTree() {
    const state = saveExpandState();

    document.getElementById('storage-loaded-count').textContent = storageList.length;
    const el = document.getElementById('storage-tree');

    if (storageList.length === 0) {
        el.innerHTML = '<p class="empty">좌측 버튼으로 스토리지를 로드하세요</p>';
        return;
    }

    el.innerHTML = storageList.map((s, si) => `
        <div class="snode" id="snode-${si}">
            <div class="snode-header">
                <span class="snode-toggle" onclick="toggleSnode(${si})">▼</span>
                <span class="snode-key">${esc(s.key)}</span>
                <span class="badge">${s.entries.length}개 엔트리</span>
                <div class="snode-actions">
                    <button class="btn btn-primary btn-xs" onclick="refreshStorage(${si})">새로고침</button>
                    <button class="btn btn-warn btn-xs" onclick="saveStorage(${si})">저장</button>
                    <button class="btn btn-danger btn-xs" onclick="removeStorage(${si})">✕</button>
                </div>
            </div>
            <div class="snode-body open" id="snode-body-${si}">
                ${s.entries.length === 0
                    ? '<p class="empty small">메모리에 로드된 데이터 없음</p>'
                    : s.entries.map((entry, ei) => renderEntry(entry, si, ei)).join('')
                }
            </div>
        </div>`).join('');

    restoreExpandState(state);
}

function toggleSnode(si) {
    const body   = document.getElementById('snode-body-' + si);
    const toggle = document.querySelector(`#snode-${si} .snode-toggle`);
    const open   = body.classList.toggle('open');
    toggle.textContent = open ? '▼' : '▶';
}

function renderEntry(entry, si, ei) {
    const count = Object.keys(entry.data).length;
    return `
        <div class="enode">
            <div class="enode-header" onclick="toggleEnode(this)">
                <span class="enode-toggle">▶</span>
                <span class="enode-key">${esc(entry.dataKey)}</span>
                <span class="badge">${count}</span>
                <button class="btn-action-field btn-add-field"
                    data-si="${si}" data-datakey="${esc(entry.dataKey)}"
                    data-path="" data-is-list="false"
                    onclick="event.stopPropagation(); handleAddBtn(this)" title="항목 추가">+</button>
            </div>
            <div class="enode-body">
                ${renderDataMap(entry.data, entry.dataKey, si, [])}
            </div>
        </div>`;
}

function toggleEnode(header) {
    const body   = header.nextElementSibling;
    const toggle = header.querySelector('.enode-toggle');
    const open   = body.classList.toggle('open');
    toggle.textContent = open ? '▼' : '▶';
}

function renderDataMap(data, dataKey, si, path) {
    if (!data || typeof data !== 'object') return '';
    return Object.entries(data).map(([key, vo]) => {
        const fullPath = [...path, key];
        if (vo && vo.type === 'A_DataMap') {
            return renderExpandable('A_DataMap', 'tbadge-datamap', key,
                renderDataMap(vo.value, dataKey, si, fullPath),
                si, dataKey, fullPath, false);
        }
        if (vo && vo.type === 'CS') {
            const cls = vo['__class__'] || '';
            const label = cls.split('.').pop() || 'CS';
            return renderExpandable(label, 'tbadge-cs', key,
                renderDataMap(vo.value, dataKey, si, fullPath),
                si, dataKey, fullPath, false);
        }
        if (vo && vo.type === 'Map') {
            return renderExpandable('Map', 'tbadge-other', key,
                renderDataMap(vo.value, dataKey, si, fullPath),
                si, dataKey, fullPath, false);
        }
        if (vo && vo.type === 'List') {
            const items = Array.isArray(vo.value) ? vo.value : [];
            const inner = items.map((item, idx) =>
                renderListItem(idx, item, dataKey, si, [...fullPath, String(idx)])
            ).join('');
            return renderExpandable('List[' + items.length + ']', 'tbadge-other', key,
                inner || '<p class="empty small">비어있음</p>',
                si, dataKey, fullPath, true);
        }
        return renderField(key, vo, dataKey, si, fullPath);
    }).join('');
}

function renderExpandable(badgeLabel, badgeCls, key, innerHtml, si, dataKey, fullPath, isList) {
    const pathStr = fullPath.join('/');
    return `
        <div class="fnode fnode-nested">
            <div class="fnode-nested-header" onclick="toggleFnested(this)">
                <span class="fnested-toggle">▶</span>
                <span class="tbadge ${esc(badgeCls)}">${esc(badgeLabel)}</span>
                <span class="fkey">${esc(key)}</span>
                <span class="fnode-actions">
                    <button class="btn-action-field btn-add-field"
                        data-si="${si}" data-datakey="${esc(dataKey)}"
                        data-path="${esc(pathStr)}" data-is-list="${isList}"
                        onclick="event.stopPropagation(); handleAddBtn(this)" title="항목 추가">+</button>
                    <button class="btn-action-field btn-delete-field"
                        data-si="${si}" data-datakey="${esc(dataKey)}" data-path="${esc(pathStr)}"
                        onclick="event.stopPropagation(); deleteField(this)" title="삭제">✕</button>
                </span>
            </div>
            <div class="fnested-body">
                ${innerHtml}
            </div>
        </div>`;
}

function renderListItem(idx, vo, dataKey, si, path) {
    if (vo && (vo.type === 'A_DataMap' || vo.type === 'CS' || vo.type === 'Map' || vo.type === 'List')) {
        return renderDataMap({ [String(idx)]: vo }, dataKey, si, path.slice(0, -1));
    }
    return renderField(String(idx), vo, dataKey, si, path);
}

function renderField(key, vo, dataKey, si, path) {
    const type        = vo?.type ?? 'null';
    const rawVal      = vo?.value ?? null;
    const isProtected = !vo || vo.readonly === true;
    const editable    = !isProtected && isEditableType(type);
    const badgeCls    = typeBadgeClass(type);
    const pathStr     = path.join('/');

    const typeBadgeHtml = !isProtected
        ? `<span class="tbadge ${badgeCls} type-btn"
               data-si="${si}" data-datakey="${esc(dataKey)}"
               data-path="${esc(pathStr)}" data-type="${esc(type)}"
               onclick="showTypeSelect(this)" title="타입 변경">${esc(type)}</span>`
        : `<span class="tbadge ${badgeCls}">${esc(type)}</span>`;

    const valHtml = editable
        ? `<span class="fval editable"
               data-si="${si}" data-datakey="${esc(dataKey)}"
               data-path="${esc(pathStr)}" data-type="${esc(type)}"
               data-value="${esc(String(rawVal))}"
               onclick="startEdit(this)">${esc(String(rawVal))}</span>`
        : `<span class="fval readonly">${esc(String(rawVal ?? 'null'))}</span>`;

    const delBtnHtml = !isProtected
        ? `<button class="btn-action-field btn-delete-field"
               data-si="${si}" data-datakey="${esc(dataKey)}" data-path="${esc(pathStr)}"
               onclick="deleteField(this)" title="삭제">✕</button>`
        : '';

    return `
        <div class="fnode">
            ${typeBadgeHtml}
            <span class="fkey">${esc(key)}</span>
            ${valHtml}
            ${delBtnHtml}
        </div>`;
}

function toggleFnested(header) {
    const body   = header.nextElementSibling;
    const toggle = header.querySelector('.fnested-toggle');
    const open   = body.classList.toggle('open');
    toggle.textContent = open ? '▼' : '▶';
}

function isEditableType(type) {
    return ['int','long','float','double','boolean','String','UUID'].includes(type);
}

function typeBadgeClass(type) {
    const m = {
        int:'tbadge-int', long:'tbadge-int', float:'tbadge-float', double:'tbadge-float',
        boolean:'tbadge-bool', String:'tbadge-str', UUID:'tbadge-uuid',
        A_DataMap:'tbadge-datamap', CS:'tbadge-cs', null:'tbadge-null'
    };
    return m[type] || 'tbadge-other';
}

// ── 인라인 편집 ──────────────────────────────────────────────────────────
function startEdit(span) {
    if (span.classList.contains('editing')) return;
    span.classList.add('editing');

    const si      = parseInt(span.dataset.si);
    const dataKey = span.dataset.datakey;
    const path    = span.dataset.path;
    const type    = span.dataset.type;
    const oldVal  = span.dataset.value;
    const storage = storageList[si]?.key;

    let input;
    if (type === 'boolean') {
        input = document.createElement('select');
        input.className = 'fedit-input';
        for (const v of ['true', 'false']) {
            const opt = document.createElement('option');
            opt.value = v; opt.textContent = v;
            if (v === oldVal) opt.selected = true;
            input.appendChild(opt);
        }
    } else {
        input = document.createElement('input');
        input.className = 'fedit-input';
        input.value = oldVal;
        if (type === 'int' || type === 'long') {
            input.type = 'number'; input.step = '1';
            input.addEventListener('input', () => { input.value = input.value.replace(/[^\-\d]/g, ''); });
        } else if (type === 'float' || type === 'double') {
            input.type = 'number'; input.step = 'any';
        } else {
            input.type = 'text';
        }
    }

    span.replaceWith(input);
    input.focus();
    if (type !== 'boolean') input.select?.();

    let done = false;

    const commit = async () => {
        if (done) return;
        done = true;
        const newVal = input.value;
        if ((type === 'int' || type === 'long') && !/^-?\d+$/.test(newVal.trim())) {
            restoreSpan(input, si, dataKey, path, type, oldVal, true);
            showToast('정수를 입력하세요', 'err');
            return;
        }
        try {
            const res  = await apiFetch('/api/storage/set', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ storage, dataKey, field: path, type, value: newVal })
            });
            const data = await res.json();
            if (data.ok) {
                restoreSpan(input, si, dataKey, path, type, newVal, false);
                showToast('수정됨: ' + path.split('/').pop(), 'ok');
            } else {
                restoreSpan(input, si, dataKey, path, type, oldVal, true);
                showToast(data.error || '수정 실패', 'err');
            }
        } catch (e) {
            restoreSpan(input, si, dataKey, path, type, oldVal, true);
            showToast('오류: ' + e.message, 'err');
        }
    };

    const cancel = () => {
        if (done) return;
        done = true;
        restoreSpan(input, si, dataKey, path, type, oldVal, false);
    };

    if (type === 'boolean') {
        input.addEventListener('change', commit);
        input.addEventListener('blur', () => { if (!done) cancel(); });
        input.addEventListener('keydown', e => { if (e.key === 'Escape') cancel(); });
    } else {
        input.addEventListener('blur', commit);
        input.addEventListener('keydown', e => {
            if (e.key === 'Enter')  { e.preventDefault(); input.blur(); }
            if (e.key === 'Escape') { e.preventDefault(); cancel(); }
        });
    }
}

function restoreSpan(input, si, dataKey, path, type, value, error) {
    const span = document.createElement('span');
    span.className  = 'fval editable' + (error ? ' error' : '');
    span.dataset.si      = si;
    span.dataset.datakey = dataKey;
    span.dataset.path    = path;
    span.dataset.type    = type;
    span.dataset.value   = value;
    span.textContent = value;
    span.onclick = function() { startEdit(this); };
    input.replaceWith(span);
}

// ── 타입 변경 ──────────────────────────────────────────────────────────
function showTypeSelect(badgeSpan) {
    const si      = parseInt(badgeSpan.dataset.si);
    const dataKey = badgeSpan.dataset.datakey;
    const path    = badgeSpan.dataset.path;
    const curType = badgeSpan.dataset.type;
    const storage = storageList[si]?.key;

    const sel = document.createElement('select');
    sel.className = 'type-select';
    EDITABLE_TYPES.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t; opt.textContent = t;
        if (t === curType) opt.selected = true;
        sel.appendChild(opt);
    });

    badgeSpan.replaceWith(sel);
    sel.focus();

    let committed = false;

    const commit = async () => {
        if (committed) return;
        committed = true;
        const newType = sel.value;
        if (newType === curType) { sel.replaceWith(badgeSpan); return; }

        // 새 타입 배지를 "…" 상태로 즉시 표시해 이전 값이 잔류하는 것을 방지
        const pending = document.createElement('span');
        pending.className = `tbadge ${typeBadgeClass(newType)}`;
        pending.textContent = newType + '…';
        sel.replaceWith(pending);

        const randomUUID = (typeof crypto !== 'undefined' && crypto.randomUUID)
            ? crypto.randomUUID() : '00000000-0000-0000-0000-000000000000';
        const defaults = { int:'0', long:'0', float:'0.0', double:'0.0',
                           boolean:'false', String:'', UUID: randomUUID,
                           A_DataMap:'', List:'' };
        try {
            const res  = await apiFetch('/api/storage/set', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ storage, dataKey, field: path, type: newType, value: defaults[newType] ?? '' })
            });
            const data = await res.json();
            if (data.ok) { showToast('타입 변경: ' + newType, 'ok'); await refreshStorage(si); }
            else { pending.replaceWith(badgeSpan); showToast(data.error || '타입 변경 실패', 'err'); }
        } catch (e) { pending.replaceWith(badgeSpan); showToast('오류: ' + e.message, 'err'); }
    };

    const cancel = () => { if (!committed) { committed = true; sel.replaceWith(badgeSpan); } };

    sel.addEventListener('change', commit);
    sel.addEventListener('blur', () => setTimeout(cancel, 80));
    sel.addEventListener('keydown', e => { if (e.key === 'Escape') cancel(); });
}

// ── 추가 ──────────────────────────────────────────────────────────────────
function handleAddBtn(btn) {
    const isList        = btn.dataset.isList === 'true';
    const si            = parseInt(btn.dataset.si);
    const dataKey       = btn.dataset.datakey;
    const containerPath = btn.dataset.path;

    if (isList) { doAddField(si, dataKey, containerPath, ''); return; }

    const header = btn.closest('.fnode-nested-header, .enode-header');
    if (!header) return;
    const body = header.nextElementSibling;

    const toggle = header.querySelector('.fnested-toggle, .enode-toggle');
    if (toggle && !body.classList.contains('open')) {
        body.classList.add('open'); toggle.textContent = '▼';
    }
    if (body.querySelector('.add-row')) {
        body.querySelector('.add-row .add-key-input')?.focus(); return;
    }

    const row = document.createElement('div');
    row.className = 'fnode add-row';
    row.innerHTML = `<input class="fedit-input add-key-input" placeholder="새 키 이름..." style="flex:1;min-width:80px">
                     <button class="btn btn-xs btn-primary" style="flex-shrink:0">추가</button>
                     <button class="btn btn-xs btn-muted" style="flex-shrink:0">✕</button>`;

    const input     = row.querySelector('.add-key-input');
    const confirmBtn = row.querySelectorAll('button')[0];
    const cancelBtn  = row.querySelectorAll('button')[1];

    const doAdd = async () => {
        const newKey = input.value.trim();
        if (!newKey) { input.focus(); return; }
        confirmBtn.disabled = true;
        await doAddField(si, dataKey, containerPath, newKey);
    };

    confirmBtn.onclick = doAdd;
    cancelBtn.onclick = () => row.remove();
    input.addEventListener('keydown', e => {
        if (e.key === 'Enter') doAdd();
        if (e.key === 'Escape') row.remove();
    });

    body.appendChild(row);
    setTimeout(() => input.focus(), 0);
}

async function doAddField(si, dataKey, containerPath, newKey) {
    const storage = storageList[si]?.key;
    if (!storage) return;
    try {
        const res  = await apiFetch('/api/storage/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ storage, dataKey, container: containerPath, newKey })
        });
        const data = await res.json();
        if (data.ok) { showToast('추가됨', 'ok'); await refreshStorage(si); }
        else showToast(data.error || '추가 실패', 'err');
    } catch (e) { showToast('추가 오류: ' + e.message, 'err'); }
}

// ── 삭제 ──────────────────────────────────────────────────────────────────
async function deleteField(btn) {
    const si      = parseInt(btn.dataset.si);
    const dataKey = btn.dataset.datakey;
    const path    = btn.dataset.path;
    const storage = storageList[si]?.key;
    if (!storage) return;
    try {
        const res  = await apiFetch('/api/storage/delete', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ storage, dataKey, field: path })
        });
        const data = await res.json();
        if (data.ok) { showToast('삭제됨', 'ok'); await refreshStorage(si); }
        else showToast(data.error || '삭제 실패', 'err');
    } catch (e) { showToast('삭제 오류: ' + e.message, 'err'); }
}

// ── 스토리지 새로고침 / 저장 / 제거 ─────────────────────────────────────
async function refreshStorage(si) {
    const s = storageList[si];
    if (!s) return;
    try {
        const res  = await apiFetch('/api/storage/load?storage=' + encodeURIComponent(s.key));
        const data = await res.json();
        if (data.error) { showToast(data.error, 'err'); return; }
        storageList[si].entries = data.entries;
        renderStorageTree();
        showToast('새로고침 완료: ' + s.key, 'ok');
    } catch (e) {
        showToast('새로고침 실패: ' + e.message, 'err');
    }
}

async function saveStorage(si) {
    const s = storageList[si];
    if (!s) return;
    try {
        const res  = await apiFetch('/api/storage/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ storage: s.key })
        });
        const data = await res.json();
        showToast(data.ok ? '저장 완료: ' + s.key : (data.error || '저장 실패'), data.ok ? 'ok' : 'err');
    } catch (e) {
        showToast('저장 오류: ' + e.message, 'err');
    }
}

function removeStorage(si) {
    storageList.splice(si, 1);
    renderStorageTree();
}

// ── 초기 진입점 ───────────────────────────────────────────────────────────
initAuth();