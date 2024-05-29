package org.d3if3102.ezyretail.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = ""
)
