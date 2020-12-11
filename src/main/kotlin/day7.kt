import java.io.File

val contentParse = "(?<amount>\\d) (?<bag>[a-z ]+) bags?".toRegex()
const val c = "\\d [a-z ]+ bags?"
val bagParse = "(?<color>[a-z ]+) bags contain (?<content>(no other bags|$c(, $c)*)).".toRegex()
val registry = HashMap<String, Bag>()
fun main() {
	val start = System.nanoTime()
	val input = File("input/7").readLines().map {
		val parsedBag = bagParse.matchEntire(it)!!.groups
		val color = parsedBag["color"]!!.value
		val bag = Bag(color, parsedBag["content"]!!.value)
		registry[color] = bag
		bag
	}
	for (bag in input) {
		if (bag.rawContent != "no other bags") {
			val parsedContent = bag.rawContent.split(", ").map { contentParse.matchEntire(it)!!.groups }
			for (pContent in parsedContent)
				bag.content.add(BagContent(pContent["amount"]!!.value.toInt(), registry[pContent["bag"]!!.value]!!))
		}
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day7part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day7part2()
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class Bag(val color: String, val rawContent: String, val content: MutableList<BagContent> = mutableListOf()) {
	fun canContains(bagColor: String): Boolean {
		for (c in content)
			if (c.bag.color == bagColor || c.bag.canContains(bagColor))
				return true
		return false
	}

	fun countNestedBags(): Int {
		var n = 0
		for (c in content)
			n += c.amount * (1 + c.bag.countNestedBags())
		return n
	}
}
data class BagContent(val amount: Int, val bag: Bag)
fun day7part1(bags: List<Bag>): Int {
	var n = 0
	for (bag in bags)
		if (bag.canContains("shiny gold"))
			n++
	return n
}

fun day7part2() = registry["shiny gold"]!!.countNestedBags()
