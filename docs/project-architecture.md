# Project Architecture

Technical overview of ThrowableFireballs' design and structure.

---

## Overview

ThrowableFireballs follows a modular, event-driven architecture designed for maintainability, extensibility, and cross-version compatibility. This document explains the technical design decisions and code organization.

## Architecture Principles

### Core Design Goals

1. **Simplicity** - Easy to understand and modify
2. **Modularity** - Independent, reusable components
3. **Performance** - Minimal overhead, efficient processing
4. **Compatibility** - Works across Minecraft versions without NMS
5. **Extensibility** - Easy to add features or integrate with other plugins

### Design Patterns Used

- **Adapter Pattern** - Cross-version compatibility
- **Manager Pattern** - Organized responsibility separation
- **Factory Pattern** - Item creation
- **Observer Pattern** - Event-driven architecture (Bukkit events)
- **Singleton Pattern** - Plugin instance management

## Project Structure

```
ThrowableFireballs-/
├── src/main/java/dev/throwablefireballs/
│   ├── ThrowableFireballsPlugin.java      # Main plugin class
│   ├── commands/                           # Command handling
│   │   ├── TFireballsCommand.java
│   │   └── TFireballsTabCompleter.java
│   ├── compat/                             # Version compatibility
│   │   ├── VersionAdapter.java             # Interface
│   │   ├── ModernVersionAdapter.java       # 1.18+
│   │   └── LegacyVersionAdapter.java       # 1.16-1.17
│   ├── config/                             # Configuration management
│   │   ├── ConfigManager.java
│   │   └── MessageManager.java
│   ├── cooldown/                           # Cooldown system
│   │   └── CooldownManager.java
│   ├── items/                              # Item creation
│   │   └── FireballItemFactory.java
│   ├── listeners/                          # Event handlers
│   │   ├── ThrowListener.java              # Throw mechanics
│   │   └── ExplosionControlListener.java   # Explosion control
│   └── util/                               # Utilities
│       └── TextUtil.java
├── src/main/resources/
│   ├── plugin.yml                          # Plugin metadata
│   └── config.yml                          # Default configuration
├── build.gradle                            # Build configuration
├── settings.gradle                         # Project settings
└── gradlew                                 # Gradle wrapper
```

## Core Components

### 1. ThrowableFireballsPlugin

**Purpose:** Main plugin class, initialization hub

**Responsibilities:**
- Plugin lifecycle management (`onEnable()`, `onDisable()`)
- Manager initialization
- Version adapter selection
- Command registration
- Central configuration access

**Key methods:**

```java
public void onEnable() {
    // 1. Load configuration
    saveDefaultConfig();
    
    // 2. Initialize managers
    configManager = new ConfigManager(this);
    messageManager = new MessageManager(this);
    cooldownManager = new CooldownManager(this);
    fireballItemFactory = new FireballItemFactory(this);
    
    // 3. Select version adapter
    versionAdapter = selectVersionAdapter();
    
    // 4. Register events and commands
    registerListeners();
    registerCommands();
    
    // 5. Check for soft dependencies
    checkWorldGuard();
}
```

**Design decisions:**
- All managers created in `onEnable()` for predictable initialization
- Graceful degradation if soft dependencies missing
- No static singletons (uses instance-based approach)

### 2. Command System

**Architecture:**

```
TFireballsCommand (CommandExecutor)
    ├── Subcommand: reload
    ├── Subcommand: give [player]
    └── Subcommand: blockdamage

TFireballsTabCompleter (TabCompleter)
    ├── Tab complete: subcommands
    ├── Tab complete: player names
    └── Tab complete: context-aware suggestions
```

**Implementation pattern:**

```java
@Override
public boolean onCommand(CommandSender sender, Command command, 
                        String label, String[] args) {
    // Permission check
    if (!sender.hasPermission("throwablefireballs.command")) {
        sender.sendMessage(messages.getNoPermission());
        return true;
    }
    
    // Subcommand routing
    if (args.length == 0) {
        sendHelp(sender);
        return true;
    }
    
    switch (args[0].toLowerCase()) {
        case "reload" -> handleReload(sender);
        case "give" -> handleGive(sender, args);
        case "blockdamage" -> handleBlockDamage(sender);
        default -> sendHelp(sender);
    }
    
    return true;
}
```

**Design decisions:**
- Switch expressions for clean routing (Java 17+ feature)
- Early permission checks
- Always return `true` (we handle all cases)
- Separate methods for each subcommand (maintainability)

### 3. Version Compatibility Layer

**Problem:** API differences between Minecraft versions

**Solution:** Adapter pattern

