# Tic Tac Tae — Android Game Development Specification

## 1. Project Overview

**Game Name:** Tic Tac Tae  
**Genre:** Casual / Strategy / Board Game  
**Platform:** Android  
**Orientation:** Portrait by default  
**Offline:** Yes  
**Primary Theme:** Futuristic Neon X vs O

Tic Tac Tae is a polished neon-themed Tic-Tac-Toe game supporting:

- Human vs Computer
- Human vs Human
- 3×3 board
- 6×6 board
- 9×9 board
- 11×11 board
- Multiple AI difficulty levels
- Neon animations and effects
- Local statistics
- Sound and haptic feedback
- Offline gameplay

The architecture must be designed so future features such as online multiplayer, tournaments, achievements, and additional board sizes can be added without rewriting the core game engine.

---

# 2. Product Goals

The game should feel like a futuristic arcade game rather than a basic Tic-Tac-Toe utility.

Core visual identity:

> Dark background + glowing cyan X + glowing magenta O + neon grid + smooth animations.

Primary goals:

1. Start a game within a few taps.
2. Make 3×3 immediately familiar.
3. Make larger boards strategic.
4. Make AI responsive and challenging.
5. Keep gameplay fully offline.
6. Keep UI smooth on lower-end Android devices.
7. Separate game logic from UI.
8. Make the game easy to extend.

---

# 3. Game Modes

## 3.1 Human vs Human

Two players share the same device.

- Player 1 = X
- Player 2 = O
- Players alternate turns.
- No AI is used.

Display:

```text
X'S TURN
```

or:

```text
O'S TURN
```

---

## 3.2 Human vs Computer

One player competes against an AI.

The user can select:

- Human = X / AI = O
- Human = O / AI = X

If the user selects O, the AI starts.

Difficulty:

- Easy
- Medium
- Hard

---

# 4. Board Configurations

Use a generic board engine.

| Mode | Size | Win Length |
|---|---:|---:|
| Classic | 3×3 | 3 |
| Mega | 6×6 | 4 |
| Pro | 9×9 | 5 |
| Ultimate | 11×11 | 5 |

Do not create separate game engines for each board size.

Use:

```kotlin
data class BoardConfig(
    val size: Int,
    val winLength: Int
)
```

Predefined configurations:

```kotlin
object BoardConfigs {

    val CLASSIC = BoardConfig(3, 3)
    val MEGA = BoardConfig(6, 4)
    val PRO = BoardConfig(9, 5)
    val ULTIMATE = BoardConfig(11, 5)

    val all = listOf(
        CLASSIC,
        MEGA,
        PRO,
        ULTIMATE
    )
}
```

---

# 5. Winning Rules

## 5.1 3×3

Three consecutive X or O symbols win.

Directions:

- Horizontal
- Vertical
- Diagonal
- Reverse diagonal

---

## 5.2 6×6

Four consecutive symbols win.

Example:

```text
X X X X
```

---

## 5.3 9×9

Five consecutive symbols win.

Example:

```text
O O O O O
```

---

## 5.4 11×11

Five consecutive symbols win.

This prevents the game from requiring an entire 11-cell row.

---

# 6. Winning Sequence Rules

A winning sequence may be part of a longer sequence.

Example for 11×11:

```text
X X X X X X
```

The player has a winning sequence because at least five consecutive X symbols exist.

The `WinChecker` must return the actual winning cells that should be highlighted.

---

# 7. Android Technology

Use:

```text
Language: Kotlin
UI: Jetpack Compose
Architecture: MVVM
Build: Gradle Kotlin DSL
Minimum SDK: 26
Target SDK: Latest stable Android SDK
JVM: 17
```

Use current stable versions of AndroidX and Compose dependencies when creating the project.

Suggested dependencies:

```kotlin
dependencies {
    implementation(platform("androidx.compose:compose-bom:<latest>"))

    implementation("androidx.activity:activity-compose:<latest>")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:<latest>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<latest>")

    implementation("androidx.datastore:datastore-preferences:<latest>")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
```

Do not copy old dependency versions from this document. Use compatible current stable versions.

---

# 8. Package Structure

Recommended structure:

```text
com.tictactae.app
│
├── MainActivity.kt
│
├── data
│   ├── PreferencesRepository.kt
│   ├── StatisticsRepository.kt
│   └── GamePreferences.kt
│
├── domain
│   ├── model
│   │   ├── BoardConfig.kt
│   │   ├── Board.kt
│   │   ├── Cell.kt
│   │   ├── Difficulty.kt
│   │   ├── GameMode.kt
│   │   ├── GameResult.kt
│   │   ├── GameSetupState.kt
│   │   ├── GameUiState.kt
│   │   ├── Move.kt
│   │   └── Player.kt
│   │
│   ├── engine
│   │   ├── GameEngine.kt
│   │   ├── MoveValidator.kt
│   │   └── WinChecker.kt
│   │
│   └── ai
│       ├── AiEngine.kt
│       ├── EasyAi.kt
│       ├── MediumAi.kt
│       ├── HardAi3x3.kt
│       ├── HeuristicAi.kt
│       ├── BoardEvaluator.kt
│       └── CandidateGenerator.kt
│
├── ui
│   ├── navigation
│   │   └── AppNavigation.kt
│   │
│   ├── screens
│   │   ├── MainMenuScreen.kt
│   │   ├── SetupScreen.kt
│   │   ├── GameScreen.kt
│   │   ├── ResultScreen.kt
│   │   ├── SettingsScreen.kt
│   │   ├── StatsScreen.kt
│   │   └── HowToPlayScreen.kt
│   │
│   ├── components
│   │   ├── NeonBackground.kt
│   │   ├── NeonButton.kt
│   │   ├── NeonBoard.kt
│   │   ├── NeonCell.kt
│   │   ├── NeonX.kt
│   │   ├── NeonO.kt
│   │   ├── WinningLine.kt
│   │   └── TurnIndicator.kt
│   │
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── util
    ├── Constants.kt
    ├── SoundManager.kt
    └── HapticManager.kt
```

