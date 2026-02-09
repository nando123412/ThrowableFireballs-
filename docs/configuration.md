# Configuration Guide

Complete reference for `config.yml` settings.

## Configuration File Location

`plugins/ThrowableFireballs/config.yml`

## Full Configuration Reference

### Item Settings

Controls what item is used to throw fireballs.

```yaml
item:
  # Material name (must be valid Bukkit Material)
  material: FIRE_CHARGE
  
  # Custom model data for resource packs
  custom-model-data:
    enabled: false
    value: 0
  
  # Custom display name (requires exact match)
  display-name:
    enabled: false
    value: "&cFireball"
  
  # Consume item on throw?
  consume-on-throw: true
  
  # Consume in creative mode?
  consume-in-creative: false
```

**material** - Any valid Material name
- Default: `FIRE_CHARGE`
- Examples: `SNOWBALL`, `ENDER_PEARL`, `DIAMOND`

**custom-model-data** - For custom textures
- Set `enabled: true` to require specific model data
- Used with resource packs

**display-name** - For custom named items
- Set `enabled: true` to require exact name match
- Use `&` color codes

**consume-on-throw** - Should item be used up?
- `true` - One item per throw (default)
- `false` - Unlimited throws

**consume-in-creative** - Apply to creative mode?
- `false` - Creative players don't lose items (default)
- `true` - Even creative players consume items

---

### Projectile Settings

Controls the fireball entity behavior.

```yaml
projectile:
  # Type: SMALL or LARGE
  type: SMALL
  
  # Velocity multiplier
  speed: 1.5
```

**type** - Fireball size
- `SMALL` - SmallFireball entity (recommended, balanced)
- `LARGE` - Fireball entity (more explosive)

**speed** - How fast the fireball travels
- Default: `1.5`
- Range: `0.1` - `5.0`
- Higher = faster projectile

---

### Explosion Settings

**Most important section** - controls damage and destruction.

```yaml
explosion:
  # Break blocks?
  block-damage: false
  
  # Damage entities?
  entity-damage: true
  
  # Explosion power/radius
  power: 1.0
  
  # Create fire blocks?
  create-fire: true
  
  # Fire duration in ticks
  fire-ticks: 100
```

**block-damage** - Terrain destruction
- `false` - Blocks are protected (default, recommended)
- `true` - Fireballs break blocks

**entity-damage** - Player/mob damage
- `true` - Entities take damage (default)
- `false` - No entity damage (visual only)

**power** - Explosion strength
- Default: `1.0`
- Reference: TNT = `4.0`, Creeper = `3.0`
- Range: `0.0` - `10.0`

**create-fire** - Fire blocks
- `true` - Creates fire on impact (default)
- `false` - No fire blocks

**fire-ticks** - Fire duration
- Default: `100` (5 seconds)
- Formula: ticks ÷ 20 = seconds

---

### Damage Settings

Fine-tune direct hit damage.

```yaml
damage:
  direct-hit:
    enabled: true
    # Damage in half-hearts
    amount: 5.0
```

**enabled** - Custom direct hit damage?
- `true` - Use custom amount (default)
- `false` - Use vanilla damage

**amount** - Damage value
- Default: `5.0` (2.5 hearts)
- Formula: amount ÷ 2 = hearts
- Example: `10.0` = 5 hearts

---

### Cooldown Settings

Anti-spam protection.

```yaml
cooldown:
  # Cooldown duration in milliseconds
  time-millis: 1000
  
  # Show action bar message?
  show-actionbar: true
```

**time-millis** - Cooldown length
- Default: `1000` (1 second)
- Formula: milliseconds ÷ 1000 = seconds
- Examples:
  - `500` = 0.5 seconds
  - `2000` = 2 seconds
  - `5000` = 5 seconds

**show-actionbar** - Visual feedback
- `true` - Shows countdown in action bar (default)
- `false` - Silent cooldown

---

### Protection Settings

Integration with protection plugins.

```yaml
protection:
  # Respect WorldGuard regions?
  respect-worldguard: true
  
  # Check build permission?
  check-build-permission: true
```

**respect-worldguard** - Use WorldGuard?
- `true` - Check regions (default, requires WorldGuard)
- `false` - Ignore WorldGuard

**check-build-permission** - Basic protection
- `true` - Basic build checks (default)
- `false` - Allow throwing anywhere

---

### Per-Player Settings

Allow player customization.

```yaml
per-player-settings:
  # Allow /tfireballs blockdamage command?
  allow-block-damage-toggle: true
```

**allow-block-damage-toggle** - Personal preferences
- `true` - Players can toggle block damage (default)
- `false` - Only global config applies

---

### Messages

Customize all plugin messages.

```yaml
messages:
  prefix: "&8[&cFireballs&8]&r "
  no-permission: "&cYou don't have permission to do that!"
  reload-success: "&aConfiguration reloaded successfully!"
  cooldown: "&cWait {time}s before throwing another fireball!"
  # ... more messages
```

**Color Codes:**
- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White
- `&l` - Bold
- `&o` - Italic
- `&n` - Underline
- `&r` - Reset

**Placeholders:**
- `{time}` - Cooldown remaining (in cooldown message)
- `{player}` - Player name (in give message)
- `{amount}` - Item amount (in give message)

---

### Debug

Development/troubleshooting option.

```yaml
debug: false
```

**debug** - Verbose logging
- `false` - Normal logging (default)
- `true` - Extra debug info in console

---

## Common Configurations

### PvP Server (Protect Builds)
```yaml
explosion:
  block-damage: false
  entity-damage: true
  power: 1.5
cooldown:
  time-millis: 2000
```

### Creative Server (Unlimited Fun)
```yaml
explosion:
  block-damage: false
  entity-damage: false
  power: 2.0
item:
  consume-on-throw: false
cooldown:
  time-millis: 250
```

### Survival Server (Balanced)
```yaml
explosion:
  block-damage: true
  entity-damage: true
  power: 1.0
item:
  consume-on-throw: true
cooldown:
  time-millis: 1000
```

### Minigame Server (Fast-Paced)
```yaml
explosion:
  block-damage: false
  entity-damage: true
  power: 2.0
projectile:
  speed: 2.5
cooldown:
  time-millis: 500
```

---

## Reloading Configuration

After editing `config.yml`:

```
/tfireballs reload
```

Changes take effect immediately without server restart.

## Configuration Tips

1. **Always backup** `config.yml` before editing
2. **Use a YAML validator** to check syntax
3. **Test changes** in a test environment first
4. **Document custom values** with comments
5. **Keep default config** as reference

## Next Steps

- Set up [Commands & Permissions](commands-permissions.md)
- Learn about [Block vs Entity Damage](block-entity-damage.md)
- Configure [WorldGuard Integration](worldguard.md)
