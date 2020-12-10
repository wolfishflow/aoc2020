package day5

import java.io.File

/*
 Part 1

Instead of zones or groups, this airline uses binary space partitioning to seat people.
A seat might be specified like FBFBBFFRLR, where F means "front", B means "back", L means "left", and R means "right".

The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0 through 127).
Each letter tells you which half of a region the given seat is in. Start with the whole list of rows;
the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127).
The next letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.

For example, consider just the first seven characters of FBFBBFFRLR:

Start by considering the whole range, rows 0 through 127.
F means to take the lower half, keeping rows 0 through 63.
B means to take the upper half, keeping rows 32 through 63.
F means to take the lower half, keeping rows 32 through 47.
B means to take the upper half, keeping rows 40 through 47.
B keeps rows 44 through 47.
F keeps rows 44 through 45.
The final F keeps the lower of the two, row 44.
The last three characters will be either L or R; these specify exactly one of the 8 columns of seats on the plane (numbered 0 through 7).
The same process as above proceeds again, this time with only three steps. L means to keep the lower half, while R means to keep the upper half.

For example, consider just the last 3 characters of FBFBBFFRLR:

Start by considering the whole range, columns 0 through 7.
R means to take the upper half, keeping columns 4 through 7.
L means to take the lower half, keeping columns 4 through 5.
The final R keeps the upper of the two, column 5.
So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.

Every seat also has a unique seat ID: multiply the row by 8, then add the column. In this example, the seat has ID 44 * 8 + 5 = 357.

Here are some other boarding passes:

BFFFBBFRRR: row 70, column 7, seat ID 567.
FFFBBBFRRR: row 14, column 7, seat ID 119.
BBFFBBFRLL: row 102, column 4, seat ID 820.


--- Part Two ---
Ding! The "fasten seat belt" signs have turned on. Time to find your seat.

It's a completely full flight, so your seat should be the only missing boarding pass in your list.
However, there's a catch: some of the seats at the very front and back of the plane don't exist on this aircraft, so they'll be missing from your list as well.

Your seat wasn't at the very front or back, though; the seats with IDs +1 and -1 from yours will be in your list.

What is the ID of your seat?

*/


fun main() {
    runTest()
    runPart1andPart2()
}

fun runTest() {
    val testData = listOf("FBFBBFFRLR", "BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL")
    val seatIds = mutableListOf<Int>()
    testData.map {
        seatIds.add(getDetailsFromSeatData(it))
    }
    seatIds.sort()
    println("Max seatId is:${seatIds.maxOf { it }}")
}

fun runPart1andPart2() {
    val input = File("src/day5/input").useLines { files ->
        files.toList()
    }
    val seatIds = mutableListOf<Int>()

    input.map {
        seatIds.add(getDetailsFromSeatData(it))
    }
    seatIds.sort()
    println("Part 1: Max seatId is:${seatIds.maxOf { it }}")
    println("Part 2: Missing seatId is:${findMissingSeatId(seatIds)}")
}


fun getDetailsFromSeatData(seatData: String): Int {
    var upperBoundsRow = 127
    var lowerBoundsRow = 0

    var upperBoundsCol = 7
    var lowerBoundsCol = 0

    seatData.forEachIndexed { index, currentChar ->
        when (index) {
            0, 1, 2, 3, 4, 5, 6 -> {
                when (currentChar) {
                    'F' -> {
                        //Take lower half (towards 0)
                        upperBoundsRow = lowerBoundsRow + (upperBoundsRow - lowerBoundsRow) / 2
                    }
                    'B' -> {
                        //Take upper half (towards 127)
                        lowerBoundsRow = lowerBoundsRow + (upperBoundsRow - lowerBoundsRow) / 2 + 1
                    }
                }
            }
            7, 8, 9 -> {
                when (currentChar) {
                    'L' -> {
                        //Take lower half (towards 0)
                        upperBoundsCol = lowerBoundsCol + (upperBoundsCol - lowerBoundsCol) / 2
                    }
                    'R' -> {
                        //Take upper half (towards 7)
                        lowerBoundsCol = lowerBoundsCol + (upperBoundsCol - lowerBoundsCol) / 2 + 1
                    }
                }
            }
        }
    }
    val seatId = lowerBoundsRow * 8 + lowerBoundsCol
    //println("Row:$lowerBoundsRow Col:$lowerBoundsCol SeatId:$seatId")
    return seatId
}

fun findMissingSeatId(seatIds: MutableList<Int>) : Int {
    //iterate from start to end (avoiding OOB)
    for (currentIndex in 0 until seatIds.size - 1) {
        // the seats with IDs +1 and -1 from yours will be in your list
        // If we add +1 to the current indexed value , and if it doesn't equal the index+1 value , its a missing seat
        if (seatIds[currentIndex]+1 != seatIds[currentIndex+1]) {
            return seatIds[currentIndex]+1
        }
    }
    return 0
}



