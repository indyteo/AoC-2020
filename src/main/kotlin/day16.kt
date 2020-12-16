import java.io.File

val ticketFieldParse = "(?<name>[^:]+): (?<r1min>\\d+)-(?<r1max>\\d+) or (?<r2min>\\d+)-(?<r2max>\\d+)".toRegex()
fun main() {
	val start = System.nanoTime()
	val input = File("input/16").readText().split("\n\n")
	val input1 = input[0].lines().map {
		val field = ticketFieldParse.matchEntire(it)!!.groups
		TicketField(field["name"]!!.value, field["r1min"]!!.value.toInt()..field["r1max"]!!.value.toInt(), field["r2min"]!!.value.toInt()..field["r2max"]!!.value.toInt())
	}
	val input2 = Ticket(input[1].lines()[1].split(",").map { it.toInt() })
	val input3 = input[2].lines().drop(1).map { Ticket(it.split(",").map { i -> i.toInt() }) }
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day16part1(input1, input3)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day16part2(input1, input2, input3)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

data class TicketField(val name: String, val range1: IntRange, val range2: IntRange, val possibleIndexes: MutableSet<Int> = mutableSetOf()) {
	val index get() = possibleIndexes.first()
}
data class Ticket(val values: List<Int>, var valid: Boolean = true)
fun valid(field: TicketField, value: Int) = value in field.range1 || value in field.range2
fun day16part1(fields: List<TicketField>, others: List<Ticket>): Int {
	var n = 0
	for (ticket in others) {
		for (value in ticket.values) {
			var notOk = true
			for (field in fields) {
				if (valid(field, value)) {
					notOk = false
					break
				}
			}
			if (notOk) {
				n += value
				ticket.valid = false
			}
		}
	}
	return n
}

fun day16part2(fields: List<TicketField>, self: Ticket, others: List<Ticket>): Long {
	val valids = others.filter { it.valid }.plusElement(self)
	for (field in fields) {
		field.possibleIndexes.addAll(self.values.indices)
		for (i in self.values.indices) {
			var notOk = false
			for (ticket in valids) {
				if (!valid(field, ticket.values[i])) {
					notOk = true
					break
				}
			}
			if (notOk)
				field.possibleIndexes.remove(i)
		}
	}
	val sortedFields = fields.sortedBy { it.possibleIndexes.size }
	val reservedIndexes = mutableSetOf(sortedFields.first().index)
	for (i in 1 until sortedFields.size) {
		sortedFields[i].possibleIndexes.removeAll(reservedIndexes)
		reservedIndexes.add(sortedFields[i].index)
	}
	var n: Long = 1
	for (field in fields)
		if (field.name.startsWith("departure"))
			n *= self.values[field.index]
	return n
}
