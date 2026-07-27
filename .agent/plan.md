# Project Plan

The "Lucky money opener" app, you can custom how many lucky money there to open, this app should have cool animation, animation everywhere, animation when add lucky money, and when open it, it should have the theme of vietnamese lunar new year "TET"

## Project Brief

# Project Brief: Lucky Money Opener

## Features
1. **Envelope Configuration**: A dedicated setup screen allowing users to specify the number of lucky money (Lì Xì) envelopes to be generated.
2. **TET Festive UI**: A visually rich main screen themed around the Vietnamese Lunar New Year, featuring traditional red and gold color palettes, apricot blossoms, and festive decorative elements.
3. **Animated Entry**: A "cool" staggered animation sequence that triggers when envelopes are added to the screen, providing a dynamic and lively feel.
4. **Interactive "Open" Animation**: A high-fidelity reveal animation that simulates the opening of a physical envelope to uncover lucky wishes or rewards when tapped.

## High-Level Technical Stack
* **Language**: Kotlin
* **UI Framework**: Jetpack Compose
* **Concurrency**: Kotlin Coroutines
* **Navigation**: Jetpack Navigation 3 (State-driven)
* **Adaptive Strategy**: Compose Material Adaptive Library (for multi-pane and responsive layouts)
* **Animation Core**: Jetpack Compose Animation API (Transitions, Vector animations, and GraphicsLayer effects)

## Implementation Steps

### Task_1_Setup_and_Config: Setup project theme with TET festive colors (Red/Gold) and implement the Configuration screen to specify the number of lucky money envelopes.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Material3 theme with red/gold palette defined
  - Navigation 3 setup for screen transitions
  - Configuration screen allows entering envelope count
  - App builds successfully
- **StartTime:** 2026-07-24 22:31:17 ICT

### Task_2_Main_Festive_Screen: Implement the Main screen featuring TET decorations and a staggered entry animation for the lucky money envelopes.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Festive UI elements (apricot blossoms, lanterns) displayed
  - Envelopes appear with a cool staggered animation on entry
  - Grid/Layout handles different envelope counts adaptively
  - App does not crash on navigation

### Task_3_Envelope_Open_Animation: Create the interactive 'Open' animation for envelopes, simulating a physical reveal of wishes or rewards.
- **Status:** PENDING
- **Acceptance Criteria:**
  - High-fidelity opening animation on envelope tap
  - Lucky wish or amount revealed after animation
  - Animation is smooth and festive
  - State management correctly handles opened/closed status

### Task_4_Run_and_Verify: Final verification of the application to ensure stability, performance, and alignment with requirements.
- **Status:** PENDING
- **Acceptance Criteria:**
  - App does not crash
  - Build pass
  - All animations (entry and open) work as expected
  - Critic agent verifies alignment with TET theme and user requirements

