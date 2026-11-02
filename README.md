# Network Blocker

## Overview

Network Blocker is a utility mod that allows you to block unwanted network connection.

By default, this mod permits connections made by vanilla Minecraft, which are necessary for authentication, etc. Network connections made by mods are blocked by default.

While the default is to block all non-vanilla connections, there is also a blacklist mode. This is useful when you want to block only specific connections, filtering by either domain name or the mod that attempted the connection.

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
#When enabled, treat the whitelist as blacklist. This means all connections are allowed by default, and only connections to the specified domains or attempted by specified mods will be blocked.
#This is useful when you want to only block specific connections that are causing issues.
blacklistMode = false
#Specifies the method used to block network connection. Valid values:
#DEFAULT: use the default blocking method (THROW)
#REDIRECT: redirect addresses to 0.0.0.0
#THROW: throw an exception
blockingMethod = "DEFAULT"
```

You can add domains or mods to the whitelist in `config/networkblocker/whitelist.txt`:

```
# Add additional domains/mods to the whitelist in this file, one per line
# Lines starting with # are ignored. (use them as comments!)
# Lines starting with = are treated as modid's.
# Other lines are treated as domain names.
# The following default allows connection by any mod to *.minecraft.net, as well as any connection made by vanilla game.

minecraft.net
=minecraft
```

When `logPermitted` or `logBlocked` is enabled, detailed information is logged, which helps monitor network connection and tells you the mod that attempted each connection:

```
[Network Blocker/]: Blocked connection to example.com:443, attempted by mod some_awesome_mod
[Network Blocker/]: Permitted connection to api.minecraftservices.com:443, attempted by mod minecraft
```

## Side Requirement

This mod does not have strict side requirements. This means it is possible to install the mod on the client-side without it being present on the dedicated server, and vice versa. Network blocking will work on the side(s) with the mod installed.
