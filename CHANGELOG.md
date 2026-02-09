# Changelog

All notable changes to ThrowableFireballs will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-02-09

### Changed
- **Target Version**: Now targets Minecraft 1.21.x (Paper 1.21.4)
- **Java Requirement**: Upgraded to Java 21 (minimum: Java 21)
- **API Version**: Updated from 1.16 to 1.21 in plugin.yml
- **Backward Compatibility**: Still compatible with 1.16.5+ servers

### Technical Details
- Updated Paper API dependency from 1.20.4 to 1.21.4-SNAPSHOT
- Changed Java source/target compatibility from 17 to 21
- All features remain the same - no breaking changes to configuration or commands
- No code changes required due to version-agnostic design (no NMS)

### Notes
- If running on older Minecraft versions (1.16.5-1.20.x), use version 1.0.0
- If running on Minecraft 1.21+, use version 1.1.0 for optimal compatibility

## [1.0.0] - 2026-02-09

### Added
- Initial release
- Throwable fireballs with right-click interaction
- **Block damage vs entity damage control** - Protect terrain while allowing PvP damage
- Per-player block damage toggle (`/tfireballs blockdamage`)
- Comprehensive configuration system (item, projectile, explosion, cooldown settings)
- Multi-version support (Paper 1.16.5 - 1.20.x)
- Version adapter pattern for cross-version compatibility
- WorldGuard soft dependency for region protection
- Commands: `/tfireballs reload`, `/tfireballs give`, `/tfireballs blockdamage`
- Permission system with granular control
- Cooldown system with action bar messages
- Fully customizable messages with color code support
- PersistentDataContainer projectile tagging
- Custom item support (material, model data, display name)
- Fire and explosion effects configuration
- Direct hit damage configuration
- Tab completion for all commands

### Technical Features
- No NMS dependencies - pure Bukkit/Paper API
- Event-driven architecture for performance
- Adventure API integration for modern Paper servers
- Reflection-based fallback for legacy versions
- Java 17+ requirement
- Gradle build system
- Comprehensive documentation (README, QUICKSTART, config comments)

---

## Version Compatibility Matrix

| Plugin Version | Minecraft Versions | Java Version | Status |
|----------------|-------------------|--------------|--------|
| 1.1.0 | 1.16.5 - 1.21.x | Java 21+ | ✅ Current |
| 1.0.0 | 1.16.5 - 1.20.x | Java 17+ | ✅ Legacy Support |

**Recommendation**: Use the highest plugin version that matches your Java version capabilities.
