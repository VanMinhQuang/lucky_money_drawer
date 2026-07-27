# Refinement Walkthrough - React Reference Matching

The Android app has been updated to match the React reference (Tet Greeting App) exactly, following the provided visual analysis.

## Key Changes

### 1. Visual Design & Color Palette
- Implemented the exact radial gradients for each screen background:
    - **Welcome:** Dark Red to Deep Red.
    - **Setup:** Dark Red to Shadow Red.
    - **Shuffle & Selection:** Specific dark red/black gradients.
- Updated accent colors to match the HEX codes: `#9A0000`, `#C41E3A`, `#FFD700`, `#FFA500`.
- Added a `PrimaryButtonGradient` used in all primary buttons.

### 2. Typography
- Integrated **Google Fonts**:
    - `Noto Serif Display`: Used for main titles and money reveal.
    - `Be Vietnam Pro`: Used for body text and buttons.
- Updated `Type.kt` with matching hierarchies (e.g., Year Pill, Animated Title).

### 3. Components
- **CapsuleButton**: A reusable button with the exact linear gradient, 58px height, and 999dp corner radius.
- **Envelope**: A custom component matching the SVG reference (Chevron top, center diamond, flap opening animation).
- **MoneyBill**: A Vietnamese Money Bill component with the specified colors and "NGÂN HÀNG NHÀ NƯỚC VIỆT NAM" text.
- **Lantern**: Added decorative animated lanterns to the Welcome screen.

### 4. Animations
- Defined `AppEasing` using `CubicBezierEasing(0.22f, 1f, 0.36f, 1f)`.
- **Welcome Screen**: Added an animated title with alternating drop-shadow glow.
- **Shuffle Screen**: Updated to use the "shuffle-move" animation with random offsets and 550ms duration.
- **Selection Screen**: Improved the 3D envelope rotation and reveal animation with a smooth overshoot feel.
- **Setup Screen**: Added a money bill "insertion" animation when adding money to envelopes.

### 5. Cleanup
- Removed unused screens (`MainScreen.kt`, `ConfigurationScreen.kt`) that were causing build errors due to outdated color references.

## Verification
- **Build**: Successfully built `:app:assembleDebug`.
- **Layout**: Verified capsule buttons and radial gradients across all screens.
- **Typography**: Verified font loading via Google Fonts.

The app now has a premium, expressive feel that matches the high-quality React reference implementation.
