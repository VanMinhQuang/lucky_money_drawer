# Project Plan

A festive Vietnamese Tet themed Lucky Money (Lì Xì) app. Requirements: Welcome -> Setup -> Shuffle -> Selection -> Pull-to-reveal. High-quality animations, sounds, and haptics.

## Project Brief

# Project Brief: Lucky Money (Lì Xì) - Ultra-Festive Edition

## Features
1. **Interactive Setup & Shuffle**: Users create and configure lucky money envelopes, which are then randomized with smooth shuffle animations to hide the values.
2. **Gesture-Based Pull-to-Reveal**: A tactile pull-up interaction to extract money from the envelope, featuring physics-based resistance and a celebratory reveal animation.
3. **Ultra-Festive Ambient Effects**: A physics-based particle system simulating falling "Hoa Mai" (Yellow Apricot Blossom) petals that drift dynamically across the screen.
4. **Immersive Sensory Feedback**: A synchronized audio-haptic experience featuring festive BGM, frictional dragging SFX, and vibration intensity that scales with the pulling gesture.
5. **Adaptive Navigation Flow**: A seamless, state-driven transition from Welcome to Reveal screens, optimized for all device form factors.

## High-Level Technical Stack
* **Language**: Kotlin
* **UI Framework**: Jetpack Compose
* **Navigation**: Jetpack Navigation 3 (State-driven)
* **Layouts**: Compose Material Adaptive Library
* **Concurrency**: Kotlin Coroutines (for physics-based animations and UI state management)
* **Audio**: Jetpack Media3 (Low-latency sound effects and background festive music)
* **Haptics**: Android HapticFeedback API (Dynamic vibration intensity control)

## Implementation Steps
**Total Duration:** 2h 37m 45s

### Task_1_Setup_and_Config: Setup project theme with TET festive colors (Red/Gold) and implement the Configuration screen to specify the number of lucky money envelopes.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Material3 theme with red/gold palette defined
  - Navigation 3 setup for screen transitions
  - Configuration screen allows entering envelope count
  - App builds successfully

### Task_2_Main_Festive_Screen: Implement the festive main screen with staggered entry animations for envelopes.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Main screen themed with TET elements
  - Envelopes displayed as a list or grid
  - Staggered animation when entering the screen
- **Duration:** 2m 15s

### Task_3_Envelope_Open_Animation: Build high-fidelity interactive 'open' animations and reveal lucky wishes.
- **Status:** COMPLETED
- **Duration:** 2m 11s

### Task_4_Run_and_Verify: Final stability check, build verification, and alignment with requirements.
- **Status:** COMPLETED
- **Duration:** 6m 45s

### Task_5_Welcome_and_Setup_Refinement: Implement Welcome screen with festive animations and the two-phase Setup flow (count selection then sequential amount entry with 'money-in' animation).
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Welcome screen features falling petals and lanterns
  - Setup Flow includes Phase 1 (Count) and Phase 2 (Sequential amounts)
  - Money-into-envelope animation works for each amount entry
  - The implemented UI must match the design provided in https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_0.png, https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_1.png, and https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_2.png
- **Duration:** 8m 9s

### Task_6_Shuffle_Selection_and_Final_Verify: Implement Shuffle animation, Horizontal selection view, and 3D reveal with real money drawables. Instruct critic_agent to perform a final Run and Verify for stability, requirements alignment, and UI issues.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Dramatic shuffle animation where envelopes mix
  - Horizontal scrollable selection screen with snap-to-center
  - High-fidelity 3D opening animation revealing realistic Vietnamese currency
  - Make sure all existing tests pass, build pass and app does not crash
  - The implemented UI must match the design provided in https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_5.png, https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_6.png, and https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_7.png
- **Duration:** 31m 41s

### Task_7_Pull_To_Reveal: Implement high-fidelity manual pull-to-reveal gesture for the envelope with spring physics, layering (back/money/flap), and Lottie confetti celebration.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Vertical drag gesture pulls money out of envelope
  - Spring-based elastic feedback implemented
  - Layering correctly hides/shows money behind the flap
  - Lottie confetti and haptic feedback triggered on full extraction
  - The implemented UI must match the design provided in https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_5.png, https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_6.png, and https://api.deepmind.com/unsupported/v1/projects/luckymoney-c35eed/files/input_file_7.png
- **Duration:** 6m 15s

### Task_8_Triangular_Flap_Interaction: Update envelope design to a custom-drawn red Lunar New Year envelope with a gold '福' character and a triangular top flap. Implement a two-step interaction: tap to open the flap, then pull (drag) to extract money.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Envelope features a precise triangular flap drawn via Canvas/Vector
  - Gold '福' (Phúc) character is visible on the envelope front
  - Single tap on the envelope triggers a smooth flap-opening animation
  - Money pull (drag) gesture is only enabled after the flap is open
  - The implemented UI must match the design provided in the project description
  - build pass and app does not crash
- **Duration:** 4m 47s

### Task_10_Reveal_UI_Refinement: Implement the dynamic money breakdown logic and the high-fidelity reveal UI stack including shining text and currency list.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Breakdown logic correctly splits total amount into Vietnamese currency notes
  - Shining Amount Text features a glowing/pulsating animation
  - Reveal UI displays the Money Icon, Shining Text, and Currency Breakdown list stacked correctly
  - build pass and app does not crash
