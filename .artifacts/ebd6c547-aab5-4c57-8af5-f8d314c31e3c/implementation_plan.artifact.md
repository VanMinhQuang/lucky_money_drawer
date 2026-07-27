# Implementation Plan - TET Festive Theme and Configuration Screen

This plan covers setting up a TET festive theme (Red/Gold) using Material 3 and implementing a Configuration screen using Navigation 3.

## Proposed Changes

### [Theme]
Update the theme to use TET festive colors (Red and Gold).

#### [MODIFY] [Color.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/theme/Color.kt)
- Define `TetRed` and `TetGold` color tokens.

#### [MODIFY] [Theme.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/theme/Theme.kt)
- Update `LightColorScheme` and `DarkColorScheme` to use `TetRed` as primary and `TetGold` as secondary.
- Ensure `dynamicColor` is handled correctly (as fallback or secondary priority if TET theme is forced).

### [Navigation]
Setup Navigation 3 for the app.

#### [NEW] [NavRoute.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/navigation/NavRoute.kt)
- Define routes for the app (e.g., `Configuration`).

### [Features]
Implement the Configuration screen.

#### [NEW] [ConfigurationScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/ConfigurationScreen.kt)
- Create a screen to input the number of lucky money envelopes.
- Use Material 3 components (TextField, Button).

### [Main Activity]
Wire everything together.

#### [MODIFY] [MainActivity.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/MainActivity.kt)
- Implement `NavDisplay` with the `Configuration` screen as the start destination.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure the app builds.

### Manual Verification
- Deploy to a device/emulator.
- Verify the Red/Gold theme is applied.
- Verify the Configuration screen is shown and accepts input.
