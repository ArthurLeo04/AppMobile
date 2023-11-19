package com.example.picturetocard.game

class RuleManager {
    // La matrice des régles du jeu
    var matrice = MatriceType.createDefault()

    // Liste des cartes dans le jeu, à remplacer plus tard par une BD
    val cards : CardList = CardList()

    init {
        val carte1 = Card(Colors.ROCHE, Effets.PLUS_UN)
        cards.addCard(carte1)

        val carte2 = Card(Colors.EAU, Effets.FEU)
        cards.addCard(carte2)

        val carte3 = Card(Colors.METAL, Effets.PLUS_UN)
        cards.addCard(carte3)

        val carte4 = Card(Colors.ROCHE, Effets.FEU)
        cards.addCard(carte4)

        val carte5 = Card(Colors.NATURE, Effets.AIR)
        cards.addCard(carte5)

        val carte6 = Card(Colors.GLACE, Effets.FOUDRE)
        cards.addCard(carte6)
    }
}