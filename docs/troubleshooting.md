# Troubleshooting Guide

Solutions to common problems with ThrowableFireballs.

## Installation Issues

### Plugin Won't Load

**Symptom:** No ThrowableFireballs messages in console

**Causes & Solutions:**

1. **Wrong Server Software**
   - Required: Paper 1.16.5+
   - Check: Look for "Paper" in console startup
   - Fix: Switch to Paper from PaperMC.io

2. **Wrong Java Version**
   - Required: Java 21 for v1.1.0, Java 17 for v1.0.0
   - Check: `java -version`
   - Fix: Update Java or use correct plugin version

3. **Corrupted JAR**
   - Check: File size (should be ~32KB)
   - Fix: Re-download the plugin

4. **Plugin Errors**
   - Check: Console for error messages
   - Fix: Copy error and create GitHub issue

### "Unsupported API Version" Error

**Symptom:** Console shows API version error

**Solution:**
- You're using the wrong plugin version!
- **v1.1.0** = Minecraft 1.21.x
- **v1.0.0** = Minecraft 1.16.5-1.20.x
- Download the correct version for your MC version

---

## Functionality Issues

### Players Can't Throw Fireballs

**Possible Causes:**

**1. No Permission**
- Check: Player has `throwablefireballs.use`
- Fix: `/lp user <player> permission set throwablefireballs.use true`

**2. Wrong Item**
- Check: Using fire charge (or configured material)
- Fix: Give item with `/tfireballs give <player> 1`

**3. On Cooldown**
- Check: Console shows cooldown message
- Fix: Wait or grant `throwablefireballs.bypasscooldown`

**4. Protected Region**
- Check: WorldGuard or spawn protection
- Fix: Grant `throwablefireballs.bypassprotection` or allow building

**5. Custom Item Requirements**
- Check: `config.yml` for custom-model-data or display-name
- Fix: Match exact requirements or disable custom item checks

### Fireballs Don't Break Blocks

**This is normal!** Default config protects blocks:
```yaml
explosion:
  block-damage: false  # Default
```

**To enable block damage:**
```yaml
explosion:
  block-damage: true
```

Then reload: `/tfireballs reload`

### Fireballs Don't Damage Players

**Check your config:**
```yaml
explosion:
  entity-damage: true  # Should be true
```

**Also check:**
- Players aren't in invincible mode
- No other plugins protecting them
- Explosion power isn't too low

### Action Bar Messages Don't Show

**Cause:** You're on Spigot, not Paper

**Solutions:**
1. **Recommended:** Switch to Paper
2. **Alternative:** Accept chat messages fallback
3. **Workaround:** Disable action bars:
   ```yaml
   cooldown:
     show-actionbar: false
   ```

---

## Command Issues

### /tfireballs Command Not Found

**Causes & Solutions:**

1. **Plugin Not Loaded**
   - Check: `/plugins` output
   - Fix: Ensure plugin loaded (green)

2. **Tab Complete Not Working**
   - Try: Full command `/tfireballs reload`
   - Check: Console for errors

### Reload Command Not Working

**1. No Permission**
- Need: `throwablefireballs.admin` or `throwablefireballs.reload`
- Fix: Grant permission

**2. Config Syntax Error**
- Check: YAML validator (yamllint.com)
- Fix: Correct spacing (spaces, not tabs)

**3. No Changes After Reload**
- Try: Full server restart instead
- Check: Correct config file edited

### Give Command Not Working

**1. Invalid Player Name**
- Check: Player is online
- Fix: Use exact in-game name

**2. Invalid Amount**
- Check: Positive number
- Fix: Use valid amount (1-64)

**3. Inventory Full**
- Check: Player has space
- Fix: Clear inventory space

---

## Configuration Issues

### Config Doesn't Save Changes

**Causes:**

1. **File Permissions**
   - Check: Server can write to plugins folder
   - Fix: Adjust file permissions (chmod)

2. **External Editor Issues**
   - Problem: Some editors don't save properly
   - Fix: Use nano, vim, or proper text editor

3. **Wrong File Edited**
   - Check: Editing `plugins/ThrowableFireballs/config.yml`
   - Not: `plugins/ThrowableFireballs.jar/config.yml`

### YAML Parse Error

**Common Mistakes:**

1. **Using Tabs**
   - Wrong: `<tab>setting: value`
   - Right: `  setting: value` (2 spaces)

