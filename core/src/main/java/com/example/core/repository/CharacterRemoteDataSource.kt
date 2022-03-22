package com.example.core.repository

interface CharacterRemoteDataSource<T> {
    suspend fun fetchCharacters(queries: Map<String,String>): T
}