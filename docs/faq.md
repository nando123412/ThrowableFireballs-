# Frequently Asked Questions (FAQ)

Common questions and answers about ThrowableFireballs.

## General Questions

### Q: What Minecraft versions are supported?
**A:** Paper 1.16.5 through 1.21.x
- Use **v1.1.0** for MC 1.21.x (Java 21)
- Use **v1.0.0** for MC 1.16.5-1.20.x (Java 17)

### Q: Does it work on Spigot?
**A:** Partially. Core features work, but action bar messages fall back to chat. Paper is strongly recommended.

### Q: Does it work on Purpur?
**A:** Yes! Purpur is a Paper fork, so it's fully compatible.

### Q: Is Java 17 or 21 required?
**A:** 
- Version 1.1.0 requires Java 21
- Version 1.0.0 requires Java 17
- Choose based on your Minecraft version

---

## Functionality Questions

### Q: Can fireballs damage players but not break blocks?
**A:** Yes! This is the main feature. Set:
```yaml
explosion:
  block-damage: false
  entity-damage: true
```

### Q: Can players spam fireballs?
**A:** No, there's a cooldown system. Adjust in config:
```yaml
cooldown:
  time-millis: 1000  # 1 second
```

### Q: Can I use items other than fire charges?
**A:** Yes! Change the material in config:
```yaml
item:
  material: SNOWBALL  # Or any valid Material
```

### Q: Do fireballs work in WorldGuard regions?
**A:** By default, they respect region protection. Players need build permission or `throwablefireballs.bypassprotection`.

### Q: Can I disable the plugin in certain worlds?
**A:** Not currently built-in. Use a permissions plugin to deny `throwablefireballs.use` in specific worlds.

---

## Configuration Questions

### Q: How do I make explosions stronger?
**A:** Increase the power in config:
```yaml
explosion:
  power: 2.0  # Default is 1.0
```

### Q: How do I disable item consumption?
**A:** Set this in config:
```yaml
item:
  consume-on-throw: false
```

### Q: Can I make fireballs faster?
**A:** Yes, increase speed:
```yaml
projectile:
  speed: 2.5  # Default is 1.5
```

### Q: How do I prevent fire spread?
**A:** Disable fire creation:
```yaml
explosion:
  create-fire: false
```

### Q: Can I use custom item names?
**A:** Yes:
```yaml
item:
  display-name:
    enabled: true
    value: "&cMy Custom Fireball"
```

---

## Permission Questions

### Q: What's the minimum permission needed?
**A:** Just `throwablefireballs.use` (default: everyone has it)

### Q: How do I give VIPs no cooldown?
**A:** Grant them `throwablefireballs.bypasscooldown`

### Q: Can players toggle block damage themselves?
**A:** Yes, with `throwablefireballs.blockdamage.toggle` permission (default: true)

### Q: Do ops get all permissions?
**A:** Yes, ops have `throwablefireballs.*` by default.

---

## Technical Questions

### Q: Does it use NMS (Net Minecraft Server)?
**A:** No! It uses only Bukkit/Paper API for maximum compatibility.

### Q: Will it work on future Minecraft versions?
**A:** Very likely, since we don't use NMS. May need minor updates for major API changes.

### Q: Is it laggy?
**A:** No. It's event-driven with no tick loops. Very lightweight.

### Q: Can I use it with other fireball plugins?
**A:** Maybe. Depends if they conflict. Test in a dev environment first.

### Q: Does it support Folia (multithreaded Paper)?
**A:** Should work, but not officially tested. The code is event-driven and should be thread-safe.

---

## Troubleshooting Questions

### Q: Why don't action bars show?
**A:** You're probably on Spigot. Use Paper for action bars, or accept chat messages.

### Q: Why can't players throw in spawn?
**A:** Check WorldGuard protection. Either:
1. Allow `throwablefireballs.bypassprotection` permission, or
2. Set region flag to allow fireballs

### Q: Why does reload not work?
**A:** You need `throwablefireballs.admin` or `throwablefireballs.reload` permission.

### Q: Plugin won't load - "Unsupported API version"?
**A:** You're using the wrong version. Use v1.1.0 for MC 1.21+ or v1.0.0 for MC 1.16-1.20.

### Q: Config changes don't apply?
**A:** Run `/tfireballs reload` after editing config. Or restart server.

---

## Gameplay Questions

### Q: What happens in creative mode?
**A:** By default, items aren't consumed. Configurable with `consume-in-creative`.

### Q: Can villagers/mobs use fireballs?
**A:** No, only players can throw them.

### Q: Do fireballs damage the thrower?
**A:** Yes, if they're in the explosion radius (vanilla behavior).

### Q: Can you throw underwater?
**A:** Yes, you can throw the fireball, but it may behave oddly in water (vanilla fireball behavior).

### Q: What's the maximum explosion power?
**A:** Technically unlimited, but `10.0` is already massive. Vanilla TNT is `4.0`.

---

## Customization Questions

### Q: Can I use this with a resource pack?
**A:** Yes! Use custom model data:
```yaml
item:
  custom-model-data:
    enabled: true
    value: 1234  # Your custom model
```

### Q: Can I change all the messages?
**A:** Yes, all messages are in config with color code support.

### Q: Can I disable explosion sounds?
**A:** No, sounds are vanilla Minecraft behavior. Use a resource pack to mute them.

### Q: Can I make different fireballs for different ranks?
**A:** Not directly. You'd need multiple plugins or custom development.

---

## Performance Questions

### Q: How many fireballs can be thrown at once?
**A:** Hundreds+, but that depends on your server's resources and spawn limits.

### Q: Does it cause TPS lag?
**A:** Not normally. Each fireball is just a regular entity.

### Q: Should I worry about memory usage?
**A:** No, the plugin uses minimal memory (only cooldown data and config).

---

## WorldGuard Questions

### Q: Is WorldGuard required?
**A:** No, it's optional. Plugin works fine without it.

### Q: How do I allow fireballs in a region?
**A:** Currently, players need build permission in that region. Full WorldGuard flag support could be added in future.

### Q: Can I block fireballs globally but allow in arena?
**A:** Yes, use WorldGuard regions and deny `throwablefireballs.use` globally, then grant it per-region with permission flags.

---

## Comparison Questions

### Q: How is this different from other fireball plugins?
**A:** 
- **Block vs Entity damage** - Unique separation
- **Per-player settings** - Players can customize
- **Multi-version** - Works across many versions without NMS
- **Well documented** - Extensive docs and config comments

### Q: Should I use this or MoreExplosives?
**A:** Different purposes. ThrowableFireballs is focused on fireballs. MoreExplosives has many explosive types.

---

## Future Questions

### Q: Will you add more projectile types?
**A:** Maybe in future updates. Open an issue on GitHub to suggest features!

### Q: Will you add economy integration?
**A:** Not planned, but could be added in future if there's demand.

### Q: Will you add per-world configs?
**A:** Not currently planned. Use a permissions plugin for per-world control.

### Q: Can I contribute?
**A:** Yes! See [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

---

## Still Have Questions?

1. Check [Troubleshooting Guide](troubleshooting.md)
2. Search [GitHub Issues](https://github.com/nando123412/ThrowableFireballs-/issues)
3. Open a [new issue](https://github.com/nando123412/ThrowableFireballs-/issues/new)
4. Read the [full documentation](README.md)
