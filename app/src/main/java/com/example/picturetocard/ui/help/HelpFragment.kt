package com.example.picturetocard.ui.help

import GridViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.picturetocard.databinding.FragmentHelpBinding
import com.example.picturetocard.game.GameViewModel

class HelpFragment() : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val gridView: GridView = binding.gridView

        // Remplacez "data" par votre liste de données pour le tableau
        val data : Array<Array<Int>> = gameViewModel.matrice.matrice// Votre liste de données

        // Calculez le nombre de colonnes en fonction de la taille de la liste de données
        val numColumns = data[0].size

        // Calculez le nombre de lignes en fonction de la taille de la liste de données et le nombre de colonnes
        val numRows = data.size

        // Définissez le nombre de colonnes et de lignes pour le GridView
        gridView.numColumns = numColumns
        gridView.layoutParams.height = gridView.columnWidth * numRows

        val gridViewAdapter = GridViewAdapter(data.map { row ->
            row.map { it.toString() }.toTypedArray()
        }.toTypedArray(), requireContext())

        // Défini l'adaptateur pour la gridView
        gridView.adapter = gridViewAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}