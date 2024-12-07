import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

fun main() {

	data class Equation(
		val result: Long,
		val values: List<Long>
	)

	data class Operator(
		val operator: String,
		val lambda: (Long, Long) -> Long
	) {
		fun apply(a: Long, b: Long): Long {
			return lambda(a, b)
		}
	}

	fun generatePermutationsRecursive(
		current: List<Operator>,
		size: Int,
		ops: List<Operator>,
		permutations: MutableList<List<Operator>>
	) {
		if (current.size == size) {
			permutations.add(current)
			return
		}
		for (operator in ops) {
			generatePermutationsRecursive(
				current + operator,
				size,
				ops,
				permutations
			)
		}
	}

	fun generateOperatorPermutations(
		size: Int,
		ops: List<Operator>
	): List<List<Operator>> {
		val permutations = mutableListOf<List<Operator>>()
		generatePermutationsRecursive(listOf(), size, ops, permutations)
		return permutations
	}

	fun checkEquation(input: List<String>, ops: List<Operator>): Long {
		val equations = input.map {
			val (result, values) = it.split(": ")
			Equation(result.toLong(), values.split(" ").map { it.toLong() })
		}

		val totalCalibResult = AtomicLong(0)
		val executor = Executors.newFixedThreadPool(10)
		val latch = CountDownLatch(equations.size)

		for (equation in equations) {
			executor.submit {
				val found =
					generateOperatorPermutations(equation.values.size - 1, ops).find {
						val values = equation.values.toMutableList()

						var result = values.removeFirst()
						for (i in values.indices) {
							val operator = it[i]
							val value = values[i]
							result = operator.apply(result, value)
						}
						result == equation.result
					}

				if (found != null) {
					totalCalibResult.addAndGet(equation.result)
				}

				latch.countDown()
			}
		}

		latch.await()
		executor.shutdown()

		return totalCalibResult.toLong()
	}

	fun part1(input: List<String>): Long {
		val ops = listOf(
			Operator("+") { a, b -> a + b },
			Operator("*") { a, b -> a * b }
		)
		return checkEquation(input, ops)
	}

	fun part2(input: List<String>): Long {
		val ops = listOf(
			Operator("+") { a, b -> a + b },
			Operator("*") { a, b -> a * b },
			Operator("||") { a, b -> (a.toString() + b.toString()).toLong() }
		)
		return checkEquation(input, ops)
	}

	check(
		part1(
			listOf(
				"190: 10 19",
				"3267: 81 40 27",
				"83: 17 5",
				"156: 15 6",
				"7290: 6 8 6 15",
				"161011: 16 10 13",
				"192: 17 8 14",
				"21037: 9 7 18 13",
				"292: 11 6 16 20",
			)
		).also {
			it.println()
		} == 3749L
	)

	check(
		part2(
			listOf(
				"190: 10 19",
				"3267: 81 40 27",
				"83: 17 5",
				"156: 15 6",
				"7290: 6 8 6 15",
				"161011: 16 10 13",
				"192: 17 8 14",
				"21037: 9 7 18 13",
				"292: 11 6 16 20",
			)
		).also {
			it.println()
		} == 11387L
	)

	val input = readInput("Day07")
	part1(input).println()
	part2(input).println()
}