---

# 9. Core Models

## 9.1 Player

```kotlin
enum class Player {
    X,
    O;

    fun opponent(): Player {
        return if (this == X) O else X
    }
}
```

---

## 9.2 Cell

```kotlin
enum class Cell {
    EMPTY,
    X,
    O
}
```

Conversion:

```kotlin
fun Player.toCell(): Cell {
    return when (this) {
        Player.X -> Cell.X
        Player.O -> Cell.O
    }
}
```

---

## 9.3 Game Mode

```kotlin
enum class GameMode {
    HUMAN_VS_HUMAN,
    HUMAN_VS_COMPUTER
}
```

---

## 9.4 Difficulty

```kotlin
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}
```

---

## 9.5 Move

```kotlin
data class Move(
    val row: Int,
    val column: Int,
    val player: Player
)
```

---

# 10. Board Implementation

Use a one-dimensional array for efficient access.

```kotlin
class Board(
    val size: Int
) {
    private val cells = Array(size * size) {
        Cell.EMPTY
    }

    fun get(row: Int, column: Int): Cell {
        return cells[row * size + column]
    }

    fun set(
        row: Int,
        column: Int,
        cell: Cell
    ) {
        cells[row * size + column] = cell
    }

    fun isEmpty(
        row: Int,
        column: Int
    ): Boolean {
        return get(row, column) == Cell.EMPTY
    }

    fun isFull(): Boolean {
        return cells.none { it == Cell.EMPTY }
    }

    fun copy(): Board {
        val result = Board(size)

        for (row in 0 until size) {
            for (column in 0 until size) {
                result.set(
                    row,
                    column,
                    get(row, column)
                )
            }
        }

        return result
    }
}
```

For AI performance, an optimized `copy()` or internal array-copy implementation may be used.

---

# 11. Game Result

```kotlin
sealed interface GameResult {

    data object InProgress : GameResult

    data class Winner(
        val player: Player
    ) : GameResult

    data object Draw : GameResult
}
```

---

# 12. Game UI State

Keep the UI state immutable.

```kotlin
data class GameUiState(
    val board: Board,
    val currentPlayer: Player,
    val gameMode: GameMode,
    val difficulty: Difficulty,
    val boardConfig: BoardConfig,
    val humanPlayer: Player,
    val result: GameResult = GameResult.InProgress,
    val winningCells: List<Pair<Int, Int>> = emptyList(),
    val isPaused: Boolean = false,
    val isAiThinking: Boolean = false
)
```

The ViewModel exposes:

```kotlin
val uiState: StateFlow<GameUiState>
```

using:

```kotlin
MutableStateFlow
```

internally.

---

# 13. Game Engine

The game engine must not depend on Android or Compose.

```kotlin
class GameEngine(
    private val config: BoardConfig
) {

    private val board = Board(config.size)

    var currentPlayer: Player = Player.X
        private set

    fun makeMove(
        row: Int,
        column: Int
    ): MoveResult {

        if (!board.isEmpty(row, column)) {
            return MoveResult.Invalid
        }

        board.set(
            row,
            column,
            currentPlayer.toCell()
        )

        val winningCells =
            WinChecker.findWinningCells(
                board = board,
                row = row,
                column = column,
                player = currentPlayer,
                winLength = config.winLength
            )

        if (winningCells.isNotEmpty()) {
            return MoveResult.Win(
                player = currentPlayer,
                cells = winningCells
            )
        }

        if (board.isFull()) {
            return MoveResult.Draw
        }

        currentPlayer = currentPlayer.opponent()

        return MoveResult.Continue(
            nextPlayer = currentPlayer
        )
    }

    fun getBoard(): Board {
        return board
    }
}
```

---

# 14. Move Result

```kotlin
sealed interface MoveResult {

    data object Invalid : MoveResult

    data class Continue(
        val nextPlayer: Player
    ) : MoveResult

    data class Win(
        val player: Player,
        val cells: List<Pair<Int, Int>>
    ) : MoveResult

    data object Draw : MoveResult
}
```

---

# 15. Win Detection

Only inspect lines passing through the newly placed cell.

Directions:

```text
Horizontal:      0,  1
Vertical:        1,  0
Diagonal:        1,  1
Reverse diagonal:1, -1
```

Implementation:

