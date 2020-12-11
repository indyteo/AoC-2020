import java.io.File

val codeParse = "(?<op>acc|nop|jmp) (?<param>[+-]\\d+)".toRegex()
fun main() {
	val start = System.nanoTime()
	val input = File("input/8").readLines().map {
		val code = codeParse.matchEntire(it)!!.groups
		CodeLine(Operation.valueOf(code["op"]!!.value.toUpperCase()), code["param"]!!.value.toInt())
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day8part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day8part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class CodeLine(var op: Operation, val param: Int)
enum class Operation {
	ACC, JMP, NOP
}
fun day8part1(program: List<CodeLine>, returnCurrent: Boolean = true): Int? {
	var n = 0
	val executed = mutableListOf<Int>()
	var i = 0
	while (!executed.contains(i)) {
		if (i >= program.size)
			return n
		val line = program[i]
		executed.add(i)
		i += when (line.op) {
			Operation.ACC -> {
				n += line.param
				1
			}
			Operation.JMP -> line.param
			Operation.NOP -> 1
		}
	}
	return if (returnCurrent) n else null
}

fun day8part2(program: List<CodeLine>): Int {
	for (k in program.indices) {
		val old = program[k].op
		program[k].op = when (program[k].op) {
			Operation.JMP -> Operation.NOP
			Operation.NOP -> Operation.JMP
			else -> continue
		}
		val n = day8part1(program, false)
		if (n != null)
			return n
		program[k].op = old
	}
	return -1
}
