package day3

import java.io.File


/*

Due to the local geology, trees in this area only grow on exact integer coordinates in a grid.
You make a map (your puzzle input) of the open squares (.) and trees (#) you can see. For example:

..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#

These aren't the only trees, though; due to something you read about once involving arboreal genetics and biome stability,
the same pattern repeats to the right many times:
You start on the open square (.) in the top-left corner and need to reach the bottom (below the bottom-most row on your map).

The toboggan can only follow a few specific slopes (you opted for a cheaper model that prefers rational numbers);
start by counting all the trees you would encounter for the slope right 3, down 1:

From your starting position at the top-left, check the position that is right 3 and down 1.
Then, check the position that is right 3 and down 1 from there, and so on until you go past the bottom of the map.
*/
/*

--- Part Two ---
Time to check the rest of the slopes - you need to minimize the probability of a sudden arboreal stop, after all.

Determine the number of trees you would encounter if, for each of the following slopes,
you start at the top-left corner and traverse the map all the way to the bottom:

Right 1, down 1.
Right 3, down 1. (This is the slope you already checked.)
Right 5, down 1.
Right 7, down 1.
Right 1, down 2.
In the above example, these slopes would find 2, 7, 3, 4, and 2 tree(s) respectively; multiplied together, these produce the answer 336.

What do you get if you multiply together the number of trees encountered on each of the listed slopes?
*/

fun main() {
    runTest()
    runPart1()
    runPart2()
}

fun runTest() {
    //Convert "map" (lines) into 2D Array
    val testMap = mutableListOf(
            "..##.......", "#...#...#..", ".#....#..#.", "..#.#...#.#", ".#...##..#.", "..#.##.....",
            ".#.#.#....#", ".#........#", "#.##...#...", "#...##....#", ".#..#...#.#"
    )

    calculateCrashes(testMap, 3, 1)
}

fun runPart1() {
    val input = File("src/day3/input").readLines()
    calculateCrashes(input, 3, 1)
}

fun calculateCrashes(lineMap: List<String>, rightIncrement: Int, downIncrement: Int): Long {
    var numOfCrashes = 0
    var currentColumn = 0
    val rollOver = lineMap.first().length
    var skipFirstRow = true

    for (row in lineMap.indices step downIncrement) {

        if (skipFirstRow) {
            skipFirstRow = false
            continue
        }

        currentColumn = (currentColumn + rightIncrement) % rollOver

        if (lineMap[row][currentColumn] == '#') {
            numOfCrashes++
        }
    }

    println(numOfCrashes)
    return numOfCrashes.toLong()
}


fun runPart2() {
    val input = File("src/day3/input").readLines()

    val treesWithSlopeOf1by1 = calculateCrashes(input, 1,1)
    val treesWithSlopeOf3by1 = calculateCrashes(input, 3,1)
    val treesWithSlopeOf5by1 = calculateCrashes(input, 5,1)
    val treesWithSlopeOf7by1 = calculateCrashes(input, 7,1)
    val treesWithSlopeOf1by2 = calculateCrashes(input, 1,2)

    val productOfAllSlopes = treesWithSlopeOf1by1*treesWithSlopeOf3by1*treesWithSlopeOf5by1*treesWithSlopeOf7by1*treesWithSlopeOf1by2
    println("Day 3 Part 2: $productOfAllSlopes")
}
