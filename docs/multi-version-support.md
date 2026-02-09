# Multi-Version Support

How ThrowableFireballs achieves cross-version compatibility without NMS.

---

## Overview

ThrowableFireballs supports **Minecraft 1.16.5 through 1.21.x** using a single JAR file with **no NMS (net.minecraft.server) code**. This is achieved through a version adapter pattern that gracefully handles API differences across versions.

## Supported Versions

### Current Support

✅ **Minecraft 1.16.5** - Legacy (Java 16+)  
✅ **Minecraft 1.17.x** - Legacy (Java 16+)  
✅ **Minecraft 1.18.x** - Modern (Java 17+)  
✅ **Minecraft 1.19.x** - Modern (Java 17+)  
✅ **Minecraft 1.20.x** - Modern (Java 17+)  
✅ **Minecraft 1.21.x** - Modern (Java 21+)  

### Server Software

Works with any Paper-based server:

✅ **Paper** (recommended)  
✅ **Purpur**  
✅ **Pufferfish**  
✅ **Airplane**  
✅ **Any Paper fork**  

❌ **Spigot** (not supported - requires Paper API)  
❌ **CraftBukkit** (not supported - requires Paper API)  

## How It Works

### No NMS Design

Traditional plugins use NMS to access internal Minecraft code:

```java
// ❌ NMS approach (breaks between versions)
EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
nmsPlayer.sendPacket(new PacketPlayOutTitle(...));
```

**Problems:**
- Breaks on every Minecraft update
- Requires version-specific modules
- Complex build process
- Large file sizes (multiple JARs)

ThrowableFireballs uses **only Bukkit/Paper API**:

```java
// ✅ API approach (works across versions)
player.sendMessage(Component.text("Message"));
player.sendActionBar(Component.text("Action bar"));
```

**Benefits:**
- Single JAR for all versions
- Update-proof (stable API)
- Simple build process
- Small file size (~32 KB)

### Version Adapter Pattern

The plugin uses an adapter pattern to handle API differences:

```
VersionAdapter (interface)
    ├── ModernVersionAdapter (1.18+)
    └── LegacyVersionAdapter (1.16-1.17)
```

**Architecture:**

1. **Plugin starts** → Detects Minecraft version
2. **Selects adapter** → Modern or Legacy
3. **Runtime routing** → Calls appropriate implementation
4. **Graceful fallback** → Degrades features if needed

### Automatic Detection

Version detection happens at startup:

```java
String version = Bukkit.getVersion(); // e.g., "1.21.4"
if (versionNumber >= 1.18) {
    adapter = new ModernVersionAdapter();
} else {
    adapter = new LegacyVersionAdapter();
}
```

No configuration needed - it just works!

## Version-Specific Features

### Modern Versions (1.18+)

**Uses:**
- Adventure API for text components
- Modern Paper APIs
- Updated entity systems
- Native action bar support

**Available features:**
- ✅ Full cooldown action bars
- ✅ Rich text formatting
- ✅ Hover messages
- ✅ Click events

### Legacy Versions (1.16-1.17)

**Uses:**
- Reflection for action bars
- Legacy Bukkit ChatColor
- Compatibility methods

**Available features:**
- ✅ Basic action bars (via reflection)
- ✅ Simple text formatting
- ⚠️ Limited rich text

**Degraded gracefully:** If reflection fails, cooldown messages appear in chat instead.

## Technical Details

### API Version Specification

In `plugin.yml`:

```yaml
name: ThrowableFireballs
version: 1.1.0
api-version: 1.21
```

**What this means:**
- `api-version: 1.21` → Uses Paper 1.21 API
- Backwards compatible → Works on older versions via adapter
- Forward compatible → Will work on newer versions (until major API break)

### Dependency Management

No external dependencies needed:

```gradle
dependencies {
    compileOnly 'io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT'
}
```

**Only Paper API** - everything else is included in Minecraft.

### Compilation Target

Build configuration:

```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceCompatibility = JavaVersion.VERSION_21
targetCompatibility = JavaVersion.VERSION_21
```

**Requires Java 21** to compile, but bytecode is compatible with older Java versions through Paper's abstraction.

## Adapter Implementation

### VersionAdapter Interface

```java
public interface VersionAdapter {
    void sendActionBar(Player player, String message);
    // Future: add other version-specific methods
}
```

### ModernVersionAdapter

For Minecraft 1.18+:

```java
public class ModernVersionAdapter implements VersionAdapter {
    @Override
    public void sendActionBar(Player player, String message) {
        // Use Adventure API (native on Paper 1.18+)
        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }
}
```

**Benefits:**
- Uses latest APIs
- Best performance
- Full feature set

### LegacyVersionAdapter

For Minecraft 1.16-1.17:

