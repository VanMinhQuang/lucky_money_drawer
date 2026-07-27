# Implementation Plan - Match React Reference Exactly

Update the Android app to match the React reference (Tet Greeting App) visual style, including exact colors, typography, layout, and animations.

## Proposed Changes

### [Theme & Resources]

#### [MODIFY] [libs.versions.toml](file:///D:/projects/New%20folder/LuckyMoney/gradle/libs.versions.toml)
- Add `androidx-compose-ui-text-google-fonts` dependency.

#### [MODIFY] [build.gradle.kts](file:///D:/projects/New%20folder/LuckyMoney/app/build.gradle.kts)
- Add `androidx-compose-ui-text-google-fonts` to implementation.

#### [MODIFY] [Color.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/theme/Color.kt)
- Add exact HEX codes from React reference analysis.
- Define gradients as reusable Brush constants.

#### [MODIFY] [Type.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/theme/Type.kt)
- Implement `Be Vietnam Pro` and `Noto Serif Display` using Google Fonts.
- Set up typography hierarchies matching the React reference.

#### [MODIFY] [Theme.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/theme/Theme.kt)
- Update `LuckyMoneyTheme` to use the new color palette and typography.
- Disable dynamic color by default to ensure brand consistency as per the React reference.

---

### [Components]

#### [NEW] [Common.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/Common.kt)
- Create `RadialGradientBackground` component.
- Create `CapsuleButton` component with linear gradient and specific padding.
- Define `AppCubicBezier` easing constant.

#### [NEW] [Envelope.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/Envelope.kt)
- Implement the `Envelope` component matching the SVG reference (Chevron top, center diamond).
- Add flap opening animation logic.

#### [NEW] [MoneyBill.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/MoneyBill.kt)
- Implement the Vietnamese Money Bill component.

---

### [Screens]

#### [MODIFY] [WelcomeScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/WelcomeScreen.kt)
- Update layout to match React version.
- Use `Noto Serif Display` for the title with glow effect.
- Apply `Welcome` radial gradient.

#### [MODIFY] [SetupCountScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/SetupCountScreen.kt)
- Update input field styles and layout.
- Apply `Setup` radial gradient.

#### [MODIFY] [SetupAmountScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/SetupAmountScreen.kt)
- Match React's "Setup Reveal" style.

#### [MODIFY] [ShuffleScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/ShuffleScreen.kt)
- Implement "shuffle-move" animation with random offsets.
- Apply `Shuffle` radial gradient.

#### [MODIFY] [SelectionScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/SelectionScreen.kt)
- Implement "deal-out" grid animation.
- Match envelope and money reveal animations exactly.
- Apply `Selection` radial gradient.

## Verification Plan

### Automated Tests
- Build the project to ensure all dependencies and changes are correct.
- Run the app on an emulator to verify visual changes.

### Manual Verification
- Verify the colors match the HEX codes.
- Verify typography uses the correct fonts.
- Verify animations use the specified cubic-bezier and feel "smooth/overshooting".
- Check that all screens have the correct radial gradient background.
