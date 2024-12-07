import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

fun main() {

	val obstruction = '#'
	val marker = 'X'

	fun Array<CharArray>.deepCopy(): Array<CharArray> {
		return this.map { it.clone() }.toTypedArray()
	}

	fun updPosition(pos: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
		return when (direction) {
			Direction.UP -> Pair(pos.first - 1, pos.second)
			Direction.DOWN -> Pair(pos.first + 1, pos.second)
			Direction.LEFT -> Pair(pos.first, pos.second - 1)
			Direction.RIGHT -> Pair(pos.first, pos.second + 1)
		}
	}

	fun solve(input: List<String>): Pair<MutableSet<Pair<Int, Int>>, Pair<Int, Int>> {
		var position = Pair(0, 0)
		val field = Array(input.size) {
			input[it].toCharArray().also { line ->
				if (line.contains('^')) {
					position = Pair(it, line.indexOf('^'))
				}
			}
		}
		val initPosition = position
		val markedPositions = mutableSetOf<Pair<Int, Int>>()

		var direction = Direction.UP

		while (true) {
			field[position.first][position.second] = marker
			markedPositions.add(position)

			var next = updPosition(position, direction)

			if (next.first < 0 || next.first >= field.size ||
				next.second < 0 || next.second >= field[0].size
			) {
				break
			}

			val nextChr = field[next.first][next.second]

			if (nextChr == obstruction) {
				direction = direction.rotate()
				next = updPosition(position, direction)

			}
			position = next
		}

		return Pair(markedPositions, initPosition)
	}

	fun part1(input: List<String>): Int {
		val (markedPositions, _) = solve(input)
		return markedPositions.size
	}

	fun part2(input: List<String>): Int {
		val field = Array(input.size) { input[it].toCharArray() }
		val (markedPositions, initPosition) = solve(input)

		val infLoop = AtomicInteger(0)
		val executor = Executors.newFixedThreadPool(10)
		val latch = CountDownLatch(markedPositions.size)

		for (pos in markedPositions) {
			executor.execute {
				if (field[pos.first][pos.second] == obstruction) {
					latch.countDown()
					return@execute
				}
				if (initPosition == pos) {
					latch.countDown()
					return@execute
				}

				val newField = field.deepCopy()
				newField[pos.first][pos.second] = obstruction
				newField[initPosition.first][initPosition.second] = '^'

				val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
				var direction = Direction.UP
				var currentPosition = initPosition

				while (true) {
					if (currentPosition.first < 0 || currentPosition.first >= newField.size ||
						currentPosition.second < 0 || currentPosition.second >= newField[0].size
					) {
						break
					}

					if (visited.contains(Pair(currentPosition, direction))) {
						infLoop.incrementAndGet()
						break
					}
					visited.add(Pair(currentPosition, direction))
					newField[currentPosition.first][currentPosition.second] = marker

					var next = updPosition(currentPosition, direction)

					if (next.first < 0 || next.first >= newField.size ||
						next.second < 0 || next.second >= newField[0].size
					) {
						break
					}

					val nextChr = newField[next.first][next.second]
					if (nextChr == obstruction) {
						direction = direction.rotate()
						next = updPosition(currentPosition, direction)
						if (newField[next.first][next.second] == obstruction) {
							direction = direction.rotate()
							next = updPosition(currentPosition, direction)
						}
					}
					currentPosition = next
				}
				latch.countDown()
			}
		}

		latch.await()

		executor.shutdown()

		return infLoop.get()
	}

	check(
		part1(
			listOf(
				"....#.....",
				".........#",
				"..........",
				"..#.......",
				".......#..",
				"..........",
				".#..^.....",
				"........#.",
				"#.........",
				"......#..."
			)
		).also {
			it.println()
		} == 41
	)

	check(
		part2(
			listOf(
				"....#.....",
				".........#",
				"..........",
				"..#.......",
				".......#..",
				"..........",
				".#..^.....",
				"........#.",
				"#.........",
				"......#..."
			)
		).also {
			it.println()
		} == 6
	)

	val input = readInput("Day06")
	part1(input).println()
	part2(input).println()
}

enum class Direction {
	UP, DOWN, LEFT, RIGHT;

	fun rotate(): Direction {
		return when (this) {
			UP -> RIGHT
			RIGHT -> DOWN
			DOWN -> LEFT
			LEFT -> UP
		}
	}
}