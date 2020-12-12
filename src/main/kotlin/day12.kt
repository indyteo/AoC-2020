import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

fun main() {
	val start = System.nanoTime()
	val input = File("input/12").readLines().map { Instruction(it[0], it.substring(1).toInt()) }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day12part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day12part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

class Position(var x: Int = 0, var y: Int = 0, var rot: Int = 0)
data class Instruction(val action: Char, val value: Int)
fun day12part1(instructions: List<Instruction>): Int {
	val ship = Position()
	for (instruction in instructions) {
		when (instruction.action) {
			'N' -> ship.y += instruction.value
			'S' -> ship.y -= instruction.value
			'E' -> ship.x += instruction.value
			'W' -> ship.x -= instruction.value
			'R' -> ship.rot -= instruction.value
			'L' -> ship.rot += instruction.value
			'F' -> when (Math.floorMod(ship.rot, 360)) {
				0 -> ship.x += instruction.value
				90 -> ship.y += instruction.value
				180 -> ship.x -= instruction.value
				270 -> ship.y -= instruction.value
			}
		}
	}
	return abs(ship.x) + abs(ship.y)
}

fun cos(deg: Int): Int = when (Math.floorMod(deg, 360)) {
	0 -> 1
	90 -> 0
	180 -> -1
	270 -> 0
	else -> throw IllegalArgumentException("Invalid cos: ${Math.floorMod(deg, 360)}")
}
fun sin(deg: Int): Int = when (Math.floorMod(deg, 360)) {
	0 -> 0
	90 -> 1
	180 -> 0
	270 -> -1
	else -> throw IllegalArgumentException("Invalid sin: ${Math.floorMod(deg, 360)}")
}
fun rotate(pos: Position, value: Int) {
	val cos = cos(value)
	val sin = sin(value)
	val old = pos.x
	pos.x = pos.x * cos - pos.y * sin
	pos.y = old * sin + pos.y * cos
}
fun day12part2(instructions: List<Instruction>): Int {
	val ship = Position()
	val waypoint = Position(10, 1)
	for (instruction in instructions) {
		when (instruction.action) {
			'N' -> waypoint.y += instruction.value
			'S' -> waypoint.y -= instruction.value
			'E' -> waypoint.x += instruction.value
			'W' -> waypoint.x -= instruction.value
			'R' -> rotate(waypoint, -instruction.value)
			'L' -> rotate(waypoint, instruction.value)
			'F' -> {
				ship.x += instruction.value * waypoint.x
				ship.y += instruction.value * waypoint.y
			}
		}
	}
	return abs(ship.x) + abs(ship.y)
}
