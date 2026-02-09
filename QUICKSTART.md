# Quick Start Guide - ThrowableFireballs

## For Server Administrators

### Installation
1. Download `ThrowableFireballs-1.1.0.jar` from the releases page or build it yourself
2. Place the JAR in your Paper server's `plugins/` folder
3. Start or restart your server
4. The plugin will generate `plugins/ThrowableFireballs/config.yml`
5. Configure as needed and run `/tfireballs reload`

### First Steps
1. Give yourself a fireball: `/tfireballs give <your name> 64`
2. Right-click with the fire charge to throw!
3. Configure in `config.yml`:
   - Set `explosion.block-damage: false` to protect terrain
   - Adjust `cooldown.time-millis` for desired cooldown
   - Customize messages with color codes

### Key Config Options
```yaml
explosion:
  block-damage: false     # IMPORTANT: Set to false to protect builds
  entity-damage: true     # Keep true to allow PvP damage
  power: 1.0              # Explosion strength
```

### Permissions
- Everyone: `throwablefireballs.use` (default: true)
- Admins: `throwablefireballs.admin` for commands

## For Developers

### Building from Source
```bash
git clone <repository-url>
cd ThrowableFireballs-
./gradlew build
# JAR will be in build/libs/ThrowableFireballs-1.1.0.jar
```

### Requirements
- Java 21+ (Java 17 minimum)
- Gradle 7.0+
- Paper API 1.21.x

### Project Structure
```
src/main/java/com/nandoothjuuh/throwablefireballs/
├── ThrowableFireballsPlugin.java    # Main class
├── commands/                         # Command handlers
├── compat/                           # Version adapters
├── config/                           # Config management
├── cooldown/                         # Cooldown system
├── items/                            # Item creation
├── listeners/                        # Event handlers
└── util/                             # Utilities
```

### Adding Features
1. Add config options in `ConfigManager.java`
2. Add messages in `MessageManager.java`
3. Update `config.yml` with defaults
4. Implement feature in appropriate listener or command
5. Test on Paper 1.16.5 and latest version

### Version Support
The plugin uses a version adapter pattern to support multiple Paper versions:
- `VersionAdapter` interface for version-specific operations
- `ModernVersionAdapter` for Paper 1.16.5+ (Adventure API)
- `LegacyVersionAdapter` for fallback (reflection)

### Testing
1. Build: `./gradlew build`
2. Copy JAR to test server plugins folder
3. Restart server
4. Test all commands and features
5. Check console for errors

**Recommended Test Versions**: Paper 1.21.4, 1.20.4, 1.16.5

## Troubleshooting

### Build Issues
**Problem**: "Cannot find Java 17"
**Solution**: Install JDK 17+ or use `JAVA_HOME` environment variable

**Problem**: "Build failed"
**Solution**: Run `./gradlew clean build` to do a fresh build

### Runtime Issues
**Problem**: "Plugin doesn't load"
**Solution**: Check server is Paper 1.16.5+, check logs for errors

**Problem**: "Action bars don't show"
**Solution**: Make sure you're on Paper (not Spigot), or messages will fall back to chat

## Support
- Report issues on GitHub
- Read full documentation in README.md
- Check config.yml comments for detailed options