```kotlin
object WinChecker {

    private val directions = listOf(
        0 to 1,
        1 to 0,
        1 to 1,
        1 to -1
    )

    fun findWinningCells(
        board: Board,
        row: Int,
        column: Int,
        player: Player,
        winLength: Int
    ): List<Pair<Int, Int>> {

        val target = player.toCell()

        for ((dr, dc) in directions) {

            val positive = collectDirection(
                board,
                row,
                column,
                dr,
                dc,
                target
            )

            val negative = collectDirection(
                board,
                row,
                column,
                -dr,
                -dc,
                target
            )

            val completeLine =
                negative.reversed() +
                listOf(row to column) +
                positive

            if (completeLine.size >= winLength) {
                val center =
                    completeLine.indexOfFirst {
                        it.first == row &&
                        it.second == column
                    }

                val start =
                    (center - winLength + 1)
                        .coerceAtLeast(0)

                val end =
                    (start + winLength)
                        .coerceAtMost(
                            completeLine.size
                        )

                if (end - start >= winLength) {
                    return completeLine.subList(
                        start,
                        end
                    )
                }
            }
        }

        return emptyList()
    }

    private fun collectDirection(
        board: Board,
        startRow: Int,
        startColumn: Int,
        dr: Int,
        dc: Int,
        target: Cell
    ): List<Pair<Int, Int>> {

        val result =
            mutableListOf<Pair<Int, Int>>()

        var row = startRow + dr
        var column = startColumn + dc

        while (
            row in 0 until board.size &&
            column in 0 until board.size &&
            board.get(row, column) == target
        ) {
            result += row to column

            row += dr
            column += dc
        }

        return result
    }
}
```

The win checker must have unit tests for all four directions and all board sizes.

---

# 16. Game ViewModel

The ViewModel coordinates UI, engine, AI, sound, haptics, and persistence.

Basic structure:

```kotlin
class GameViewModel(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private var engine: GameEngine? = null

    private val _uiState =
        MutableStateFlow(
            createInitialState()
        )

    val uiState =
        _uiState.asStateFlow()

    fun startGame(
        config: BoardConfig,
        mode: GameMode,
        difficulty: Difficulty,
        humanPlayer: Player
    ) {
        // Create engine.
        // Create initial UI state.
    }

    fun onCellClicked(
        row: Int,
        column: Int
    ) {
        // Validate.
        // Make move.
        // Update state.
        // Start AI if required.
    }

    fun restartGame() {
        // Reset board using same configuration.
    }

    fun pauseGame() {
        _uiState.update {
            it.copy(isPaused = true)
        }
    }

    fun resumeGame() {
        _uiState.update {
            it.copy(isPaused = false)
        }
    }
}
```

Never place this logic inside `GameScreen`.

---

# 17. Input Validation

Reject moves when:

```text
Game is over
Game is paused
AI is thinking
Cell is occupied
Current player is AI
Row is outside board
Column is outside board
```

Example:

```kotlin
if (state.result != GameResult.InProgress) return
if (state.isPaused) return
if (state.isAiThinking) return

if (
    state.gameMode ==
    GameMode.HUMAN_VS_COMPUTER &&
    state.currentPlayer != state.humanPlayer
) {
    return
}

if (
    row !in 0 until state.boardConfig.size ||
    column !in 0 until state.boardConfig.size
) {
    return
}

if (!state.board.isEmpty(row, column)) {
    return
}
```

This prevents double taps and invalid state transitions.

---

# 18. AI Interface

```kotlin
interface AiEngine {

    suspend fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        difficulty: Difficulty
    ): Move
}
```

AI must never modify the live board.

It should simulate moves on copied boards and return one move.

---

# 19. Easy AI

Behavior:

1. Collect empty cells.
2. Optionally detect immediate win.
3. Optionally detect immediate block.
4. Otherwise select randomly.

For a more useful Easy mode:

```text
60% random move
20% block immediate threat
20% take immediate win
```

Make these probabilities configurable.

For unit tests, inject a deterministic random source.

---

# 20. Medium AI

Priority:

```text
1. Immediate win
2. Immediate block
3. Create a threat
4. Block a strong threat
5. Prefer center
6. Prefer cells near existing pieces
7. Random valid move
```

Do not scan every empty cell repeatedly when unnecessary.

Use candidate generation.

---

# 21. Hard AI — 3×3

Use Minimax with alpha-beta pruning.

Terminal scores:

```text
AI win  = +100 - depth
Loss    = -100 + depth
Draw    = 0
```

Function:

```kotlin
fun minimax(
    board: Board,
    depth: Int,
    maximizing: Boolean,
    alpha: Int,
    beta: Int
): Int
```

Hard 3×3 AI must:

- Never lose against perfect play.
- Take an immediate win.
- Block an immediate loss.
- Prefer faster wins.
- Prefer slower losses.

Use complete search because 3×3 has a small state space.

---

# 22. Hard AI — 6×6 / 9×9 / 11×11

Do not use unrestricted minimax.

Use:

```text
Candidate generation
        ↓
Immediate win detection
        ↓
Immediate block detection
        ↓
Threat detection
        ↓
Heuristic evaluation
        ↓
Limited-depth search
        ↓
Best candidate
```

Recommended starting configuration:

```text
6×6:
searchDepth = 2
neighborRadius = 2
maxCandidates = 40

9×9:
searchDepth = 2
neighborRadius = 2
maxCandidates = 50

11×11:
searchDepth = 2
neighborRadius = 2
maxCandidates = 60
```

These are starting values and must be benchmarked.

---

# 23. Candidate Generation

On large boards, do not evaluate every empty cell.

Generate cells within a radius of existing symbols.

Example:

```text
Existing X/O
     ↓
Candidate radius = 2
     ↓
Evaluate nearby empty cells
```

If the board is empty, choose the center.

Example:

```kotlin
fun centerMove(size: Int): Move {
    val center = size / 2

    return Move(
        row = center,
        column = center,
        player = Player.X
    )
}
```

For larger boards, candidate cells can be sorted by proximity to the center and nearby occupied cells.

---

# 24. Immediate Win Detection

For every candidate:

```text
1. Simulate AI move.
2. Run WinChecker.
3. If AI wins, return immediately.
```

