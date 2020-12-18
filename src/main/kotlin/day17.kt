import java.io.File

fun main() {
	val start = System.nanoTime()
	val input1 = ConwayDimension()
	val input2 = ConwayDimension(withW = true)
	var i = 0
	var j: Int
	File("input/17").readLines().forEach {
		j = 0
		it.toCharArray().forEach { c ->
			if (c == '#') {
				input1.cubes.add(ConwayCube(i, j, active = true))
				input2.cubes.add(ConwayCube(i, j, active = true))
			}
			j++
		}
		i++
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day17part1(input1)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day17part2(input2)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class ConwayCube(val x: Int, val y: Int, val z: Int = 0, var w: Int = 0, var active: Boolean = false, var next: Boolean? = null) {
	fun applyNext() {
		if (next != null) {
			active = next!!
			next = null
		}
	}
}
data class ConwayDimension(val cubes: MutableSet<ConwayCube> = mutableSetOf(), val withW: Boolean = false) {
	private fun getCube(x: Int, y: Int, z: Int, w: Int = 0): ConwayCube? {
		for (cube in cubes)
			if (cube.x == x && cube.y == y && cube.z == z && cube.w == w)
				return cube
		return null
	}
	fun isActive(x: Int, y: Int, z: Int, w: Int = 0): Boolean = getCube(x, y, z, w)?.active ?: false
	fun next(x: Int, y: Int, z: Int, w: Int = 0, next: Boolean) {
		val cube = getCube(x, y, z, w)
		if (cube == null)
			cubes.add(ConwayCube(x, y, z, w, next = next))
		else
			cube.next = next
	}
	private val minX get() = cubes.map { it.x }.minOrNull()!! - 1
	private val maxX get() = cubes.map { it.x }.maxOrNull()!! + 1
	val x get() = minX..maxX
	private val minY get() = cubes.map { it.y }.minOrNull()!! - 1
	private val maxY get() = cubes.map { it.y }.maxOrNull()!! + 1
	val y get() = minY..maxY
	private val minZ get() = cubes.map { it.z }.minOrNull()!! - 1
	private val maxZ get() = cubes.map { it.z }.maxOrNull()!! + 1
	val z get() = minZ..maxZ
	private val minW get() = cubes.map { it.w }.minOrNull()!! - 1
	private val maxW get() = cubes.map { it.w }.maxOrNull()!! + 1
	val w get() = if (withW) minW..maxW else 0..0
}
data class Dir3or4D(val dx: Int, val dy: Int, val dz: Int, val dw: Int = 0)
val neighbors3D = generateNeighbors()
val neighbors4D = generateNeighbors(true)
fun generateNeighbors(withW: Boolean = false): MutableSet<Dir3or4D> {
	val neighbors = mutableSetOf<Dir3or4D>()
	for (i in -1..1)
		for (j in -1..1)
			for (k in -1..1)
				for (w in if (withW) -1..1 else 0..0)
					if (i != 0 || j != 0 || k != 0 || w != 0)
						neighbors.add(Dir3or4D(i, j, k, w))
	return neighbors
}

fun day17(conway: ConwayDimension, withW: Boolean = false): Int {
	for (i in 0..5) {
		for (x in conway.x) {
			for (y in conway.y) {
				for (z in conway.z) {
					for (w in conway.w) {
						var activeNeighbors = 0
						for (neighbor in if (withW) neighbors4D else neighbors3D)
							if (conway.isActive(x + neighbor.dx, y + neighbor.dy, z + neighbor.dz, w + neighbor.dw))
								activeNeighbors++
						if (conway.isActive(x, y, z, w)) {
							if (activeNeighbors !in 2..3)
								conway.next(x, y, z, w, next = false)
						} else if (activeNeighbors == 3)
							conway.next(x, y, z, w, next = true)
					}
				}
			}
		}
		for (cube in conway.cubes)
			cube.applyNext()
	}
	var n = 0
	for (cube in conway.cubes)
		if (cube.active)
			n++
	return n
}

fun day17part1(conway: ConwayDimension): Int {
	return day17(conway)
}

fun day17part2(conway: ConwayDimension): Int {
	return day17(conway, true)
}
