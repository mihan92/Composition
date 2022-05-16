package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl
    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _generateQuestion = MutableLiveData<Question>()
    val generateQuestion: LiveData<Question>
        get() = _generateQuestion

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    var countOfRightAnswers = 0
    private var countOfAllAnswers = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateGameQuestion()
        updateProgress()
    }

    fun chooseAnswer(number: Int) {
        val rightAnswer = _generateQuestion.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfAllAnswers++
        updateProgress()
        generateGameQuestion()
    }

    private fun updateProgress() {
        val progressInPercent = ((countOfRightAnswers / countOfAllAnswers.toDouble()) * 100).toInt()
        _percentOfRightAnswers.value = progressInPercent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.tv_count_right_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
    }

    private fun generateGameQuestion() {
        _generateQuestion.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        gameSettings = getGameSettingsUseCase(level)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formattedTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formattedTime(p0: Long): String {
        val seconds = p0 / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTE
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTE)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfAllAnswers,
            gameSettings
        )

    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}