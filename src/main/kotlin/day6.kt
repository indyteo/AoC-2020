import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/6").readText().split("\n\n".toRegex()).map { it.split("\n".toRegex()) }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day6part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day6part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

const val alphabet = "abcdefghijklmnopqrstuvwxyz"
fun day6part1(answers: List<List<String>>): Int {
	var n = 0
	for (answer in answers)
		for (c in alphabet)
			if (answer.stream().anyMatch { it.contains(c) })
				n++
	return n
}

fun day6part2(answers: List<List<String>>): Int {
	var n = 0
	for (answer in answers)
		for (c in alphabet)
			if (answer.stream().allMatch { it.contains(c) })
				n++
	return n
}
