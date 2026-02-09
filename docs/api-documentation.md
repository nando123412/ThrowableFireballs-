# API Documentation

Developer guide for integrating with and extending ThrowableFireballs.

---

## Overview

ThrowableFireballs provides a simple but powerful API for:

- **Event listening** - React to fireball throws and explosions
- **Programmatic access** - Give items, check settings, manage cooldowns
- **Custom extensions** - Add your own mechanics
- **Hook integration** - Connect with other plugins

## Getting Started

### Adding as Dependency

**Maven:**

```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.nando123412</groupId>
        <artifactId>ThrowableFireballs</artifactId>
        <version>1.1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**Gradle (Kotlin DSL):**

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.nando123412:ThrowableFireballs:1.1.0")
}
```

**Gradle (Groovy):**

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.nando123412:ThrowableFireballs:1.1.0'
}
```

### Plugin Dependency

In your `plugin.yml`:

```yaml
depend: [ThrowableFireballs]
# or as soft dependency:
softdepend: [ThrowableFireballs]
```

### Getting Plugin Instance

```java
import dev.throwablefireballs.ThrowableFireballsPlugin;

@Override
public void onEnable() {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("ThrowableFireballs");
    
    if (plugin instanceof ThrowableFireballsPlugin) {
        ThrowableFireballsPlugin tfPlugin = (ThrowableFireballsPlugin) plugin;
        // Use API
    }
}
```

## Core API

### Giving Fireball Items

```java
import dev.throwablefireballs.items.FireballItemFactory;

// Get the factory
FireballItemFactory factory = plugin.getFireballItemFactory();

// Create fireball item
ItemStack fireball = factory.createFireballItem();

// Give to player
player.getInventory().addItem(fireball);

// Or directly via plugin method
plugin.giveFireballItem(player);
```

### Checking Player Settings

```java
// Check if player has block damage enabled
boolean hasBlockDamage = plugin.shouldEnableBlockDamage(player);

if (hasBlockDamage) {
    player.sendMessage("Your fireballs will damage blocks!");
} else {
    player.sendMessage("Your fireballs won't damage blocks.");
}
```

### Managing Cooldowns

```java
import dev.throwablefireballs.cooldown.CooldownManager;

CooldownManager cooldownManager = plugin.getCooldownManager();

// Check if player has cooldown
if (cooldownManager.hasCooldown(player)) {
    long remaining = cooldownManager.getRemainingCooldown(player);
    player.sendMessage("Wait " + remaining + " seconds");
} else {
    // Start cooldown
    cooldownManager.setCooldown(player);
}

// Remove cooldown (admin bypass)
cooldownManager.removeCooldown(player);
```

### Reloading Configuration

```java
// Reload all configs
plugin.reload();

// Plugin will reload:
// - config.yml
// - messages.yml
// - All managers (cooldowns reset, etc.)
```

### Accessing Configuration

```java
import dev.throwablefireballs.config.ConfigManager;

ConfigManager config = plugin.getConfigManager();

// Get config values
boolean blockDamage = config.isEnableBlockDamage();
double blockPower = config.getBlockPower();
int cooldownSeconds = config.getCooldownSeconds();

// Check feature flags
boolean perPlayerChoice = config.isEnablePerPlayerChoice();
boolean worldGuardCheck = config.isCheckWorldGuard();
```

### Version Adapter

```java
import dev.throwablefireballs.compat.VersionAdapter;

VersionAdapter adapter = plugin.getVersionAdapter();

// Send action bar (version-agnostic)
adapter.sendActionBar(player, "§cCooldown: 5s");

