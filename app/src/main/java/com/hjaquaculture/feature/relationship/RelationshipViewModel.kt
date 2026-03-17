package com.hjaquaculture.feature.relationship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.data.local.repository.RelationshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class RelationshipViewModel @Inject constructor(
    repo: RelationshipRepository
) : ViewModel() {

    val relationshipPagingData: Flow<PagingData<PeopleVO>> = repo.getCombinedPeople("",null)
        .map { pagingData ->
            pagingData.map { domain ->
                 domain.toVO()
            }
        }.cachedIn(viewModelScope)
}

