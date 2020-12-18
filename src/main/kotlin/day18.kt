import java.io.File
import java.util.Stack

fun main() {
	val start = System.nanoTime()
	val input = File("input/18").readLines().map { Calculation(it) }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day18part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day18part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

enum class CalculationOperation(val char: Char) {
	ADD('+') {
		override fun execute(a: Long, b: Long) = a + b
	},
	MUL('*') {
		override fun execute(a: Long, b: Long) = a * b
	};
	abstract fun execute(a: Long, b: Long): Long
	companion object {
		fun fromChar(char: Char): CalculationOperation {
			for (op in values())
				if (op.char == char)
					return op
			throw IllegalArgumentException("Unknown operation character: $char")
		}
	}
}
data class CalculationValue(var value: Long = 0, var op: CalculationOperation = CalculationOperation.ADD, val priority: Boolean = false) {
	private var buffer: Long? = null
	fun then(n: Long) {
		if (priority && op == CalculationOperation.MUL) {
			if (buffer == null) {
				buffer = value
				value = n
			} else {
				buffer = CalculationOperation.MUL.execute(buffer!!, value)
				value = n
			}
		} else
			value = op.execute(value, n)
	}
	fun end() {
		if (buffer != null)
			value = CalculationOperation.MUL.execute(value, buffer!!)
	}
}
data class Calculation(val calc: String) {
	fun result(priority: Boolean = false): Long {
		val stack = Stack<CalculationValue>()
		stack.push(CalculationValue(priority = priority))
		var nb = ""
		for (c in "$calc ") {
			if (c in '0'..'9')
				nb += c
			else {
				if (nb.isNotEmpty()) {
					stack.peek().then(nb.toLong())
					nb = ""
				}
				when (c) {
					' ' -> {}
					'(' -> stack.push(CalculationValue(priority = priority))
					')' -> {
						val last = stack.pop()
						last.end()
						stack.peek().then(last.value)
					}
					else -> stack.peek().op = CalculationOperation.fromChar(c)
				}
			}
		}
		stack.peek().end()
		return stack.peek().value
	}
}
fun day18part1(calculations: List<Calculation>): Long {
	var n: Long = 0
	for (calculation in calculations)
		n += calculation.result()
	return n
}

fun day18part2(calculations: List<Calculation>): Long {
	var n: Long = 0
	for (calculation in calculations)
		n += calculation.result(true)
	return n
}
