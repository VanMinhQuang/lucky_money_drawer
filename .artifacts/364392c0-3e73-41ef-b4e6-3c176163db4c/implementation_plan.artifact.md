# Reveal Interaction and Blue Starburst Implementation

Fix the reveal interaction snap-back bug and implement the Blue Starburst effect with proper layering and persistent state.

## Proposed Changes

### [MainViewModel](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/viewmodel/MainViewModel.kt)

#### [MODIFY] [MainViewModel.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/viewmodel/MainViewModel.kt)
- Add `fullyRevealedIndices: Set<Int>` to `AppState`.
- Add `revealEnvelope(index: Int)` to mark an envelope as fully revealed.
- Update `shuffleEnvelopes` and `resetSetup` to clear `fullyRevealedIndices`.

### [Components]

#### [MODIFY] [InteractiveEnvelope.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/InteractiveEnvelope.kt)
- Update `onDragEnd` to prevent snap-back when past the 50% threshold.
- Ensure the money stays at `maxOffset` if `isOpened` is true.

#### [MODIFY] [FunkyText.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/FunkyText.kt)
- Redesign `ShineBurst` to match the Blue Starburst visual:
    - Bright white center.
    - Cyan/Blue radiating rays (triangular).
    - Slow rotation animation.

#### [MODIFY] [MoneyRevealOverlay.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/MoneyRevealOverlay.kt)
- Reorder components for correct layering: Starburst -> Funky Amount Text -> Money Icon -> Money Image Stack.
- Adjust `Box` and `Column` to allow overlapping as specified in the "stack" requirement.

### [Screens]

#### [MODIFY] [SelectionScreen.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/screens/SelectionScreen.kt)
- Sync `revealedPage` with the pager's current page if it is in `fullyRevealedIndices`.
- Update `onOpen` to call `viewModel.revealEnvelope(page)`.

## Verification Plan

### Automated Tests
- Build the project using `./gradlew :app:assembleDebug` to ensure no compilation errors.

### Manual Verification
1. Open an envelope by pulling it up past 50%.
2. Verify it doesn't snap back to hidden.
3. Verify the Blue Starburst overlay appears with the correct layering.
4. Verify the overlay remains visible for that envelope when swiping away and back.
5. Verify the money breakdown matches the envelope's amount.
