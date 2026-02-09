# Quick Start Guide

Get ThrowableFireballs running on your server in 5 minutes!

## Prerequisites

✅ Paper 1.21.x server (or 1.16.5+)  
✅ Java 21 installed  
✅ Access to server files

## Step 1: Download (30 seconds)

Download the latest JAR from:
- [GitHub Releases](https://github.com/nando123412/ThrowableFireballs-/releases)

Pick the right version:
- **v1.1.0** - For Minecraft 1.21.x
- **v1.0.0** - For Minecraft 1.16.5-1.20.x

## Step 2: Install (1 minute)

1. Place `ThrowableFireballs-X.X.X.jar` in your `plugins/` folder
2. Restart your server (not reload!)
3. Check console for:
   ```
   [ThrowableFireballs] ThrowableFireballs has been enabled!
   ```

✅ If you see that message, installation succeeded!

## Step 3: Test (1 minute)

Give yourself a fireball:
```
/tfireballs give YourName 1
```

Right-click with the fire charge to throw it! 🔥

## Step 4: Configure (2 minutes)

Edit `plugins/ThrowableFireballs/config.yml`:

### For PvP Servers (protect builds):
```yaml
explosion:
  block-damage: false  # Blocks safe
  entity-damage: true  # Players take damage
```

### For Creative/Minigame Servers:
```yaml
explosion:
  block-damage: false  # No griefing
  entity-damage: false # No damage
item:
  consume-on-throw: false  # Unlimited
cooldown:
  time-millis: 250  # Fast-paced
```

Reload config:
```
/tfireballs reload
```

## Step 5: Set Permissions (1 minute)

### Everyone can throw:
```
/lp group default permission set throwablefireballs.use true
```

### VIPs get no cooldown:
```
/lp group vip permission set throwablefireballs.bypasscooldown true
```

### Mods get admin commands:
```
/lp group mod permission set throwablefireballs.admin true
```

## You're Done! 🎉

Players can now throw fireballs by right-clicking with fire charges!

## Common First-Time Issues

**"Plugin won't load"**
- Make sure you're on Paper (not Bukkit)
- Check Java version: `java -version`
- Look for errors in console

**"Players can't throw"**
- Grant `throwablefireballs.use` permission
- Check they have the right item (default: fire charge)

**"Config not working"**
- Run `/tfireballs reload` after editing
- Check YAML syntax (no tabs, proper spacing)

## Next Steps

- Read [Configuration Guide](configuration.md) for all options
- Check [Commands & Permissions](commands-permissions.md) for detailed permission setup
- Browse [FAQ](faq.md) for common questions

## Need Help?

- Check [FAQ](faq.md)
- Read [Full Documentation](README.md)
- Open an [issue on GitHub](https://github.com/nando123412/ThrowableFireballs-/issues)

---

**Time to fun with fireballs:** ~5 minutes total! ⚡
