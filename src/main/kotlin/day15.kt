import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/15").readText().split(",").map { it.toInt() }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day15part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day15part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

fun day15(start: List<Int>, target: Int): Int {
	val memory = HashMap<Int, Int>()
	var i = 0
	while (i < start.size) {
		memory[start[i]] = i
		i++
	}
	var current = 0
	var next: Int
	while (i < target - 1) {
		next = if (memory.containsKey(current)) i - memory[current]!! else 0
		memory[current] = i
		current = next
		i++
	}
	return current
}
fun day15part1(start: List<Int>): Int {
	return day15(start, 2020)
}

fun day15part2(start: List<Int>): Int {
	return day15(start, 30000000)
}