This must happen before heuristic evaluation.

---

# 25. Immediate Block Detection

For every candidate:

```text
1. Simulate opponent move.
2. Run WinChecker.
3. If opponent wins, block that cell.
```

This must happen before normal scoring.

---

# 26. Heuristic Evaluation

Evaluate board patterns.

Example starting weights:

```text
Immediate win        1,000,000
Immediate block        900,000
Double threat          100,000
Open 4                   20,000
Open 3                    2,000
Open 2                      200
Center                     100
Other                        1
```

For opponent patterns, subtract the equivalent defensive score.

Blocked patterns must score lower.

Example:

```text
X X X X _
```

is stronger than:

```text
X X X X O
```

---

# 27. AI Configuration

Centralize tuning values:

```kotlin
data class AiConfig(
    val searchDepth: Int,
    val neighborRadius: Int,
    val maxCandidates: Int,
    val randomness: Float
)
```

This allows difficulty tuning without rewriting AI logic.

---

# 28. AI Difficulty Design

## Easy

```text
High randomness
Basic threat awareness
Fast response
```

## Medium

```text
Low randomness
Immediate win/block
Basic strategy
Candidate evaluation
```

## Hard

```text
No randomness by default
Immediate win/block
Threat analysis
Heuristic evaluation
Limited search
Strong positional play
```

---

# 29. AI Threading

Never run AI search on the UI thread.

Use:

```kotlin
viewModelScope.launch {

    _uiState.update {
        it.copy(isAiThinking = true)
    }

    val move = withContext(Dispatchers.Default) {
        aiEngine.findMove(
            board = currentBoard,
            aiPlayer = aiPlayer,
            winLength = winLength,
            difficulty = difficulty
        )
    }

    // Apply move.
}
```

The game UI must remain responsive while AI is calculating.

---

# 30. AI Cancellation

AI coroutine must be cancelled when:

- User restarts.
- User leaves the game.
- User returns to main menu.
- New game starts.
- ViewModel is destroyed.

Use a `Job`:

```kotlin
private var aiJob: Job? = null
```

Before starting:

```kotlin
aiJob?.cancel()
```

Then:

```kotlin
aiJob = viewModelScope.launch {
    // AI work.
}
```

---

# 31. Compose Game Board

Use `Canvas` for the board.

This provides efficient control over:

- Grid
- Glow
- X
- O
- Winning line
- Animations

Basic structure:

```kotlin
@Composable
fun NeonBoard(
    board: Board,
    winningCells: List<Pair<Int, Int>>,
    enabled: Boolean,
    onCellClick: (Int, Int) -> Unit
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(
                board.size,
                enabled
            ) {
                detectTapGestures { offset ->
                    // Convert touch position.
                }
            }
    ) {
        // Draw background.
        // Draw glow.
        // Draw grid.
        // Draw symbols.
        // Draw winning line.
    }
}
```

---

# 32. Board Touch Mapping

For an N×N board:

```text
cellWidth = canvasWidth / N
cellHeight = canvasHeight / N
```

Convert touch:

```kotlin
val column =
    (offset.x / cellWidth).toInt()

val row =
    (offset.y / cellHeight).toInt()
```

Clamp:

```kotlin
val safeRow =
    row.coerceIn(0, board.size - 1)

val safeColumn =
    column.coerceIn(0, board.size - 1)
```

Then:

```kotlin
onCellClick(
    safeRow,
    safeColumn
)
```

---

# 33. Neon Theme

Suggested palette:

```kotlin
object NeonColors {

    val Background =
        Color(0xFF050510)

    val Panel =
        Color(0xFF0B0B1A)

    val NeonX =
        Color(0xFF00F5FF)

    val NeonO =
        Color(0xFFFF2BD6)

    val Grid =
        Color(0xFF5146FF)

    val TextPrimary =
        Color.White

    val TextSecondary =
        Color(0xFFB8B4D9)
}
```

Primary identity:

```text
CYAN X
MAGENTA O
DARK BACKGROUND
PURPLE/BLUE GRID
```

Do not introduce too many accent colors.

---

# 34. Neon X Rendering

Draw X using two diagonal lines.

```kotlin
drawLine(
    start = Offset(left, top),
    end = Offset(right, bottom),
    strokeWidth = stroke
)

drawLine(
    start = Offset(right, top),
    end = Offset(left, bottom),
    strokeWidth = stroke
)
```

Create the glow with multiple passes:

```text
Wide dim glow
      ↓
Medium glow
      ↓
Sharp bright line
```

Animate the drawing progress:

```text
0.0 → 1.0
```

---

# 35. Neon O Rendering

Use an arc:

```kotlin
drawArc(
    color = NeonColors.NeonO,
    startAngle = 0f,
    sweepAngle = animatedSweep,
    useCenter = false,
    ...
)
```

Animate:

```text
0° → 360°
```

The O should appear to draw itself.

---

# 36. Winning Line

When a player wins:

1. Get first winning cell.
2. Get last winning cell.
3. Calculate cell centers.
4. Draw a line between them.
5. Animate line progress from 0 to 1.
6. Pulse winning symbols.

Use:

```text
startPoint
endPoint
progress
```

Calculate:

```text
currentX =
startX + (endX - startX) * progress

currentY =
startY + (endY - startY) * progress
```

---

# 37. Animations

Required animations:

## Cell placement

Duration:

```text
180ms
```

Effect:

```text
Scale 0.7 → 1.0
Glow 0 → normal
```

## X drawing

