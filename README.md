# Conway's Game of Life

A Java implementation of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) with a Swing-based GUI renderer, featuring three grid implementations with progressively optimized update strategies.

## Requirements

- Java 8
- Maven 3.x

## Running

```bash
mvn compile exec:java -Dexec.mainClass="Main"
```

This opens a 1920×1080 window running a random initial configuration at 30 FPS.

## Running Tests

```bash
mvn test
```

## Architecture

### Grid Implementations

The project contains three grid implementations, each representing an optimization iteration:

| Class | Memory Model | Best For |
|-------|-------------|----------|
| `GridV1` | Sparse — stores only alive cells in a `ConcurrentHashMap` | Large grids with sparse populations |
| `GridV2` | Dense — all cells in a 2D array | Faster array-indexed neighbor lookups |
| `GridV3` | Dense + per-cell neighbour counters (`CellV2Adjacent`) | Maximum throughput via cached neighbour counts |

**Important:** `GridV3` is designed to work exclusively with `ClassicRulesetAdjacentAwareCells`. The ruleset owns initialization and update tracking for this grid. Using `ClassicRuleset` with `GridV3` produces a blank, frozen game.

### Ruleset Implementations

| Class | Strategy |
|-------|----------|
| `ClassicRuleset` | Recomputes alive-neighbour count by scanning 8 neighbours per candidate cell each tick |
| `ClassicRulesetAdjacentAwareCells` | Each cell maintains an `AtomicInteger` neighbour count. When a cell flips, its 8 neighbours' counters are updated (O(1) per evaluation). Designed for use with `GridV3`. |

### Configuration & Patterns

`GameConfigFactory` provides preset configurations:

| Method | Pattern |
|--------|---------|
| `simpleRandomConfig(probability)` | Random alive cells at given density — **default entry point** |
| `gliderConfig(probability)` | Random gliders seeded at given density |
| `pulsarConfig(probability)` | Random pulsar patterns |
| `simpleGospelGunConfig(probability)` | Gosper glider guns |

### Project Structure

```
src/main/java/
├── Main.java                        # Entry point
├── GameRunner.java                  # Game loop (render → update → display)
├── models/
│   ├── configs/                     # GameConfig, GameConfigFactory
│   ├── displays/                    # Swing renderer (GridDisplay, GridRenderer)
│   ├── generation/config/strategy/  # Pattern generators (Random, Glider, Pulsar, GospelGun)
│   ├── grid/                        # Grid implementations (V1, V2, V3), Cell, GridLocation
│   ├── interfaces/                  # IBoard, ICell, IBoardLocation, GameOfLifeRuleset
│   ├── patterns/                    # Glider, Pulsar, GospelGun pattern data
│   └── rulesets/                    # ClassicRuleset, ClassicRulesetAdjacentAwareCells
└── utils/                           # ColorUtils, TimeUtil

src/test/java/
├── integration/KnownPatternsTest    # Block still life, blinker oscillator, cross-ruleset parity
├── models/grid/GridV2Test           # Grid operations, adjacency, update tracking
└── models/rulesets/
    ├── ClassicRulesetTest           # All 9 Conway rule cases
    └── ClassicRulesetAdjacentAwareCellsTest  # V3 optimisation: counters, updateLocations
```

## Dependencies

- **Lombok** — reduces boilerplate (`@Builder`, `@Getter`, `@EqualsAndHashCode`)
- **SLF4J Simple** — logging
- **Guava** — `ImmutableList` for neighbour delta definitions
- **JUnit 5** — test framework (test scope)
