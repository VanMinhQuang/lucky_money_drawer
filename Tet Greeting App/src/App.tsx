import { useState, useRef, useEffect, useMemo, useCallback } from 'react'

// ─── Types ────────────────────────────────────────────────────────────────────

type Screen = 'welcome' | 'setup' | 'shuffle' | 'selection'

interface Envelope {
  id: number
  amount: number
  opened: boolean
  // For shuffle/deal positioning
  dealX: number
  dealY: number
  dealR: number
  shuffleOrder: number
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

function formatMoney(amount: number): string {
  if (amount >= 1_000_000_000)
    return `${(amount / 1_000_000_000).toLocaleString('vi-VN')} tỷ`
  if (amount >= 1_000_000)
    return `${(amount / 1_000_000).toLocaleString('vi-VN')} triệu`
  return `${amount.toLocaleString('vi-VN')} đồng`
}

function shuffle<T>(arr: T[]): T[] {
  const a = [...arr]
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [a[i], a[j]] = [a[j], a[i]]
  }
  return a
}

// ─── Petal Rain ──────────────────────────────────────────────────────────────

function PetalRain({ count = 28 }: { count?: number }) {
  const petals = useMemo(() =>
    Array.from({ length: count }, (_, i) => ({
      id: i,
      left: `${(i / count) * 100 + (i % 3) * 1.5}%`,
      duration: `${5 + (i % 6)}s`,
      delay: `${-((i * 1.1) % 8)}s`,
      size: 7 + (i % 5) * 2.5,
      pink: 160 + (i % 60),
    })), [count])

  return (
    <div style={{ position: 'absolute', inset: 0, overflow: 'hidden', pointerEvents: 'none' }}>
      {petals.map(p => (
        <div key={p.id} style={{
          position: 'absolute',
          left: p.left,
          top: 0,
          width: p.size,
          height: p.size,
          borderRadius: '50% 0 50% 0',
          background: `radial-gradient(circle at 40% 40%, hsl(340, 80%, ${p.pink / 2.2}%), hsl(340, 70%, 55%))`,
          animation: `petal-fall ${p.duration} ${p.delay} linear infinite`,
          opacity: 0.75,
        }} />
      ))}
    </div>
  )
}

// ─── Stars ───────────────────────────────────────────────────────────────────

function Stars({ count = 55 }: { count?: number }) {
  const stars = useMemo(() =>
    Array.from({ length: count }, (_, i) => ({
      id: i,
      left: `${(i / count) * 100 + (i % 7) * 0.3}%`,
      top: `${(i * 13 + 7) % 100}%`,
      size: 1 + (i % 3),
      dur: `${1.5 + (i % 3)}s`,
      delay: `${(i * 0.17) % 2.5}s`,
    })), [count])

  return (
    <div style={{ position: 'absolute', inset: 0, overflow: 'hidden', pointerEvents: 'none' }}>
      {stars.map(s => (
        <div key={s.id} style={{
          position: 'absolute',
          left: s.left, top: s.top,
          width: s.size, height: s.size,
          borderRadius: '50%',
          background: '#FFD700',
          animation: `twinkle ${s.dur} ${s.delay} ease-in-out infinite alternate`,
        }} />
      ))}
    </div>
  )
}

// ─── Lantern ─────────────────────────────────────────────────────────────────

function Lantern({ left, scale = 1, delay = '0s', color = '#C41E3A' }: {
  left: string; scale?: number; delay?: string; color?: string
}) {
  const w = 44 * scale, h = 64 * scale
  return (
    <div style={{
      position: 'absolute', top: 0, left, zIndex: 5,
      transformOrigin: 'top center',
      animation: `lantern-sway ${3 + scale}s ${delay} ease-in-out infinite`,
    }}>
      <div style={{ width: 2, height: 28 * scale, background: '#FFD700', margin: '0 auto', opacity: 0.8 }} />
      <div style={{
        width: w, height: h,
        background: `radial-gradient(ellipse at 40% 35%, ${color}dd, ${color})`,
        borderRadius: '50% 50% 44% 44%',
        border: `${2 * scale}px solid #FFD700`,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        position: 'relative',
        boxShadow: `0 0 ${18 * scale}px ${color}aa, 0 0 ${36 * scale}px ${color}55`,
      }}>
        <span style={{ fontSize: 16 * scale, color: '#FFD700', fontFamily: 'serif', lineHeight: 1 }}>福</span>
        {[0.3, 0.55, 0.78].map(y => (
          <div key={y} style={{
            position: 'absolute', top: `${y * 100}%`, left: 0, right: 0,
            height: 1, background: '#FFD700', opacity: 0.35,
          }} />
        ))}
      </div>
      <div style={{ display: 'flex', justifyContent: 'center', gap: 3 * scale }}>
        {[0, 1, 2, 3].map(i => (
          <div key={i} style={{
            width: 1.5 * scale, height: 16 * scale,
            background: '#FFD700', borderRadius: 1, opacity: 0.8,
          }} />
        ))}
      </div>
    </div>
  )
}

// ─── Envelope SVG ────────────────────────────────────────────────────────────

