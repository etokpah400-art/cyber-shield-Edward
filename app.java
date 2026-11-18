// =========================================================
// 1. THREE.JS 3D CYBERSHIELD CORE + DEEP SPACE BACKGROUND
// =========================================================
const container = document.getElementById('canvas-container');
const scene = new THREE.Scene();

const camera = new THREE.PerspectiveCamera(
  50,
  container.clientWidth / container.clientHeight,
  0.1,
  1000
);
camera.position.z = 6.5;

const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
renderer.setSize(container.clientWidth, container.clientHeight);
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
container.appendChild(renderer.domElement);

// Master Group for Interactive Drag Rotation
const shieldGroup = new THREE.Group();
scene.add(shieldGroup);

// 1A. Outer Geodesic Forcefield Wireframe
const outerGeo = new THREE.IcosahedronGeometry(2.1, 2);
const outerMat = new THREE.MeshBasicMaterial({
  color: 0x00f0ff,
  wireframe: true,
  transparent: true,
  opacity: 0.38
});
const outerShield = new THREE.Mesh(outerGeo, outerMat);
shieldGroup.add(outerShield);

// 1B. Inner Counter-Rotating Octahedral Cage
const innerGeo = new THREE.OctahedronGeometry(1.45, 1);
const innerMat = new THREE.MeshBasicMaterial({
  color: 0x00ff9d,
  wireframe: true,
  transparent: true,
  opacity: 0.52
});
const innerCage = new THREE.Mesh(innerGeo, innerMat);
shieldGroup.add(innerCage);

// 1C. Solid Energy Nucleus + Inner Core Glow Shell
const coreGeo = new THREE.IcosahedronGeometry(0.72, 3);
const coreMat = new THREE.MeshBasicMaterial({
  color: 0x00f0ff,
  transparent: true,
  opacity: 0.88
});
const energyCore = new THREE.Mesh(coreGeo, coreMat);
shieldGroup.add(energyCore);

const haloGeo = new THREE.SphereGeometry(0.98, 24, 24);
const haloMat = new THREE.MeshBasicMaterial({
  color: 0x00f0ff,
  transparent: true,
  opacity: 0.18
});
const coreHalo = new THREE.Mesh(haloGeo, haloMat);
shieldGroup.add(coreHalo);

// 1D. Gyroscopic Orbital Rings
const ringGeo = new THREE.TorusGeometry(2.55, 0.016, 16, 120);
const ringMat = new THREE.MeshBasicMaterial({
  color: 0x8b5cf6,
  transparent: true,
  opacity: 0.7
});
const ring1 = new THREE.Mesh(ringGeo, ringMat);
const ring2 = new THREE.Mesh(ringGeo, ringMat);
const ring3 = new THREE.Mesh(ringGeo, ringMat);
ring1.rotation.x = Math.PI / 3;
ring2.rotation.y = Math.PI / 3;
ring3.rotation.z = Math.PI / 4;
shieldGroup.add(ring1, ring2, ring3);

// 1E. 320-Node Orbital Particle Cloud
const particleCount = 320;
const positions = new Float32Array(particleCount * 3);
for (let i = 0; i < particleCount; i++) {
  const r = 2.65 + Math.random() * 1.25;
  const theta = Math.random() * Math.PI * 2;
  const phi = Math.acos(2 * Math.random() - 1);
  positions[i * 3] = r * Math.sin(phi) * Math.cos(theta);
  positions[i * 3 + 1] = r * Math.sin(phi) * Math.sin(theta);
  positions[i * 3 + 2] = r * Math.cos(phi);
}
const particleGeo = new THREE.BufferGeometry();
particleGeo.setAttribute('position', new THREE.BufferAttribute(positions, 3));
const particleMat = new THREE.PointsMaterial({
  color: 0x00f0ff,
  size: 0.05,
  transparent: true,
  opacity: 0.85
});
const particleCloud = new THREE.Points(particleGeo, particleMat);
shieldGroup.add(particleCloud);

