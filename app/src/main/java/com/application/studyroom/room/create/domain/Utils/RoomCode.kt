package com.application.studyroom.room.create.domain.Utils
import kotlin.random.Random

fun getNewRoomCode(): String {
    val builder = StringBuilder()
    repeat(6) { index ->
        if (index % 2 == 0) {
            // Even index (0, 2, 4) → Alphabet
            val char = ('A'..'Z').random()
            builder.append(char)
        } else {
            // Odd index (1, 3, 5) → Digit
            val digit = Random.nextInt(0, 10)
            builder.append(digit)
        }
    }
    return builder.toString()
}
