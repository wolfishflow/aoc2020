package day8

import java.io.File

/*

The boot code is represented as a text file with one instruction per line of text.
Each instruction consists of an operation (acc, jmp, or nop) and an argument (a signed number like +4 or -20).

acc increases or decreases a single global value called the accumulator by the value given in the argument.
For example, acc +7 would increase the accumulator by 7. The accumulator starts at 0.
After an acc instruction, the instruction immediately below it is executed next.
jmp jumps to a new instruction relative to itself. The next instruction to execute is found using the
argument as an offset from the jmp instruction; for example, jmp +2 would skip the next instruction,
jmp +1 would continue to the instruction immediately below it, and jmp -20 would cause the instruction 20 lines above to be executed next.
nop stands for No OPeration - it does nothing. The instruction immediately below it is executed next.
For example, consider the following program:

nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6
These instructions are visited in this order:

nop +0  | 1
acc +1  | 2, 8(!)
jmp +4  | 3
acc +3  | 6
jmp -3  | 7
acc -99 |
acc +1  | 4
jmp -4  | 5
acc +6  |
First, the nop +0 does nothing. Then, the accumulator is increased from 0 to 1 (acc +1) and
jmp +4 sets the next instruction to the other acc +1 near the bottom.
After it increases the accumulator from 1 to 2, jmp -4 executes, setting the next instruction to the only acc +3.
It sets the accumulator to 5, and jmp -3 causes the program to continue back at the first acc +1.

This is an infinite loop: with this sequence of jumps, the program will run forever.
The moment the program tries to run any instruction a second time, you know it will never terminate.

Immediately before the program would run an instruction a second time, the value in the accumulator is 5.

Run your copy of the boot code. Immediately before any instruction is executed a second time, what value is in the accumulator?


*/


// increase the global var
const val ACC = "acc"

// jump above/below by the value
const val JMP = "jmp"

// go to next line
const val NOP = "nop"

var accumulator = 0
var lineNumber = 0
var invertInstruction = true

fun main() {
//    runTest()
//    runPart1()
    runPart2()
}

//Build a list from the input

fun runTest() {
    val input = File("src/day8/test").readLines()

    processInput(input)
}

fun runPart1() {
    println("Part 1 ------------------------")
    val input = File("src/day8/input").readLines()

    processInput(input)
}

fun runPart2() {
    println("Part 2 ------------------------")
    val input = File("src/day8/input").readLines()

    processInputPart2(input)
}

fun processInput(input: List<String>) {
    val processedInstructions = Array(input.size) { "" }
    var noRepeatedInstructions = true
    //Reset for any subsequent run

    accumulator = 0
    lineNumber = 0

    while (noRepeatedInstructions) {
        //get line
        val line = input[lineNumber]
        println(line)
        //check if instruction is in processed list
        if (processedInstructions[lineNumber] == line) {
            noRepeatedInstructions = false
            println("Accumulator is: $accumulator")
        }

        //break down line
        val instruction = line.substring(0..2)
        val sign = line.substring(4..4)
        val value = line.substring(5 until line.length)

        //add instruction to processed list
        processedInstructions[lineNumber] = line

        //perform instruction
        performInstruction(instruction, sign, value)
    }

}

fun performInstruction(instruction: String, sign: String, value: String) {
    when (instruction) {
        ACC -> {
            when (sign) {
                "+" -> {
                    accumulator += value.toInt()
                }
                "-" -> {
                    accumulator -= value.toInt()
                }
            }
            lineNumber++
        }
        JMP -> {
            when (sign) {
                "+" -> {
                    lineNumber += value.toInt()
                }
                "-" -> {
                    lineNumber -= value.toInt()
                }
            }
        }
        NOP -> {
            //NOOP
            lineNumber++
        }
    }
}


fun processInputPart2(input: List<String>) {
    var stillInfiniteLoop = true
    val testedJmpAndNopInstructions = Array(input.size) { "" }

    while (stillInfiniteLoop) {
        val processedInstructions = Array(input.size) { "" }
        var noRepeatedInstructionsLoop = true
        val cleanList = input.toMutableList()
        //Reset for any subsequent run
        accumulator = 0
        lineNumber = 0
        invertInstruction = true

        while (noRepeatedInstructionsLoop) {
            //get line
            var line = cleanList[lineNumber]
            //println(line)

            //break down line
            var instruction = line.substring(0..2)
            var sign = line.substring(4..4)
            var value = line.substring(5 until line.length)

            //if this line is a nop/jmp that should change, do it now
            if((instruction == "nop" || instruction == "jmp") && invertInstruction && testedJmpAndNopInstructions[lineNumber] != line ) {

                testedJmpAndNopInstructions[lineNumber] = line

                if (instruction == "nop") {
                    line = line.replace("nop", "jmp")
                    instruction = instruction.replace("nop", "jmp")
                } else {
                    line = line.replace("jmp", "nop")
                    instruction = instruction.replace("jmp", "nop")
                }
                cleanList[lineNumber] = line
                invertInstruction = false
                println("Line was modified $line")
            }

            //check if instruction is in processed list
            if (processedInstructions[lineNumber] == line) {
                noRepeatedInstructionsLoop = false
                println("Accumulator is: $accumulator")
            } else {
                //add instruction to processed list
                processedInstructions[lineNumber] = line

                //perform instruction
                performInstruction(instruction, sign, value)

                if (lineNumber == processedInstructions.size) {
                    println(accumulator)
                    noRepeatedInstructionsLoop = false
                    stillInfiniteLoop = false
                }
            }
        }
    }
}
