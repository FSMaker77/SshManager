package com.example.sshmanager.data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromAuthType(value: AuthType): String = value.name

    @TypeConverter
    fun toAuthType(value: String): AuthType = AuthType.valueOf(value)
}
