import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/9").readLines().map { it.toLong() }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day9part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day9part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

fun verifyNumber(xmas: List<Long>, index: Int, target: Long): Boolean {
	for (i in 1..25) {
		val n1 = xmas[index - i]
		for (j in (i + 1)..25) {
			val n2 = xmas[index - j]
			if (n1 + n2 == target)
				return true
		}
	}
	return false
}

fun day9part1(xmas: List<Long>): Long {
	for (i in 25 until xmas.size) {
		val n = xmas[i]
		if (!verifyNumber(xmas, i, n))
			return n
	}
	return -1
}

fun day9part2(xmas: List<Long>): Long {
	val n = day9part1(xmas)
	for (i in xmas.indices) {
		var j = 0
		val xmas1 = xmas[i]
		var sum = xmas1
		val values = mutableListOf(xmas1)
		while (sum < n) {
			j++
			val xmas2 = xmas[i + j]
			sum += xmas2
			values.add(xmas2)
		}
		if (sum == n && values.size >= 2)
			return values.minOrNull()!! + values.maxOrNull()!!
	}
	return -1
}