// 1F. Background Deep-Space Cyber Starfield (Inside 3D Viewport)
const bgStarCount = 450;
const bgStarPos = new Float32Array(bgStarCount * 3);
for (let i = 0; i < bgStarCount; i++) {
  bgStarPos[i * 3] = (Math.random() - 0.5) * 22;
  bgStarPos[i * 3 + 1] = (Math.random() - 0.5) * 16;
  bgStarPos[i * 3 + 2] = -3 - Math.random() * 10;
}
const bgStarGeo = new THREE.BufferGeometry();
bgStarGeo.setAttribute('position', new THREE.BufferAttribute(bgStarPos, 3));
const bgStarMat = new THREE.PointsMaterial({
  color: 0x64748b,
  size: 0.03,
  transparent: true,
  opacity: 0.55
});
const bgStarField = new THREE.Points(bgStarGeo, bgStarMat);
scene.add(bgStarField);

// Mouse Drag & Scroll Zoom Controls
let isDragging = false;
let prevMouse = { x: 0, y: 0 };
let rotSpeedMultiplier = 1.0;
let empPulseScale = 1.0;
let clock = 0;

container.addEventListener('pointerdown', (e) => {
  isDragging = true;
  prevMouse = { x: e.clientX, y: e.clientY };
});

window.addEventListener('pointerup', () => (isDragging = false));

window.addEventListener('pointermove', (e) => {
  if (!isDragging) return;
  const dx = (e.clientX - prevMouse.x) * 0.008;
  const dy = (e.clientY - prevMouse.y) * 0.008;
  shieldGroup.rotation.y += dx;
  shieldGroup.rotation.x += dy;
  prevMouse = { x: e.clientX, y: e.clientY };
});

container.addEventListener('wheel', (e) => {
  e.preventDefault();
  camera.position.z = Math.min(Math.max(camera.position.z + e.deltaY * 0.004, 4.0), 10.0);
}, { passive: false });

// Shield Operational Mode Switching + Ambient Background Sync
const modeConfigs = {
  nominal: {
    outer: 0x00f0ff,
    inner: 0x00ff9d,
    speed: 1.0,
    badge: 'DEFCON 5 // NOMINAL',
    integrity: '99.98%',
    stateLabel: 'STATE: AEGIS_FORCEFIELD',
    dropRate: '14.8k pps',
    threat: 'LOW',
    glow: 'radial-gradient(circle at 50% 50%, rgba(0, 240, 255, 0.24) 0%, rgba(139, 92, 246, 0.08) 45%, transparent 75%)'
  },
  ddos: {
    outer: 0xff2a5f,
    inner: 0xffb800,
    speed: 3.4,
    badge: 'DEFCON 2 // DDoS SCRUBBING',
    integrity: '94.12%',
    stateLabel: 'STATE: XDP_SYN_MITIGATION',
    dropRate: '982.4k pps',
    threat: 'CRITICAL',
    glow: 'radial-gradient(circle at 50% 50%, rgba(255, 42, 95, 0.32) 0%, rgba(255, 184, 0, 0.12) 45%, transparent 75%)'
  },
  quantum: {
    outer: 0x8b5cf6,
    inner: 0x00f0ff,
    speed: 1.6,
    badge: 'ML-KEM-1024 // LOCKED',
    integrity: '100.0%',
    stateLabel: 'STATE: LATTICE_VAULT_SEALED',
    dropRate: '21.3k pps',
    threat: 'ZERO-TRUST',
    glow: 'radial-gradient(circle at 50% 50%, rgba(139, 92, 246, 0.32) 0%, rgba(0, 240, 255, 0.12) 45%, transparent 75%)'
  },
  scan: {
    outer: 0x00ff9d,
    inner: 0x00f0ff,
    speed: 2.2,
    badge: 'DEEP HEURISTIC SCAN',
    integrity: '99.99%',
    stateLabel: 'STATE: NEURAL_GRAPH_SCAN',
    dropRate: '44.1k pps',
    threat: 'SCANNING',
    glow: 'radial-gradient(circle at 50% 50%, rgba(0, 255, 157, 0.28) 0%, rgba(0, 240, 255, 0.1) 45%, transparent 75%)'
  }
};

