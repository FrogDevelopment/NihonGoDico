package fr.frogdevelopment.nihongo.dico.data.rest

import java.io.Serializable

class EntryDetails : Serializable {
    var entrySeq = 0
    var kanji: String? = null
    var kana: String? = null
    var reading: String? = null
    var pos: Set<String>? = null
    var field: Set<String>? = null
    var misc: Set<String>? = null
    var info: String? = null
    var dial: Set<String>? = null
    var gloss: String? = null
}