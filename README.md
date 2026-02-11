# Network Blocker

## Overview

Network Blocker is a utility mod that allows you to block unwanted network connection.

By default, only connections to Minecraft services are permitted, which are necessary for authentication, etc. IP connections are also allowed, but this can be changed in the configuration file. By observing the log, you can know which connections are blocked.

This mod is currently in beta. There might be connections it cannot detect and block, and it should not be considered as a comprehensive security measure against malicious mods.

## Configuration

You can find the main configuration file in `config/networkblocker/config.toml`:

```toml
#When enabled, connections that are permitted will be logged
logPermitted = false
#When enabled, connections that are blocked will be logged
logBlocked = true
#When enabled, stacktrace will be logged
logStacktrace = false
#When enabled, direct IP connections will be allowed even if they are not in the whitelist
allowIP = true
#Set to true to add some useful domains to the whitelist. The current defaults are:
#
#minecraft.net
#minecraftservices.com
#minecraft-services.net
#mojang.com
#
#You can add additional domains to the whitelist using the file in the config folder.
useDefaultWhitelist = true
#Specifies the method used to block network connection. Valid values:
#REDIRECT: redirect addresses to 0.0.0.0
#THROW: throw an exception
blockMethod = "REDIRECT"
```

You can add domains to the whitelist in `config/networkblocker/whitelist.txt`:

```
# Add additional domains to whitelist in this file, one per line
# Lines starting with # will be ignored.
#
# Example:
minecraft.net

```