```
┌─────────────────────┐
│  VersionAdapter     │ ◄─── Interface
│  (interface)        │
└──────────┬──────────┘
           │
     ┌─────┴─────┐
     │           │
┌────▼────┐ ┌───▼────────┐
│ Modern  │ │  Legacy    │
│ Adapter │ │  Adapter   │
│ (1.18+) │ │ (1.16-1.17)│
└─────────┘ └────────────┘
```

**Interface definition:**

```java
public interface VersionAdapter {
    /**
     * Send action bar message to player
     * @param player Target player
     * @param message Message to send
     */
    void sendActionBar(Player player, String message);
    
    // Future: additional version-specific methods
}
```

**Modern implementation (1.18+):**

```java
public class ModernVersionAdapter implements VersionAdapter {
    @Override
    public void sendActionBar(Player player, String message) {
        // Use Adventure API (native on Paper 1.18+)
        Component component = Component.text(message);
        player.sendActionBar(component);
    }
}
```

**Legacy implementation (1.16-1.17):**

```java
public class LegacyVersionAdapter implements VersionAdapter {
    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // Use legacy Spigot API with reflection fallback
            player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message)
            );
        } catch (Exception e) {
            // Graceful degradation: send to chat
            player.sendMessage(message);
        }
    }
}
```

**Adapter selection (automatic):**

```java
private VersionAdapter selectVersionAdapter() {
    String version = Bukkit.getVersion();
    int minorVersion = parseMinorVersion(version); // Extract 1.21 → 21
    
    if (minorVersion >= 18) {
        getLogger().info("Using ModernVersionAdapter (1.18+)");
        return new ModernVersionAdapter();
    } else {
        getLogger().info("Using LegacyVersionAdapter (1.16-1.17)");
        return new LegacyVersionAdapter();
    }
}
```

**Design decisions:**
- Interface keeps implementations decoupled
- Runtime detection (no compile-time version modules needed)
- Graceful fallback in legacy adapter
- Easy to add new methods as needed

### 4. Configuration System

**Two-manager approach:**

1. **ConfigManager** - Game configuration (`config.yml`)
2. **MessageManager** - Messages and localization (`messages.yml` or section)

**ConfigManager design:**

```java
public class ConfigManager {
    private final ThrowableFireballsPlugin plugin;
    
    // Cached values for performance
    private boolean enableBlockDamage;
    private double blockPower;
    // ... more cached values
    
    public ConfigManager(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void reload() {
        plugin.reloadConfig();
        loadConfig();
    }
    
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        this.enableBlockDamage = config.getBoolean("explosion.enable-block-damage");
        this.blockPower = config.getDouble("explosion.block-power");
        // ... load other values
    }
    
    // Getters (no setters - read-only by design)
    public boolean isEnableBlockDamage() { return enableBlockDamage; }
    public double getBlockPower() { return blockPower; }
}
```