function EnvelopeSVG({ size = 220, sealed = true }: { size?: number; sealed?: boolean }) {
  const w = size, h = size * 1.4
  return (
    <svg width={w} height={h} viewBox="0 0 220 308" fill="none" xmlns="http://www.w3.org/2000/svg">
      {/* Shadow */}
      <ellipse cx="110" cy="302" rx="90" ry="8" fill="rgba(0,0,0,0.35)" />
      {/* Body */}
      <rect x="3" y="3" width="214" height="302" rx="14" ry="14" fill="#C41E3A" />
      {/* Subtle inner gradient */}
      <rect x="3" y="3" width="214" height="302" rx="14" ry="14"
        fill="url(#bodyGrad)" />
      {/* Gold outer border */}
      <rect x="3" y="3" width="214" height="302" rx="14" ry="14"
        fill="none" stroke="#FFD700" strokeWidth="4" />
      {/* Inner border */}
      <rect x="11" y="11" width="198" height="286" rx="9" ry="9"
        fill="none" stroke="#FFD700" strokeWidth="1.5" opacity="0.55" />
      {/* Flap divider */}
      {sealed && <line x1="3" y1="102" x2="217" y2="102" stroke="#FFD700" strokeWidth="1.5" opacity="0.6" />}
      {/* Flap chevron */}
      {sealed && (
        <path d="M3 3 L110 95 L217 3" fill="#B01020" stroke="#FFD700" strokeWidth="2" strokeLinejoin="round" />
      )}
      {/* Center diamond */}
      <path d="M110 22 L138 62 L110 102 L82 62 Z" fill="#FFD700" opacity="0.92" />
      <path d="M110 34 L130 62 L110 90 L90 62 Z" fill="#C41E3A" />
      <circle cx="110" cy="62" r="7" fill="#FFD700" />
      <circle cx="110" cy="62" r="3.5" fill="#C41E3A" />
      {/* Top corner ornaments */}
      <circle cx="22" cy="22" r="7" fill="#FFD700" opacity="0.7" />
      <circle cx="198" cy="22" r="7" fill="#FFD700" opacity="0.7" />
      {/* 福 character */}
      <text x="110" y="192" textAnchor="middle" fontSize="78" fill="#FFD700"
        fontFamily="serif" fontWeight="bold" opacity="0.97">福</text>
      {/* Lì Xì label */}
      <text x="110" y="228" textAnchor="middle" fontSize="18" fill="#FFD700"
        fontFamily="serif" letterSpacing="3" opacity="0.9">Lì Xì</text>
      {/* Bottom dots */}
      <circle cx="68" cy="264" r="3.5" fill="#FFD700" opacity="0.55" />
      <circle cx="110" cy="264" r="3.5" fill="#FFD700" opacity="0.55" />
      <circle cx="152" cy="264" r="3.5" fill="#FFD700" opacity="0.55" />
      {/* Bottom corners */}
      <circle cx="22" cy="289" r="6" fill="#FFD700" opacity="0.65" />
      <circle cx="198" cy="289" r="6" fill="#FFD700" opacity="0.65" />
      {/* Gradient def */}
      <defs>
        <radialGradient id="bodyGrad" cx="30%" cy="25%" r="80%">
          <stop offset="0%" stopColor="#E02040" stopOpacity="0.6" />
          <stop offset="100%" stopColor="#8B0000" stopOpacity="0.3" />
        </radialGradient>
      </defs>
    </svg>
  )
}

// ─── Money Bill SVG ───────────────────────────────────────────────────────────

function MoneyBill({ amount, width = 160 }: { amount: number; width?: number }) {
  const h = width * 0.46
  return (
    <svg width={width} height={h} viewBox="0 0 160 74" fill="none">
      <rect x="1" y="1" width="158" height="72" rx="6" fill="#2D8B4E" />
      <rect x="1" y="1" width="158" height="72" rx="6" fill="none" stroke="#1A5C33" strokeWidth="2" />
      <rect x="8" y="8" width="144" height="58" rx="3" fill="none" stroke="#3AAD64" strokeWidth="1" opacity="0.5" />
      <circle cx="80" cy="37" r="18" fill="none" stroke="#3AAD64" strokeWidth="1.5" opacity="0.4" />
      <text x="80" y="31" textAnchor="middle" fontSize="9" fill="#90EE90" fontFamily="serif" opacity="0.7">SOCIALIST REPUBLIC</text>
      <text x="80" y="42" textAnchor="middle" fontSize="14" fill="#90EE90" fontFamily="serif" fontWeight="bold">VN</text>
      <text x="22" y="43" textAnchor="middle" fontSize="13" fill="#FFD700" fontFamily="monospace" fontWeight="bold">
        {amount >= 1_000_000 ? `${amount / 1_000_000}M` : `${Math.round(amount / 1000)}K`}
      </text>
      <text x="138" y="43" textAnchor="middle" fontSize="13" fill="#FFD700" fontFamily="monospace" fontWeight="bold">
        {amount >= 1_000_000 ? `${amount / 1_000_000}M` : `${Math.round(amount / 1000)}K`}
      </text>
      <text x="80" y="60" textAnchor="middle" fontSize="8" fill="#90EE90" fontFamily="sans-serif" opacity="0.6">NGÂN HÀNG NHÀ NƯỚC VIỆT NAM</text>
    </svg>
  )
}

// ─── Fireworks / Particles ────────────────────────────────────────────────────

function Fireworks({ active }: { active: boolean }) {
  const particles = useMemo(() => {
    const colors = ['#FFD700', '#FF6B6B', '#FF4500', '#FFA500', '#FF69B4', '#00CED1', '#7CFC00']
    return Array.from({ length: 32 }, (_, i) => {
      const angle = (i / 32) * Math.PI * 2
      const dist = 80 + Math.random() * 60
      return {
        id: i,
        color: colors[i % colors.length],
        px: `${Math.cos(angle) * dist}px`,
        py: `${Math.sin(angle) * dist}px`,
        size: 4 + Math.random() * 5,
        delay: `${Math.random() * 0.15}s`,
      }
    })
  }, [])

  const confetti = useMemo(() =>
    Array.from({ length: 20 }, (_, i) => ({
      id: i,
      left: `${20 + (i / 20) * 60}%`,
      color: ['#FFD700', '#FF6B6B', '#FF4500', '#00CED1', '#FF69B4'][i % 5],
      size: 5 + (i % 4) * 2,
      delay: `${(i * 0.08) % 0.5}s`,
      rotation: `${(i * 137) % 720}deg`,
    })), [])

  if (!active) return null
  return (
    <>
      {/* Ring burst */}
      <div style={{
        position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center',
        pointerEvents: 'none', zIndex: 20,
      }}>
        <div style={{
          width: 80, height: 80, borderRadius: '50%',
          border: '3px solid #FFD700',
          animation: 'ring-burst 0.7s ease-out forwards',
        }} />
        <div style={{
          position: 'absolute', width: 60, height: 60, borderRadius: '50%',
          border: '2px solid #FF4500',
          animation: 'ring-burst 0.7s 0.1s ease-out forwards',
        }} />
      </div>
      {/* Particles */}
      <div style={{
        position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center',
        pointerEvents: 'none', zIndex: 21, overflow: 'hidden',
      }}>
        {particles.map(p => (
          <div key={p.id} style={{
            position: 'absolute',
            width: p.size, height: p.size,
            borderRadius: '50%',
            background: p.color,
            '--px': p.px, '--py': p.py,
            animation: `particle-burst 0.7s ${p.delay} ease-out forwards`,
          } as React.CSSProperties} />
        ))}
      </div>
      {/* Confetti */}
      <div style={{ position: 'absolute', inset: 0, overflow: 'hidden', pointerEvents: 'none', zIndex: 19 }}>
        {confetti.map(c => (
          <div key={c.id} style={{
            position: 'absolute',
            left: c.left, top: '-10px',
            width: c.size, height: c.size * 0.5,
            background: c.color,
            borderRadius: 2,
            '--cr': c.rotation,
            animation: `confetti-drop 1s ${c.delay} ease-in forwards`,
          } as React.CSSProperties} />
        ))}
      </div>
    </>
  )
}