2. **Wrong Indentation**
   - Each level: exactly 2 spaces
   - Use YAML validator

3. **Missing Colons**
   - Wrong: `setting value`
   - Right: `setting: value`

4. **Unquoted Special Characters**
   - Wrong: `message: "Hello:" world`
   - Right: `message: "Hello: world"`

---

## Performance Issues

### Server Lag When Using Fireballs

**Solutions:**

1. **Limit Explosion Power**
   ```yaml
   explosion:
     power: 1.0  # Lower if needed
   ```

2. **Increase Cooldown**
   ```yaml
   cooldown:
     time-millis: 2000  # Slower throw rate
   ```

3. **Disable Fire Spread**
   ```yaml
   explosion:
     create-fire: false
   ```

4. **Check Other Plugins**
   - Conflict with explosion plugins?
   - Disable and test

### Too Many Fireballs Spawned

**Solution:** Enforce cooldown
- Remove `bypasscooldown` permission
- Increase cooldown time
- Limit item availability

---

## WorldGuard Integration Issues

### WorldGuard Not Detected

**Check:**
1. WorldGuard is installed and enabled
2. WorldGuard version 7.0+
3. Console shows: "WorldGuard detected!"

**If not detected:**
- Ensure WorldGuard loads BEFORE ThrowableFireballs
- Check `plugins/` folder for WorldGuard JAR

### Can't Throw in Protected Regions

**This is intentional!**

**Solutions:**
1. Grant `throwablefireballs.bypassprotection` permission
2. Add build permission to that region
3. Disable protection check:
   ```yaml
   protection:
     respect-worldguard: false
   ```

---

## Version Compatibility Issues

### Features Don't Work on Older Versions

**Known Limitations:**

**On Spigot:**
- No action bars (chat fallback works)
- May have other cosmetic issues

**On 1.16.5:**
- Some Adventure API features limited
- Core functionality works fine

**Solution:** Use Paper for best experience

### Errors About Missing Classes

**Cause:** Version mismatch

**Solution:**
1. Check your Minecraft version
2. Download matching plugin version
3. Ensure Paper (not Bukkit)

---

## Permission Issues

### Player Has Permission But Can't Use

**Debug Steps:**

1. **Verify Permission Applied**
   ```
   /lp user <player> permission check throwablefireballs.use
   ```

2. **Check Inheritance**
   - Ensure group permissions work
   - No negative permissions

3. **Test as OP**
   ```
   /op <player>
   ```
   - If works: permission issue
   - If doesn't work: plugin issue

4. **Reload Permissions**
   ```
   /lp reloadcfg
   ```

---

## Other Issues

### Fireballs Go Through Blocks

**This is vanilla behavior.** Small fireballs can pass through some gaps.

**Workaround:**
```yaml
projectile:
  type: LARGE  # Larger hitbox
```

### Self-Damage from Fireballs

**This is intentional** (vanilla fireball behavior)

**Workaround:** Stand farther away when throwing

### Fire Spreads Too Much

**Solution:**
1. Disable fire:
   ```yaml
   explosion:
     create-fire: false
   ```

2. Or reduce duration:
   ```yaml
   explosion:
     fire-ticks: 20  # 1 second
   ```

3. Or use `gamerule`:
   ```
   /gamerule doFireTick false
   ```

---

## Getting More Help

### Information to Provide

When asking for help, include:

1. **Plugin Version** (`/version ThrowableFireballs`)
2. **Server Version** (`/version`)
3. **Java Version** (`java -version`)
4. **Config File** (paste to pastebin)
5. **Console Errors** (full stack trace)
6. **Other Plugins** (`/plugins`)
7. **Steps to Reproduce**

### Where to Get Help

1. **Read Documentation**
   - [FAQ](faq.md)
   - [Configuration Guide](configuration.md)
   - [Commands & Permissions](commands-permissions.md)

2. **Search Issues**
   - [GitHub Issues](https://github.com/nando123412/ThrowableFireballs-/issues)

3. **Report Bug**
   - [Create Issue](https://github.com/nando123412/ThrowableFireballs-/issues/new)

---

## Still Not Fixed?

1. Try on a **test server** with **no other plugins**
2. Test with **fresh config** (delete and regenerate)
3. Check for **plugin conflicts**
4. Create detailed **bug report** on GitHub

We're here to help! 🎯
