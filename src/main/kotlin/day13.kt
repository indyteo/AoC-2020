import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/13").readLines()
	val input1 = input[0].toInt()
	var i = -1
	val input2 = input[1].split(",").map { i++; Bus(if (it == "x") -1 else it.toInt(), i) }.filter { it.id != -1 }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day13part1(input1, input2)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day13part2(input2)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class Bus(val id: Int, val index: Int)
fun day13part1(depart: Int, bus: List<Bus>): Int {
	val delay = mutableListOf<Int>()
	for (b in bus)
		delay.add(-(depart % b.id) + b.id)
	val i = delay.indexOf(delay.minOrNull()!!)
	return delay[i] * bus[i].id
}

fun day13part2(bus: List<Bus>): Long {
	var mod = 1.toBigInteger()
	for (b in bus)
		mod = mod.multiply(b.id.toBigInteger())

	var n = 0.toBigInteger()
	for (b in bus) {
		val a = (b.id - b.index).toBigInteger()
		val m = mod.divide(b.id.toBigInteger())
		val y = m.modInverse(b.id.toBigInteger())
		n = n.add(a.multiply(m).multiply(y))
	}

	return n.mod(mod).toLong()
}