// ─── WELCOME SCREEN ───────────────────────────────────────────────────────────

function WelcomeScreen({ onStart }: { onStart: () => void }) {
  return (
    <div style={{
      position: 'fixed', inset: 0,
      background: 'radial-gradient(ellipse 120% 90% at 50% 0%, #9A0000 0%, #6B0000 35%, #3D0000 100%)',
      display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      fontFamily: '"Be Vietnam Pro", sans-serif',
      overflow: 'hidden',
    }}>
      <Stars count={60} />
      <PetalRain count={32} />

      {/* Top gold bar */}
      <div style={{
        position: 'absolute', top: 0, left: 0, right: 0, height: 5,
        background: 'linear-gradient(90deg, transparent 0%, #FFD700 20%, #FFA500 50%, #FFD700 80%, transparent 100%)',
      }} />
      {/* Bottom gold bar */}
      <div style={{
        position: 'absolute', bottom: 0, left: 0, right: 0, height: 5,
        background: 'linear-gradient(90deg, transparent 0%, #FFD700 20%, #FFA500 50%, #FFD700 80%, transparent 100%)',
      }} />

      {/* Lanterns */}
      <Lantern left="-12px" scale={1.3} delay="0s" color="#C41E3A" />
      <Lantern left="calc(100% - 66px)" scale={1.1} delay="0.6s" color="#A01020" />
      <Lantern left="15%" scale={0.75} delay="1.2s" color="#B01828" />
      <Lantern left="calc(85% - 36px)" scale={0.8} delay="0.3s" color="#8B0000" />

      {/* Main content */}
      <div style={{
        display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 18,
        padding: '0 32px', maxWidth: 480, width: '100%', textAlign: 'center',
        animation: 'slide-up 0.9s ease-out', zIndex: 10,
      }}>
        {/* Year pill */}
        <div style={{
          background: 'linear-gradient(135deg, #FFD700, #FFA500)',
          color: '#7A0000', padding: '6px 22px', borderRadius: 999,
          fontSize: 13, fontWeight: 800, letterSpacing: 3,
          animation: 'float 3s ease-in-out infinite',
        }}>
          ✦ ẤT TỴ — 2025 ✦
        </div>

        {/* Title */}
        <h1 style={{
          fontSize: 'clamp(40px, 10vw, 76px)',
          fontFamily: '"Noto Serif Display", serif',
          fontWeight: 900, color: '#FFD700',
          lineHeight: 1.08, margin: 0,
          animation: 'title-glow 2.5s ease-in-out infinite',
          textShadow: '0 2px 40px #FF8C00aa',
        }}>
          Chúc Mừng<br />Năm Mới
        </h1>

        {/* Divider */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 14, width: '100%' }}>
          <div style={{ flex: 1, height: 1, background: 'linear-gradient(90deg, transparent, #FFD700aa)' }} />
          <span style={{ fontSize: 22 }}>🧧</span>
          <div style={{ flex: 1, height: 1, background: 'linear-gradient(90deg, #FFD700aa, transparent)' }} />
        </div>

        {/* Tagline */}
        <p style={{
          color: '#FFDDB5', fontSize: 15, margin: 0, fontStyle: 'italic',
          animation: 'float-slow 4s ease-in-out infinite 1s',
        }}>
          Phát tài phát lộc — Vạn sự như ý
        </p>

        {/* Floating envelope */}
        <div style={{ animation: 'float-slow 3.5s ease-in-out infinite 0.5s', filter: 'drop-shadow(0 8px 24px #FF000055)' }}>
          <EnvelopeSVG size={160} />
        </div>

        {/* Start button */}
        <button onClick={onStart} style={{
          background: 'linear-gradient(135deg, #FFD700 0%, #FFA500 60%, #FF8C00 100%)',
          color: '#6B0000', border: 'none',
          padding: '18px 56px', borderRadius: 999,
          fontSize: 22, fontWeight: 800,
          cursor: 'pointer', fontFamily: '"Be Vietnam Pro", sans-serif',
          letterSpacing: 1.5, display: 'flex', alignItems: 'center', gap: 10,
          animation: 'btn-pulse 2s ease-in-out infinite',
          transition: 'transform 0.12s ease',
          touchAction: 'manipulation',
          WebkitAppearance: 'none',
        }}
          onPointerDown={e => { e.currentTarget.style.transform = 'scale(0.95)' }}
          onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)' }}
          onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
        >
          <span>🧧</span>
          <span>Bắt Đầu</span>
        </button>

        <p style={{ color: '#FFD70055', fontSize: 11, margin: 0, letterSpacing: 3, textTransform: 'uppercase' }}>
          ◆ Trao Lì Xì ◆ Chúc Phúc ◆
        </p>
      </div>
    </div>
  )
}

// ─── SETUP SCREEN ────────────────────────────────────────────────────────────

