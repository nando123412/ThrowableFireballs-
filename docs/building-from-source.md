# Building from Source

Complete guide to compiling ThrowableFireballs yourself.

---

## Overview

Learn how to compile ThrowableFireballs from source code. This is useful for:

- **Development** - Making custom modifications
- **Testing** - Trying unreleased features
- **Learning** - Understanding how the plugin works
- **Contributing** - Submitting pull requests

## Requirements

### Software Requirements

**Required:**
- ✅ **Git** - Version control (2.0+)
- ✅ **Java JDK 21** - Development kit (not just JRE)
- ✅ **IDE** (optional but recommended):
  - IntelliJ IDEA (recommended)
  - Eclipse
  - VS Code with Java extensions

**Java 21 specifically required** - Plugin uses Paper 1.21.4 API.

### System Requirements

**Minimum:**
- 2 GB RAM
- 500 MB disk space
- Any OS (Windows, macOS, Linux)

**Recommended:**
- 4 GB RAM
- 1 GB disk space (for IDE + dependencies)
- SSD for faster builds

## Quick Start

### 1. Install Java 21

**Windows:**
1. Download OpenJDK 21 from [Adoptium](https://adoptium.net/)
2. Run installer
3. Verify installation:
   ```cmd
   java -version
   ```
   Should show: `openjdk version "21..."`

**macOS:**
```bash
brew install openjdk@21
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**Verify:**
```bash
java -version
javac -version
```

Both should show version 21.

### 2. Clone Repository

```bash
git clone https://github.com/nando123412/ThrowableFireballs-.git
cd ThrowableFireballs-
```

**Alternative (SSH):**
```bash
git clone git@github.com:nando123412/ThrowableFireballs-.git
```

### 3. Build Plugin

**Windows:**
```cmd
gradlew.bat clean build
```

**macOS/Linux:**
```bash
./gradlew clean build
```

### 4. Find Compiled JAR

```bash
ls -lh build/libs/
```

Output JAR: `build/libs/ThrowableFireballs-1.1.0.jar`

**That's it!** You've built the plugin. 🎉

## Detailed Build Process

### Understanding the Build

The build process:

1. **Clean** - Removes old build artifacts
2. **Compile** - Compiles Java source files
3. **Process Resources** - Copies `plugin.yml`, `config.yml`, etc.
4. **Package** - Creates JAR file
5. **Test** - Runs unit tests (if any)

### Build Commands

**Full clean build:**
```bash
./gradlew clean build
```

**Build without cleaning:**
```bash
./gradlew build
```

**Build without tests:**
```bash
./gradlew build -x test
```

**Show build info:**
```bash
./gradlew build --info
```

**Debug mode:**
```bash
./gradlew build --debug
```

### Build Output

**Successful build:**
```
BUILD SUCCESSFUL in 5s
5 actionable tasks: 5 executed
```

**Build artifacts:**
```
build/
├── classes/           # Compiled .class files
├── resources/         # Processed resources
├── libs/              # Final JAR
│   └── ThrowableFireballs-1.1.0.jar
└── tmp/               # Temporary build files
```

## IDE Setup

### IntelliJ IDEA (Recommended)

**Import project:**

1. **Open IntelliJ IDEA**
2. **File → Open**
3. **Select** `ThrowableFireballs-` folder
4. **Click** "Open as Gradle Project"
5. **Wait** for indexing to complete

**Configure JDK:**

1. **File → Project Structure**
2. **Project → Project SDK** → Select Java 21
3. **Project → Project language level** → 21
4. **Apply**

**Run build:**

1. **View → Tool Windows → Gradle**
2. **Tasks → build → build** (double-click)

**Hot tip:** Enable "Build Automatically" in Settings → Build → Compiler.

### Eclipse

**Import project:**

1. **File → Import**
2. **Gradle → Existing Gradle Project**
3. **Select** `ThrowableFireballs-` folder
4. **Finish**

**Configure JDK:**

1. **Window → Preferences**
2. **Java → Installed JREs** → Add JDK 21
3. **Project → Properties → Java Build Path** → Set JDK 21

### VS Code

**Open project:**

1. **File → Open Folder**
2. **Select** `ThrowableFireballs-` folder

**Install extensions:**
- Extension Pack for Java
- Gradle for Java

**Build:**
- **Terminal → gradle build** or
- **Gradle sidebar → build → build**

## Development Workflow

### Making Changes

1. **Create branch:**
   ```bash
   git checkout -b feature/my-feature
   ```

2. **Edit code** in `src/main/java/`

3. **Test locally:**
   ```bash
   ./gradlew build
   ```

4. **Copy to test server:**
   ```bash
   cp build/libs/ThrowableFireballs-1.1.0.jar ~/test-server/plugins/
   ```

5. **Run test server:**
   ```bash
   cd ~/test-server
   java -jar paper.jar
   ```

6. **Test in-game**

7. **Commit changes:**
   ```bash
   git add .
   git commit -m "feat: add awesome feature"
   ```

### Testing Changes

**Quick test cycle:**

```bash
# Build plugin
./gradlew build && \
# Copy to server
cp build/libs/ThrowableFireballs-1.1.0.jar ~/server/plugins/ && \
# Reload (if server running)
screen -S minecraft -X stuff "plugman reload ThrowableFireballs\n"
```

**Or use a script:**

```bash
#!/bin/bash
# build-and-deploy.sh

echo "Building plugin..."
./gradlew clean build

if [ $? -eq 0 ]; then
  echo "Build successful! Copying to server..."
  cp build/libs/ThrowableFireballs-1.1.0.jar ~/server/plugins/
  echo "Done! Restart or reload server."
else
  echo "Build failed!"
  exit 1
fi
```

## Customization

### Changing Version

**In `build.gradle`:**

```gradle
version = '1.2.0-SNAPSHOT'  // Change this
```

**In `src/main/resources/plugin.yml`:**

```yaml
version: 1.2.0-SNAPSHOT  # Change this too
```

### Changing Plugin Name

**In `src/main/resources/plugin.yml`:**

```yaml
name: CustomFireballs  # Change this
main: dev.throwablefireballs.ThrowableFireballsPlugin
```

**Build:**
```bash
./gradlew clean build
```

Output: `build/libs/CustomFireballs-1.2.0-SNAPSHOT.jar`

### Adding Dependencies

**In `build.gradle`:**

```gradle
dependencies {
    compileOnly 'io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT'
    
    // Add external dependency
    implementation 'com.example:library:1.0.0'
    
    // Or compile-only (provided by server)
    compileOnly 'com.github.MilkBowl:VaultAPI:1.7'
}
```

Then rebuild:
```bash
./gradlew clean build --refresh-dependencies
```

### Changing Java Version

**If you need to target Java 17 instead of 21:**

**In `build.gradle`:**

```gradle
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

**In `plugin.yml`:**

```yaml
api-version: 1.20  # Use 1.20 API instead of 1.21
```

**Rebuild:**
```bash
./gradlew clean build
```

## Troubleshooting

### Build Failures

**Issue: "Could not find or load main class Wrapper"**

**Solution:**
```bash
# Generate Gradle wrapper
gradle wrapper --gradle-version 8.5
./gradlew build
```

---

**Issue: "Unsupported class file major version 65"**

**Cause:** Java version mismatch

**Solution:**
```bash
# Check Java version
java -version  # Must be 21+

# If wrong, install Java 21 and set JAVA_HOME
export JAVA_HOME=/path/to/java-21
./gradlew build
```

---

**Issue: "Could not resolve all dependencies"**

**Solution:**
```bash
# Refresh dependencies
./gradlew build --refresh-dependencies

# Or clear caches
rm -rf ~/.gradle/caches/
./gradlew clean build
```

---

**Issue: "Permission denied: ./gradlew"**

**Solution (macOS/Linux):**
```bash
chmod +x gradlew
./gradlew build
```

### Runtime Errors

**Issue: Plugin doesn't load on server**

**Check:**
1. **Server version:** Must be Paper 1.16.5+
2. **Java version:** Server must run Java 17+ (21 for MC 1.21.4)
3. **Plugin file:** JAR file in `plugins/` folder
4. **Server logs:** Check `logs/latest.log` for errors

**Common error: "Unsupported API version"**

**Fix:**
```yaml
# In plugin.yml
api-version: 1.21  # Match your server version
```

### IDE Issues

**Issue: "Cannot resolve symbol 'Bukkit'"**

**Solution (IntelliJ):**
1. **File → Invalidate Caches → Invalidate and Restart**
2. **View → Tool Windows → Gradle → Reload All Gradle Projects**

**Issue: "Project SDK not defined"**

**Solution:**
1. **File → Project Structure**
2. **Project SDK → Add SDK → Download JDK → Version 21**

## Advanced Topics

### Creating Shaded JAR

If you add dependencies, you may need shading:

**In `build.gradle`:**

```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

shadowJar {
    archiveClassifier.set('')
    relocate 'com.example.library', 'dev.throwablefireballs.libs.library'
}

build.dependsOn shadowJar
```

**Build:**
```bash
./gradlew shadowJar
```

### Building for Multiple Versions

**Create version-specific modules:**

```
ThrowableFireballs/
├── common/          # Shared code
├── v1_16/           # 1.16-specific code
├── v1_21/           # 1.21-specific code
└── build.gradle     # Multi-module build
```

**In `build.gradle`:**

```gradle
subprojects {
    apply plugin: 'java'
    
    repositories {
        maven { url = 'https://repo.papermc.io/repository/maven-public/' }
    }
}
```

This is complex - see [Multi-Version Support](multi-version-support.md) for details.

### Running Tests

**Add test dependency:**

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}

