import kotlin.math.absoluteValue

fun main() {

	fun part1(input: List<String>): Int {
		var safeReports = 0
		input.forEach rit@{ report ->
			val levels = report.split(" ").map { it.toInt() }
			var lastValue = levels.first()
			var direction: Direction? = null
			levels.subList(1, levels.size).forEach { level ->
				val diff = level - lastValue
				lastValue = level
				val localDirection = if (diff > 0) {
					Direction.INCREASING
				} else if (diff < 0) {
					Direction.DECREASING
				} else {
					return@rit
				}

				if (direction == null) {
					direction = localDirection
				}

				if (diff.absoluteValue > 3 || direction != localDirection) {
					return@rit
				}
			}
			safeReports++
		}

		return safeReports
	}

	fun part2(input: List<String>): Int {
		var safeReports = 0
		input.forEach rit@{ report ->
			val levels = report.split(" ").map { it.toInt() }
			var lastValue = levels.first()
			var direction: Direction? = null
			val remainingLevels = levels.subList(1, levels.size)
			var errors = 0

			remainingLevels.forEachIndexed lit@{ idx, _ ->
				val level = remainingLevels[idx - errors]
				val diff = level - lastValue
				val localDirection = when {
					diff > 0 -> Direction.INCREASING
					diff < 0 -> Direction.DECREASING
					else -> {
						// fail-fast
						if (errors > 1) {
							return@rit
						} else {
							errors++
							return@lit
						}
					}
				}

				if (direction == null) {
					direction = localDirection
				}

				if (diff.absoluteValue > 3 || direction != localDirection) {
					if (errors >= 1) {
						return@rit
					} else {
						errors++
						if (idx > 0) {
							lastValue = levels[idx - errors]
							if ((idx - errors) == 0) {
								direction = if (level - lastValue > 0) {
									Direction.INCREASING
								} else {
									Direction.DECREASING
								}
							}
						}
						return@lit
					}
				}

				lastValue = level
			}
			errors = 0
			safeReports++
		}

		return safeReports
	}

	check(
		part2(
			listOf(
				"7 6 4 2 1",
				"1 2 7 8 9",
				"9 7 6 2 1",
				"1 3 2 4 5",
				"8 6 4 4 1",
				"1 3 6 7 9",
			)
		) == 4
	)

	val input = readInput("Day02")
	part1(input).println()
	part2(input).println()
}


enum class Direction {
	INCREASING,
	DECREASING
}