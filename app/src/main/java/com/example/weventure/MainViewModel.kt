package com.example.weventure

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    private var timeCountInMilliSeconds = 1 * 60000.toLong()


    private enum class TimerStatus {
        STARTED, STOPPED
    }

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val _currentTimeString = MutableLiveData<String>()
    val currentTimeString: LiveData<String>
        get() = _currentTimeString


    private val _tokenString = MutableLiveData<String>()
    val tokenString: LiveData<String>
        get() = _tokenString


    private var timerStatus = TimerStatus.STOPPED

    private var countDownTimer: CountDownTimer? = null


    /**
     * method to start count down timer
     */
    private fun startCountDownTimer() {
        timerStatus = TimerStatus.STARTED
        countDownTimer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                _currentTimeString.value = dhmsTimeFormatter(millisUntilFinished)
                _currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED
                // doGetToken()

            }
        }.start()
    }



    /**
     * method to initialize the values for count down timer
     */
    fun setTimerValues(time:Long) {
        //Time time in minutes
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 1000.toLong()
        startCountDownTimer()
    }

    /**
     * method to stop count down timer
     */
    private fun stopCountDownTimer() {
        countDownTimer!!.cancel()
    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return dd:hh:mm:ss time formatted string
     */
    private fun dhmsTimeFormatter(milliSeconds: Long): String? {

        return String.format(
            "%02d:%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toDays(milliSeconds),
            TimeUnit.MILLISECONDS.toHours(milliSeconds) - TimeUnit.DAYS.toHours(
                TimeUnit.MILLISECONDS.toDays(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }

}