function SetupScreen({ onComplete }: { onComplete: (envelopes: Envelope[]) => void }) {
  const [phase, setPhase] = useState<'count' | 'amounts'>('count')
  const [countStr, setCountStr] = useState('')
  const [total, setTotal] = useState(0)
  const [amounts, setAmounts] = useState<number[]>([])
  const [amountStr, setAmountStr] = useState('')
  const [envelopeAnim, setEnvelopeAnim] = useState<'arrive' | 'absorb' | 'idle'>('idle')
  const [moneyAnim, setMoneyAnim] = useState(false)
  const [allDone, setAllDone] = useState(false)
  const inputRef = useRef<HTMLInputElement>(null)
  const currentIdx = amounts.length

  // focus input when visible
  useEffect(() => {
    if (phase === 'count' || (phase === 'amounts' && envelopeAnim === 'arrive')) return
    inputRef.current?.focus()
  }, [phase, envelopeAnim])

  const handleCountConfirm = () => {
    const n = parseInt(countStr)
    if (!n || n < 1 || n > 20) return
    setTotal(n)
    setPhase('amounts')
    setEnvelopeAnim('arrive')
    setTimeout(() => setEnvelopeAnim('idle'), 700)
    setTimeout(() => inputRef.current?.focus(), 750)
  }

  const handleAmountConfirm = useCallback(() => {
    const raw = amountStr.replace(/\D/g, '')
    const val = parseInt(raw)
    if (!val || val <= 0) return

    setMoneyAnim(true)
    setTimeout(() => {
      setMoneyAnim(false)
      setEnvelopeAnim('absorb')
      setTimeout(() => {
        const next = [...amounts, val]
        setAmounts(next)
        setAmountStr('')
        if (next.length >= total) {
          setAllDone(true)
          setEnvelopeAnim('idle')
        } else {
          setEnvelopeAnim('arrive')
          setTimeout(() => { setEnvelopeAnim('idle'); inputRef.current?.focus() }, 650)
        }
      }, 400)
    }, 800)
  }, [amountStr, amounts, total])

  const handleComplete = () => {
    const shuffledOrder = shuffle(Array.from({ length: total }, (_, i) => i))
    const envelopes: Envelope[] = amounts.map((amount, i) => ({
      id: i,
      amount,
      opened: false,
      shuffleOrder: shuffledOrder[i],
      dealX: (Math.random() - 0.5) * 180,
      dealY: (Math.random() - 0.5) * 120,
      dealR: (Math.random() - 0.5) * 36,
    }))
    onComplete(envelopes)
  }

  const envStyle: React.CSSProperties = {
    animation: envelopeAnim === 'arrive' ? 'envelope-arrive 0.65s cubic-bezier(0.22,1,0.36,1) forwards'
      : envelopeAnim === 'absorb' ? 'envelope-absorb 0.4s ease-out'
      : 'none',
    display: 'flex', justifyContent: 'center',
    filter: 'drop-shadow(0 12px 32px #FF000066)',
  }

  return (
    <div style={{
      position: 'fixed', inset: 0, overflow: 'hidden',
      background: 'radial-gradient(ellipse 130% 90% at 50% -10%, #8B0000 0%, #4D0000 50%, #2A0000 100%)',
      display: 'flex', flexDirection: 'column',
      fontFamily: '"Be Vietnam Pro", sans-serif',
    }}>
      <Stars count={35} />
      <PetalRain count={16} />

      {/* Top gold bar */}
      <div style={{
        position: 'absolute', top: 0, left: 0, right: 0, height: 4, zIndex: 20,
        background: 'linear-gradient(90deg, transparent, #FFD700, #FFA500, #FFD700, transparent)',
      }} />

      {/* All content scrolls inside here */}
      <div style={{
        flex: 1, overflowY: 'auto', overflowX: 'hidden', scrollbarWidth: 'none',
        display: 'flex', flexDirection: 'column', alignItems: 'center',
        padding: '56px 24px 48px', gap: 24, zIndex: 10,
      }}>

        {/* Header */}
        <div style={{ textAlign: 'center', animation: 'slide-up 0.6s ease-out' }}>
          <div style={{ fontSize: 12, color: '#FFD700aa', letterSpacing: 3, marginBottom: 8, textTransform: 'uppercase' }}>
            Chuẩn Bị Lì Xì
          </div>
          <h2 style={{
            fontFamily: '"Noto Serif Display", serif', fontWeight: 900,
            fontSize: 'clamp(22px, 5.5vw, 30px)',
            color: '#FFD700', margin: 0,
            textShadow: '0 0 20px #FF8C0060',
          }}>
            {phase === 'count'
              ? 'Bạn muốn tặng mấy phong bì?'
              : allDone
              ? '🎉 Tất cả đã sẵn sàng!'
              : `Nhập tiền cho phong bì #${currentIdx + 1}`}
          </h2>
        </div>

        {/* ── Count phase ── */}
        {phase === 'count' && (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 20, animation: 'scale-in 0.5s ease-out' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
              <button
                style={counterBtnStyle}
                onPointerDown={e => { e.currentTarget.style.transform = 'scale(0.9)' }}
                onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)'; setCountStr(String(Math.max(1, (parseInt(countStr) || 0) - 1))) }}
                onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
              >−</button>
              <input
                type="number" min={1} max={20}
                value={countStr}
                onChange={e => setCountStr(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && handleCountConfirm()}
                placeholder="0"
                style={{
                  width: 96, height: 72, textAlign: 'center',
                  fontSize: 42, fontWeight: 800, color: '#FFD700',
                  background: 'rgba(255,215,0,0.08)',
                  border: '2px solid #FFD70055', borderRadius: 16,
                  outline: 'none',
                  fontFamily: '"Be Vietnam Pro", sans-serif',
                }}
              />
              <button
                style={counterBtnStyle}
                onPointerDown={e => { e.currentTarget.style.transform = 'scale(0.9)' }}
                onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)'; setCountStr(String(Math.min(20, (parseInt(countStr) || 0) + 1))) }}
                onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
              >+</button>
            </div>
            <p style={{ color: '#FFD70077', fontSize: 13, margin: 0 }}>Tối đa 20 phong bì</p>
            <button
              onClick={handleCountConfirm}
              disabled={!parseInt(countStr) || parseInt(countStr) < 1}
              style={primaryBtnStyle(!!parseInt(countStr) && parseInt(countStr) >= 1)}
              onPointerDown={e => { if (parseInt(countStr) >= 1) e.currentTarget.style.transform = 'scale(0.95)' }}
              onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)' }}
              onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
            >
              Xác nhận ✓
            </button>
          </div>
        )}

        {/* ── Amounts phase ── */}
        {phase === 'amounts' && (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 18, width: '100%', maxWidth: 360 }}>

            {/* Progress bar */}
            <div style={{ width: '100%', display: 'flex', gap: 5 }}>
              {Array.from({ length: total }, (_, i) => (
                <div key={i} style={{ flex: 1, height: 5, borderRadius: 999, overflow: 'hidden', background: '#FFD70018' }}>
                  <div style={{
                    width: '100%', height: '100%', borderRadius: 999,
                    background: i < amounts.length ? '#FFD700' : i === amounts.length && !allDone ? '#FFA50070' : 'transparent',
                    transition: 'background 0.3s',
                  }} />
                </div>
              ))}
            </div>
            <p style={{ color: '#FFD70088', fontSize: 12, margin: '-6px 0 0', letterSpacing: 1 }}>
              {amounts.length} / {total} phong bì
            </p>

            {/* Envelope + money */}
            {!allDone && (
              <div style={{ position: 'relative', display: 'flex', justifyContent: 'center' }}>
                {moneyAnim && (
                  <div style={{ position: 'absolute', top: -64, zIndex: 20, animation: 'money-fly-in 0.8s ease-in forwards' }}>
                    <MoneyBill amount={parseInt(amountStr.replace(/\D/g, '')) || 0} width={130} />
                  </div>
                )}
                <div style={envStyle}>
                  <EnvelopeSVG size={170} />
                </div>
              </div>
            )}

            {/* Fan of completed envelopes */}
            {allDone && (
              <div style={{ display: 'flex', justifyContent: 'center', animation: 'scale-in 0.5s ease-out' }}>
                {amounts.slice(0, Math.min(amounts.length, 6)).map((_, i) => (
                  <div key={i} style={{
                    marginLeft: i > 0 ? -28 : 0,
                    transform: `rotate(${(i - Math.min(amounts.length, 6) / 2) * 9}deg)`,
                    filter: 'drop-shadow(0 4px 10px #00000055)',
                    zIndex: i,
                  }}>
                    <EnvelopeSVG size={76} />
                  </div>
                ))}
              </div>
            )}

            {/* Amount input */}
            {!allDone && (
              <div style={{ width: '100%', display: 'flex', flexDirection: 'column', gap: 10 }}>
                <div style={{ position: 'relative' }}>
                  <span style={{
                    position: 'absolute', left: 14, top: '50%', transform: 'translateY(-50%)',
                    color: '#FFD700aa', fontSize: 18, fontWeight: 700, pointerEvents: 'none',
                  }}>₫</span>
                  <input
                    ref={inputRef}
                    type="text" inputMode="numeric"
                    value={amountStr}
                    onChange={e => setAmountStr(e.target.value.replace(/\D/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, '.'))}
                    onKeyDown={e => e.key === 'Enter' && handleAmountConfirm()}
                    placeholder="Nhập số tiền..."
                    disabled={moneyAnim}
                    style={{
                      width: '100%', height: 58, paddingLeft: 38, paddingRight: 14,
                      fontSize: 22, fontWeight: 700, color: '#FFD700',
                      background: 'rgba(255,215,0,0.07)',
                      border: '2px solid #FFD70055', borderRadius: 14,
                      outline: 'none', boxSizing: 'border-box',
                      fontFamily: '"Be Vietnam Pro", sans-serif',
                      transition: 'border-color 0.2s',
                    }}
                    onFocus={e => (e.target.style.borderColor = '#FFD700')}
                    onBlur={e => (e.target.style.borderColor = '#FFD70055')}
                  />
                </div>
                {/* Quick amounts */}
                <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', justifyContent: 'center' }}>
                  {[50000, 100000, 200000, 500000].map(amt => (
                    <button key={amt}
                      onClick={() => setAmountStr(amt.toLocaleString('vi-VN').replace(/\./g, '.'))}
                      style={{
                        background: 'rgba(255,215,0,0.12)', border: '1px solid #FFD70040',
                        color: '#FFD700', borderRadius: 8, padding: '8px 14px',
                        fontSize: 13, fontWeight: 600, cursor: 'pointer',
                        fontFamily: '"Be Vietnam Pro", sans-serif',
                        touchAction: 'manipulation', minHeight: 40,
                        transition: 'transform 0.1s',
                      }}
                      onPointerDown={e => { e.currentTarget.style.transform = 'scale(0.93)'; e.currentTarget.style.background = 'rgba(255,215,0,0.25)' }}
                      onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)'; e.currentTarget.style.background = 'rgba(255,215,0,0.12)' }}
                      onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)'; e.currentTarget.style.background = 'rgba(255,215,0,0.12)' }}
                    >
                      {amt >= 1_000_000 ? `${amt / 1_000_000}M` : `${amt / 1000}K`}
                    </button>
                  ))}
                </div>
                <button
                  onClick={handleAmountConfirm}
                  disabled={moneyAnim || !amountStr}
                  style={primaryBtnStyle(!moneyAnim && !!amountStr)}
                  onPointerDown={e => { if (!moneyAnim && amountStr) e.currentTarget.style.transform = 'scale(0.95)' }}
                  onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)' }}
                  onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
                >
                  {moneyAnim ? '✨ Đang bỏ vào...' : 'Bỏ vào phong bì 🧧'}
                </button>
              </div>
            )}

            {/* Complete */}
            {allDone && (
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12, animation: 'slide-up 0.5s ease-out' }}>
                <p style={{ color: '#FFD700', fontSize: 15, margin: 0, textAlign: 'center' }}>
                  {total} phong bì — tổng{' '}
                  <strong>{formatMoney(amounts.reduce((a, b) => a + b, 0))}</strong>
                </p>
                <button
                  onClick={handleComplete}
                  style={{
                    ...primaryBtnStyle(true),
                    fontSize: 19, padding: '16px 44px',
                    animation: 'btn-pulse 2s ease-in-out infinite',
                  }}
                  onPointerDown={e => { e.currentTarget.style.transform = 'scale(0.95)' }}
                  onPointerUp={e => { e.currentTarget.style.transform = 'scale(1)' }}
                  onPointerCancel={e => { e.currentTarget.style.transform = 'scale(1)' }}
                >
                  🎊 Hoàn tất & Xáo bài
                </button>
              </div>
            )}
          </div>
        )}

      </div>{/* /scrollable */}
    </div>
  )
}