**Design decisions:**
- Values cached in memory (don't read YAML every time)
- Reload method refreshes cache
- Read-only getters (no runtime config modification)
- Wrapper pattern simplifies config access

### 5. Event Listeners

**Two specialized listeners:**

#### ThrowListener

**Purpose:** Handle fireball throwing mechanics

**Events handled:**
- `PlayerInteractEvent` - Right-click to throw

**Processing flow:**

```
PlayerInteractEvent
    ↓
Check if right-click with fire charge
    ↓
Permission check
    ↓
Cooldown check
    ↓
WorldGuard protection check
    ↓
Consume item
    ↓
Launch fireball projectile
    ↓
Tag with PersistentDataContainer
    ↓
Start cooldown
```

**Code structure:**

```java
@EventHandler
public void onThrow(PlayerInteractEvent event) {
    // 1. Validate event
    if (!isValidThrowAction(event)) return;
    
    // 2. Check permissions
    if (!hasPermission(player)) {
        sendNoPermissionMessage(player);
        return;
    }
    
    // 3. Check cooldown
    if (cooldownManager.hasCooldown(player)) {
        sendCooldownMessage(player);
        return;
    }
    
    // 4. Check protection
    if (isProtected(location, player)) {
        sendProtectionMessage(player);
        return;
    }
    
    // 5. Launch fireball
    Fireball fireball = launchFireball(player);
    tagProjectile(fireball, player.getUniqueId());
    
    // 6. Consume item and start cooldown
    consumeItem(player);
    cooldownManager.setCooldown(player);
    
    event.setCancelled(true);
}
```

**Design decisions:**
- Early returns for failed checks (guard clauses)
- Separate validation methods (testable)
- Event cancellation only after full processing
- PersistentDataContainer for projectile tracking

#### ExplosionControlListener

**Purpose:** Control fireball explosion damage

**Events handled:**
- `EntityExplodeEvent` - Fireball explodes

**Processing flow:**

```
EntityExplodeEvent
    ↓
Check if entity is fireball
    ↓
Check if tagged as thrown fireball
    ↓
Retrieve thrower UUID from tag
    ↓
Determine block damage setting
    ↓
Modify block list accordingly
    ↓
Apply entity damage (via explosion power)
```

**Code structure:**

```java
@EventHandler
public void onExplosion(EntityExplodeEvent event) {
    // 1. Check if this is a fireball
    if (!(event.getEntity() instanceof Fireball)) return;
    
    Fireball fireball = (Fireball) event.getEntity();
    
    // 2. Check if thrown by plugin
    if (!hasThrowTag(fireball)) return;
    
    // 3. Get thrower
    UUID throwerId = getThrowerId(fireball);
    Player thrower = Bukkit.getPlayer(throwerId);
    
    // 4. Determine block damage setting
    boolean shouldDamageBlocks = shouldEnableBlockDamage(thrower);
    
    // 5. Modify explosion
    if (!shouldDamageBlocks) {
        event.blockList().clear(); // Remove all blocks
    }
    
    // Entity damage handled by Minecraft (via explosion power)
}
```

**Design decisions:**
- Type checking before casting (safety)
- Tag validation prevents affecting other fireballs
- Block list manipulation (clean API usage)
- Entity damage left to Minecraft engine

### 6. Cooldown System

**Architecture:**

```java
public class CooldownManager {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final ThrowableFireballsPlugin plugin;
    private final ScheduledExecutorService scheduler;
    
    public boolean hasCooldown(Player player) {
        Long expiryTime = cooldowns.get(player.getUniqueId());
        if (expiryTime == null) return false;
        
        return System.currentTimeMillis() < expiryTime;
    }
    
    public void setCooldown(Player player) {
        long duration = plugin.getConfigManager().getCooldownSeconds() * 1000L;
        long expiryTime = System.currentTimeMillis() + duration;
        cooldowns.put(player.getUniqueId(), expiryTime);
        
        // Action bar countdown
        startCountdown(player, duration);
        
        // Auto-remove after expiry
        scheduler.schedule(() -> cooldowns.remove(player.getUniqueId()),
                          duration, TimeUnit.MILLISECONDS);
    }
}
```

**Design decisions:**
- `Map<UUID, Long>` for O(1) lookups
- Millisecond timestamp for precision
- Auto-cleanup via scheduled executor
- Action bar updates on countdown

### 7. Data Persistence

**Player preferences:**

```java
// Store per-player block damage preference
NamespacedKey key = new NamespacedKey(plugin, "block-damage-enabled");
player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

// Retrieve preference
if (player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
    byte value = player.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
    boolean enabled = (value == 1);
}
```

**Projectile tagging:**

```java
// Tag thrown fireball with player UUID
NamespacedKey key = new NamespacedKey(plugin, "thrown-fireball");
fireball.getPersistentDataContainer().set(key, PersistentDataType.STRING, 
                                          player.getUniqueId().toString());

// Later, retrieve thrower
String uuidString = fireball.getPersistentDataContainer().get(key, PersistentDataType.STRING);
UUID throwerUuid = UUID.fromString(uuidString);
```

**Design decisions:**
- Use Bukkit's PersistentDataContainer (no custom file I/O)
- Namespaced keys prevent conflicts with other plugins
- Player data survives restarts automatically
- Minimal disk I/O (handled by Minecraft)

## Data Flow

### Throw Action Data Flow

```
[Player Right-Click]
        ↓
[ThrowListener.onThrow()]
        ↓
    Validate Action
        ↓
    Check Permission ───No──> [Send Error Message]
        ↓ Yes
    Check Cooldown ───Yes──> [Send Cooldown Message]
        ↓ No
    Check WorldGuard ───Protected──> [Send Protection Message]
        ↓ Not Protected
    Launch Fireball
        ↓
    Tag with UUID ────────────┐
        ↓                      ↓
    Start Cooldown    [PersistentDataContainer]
        ↓                      │
    Action Bar Timer          │
                              │
[Fireball Flies] <────────────┘
        ↓
[Fireball Hits Target]
        ↓
[EntityExplodeEvent]
        ↓
[ExplosionControlListener.onExplosion()]
        ↓
    Check if tagged fireball ───No──> [Ignore]
        ↓ Yes
    Retrieve thrower UUID
        ↓
    Check block damage setting
        ↓
    Clear block list if disabled
        ↓
[Explosion Occurs]
        ↓
    Damage entities
    (Optionally) Damage blocks
```

### Configuration Reload Flow

```
[Admin runs /tfireballs reload]
        ↓
[TFireballsCommand.handleReload()]
        ↓
[plugin.reload()]
        ↓
    ConfigManager.reload()
        ↓
    MessageManager.reload()
        ↓
    Clear cooldowns
        ↓
    Recalculate cached values
        ↓
[Send confirmation message]
```

## Threading Model

**All operations are synchronous** (Main server thread):

- Event handlers run on main thread
- Command execution on main thread
- Cooldown timers use Bukkit scheduler (thread-safe)

**Why no async?**
- Bukkit/Paper APIs are not thread-safe
- Entity/world modifications must be synchronous
- Plugin is fast enough without async

**Scheduled tasks:**
- Cooldown countdown (action bar updates)
- Cooldown expiry cleanup

## Dependency Management

**Hard dependencies:** None (standalone plugin)

**Soft dependencies:**
- WorldGuard 7.0+ (optional, detected at runtime)
- GriefPrevention (optional, future; detected at runtime)

**Dependency detection pattern:**

```java
private boolean isWorldGuardEnabled() {
    Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
    if (wg != null && wg.isEnabled()) {
        getLogger().info("WorldGuard detected! Using WorldGuard integration.");
        return true;
    }
    return false;
}
```

**Design decision:** Soft dependencies = more compatibility, but requires null checks.

## Error Handling

**Philosophy:** Fail gracefully, never crash

**Strategies:**

1. **Validation before processing:**
   ```java
   if (player == null) {
       getLogger().warning("Null player in throw event!");
       return;
   }
   ```

2. **Try-catch on external calls:**
   ```java
   try {
       // WorldGuard query
       worldGuard.canBuild(location, player);
   } catch (Exception e) {
       getLogger().warning("WorldGuard check failed: " + e.getMessage());
       // Assume protected (safe default)
       return true;
   }
   ```

3. **Graceful degradation:**
   ```java
   try {
       adapter.sendActionBar(player, message);
   } catch (Exception e) {
       // Fallback: send to chat
       player.sendMessage(message);
   }
   ```

4. **Logging:**
   ```java
   getLogger().severe("Critical error: " + e.getMessage());
   e.printStackTrace();
   ```

**Never throw uncaught exceptions** - always handle or log.

## Testing Strategy

### Manual Testing

**Test matrix:**
- ✅ Minecraft versions: 1.16.5, 1.18.2, 1.19.4, 1.20.6, 1.21.4
- ✅ With/without WorldGuard
- ✅ Different configurations (block damage on/off, per-player settings, etc.)
- ✅ Edge cases (offline player,large explosions, rapid throws)

### Automated Testing

Currently: Minimal unit tests (planned for v1.2.0)

**Future test structure:**

```
src/test/java/
├── ConfigManagerTest.java
├── CooldownManagerTest.java
├── TextUtilTest.java
└── VersionAdapterTest.java
```

## Performance Considerations

### Optimization techniques:

1. **Config caching** - Read YAML once, cache values
2. **Map lookups** - O(1) cooldown checks
3. **Early returns** - Fail fast on validation
4. **No unnecessary loops** - Process only what's needed
5. **Minimal reflection** - Only in legacy adapter fallback

### Profiling points:

- Event handler execution time
- WorldGuard query latency
- Explosion processing time

See [Performance Guide](performance-guide.md) for benchmarks.

## Future Enhancements

**Planned architecture improvements:**

1. **Event API** - Custom events for external plugins
2. **Database support** - Optional persistent statistics
3. **Modular abilities** - Plugin addon system
4. **Async WorldGuard queries** - If WorldGuard adds async API
5. **Unit test suite** - Comprehensive test coverage

## FAQ

**Q: Why no database for player preferences?**  
A: PersistentDataContainer is simpler, doesn't require MySQL, and is fast enough.

**Q: Why not use NMS for better features?**  
A: NMS breaks every update. API-only design is more maintainable and future-proof.

**Q: Could this be a library instead of a plugin?**  
A: Possibly! But as a plugin it's easier for server owners to install and use.

**Q: Why Gradle instead of Maven?**  
A: Gradle is faster and more flexible. Personal preference.

**Q: Is the code open source?**  
A: Yes! MIT licensed. See [GitHub repository](https://github.com/nando123412/ThrowableFireballs-).

## See Also

- [API Documentation](api-documentation.md) - For extending the plugin
- [Building from Source](building-from-source.md) - Compile and modify
- [Multi-Version Support](multi-version-support.md) - Version adapter details
- [Contributing Guide](../CONTRIBUTING.md) - Contribution workflow
