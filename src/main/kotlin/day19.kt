import java.io.File

val ruleRegistry = mutableMapOf<Int, Rule>()
val groupsRegistry = mutableListOf<RuleGroup>()
fun main() {
	val start = System.nanoTime()
	val rawInput = File("input/19").readText().split("\n\n")
	rawInput[0].lines().forEach {
		val rule = it.split(": ")
		val r = if (rule[1][0] == '"' && rule[1][2] == '"')
			FinalRule(rule[0].toInt(), rule[1][1])
		else
			IntermediateRule(rule[0].toInt(), rule[1].split(" | ").map { g ->
				val group = RuleGroup(g.split(' ').map { r -> r.toInt() })
				groupsRegistry.add(group)
				group
			})
		ruleRegistry[r.index] = r
	}
	for (group in groupsRegistry)
		for (i in group.rulesIndex)
			group.rules.add(ruleRegistry[i]!!)
	val input = rawInput[1].lines()
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day19part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day19part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

abstract class Rule(val index: Int) {
	val regex: Regex get() = "^$regexStr$".toRegex()
	abstract val regexStr: String
}

class FinalRule(index: Int, private val char: Char): Rule(index) {
	override val regexStr: String get() = char.toString()
}

class IntermediateRule(index: Int, private val subrules: List<RuleGroup>): Rule(index) {
	override val regexStr: String get() = subrules.joinToString("|", "(?:", ")")
}

data class RuleGroup(val rulesIndex: List<Int>, val rules: MutableList<Rule> = mutableListOf()) {
	override fun toString() = rules.joinToString("") { it.regexStr }
}

fun day19part1(messages: List<String>): Int {
	var n = 0
	val rule = ruleRegistry[0]!!.regex
	for (message in messages)
		if (message.matches(rule))
			n++
	return n
}

fun day19part2(messages: List<String>): Int {
	var n = 0
	val rule31 = ruleRegistry[31]!!.regexStr
	val rule42 = ruleRegistry[42]!!.regexStr
	for (message in messages) {
		var msg = message
		var max = -1
		while (msg.matches("^$rule42.*$".toRegex())) {
			msg = msg.replace("^$rule42(.*)$".toRegex(), "$1")
			max++
		}
		if (max > 0 && msg.matches("^$rule31{1,$max}$".toRegex()))
			n++
	}
	return n
}
