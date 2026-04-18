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
