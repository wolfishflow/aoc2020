package day3

import day2.isPasswordXorValid
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



fun main() {
    runTest()
    runPart1()
}

fun runTest() {
    //Convert "map" (lines) into 2D Array
    val testMap = mutableListOf(
            "..##.......", "#...#...#..", ".#....#..#.", "..#.#...#.#", ".#...##..#.", "..#.##.....",
            ".#.#.#....#", ".#........#", "#.##...#...", "#...##....#", ".#..#...#.#"
    )

    calculateCrashes(testMap)
}

fun runPart1() {
    val input = File("src/day3/input").readLines()
    calculateCrashes(input)
}

fun calculateCrashes(lineMap: List<String>) {
    val tree = '#'
    var numOfCrashes = 0
    var currentColumn = 0
    val rollOver = lineMap.first().length
    var skipFirstRow = true
    lineMap.map { line ->

        if (skipFirstRow) {
            skipFirstRow = false
        } else {
            currentColumn = (currentColumn + 3) % rollOver

            if (line[currentColumn] == tree) {
                numOfCrashes++
            }
        }
    }

    println(numOfCrashes)
}
