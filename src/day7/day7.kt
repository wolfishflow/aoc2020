package day7

import java.io.File


/*

For example, consider the following rules:

light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.
These rules specify the required contents for 9 bag types.
In this example, every faded blue bag is empty, every vibrant plum bag contains 11 bags
(5 faded blue and 6 dotted black), and so on.

You have a shiny gold bag. If you wanted to carry it in at least one other bag,
how many different bag colors would be valid for the outermost bag?
 (In other words: how many colors can, eventually, contain at least one shiny gold bag?)

In the above rules, the following options would be available to you:

A bright white bag, which can hold your shiny gold bag directly.
A muted yellow bag, which can hold your shiny gold bag directly, plus some other bags.
A dark orange bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny gold bag.
A light red bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny gold bag.
So, in this example, the number of bag colors that can eventually contain at least one shiny gold bag is 4.

How many bag colors can eventually contain at least one shiny gold bag?


--- Part Two ---
It's getting pretty expensive to fly these days - not because of ticket prices, but because of the ridiculous number of bags you need to buy!

Consider again your shiny gold bag and the rules from the above example:

faded blue bags contain 0 other bags.
dotted black bags contain 0 other bags.
vibrant plum bags contain 11 other bags: 5 faded blue bags and 6 dotted black bags.
dark olive bags contain 7 other bags: 3 faded blue bags and 4 dotted black bags.
So, a single shiny gold bag must contain 1 dark olive bag (and the 7 bags within it) plus 2 vibrant plum bags (and the 11 bags within each of those): 1 + 1*7 + 2 + 2*11 = 32 bags!

Of course, the actual rules have a small chance of going several levels deeper than this example; be sure to count all of the bags, even if the nesting becomes topologically impractical!

Here's another example:

shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags.
In this example, a single shiny gold bag must contain 126 other bags.

How many individual bags are required inside your single shiny gold bag?

*/

var sum = 0
var level = 1
val masterBagList = mutableListOf<Bag>()

fun main() {
    runPart1And2()
}


fun runPart1And2() {
    val file = File("src/day7/input").readLines().map {
        it.replace("bags", "bag")
    }
    buildMasterBagList(file)
}

//input is a list of rules
// objective is to find a bag that leads to a shiny gold bag

// in order to come up with all possibilities - first identify all bags that can contains a shiny gold bag.
// then shake them till you get to the top


data class Bag(val name: String, val bagsContained: HashMap<String, Int>)

fun buildMasterBagList(rules: List<String>) {
    // chart all rules into master bag list

    rules.map { rule ->
        masterBagList.add(getBagFromRule(rule))
    }

    println(masterBagList)
    val tempSetOfTravelledBags = mutableSetOf<Bag>()
    val validSetOfTravelledBags = mutableSetOf<Bag>()
    // iterate master bag list and recurse till you hit shiny gold add temp set to master set
    // - if not clear the temp set thats being populated as you delve
    masterBagList.map { bag ->
        searchForBag("shiny gold bag", bag, masterBagList, tempSetOfTravelledBags, validSetOfTravelledBags)
    }

    println("Part1: ${validSetOfTravelledBags.size}")
    val shinyGoldBag = masterBagList.find { bag -> bag.name == "shiny gold bag" }!!
    sumOfAllBagsFromShinyGoldBag(shinyGoldBag, 1)
    println("Part2: $sum")
}


fun searchForBag(
    nameOfBagToFind: String,
    bag: Bag,
    masterBagList: MutableList<Bag>,
    tempSetOfTravelledBags: MutableSet<Bag>,
    validSetOfTravelledBags: MutableSet<Bag>
) {
    tempSetOfTravelledBags.add(bag)

    //Dead End
    if (bag.bagsContained.isEmpty()) {
        tempSetOfTravelledBags.remove(bag)
        return
    }


    bag.bagsContained.map { containedBag ->
        masterBagList.find { bag -> bag.name == containedBag.key }?.let { foundBag ->

            //Base case
            if (foundBag.name == nameOfBagToFind) {
                validSetOfTravelledBags.addAll(tempSetOfTravelledBags)
                return@map
            } else {
                searchForBag(
                    nameOfBagToFind,
                    foundBag,
                    masterBagList,
                    tempSetOfTravelledBags,
                    validSetOfTravelledBags
                )
            }
        }
    }
    tempSetOfTravelledBags.remove(bag)
}

fun getBagFromRule(rule: String): Bag {
    //Syntax is - XXX XXX bags contain x xxx xxx bags, x xxx xxx bags.
    //Syntax is - XXX XXX bags contain x xxx xxx bags.
    //Syntax is - XXX XXX bags contain no other bags.

    //Split the Holding Bag from the contents
    val holdingBagName = rule.split(" contain").first()

    //Split the contents from each other
    val remainingRule = rule.split(" contain").last().replace(".", "").split(",")
    //Trim all strings - add an "s" if required for a single content bag
    val contents = HashMap<String, Int>()

    if (remainingRule.contains(" no other bag")) {
        return Bag(holdingBagName, HashMap())
    }

    remainingRule.map { currentBag ->
        val name = currentBag.substring(3 until currentBag.length)
        val numberOfBags = currentBag.substring(1..1)
        contents.put(name, numberOfBags.toInt())
    }


    return Bag(holdingBagName, contents)
}


fun sumOfAllBagsFromShinyGoldBag(bag: Bag, amount: Int) {
    //initial bag is gold
    //inside bag would be 2 dark red

     bag.bagsContained.map { insideBag ->
         sum += insideBag.value * amount

         //Find the insideBag obj
         val derivedBag = masterBagList.find { bag -> bag.name == insideBag.key }!!
         if (derivedBag.bagsContained.isNotEmpty()) {
             sumOfAllBagsFromShinyGoldBag(derivedBag, insideBag.value * amount)
         }
     }
}

