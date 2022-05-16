package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.preference.AuthModel

object UserDataDummy {

    val authModel = AuthModel(
        email = "email",
        username = "username",
    )

    val userEntity = UserEntity(
        email = "email",
        username = "username",
        password = "password",
        fullName = null,
        birthDate = null,
        address = null,
        profileImage = null,
    )

}