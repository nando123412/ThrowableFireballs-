# Commands & Permissions

Complete reference for all commands and permission nodes.

## Commands

### Main Command

```
/tfireballs [subcommand]
```

**Aliases:** `/tfb`, `/throwablefireball`

### Subcommands

#### Reload
```
/tfireballs reload
```
- **Description:** Reload configuration without restarting server
- **Permission:** `throwablefireballs.admin`
- **Usage:** `/tfireballs reload`

Changes in `config.yml` take effect immediately.

#### Give
```
/tfireballs give <player> [amount]
```
- **Description:** Give fireball items to a player
- **Permission:** `throwablefireballs.admin`
- **Arguments:**
  - `<player>` - Target player name (required)
  - `[amount]` - Number of items (optional, default: 1)
- **Examples:**
  - `/tfireballs give Steve` - Give Steve 1 fireball
  - `/tfireballs give Alex 64` - Give Alex 64 fireballs

Items are added to player's inventory.

#### Block Damage Toggle
```
/tfireballs blockdamage <on|off>
```
- **Description:** Toggle block damage for your own fireballs
- **Permission:** `throwablefireballs.blockdamage.toggle`
- **Arguments:**
  - `<on|off>` - Enable or disable (required)
- **Config:** Must have `per-player-settings.allow-block-damage-toggle: true`
- **Examples:**
  - `/tfireballs blockdamage off` - Your fireballs won't break blocks
  - `/tfireballs blockdamage on` - Your fireballs will break blocks

Personal setting overrides global config.

---

## Permissions

### Permission Hierarchy

```
throwablefireballs.*
├── throwablefireballs.use
├── throwablefireballs.admin
│   ├── throwablefireballs.reload
│   └── throwablefireballs.give
├── throwablefireballs.bypasscooldown
├── throwablefireballs.bypassprotection
└── throwablefireballs.blockdamage.toggle
```

### Main Permissions

#### throwablefireballs.*
- **Description:** Grants all permissions
- **Default:** `op`
- **Includes:** All permissions below

#### throwablefireballs.use
- **Description:** Throw fireballs
- **Default:** `true` (everyone)
- **Function:** Required to use fireball items

#### throwablefireballs.admin
- **Description:** Access admin commands
- **Default:** `op`
- **Includes:**
  - `throwablefireballs.reload`
  - `throwablefireballs.give`

### Advanced Permissions

#### throwablefireballs.bypasscooldown
- **Description:** No cooldown when throwing
- **Default:** `op`
- **Use Case:** VIP players, staff

#### throwablefireballs.bypassprotection
- **Description:** Throw in protected regions
- **Default:** `op`
- **Use Case:** Server events, admins
- **Note:** Overrides WorldGuard checks

#### throwablefireballs.blockdamage.toggle
- **Description:** Use `/tfireballs blockdamage` command
- **Default:** `true` (everyone)
- **Requires:** `per-player-settings.allow-block-damage-toggle: true` in config

#### throwablefireballs.reload
- **Description:** Reload configuration
- **Default:** `op`
- **Parent:** `throwablefireballs.admin`

#### throwablefireballs.give
- **Description:** Give items to players
- **Default:** `op`
- **Parent:** `throwablefireballs.admin`

---

## Permission Examples

### LuckPerms

#### Give basic access to all players:
```
/lp group default permission set throwablefireballs.use true
```

#### Create VIP group with no cooldown:
```
/lp creategroup vip
/lp group vip permission set throwablefireballs.use true
/lp group vip permission set throwablefireballs.bypasscooldown true
```

#### Grant admin permissions to moderators:
```
/lp group mod permission set throwablefireballs.admin true
```

### GroupManager

In `groups.yml`:
```yaml
groups:
  default:
    permissions:
      - throwablefireballs.use
      - throwablefireballs.blockdamage.toggle
  
  vip:
    permissions:
      - throwablefireballs.bypasscooldown
  
  mod:
    permissions:
      - throwablefireballs.admin
```

### PermissionsEx (PEX)

```
/pex group default add throwablefireballs.use
/pex group vip add throwablefireballs.bypasscooldown
/pex group admin add throwablefireballs.*
```

---

## Tab Completion

All commands support tab completion:

### /tfireballs
Suggests: `reload`, `give`, `blockdamage`

### /tfireballs give
- First argument: Online player names
- Second argument: `1`, `8`, `16`, `32`, `64`

### /tfireballs blockdamage
Suggests: `on`, `off`

---

## Permission Scenarios

### Scenario 1: Public Server
**Everyone can throw, mods can give items:**

```yaml
default:
  - throwablefireballs.use
  - throwablefireballs.blockdamage.toggle

moderator:
  - throwablefireballs.admin
  - throwablefireballs.bypasscooldown
```

### Scenario 2: Donor Perks
**Free players have cooldown, donors bypass it:**

```yaml
default:
  - throwablefireballs.use

donor:
  - throwablefireballs.bypasscooldown
```

### Scenario 3: Event Server
**Staff can throw anywhere, players only in arena:**

```yaml
player:
  - throwablefireballs.use

staff:
  - throwablefireballs.use
  - throwablefireballs.bypassprotection
  - throwablefireballs.bypasscooldown
```

### Scenario 4: Creative Server
**Everyone has full access:**

```yaml
default:
  - throwablefireballs.use
  - throwablefireballs.bypasscooldown
  - throwablefireballs.blockdamage.toggle
```

---

## Command Aliases

You can use any of these:
- `/tfireballs` (main)
- `/tfb` (short)
- `/throwablefireball` (long)

All work identically.

---

## Permission Troubleshooting

### Player can't throw fireballs
**Check:**
1. Has `throwablefireballs.use` permission
2. Holding correct item (check `config.yml`)
3. Not in protected region (unless has bypass)
4. Not on cooldown (check console)

### Admin commands not working
**Check:**
1. Has `throwablefireballs.admin` permission
2. Or specific `throwablefireballs.reload`/`give` permission
3. Permission plugin is loaded
4. Used `/reload` after permission change

### Cooldown bypass not working
**Check:**
1. Has `throwablefireballs.bypasscooldown` permission
2. Permission is applied (check with permission plugin)
3. Tested after getting permission

---

## Next Steps

- Configure [Block vs Entity Damage](block-entity-damage.md)
- Set up [Per-Player Settings](per-player-settings.md)
- Learn [WorldGuard Integration](worldguard.md)
