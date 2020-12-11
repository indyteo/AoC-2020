import java.io.File

fun main() {
	val start = System.nanoTime()
	val numbers = File("input/1").readLines().map { it.toInt() }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day1part1(numbers)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day1part2(numbers)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

fun day1part1(numbers: List<Int>): Int {
	val length = numbers.size
	for (i in numbers.indices) {
		val n1 = numbers[i]
		for (j in i + 1 until length) {
			val n2 = numbers[j]
			if (n1 + n2 == 2020)
				return n1 * n2
		}
	}
	return -1
}

fun day1part2(numbers: List<Int>): Int {
	val length = numbers.size
	for (i in numbers.indices) {
		val n1 = numbers[i]
		for (j in i + 1 until length) {
			val n2 = numbers[j]
			val n12 = n1 + n2
			if (n12 < 2020) {
				for (k in j + 1 until length) {
					val n3 = numbers[k]
					if (n12 + n3 == 2020)
						return n1 * n2 * n3
				}
			}
		}
	}
	return -1
}