// ─── SHUFFLE SCREEN ───────────────────────────────────────────────────────────

function ShuffleScreen({ envelopes, onDone }: { envelopes: Envelope[]; onDone: () => void }) {
  const [phase, setPhase] = useState<'pile' | 'shuffling' | 'dealing' | 'done'>('pile')
  const count = envelopes.length

  useEffect(() => {
    const t1 = setTimeout(() => setPhase('shuffling'), 600)
    const t2 = setTimeout(() => setPhase('dealing'), 2600)
    const t3 = setTimeout(() => setPhase('done'), 3800)
    const t4 = setTimeout(() => onDone(), 4400)
    return () => { clearTimeout(t1); clearTimeout(t2); clearTimeout(t3); clearTimeout(t4) }
  }, [onDone])

  // Grid layout for dealt envelopes
  const cols = Math.min(count, 4)
  const rows = Math.ceil(count / cols)

  const getCardStyle = (i: number): React.CSSProperties => {
    const env = envelopes[i]
    if (phase === 'pile') {
      return {
        position: 'absolute',
        transform: `rotate(${(i - count / 2) * 3}deg) translateY(${(i % 3) * 2}px)`,
        transition: 'transform 0.4s ease',
        zIndex: i,
      }
    }
    if (phase === 'shuffling') {
      return {
        position: 'absolute',
        '--sx1': `${env.dealX * 1.5}px`,
        '--sy1': `${env.dealY * 1.2}px`,
        '--sr1': `${env.dealR * 2}deg`,
        '--sx2': `${env.dealX * -0.8}px`,
        '--sy2': `${env.dealY * -0.6}px`,
        '--sr2': `${env.dealR * -1.5}deg`,
        '--sx3': `${env.dealX * 0.5}px`,
        '--sy3': `${env.dealY * 0.9}px`,
        '--sr3': `${env.dealR * 1.2}deg`,
        animation: `shuffle-move 0.55s ${i * 0.04}s ease-in-out infinite`,
        zIndex: i,
      } as React.CSSProperties
    }
    if (phase === 'dealing' || phase === 'done') {
      const col = i % cols
      const row = Math.floor(i / cols)
      const cardW = 100, cardH = 140
      const gapX = 18, gapY = 20
      const totalW = cols * cardW + (cols - 1) * gapX
      const totalH = rows * cardH + (rows - 1) * gapY
      const x = col * (cardW + gapX) - totalW / 2 + cardW / 2
      const y = row * (cardH + gapY) - totalH / 2 + cardH / 2
      return {
        position: 'absolute',
        '--dx': `${x}px`,
        '--dy': `${y}px`,
        '--dr': `${(i % 2 === 0 ? 1 : -1) * (i % 3) * 3}deg`,
        animation: `deal-out 0.6s ${i * 0.07}s cubic-bezier(0.22,1,0.36,1) both`,
        zIndex: i,
      } as React.CSSProperties
    }
    return {}
  }

  return (
    <div style={{
      position: 'fixed', inset: 0,
      background: 'radial-gradient(ellipse 130% 100% at 50% 20%, #8B0000, #4D0000 50%, #1A0000)',
      display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      fontFamily: '"Be Vietnam Pro", sans-serif',
      overflow: 'hidden',
    }}>
      <Stars count={40} />
      <PetalRain count={20} />

      <div style={{ textAlign: 'center', marginBottom: 40, zIndex: 10 }}>
        <h2 style={{
          fontFamily: '"Noto Serif Display", serif', fontSize: 30, fontWeight: 900,
          color: '#FFD700', margin: 0, animation: 'title-glow 2s infinite',
        }}>
          {phase === 'pile' ? '🎴 Đang xếp bài...' :
            phase === 'shuffling' ? '🔀 Xáo bài...' :
            phase === 'dealing' ? '✨ Chia bài...' : '🎉 Xong rồi!'}
        </h2>
      </div>

      {/* Card pile */}
      <div style={{
        position: 'relative', width: 120, height: 168,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        zIndex: 10,
      }}>
        {envelopes.map((_, i) => (
          <div key={i} style={getCardStyle(i)}>
            <EnvelopeSVG size={100} />
          </div>
        ))}
      </div>
    </div>
  )
}

