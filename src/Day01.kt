import kotlin.math.absoluteValue

fun main() {

	data class Input(
		val first: MutableList<Int> = mutableListOf(),
		val second: MutableList<Int> = mutableListOf()
	)

	fun parse(input: List<String>): Input {
		val res = Input()
		input.forEach {
			val (first, second) = it.split("   ")
			res.first.add(first.toInt())
			res.second.add(second.toInt())
		}
		res.first.sort()
		res.second.sort()

		return res
	}

	fun part1(input: List<String>): Int {
		val parsed = parse(input)

		var result = 0

		repeat(input.size) {
			val lastFirst = parsed.first.removeFirst()
			val lastSecond = parsed.second.removeFirst()
			val diff = (lastSecond - lastFirst).absoluteValue
			result += diff
		}

		return result
	}

	data class Input2(
		val first: MutableList<Int> = mutableListOf(),
		val second: MutableMap<Int, Int> = mutableMapOf()
	)

	fun parse2(input: List<String>): Input2 {
		val res = Input2()
		input.forEach {
			val (first, second) = it.split("   ")
			res.first.add(first.toInt())
			res.second[second.toInt()] = (res.second[second.toInt()] ?: 0) + 1
		}
		return res
	}

	fun part2(input: List<String>): Int {
		val parsed = parse2(input)

		var result = 0

		parsed.first.forEach {
			result += (parsed.second[it] ?: 0) * it
		}

		return result
	}

	// Test if implementation meets criteria from the description, like:
	check(
		part2(
			listOf(
				"3   4",
				"4   3",
				"2   5",
				"1   3",
				"3   9",
				"3   3",
			)
		) == 31
	)

	// Read the input from the `src/Day01.txt` file.
	val input = readInput("Day01")
	part1(input).println()
	part2(input).println()
}
