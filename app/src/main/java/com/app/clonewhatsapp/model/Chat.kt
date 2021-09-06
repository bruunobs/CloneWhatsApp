package com.app.clonewhatsapp.model

import java.time.temporal.Temporal

data class Chat(
    var remetenteId: String? = "",
    var destinaratioId: String? = "",
    var mensagem: String? = "",
    var tempo: Long? = -1,

)