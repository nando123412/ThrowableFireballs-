# Contributing to ThrowableFireballs

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

## How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates.

**Good bug reports include:**
- Clear, descriptive title
- Exact steps to reproduce
- Expected vs actual behavior
- Minecraft & plugin version
- Server software (Paper version)
- Config files (if relevant)
- Console errors/logs
- Screenshots (if applicable)

**Bug Report Template:**
```markdown
**Description:**
Brief description of the bug

**Steps to Reproduce:**
1. Step one
2. Step two
3. ...

**Expected Behavior:**
What should happen

**Actual Behavior:**
What actually happens

**Environment:**
- Plugin Version: X.X.X
- Minecraft Version: 1.XX.X
- Server Software: Paper build XXXX
- Java Version: XX

**Additional Context:**
Any other information (logs, screenshots, etc.)
```

### ✨ Suggesting Features

Feature requests are welcome! Please check existing suggestions first.

**Good feature requests include:**
- Clear description of the feature
- Use case / why it's needed
- Example of how it would work
- Mockups or examples (if applicable)

### 📝 Pull Requests

We love pull requests! Here's how to contribute code:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Commit with clear messages**
   ```bash
   git commit -m "Add amazing feature"
   ```
6. **Push to your fork**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

## Development Setup

### Prerequisites

- **Java 21 JDK** (or Java 17 for older versions)
- **Git**
- **Gradle** (wrapper included)
- **IDE** (IntelliJ IDEA recommended)

### Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/nando123412/ThrowableFireballs-.git
   cd ThrowableFireballs-
   ```

2. **Import into IDE**
   - IntelliJ: File → Open → Select folder
   - Auto-imports Gradle project

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run tests** (when available)
   ```bash
   ./gradlew test
   ```

### Project Structure

```
src/main/java/com/nandoothjuuh/throwablefireballs/
├── ThrowableFireballsPlugin.java    # Main plugin class
├── commands/                         # Command handlers
├── compat/                           # Version adapters
├── config/                           # Configuration management
├── cooldown/                         # Cooldown system
├── items/                            # Item factory
├── listeners/                        # Event listeners
└── util/                             # Utilities

src/main/resources/
├── plugin.yml                        # Plugin metadata
└── config.yml                        # Default configuration
```

## Coding Standards

### Java Style

- **Indentation:** 4 spaces (no tabs)
- **Line length:** 120 characters max
- **Braces:** K&R style
- **Naming:**
  - Classes: `PascalCase`
  - Methods: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Variables: `camelCase`

### Example

```java
public class ExampleClass {
    
    private static final String EXAMPLE_CONSTANT = "value";
    private final PluginManager manager;
    
    public ExampleClass(PluginManager manager) {
        this.manager = manager;
    }
    
    public void exampleMethod(Player player) {
        if (player == null) {
            return;
        }
        
        // Implementation
    }
}
```

### Best Practices

1. **Null Safety**
   - Always check for null
   - Use `@Nullable` annotations where appropriate

2. **Documentation**
   - Add JavaDoc for public methods
   - Comment complex logic
   - Keep comments up-to-date

3. **Error Handling**
   - Catch specific exceptions
   - Log errors appropriately
   - Fail gracefully

4. **Performance**
   - Avoid unnecessary object creation
   - Cache when appropriate
   - Use efficient data structures

5. **Backward Compatibility**
   - Don't break existing configs
   - Don't remove public APIs
   - Deprecate before removing

### Code Review Checklist

Before submitting a PR, ensure:

- [ ] Code compiles without errors
- [ ] No new warnings introduced
- [ ] Follows project style guide
- [ ] Added/updated JavaDoc
- [ ] Tested on Paper server
- [ ] Config changes documented
- [ ] README updated (if needed)
- [ ] No hardcoded values (use config)
- [ ] Proper error handling
- [ ] Null checks added

## Commit Message Guidelines

### Format

```
type(scope): brief description

Longer description if needed

Fixes #123
```

### Types

- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation only
- `style` - Code style changes
- `refactor` - Code refactoring
- `perf` - Performance improvement
- `test` - Adding tests
- `chore` - Maintenance tasks

### Examples

```
feat(commands): add /tfireballs info command

Added new command to show plugin info and stats.
Includes player count and config summary.

Closes #45
```

```
fix(explosion): prevent null player ID in damage events

Fixed crash when non-player entities trigger explosions.
Added null check for player UUID lookup.

Fixes #78
```

## Testing

### Manual Testing

Test your changes on a real Paper server:

1. Build the plugin
2. Copy JAR to test server
3. Restart server
4. Test all affected features
5. Check console for errors
6. Test edge cases

### Test Checklist

- [ ] Basic throw functionality
- [ ] Command execution
- [ ] Permission checks
- [ ] Config reload
- [ ] Cooldown system
- [ ] Block/entity damage
- [ ] WorldGuard integration (if installed)
- [ ] Multi-version compatibility

## Documentation

### When to Update Docs

Update documentation when you:
- Add/change features
- Modify config options
- Add/change commands
- Add/change permissions
- Fix significant bugs

### Documentation Files

- `README.md` - Main documentation
- `docs/*.md` - Detailed guides
- `config.yml` - Inline comments
- `CHANGELOG.md` - Version history

## Community Guidelines

### Be Respectful

- Be kind and courteous
- Respect different opinions
- Provide constructive feedback
- Help others learn

### Communication

- Use clear, concise language
- Provide context and examples
- Be patient with responses
- Follow up on your issues/PRs

## License

By contributing, you agree that your contributions will be licensed under the same terms as the project.

## Questions?

- Check [FAQ](docs/faq.md)
- Open a [Discussion](https://github.com/nando123412/ThrowableFireballs-/discussions)
- Contact maintainers

## Attribution

Contributors are credited in:
- GitHub Contributors page
- CHANGELOG.md
- Release notes

Thank you for contributing to ThrowableFireballs! 🎉
