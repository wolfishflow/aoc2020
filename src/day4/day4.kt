package day4

import java.io.File

/*

The automatic passport scanners are slow because they're having trouble detecting which passports have all required fields.
The expected fields are as follows:

byr (Birth Year)
iyr (Issue Year)
eyr (Expiration Year)
hgt (Height)
hcl (Hair Color)
ecl (Eye Color)
pid (Passport ID)
cid (Country ID)
Passport data is validated in batch files (your puzzle input).
Each passport is represented as a sequence of key:value pairs separated by spaces or newlines.
Passports are separated by blank lines.

Count the number of valid passports - those that have all required fields.
Treat cid as optional. In your batch file, how many passports are valid?


--- Part Two ---
The line is moving more quickly now, but you overhear airport security talking about how passports with invalid data are getting through. Better add some data validation, quick!

You can continue to ignore the cid field, but each other field has strict rules about what values are valid for automatic validation:

byr (Birth Year) - four digits; at least 1920 and at most 2002.
iyr (Issue Year) - four digits; at least 2010 and at most 2020.
eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
hgt (Height) - a number followed by either cm or in:
If cm, the number must be at least 150 and at most 193.
If in, the number must be at least 59 and at most 76.
hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
pid (Passport ID) - a nine-digit number, including leading zeroes.
cid (Country ID) - ignored, missing or not.

*/

const val byr = "byr"
const val iyr = "iyr"
const val eyr = "eyr"
const val hgt = "hgt"
const val hcl = "hcl"
const val ecl = "ecl"
const val pid = "pid"
const val cid = "cid"

fun main() {
//    runTest()
//    runPart1()
//    runPart2()
    runTest2()
}

fun runTest() {
    //Build sample data
    val data = mutableListOf<HashMap<String, String>>()
    File("src/day4/sample").useLines { fileOutput ->
        buildPassportList(fileOutput, data)
    }

    println("Test Data: # of Valid Passports: ${getNumberOfValidPassports(data)}")
}


fun runPart1() {
    val data = mutableListOf<HashMap<String, String>>()
    File("src/day4/input").useLines { fileOutput ->
        buildPassportList(fileOutput, data)
    }

    println("Part 1: # of Valid Passports: ${getNumberOfValidPassports(data)}")
}

fun runTest2() {
    val data = mutableListOf<HashMap<String, String>>()
    File("src/day4/sample2").useLines { fileOutput ->
        buildPassportList(fileOutput, data)
    }

    println("Test Data: # of Valid Passports with Valid Fields (should be 4): ${getNumberOfValidPassportsWithValidFields(data)}")
}

fun runPart2() {
    val data = mutableListOf<HashMap<String, String>>()
    File("src/day4/input").useLines { fileOutput ->
        buildPassportList(fileOutput, data)
    }

    println("Part 2: # of Valid Passports with Valid Fields: ${getNumberOfValidPassportsWithValidFields(data)}")
}


private fun buildPassportList(fileOutput: Sequence<String>, data: MutableList<HashMap<String, String>>): Boolean {
    var passport = HashMap<String, String>()

    fileOutput.iterator().forEachRemaining { line ->
        if (line == "") {
            data.add(passport)
            //New passport object
            passport = HashMap()
        } else {
            // Parse the line into KV(s)
            val splitLine = line.split(" ")
            splitLine.forEach {
                passport.put(it.split(":").first(), it.split(":").last())
            }
        }
    }
    return data.add(passport)
}

fun getNumberOfValidPassports(data: MutableList<HashMap<String, String>>): Int {
    var numberOfValidPassports = 0

    data.forEach {
        if (isValidPassport(it)) {
            numberOfValidPassports++
        }
    }
    return numberOfValidPassports
}

fun getNumberOfValidPassportsWithValidFields(data: MutableList<HashMap<String, String>>): Int {
    var numberOfValidPassports = 0

    data.forEach {
        if (isValidPassportWithValidFields(it)) {
            numberOfValidPassports++
        }
    }
    return numberOfValidPassports
}

fun isValidPassport(passport: HashMap<String, String>): Boolean {
    //Since cid is optional - we can add the field if it exists
    //Check hashmap for all the required fields, and ignore cid
    return passport.containsKey(byr) &&
            passport.containsKey(iyr) &&
            passport.containsKey(eyr) &&
            passport.containsKey(hgt) &&
            passport.containsKey(hcl) &&
            passport.containsKey(ecl) &&
            passport.containsKey(pid)
}

fun isValidByr(value: String): Boolean {
    return value.toInt() in 1920..2002
}

fun isValidIyr(value: String): Boolean {
    return value.toInt() in 2010..2020
}

fun isValidEyr(value: String): Boolean {
    return value.toInt() in 2020..2030
}

fun isValidHgt(value: String): Boolean {
    return if (value.contains("cm")) {
        value.replace("cm", "").toInt() in 150..193
    } else {
        value.replace("in", "").toInt() in 59..76
    }
}

fun isValidHcl(value: String): Boolean {
    var hasOnlyHex = false

    if (value.length == 7&& value[0] == '#') {
        value.replace("#", "")
        value.toCharArray().forEach {
            if (it in '0' .. '9' || it in 'a'.. 'f') {
                hasOnlyHex = true
            }
        }

        return hasOnlyHex
    }

    return false
}

fun isValidEcl(value: String): Boolean {
    return value.contentEquals("amb") ||
            value.contentEquals("blu") ||
            value.contentEquals("brn") ||
            value.contentEquals("gry") ||
            value.contentEquals("grn") ||
            value.contentEquals("hzl") ||
            value.contentEquals("oth")
}

fun isValidPid(value: String): Boolean {
    if (value.length == 9) {
        value.toCharArray().iterator().forEach {
            if (it !in '0'..'9') {
                return false
            }
            return true
        }
    }

    return false
}

fun isValidPassportWithValidFields(passport: HashMap<String, String>): Boolean {
    //Since cid is optional - we can add the field if it exists
    //Check hashmap for all the required fields, and ignore cid
    return passport.containsKey(byr) && isValidByr(passport[byr]!!) &&
            passport.containsKey(iyr) && isValidIyr(passport[iyr]!!) &&
            passport.containsKey(eyr) && isValidEyr(passport[eyr]!!) &&
            passport.containsKey(hgt) && isValidHgt(passport[hgt]!!) &&
            passport.containsKey(hcl) && isValidHcl(passport[hcl]!!) &&
            passport.containsKey(ecl) && isValidEcl(passport[ecl]!!) &&
            passport.containsKey(pid) && isValidPid(passport[pid]!!)
}


