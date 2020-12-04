package day1

import java.io.File

/*
Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input);
apparently, something isn't quite adding up.

Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.

For example, suppose your expense report contained the following:

1721
979
366
299
675
1456
In this list, the two entries that sum to 2020 are 1721 and 299.
Multiplying them together produces 1721 * 299 = 514579, so the correct answer is 514579.
*/


fun main() {
    println(testWithSampleArray())
    println(testWithInput())
}

fun testWithSampleArray(): Int {
    val input = listOf(1721, 979, 366, 299, 675, 1256)
    val output = handleInput(input)

    return output.first * output.second
}

fun testWithInput(): Int {
    val input = File("src/day1/input").useLines { files ->
        files.toList().map {
            it.toInt()
        }
    }
    val output = handleInput(input)

    return output.first * output.second
}

fun handleInput(input: List<Int>): Pair<Int, Int> {
    //Naive solution - dbl for loop
    for (outerNumber in input) {
        //First Loop
        for (innerNumber in input) {
            //Second Loop
            if (solveFor2020(outerNumber, innerNumber)) {
                return Pair(outerNumber, innerNumber)
            }
        }
    }

    return Pair(0, 0)
}

fun solveFor2020(firstValue: Int, secondValue: Int): Boolean {
    return firstValue + secondValue == 2020
}

