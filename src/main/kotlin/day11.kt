import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/11").readLines().map { it.toCharArray().map { c ->
		if (c == '.') Floor() else Seat(c == '#')
	} }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day11part1(input)
	val end1 = System.nanoTime()

	for (row in input)
		for (slot in row)
			slot.reset()

	val start2 = System.nanoTime()
	val n2 = day11part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

interface FerrySlot {
	fun isOccupied() = false
	fun applyNext() = false
	fun reset() {}
}
class Floor: FerrySlot
class Seat(var occupied: Boolean = false, var next: Boolean? = null): FerrySlot {
	override fun isOccupied() = occupied
	override fun applyNext(): Boolean {
		if (next != null) {
			occupied = next!!
			next = null
			return true
		}
		return false
	}
	override fun reset() {
		occupied = false
		next = null
	}
}

data class Dir(val dx: Int, val dy: Int)
val adjacent = listOf(
	Dir(-1, -1),
	Dir(-1, 0),
	Dir(-1, 1),
	Dir(0, -1),
	Dir(0, 1),
	Dir(1, -1),
	Dir(1, 0),
	Dir(1, 1)
)

fun checkVision(x: Int, y: Int, grid: List<List<FerrySlot>>, visionLimit: Int = 1, occupiedLimit: Int): Boolean {
	val yDim = 0..grid.lastIndex
	val xDim = 0..grid[0].lastIndex
	var occupiedCount = 0
	for (adj in adjacent) {
		var xa = x
		var ya = y
		var seeNothing = true
		var vision = 0
		while (seeNothing && (visionLimit < 0 || vision < visionLimit)) {
			vision++
			xa += adj.dx
			ya += adj.dy
			seeNothing = if (xa in xDim && ya in yDim) {
				if (grid[ya][xa] is Seat) {
					if (grid[ya][xa].isOccupied()) {
						occupiedCount++
						if (occupiedCount > occupiedLimit)
							return false
					}
					false
				} else true
			} else false
		}
	}
	return true
}

fun day11(grid: List<List<FerrySlot>>, vision: Int, toleranceToQuitSeat: Int): Int {
	var chaos = true
	while (chaos) {
		for (y in grid.indices) {
			for (x in grid[y].indices) {
				val slot = grid[y][x]
				if (slot is Seat) {
					if (slot.occupied) {
						if (!checkVision(x, y, grid, vision, toleranceToQuitSeat))
							slot.next = false
					} else {
						if (checkVision(x, y, grid, vision, 0))
							slot.next = true
					}
				}
			}
		}
		chaos = false
		for (row in grid)
			for (slot in row)
				if (slot.applyNext())
					chaos = true
	}
	var n = 0
	for (row in grid)
		for (slot in row)
			if (slot.isOccupied())
				n++
	return n
}

fun day11part1(grid: List<List<FerrySlot>>): Int {
	return day11(grid, 1, 3)
}

fun day11part2(grid: List<List<FerrySlot>>): Int {
	return day11(grid, -1, 4)
}
