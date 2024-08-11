## [1.10.1] - 2024-08-11

Available versions:
-   Fabric: 1.21
### Changed/Fixed

-   Fixed sodium compatibility.
-   Do not load compat mixins when mod is not present.

## [1.10] - 2024-08-10

Available versions:
-   Fabric: 1.21
### Added

-   Added Big Baby villager heads (#354).
    
### Changed/Fixed

-   Added compatibility with "Detail Armor Bar" mod for Screen safe area and Hud opacity (Fix #279).
-   Fixed compatibility with AppleSkin mod for Screen safe area and Hud opacity (Fix #278).
-   Added option to hide hotbar selector overhang (black line below the selector that is visible with custom resource packs) (Fix #361).
-   Improved Sun Glare fidelity to better match bedrock edition (Fix #359):
    - Sun gets brighter.
    - Sky darkens with a bluer tint.
    - Clouds darkens a bit.
-   Apply sheep color to sheep heads before shearing (Fix #264).
-   Some features have been rewritten to use less invasive mixins, hopefully improving inter-mod compatibility.

## [1.9.7] - 2024-08-04

Available versions:
-   Fabric: 1.21

### Changed

-   Add container Tooltips to any item with Container or bundle components (Fix #326 Fix #356)
-   Added Traditional Chinese translation (yichifauzi)

## [1.9.6] - 2024-06-25

Available versions:
-   Fabric: 1.21

### Changed

-   Updated to 1.21! 

## [1.9.5] - 2024-05-16

Available versions:
-   Fabric: 1.20.6+

### Changed

-   Remove Cauldrons when mixin is disabled to allow compatibility with local multiplayer where conencting clients dont use bedrockify (by lonefelidae16)
-   Third person eating animation is now more accurate to bedrock edition (by MidyGamy)

### Fixed

-   Fixed sun glare compatibility with Iris while shaders are disabled. (By lonefelidae16)

## [1.9.4] - 2024-05-08

Available versions:
-   Fabric: 1.20.6+

### Changed

-   Updated loading tooltips for most languages.

### Fixed

-   Fixed #342 - Idle hand animation speed no longer bound to framerate

## [1.9.3] - 2024-05-08

Available versions:
-   Fabric: 1.20.6+

### Fixed

-   Fixed #339 - Armor bars not rendering correctly with screen safe area enabled

## [1.9.2] - 2024-05-07

Available versions:
-   Fabric: 1.20.6+

### Changed

-   1.20.6 Port by @lonefelidae16.
-   Reach around block placement has been reworked and now uses ray-casting in order to be more accurate and close to Bedrock Edition implementation. (@axialeaa)
-   Removed fastload compatibility since the mod is no longer maintained to current versions.
-   Panorama-Screens is no longer bundled with bedrockIfy since its main functionality has been implemented in game by mojang.

### Fixed

-   Fixed IdleHandAnimations animation being accelerated when holding items in both hands. (@lonefelidae16)
-   Fixed small bedrock chat mouse hover offset.