function activateShieldMode(mode) {
  const cfg = modeConfigs[mode];
  if (!cfg) return;

  document.querySelectorAll('.shield-mode-btn').forEach((b) => {
    const isMatch = b.getAttribute('data-mode') === mode;
    b.classList.toggle('active-mode', isMatch);
    b.classList.toggle('border-cyan-500/40', isMatch);
    b.classList.toggle('bg-cyan-500/10', isMatch);
    b.classList.toggle('text-cyber-cyan', isMatch);
  });

  outerMat.color.setHex(cfg.outer);
  coreMat.color.setHex(cfg.outer);
  haloMat.color.setHex(cfg.outer);
  particleMat.color.setHex(cfg.outer);
  innerMat.color.setHex(cfg.inner);
  rotSpeedMultiplier = cfg.speed;

  document.getElementById('hud-integrity').textContent = cfg.integrity;
  document.getElementById('hud-mode-label').textContent = cfg.stateLabel;
  document.getElementById('hud-drop-rate').textContent = cfg.dropRate;
  document.getElementById('metric-threat').textContent = cfg.threat;
  document.getElementById('core-backlight').style.background = cfg.glow;
  document.getElementById('global-status-badge').innerHTML = `<span class="w-2 h-2 rounded-full bg-cyber-cyan animate-pulse"></span> ${cfg.badge}`;
}

document.querySelectorAll('.shield-mode-btn').forEach((btn) => {
  btn.addEventListener('click', () => activateShieldMode(btn.getAttribute('data-mode')));
});

// Sync 4-Layer Architecture Cards with 3D Shield Mode
document.querySelectorAll('[data-sync-mode]').forEach((card) => {
  card.addEventListener('click', () => {
    activateShieldMode(card.getAttribute('data-sync-mode'));
    document.getElementById('core').scrollIntoView({ behavior: 'smooth' });
  });
});

// EMP Shockwave Trigger
document.getElementById('emp-btn').addEventListener('click', () => {
  empPulseScale = 1.52;
  outerMat.opacity = 0.95;
});

// Animation Loop
function animate() {
  requestAnimationFrame(animate);
  clock += 0.03;

  const baseStep = 0.004 * rotSpeedMultiplier;
  outerShield.rotation.y += baseStep;
  outerShield.rotation.x += baseStep * 0.5;
  innerCage.rotation.y -= baseStep * 1.4;
  innerCage.rotation.z += baseStep * 0.8;
  ring1.rotation.z += baseStep * 1.2;
  ring2.rotation.x -= baseStep * 1.1;
  ring3.rotation.y += baseStep * 0.9;
  particleCloud.rotation.y += baseStep * 0.4;
  bgStarField.rotation.y -= 0.0004;

  // Breathing core pulse
  const pulse = 1 + Math.sin(clock * 2) * 0.06;
  coreHalo.scale.setScalar(pulse);

  // Smooth EMP Decay
  if (empPulseScale > 1.001) {
    empPulseScale += (1.0 - empPulseScale) * 0.08;
    outerShield.scale.setScalar(empPulseScale);
    outerMat.opacity = 0.38 + (empPulseScale - 1.0);
  }

  document.getElementById('hud-rotation').textContent = `${(baseStep * 60).toFixed(2)} rad/s`;
  renderer.render(scene, camera);
}
animate();

// Responsive Resize
window.addEventListener('resize', () => {
  camera.aspect = container.clientWidth / container.clientHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(container.clientWidth, container.clientHeight);
});

// =========================================================
// 2. LIVE SOC PACKET STREAM & INSPECTOR
// =========================================================
const packetLog = document.getElementById('packet-log');
let streamActive = true;
let blockedCounter = 14820;

const sampleEvents = [
  { type: 'XDP_DROP', ip: '185.220.101.44', msg: 'SYN Flood signature mitigated at NIC edge', color: 'text-cyber-crimson' },
  { type: 'mTLS_OK', ip: '10.24.8.112', msg: 'SPIFFE X.509 workload identity verified', color: 'text-cyber-emerald' },
  { type: 'WAF_BLOCK', ip: '45.155.205.9', msg: 'SQLi UNION SELECT payload scrubbed', color: 'text-amber-400' },
  { type: 'PQC_KEM', ip: '172.16.0.19', msg: 'ML-KEM-1024 lattice key exchange sealed', color: 'text-violet-400' }
];

