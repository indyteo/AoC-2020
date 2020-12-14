import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/14").readLines().map {
		if (it.startsWith("mask"))
			Mask(it.substring(7))
		else
			MemoryInstruction(it.substring(4, it.indexOf(']')).toLong(), it.substring(it.indexOf('=') + 2).toLong())
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day14part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day14part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

fun Long.toBinaryString(length: Int? = null): String {
	val str = java.lang.Long.toBinaryString(this)
	return if (length == null) str else "0".repeat(length - str.length) + str
}
interface ProgramInstruction
data class MemoryInstruction(val address: Long, val value: Long): ProgramInstruction
data class Mask(val mask: String): ProgramInstruction {
	fun applyToValue(value: Long): Long {
		var n = value
		for (i in mask.indices) {
			n = when (mask[mask.length - i - 1]) {
				'0' -> n and (1L shl i).inv()
				'1' -> n or (1L shl i)
				else -> n
			}
		}
		return n
	}
	fun applyToAddress(address: Long): FloatingAddress {
		val n = address.toBinaryString(mask.length).toCharArray()
		for (i in mask.indices) {
			n[i] = when (mask[i]) {
				'0' -> n[i]
				'1' -> '1'
				else -> 'X'
			}
		}
		return FloatingAddress(n)
	}
}
class FloatingAddress(private val address: CharArray) {
	fun possibleAddresses(): List<Long> {
		val addresses = ArrayList<Long>()
		generateAddressesIn(addresses)
		return addresses
	}
	private fun generateAddressesIn(addresses: MutableList<Long>) {
		val x = address.indexOf('X')
		if (x < 0)
			addresses.add(String(address).toLong(2))
		else {
			val a = address.copyOf()
			a[x] = '0'
			FloatingAddress(a.copyOf()).generateAddressesIn(addresses)
			a[x] = '1'
			FloatingAddress(a).generateAddressesIn(addresses)
		}
	}
}
fun day14(program: List<ProgramInstruction>, memInstr: (MemoryInstruction, Mask, MutableMap<Long, Long>) -> Unit): Long {
	var mask: Mask = program.first() as Mask
	val memory = HashMap<Long, Long>()
	for (i in 1 until program.size) {
		val ins = program[i]
		if (ins is MemoryInstruction)
			memInstr.invoke(ins, mask, memory)
		else
			mask = ins as Mask
	}
	return memory.values.sum()
}

fun day14part1(program: List<ProgramInstruction>): Long {
	return day14(program) { ins, mask, memory -> memory[ins.address] = mask.applyToValue(ins.value) }
}

fun day14part2(program: List<ProgramInstruction>): Long {
	return day14(program) { ins, mask, memory -> for (address in mask.applyToAddress(ins.address).possibleAddresses()) memory[address] = ins.value }
}
