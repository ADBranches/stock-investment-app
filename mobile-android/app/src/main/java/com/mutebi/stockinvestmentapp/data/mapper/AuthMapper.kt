package com.mutebi.stockinvestmentapp.data.mapper

import com.mutebi.stockinvestmentapp.data.remote.dto.AuthUserDto
import com.mutebi.stockinvestmentapp.domain.model.User

fun AuthUserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        profileCompleted = profileCompleted
    )
}