```text
180–250ms
```

## O drawing

```text
250–350ms
```

## Winning line

```text
500ms
```

## Winning pulse

```text
700ms
```

## Button press

```text
100ms
```

## Screen transition

```text
250ms
```

Centralize durations:

```kotlin
object AnimationDurations {
    const val CELL_APPEAR = 180
    const val X_DRAW = 220
    const val O_DRAW = 300
    const val WIN_LINE = 500
    const val WIN_PULSE = 700
    const val BUTTON_PRESS = 100
    const val SCREEN_TRANSITION = 250
}
```

---

# 38. Background Effects

Use a subtle animated background.

Possible effects:

- Small particles
- Faint neon grid
- Slow gradient movement
- Light streaks
- Glow particles

Rules:

- Keep effects subtle.
- Do not interfere with board readability.
- Disable expensive effects when animations are turned off.
- Avoid unnecessary per-frame object allocations.

---

# 39. Main Menu

Screen hierarchy:

```text
NeonBackground

TIC TAC TAE

NEON X • NEON O

PLAY

HOW TO PLAY

STATS

SETTINGS
```

Title should have a glow effect.

Primary Play button should be visually dominant.

---

# 40. Setup Screen

Step 1:

```text
SELECT GAME MODE

[ HUMAN VS COMPUTER ]

[ HUMAN VS HUMAN ]
```

Step 2:

```text
SELECT BOARD

[ 3 × 3 ]
Classic • Get 3

[ 6 × 6 ]
Mega • Get 4

[ 9 × 9 ]
Pro • Get 5

[ 11 × 11 ]
Ultimate • Get 5
```

Step 3 for Human vs Computer:

```text
DIFFICULTY

[ EASY ]
[ MEDIUM ]
[ HARD ]
```

Step 4:

```text
YOUR SYMBOL

[ X ]
[ O ]
```

Final:

```text
[ START GAME ]
```

---

# 41. Game Screen

Recommended hierarchy:

```text
Scaffold
└── Box
    ├── NeonBackground
    └── Column
        ├── TopBar
        ├── ScoreRow
        ├── TurnIndicator
        ├── BoardContainer
        │   └── NeonBoard
        └── BottomControls
```

Top bar:

```text
TIC TAC TAE        PAUSE
```

Score:

```text
X  03             O  02
```

Turn:

```text
X'S TURN
```

Bottom:

```text
RESTART
```

---

# 42. Score System

Track current match:

```kotlin
data class MatchScore(
    val xWins: Int = 0,
    val oWins: Int = 0,
    val draws: Int = 0
)
```

Rematch keeps the match score.

"New Game" resets the match score.

Lifetime statistics are separate.

---

# 43. Pause Screen

Overlay:

```text
GAME PAUSED

[ RESUME ]

[ RESTART ]

[ MAIN MENU ]
```

Use a translucent dark overlay.

Pause must prevent board input.

---

# 44. Result Screen

## Player Wins

```text
✦ WINNER ✦

NEON X

YOU WON!

X 05  -  O 03

[ REMATCH ]

[ CHANGE GAME ]

[ MAIN MENU ]
```

## Computer Wins

```text
GAME OVER

COMPUTER WINS

[ TRY AGAIN ]

[ MAIN MENU ]
```

## Draw

```text
DRAW

PERFECTLY MATCHED

[ REMATCH ]

[ MAIN MENU ]
```

---

# 45. Navigation

Routes:

```text
main
setup
game
result
settings
stats
how_to_play
```

Use Navigation Compose.

Do not pass the entire game state through navigation arguments.

The ViewModel owns active game state.

---

# 46. Persistence

Use Jetpack DataStore Preferences.

Settings:

```text
soundEnabled
musicEnabled
vibrationEnabled
animationsEnabled
```

Statistics:

```text
gamesPlayed
wins
losses
draws
xWins
oWins

classicGames
megaGames
proGames
ultimateGames
```

Do not use a database for simple counters unless future requirements require detailed game history.

---

# 47. Preferences Repository

Example:

```kotlin
class PreferencesRepository(
    private val context: Context
) {

    private val dataStore =
        context.dataStore

    suspend fun setSoundEnabled(
        enabled: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[SOUND_ENABLED] =
                enabled
        }
    }

    val soundEnabled: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[SOUND_ENABLED] ?: true
        }
}
```

Define preference keys in one file.

---

# 48. Statistics Repository

Expose methods:

```kotlin
interface StatisticsRepository {

    suspend fun recordWin(
        player: Player,
        boardSize: Int,
        againstComputer: Boolean
    )

    suspend fun recordLoss(
        boardSize: Int
    )

    suspend fun recordDraw(
        boardSize: Int
    )

    fun statistics(): Flow<Statistics>
}
```

Update statistics only after a terminal result.

Protect against duplicate result recording.

---

# 49. Statistics Model

```kotlin
data class Statistics(
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val xWins: Int = 0,
    val oWins: Int = 0
)
```

Optional future fields:

```text
bestWinStreak
currentWinStreak
hardAiWins
threeByThreeWins
sixBySixWins
nineByNineWins
elevenByElevenWins
```

---

# 50. Sound

Create:

```kotlin
class SoundManager(
    private val context: Context
)
```

Resources:

```text
res/raw/
    click.wav
    move_x.wav
    move_o.wav
    win.wav
    draw.wav
    game_start.wav
    game_over.wav
```

Preload commonly used short effects.

Sound must be controlled centrally.

---

# 51. Haptics

Create:

