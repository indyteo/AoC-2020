import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/5").readLines().map { it.replace("[BR]".toRegex(), "1").replace("[FL]".toRegex(), "0").toInt(2) }.sorted()
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day5part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day5part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

fun day5part1(seats: List<Int>): Int {
	return seats.last()
}

fun day5part2(seats: List<Int>): Int {
	var previous = seats.first()
	for (i in 1 until seats.size) {
		if (seats[i] - previous == 2)
			return previous + 1
		previous = seats[i]
	}
	return -1
}