// ─── SELECTION SCREEN ─────────────────────────────────────────────────────────

type OpenState = 'closed' | 'shaking' | 'opening' | 'opened'

function SelectionScreen({ envelopes, onFinish }: {
  envelopes: Envelope[]
  onFinish: () => void
}) {
  const [openStates, setOpenStates] = useState<OpenState[]>(envelopes.map(() => 'closed'))
  const [fireworks, setFireworks] = useState<boolean[]>(envelopes.map(() => false))
  const scrollRef = useRef<HTMLDivElement>(null)

  const allOpened = openStates.every(s => s === 'opened')

  const handleTap = useCallback((idx: number) => {
    if (openStates[idx] !== 'closed') return
    // Shake
    setOpenStates(prev => prev.map((s, i) => i === idx ? 'shaking' : s))
    setTimeout(() => {
      // Open flap
      setOpenStates(prev => prev.map((s, i) => i === idx ? 'opening' : s))
      setTimeout(() => {
        // Reveal + fireworks
        setOpenStates(prev => prev.map((s, i) => i === idx ? 'opened' : s))
        setFireworks(prev => prev.map((f, i) => i === idx ? true : f))
        setTimeout(() => setFireworks(prev => prev.map((f, i) => i === idx ? false : f)), 1200)
      }, 900)
    }, 600)
  }, [openStates])

  const sorted = useMemo(() => [...envelopes].sort((a, b) => a.shuffleOrder - b.shuffleOrder), [envelopes])
  const sortedIndices = useMemo(() => sorted.map(e => envelopes.indexOf(e)), [sorted, envelopes])

  return (
    <div style={{
      position: 'fixed', inset: 0,
      background: 'radial-gradient(ellipse 130% 100% at 50% 10%, #8B0000, #3D0000 60%, #1A0000)',
      fontFamily: '"Be Vietnam Pro", sans-serif',
      overflow: 'hidden',
    }}>
      <Stars count={30} />
      <PetalRain count={14} />

      {/* Header */}
      <div style={{
        position: 'absolute', top: 0, left: 0, right: 0, zIndex: 20,
        padding: '16px 24px',
        background: 'linear-gradient(180deg, #3D000099 0%, transparent 100%)',
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      }}>
        <h2 style={{
          fontFamily: '"Noto Serif Display", serif', fontSize: 20, fontWeight: 900,
          color: '#FFD700', margin: 0,
        }}>
          🧧 Chọn Phong Bì
        </h2>
        <span style={{ color: '#FFD700aa', fontSize: 13 }}>
          {openStates.filter(s => s === 'opened').length}/{envelopes.length} đã mở
        </span>
      </div>

      {/* Horizontal scroll */}
      <div ref={scrollRef} className="scrollbar-none" style={{
        width: '100%', height: '100%',
        display: 'flex', overflowX: 'scroll',
        scrollSnapType: 'x mandatory',
        WebkitOverflowScrolling: 'touch',
        alignItems: 'center',
      }}>
        {sorted.map((env, displayIdx) => {
          const origIdx = sortedIndices[displayIdx]
          const state = openStates[origIdx]
          const fwActive = fireworks[origIdx]
          return (
            <div key={env.id} style={{
              flex: '0 0 100vw',
              scrollSnapAlign: 'center',
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              position: 'relative',
              gap: 0,
            }}>
              <EnvelopeCard
                env={env}
                state={state}
                fwActive={fwActive}
                onTap={() => handleTap(origIdx)}
                displayIdx={displayIdx}
                total={sorted.length}
              />
            </div>
          )
        })}
      </div>

      {/* Scroll hint */}
      {!allOpened && envelopes.length > 1 && (
        <div style={{
          position: 'absolute', bottom: 24, left: 0, right: 0,
          display: 'flex', justifyContent: 'center', gap: 8,
          pointerEvents: 'none',
        }}>
          <div style={{ animation: 'hint-bounce 1.5s ease-in-out infinite', color: '#FFD70088', fontSize: 12, letterSpacing: 2 }}>
            ◀ vuốt để xem thêm ▶
          </div>
        </div>
      )}

      {/* Finish button */}
      {allOpened && (
        <div style={{
          position: 'absolute', bottom: 36, left: 0, right: 0,
          display: 'flex', justifyContent: 'center', zIndex: 30,
          animation: 'slide-up 0.6s ease-out',
        }}>
          <button onClick={onFinish} style={{
            ...primaryBtnStyle(true),
            fontSize: 20, padding: '16px 52px',
            animation: 'btn-pulse 2s ease-in-out infinite',
          }}>
            🎊 Kết Thúc
          </button>
        </div>
      )}
    </div>
  )
}

