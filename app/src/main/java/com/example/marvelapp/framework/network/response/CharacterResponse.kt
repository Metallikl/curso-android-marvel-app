package com.example.marvelapp.framework.network.response

import com.luche.core.domain.Character

data class CharacterResponse(
    val id: String,
    val name: String,
    val thumbnail: ThumbnailResponse
)

//Extension fun que converte o Character de network para o de Model
fun CharacterResponse.toCharacterModel() : Character{
    return Character(
        name = this.name,
        imageUrl = "${thumbnail.path}.${thumbnail.extension}"
    )
}