function appendPacketLog() {
  if (!streamActive) return;
  const ev = sampleEvents[Math.floor(Math.random() * sampleEvents.length)];
  const time = new Date().toISOString().split('T')[1].slice(0, 12);

  blockedCounter += Math.floor(Math.random() * 19) + 3;
  document.getElementById('metric-blocked').textContent = blockedCounter.toLocaleString();
  document.getElementById('metric-latency').textContent = `${(0.36 + Math.random() * 0.12).toFixed(2)} ms`;

  const row = document.createElement('div');
  row.className = 'p-2.5 rounded bg-slate-900/75 border border-slate-800/80 flex items-center justify-between gap-2';
  row.innerHTML = `
    <div>
      <span class="text-slate-500">[${time}]</span>
      <span class="font-bold ${ev.color} ml-1">${ev.type}</span>
      <span class="text-slate-300 ml-2">${ev.msg}</span>
    </div>
    <span class="text-slate-500">${ev.ip}</span>
  `;
  packetLog.prepend(row);
  if (packetLog.children.length > 18) packetLog.removeChild(packetLog.lastChild);
}
setInterval(appendPacketLog, 1100);
for (let i = 0; i < 6; i++) appendPacketLog();

document.getElementById('toggle-stream-btn').addEventListener('click', (e) => {
  streamActive = !streamActive;
  e.target.textContent = streamActive ? 'PAUSE FEED' : 'RESUME FEED';
});

// Deep Packet Inspector
const payloadInput = document.getElementById('payload-input');
const inspectResult = document.getElementById('inspect-result');

document.querySelectorAll('.preset-btn').forEach((btn) => {
  btn.addEventListener('click', () => {
    payloadInput.value = btn.getAttribute('data-payload');
  });
});

document.getElementById('inspect-btn').addEventListener('click', () => {
  const val = payloadInput.value.trim();
  if (!val) return;

  const isMalicious = /(\bUNION\b|\bSELECT\b|--|<script>|jndi:ldap|\bOR\b\s+['"]?1['"]?=['"]?1)/i.test(val);
  if (isMalicious) {
    inspectResult.className = 'p-3 rounded-lg bg-rose-950/50 border border-rose-500/50 font-mono text-xs text-cyber-crimson';
    inspectResult.innerHTML = `⚠️ THREAT DETECTED: Signature match [OWASP-CRS-942100]. Action: XDP_DROP & IP Quarantine triggered.`;
    activateShieldMode('ddos');
  } else {
    inspectResult.className = 'p-3 rounded-lg bg-emerald-950/40 border border-emerald-500/40 font-mono text-xs text-cyber-emerald';
    inspectResult.innerHTML = `✓ PAYLOAD CLEAN: Zero-Trust mTLS headers validated. Forwarded to internal mesh.`;
    activateShieldMode('nominal');
  }
});

// =========================================================
// 3. 3D SPATIAL TILT CARDS
// =========================================================
document.querySelectorAll('.tilt-card').forEach((card) => {
  card.addEventListener('mousemove', (e) => {
    const rect = card.getBoundingClientRect();
    const x = e.clientX - rect.left - rect.width / 2;
    const y = e.clientY - rect.top - rect.height / 2;
    card.style.transform = `perspective(700px) rotateX(${(-y / 14).toFixed(2)}deg) rotateY(${(x / 14).toFixed(2)}deg)`;
  });
  card.addEventListener('mouseleave', () => {
    card.style.transform = 'perspective(700px) rotateX(0deg) rotateY(0deg)';
  });
});

// =========================================================
// 4. CRYPTOGRAPHIC WORKBENCH (WITH FILE:// FALLBACK)
// =========================================================
const shaInput = document.getElementById('sha-input');
const shaOutput = document.getElementById('sha-output');

