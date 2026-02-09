# WorldGuard Integration

How ThrowableFireballs integrates with WorldGuard for region protection.

---

## Overview

ThrowableFireballs automatically integrates with **WorldGuard 7.0+** to respect region protections. This ensures that protected areas remain safe from fireball damage while maintaining full functionality elsewhere.

## Features

✅ **Automatic Detection** - Plugin detects WorldGuard if installed  
✅ **Region Protection** - Fireballs respect region flags  
✅ **Soft Dependency** - Works with or without WorldGuard  
✅ **Bypass Permission** - Admins can override protection  
✅ **Zero Configuration** - Works out of the box  

## Requirements

### Supported Versions

- **WorldGuard 7.0+** (required)
- **WorldEdit** (required by WorldGuard)
- **Minecraft 1.16.5 - 1.21.x**

### Installation

1. **Install WorldGuard:**
   - Download from [EngineHub](https://enginehub.org/worldguard/)
   - Place `worldguard-bukkit-7.x.x.jar` in `/plugins/`

2. **Install WorldEdit:**
   - Download from [EngineHub](https://enginehub.org/worldedit/)
   - Place `worldedit-bukkit-7.x.x.jar` in `/plugins/`

3. **Restart Server:**
   ```
   /stop
   ```
   (or `/restart` if you have a restart script)

4. **Verify Integration:**
   ```
   /tfireballs reload
   ```
   
   Look for messages:
   ```
   [ThrowableFireballs] WorldGuard detected! Using WorldGuard integration.
   ```

## How It Works

### Protection Check Flow

When a player throws a fireball:

1. **Throw Event** → Plugin checks if throw location is protected
2. **WorldGuard Query** → `canBuild()` check at throw location
3. **If Protected** → Prevent throw, send message to player
4. **If Not Protected** → Allow throw, fireball launches

### Protected Regions Behavior

In a protected region:

- ❌ **Cannot throw fireballs** (unless bypass permission)
- ❌ **Fireballs from outside don't damage blocks inside**
- ✅ **Fireballs can still damage entities** (if PvP allowed)

### Region Flags

ThrowableFireballs respects these WorldGuard flags:

| Flag | Effect |
|------|--------|
| `build: deny` | Prevents fireball throws |
| `tnt: deny` | May prevent fireballs (depends on server) |
| `other-explosion: deny` | May prevent fireballs (depends on server) |
| `pvp: deny` | Allows throws but may block entity damage |

**Note:** Primary check is `build` flag. PvP flag affects entity damage separately.

## Configuration

### Plugin Configuration

In `config.yml`:

```yaml
protection:
  check-worldguard: true
  enable-bypass-permission: true
```

- `check-worldguard: true` - Enable WorldGuard integration
- `enable-bypass-permission: true` - Allow bypass permission to work

### WorldGuard Region Setup

No special configuration needed in WorldGuard itself. Standard region protection automatically applies.

## Permissions

### Bypass Permission

```
throwablefireballs.bypass
```

**Grants:**
- Throw fireballs in protected regions
- Damage blocks in protected regions
- Override WorldGuard protection

**Recommended for:** Server admins and trusted staff only

**Warning:** This is very powerful. Use carefully!

**Example (LuckPerms):**
```
/lp group admin permission set throwablefireballs.bypass true
```

### Permission Matrix

| Permission | Can Throw in Protected Region? | Can Damage Protected Blocks? |
|------------|----------------------------------|------------------------------|
| `throwablefireballs.use` only | ❌ No | ❌ No |
| `throwablefireballs.use` + `throwablefireballs.bypass` | ✅ Yes | ✅ Yes |
| No permissions | ❌ No | ❌ No |

## Common Scenarios

### Scenario 1: PvP Arena

**Goal:** Allow fireballs in arena, protect surrounding areas

**Setup:**
1. Create arena region:
   ```
   /pos1    # Select corner 1
   /pos2    # Select corner 2
   /rg create pvp_arena
   ```

2. Allow PvP in arena:
   ```
   /rg flag pvp_arena pvp allow
   /rg flag pvp_arena build allow
   ```

3. Protect spawn:
   ```
   /rg create spawn
   /rg flag spawn build deny
   /rg flag spawn pvp deny
   ```

**Result:**
- ✅ Fireballs work in arena
- ❌ Fireballs blocked at spawn

### Scenario 2: Town Protection

**Goal:** Protect player town, allow fireballs in wilderness

**Setup:**
1. Create town region:
   ```
   /rg create town_name
   /rg flag town_name build deny
   ```

2. Add town members:
   ```
   /rg addmember town_name player1 player2
   ```

3. Allow members to build:
   ```
   /rg flag town_name -g members build allow
   ```

**Result:**
- ❌ Non-members can't throw fireballs in town
- ✅ Members can throw fireballs in town
- ✅ Anyone can throw fireballs in wilderness

### Scenario 3: Event Areas

**Goal:** Allow fireballs during events, protect normally otherwise

**Setup:**
1. Create event region:
   ```
   /rg create event_arena
   /rg flag event_arena build deny
   ```

2. During event, temporarily allow fireballs:
   ```
   /rg flag event_arena build allow
   ```

3. After event, restore protection:
   ```
   /rg flag event_arena build deny
   ```

**Alternative:** Give event participants bypass permission temporarily.

### Scenario 4: Admin Build Areas

**Goal:** Staff can use fireballs anywhere, players only in allowed zones

**Setup:**
1. Give admins bypass:
   ```
   /lp group admin permission set throwablefireballs.bypass true
   ```

2. Protect sensitive areas:
   ```
   /rg flag __global__ build deny
   /rg flag wilderness build allow
   ```

**Result:**
- ✅ Admins throw fireballs anywhere
- ❌ Players blocked in protected regions
- ✅ Players allowed in wilderness

## Troubleshooting

### "Fireballs work in protected regions when they shouldn't"

**Check:**
1. WorldGuard is installed and loaded:
   ```
   /plugins
   ```
   Should show "WorldGuard" in green

2. ThrowableFireballs detected WorldGuard:
   ```
   /tfireballs reload
   ```
   Look for "WorldGuard detected!" message

3. `check-worldguard: true` in ThrowableFireballs config

4. Region has `build: deny` flag:
   ```
   /rg info <region_name>
   ```

5. Player doesn't have bypass permission

### "Fireballs don't work anywhere after installing WorldGuard"

**Check:**
1. Global region isn't blocking everything:
   ```
   /rg info __global__
   ```
   If `build: deny`, either remove it or set `build: allow` in specific regions

2. Players have `throwablefireballs.use` permission

3. Regions are configured correctly:
   ```
   /rg list
   ```

### "Players with bypass permission still can't throw fireballs"

**Check:**
1. `enable-bypass-permission: true` in config
2. Permission is exactly: `throwablefireballs.bypass` (no typos)
3. Permission plugin has loaded permissions:
   ```
   /lp user <player> permission check throwablefireballs.bypass
   ```
4. Player has logged out and back in (to refresh permissions)

### "WorldGuard not detected"

**Verify:**
1. WorldGuard **7.0 or higher** (6.x won't work)
2. WorldGuard is loaded before ThrowableFireballs:
   ```
   /plugins
   ```
   Both should show in green

3. Check server logs for errors:
   ```
   grep -i "worldguard" logs/latest.log
   grep -i "throwablefireballs" logs/latest.log
   ```

**Solution:** Ensure correct WorldGuard version, restart server.

## Advanced Configuration

### Multiple Protection Plugins

ThrowableFireballs supports multiple protection plugins simultaneously:

- **WorldGuard** - Preferred, full integration
- **GriefPrevention** - Automatic claim protection
- **Towny** - Town protection
- **Factions** - Faction territory protection

**Priority:** All protection plugins are checked. If ANY plugin blocks the action, it's prevented.

### Custom Region Priorities

WorldGuard regions have priorities. Higher priority wins:

```
/rg priority pvp_arena 10
/rg priority spawn 100
```

Higher number = higher priority. Useful for overlapping regions.

### Per-Region Messages

You can customize messages when players try to throw fireballs in protected regions by editing `messages.yml`:

```yaml
protection:
  cannot-throw-here: "&cYou cannot throw fireballs in this region!"
```

Then reference in WorldGuard greeting/farewell flags if desired.

## Comparison: With vs Without WorldGuard

| Feature | Without WorldGuard | With WorldGuard |
|---------|-------------------|-----------------|
| Region Protection | ❌ None | ✅ Full region support |
| Per-Area Control | ❌ Not possible | ✅ Per-region flags |
| Town Protection | ❌ Manual only | ✅ Automatic |
| PvP Zones | ❌ Hard to manage | ✅ Easy with flags |
| Admin Override | ❌ Not available | ✅ Bypass permission |

Recommendation: **Use WorldGuard** for any survival or factions server.

## API for Developers

### Checking Protection Programmatically

```java
// Check if player can throw at location
Location loc = player.getLocation();
boolean canThrow = !isProtected(loc, player);

private boolean isProtected(Location loc, Player player) {
    // Your WorldGuard integration check
    if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
        // WorldGuard API check
        return !WorldGuard.getInstance()
            .getPlatform()
            .getRegionContainer()
            .createQuery()
            .testBuild(BukkitAdapter.adapt(loc), 
                       WorldGuardPlugin.inst().wrapPlayer(player));
    }
    return false;
}
```

See [API Documentation](api-documentation.md) for more examples.

## FAQ

**Q: Do I need WorldGuard?**  
A: No, it's optional. ThrowableFireballs works fine without it. Install WorldGuard if you want region protection.

**Q: What if I use GriefPrevention instead?**  
A: ThrowableFireballs supports GriefPrevention too. Protection checks work automatically.

**Q: Can I use both WorldGuard and GriefPrevention?**  
A: Yes! Both protections will be checked. If either blocks it, the action is prevented.

**Q: Why use WorldGuard over other plugins?**  
A: WorldGuard is industry-standard, actively maintained, and has the most features. But others work too.

**Q: Does this work with WorldGuard 6.x?**  
A: No, you need WorldGuard 7.0 or higher for API compatibility.

**Q: Can I block fireballs but allow TNT in a region?**  
A: Not directly. Fireballs follow the `build` flag. You'd need custom scripting or multiple regions.

## See Also

- [Configuration Guide](configuration.md) - Protection config options
- [Commands & Permissions](commands-permissions.md) - Bypass permission details
- [Troubleshooting](troubleshooting.md) - Common WorldGuard issues
- [Block vs Entity Damage](block-vs-entity-damage.md) - How protection affects damage