```kotlin
class HapticManager(
    private val context: Context
) {

    fun lightTap() {
        // Light feedback.
    }

    fun win() {
        // Strong feedback.
    }
}
```

Use:

```text
Cell tap       → light
Button         → light
Winning move   → strong
Draw           → medium
```

Respect the user's vibration setting.

---

# 52. Accessibility

Do not rely solely on cyan vs magenta.

X and O must have clearly different shapes.

Board cells should expose semantic information:

```text
Row 3 Column 5, empty
Row 3 Column 5, X
Row 3 Column 5, O
Row 3 Column 5, X, winning cell
```

Buttons require meaningful labels.

---

# 53. Responsive Design

Support:

- Small phones
- Standard phones
- Large phones
- Tablets

Use Compose constraints.

The board should normally be square:

```text
width = min(availableWidth, availableHeight)
height = width
```

Do not shrink 11×11 cells below a usable touch size.

If necessary, implement optional zoom/pan.

---

# 54. Large Board UX

9×9 and 11×11 must remain easy to play.

Requirements:

- Clear grid
- Consistent cell spacing
- Fixed turn indicator
- Fixed controls
- Board remains centered
- No accidental scrolling
- Winning line visible
- Touch feedback

If zoom is implemented:

```text
Pinch → zoom
Drag → pan
Double tap → reset zoom
```

Only add zoom if actual device testing shows it is necessary.

---

# 55. Game Lifecycle

The active game must survive normal lifecycle changes.

Use:

```text
ViewModel
SavedStateHandle where appropriate
```

Handle:

- Activity recreation
- Background → foreground
- Configuration changes

Do not store active game state only in Activity fields.

---

# 56. Error Handling

AI failure:

```text
1. Cancel failed AI operation.
2. Set isAiThinking = false.
3. Preserve current board.
4. Allow retry.
5. Log technical error in debug builds.
```

Never expose stack traces to users.

The game must fail gracefully.

---

# 57. Performance

Important performance targets:

- Smooth board rendering.
- No unnecessary Compose recompositions.
- No AI work on Main thread.
- No unnecessary object allocation inside Canvas drawing.
- Efficient board access.
- Limited candidate generation for large AI boards.

Most important stress test:

```text
11×11
+
Hard AI
+
Animations enabled
```

---

# 58. AI Performance

If Hard AI becomes too slow:

Reduce in this order:

```text
1. Candidate count
2. Search depth
3. Neighbor radius
4. Expensive heuristic patterns
```

Never freeze the UI while waiting for AI.

---

# 59. Unit Tests

Create:

```text
src/test/
├── BoardTest.kt
├── WinCheckerTest.kt
├── GameEngineTest.kt
├── MoveValidatorTest.kt
├── EasyAiTest.kt
├── MediumAiTest.kt
├── HardAi3x3Test.kt
└── HeuristicAiTest.kt
```

---

# 60. Win Tests

Test:

```text
3×3 horizontal
3×3 vertical
3×3 diagonal
3×3 reverse diagonal

6×6 four horizontal
6×6 four vertical
6×6 four diagonal
6×6 four reverse diagonal

9×9 five horizontal
9×9 five vertical
9×9 five diagonal
9×9 five reverse diagonal

11×11 five horizontal
11×11 five vertical
11×11 five diagonal
11×11 five reverse diagonal
```

Also test wins:

```text
top-left
top-right
bottom-left
bottom-right
center
with extra symbols
inside longer sequences
```

---

# 61. Draw Tests

Verify:

```text
Board full
AND
No winning sequence
```

returns:

```kotlin
GameResult.Draw
```

Test all supported board sizes.

---

# 62. Invalid Move Tests

Verify:

```text
Occupied cell
Negative row
Negative column
Row >= board size
Column >= board size
Move after win
Move after draw
Move while paused
Move while AI thinking
AI move during human turn
```

All must be rejected.

---

# 63. AI Tests

3×3 Hard:

```text
Must take immediate win.
Must block immediate loss.
Must never lose against perfect play.
```

Large-board AI:

```text
Must take immediate win.
Must block immediate loss.
Must return valid empty cell.
Must not modify original board.
```

---

# 64. UI Tests

Test navigation:

```text
Main → Setup
Setup → Game
Game → Result
Result → Rematch
Result → Main Menu
```

Test:

```text
Pause → Resume
Pause → Restart
Settings persistence
Stats persistence
```

---

# 65. Build Configuration

Use release and debug build types.

Debug:

```text
minifyEnabled = false
debuggable = true
```

Release:

```text
minifyEnabled = true
shrinkResources = true
```

Configure R8 rules only when required by dependencies.

Do not enable minification until release testing is complete.

---

# 66. Application ID

Suggested:

```text
com.tictactae.app
```

Use a production package name that will not need to change after publishing.

---

# 67. App Icon

Create a neon X/O icon:

```text
Dark background

       X

       O
```

Requirements:

- Recognizable at small size.
- No tiny text.
- Strong contrast.
- Cyan X.
- Magenta O.
- Dark background.
- Adaptive Android icon support.

---

# 68. Splash Screen

Use Android's splash-screen API.

Visual:

```text
Dark background

NEON X
   +
NEON O

TIC TAC TAE
```

Keep the splash short and avoid unnecessary loading.

---

# 69. Settings Screen

```text
SETTINGS

Sound Effects       ON
Music               ON
Vibration           ON
Neon Animations     ON

[ RESET SETTINGS ]

[ BACK ]
```

If music is not included in MVP, remove the Music setting rather than showing a non-functional option.

