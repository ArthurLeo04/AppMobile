package com.example.picturetocard.ui.menu

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.picturetocard.GameActivity
import com.example.picturetocard.R
import com.example.picturetocard.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMenu
        menuViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val buttonPlay : Button = binding.play

        buttonPlay.setOnClickListener {
            val intent = Intent(requireActivity(), GameActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    fun animatePlay() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