// Fallback deterministic 256-bit hash when opened via file:// (where crypto.subtle is blocked)
function fallbackSha256(str) {
  let h1 = 0xdeadbeef ^ str.length, h2 = 0x41c6ce57 ^ str.length;
  let h3 = 0x9e3779b9 ^ str.length, h4 = 0x85ebca6b ^ str.length;
  for (let i = 0; i < str.length; i++) {
    const ch = str.charCodeAt(i);
    h1 = Math.imul(h1 ^ ch, 2654435761);
    h2 = Math.imul(h2 ^ ch, 1597334677);
    h3 = Math.imul(h3 ^ ch, 2246822507);
    h4 = Math.imul(h4 ^ ch, 3266489909);
  }
  const mix = (n) => (n >>> 0).toString(16).padStart(8, '0');
  return (
    mix(h1) + mix(h2) + mix(h3) + mix(h4) +
    mix(h1 ^ h3) + mix(h2 ^ h4) + mix(Math.imul(h1, 7)) + mix(Math.imul(h4, 13))
  );
}

async function updateSha256() {
  const text = shaInput.value;
  if (window.crypto && window.crypto.subtle) {
    try {
      const msgBuffer = new TextEncoder().encode(text);
      const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
      const hashArray = Array.from(new Uint8Array(hashBuffer));
      shaOutput.textContent = hashArray.map((b) => b.toString(16).padStart(2, '0')).join('');
      return;
    } catch (e) {
      // Fallback if blocked
    }
  }
  shaOutput.textContent = fallbackSha256(text);
}
shaInput.addEventListener('input', updateSha256);
updateSha256();

document.getElementById('mutate-btn').addEventListener('click', () => {
  const current = shaInput.value || 'CyberShield';
  const lastChar = current.slice(-1);
  const mutatedChar = String.fromCharCode(lastChar.charCodeAt(0) ^ 1);
  shaInput.value = current.slice(0, -1) + mutatedChar;
  updateSha256();
});

// Module 2: AES-256-GCM Simulator
const aesInput = document.getElementById('aes-input');
const aesOutput = document.getElementById('aes-output');
let aesNonceSeed = 4096;

function updateAesGcm() {
  const raw = aesInput.value || '';
  const ivHex = (aesNonceSeed * 982451653 >>> 0).toString(16).padStart(8, '0') + 'a4f901c2';
  const cipherHex = Array.from(raw).map((c, idx) =>
    ((c.charCodeAt(0) ^ ((aesNonceSeed + idx * 31) & 0xff)) & 0xff).toString(16).padStart(2, '0')
  ).join('');
  const tagHex = fallbackSha256(raw + ivHex).slice(0, 32);
  aesOutput.innerHTML = `<span class="text-slate-400">IV:</span>${ivHex} <span class="text-slate-400">CT:</span>${cipherHex} <span class="text-cyber-emerald">TAG:${tagHex}</span>`;
}
aesInput.addEventListener('input', updateAesGcm);
document.getElementById('aes-btn').addEventListener('click', () => {
  aesNonceSeed += Math.floor(Math.random() * 9000) + 1;
  updateAesGcm();
});
updateAesGcm();

// Module 3: Quantum Entropy Calculator
const entropyInput = document.getElementById('entropy-input');
function evaluateEntropy() {
  const val = entropyInput.value;
  let pool = 0;
  if (/[a-z]/.test(val)) pool += 26;
  if (/[A-Z]/.test(val)) pool += 26;
  if (/[0-9]/.test(val)) pool += 10;
  if (/[^a-zA-Z0-9]/.test(val)) pool += 32;

  const entropy = val.length ? Math.round(val.length * Math.log2(pool || 1)) : 0;
  const quantumBits = Math.round(entropy / 2);

  document.getElementById('entropy-bits').textContent = `${entropy} bits`;
  document.getElementById('quantum-bits').textContent = `${quantumBits} bits`;

  const gradeEl = document.getElementById('entropy-grade');
  if (quantumBits >= 64) {
    gradeEl.textContent = 'QUANTUM SAFE';
    gradeEl.className = 'text-sm font-bold text-cyber-emerald mt-1';
  } else if (quantumBits >= 40) {
    gradeEl.textContent = 'MODERATE';
    gradeEl.className = 'text-sm font-bold text-amber-400 mt-1';
  } else {
    gradeEl.textContent = 'VULNERABLE';
    gradeEl.className = 'text-sm font-bold text-cyber-crimson mt-1';
  }
}
entropyInput.addEventListener('input', evaluateEntropy);
evaluateEntropy();