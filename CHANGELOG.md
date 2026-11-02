## 1.0.0

This is a major update to Network Blocker that brings numerous enhancements. It is now possible to block specific connections with the blacklist mode, filtering by either hostname or the mod that attempted the connection.

If you have made changes to the configuration, it is recommended that you either delete the old configurations, or manually add `=minecraft` to your whitelist to make sure the game can make necessary network requests.

- Added the ability to filter network requests by modid
- Added a blacklist mode
- Removed the `useDefaultWhitelist` option (the new default whitelist file already allows connections made by vanilla game)
  - This is a breaking change - if you use the whitelist mode, make sure to add `=minecraft` to the whitelist to make sure the game can still make normal network requests
- Implemented `EarlyBootstrapper` for 26.3-neoforge compatibility
- Added `bannerFile` and `iconFile` for compatibility with newer versions of NeoForge
- Fixed the default value of `blockingMethod`
- Changed the default value of `allowIP` to `false`
- Updated bundled dependencies

## 0.4.1

- Fixed JiJ discovery on Forge (lazyyyyy compatibility)

## 0.4.0

- Improved `THROW` blocking method 
- Changed default blocking method to `THROW` to avoid mutating final fields warnings on Java 26+
- [1.21.1 NeoForge] Fixed `java.nio.file.ProviderNotFoundException: Provider "jar" not found` errors caused by the mod
- Added 26.1 support

## 0.3.1

Fixed crash when Socket class is loaded early.

## 0.3.0

Fixed crash at startup under certain environments.

## 0.2.2

- Fixed crash at startup on Windows
- Fixed warnings about deprecated methods

## 0.2.1

Hotfix for log spam at launch.

## 0.2.0

- Added new THROW method for blocking connection
- Removed unused config option
- Fixed whitelisting domains with allowIP disabled

## 0.1.0

- Initial release
