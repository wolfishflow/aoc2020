package day1

import java.io.File

/*
--- Part One ---

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
/*

--- Part Two ---
The Elves in accounting are thankful for your help;
one of them even offers you a starfish coin they had left over from a past vacation.
They offer you a second one if you can find three numbers in your expense report that meet the same criteria.

Using the above example again, the three entries that sum to 2020 are 979, 366, and 675.
Multiplying them together produces the answer, 241861950.

In your expense report, what is the product of the three entries that sum to 2020?

*/

fun main() {
    println(testWithSampleArray())
    println(runPart1())
    println(runPart2())
}

fun testWithSampleArray(): Int {
    val input = listOf(1721, 979, 366, 299, 675, 1256)
    val output = handleInput(input)

    return output.first * output.second
}

fun runPart1(): Int {
    val input = File("src/day1/input").useLines { files ->
        files.toList().map {
            it.toInt()
        }
    }
    val output = handleInput(input)

    return output.first * output.second
}

fun runPart2(): Int {
    val input = File("src/day1/input").useLines { files ->
        files.toList().map {
            it.toInt()
        }
    }
    val output = handleInputTriple(input)

    return output.first * output.second * output.third
}

fun handleInput(input: List<Int>): Pair<Int, Int> {
    //Naive solution - dbl for loop
    for (outerNumber in input) {
        //First Loop
        if (outerNumber < 2020) {
            for (innerNumber in input) {
                //Second Loop
                if (solveFor2020(outerNumber, innerNumber)) {
                    return Pair(outerNumber, innerNumber)
                }
            }
        }
    }

    return Pair(0, 0)
}

fun handleInputTriple(input: List<Int>): Triple<Int, Int, Int> {
    //Naive solution - dbl for loop
    for (firstLoop in input) {
        //First Loop
        if (firstLoop < 2020) {
            for (secondLoop in input) {
                //Second Loop
                if (firstLoop + secondLoop < 2020) {
                    for (thirdLoop in input) {
                        if (solveFor2020(firstLoop, secondLoop, thirdLoop)) {
                            return Triple(firstLoop, secondLoop, thirdLoop)
                        }
                    }
                }

            }
        }
    }

    return Triple(0, 0, 0)
}

fun solveFor2020(firstValue: Int, secondValue: Int): Boolean {
    return firstValue + secondValue == 2020
}

fun solveFor2020(firstValue: Int, secondValue: Int, thirdValue: Int): Boolean {
    return firstValue + secondValue + thirdValue == 2020
}

