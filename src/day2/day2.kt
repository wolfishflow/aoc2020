package day2

import java.io.File

/*

To try to debug the problem, they have created a list (your puzzle input) of passwords
(according to the corrupted database) and the corporate policy when that password was set.

For example, suppose you have the following list:

1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc

Each line gives the password policy and then the password.
The password policy indicates the lowest and highest number of times a given letter must appear for the password to be valid.
For example, 1-3 a means that the password must contain a at least 1 time and at most 3 times.

In the above example, 2 passwords are valid. The middle password, cdefg, is not;
it contains no instances of b, but needs at least 1.
The first and third passwords are valid: they contain one a or nine c, both within the limits of their respective policies.

How many passwords are valid according to their policies?
*/

/*

--- Part Two ---
While it appears you validated the passwords correctly, they don't seem to be what the Official Toboggan Corporate Authentication System is expecting.

The shopkeeper suddenly realizes that he just accidentally explained the password policy rules from his old job at the sled rental place down the street!
The Official Toboggan Corporate Policy actually works a little differently.

Each policy actually describes two positions in the password, where 1 means the first character, 2 means the second character, and so on.
(Be careful; Toboggan Corporate Policies have no concept of "index zero"!) Exactly one of these positions must contain the given letter.
Other occurrences of the letter are irrelevant for the purposes of policy enforcement.

Given the same example list from above:

1-3 a: abcde is valid: position 1 contains a and position 3 does not.
1-3 b: cdefg is invalid: neither position 1 nor position 3 contains b.
2-9 c: ccccccccc is invalid: both position 2 and position 9 contain c.
How many passwords are valid according to the new interpretation of the policies?
*/

fun main() {
    testSample()
    runPart1()
}

fun testSample() {
    //Pattern of input line
    // NNN-NNN A: AAAAAAAAA
    //Where N == int 0-9, A == alpha value a-z
    //The min and max occurrence is unknown, but the conditional letter can only be 1 letter
    val sampleData = mutableListOf("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc")

    sampleData.map { line ->
        println(isPasswordValid(line))
        println("------------------")
    }

}
fun isPasswordValid( input: String): Boolean {
    val conditionSplit = input.split(":")
    val numericCondition = conditionSplit.first().split(" ").first()
    val letterCondition = conditionSplit.first().split(" ").last().trim().single()
    val password = conditionSplit.last()
    val minimum = numericCondition.split("-").first().toInt()
    val maximum = numericCondition.split("-").last().replace("-", "").toInt()

    println("Numeric Condition - $numericCondition, Letter Condition - $letterCondition, Password - $password, Min - $minimum & Max - $maximum")
//        //Naive Sln
//        //populate map and increment letters
//        val passwordMap = mutableMapOf<Char, Int>()
//        password.map { letter ->
//            passwordMap.put(letter, passwordMap.getOrDefault(letter, 0) + 1)
//        }
//
//        //Check map for key, and if it exists, check range for validity
//        if (passwordMap.containsKey(alphaCondition)) {
//            val value = passwordMap[alphaCondition]!!
//
//            if (value in minimum..maximum) {
//                println("true")
//            }
//        }

    //Filter the password by the letter condition, if the letter is within the range its valid
    val numberOfOccurrences = password.filter { letter -> letter == letterCondition }.length
    return numberOfOccurrences in minimum .. maximum
}

fun runPart1() {
    var validPasswords = 0
    File("src/day2/input").readLines().map { input ->
        if(isPasswordValid(input)) {
            validPasswords++
        }
    }

    println("Part1: # of valid passwords - $validPasswords")
}

