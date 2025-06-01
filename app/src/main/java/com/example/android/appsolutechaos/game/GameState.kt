package com.example.android.appsolutechaos.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class GameLevel {
    MENU,
    LEVEL_1,
    LEVEL_2,
    GAME_OVER
}

data class GameScore(
    val level: Int,
    val loops: Int,
    val totalTime: Float,
    val incorrectPresses: Int
)

class GameState {
    private val _currentLevel = MutableStateFlow(GameLevel.MENU)
    val currentLevel: StateFlow<GameLevel> = _currentLevel.asStateFlow()
    
    private val _score = MutableStateFlow(GameScore(1, 0, 0f, 0))
    val score: StateFlow<GameScore> = _score.asStateFlow()
    
    var isGamePaused by mutableStateOf(false)
        private set
    
    fun startLevel1() {
        _currentLevel.value = GameLevel.LEVEL_1
        _score.value = GameScore(1, 0, 0f, 0)
        isGamePaused = false
    }
    
    fun startLevel2() {
        _currentLevel.value = GameLevel.LEVEL_2
        _score.value = _score.value.copy(level = 2, loops = 0, totalTime = 0f, incorrectPresses = 0)
        isGamePaused = false
    }
    
    fun completeLevel1(loops: Int, totalTime: Float, incorrectPresses: Int) {
        _score.value = _score.value.copy(
            loops = loops,
            totalTime = totalTime,
            incorrectPresses = incorrectPresses
        )
        startLevel2()
    }
    
    fun completeLevel2(totalTime: Float, incorrectPresses: Int) {
        _score.value = _score.value.copy(
            totalTime = _score.value.totalTime + totalTime,
            incorrectPresses = _score.value.incorrectPresses + incorrectPresses
        )
        _currentLevel.value = GameLevel.GAME_OVER
    }
    
    fun resetGame() {
        _currentLevel.value = GameLevel.MENU
        _score.value = GameScore(1, 0, 0f, 0)
        isGamePaused = false
    }
    
    fun pauseGame() {
        isGamePaused = true
    }
    
    fun resumeGame() {
        isGamePaused = false
    }
} 