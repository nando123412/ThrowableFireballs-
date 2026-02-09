# ThrowableFireballs

A highly configurable Minecraft Paper plugin that allows players to throw fireballs as projectiles. Built with multi-version support (1.16.5 through latest Paper) and clean architecture to avoid NMS dependencies.

## Features

✨ **Throwable Fireballs** - Right-click with a fire charge (or custom item) to launch a fireball projectile  
🎯 **Configurable Damage** - Separate control for block damage and entity damage  
🛡️ **Protection Integration** - WorldGuard soft dependency with fallback protection  
⏱️ **Anti-Spam Cooldowns** - Per-player cooldowns with action bar messages  
🎨 **Fully Customizable** - Everything from item type to explosion power is configurable  
👤 **Per-Player Settings** - Players can toggle block damage for their own fireballs  
🔧 **Multi-Version Support** - Works on Paper 1.16.5+ without NMS  
📦 **Easy to Use** - Simple commands and intuitive permissions system

## Key Feature: Block Damage vs Entity Damage

**IMPORTANT:** This plugin allows you to **disable block damage while keeping entity/player damage enabled**. This means:

- ✅ Fireballs can damage players and mobs
- ❌ Fireballs won't break blocks or terrain
- ✅ Explosion effects, knockback, and fire still work
- 🎯 Perfect for PvP servers that want to protect builds

Configure this in `config.yml`:
```yaml
explosion:
  block-damage: false  # Blocks are safe
  entity-damage: true  # Players still take damage
```

Players can also toggle block damage for their own fireballs:
```
/tfireballs blockdamage <on|off>
```

## Installation

