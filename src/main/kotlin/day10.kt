import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/10").readLines().map { Adapter(it.toInt()) }.sortedBy { it.jolt }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day10part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day10part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class Adapter(val jolt: Int, var count: Long = 0)
fun day10part1(adapters: List<Adapter>): Int {
	val n = mutableListOf(0, 0, 1)
	var jolt = 0
	for (adapter in adapters) {
		n[adapter.jolt - jolt - 1]++
		jolt = adapter.jolt
	}
	return n[0] * n[2]
}

fun day10part2(adapters: List<Adapter>): Long {
	adapters.last().count = 1
	for (i in adapters.lastIndex - 1 downTo 0) {
		val adapter = adapters[i]
		for (j in 1..3) {
			if (i + j < adapters.size && adapters[i + j].jolt <= adapter.jolt + 3)
				adapter.count += adapters[i + j].count
			else
				break
		}
	}
	val plug = Adapter(0)
	for (i in 0..2) {
		if (adapters[i].jolt <= 3)
			plug.count += adapters[i].count
		else
			break
	}
	return plug.count
}