// ─── ENVELOPE CARD (selection screen) ────────────────────────────────────────

function EnvelopeCard({ env, state, fwActive, onTap, displayIdx, total }: {
  env: Envelope
  state: OpenState
  fwActive: boolean
  onTap: () => void
  displayIdx: number
  total: number
}) {
  const isOpen = state === 'opened'

  return (
    <div style={{
      display: 'flex', flexDirection: 'column', alignItems: 'center',
      gap: 24, padding: '80px 32px 80px', width: '100%',
      position: 'relative',
    }}>
      {/* Number indicator */}
      <div style={{
        fontSize: 12, color: '#FFD70066', letterSpacing: 2, textTransform: 'uppercase',
      }}>
        Phong bì {displayIdx + 1} / {total}
      </div>

      {/* Envelope + content */}
      <div style={{ position: 'relative', cursor: state === 'closed' ? 'pointer' : 'default' }}>

        {/* Glow aura */}
        {state === 'closed' && (
          <div style={{
            position: 'absolute', inset: -20, borderRadius: '50%',
            background: 'radial-gradient(circle, #FFD70020 30%, transparent 70%)',
            animation: 'float 3s ease-in-out infinite',
            pointerEvents: 'none',
          }} />
        )}

        {/* Shake wrapper */}
        <div style={{
          animation: state === 'shaking' ? 'envelope-shake 0.6s ease-out' : 'none',
        }}>
          {/* 3D perspective wrapper for flap */}
          <div style={{ position: 'relative', display: 'inline-block' }} onClick={onTap}>

            {/* Flap layer — animated open */}
            {(state === 'opening' || isOpen) && (
              <div style={{
                position: 'absolute',
                top: 0, left: 0, right: 0,
                height: '45%',
                zIndex: 10,
                transformOrigin: 'top center',
                animation: state === 'opening' ? 'flap-open 0.9s cubic-bezier(0.4,0,0.2,1) forwards' : 'none',
                transform: isOpen ? 'perspective(500px) rotateX(-185deg)' : 'none',
                overflow: 'hidden',
              }}>
                {/* Flap shape - mirrors envelope top */}
                <svg width="240" height="108" viewBox="0 0 240 108">
                  <path d="M0 0 L120 95 L240 0 Z" fill="#B01020" />
                  <path d="M0 0 L120 95 L240 0 Z" fill="none" stroke="#FFD700" strokeWidth="2.5" />
                  <path d="M120 22 L148 62 L120 102 L92 62 Z" fill="#FFD700" opacity="0.92" />
                  <path d="M120 34 L140 62 L120 90 L100 62 Z" fill="#C41E3A" />
                  <circle cx="120" cy="62" r="7" fill="#FFD700" />
                </svg>
              </div>
            )}

            {/* Main envelope body */}
            <div style={{
              filter: isOpen ? 'brightness(0.85)' : state === 'closed' ? 'none' : 'none',
            }}>
              <EnvelopeSVG size={240} sealed={state === 'closed' || state === 'shaking'} />
            </div>

            {/* Money emerging from inside */}
            {(state === 'opening' || isOpen) && (
              <div style={{
                position: 'absolute',
                top: '12%', left: '50%', transform: 'translateX(-50%)',
                zIndex: 8,
                animation: 'money-emerge 1s 0.5s ease-out both',
                pointerEvents: 'none',
              }}>
                <MoneyBill amount={env.amount} width={160} />
                {/* Second bill */}
                <div style={{ marginTop: -8, transform: 'rotate(4deg)', opacity: 0.85 }}>
                  <MoneyBill amount={env.amount} width={160} />
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Fireworks overlay */}
        <Fireworks active={fwActive} />

        {/* Flash overlay */}
        {state === 'opening' && (
          <div style={{
            position: 'absolute', inset: -40, borderRadius: 24,
            animation: 'flash-gold 0.4s 0.5s ease-out',
            pointerEvents: 'none',
          }} />
        )}
      </div>

      {/* Amount reveal */}
      {isOpen ? (
        <div style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8,
          animation: 'amount-pop 0.7s 0.8s cubic-bezier(0.22,1,0.36,1) both',
        }}>
          <div style={{
            background: 'linear-gradient(135deg, #FFD700, #FFA500)',
            borderRadius: 999, padding: '4px 20px',
            fontSize: 11, fontWeight: 800, color: '#7A0000', letterSpacing: 2,
            textTransform: 'uppercase',
          }}>
            Số tiền nhận được
          </div>
          <div style={{
            fontFamily: '"Noto Serif Display", serif',
            fontSize: 'clamp(28px, 8vw, 48px)',
            fontWeight: 900, color: '#FFD700',
            textShadow: '0 0 30px #FFD700aa',
            textAlign: 'center',
          }}>
            {formatMoney(env.amount)}
          </div>
          <div style={{ fontSize: 13, color: '#FFDDB5aa', fontStyle: 'italic' }}>
            Chúc mừng! 🎊
          </div>
        </div>
      ) : (
        <div style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6,
          opacity: state === 'closed' ? 1 : 0,
          transition: 'opacity 0.2s',
        }}>
          <div style={{
            background: 'linear-gradient(135deg, #FFD700, #FFA500)',
            color: '#7A0000', padding: '10px 28px', borderRadius: 999,
            fontSize: 16, fontWeight: 800,
            cursor: 'pointer',
            animation: 'btn-pulse 2s ease-in-out infinite',
            boxShadow: '0 4px 20px rgba(255,215,0,0.4)',
          }} onClick={onTap}>
            👆 Nhấn để mở
          </div>
          <p style={{ color: '#FFD70055', fontSize: 11, margin: 0 }}>Bên trong có gì? 🤩</p>
        </div>
      )}
    </div>
  )
}

