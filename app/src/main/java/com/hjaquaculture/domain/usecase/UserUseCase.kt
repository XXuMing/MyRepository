package com.hjaquaculture.domain.usecase

import com.hjaquaculture.domain.repository.RegisterResult
import com.hjaquaculture.domain.repository.UserRepository
import jakarta.inject.Inject

class UserLoginUseCase  @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String, passwordHash: String) {
        repository.login(username, passwordHash)
    }
}

class UserRegisterUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(account: String, passwordHash: String): RegisterResult {
        return repository.registerUser(account, passwordHash)
    }
}