1. **Download** the latest release JAR from the releases page
2. **Place** the JAR file in your server's `plugins/` folder
3. **Restart** your server (or use a plugin manager)
4. **Configure** the plugin in `plugins/ThrowableFireballs/config.yml`
5. **(Optional)** Install [WorldGuard](https://dev.bukkit.org/projects/worldguard) for advanced region protection

## Requirements

- **Minecraft Server**: Paper 1.21.x (also compatible with 1.16.5 - 1.21.x)
- **Java**: 21 or newer (Java 17 minimum)
- **Optional**: WorldGuard 7.0+ for region protection

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/tfireballs reload` | Reload configuration | `throwablefireballs.admin` |
| `/tfireballs give <player> [amount]` | Give fireball items | `throwablefireballs.admin` |
| `/tfireballs blockdamage <on\|off>` | Toggle block damage for your fireballs | `throwablefireballs.blockdamage.toggle` |

**Aliases:** `/tfb`, `/throwablefireball`

## Permissions

### Main Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `throwablefireballs.*` | Grant all permissions | op |
| `throwablefireballs.use` | Throw fireballs | true (everyone) |
| `throwablefireballs.admin` | Access admin commands | op |

### Advanced Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `throwablefireballs.bypasscooldown` | Bypass throw cooldown | op |
| `throwablefireballs.bypassprotection` | Bypass region protection | op |
| `throwablefireballs.blockdamage.toggle` | Toggle personal block damage | true |

## Configuration

The `config.yml` file is extensively documented. Here are the key sections:

### Item Settings
```yaml
item:
  material: FIRE_CHARGE          # Item to throw
  consume-on-throw: true         # Consume item?
  consume-in-creative: false     # Consume in creative?
  custom-model-data:             # For resource packs
    enabled: false
    value: 0
```

### Projectile Settings
```yaml
projectile:
  type: SMALL                    # SMALL or LARGE
  speed: 1.5                     # Velocity multiplier
```

### Explosion Settings
```yaml
explosion:
  block-damage: false            # Break blocks?
  entity-damage: true            # Damage players/mobs?
  power: 1.0                     # Explosion radius
  create-fire: true              # Create fire?
  fire-ticks: 100                # Fire duration
```

### Cooldown Settings
```yaml
cooldown:
  time-millis: 1000              # 1 second cooldown
  show-actionbar: true           # Show message?
```

### Protection Settings
```yaml
protection:
  respect-worldguard: true       # Use WorldGuard?
  check-build-permission: true   # Basic protection
```

### Per-Player Settings
```yaml
per-player-settings:
  allow-block-damage-toggle: true  # Players can toggle?
```

All messages are customizable with color code support (`&` codes).

## Multi-Version Support

This plugin is designed to work across multiple Paper versions (1.16.5+) without using NMS (Net Minecraft Server) code.

### How It Works

The plugin uses a **Version Adapter** pattern:

1. **VersionAdapter Interface** - Defines version-specific operations (e.g., action bar messages)
2. **ModernVersionAdapter** - Uses Paper's Adventure API (1.16.5+)
3. **LegacyVersionAdapter** - Reflection-based fallback for older versions

The plugin automatically detects your server version and uses the appropriate adapter.

### API Version

In `plugin.yml`, we set `api-version: 1.21` because:
- Version 1.1.0 targets Minecraft 1.21.x
- Maintains backward compatibility with 1.16.5+ due to no NMS usage
- Paper's API is stable across versions
- Modern features and optimizations available in 1.21

### Supported Versions

| Version | Status | Notes |
|---------|--------|-------|
| 1.16.5 | ✅ Compatible | Minimum supported |
| 1.17.x | ✅ Compatible | Uses modern adapter |
| 1.18.x | ✅ Compatible | Uses modern adapter |
| 1.19.x | ✅ Compatible | Uses modern adapter |
| 1.20.x | ✅ Compatible | Uses modern adapter |
| 1.21.x | ✅ Tested | **Primary target** |
| Future | ✅ Should work | No NMS dependencies |

## Building from Source

### Prerequisites
- Java 17 JDK or newer
- Git

### Build Steps

```bash
# Clone the repository
git clone https://github.com/nando123412/ThrowableFireballs.git
cd ThrowableFireballs

# Build with Gradle
./gradlew build

# The JAR will be in build/libs/ThrowableFireballs-1.1.0.jar
```

### Gradle Tasks

```bash
./gradlew clean          # Clean build directory
./gradlew build          # Build JAR (includes shadowJar)
./gradlew shadowJar      # Build shaded JAR with dependencies
```

## How It Works

### Throwing Fireballs

1. Player right-clicks with a fire charge (or configured item)
2. Plugin checks:
   - Permission (`throwablefireballs.use`)
   - Cooldown (unless bypassed)
   - Item validity (material, custom model data, display name)
   - Region protection (WorldGuard or basic checks)
3. Fireball is spawned with:
   - Player's eye direction
   - Configured velocity
   - Tagged with `PersistentDataContainer` (player UUID stored)
4. Item is consumed (if configured)
5. Cooldown is applied

### Explosion Control

When a fireball explodes:

1. Plugin checks if it's tagged as "ours" (via `PersistentDataContainer`)
2. If not tagged, explosion proceeds normally (vanilla behavior)
3. If tagged:
   - Gets the player UUID from the tag
   - Checks per-player settings (if enabled)
   - Falls back to global config
   - **Block Damage**: If disabled, clears block list and sets yield to 0
   - **Entity Damage**: If disabled, cancels damage events
   - **Fire**: Applies fire ticks if configured

**Critical Detail:** Even when `block-damage: false`, the explosion event is NOT cancelled, so entity damage still occurs naturally through Bukkit's damage system.

### Damage Handling

The plugin handles damage in two ways:

1. **Direct Hit** - `EntityDamageByEntityEvent` with `PROJECTILE` cause
   - Applies custom damage if configured
   - Can be disabled with `entity-damage: false`

2. **Explosion Damage** - Handled by Minecraft's explosion system
   - Works automatically when explosion is not cancelled
   - Controlled by `entity-damage` config option

## Troubleshooting

### Fireballs Don't Break Blocks
**Solution:** This is intentional if `explosion.block-damage: false` in config. Players still take damage.

### Action Bar Messages Don't Show
**Solution:** The plugin uses Adventure API (Paper 1.16.5+). If using older Spigot, messages fall back to chat.

### WorldGuard Integration Not Working
**Solution:** Ensure WorldGuard 7.0+ is installed and `protection.respect-worldguard: true` in config.

### Players Can Spam Fireballs
**Solution:** Reduce `cooldown.time-millis` or ensure players don't have `throwablefireballs.bypasscooldown` permission.

### Explosions Are Too Weak/Strong
**Solution:** Adjust `explosion.power` in config. Vanilla TNT is 4.0, Creeper is 3.0.

## Performance

The plugin is designed to be lightweight and performant:

- ✅ **No tick loops** - Event-driven architecture
- ✅ **Minimal reflection** - Only for action bars on legacy versions
- ✅ **Efficient cooldowns** - HashMap-based with cleanup
- ✅ **No database** - All data in memory
- ✅ **PersistentDataContainer** - Uses Bukkit's built-in tagging system

## WorldGuard Integration

If WorldGuard is installed:

1. Plugin detects WorldGuard on startup
2. Basic region checks are performed
3. **Note:** Full WorldGuard integration requires additional API usage
4. Server admins can use WorldGuard flags to control fireball behavior:
   - `creeper-explosion` flag
   - `ghast-fireball` flag
   - Custom flags (requires WorldGuard configuration)

For now, the plugin respects basic WorldGuard presence and can be extended with full API integration.

## Development

### Project Structure

```
src/main/java/com/nandoothjuuh/throwablefireballs/
├── ThrowableFireballsPlugin.java       # Main plugin class
├── commands/
│   ├── TFireballsCommand.java          # Command executor
│   └── TFireballsTabCompleter.java     # Tab completion
├── compat/
│   ├── VersionAdapter.java             # Version adapter interface
│   ├── ModernVersionAdapter.java       # Adventure API (1.16.5+)
│   └── LegacyVersionAdapter.java       # Reflection fallback
├── config/
│   ├── ConfigManager.java              # Config wrapper
│   └── MessageManager.java             # Message handler
├── cooldown/
│   └── CooldownManager.java            # Cooldown tracking
├── items/
│   └── FireballItemFactory.java        # Item creation
├── listeners/
│   ├── ThrowListener.java              # Throw event handler
│   └── ExplosionControlListener.java   # Explosion control
└── util/
    └── TextUtil.java                   # Text utilities
```

### Adding Features

To add new features:

1. Add config options to `config.yml` and `ConfigManager.java`
2. Add messages to `config.yml` and `MessageManager.java`
3. Create new listeners or modify existing ones
4. Register listeners in `ThrowableFireballsPlugin.java`
5. Add permissions to `plugin.yml`
6. Update README.md

## License

This project is provided as-is for educational and server use. Feel free to modify and use it on your server.

## Credits

- **Author**: nandoothjuuh
- **Plugin API**: Paper/Spigot/Bukkit
- **Built with**: Java 17, Gradle

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check the wiki for detailed guides
- Join the community Discord (if available)

---

**Note:** This plugin is designed for Paper servers. While it may work on Spigot, Paper is highly recommended for best performance and compatibility.
