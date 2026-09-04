package com.proto.simpletictactoe.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proto.simpletictactoe.data.PreferencesRepository
import com.proto.simpletictactoe.data.StatisticsRepository
import com.proto.simpletictactoe.domain.ai.AiEngine
import com.proto.simpletictactoe.domain.ai.AiEngineImpl
import com.proto.simpletictactoe.domain.engine.GameEngine
import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.BoardConfig
import com.proto.simpletictactoe.domain.model.BoardConfigs
import com.proto.simpletictactoe.domain.model.Difficulty
import com.proto.simpletictactoe.domain.model.GameMode
import com.proto.simpletictactoe.domain.model.GameResult
import com.proto.simpletictactoe.domain.model.MoveResult
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.util.AnalyticsManager
import com.proto.simpletictactoe.util.HapticManager
import com.proto.simpletictactoe.util.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
    val board: Board = Board(BoardConfigs.CLASSIC.size),
    val currentPlayer: Player = Player.X,
    val gameMode: GameMode = GameMode.HUMAN_VS_COMPUTER,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val boardConfig: BoardConfig = BoardConfigs.CLASSIC,
    val humanPlayer: Player = Player.X,
    val result: GameResult = GameResult.InProgress,
    val winningCells: List<Pair<Int, Int>> = emptyList(),
    val isPaused: Boolean = false,
    val isAiThinking: Boolean = false,
    val soundEnabled: Boolean = true,
    val xMatchWins: Int = 0,
    val oMatchWins: Int = 0,
    val matchDraws: Int = 0
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)
    private val statisticsRepository = StatisticsRepository(application)
    val soundManager = SoundManager(application)
    val hapticManager = HapticManager(application)
    private val aiEngine: AiEngine = AiEngineImpl()

    private var engine: GameEngine? = null
    private var aiJob: Job? = null
    private var resultRecorded = false

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferencesFlow.collect { prefs ->
                soundManager.isSoundEnabled = prefs.soundEnabled
                hapticManager.isVibrationEnabled = prefs.vibrationEnabled
                _uiState.update { it.copy(soundEnabled = prefs.soundEnabled) }
            }
        }
    }

    fun toggleSound() {
        val newSound = !_uiState.value.soundEnabled
        soundManager.isSoundEnabled = newSound
        _uiState.update { it.copy(soundEnabled = newSound) }
        viewModelScope.launch {
            preferencesRepository.setSoundEnabled(newSound)
        }
    }

    fun startGame(
        config: BoardConfig = _uiState.value.boardConfig,
        mode: GameMode = _uiState.value.gameMode,
        difficulty: Difficulty = _uiState.value.difficulty,
        humanPlayer: Player = _uiState.value.humanPlayer,
        resetScore: Boolean = false
    ) {
        aiJob?.cancel()
        resultRecorded = false

        engine = GameEngine(config, initialPlayer = Player.X)

        _uiState.update { current ->
            GameUiState(
                board = engine!!.board.copy(),
                currentPlayer = engine!!.currentPlayer,
                gameMode = mode,
                difficulty = difficulty,
                boardConfig = config,
                humanPlayer = humanPlayer,
                result = GameResult.InProgress,
                winningCells = emptyList(),
                isPaused = false,
                isAiThinking = false,
                soundEnabled = current.soundEnabled,
                xMatchWins = if (resetScore) 0 else current.xMatchWins,
                oMatchWins = if (resetScore) 0 else current.oMatchWins,
                matchDraws = if (resetScore) 0 else current.matchDraws
            )
        }

        soundManager.playClick()
        AnalyticsManager.logGameStarted(mode.name, "${config.size}x${config.size}", difficulty.name)
        checkAiTurn()
    }

    fun resetGame() {
        aiJob?.cancel()
        resultRecorded = false
        engine = null
        _uiState.update { current ->
            current.copy(
                result = GameResult.InProgress,
                winningCells = emptyList(),
                isPaused = false,
                isAiThinking = false
            )
        }
    }

    fun onCellClicked(row: Int, column: Int) {
        applyMove(row, column, isHuman = true)
    }

    private fun applyMove(row: Int, column: Int, isHuman: Boolean) {
        val state = _uiState.value
        if (state.result != GameResult.InProgress) return
        if (state.winningCells.isNotEmpty()) return
        if (state.isPaused) return
        if (isHuman && state.isAiThinking) return

        if (isHuman && state.gameMode == GameMode.HUMAN_VS_COMPUTER && state.currentPlayer != state.humanPlayer) {
            return
        }

        val activeEngine = engine ?: return
        val mover = activeEngine.currentPlayer

        when (val moveResult = activeEngine.makeMove(row, column)) {
            is MoveResult.Invalid -> {
                return
            }
            is MoveResult.Continue -> {
                playMoveAudioHaptic(mover)
                _uiState.update {
                    it.copy(
                        board = activeEngine.board.copy(),
                        currentPlayer = moveResult.nextPlayer
                    )
                }
                checkAiTurn()
            }
            is MoveResult.Win -> {
                playMoveAudioHaptic(mover)
                soundManager.playWin()
                hapticManager.winFeedback()

                _uiState.update {
                    it.copy(
                        board = activeEngine.board.copy(),
                        winningCells = moveResult.cells,
                        xMatchWins = if (moveResult.player == Player.X) it.xMatchWins + 1 else it.xMatchWins,
                        oMatchWins = if (moveResult.player == Player.O) it.oMatchWins + 1 else it.oMatchWins
                    )
                }

                viewModelScope.launch {
                    delay(850) // Allow victory line & glow animation to complete on board
                    _uiState.update {
                        it.copy(
                            result = GameResult.Winner(moveResult.player)
                        )
                    }
                    recordGameEnd(GameResult.Winner(moveResult.player))
                }
            }
            is MoveResult.Draw -> {
                playMoveAudioHaptic(mover)
                soundManager.playDraw()
                hapticManager.mediumTap()

                _uiState.update {
                    it.copy(
                        board = activeEngine.board.copy(),
                        matchDraws = it.matchDraws + 1
                    )
                }

                viewModelScope.launch {
                    delay(500)
                    _uiState.update {
                        it.copy(
                            result = GameResult.Draw
                        )
                    }
                    recordGameEnd(GameResult.Draw)
                }
            }
        }
    }

    private fun checkAiTurn() {
        val state = _uiState.value
        if (state.result != GameResult.InProgress) return
        if (state.gameMode != GameMode.HUMAN_VS_COMPUTER) return
        if (state.currentPlayer == state.humanPlayer) return

        aiJob?.cancel()
        _uiState.update { it.copy(isAiThinking = true) }

        aiJob = viewModelScope.launch {
            delay(750) // Human-like thinking delay so "AI IS THINKING..." is clearly visible
            val activeEngine = engine
            if (activeEngine == null) {
                _uiState.update { it.copy(isAiThinking = false) }
                return@launch
            }

            val currentBoard = activeEngine.board.copy()
            val aiPlayer = state.currentPlayer

            val aiMove = aiEngine.findMove(
                board = currentBoard,
                aiPlayer = aiPlayer,
                winLength = state.boardConfig.winLength,
                difficulty = state.difficulty
            )

            _uiState.update { it.copy(isAiThinking = false) }

            if (aiMove != null && _uiState.value.result == GameResult.InProgress) {
                applyMove(aiMove.row, aiMove.column, isHuman = false)
            }
        }
    }

    fun restartGame() {
        startGame(resetScore = false)
    }

    fun pauseGame() {
        _uiState.update { it.copy(isPaused = true) }
        soundManager.playClick()
    }

    fun resumeGame() {
        _uiState.update { it.copy(isPaused = false) }
        soundManager.playClick()
    }

    private fun playMoveAudioHaptic(player: Player) {
        if (player == Player.X) {
            soundManager.playMoveX()
        } else {
            soundManager.playMoveO()
        }
        hapticManager.lightTap()
    }

    private fun recordGameEnd(result: GameResult) {
        if (resultRecorded) return
        resultRecorded = true

        val state = _uiState.value
        val isComputerMode = state.gameMode == GameMode.HUMAN_VS_COMPUTER

        viewModelScope.launch {
            when (result) {
                is GameResult.Winner -> {
                    AnalyticsManager.logGameEnded("win", result.player.name, state.gameMode.name)
                    if (isComputerMode) {
                        if (result.player == state.humanPlayer) {
                            statisticsRepository.recordWin(result.player, state.boardConfig.size, againstComputer = true)
                        } else {
                            statisticsRepository.recordLoss(state.boardConfig.size)
                        }
                    } else {
                        statisticsRepository.recordWin(result.player, state.boardConfig.size, againstComputer = false)
                    }
                }
                is GameResult.Draw -> {
                    AnalyticsManager.logGameEnded("draw", null, state.gameMode.name)
                    statisticsRepository.recordDraw(state.boardConfig.size)
                }
                else -> {}
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        aiJob?.cancel()
        soundManager.release()
    }
}
