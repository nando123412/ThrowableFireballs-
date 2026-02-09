# Per-Player Settings

Complete guide to player-specific customization options in ThrowableFireballs.

---

## Overview

ThrowableFireballs allows individual players to customize their fireball behavior, giving them control over their gameplay experience while maintaining server-wide defaults.

## Block Damage Toggle

### What It Does

Players can choose whether **their own fireballs** damage blocks, independent of other players' settings.

### Enabling This Feature

In `config.yml`:

```yaml
explosion:
  enable-per-player-choice: true
  default-player-block-damage-enabled: false
```

- `enable-per-player-choice: true` - Allows players to toggle their preference
- `default-player-block-damage-enabled: false` - New players start with block damage OFF

### Player Command

```
/tfireballs blockdamage
```

**What happens:**
- First use: Toggles block damage ON/OFF
- Shows current status: "Block damage is now enabled/disabled for your fireballs"
- Saved permanently (survives restarts)

### Permission Required

```
throwablefireballs.toggleblockdamage
```

Grant this permission to players you want to allow customization.

### Example Permission Setup

**Using LuckPerms:**
```
/lp group default permission set throwablefireballs.toggleblockdamage true
```

**Using permissions.yml:**
```yaml
default:
  permissions:
    throwablefireballs.toggleblockdamage: true
```

## How It Works

### Data Storage

Player preferences are stored using Bukkit's **PersistentDataContainer** on the player object:

- **Where:** Stored in player data files (per-player)
- **Persistence:** Survives server restarts
- **Format:** Boolean flag (true/false)

### Workflow

1. **Player throws fireball** → Plugin tags it with player's UUID
2. **Fireball explodes** → Plugin checks:
   - Is per-player choice enabled?
   - What is this player's preference?
3. **Apply preference** → Modify explosion accordingly

### Priority System

When determining if block damage should occur:

1. **Protected regions** (highest priority) - Always block damage in protected areas
2. **Global setting** - If `enable-block-damage: false`, blocks never damaged
3. **Per-player preference** - If enabled and player toggled OFF, their fireballs don't damage blocks
4. **Default behavior** - Use `default-player-block-damage-enabled` for new players

## Configuration Reference

### Complete Settings

```yaml
explosion:
  # Global block damage control
  enable-block-damage: false
  
  # Per-player system
  enable-per-player-choice: true
  default-player-block-damage-enabled: false
  
  # Entity damage (not affected by per-player settings)
  enable-entity-damage: true
```

### Setting Combinations

| Global Block Damage | Per-Player Choice | Player Preference | Result |
|---------------------|-------------------|-------------------|---------|
| `false` | `true` | ON | ❌ No block damage (global overrides) |
| `false` | `true` | OFF | ❌ No block damage |
| `true` | `true` | ON | ✅ Block damage enabled |
| `true` | `true` | OFF | ❌ No block damage (player choice) |
| `true` | `false` | (ignored) | ✅ Block damage enabled (global only) |
| `false` | `false` | (ignored) | ❌ No block damage (global only) |

**Key takeaway:** Global `enable-block-damage: false` overrides all player preferences.

## Commands & Feedback

### Toggle Command

```
/tfireballs blockdamage
```

**Output messages:**
- When enabled: "Block damage is now enabled for your fireballs"
- When disabled: "Block damage is now disabled for your fireballs"

### Admin Checking

Admins cannot currently check other players' preferences via command. To manually check, you'd need to:

1. Have player run `/tfireballs blockdamage` twice (toggles back)
2. Check player data files directly (advanced)

## Use Cases

### Use Case 1: Flexible PvP Server

**Setup:**
```yaml
enable-block-damage: true
enable-per-player-choice: true
default-player-block-damage-enabled: true
```

**Result:**
- Most players have block damage enabled (default)
- Players who want "clean" combat can disable it
- PvP arenas stay intact for careful players

### Use Case 2: Protected-by-Default

**Setup:**
```yaml
enable-block-damage: true
enable-per-player-choice: true
default-player-block-damage-enabled: false
```

**Result:**
- New players can't damage blocks (safe default)
- Trusted players can enable block damage
- Prevents accidental grief

### Use Case 3: No Player Control

**Setup:**
```yaml
enable-block-damage: false
enable-per-player-choice: false
```

**Result:**
- Server decides: No block damage, period
- Players cannot change this
- Consistent experience for all

## Permissions Matrix

Complete permission setup for per-player features:

| Permission | Purpose | Default | Recommended For |
|------------|---------|---------|-----------------|
| `throwablefireballs.use` | Throw fireballs | `false` | All players |
| `throwablefireballs.toggleblockdamage` | Toggle own block damage | `false` | Trusted players |
| `throwablefireballs.give` | Use give command | `false` | Moderators |
| `throwablefireballs.reload` | Reload plugin | `false` | Admins |
| `throwablefireballs.bypass` | Bypass protection | `false` | Admins only |

**Minimal setup for players:**
```
- throwablefireballs.use
- throwablefireballs.toggleblockdamage
```

## Troubleshooting

### "Player can't toggle block damage"

**Check:**
1. `enable-per-player-choice: true` in config
2. Player has permission: `throwablefireballs.toggleblockdamage`
3. `/tfireballs reload` was run after config change

### "Player's setting isn't working"

**Check:**
1. Global `enable-block-damage` isn't set to `false` (this overrides player choice)
2. Region isn't protected (protection always blocks damage)
3. Player actually threw the fireball (pickup/throw mechanism)

### "Setting resets after restart"

**Possible causes:**
- Player data corruption (rare)
- Permission plugin resetting data
- Multiple servers with shared worlds but separate plugin data

**Solution:** Settings are stored in player data; ensure player data directory is persistent.

### "All players have same setting"

**Check:**
- Settings are stored per-player, not globally
- Each player must run `/tfireballs blockdamage` themselves
- Admins can't set preferences for others (by design)

## API for Developers

If you're developing addons, you can check/modify player preferences:

### Check Player Preference

```java
ThrowableFireballsPlugin plugin = ...; // Get plugin instance
boolean playerHasBlockDamage = plugin.shouldEnableBlockDamage(player);
```

### Modify Player Preference

```java
// Set player preference
NamespacedKey key = new NamespacedKey(plugin, "block-damage-enabled");
player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1); // Enable
player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 0); // Disable
```

See [API Documentation](api-documentation.md) for more developer info.

## Future Enhancements

Potential features being considered:

- **Admin command** to check/set other players' preferences
- **GUI interface** for settings instead of just commands
- **Per-player cooldowns** - different cooldown times per player
- **Per-player fireball speed** - customize projectile velocity
- **Preset profiles** - "Destroyer", "Protector", "Balanced" presets

These are not guaranteed but are under consideration.

## FAQ

**Q: Can players have different cooldowns?**  
A: Not currently. Cooldowns are global. This may be added in the future.

**Q: Does this work with entity damage too?**  
A: No, only block damage is customizable per-player. Entity damage is global.

**Q: What happens if a player quits and rejoins?**  
A: Their preference is saved and restored automatically.

**Q: Can I set different defaults per permission group?**  
A: Not directly. All new players use `default-player-block-damage-enabled`. However, you could use permission-based commands on first join.

**Q: Do preferences sync across multiple servers?**  
A: Only if you use a shared player data system (like MySQL player data storage). By default, preferences are per-server.

## See Also

- [Block vs Entity Damage](block-vs-entity-damage.md) - Understanding damage systems
- [Commands & Permissions](commands-permissions.md) - All commands and permissions
- [Configuration Guide](configuration.md) - Full config.yml reference
- [API Documentation](api-documentation.md) - Developer integration
