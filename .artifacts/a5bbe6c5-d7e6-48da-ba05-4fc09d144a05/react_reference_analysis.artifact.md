# Tet Greeting App Visual Analysis (React Reference)

This document provides the specific technical details extracted from the React project for replication in the Android app.

## 1. Color Palette

### Backgrounds (Radial Gradients)
- **Welcome:** `radial-gradient(ellipse 120% 90% at 50% 0%, #9A0000 0%, #6B0000 35%, #3D0000 100%)`
- **Setup:** `radial-gradient(ellipse 130% 90% at 50% -10%, #8B0000 0%, #4D0000 50%, #2A0000 100%)`
- **Shuffle:** `radial-gradient(ellipse 130% 100% at 50% 20%, #8B0000 0%, #4D0000 50%, #1A0000 100%)`
- **Selection:** `radial-gradient(ellipse 130% 100% at 50% 10%, #8B0000 0%, #3D0000 60%, #1A0000 100%)`

### Accent Colors
- **Gold (Primary):** `#FFD700`
- **Orange (Bright):** `#FFA500`
- **Deep Orange:** `#FF8C00`
- **Envelope Red:** `#C41E3A`
- **Flap Red (Dark):** `#B01020`
- **Money Green:** `#2D8B4E`
- **Text Light (Peach):** `#FFDDB5`

### Gradients & Effects
- **Primary Button:** `linear-gradient(135deg, #FFD700 0%, #FFA500 60%, #FF8C00 100%)`
- **Title Glow:** Alternates between 12px and 28px `drop-shadow` blur using `#FFD700` and `#FF8C00`.

## 2. Typography

- **Primary Sans-serif:** `Be Vietnam Pro`
- **Display Serif:** `Noto Serif Display`

### Hierarchies
| Element | Font Family | Size | Weight | Note |
| :--- | :--- | :--- | :--- | :--- |
| **Main Title** | Noto Serif Display | 40px - 76px | 900 | Animated glow |
| **Button Text** | Be Vietnam Pro | 22px | 800 | Letter spacing 1.5px |
| **Year Pill** | Be Vietnam Pro | 13px | 800 | Letter spacing 3px |
| **Reveal Amount** | Noto Serif Display | 28px - 48px | 900 | Gold text |
| **Lì Xì Label** | Noto Serif Display | 18px | 400 | On envelope |

## 3. Component Layout

- **Main Container:** Max width 480px, horizontal padding 32px.
- **Envelope Sizes (Width):**
    - Welcome: 160px
    - Setup Reveal: 170px
    - Selection Card: 240px
    - Shuffle Pile: 100px
- **Buttons:**
    - Padding: 18px 56px (Welcome), 15px 40px (Setup).
    - Corner Radius: 999px (Capsule).
    - Min Height: 52px.
- **Input Fields:**
    - Height: 58px.
    - Corner Radius: 14px.
    - Border: 2px solid `#FFD70055`.

## 4. Animation Specifics

| Name | Duration | Easing | Key Property |
| :--- | :--- | :--- | :--- |
| **slide-up** | 0.9s | ease-out | translateY(40px) -> 0 |
| **float** | 3.0s | ease-in-out | translateY(0) -> -12px (Loop) |
| **envelope-arrive** | 0.65s | cubic-bezier(0.22, 1, 0.36, 1) | translateX(120%) -> 0 |
| **flap-open** | 0.9s | cubic-bezier(0.4, 0, 0.2, 1) | rotateX(0deg) -> -185deg |
| **shuffle-move** | 0.55s | ease-in-out | Random offsets (Loop) |
| **deal-out** | 0.6s | cubic-bezier(0.22, 1, 0.36, 1) | fly to grid coords |
| **amount-pop** | 0.7s | cubic-bezier(0.22, 1, 0.36, 1) | scale(0) -> 1 |

## 5. Asset References

- **SVGs:** All graphics are SVG-based. Key shapes to replicate in XML Drawables or Compose:
    - `EnvelopeSVG`: Rect with 14px radius, Chevron top, center Diamond.
    - `Lantern`: Ellipse with 2px gold border, 福 character center.
    - `MoneyBill`: Rect with 6px radius, "NGÂN HÀNG NHÀ NƯỚC VIỆT NAM" text.
- **Iconography:** Strictly uses Material-style rounded shapes or emojis (🧧).
