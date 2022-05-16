package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()


    private lateinit var binding: FragmentGameFinishedBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.btFinish.setOnClickListener { retryGame() }
    }

    private fun bindViews(){
        binding.gameResult = args.gameResult
//        with(binding){
//            tvMinAnswers.text = String.format(
//                getString(R.string.tv_min_right_answers),
//                gameResult.countRightAnswers
//
//            )
//            tvRightAnswers.text = String.format(
//                getString(R.string.tv_right_answers),
//                gameResult.gameSettings.minCountOfRightAnswers
//            )
//        }
    }


    private fun retryGame() {
        findNavController().popBackStack()

    }
}