// Works on all Minecraft versions (1.16.5 - 1.21.x)
```

## Events API

### Custom Events

ThrowableFireballs fires custom events you can listen to:

#### FireballThrowEvent

Fired when a player throws a fireball.

```java
import dev.throwablefireballs.events.FireballThrowEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyListener implements Listener {
    
    @EventHandler
    public void onFireballThrow(FireballThrowEvent event) {
        Player player = event.getPlayer();
        Projectile projectile = event.getProjectile();
        
        // Check if cancelled
        if (event.isCancelled()) {
            return;
        }
        
        // Cancel the throw
        event.setCancelled(true);
        player.sendMessage("Fireballs disabled!");
        
        // Modify projectile
        projectile.setVelocity(projectile.getVelocity().multiply(2)); // Double speed!
    }
}
```

**Event details:**
- **Cancellable:** Yes
- **When fired:** When player throws a fireball (after all internal checks pass)
- **Access:** Player, Projectile, Location

#### FireballExplosionEvent

Fired when a thrown fireball explodes.

```java
import dev.throwablefireballs.events.FireballExplosionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyListener implements Listener {
    
    @EventHandler
    public void onFireballExplosion(FireballExplosionEvent event) {
        Player thrower = event.getThrower(); // Can be null if player logged off
        Entity projectile = event.getProjectile();
        Location location = event.getLocation();
        List<Block> blocks = event.getBlockList();
        
        // Modify explosion
        event.setBlockDamageEnabled(false); // Disable block damage
        event.setEntityDamageEnabled(true);  // Enable entity damage
        
        // Clear specific blocks from explosion
        blocks.removeIf(block -> block.getType() == Material.DIAMOND_BLOCK);
        
        // Cancel entirely
        event.setCancelled(true);
    }
}
```

**Event details:**
- **Cancellable:** Yes
- **When fired:** When thrown fireball explodes (before damage is applied)
- **Access:** Thrower (UUID), Projectile, Block list, Damage settings

### Bukkit Events

You can also listen to standard Bukkit events:

```java
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

@EventHandler
public void onProjectileLaunch(ProjectileLaunchEvent event) {
    if (event.getEntity() instanceof Fireball) {
        Fireball fireball = (Fireball) event.getEntity();
        
        // Check if this is a ThrowableFireballs fireball
        if (fireball.getPersistentDataContainer().has(
                new NamespacedKey(plugin, "thrown-fireball"), 
                PersistentDataType.STRING)) {
            // This is from ThrowableFireballs!
        }
    }
}

@EventHandler
public void onExplosion(EntityExplodeEvent event) {
    if (event.getEntity() instanceof Fireball) {
        // Handle explosion
    }
}
```

## Advanced Usage

### Creating Custom Fireball Items

```java
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// Get base item
ItemStack customFireball = plugin.getFireballItemFactory().createFireballItem();

// Customize it
ItemMeta meta = customFireball.getItemMeta();
meta.setDisplayName("§6Super Fireball");
List<String> lore = new ArrayList<>();
lore.add("§7This fireball is extra powerful!");
meta.setLore(lore);
customFireball.setItemMeta(meta);

// Give to player
player.getInventory().addItem(customFireball);
```

**Note:** Custom items will still work as fireballs - the plugin checks material type, not display name.

### Per-Player Data Access

```java
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

// Get player's block damage preference
NamespacedKey key = new NamespacedKey(plugin, "block-damage-enabled");
PersistentDataContainer pdc = player.getPersistentDataContainer();

if (pdc.has(key, PersistentDataType.BYTE)) {
    byte value = pdc.get(key, PersistentDataType.BYTE);
    boolean hasBlockDamage = (value == 1);
} else {
    // Player hasn't set preference, use default
    boolean hasBlockDamage = plugin.getConfigManager().isDefaultPlayerBlockDamageEnabled();
}

// Set player's preference
pdc.set(key, PersistentDataType.BYTE, (byte) 1); // Enable
pdc.set(key, PersistentDataType.BYTE, (byte) 0); // Disable
```

### Projectile Tracking

```java
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

