package com.app.clonewhatsapp.model

import com.google.firebase.database.FirebaseDatabase

class Usuario(
    var uid: String = "",
    var nome: String = "",
    var profileImageUrl: String = "",
    var numero: String = "",
    var status: String = ""
)