---

# 70. How To Play

Explain:

```text
Place your symbol on an empty cell.

Get the required number of consecutive
symbols to win.

3×3 → Get 3
6×6 → Get 4
9×9 → Get 5
11×11 → Get 5
```

Mention all four directions:

- Horizontal
- Vertical
- Diagonal
- Reverse diagonal

---

# 71. Stats Screen

Display:

```text
YOUR STATS

Games Played
Wins
Losses
Draws

X Wins
O Wins

3×3 Games
6×6 Games
9×9 Games
11×11 Games
```

Optional:

```text
Win Rate
Best Streak
Hard AI Wins
```

---

# 72. Game Flow

```text
APP START
   ↓
MAIN MENU
   ↓
PLAY
   ↓
GAME MODE
   ↓
BOARD SIZE
   ↓
DIFFICULTY
   ↓
PLAYER SYMBOL
   ↓
START
   ↓
CURRENT PLAYER
   ↓
MAKE MOVE
   ↓
VALIDATE MOVE
   ↓
CHECK WIN
   ↓
CHECK DRAW
   ↓
AI TURN
   ↓
CHECK WIN
   ↓
CHECK DRAW
   ↓
NEXT TURN
   ↓
GAME OVER
   ↓
RESULT
   ↓
REMATCH / MAIN MENU
```

---

# 73. State Machine

Use these conceptual states:

```text
MENU
SETUP
PLAYING
PAUSED
X_WON
O_WON
DRAW
```

Do not allow impossible transitions.

Examples:

```text
PAUSED → PLAYING
PLAYING → X_WON
PLAYING → O_WON
PLAYING → DRAW
X_WON → REMATCH
O_WON → REMATCH
DRAW → REMATCH
```

---

# 74. Prevent Duplicate Game Results

A terminal result should be recorded once.

Use a guard such as:

```kotlin
private var resultRecorded = false
```

When game ends:

```kotlin
if (!resultRecorded) {
    resultRecorded = true
    statisticsRepository.record(...)
}
```

Reset it for every new game.

---

# 75. Main Game Rendering Strategy

Recommended drawing order:

```text
1. Board background
2. Wide grid glow
3. Sharp grid
4. Cell hover/tap glow
5. X/O glow
6. X/O sharp foreground
7. Winning cell glow
8. Winning line
9. Optional particles
```

Keep expensive effects disabled when animations are off.

---

# 76. Neon Button

Create reusable:

```kotlin
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
)
```

Visual:

```text
Dark transparent panel
+
Neon border
+
Soft outer glow
+
Bright text
```

Pressed state:

```text
Glow increases
Scale slightly decreases
```

---

# 77. Neon Cell

Do not create complex independent UI trees for every board cell.

For 11×11:

```text
121 cells
```

Prefer Canvas rendering.

Use Compose state only for board data and animations.

---

# 78. Board Recomposition

The board should only redraw when relevant state changes.

Avoid:

```kotlin
remember {
    expensiveObject()
}
```

inside frequently recomposed sections without need.

Use stable data where possible.

---

# 79. Offline Requirements

No network is required for:

- Human vs Human
- Human vs Computer
- All board sizes
- AI
- Statistics
- Settings
- Sound
- Haptics

No account is required for the MVP.

---

# 80. Permissions

MVP should require no dangerous permissions.

Avoid requesting:

```text
Location
Camera
Contacts
Microphone
Storage
```

Only request permissions if a future feature genuinely requires them.

---

# 81. Monetization

Optional future monetization:

- Banner ads on menu screens.
- Interstitial ads between completed games.
- Rewarded ads for optional bonuses.
- Remove Ads purchase.

Never show an ad in the middle of an active move.

Do not interrupt:

```text
Player turn
AI turn
Winning animation
```

---

# 82. Analytics

If analytics is added later, keep it isolated from game logic.

Possible events:

```text
game_started
game_completed
game_won
game_lost
game_draw
board_selected
difficulty_selected
mode_selected
rematch_clicked
```

Do not make analytics required for the game engine.

---

# 83. Future Online Multiplayer

Do not implement networking in MVP.

Design interfaces so it can be added later:

```kotlin
interface GameRepository {
    suspend fun submitMove(move: Move)
    suspend fun receiveMoves(): Flow<Move>
}
```

The current offline implementation can remain local.

---

# 84. Future Features

Potential updates:

## Online Multiplayer

Player vs player over the internet.

## Private Rooms

```text
ROOM CODE
X7K92
```

## Leaderboards

Separate rankings for:

```text
3×3
6×6
9×9
11×11
```

## Daily Challenge

One unique challenge each day.

## Achievements

```text
FIRST WIN
WIN 10 GAMES
WIN 5 IN A ROW
BEAT HARD AI
MASTER 11×11
```

## Themes

```text
Cyberpunk
Galaxy
Fire
Ice
Matrix
Retro Arcade
```

---

# 85. Development Phases

## Phase 1 — Project Setup

- Create Android project.
- Configure Kotlin.
- Configure Compose.
- Configure theme.
- Create package structure.

## Phase 2 — Game Engine

- Board.
- Player.
- Move.
- BoardConfig.
- WinChecker.
- Draw detection.
- GameEngine.

## Phase 3 — Tests

- Win tests.
- Draw tests.
- Invalid move tests.
- Engine tests.

## Phase 4 — Basic UI

- Main menu.
- Setup.
- Game screen.
- Result screen.

## Phase 5 — Human Gameplay

