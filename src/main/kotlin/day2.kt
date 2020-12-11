import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/2").readLines().map {
		val entry = "(?<min>\\d+)-(?<max>\\d+) (?<char>[a-z]): (?<pass>[a-z]+)".toRegex().matchEntire(it)!!.groups
		Entry(entry["min"]!!.value.toInt(), entry["max"]!!.value.toInt(), entry["char"]!!.value[0], entry["pass"]!!.value)
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day2part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day2part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class Entry(val min: Int, val max: Int, val char: Char, val pass: String)

fun day2part1(entries: List<Entry>): Int {
	var n = 0
	for (entry in entries) {
		var i = 0
		for (c in entry.pass)
			if (c == entry.char)
				i++
		if (i >= entry.min && i <= entry.max)
			n++
	}
	return n
}

fun day2part2(entries: List<Entry>): Int {
	var n = 0
	for (entry in entries)
		if ((entry.pass[entry.min - 1] == entry.char) xor (entry.pass[entry.max - 1] == entry.char))
			n++
	return n
}
