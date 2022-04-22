package com.redcatgames.musiclib.domain.usecase

import com.redcatgames.musiclib.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistListUseCase @Inject constructor(private val artistRepository: ArtistRepository) {
    operator fun invoke() = artistRepository.getArtistList()
}