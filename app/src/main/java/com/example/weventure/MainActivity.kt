package com.example.weventure

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weventure.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    internal var view: View? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        view = binding.root

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentTimeString.observe(this, {
            binding.textViewTime.text = it
        })

        showSecondsDialog()

    }

    private fun showSecondsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.enter_seconds_dialog)
        val seconds_edt = dialog.findViewById(R.id.seconds_edt) as EditText
        val ok_btn = dialog.findViewById(R.id.ok_btn) as Button
        ok_btn.setOnClickListener {
            if (validateSeconds(seconds_edt.text.trim().toString())) {
                dialog.dismiss()
            }
        }

        dialog.show()

    }

    private fun validateSeconds(seconds: String): Boolean {
        if (seconds.isEmpty()) {
            showMessage(resources.getString(R.string.please_input_seconds))
            return false
        }


        if (seconds.toLong() > System.currentTimeMillis() / 1000) {
            viewModel.setTimerValues(seconds.toLong() - System.currentTimeMillis() / 1000)
        } else {
            showMessage(resources.getString(R.string.please_input_upcoming_time))
            return false
        }

        return true
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}