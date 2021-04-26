package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.*

@Entity(tableName = "favorites",
        foreignKeys = [
            ForeignKey(entity = Sense::class,
                    parentColumns = arrayOf("sense_seq"),
                    childColumns = arrayOf("sense_seq"),
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.NO_ACTION)
        ],
        indices = [Index(value = ["sense_seq"], unique = true)])
class Favorite(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "sense_seq") val senseSeq: String)
