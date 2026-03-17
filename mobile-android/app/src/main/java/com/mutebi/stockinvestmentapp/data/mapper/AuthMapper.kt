package com.mutebi.stockinvestmentapp.data.mapper

import com.mutebi.stockinvestmentapp.data.remote.dto.UserDto
import com.mutebi.stockinvestmentapp.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        profileCompleted = profile_completed
    )
}