- Human vs Human.
- All board sizes.
- Score.
- Rematch.

## Phase 6 — AI

- Easy.
- Medium.
- Hard 3×3 Minimax.
- Large-board heuristic AI.

## Phase 7 — Visual Polish

- Neon grid.
- Neon X.
- Neon O.
- Glow.
- Winning line.
- Background.
- Screen transitions.

## Phase 8 — Feedback

- Sound.
- Haptics.
- Settings.

## Phase 9 — Persistence

- DataStore.
- Statistics.
- Preferences.

## Phase 10 — QA

- Unit tests.
- UI tests.
- Performance tests.
- Lifecycle tests.
- Accessibility tests.

## Phase 11 — Release

- Release signing.
- R8.
- App icon.
- Splash screen.
- Store assets.
- Production testing.

---

# 86. Testing Matrix

Test every combination:

| Mode | 3×3 | 6×6 | 9×9 | 11×11 |
|---|---|---|---|---|
| Human vs Human | ✓ | ✓ | ✓ | ✓ |
| AI Easy | ✓ | ✓ | ✓ | ✓ |
| AI Medium | ✓ | ✓ | ✓ | ✓ |
| AI Hard | ✓ | ✓ | ✓ | ✓ |

For every combination test:

- Win
- Loss
- Draw
- Restart
- Rematch
- Pause
- Resume
- Main menu
- Invalid taps

---

# 87. Device Testing

Test at minimum:

```text
Small Android phone
Standard Android phone
Large Android phone
Tablet
```

Test:

```text
Android 8+
Latest Android
```

Also test:

```text
Low memory device
Slow device
Fast device
```

---

# 88. Acceptance Criteria

The MVP is complete only when:

```text
[ ] Android project builds.
[ ] App launches without crash.
[ ] Main menu works.
[ ] Setup screen works.
[ ] Human vs Human works.
[ ] Human vs Computer works.
[ ] Easy AI works.
[ ] Medium AI works.
[ ] Hard AI works.
[ ] 3×3 works.
[ ] 6×6 works.
[ ] 9×9 works.
[ ] 11×11 works.
[ ] All win directions work.
[ ] Draw detection works.
[ ] Invalid moves are blocked.
[ ] Double taps are blocked.
[ ] AI runs off Main thread.
[ ] AI can be cancelled.
[ ] Pause works.
[ ] Restart works.
[ ] Rematch works.
[ ] Result screen works.
[ ] Statistics persist.
[ ] Settings persist.
[ ] Sound works.
[ ] Haptics work.
[ ] Neon X animation works.
[ ] Neon O animation works.
[ ] Winning line animation works.
[ ] Large boards remain responsive.
[ ] Accessibility labels exist.
[ ] Game works offline.
[ ] No critical crashes.
```

---

# 89. Coding Rules

## Rule 1

Do not put game logic inside Composables.

## Rule 2

Do not create separate engines for each board size.

## Rule 3

Do not run AI on the Main thread.

## Rule 4

Do not mutate the live board from AI code.

## Rule 5

Use immutable UI state.

## Rule 6

Keep Android dependencies outside the domain game engine.

## Rule 7

Use dependency injection or constructor injection where practical.

## Rule 8

Centralize constants.

## Rule 9

Write unit tests before adding visual complexity.

## Rule 10

Do not sacrifice board usability for visual effects.

---

# 90. Final Architecture

```text
                         ┌───────────────────────┐
                         │       Compose UI      │
                         │                       │
                         │ Main Menu             │
                         │ Setup                 │
                         │ Game                  │
                         │ Result                │
                         │ Settings              │
                         │ Statistics            │
                         └───────────┬───────────┘
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │       ViewModel       │
                         │                       │
                         │ GameUiState           │
                         │ User actions          │
                         │ AI coordination       │
                         │ Lifecycle             │
                         └───────────┬───────────┘
                                     │
                    ┌────────────────┴────────────────┐
                    │                                 │
                    ▼                                 ▼
          ┌────────────────────┐            ┌────────────────────┐
          │    Game Engine     │            │     AI Engine      │
          │                    │            │                    │
          │ Board              │            │ Easy               │
          │ Move validation    │            │ Medium             │
          │ Win detection      │            │ Hard Minimax 3×3   │
          │ Draw detection     │            │ Heuristic 6–11×11  │
          └──────────┬─────────┘            └──────────┬─────────┘
                     │                                 │
                     └────────────────┬────────────────┘
                                      │
                                      ▼
                           ┌──────────────────────┐
                           │     Local Storage    │
                           │                      │
                           │ DataStore            │
                           │ Settings             │
                           │ Statistics            │
                           └──────────────────────┘
```

---

# 91. Final Product Vision

Tic Tac Tae should be a polished, fast, offline-first neon board game.

The core experience:

```text
OPEN APP
   ↓
TAP PLAY
   ↓
SELECT MODE
   ↓
SELECT BOARD
   ↓
SELECT DIFFICULTY
   ↓
START
   ↓
GLOWING X vs O BATTLE
   ↓
WIN / DRAW
   ↓
REMATCH
```

The most important differentiators are:

1. Neon visual identity.
2. Multiple board sizes.
3. Strong AI.
4. Fast gameplay.
5. Smooth animations.
6. Large-board strategy.
7. Offline operation.
8. Clean technical architecture.
9. Persistent statistics.
10. Future-ready design.

The implementation should prioritize gameplay correctness first, then AI, then visual polish.

---

# END — TIC TAC TAE

## NEON X • NEON O • ENDLESS BATTLES
