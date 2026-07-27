# Update Visual Effects and Layering for Lucky Money App

This plan addresses three visual improvements: fixing the envelope layering, updating the money icon's glow to a starburst effect, and standardizing the white starburst for the reveal moment.

## Proposed Changes

### [UI Components]

#### [MODIFY] [InteractiveEnvelope.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/InteractiveEnvelope.kt)
- Split the front cover and flap rendering into separate stages.
- Reorder layering so the money bill is drawn on top of the flap when it is open (rotated > 90 degrees), but remains behind the front cover.

#### [MODIFY] [MoneyComponents.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/MoneyComponents.kt)
- Update `ShiningMoneyIcon` to replace the simple pulsating rounded rectangle glow with a rotating white starburst (shining) effect.

#### [MODIFY] [FunkyText.kt](file:///D:/projects/New%20folder/LuckyMoney/app/src/main/java/quang/app/luckymoney/ui/components/FunkyText.kt)
- Update `ShineBurst` to use pure white gradients instead of cyan/blue colors, creating a cleaner "radiating light" effect.

## Verification Plan

### Manual Verification
- Run the app and open an envelope.
- Verify that as the money is pulled up, it overlaps the open flap.
- Check the "Shining" icon inside the unopened envelope for the new starburst effect.
- Trigger the reveal overlay and verify the white starburst background.