- **Duration:** 3m 25s

### Task_11_Run_and_Verify: Final stability check, build verification, and alignment with requirements for the complete flow.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Verify application stability (no crashes)
  - Confirm alignment with user requirements: tap flap, pull to reveal, breakdown display
  - make sure all existing tests pass
  - build pass
  - app does not crash

### Task_12_Funky_Reveal_UI_and_Physics: Implement the 'Funky' reveal UI (cyan-gradient/purple-outline text, tilted comic style), radiating shine background, tight centered layout, and physics-based bounce for money settle.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Amount text features cyan gradient fill and thick dark purple outline
  - Text is tilted and cartoonish as per 'FUNKY' style
  - Radiating glow animation appears behind the revealed content
  - Layout is tightly stacked (text, icon, fanned money) in screen center
  - Revealed money bounces and settles smoothly using physics-based animation
  - The implemented UI must match the design provided in the project description
  - build pass
- **Duration:** 3m 17s

### Task_13_Final_Run_and_Verify: Final stability check and requirement alignment for the 'Funky' reveal features.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Verify application stability (no crashes)
  - Confirm all 'Funky' UI and animation requirements are met
  - make sure all existing tests pass
  - build pass
  - app does not crash
- **Duration:** 12m

### Task_14_Reveal_Interaction_and_Starburst: Fix the gesture snap-back bug (persistent reveal past 50%) and implement the blue starburst background effect.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Money stays revealed if pulled past 50% threshold
  - Blue starburst background (white core, cyan/blue rays) implemented
  - The implemented UI must match the design provided in the project description
- **Duration:** 4m 43s

### Task_15_Stacked_UI_and_Final_Verify: Ensure the reveal UI (text, icon, bills) is correctly stacked on top of the envelope and perform final verification for stability and requirements.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Reveal content correctly overlays the envelope
  - build pass and app does not crash
  - make sure all existing tests pass
  - Verify application stability and alignment with user requirements
- **Duration:** 9m 57s

### Task_16_Refine_Pull_to_Reveal_Visuals: Implement the pulsating 'Shining Money Icon' placeholder during the pull gesture and update the reveal logic to expose content only after passing the threshold. Enhance the blue starburst with animated sparkles.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - 'Shining Money Icon' with white glowing border and pulsating light effect used as placeholder during drag.
  - Actual reward (Funky text/bills) revealed only after passing the pull threshold.
  - Blue starburst background contains animated sparkles and stars.
  - The implemented UI must match the design provided in the project description.
- **Duration:** 5m 30s

### Task_17_Final_Run_and_Verify: Perform a final stability check and verify that the pull-to-reveal interaction and starburst sparkles align with user requirements.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Verify application stability (no crashes).
  - Confirm placeholder pulsates during drag and reveal triggers correctly.
  - Starburst sparkles are animated and visible.
  - Make sure all existing tests pass, build pass and app does not crash.
- **Duration:** 15m 10s

### Task_18_Layering_Refinement_and_White_Starburst: Refine the 'InteractiveEnvelope' layering so money overlays the flap once open. Add a radiant white starburst effect (matching the new image) to the reveal.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Money stack overlays the envelope flap once the flap is open (180-degree rotation).
  - Radiant white starburst effect with rotating rays implemented for the reveal.
  - The implemented UI must match the design provided in the project description.
  - Build pass and app does not crash.
- **Duration:** 5m 4s

### Task_19_Final_Run_and_Verify: Perform a final stability check and verify that the layering, starburst effect, and reveal animations align with the updated requirements.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Verify application stability (no crashes).
  - Confirm depth-correct content layering (money overlays the flap).
  - Confirm white starburst effect is active and radiant.
  - Make sure all existing tests pass, build pass and app does not crash.

### Task_20_Ambient_Effects_and_Haptics: Implement physics-based falling 'Hoa Mai' (Yellow Apricot Blossom) particle system and dynamic vibration intensity that scales with the pull-to-reveal gesture.
- **Status:** COMPLETED
- **Updates:** Implemented AudioManager for BGM and SFX. Added FallingApricotBlossoms (Hoa Mai) background effect. Integrated dynamic haptics in the pull-to-reveal gesture. Added shimmering animation to money bills during extraction. Updated SelectionScreen with page-swipe sound effects. Build is stable.
- **Acceptance Criteria:**
  - Yellow apricot blossoms drift dynamically across all screens.
  - Haptic vibration intensity scales linearly with the pull-to-reveal drag distance.
  - Festive BGM and dragging SFX integrated via Media3.
  - The implemented UI must match the design provided in the project description.
  - Build pass and app does not crash.
- **Duration:** 23m 5s

### Task_21_Final_Run_and_Verify: Final stability check, build verification, and alignment with the 'Ultra-Festive' requirements.
- **Status:** COMPLETED
- **Updates:** Final quality check passed for visuals, haptics, and stability. However, audio currently uses placeholders because raw assets were not provided. The app is ready for production pending the addition of actual .mp3 files by the user.
- **Acceptance Criteria:**
  - Verify application stability (no crashes).
  - Confirm alignment with all user requirements including blossoms and sensory feedback.
  - Make sure all existing tests pass.
  - Build pass and app does not crash.
- **Duration:** 13m 31s

