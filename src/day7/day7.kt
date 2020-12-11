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

*/


fun main() {
    runPart1()
}


fun runPart1() {
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
    val masterBagList = mutableListOf<Bag>()
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

    println(validSetOfTravelledBags.size)
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
