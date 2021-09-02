package com.app.clonewhatsapp.model

import java.io.Serializable

data class Usuario(
    var uid: String? = "",
    var nome: String? = "",
    var profileImageUrl: String? = "",
    var numero: String? = "",
    var status: String? = ""
)
