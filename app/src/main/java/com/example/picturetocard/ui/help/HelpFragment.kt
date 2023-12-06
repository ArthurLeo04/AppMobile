package com.example.picturetocard.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.picturetocard.databinding.FragmentHelpBinding
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.ui.game.TableTypeDialog

class HelpFragment() : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameViewModel: GameManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.helpButton2.setOnClickListener {
            val tableTypeDialog = TableTypeDialog(requireContext())
            tableTypeDialog.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}