// Check if projectile is from ThrowableFireballs
@EventHandler
public void onEntityExplode(EntityExplodeEvent event) {
    if (event.getEntity() instanceof Fireball) {
        Fireball fireball = (Fireball) event.getEntity();
        NamespacedKey key = new NamespacedKey(plugin, "thrown-fireball");
        
        if (fireball.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            // This is a thrown fireball!
            String playerUUID = fireball.getPersistentDataContainer()
                .get(key, PersistentDataType.STRING);
            
            Player thrower = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (thrower != null) {
                // Do something with thrower
            }
        }
    }
}
```

### Custom Protection Checks

```java
// Add your own protection check
@EventHandler(priority = EventPriority.HIGH)
public void onFireballThrow(FireballThrowEvent event) {
    Player player = event.getPlayer();
    Location loc = event.getLocation();
    
    // Your custom protection system
    if (myProtectionPlugin.isProtected(loc)) {
        event.setCancelled(true);
        player.sendMessage("§cThis area is protected!");
    }
}
```

### Integrate with Economy

```java
import net.milkbowl.vault.economy.Economy;

private Economy economy;

// Charge for fireball throws
@EventHandler
public void onFireballThrow(FireballThrowEvent event) {
    Player player = event.getPlayer();
    double cost = 10.0; // $10 per throw
    
    if (economy.getBalance(player) < cost) {
        event.setCancelled(true);
        player.sendMessage("§cYou need $" + cost + " to throw a fireball!");
        return;
    }
    
    economy.withdrawPlayer(player, cost);
    player.sendMessage("§a$" + cost + " charged for fireball throw!");
}
```

## Message API

```java
import dev.throwablefireballs.config.MessageManager;

MessageManager messages = plugin.getMessageManager();

// Get messages from messages.yml
String noPermission = messages.getNoPermission();
String cooldownMessage = messages.getCooldownMessage();

// Send to player
player.sendMessage(messages.getPrefix() + noPermission);

// Get with placeholders
String message = messages.getCooldownMessage()
    .replace("%seconds%", String.valueOf(seconds));
player.sendMessage(message);
```

## Utility API

```java
import dev.throwablefireballs.util.TextUtil;

// Color code support
String colored = TextUtil.colorize("&cRed &aGreen &9Blue");
player.sendMessage(colored);

// Strip colors
String stripped = TextUtil.stripColor(colored);
```

## Examples

### Example 1: Fireball Power-Up System

```java
public class FireballPowerUpPlugin extends JavaPlugin implements Listener {
    
    private ThrowableFireballsPlugin tfPlugin;
    private Set<UUID> poweredPlayers = new HashSet<>();
    
    @Override
    public void onEnable() {
        tfPlugin = (ThrowableFireballsPlugin) Bukkit.getPluginManager()
            .getPlugin("ThrowableFireballs");
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onFireballThrow(FireballThrowEvent event) {
        Player player = event.getPlayer();
        
        // Power up if player has buff
        if (poweredPlayers.contains(player.getUniqueId())) {
            Projectile projectile = event.getProjectile();
            projectile.setVelocity(projectile.getVelocity().multiply(3)); // 3x speed!
            player.sendMessage("§6SUPER FIREBALL!");
        }
    }
    
    public void givePowerUp(Player player) {
        poweredPlayers.add(player.getUniqueId());
        player.sendMessage("§aYou have fireball power-up for 30 seconds!");
        
        // Remove after 30 seconds
        Bukkit.getScheduler().runTaskLater(this, () -> {
            poweredPlayers.remove(player.getUniqueId());
            player.sendMessage("§cPower-up expired!");
        }, 600L); // 30 seconds = 600 ticks
    }
}
```

### Example 2: Fireball Statistics Tracker

```java
public class FireballStatsPlugin extends JavaPlugin implements Listener {
    
    private Map<UUID, Integer> throwCount = new HashMap<>();
    private Map<UUID, Integer> hitCount = new HashMap<>();
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onThrow(FireballThrowEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        throwCount.put(uuid, throwCount.getOrDefault(uuid, 0) + 1);
    }
    
    @EventHandler
    public void onExplosion(FireballExplosionEvent event) {
        if (event.getThrowerUUID() != null) {
            hitCount.put(event.getThrowerUUID(), 
                hitCount.getOrDefault(event.getThrowerUUID(), 0) + 1);
        }
    }
    
