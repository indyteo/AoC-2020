import java.io.File

fun main() {
	val start = System.nanoTime()
	val input = File("input/4").readText().split("\n\n".toRegex()).map { passport ->
		val p = HashMap<String, String>()
		passport.split("[\n ]".toRegex()).map { field ->
			val split = field.split(":")
			p[split[0]] = split[1]
		}
		p
	}
	val end = System.nanoTime()

	val start1 = System.nanoTime()
	val n1 = day4part1(input)
	val end1 = System.nanoTime()

	val start2 = System.nanoTime()
	val n2 = day4part2(input)
	val end2 = System.nanoTime()
	println("Lecture : ${end - start} ns\nPartie 1 : $n1 (${end1 - start1} ns)\nPartie 2 : $n2 (${end2 - start2} ns)")
}

val required = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
fun validPassport1(p: Map<String, String>) = p.keys.containsAll(required)

fun day4part1(passports: List<Map<String, String>>): Int {
	var n = 0
	for (passport in passports)
		if (validPassport1(passport))
			n++
	return n
}

val byr = "(19[2-9][0-9]|200[0-2])".toRegex()
val iyr = "(201[0-9]|2020)".toRegex()
val eyr = "(202[0-9]|2030)".toRegex()
val hgt = "((1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in)".toRegex()
val hcl = "#[0-9a-f]{6}".toRegex()
val ecl = "(amb|blu|brn|gry|grn|hzl|oth)".toRegex()
val pid = "[0-9]{9}".toRegex()
fun validPassport2(p: Map<String, String>) = validPassport1(p)
		&& p["byr"]!!.matches(byr)
		&& p["iyr"]!!.matches(iyr)
		&& p["eyr"]!!.matches(eyr)
		&& p["hgt"]!!.matches(hgt)
		&& p["hcl"]!!.matches(hcl)
		&& p["ecl"]!!.matches(ecl)
		&& p["pid"]!!.matches(pid)

fun day4part2(passports: List<Map<String, String>>): Int {
	var n = 0
	for (passport in passports)
		if (validPassport2(passport))
			n++
	return n
}