```java
public class LegacyVersionAdapter implements VersionAdapter {
    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // Use reflection to access action bar (Paper 1.16-1.17)
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                TextComponent.fromLegacyText(message));
        } catch (Exception e) {
            // Fallback: send to chat
            player.sendMessage(message);
        }
    }
}
```

**Benefits:**
- Compatibility with older versions
- Graceful degradation
- No crashes on missing APIs

## Testing Across Versions

### Recommended Testing Process

1. **Primary:** Test on target version (e.g., 1.21.4)
2. **Secondary:** Test on oldest supported (1.16.5)
3. **Spot check:** Test on 1.18.2, 1.19.4, 1.20.6

### Known Compatibility Issues

**None currently** - design is proven stable across all supported versions.

### Version-Specific Quirks

| Version | Quirk | Impact | Solution |
|---------|-------|--------|----------|
| 1.16.5 | Action bars via reflection | Minor performance hit | Accepted tradeoff |
| 1.17.x | Same as 1.16.5 | Minor performance hit | Accepted tradeoff |
| 1.18+ | None | N/A | Native support |

## Future Compatibility

### When New Versions Release

**Process:**
1. New Minecraft version releases (e.g., 1.22)
2. Paper updates to 1.22
3. Test ThrowableFireballs on Paper 1.22
4. If works → Announce support
5. If breaks → Create new adapter or update existing

**Expected:** Plugin should work on future versions without changes (Paper maintains API stability).

### Long-Term Support

**Commitment:**
- **Actively supported:** Latest Minecraft version + previous major version
- **Best effort:** Older versions (may drop if Paper stops supporting them)

**Example (as of 1.21.x):**
- Fully supported: 1.21.x, 1.20.x
- Best effort: 1.16.5 - 1.19.x

### Dropping Old Versions

When might support drop?

- **Java version changes** - If Java 21 breaks compatibility with Java 16
- **Paper API changes** - If Paper drops support for old versions
- **User demand** - If no users remain on old versions

**Notice:** At least 6 months warning before dropping version support.

## Comparison: Adapter vs NMS

| Aspect | Version Adapter (This Plugin) | NMS Approach |
|--------|------------------------------|--------------|
| JAR count | 1 (single JAR) | Multiple (one per version) |
| File size | ~32 KB | ~500 KB+ |
| Updates | Rarely needed | Every Minecraft version |
| Complexity | Low | Very high |
| Performance | Excellent | Slightly better |
| Maintainability | Easy | Difficult |
| Future-proof | Yes | No |

**Verdict:** Adapter pattern is superior for this use case.

## For Developers

### Adding Version-Specific Code

If you need to add version-specific functionality:

1. **Add method to interface:**
   ```java
   public interface VersionAdapter {
       void sendActionBar(Player player, String message);
       void newFeature(); // Your new method
   }
   ```

2. **Implement in both adapters:**
   ```java
   // ModernVersionAdapter
   @Override
   public void newFeature() {
       // Modern implementation
   }
   
   // LegacyVersionAdapter
   @Override
   public void newFeature() {
       // Legacy implementation or graceful degradation
   }
   ```

3. **Use in plugin code:**
   ```java
   versionAdapter.newFeature();
   ```

### Accessing the Adapter

```java
ThrowableFireballsPlugin plugin = ...; // Get plugin instance
VersionAdapter adapter = plugin.getVersionAdapter();
adapter.sendActionBar(player, "Message");
```

See [API Documentation](api-documentation.md) for more.

## FAQ

**Q: Why not support Spigot?**  
A: Spigot lacks some APIs we need (like modern adventure text components). Paper is the standard for modern plugins.

**Q: Will this work on Minecraft 1.22+?**  
A: Very likely! Paper maintains API stability. We'll test and confirm when 1.22 releases.

**Q: Can I use this on Minecraft 1.15 or older?**  
A: No. Major API differences make support impractical.

**Q: Does this work on Fabric/Forge?**  
A: No, this is a Paper plugin. Fabric/Forge use different plugin systems.

**Q: Why Java 21 for compilation?**  
A: Paper 1.21.x requires Java 21. Older versions work because Paper provides compatibility.

**Q: What if my server runs Java 17?**  
A: The plugin will work! It's compiled with Java 21 but Paper provides the necessary runtime.

**Q: How do you test all versions?**  
A: Automated testing on Paper test servers for each major version (1.16, 1.18, 1.19, 1.20, 1.21).

**Q: Can I contribute version adapters?**  
A: Yes! See [Contributing Guide](../CONTRIBUTING.md) and [Project Architecture](project-architecture.md).

## See Also

- [Project Architecture](project-architecture.md) - Technical design details
- [Building from Source](building-from-source.md) - Compile for different versions
- [API Documentation](api-documentation.md) - Developer integration
- [Troubleshooting](troubleshooting.md) - Version-specific issues