    public void showStats(Player player) {
        UUID uuid = player.getUniqueId();
        int throws = throwCount.getOrDefault(uuid, 0);
        int hits = hitCount.getOrDefault(uuid, 0);
        double accuracy = throws > 0 ? (hits * 100.0 / throws) : 0.0;
        
        player.sendMessage("§6Fireball Stats:");
        player.sendMessage("§7Throws: §e" + throws);
        player.sendMessage("§7Hits: §e" + hits);
        player.sendMessage("§7Accuracy: §e" + String.format("%.1f%%", accuracy));
    }
}
```

### Example 3: Custom Cooldown System

```java
public class CustomCooldownPlugin extends JavaPlugin implements Listener {
    
    private ThrowableFireballsPlugin tfPlugin;
    
    @Override
    public void onEnable() {
        tfPlugin = (ThrowableFireballsPlugin) Bukkit.getPluginManager()
            .getPlugin("ThrowableFireballs");
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFireballThrow(FireballThrowEvent event) {
        Player player = event.getPlayer();
        
        // VIP players get no cooldown
        if (player.hasPermission("myplugin.vip")) {
            tfPlugin.getCooldownManager().removeCooldown(player);
            return;
        }
        
        // Regular players get longer cooldown
        if (tfPlugin.getCooldownManager().hasCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou're on cooldown! Upgrade to VIP for no cooldowns!");
        } else {
            // Set 30 second cooldown instead of default
            Bukkit.getScheduler().runTaskLater(this, () -> {
                tfPlugin.getCooldownManager().removeCooldown(player);
            }, 600L); // 30 seconds
        }
    }
}
```

## API Reference

### ThrowableFireballsPlugin

Main plugin class:

| Method | Return | Description |
|--------|--------|-------------|
| `getConfigManager()` | `ConfigManager` | Get config manager |
| `getMessageManager()` | `MessageManager` | Get message manager |
| `getCooldownManager()` | `CooldownManager` | Get cooldown manager |
| `getFireballItemFactory()` | `FireballItemFactory` | Get item factory |
| `getVersionAdapter()` | `VersionAdapter` | Get version adapter |
| `shouldEnableBlockDamage(Player)` | `boolean` | Check player's block damage setting |
| `giveFireballItem(Player)` | `void` | Give fireball item to player |
| `reload()` | `void` | Reload configuration |

### ConfigManager

Configuration access:

| Method | Return | Description |
|--------|--------|-------------|
| `isEnableBlockDamage()` | `boolean` | Global block damage enabled |
| `isEnableEntityDamage()` | `boolean` | Global entity damage enabled |
| `getBlockPower()` | `double` | Block explosion power |
| `getEntityPower()` | `double` | Entity explosion power |
| `getCooldownSeconds()` | `int` | Cooldown duration |
| `isEnablePerPlayerChoice()` | `boolean` | Per-player toggle enabled |
| `isCheckWorldGuard()` | `boolean` | WorldGuard integration enabled |

### CooldownManager

Cooldown management:

| Method | Return | Description |
|--------|--------|-------------|
| `hasCooldown(Player)` | `boolean` | Check if player has cooldown |
| `getRemainingCooldown(Player)` | `long` | Get remaining seconds |
| `setCooldown(Player)` | `void` | Start cooldown for player |
| `removeCooldown(Player)` | `void` | Remove player's cooldown |

## FAQ

**Q: Is there JavaDoc documentation?**  
A: Not yet, but planned for v1.2.0. This guide covers all public API.

**Q: Can I modify fireball damage directly?**  
A: Use the explosion event and modify entity damage through Bukkit's EntityDamageEvent.

**Q: How do I make fireballs that don't break obsidian?**  
A: Listen to FireballExplosionEvent and remove obsidian blocks from the block list.

**Q: Can I create fireballs of different colors?**  
A: No, Minecraft's Fireball entity doesn't support colors. You'd need to use different projectiles.

**Q: Is there an async API?**  
A: No, all operations are sync. Bukkit doesn't allow async entity/world modifications.

## See Also

- [Project Architecture](project-architecture.md) - Code structure and design
- [Building from Source](building-from-source.md) - Compile with API access
- [Contributing Guide](../CONTRIBUTING.md) - Contribute to the API
- [Multi-Version Support](multi-version-support.md) - Version adapter details