test {
    useJUnitPlatform()
}
```

**Write test:**

```java
// src/test/java/ConfigManagerTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {
    @Test
    void testDefaultConfig() {
        // Test logic
        assertTrue(true);
    }
}
```

**Run tests:**
```bash
./gradlew test
```

## CI/CD Integration

### GitHub Actions

Create `.github/workflows/build.yml`:

```yaml
name: Build Plugin

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build with Gradle
      run: ./gradlew build
    
    - name: Upload artifact
      uses: actions/upload-artifact@v3
      with:
        name: ThrowableFireballs
        path: build/libs/*.jar
```

**Now each push auto-builds the plugin!**

## FAQ

**Q: Do I need to build from source to use the plugin?**  
A: No! Download pre-built JARs from [Releases](https://github.com/nando123412/ThrowableFireballs-/releases).

**Q: Can I build on Windows?**  
A: Yes! Use `gradlew.bat` instead of `./gradlew`.

**Q: Why Java 21 specifically?**  
A: Paper 1.21.4 API requires it. You can target older versions by changing `api-version`.

**Q: How do I contribute my changes?**  
A: See [Contributing Guide](../CONTRIBUTING.md).

**Q: Can I build without internet?**  
A: After first build, yes (Gradle caches dependencies). But first build needs internet.

**Q: How long does building take?**  
A: First build: ~30 seconds. Subsequent builds: ~5 seconds.

**Q: What if I modify only resources (config.yml)?**  
A: Still run `./gradlew build` to copy them to JAR.

## Resources

**Official:**
- [Paper API Docs](https://jd.papermc.io/)
- [Bukkit API Docs](https://hub.spigotmc.org/javadocs/bukkit/)
- [Gradle Docs](https://docs.gradle.org/)

**Community:**
- [SpigotMC Forums](https://www.spigotmc.org/)
- [Paper Discord](https://discord.gg/papermc)
- [r/admincraft](https://reddit.com/r/admincraft)

## See Also

- [Project Architecture](project-architecture.md) - Code structure explained
- [Contributing Guide](../CONTRIBUTING.md) - How to contribute
- [API Documentation](api-documentation.md) - Using the plugin API
- [Multi-Version Support](multi-version-support.md) - Cross-version compilation
