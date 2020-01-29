package fr.frogdevelopment.nihongo.dico.data.rest

import java.io.Serializable

class Sentence : Serializable {
    var japanese: String? = null
    var translation: String? = null
}