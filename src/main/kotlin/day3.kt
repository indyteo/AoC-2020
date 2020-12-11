import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/3").readLines().map { it.toCharArray() }.toTypedArray()
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day3part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day3part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class Slope(val dx: Int = 3, val dy: Int = 1)

fun day3part1(map: Array<CharArray>, slope: Slope = Slope()): Int {
	var n = 0
	var x = 0
	var y = 0
	val targetY = map.lastIndex
	val maxX = map[0].size
	while (y < targetY) {
		x += slope.dx
		y += slope.dy
		if (x >= maxX)
			x -= maxX
		if (map[y][x] == '#')
			n++
	}
	return n
}

fun day3part2(map: Array<CharArray>): Long {
	var n: Long = 1
	val slopes = arrayOf(
		Slope(1, 1),
		Slope(3, 1),
		Slope(5, 1),
		Slope(7, 1),
		Slope(1, 2)
	)
	for (slope in slopes)
		n *= day3part1(map, slope)
	return n
}
