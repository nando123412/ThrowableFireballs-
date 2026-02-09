# Installation Guide

This guide will walk you through installing ThrowableFireballs on your server.

## Requirements

Before installing, ensure your server meets these requirements:

### Minecraft Version
- **Paper 1.21.x** (recommended)
- Compatible with 1.16.5 - 1.21.x

### Server Software
- ✅ **Paper** (fully supported)
- ✅ **Purpur** (fully supported)
- ⚠️ **Spigot** (partial support - action bars disabled)
- ❌ **Bukkit** (not recommended)

### Java Version
- **Java 21+** for version 1.1.0 (Minecraft 1.21.x)
- **Java 17+** for version 1.0.0 (Minecraft 1.16.5-1.20.x)

### Optional Dependencies
- **WorldGuard 7.0+** for region protection (soft dependency)

## Step-by-Step Installation

### 1. Download the Plugin

Download the appropriate version:
- **v1.1.0** - For Minecraft 1.21.x servers
- **v1.0.0** - For Minecraft 1.16.5-1.20.x servers

Get it from:
- [GitHub Releases](https://github.com/nando123412/ThrowableFireballs-/releases)
- [Modrinth](https://modrinth.com/) (if published)
- [Hangar](https://hangar.papermc.io/) (if published)

### 2. Upload to Server

1. Locate your server's `plugins/` folder
2. Upload `ThrowableFireballs-X.X.X.jar` to the `plugins/` folder
3. Do NOT rename the JAR file

### 3. Restart Server

**Important:** You must restart the server, not just reload!

```bash
# Stop the server gracefully
stop

# Or restart if you have a wrapper
restart
```

### 4. Verify Installation

After restart, check the console for:

```
[ThrowableFireballs] Using version adapter: ModernVersionAdapter (Adventure API - Paper 1.16.5+)
[ThrowableFireballs] ThrowableFireballs has been enabled!
```

If you see these messages, installation was successful!

### 5. Configure the Plugin

The plugin generates default config files:
- `plugins/ThrowableFireballs/config.yml`

Edit the config file to customize behavior. See [Configuration Guide](configuration.md).

### 6. Reload Configuration

After editing the config:

```
/tfireballs reload
```

## First-Time Setup

### Quick Test

1. Give yourself a fireball:
   ```
   /tfireballs give <your_name> 1
   ```

2. Right-click with the fire charge to throw it!

3. Adjust settings as needed in `config.yml`

### Recommended Initial Config

For PvP servers that want to protect builds:

```yaml
explosion:
  block-damage: false  # Protect terrain
  entity-damage: true  # Keep PvP damage
  power: 1.0

cooldown:
  time-millis: 1000    # 1 second cooldown
```

### Setting Permissions

Grant basic permissions to all players:

```yaml
# In your permissions plugin (LuckPerms, etc.)
permissions:
  throwablefireballs.use: true
```

Grant admin permissions to staff:

```yaml
permissions:
  throwablefireballs.admin: true
```

## Installing WorldGuard (Optional)

If you want region protection:

1. Download [WorldGuard](https://dev.bukkit.org/projects/worldguard)
2. Install WorldGuard before or after ThrowableFireballs
3. Restart server
4. ThrowableFireballs will automatically detect WorldGuard

Check console for:
```
[ThrowableFireballs] WorldGuard detected! Region protection will be respected.
```

## Upgrading from Previous Version

### From 1.0.0 to 1.1.0

1. **Backup** your `config.yml`
2. **Stop** the server
3. **Replace** the old JAR with the new JAR
4. **Update** to Java 21 (if needed)
5. **Start** the server
6. **Verify** config is preserved (automatic)

No config changes required - fully backward compatible!

## Troubleshooting Installation

### Plugin Doesn't Load

**Check:**
- Java version (use `java -version`)
- Server is Paper, not Bukkit
- JAR file is not corrupted (redownload)
- No errors in console/logs

### "Unsupported API version" Error

**Solution:** You're using the wrong plugin version
- Use v1.1.0 for MC 1.21.x
- Use v1.0.0 for MC 1.16.5-1.20.x

### WorldGuard Not Detected

**Check:**
- WorldGuard is installed and enabled
- WorldGuard version is 7.0+
- Check `protection.respect-worldguard: true` in config

### Action Bars Don't Work

**Solution:** You're on Spigot, not Paper
- Install Paper for full features
- Or accept chat message fallback on Spigot

## Next Steps

- Read [Configuration Guide](configuration.md)
- Set up [Commands & Permissions](commands-permissions.md)
- Understand [Block vs Entity Damage](block-entity-damage.md)
- Check [FAQ](faq.md) for common questions