// ─── Shared button/input styles ───────────────────────────────────────────────

const counterBtnStyle: React.CSSProperties = {
  width: 56, height: 56,
  background: 'rgba(255,215,0,0.12)',
  border: '2px solid #FFD70055',
  borderRadius: 14, color: '#FFD700',
  fontSize: 28, fontWeight: 300,
  cursor: 'pointer', display: 'flex',
  alignItems: 'center', justifyContent: 'center',
  transition: 'transform 0.1s',
  touchAction: 'manipulation',
  WebkitAppearance: 'none',
}

function primaryBtnStyle(enabled: boolean): React.CSSProperties {
  return {
    background: enabled
      ? 'linear-gradient(135deg, #FFD700 0%, #FFA500 60%, #FF8C00 100%)'
      : 'rgba(255,215,0,0.15)',
    color: enabled ? '#6B0000' : '#FFD70055',
    border: 'none', padding: '15px 40px', borderRadius: 999,
    fontSize: 18, fontWeight: 800,
    cursor: enabled ? 'pointer' : 'not-allowed',
    fontFamily: '"Be Vietnam Pro", sans-serif',
    letterSpacing: 0.5,
    transition: 'transform 0.1s, opacity 0.2s',
    boxShadow: enabled ? '0 4px 20px rgba(255,165,0,0.35)' : 'none',
    touchAction: 'manipulation',
    WebkitAppearance: 'none',
    minHeight: 52,
  }
}

// ─── ROOT APP ─────────────────────────────────────────────────────────────────

export default function App() {
  const [screen, setScreen] = useState<Screen>('welcome')
  const [envelopes, setEnvelopes] = useState<Envelope[]>([])

  const handleSetupComplete = useCallback((envs: Envelope[]) => {
    setEnvelopes(envs)
    setScreen('shuffle')
  }, [])

  const handleShuffleDone = useCallback(() => {
    setScreen('selection')
  }, [])

  const handleFinish = useCallback(() => {
    setScreen('welcome')
    setEnvelopes([])
  }, [])

  return (
    <div style={{
      position: 'fixed', inset: 0,
      fontFamily: '"Be Vietnam Pro", sans-serif',
      overflow: 'hidden',
    }}>
      {screen === 'welcome' && <WelcomeScreen onStart={() => setScreen('setup')} />}
      {screen === 'setup' && <SetupScreen onComplete={handleSetupComplete} />}
      {screen === 'shuffle' && <ShuffleScreen envelopes={envelopes} onDone={handleShuffleDone} />}
      {screen === 'selection' && <SelectionScreen envelopes={envelopes} onFinish={handleFinish} />}
    </div>
  )
}
