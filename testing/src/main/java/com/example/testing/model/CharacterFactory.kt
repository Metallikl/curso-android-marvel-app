package com.example.testing.model

import com.example.core.domain.Character

/**
 * Classe que gera characters para teste
 */
class CharacterFactory {
    //Retorna o character baseado no tipo de hero passado
    fun create(hero: Hero) = when (hero) {
        Hero.ThreeDMan -> Character(
            "3-D Man",
            "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
        )
        Hero.ABomb -> Character(
            "A-Bomb (HAS)",
            "https://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg"
        )
    }
    //Sealed Class Hero que
    sealed class Hero {
        object ThreeDMan : Hero()
        object ABomb : Hero()
    }
}