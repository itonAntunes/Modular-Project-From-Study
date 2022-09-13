package com.itonAntunes.mainactivity.core.toast.toast

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.itonAntunes.mainactivity.R
import com.itonAntunes.mainactivity.databinding.FragmentToastSnakeBinding
import com.itonAntunes.mainactivity.util.toast

class ToastSnakeFragment : Fragment(R.layout.fragment_toast_snake) {

    private lateinit var binding: FragmentToastSnakeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        binding = FragmentToastSnakeBinding.bind(view)

        binding.toast.setOnClickListener {

            val msg = "Minha mensagem teste"
            Toast
                .makeText(requireContext(),msg,Toast.LENGTH_SHORT)
                .show()

        }

        binding.snake.setOnClickListener {
            Snackbar.make(view,"hello", Snackbar.LENGTH_SHORT).show()
        }

        binding.snakeAction.setOnClickListener {
            Snackbar
                .make(view,"Action",Snackbar.LENGTH_SHORT)
                .setAction("okay"){toast("i am a snake!") }
                .show()
        }
    }
}