# Block vs Entity Damage

Understanding how ThrowableFireballs separates block damage from entity damage.

---

## Overview

One of the key features of ThrowableFireballs is the ability to **keep entity damage while disabling block damage**. This is perfect for PvP servers where you want players to fight with fireballs without destroying the terrain.

## How It Works

### Two Separate Systems

The plugin uses two independent explosion systems:

1. **Block Damage System** - Controls whether fireballs destroy blocks
2. **Entity Damage System** - Controls whether fireballs hurt players and mobs

These systems work completely independently, giving you full control over gameplay.

### Default Behavior

By default (in `config.yml`):

```yaml
explosion:
  enable-block-damage: false  # Blocks are NOT damaged
  enable-entity-damage: true  # Entities ARE damaged
```

This means:
- ✅ Fireballs will damage players and mobs
- ❌ Fireballs will NOT break or damage blocks
- ✅ Perfect for PvP arenas and survival servers

## Configuration Options

### Global Settings

Located in `config.yml`:

```yaml
explosion:
  # Block damage control
  enable-block-damage: false
  block-power: 2.0
  
  # Entity damage control
  enable-entity-damage: true
  entity-power: 2.0
  
  # Additional options
  set-fire: true
  enable-per-player-choice: true
```

### Per-Player Override

If `enable-per-player-choice: true`, players with the permission `throwablefireballs.toggleblockdamage` can toggle block damage for their own fireballs:

```
/tfireballs blockdamage
```

This allows player customization while maintaining server defaults.

## Technical Details

### How Explosions Are Processed

When a fireball hits something:

1. **Explosion Event Fires** - `EntityExplodeEvent` is triggered
2. **Plugin Checks Origin** - Uses `PersistentDataContainer` to identify if this is a thrown fireball
3. **Block Damage Check**:
   - If block damage is disabled globally → Clear block list
   - If per-player choice enabled → Check player's preference
   - If player disabled block damage → Clear block list
4. **Entity Damage Check** - Separately processed via explosion power settings

### Block List Manipulation

The plugin modifies the explosion's block list:

```java
// If block damage is disabled
event.blockList().clear();  // Remove all blocks from explosion
```

This prevents terrain damage while leaving entity damage intact.

### Entity Damage Calculation

Entity damage is calculated by Minecraft's explosion mechanics using the `entity-power` value:

- `entity-power: 1.0` - Weak damage (3-4 hearts up close)
- `entity-power: 2.0` - Moderate damage (5-7 hearts up close)
- `entity-power: 3.0` - Strong damage (8-10 hearts up close)
- `entity-power: 4.0` - Very strong damage (near-lethal)

## Common Scenarios

### Scenario 1: PvP Arena (Default)
```yaml
enable-block-damage: false
enable-entity-damage: true
```
Result: Players fight with fireballs, arena stays intact.

### Scenario 2: Creative Destruction
```yaml
enable-block-damage: true
enable-entity-damage: false
```
Result: Fireballs destroy terrain but can't hurt players. Good for creative building demolition.

### Scenario 3: Full Warfare
```yaml
enable-block-damage: true
enable-entity-damage: true
```
Result: Total destruction - both players and terrain take damage.

### Scenario 4: Safe Practice
```yaml
enable-block-damage: false
enable-entity-damage: false
```
Result: Completely harmless fireballs for practice or fun.

## Per-Player Preferences

### Enabling Player Choice

Set in `config.yml`:

```yaml
explosion:
  enable-per-player-choice: true
  default-player-block-damage-enabled: false
```

### Player Commands

Players can toggle their own block damage:

```
/tfireballs blockdamage
```

This only affects **their thrown fireballs**, not other players' fireballs.

### Permission Required

```
throwablefireballs.toggleblockdamage
```

Grant this to players you trust to make this choice.

## Protection Integration

### How Protection Affects Damage

If a region is protected (WorldGuard, GriefPrevention, etc.):

1. **Block Damage** - Automatically prevented regardless of settings
2. **Entity Damage** - Still works normally (PvP in protected regions)

This ensures protected areas stay safe while allowing combat.

### Override Protection

To allow block damage even in protected regions, give players:

```
throwablefireballs.bypass
```

**Warning:** This is powerful - only give to trusted staff.

## Troubleshooting

### "Fireballs aren't damaging players"

Check:
1. `enable-entity-damage: true` in config
2. `entity-power` is above 0.0
3. Players aren't in creative/spectator mode
4. No other plugins blocking damage

### "Fireballs are breaking blocks when they shouldn't"

Check:
1. `enable-block-damage: false` in config
2. Player doesn't have personal override enabled
3. `/tfireballs reload` was run after config changes
4. No other plugins causing explosions

### "Per-player settings aren't working"

Check:
1. `enable-per-player-choice: true` in config
2. Player has `throwablefireballs.toggleblockdamage` permission
3. Player's preference is saved (check with `/tfireballs blockdamage` - should show current status)

## FAQ

**Q: Can I disable entity damage for specific players?**  
A: Not built-in, but you could use a permission plugin to deny throw permission, or use WorldGuard flags for PvP in specific regions.

**Q: Why do my fireballs create craters even with block damage disabled?**  
A: Another plugin might be interfering. Check your explosion management plugins.

**Q: Can block damage affect only certain block types?**  
A: Not currently. It's all-or-nothing for blocks. This may be added in a future version.

**Q: Does armor reduce fireball damage?**  
A: Yes! Minecraft's normal damage calculation applies, including armor, protection enchantments, and resistance effects.

## Advanced: Explosion Power Explained

The `power` values control explosion radius and damage:

### Block Power
- Radius formula: `power * 2` blocks
- `block-power: 2.0` = ~4 block radius
- Affects: Crater size, fire spread area

### Entity Power
- Damage calculation: Based on distance from explosion center
- `entity-power: 2.0` = Moderate damage, ~6 block damage radius
- Affects: Player/mob damage, knockback strength

### Fire Setting
```yaml
set-fire: true  # Creates fire blocks at explosion site
```

Fire is created regardless of block damage settings - it's decorative/environmental.

## See Also

- [Configuration Guide](configuration.md) - Full config.yml reference
- [Commands & Permissions](commands-permissions.md) - All commands and permission nodes
- [Per-Player Settings](per-player-settings.md) - Detailed player customization
- [WorldGuard Integration](worldguard-integration.md) - Protection setup
