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
