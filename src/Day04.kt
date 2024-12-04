import kotlin.math.absoluteValue

fun main() {

	data class Position(
		val x: Int,
		val y: Int
	)

	fun search(matrix: Array<CharArray>, target: Position): Int {
		val word = "XMAS"
		val wordLength = word.length
		val rowSize = matrix[0].size
		val colSize = matrix.size
		val (isLeft, isRight) = target.let {
			val left = target.y - wordLength + 1
			val right = target.y + wordLength - 1
			Pair(left >= 0, right < rowSize)
		}
		val (isUp, isDown) = target.let {
			val up = target.x - wordLength + 1
			val down = target.x + wordLength - 1
			Pair(up >= 0, down < colSize)
		}
		val (isDiagLeftUp, isDiagRightUp) = Pair(isLeft && isUp, isRight && isUp)
		val (isDiagLeftDown, isDiagRightDown) = Pair(isLeft && isDown, isRight && isDown)

		var result = 0
		val sb = StringBuilder()

		if (isLeft) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x][target.y - i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isRight) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x][target.y + i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isUp) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x - i][target.y])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isDown) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x + i][target.y])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isDiagLeftUp) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x - i][target.y - i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isDiagRightUp) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x - i][target.y + i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isDiagLeftDown) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x + i][target.y - i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}
		if (isDiagRightDown) {
			for (i in 0..<wordLength) {
				sb.append(matrix[target.x + i][target.y + i])
			}
			if (sb.toString() == word) {
				result++
			}
			sb.clear()
		}

		return result
	}

	fun isStar(matrix: Array<CharArray>, target: Position): Boolean {
		return if (matrix[target.x][target.y] == 'A') {
			val upLeft = matrix[target.x - 1][target.y - 1]
			val upRight = matrix[target.x - 1][target.y + 1]
			val downLeft = matrix[target.x + 1][target.y - 1]
			val downRight = matrix[target.x + 1][target.y + 1]
			if (listOf(
					upLeft,
					upRight,
					downLeft,
					downRight
				).all { it == 'M' || it == 'S' }
			) {
				val l2rDiff = downRight - upLeft
				val r2lDiff = downLeft - upRight
				l2rDiff.absoluteValue == 6 && r2lDiff.absoluteValue == 6
			} else {
				false
			}

		} else {
			false
		}
	}

	fun part1(input: List<String>): Int {
		val searchMatrix = input.map {
			it.toCharArray()
		}.toTypedArray()

		var result = 0
		for (i in searchMatrix.indices) {
			for (j in searchMatrix[i].indices) {
				if (searchMatrix[i][j] == 'X') {
					val subXStart = if (i - 3 < 0) 0 else i - 3
					val subYStart = if (j - 3 < 0) 0 else j - 3
					val subXEnd =
						if (i + 3 >= searchMatrix.size) searchMatrix.size - 1 else i + 3
					val subYEnd =
						if (j + 3 >= searchMatrix[i].size) searchMatrix[i].size - 1 else j + 3
					val subMatrix = searchMatrix.sliceArray(subXStart..subXEnd)
						.map { it.sliceArray(subYStart..subYEnd) }
						.toTypedArray()

					result += search(subMatrix, Position(i - subXStart, j - subYStart))
				}
			}
		}

		return result
	}

	fun part2(input: List<String>): Int {
		val searchMatrix = input.map {
			it.toCharArray()
		}.toTypedArray()

		var result = 0
		for (i in searchMatrix.indices) {
			for (j in searchMatrix[i].indices) {
				if (searchMatrix[i][j] == 'A') {
					if ((i - 1 >= 0 && j - 1 >= 0) &&
						(i + 1 < searchMatrix.size && j + 1 < searchMatrix[i].size)
					) {
						val subXStart = i - 1
						val subYStart = j - 1
						val subXEnd = i + 1
						val subYEnd = j + 1
						val subMatrix = searchMatrix.sliceArray(subXStart..subXEnd)
							.map { it.sliceArray(subYStart..subYEnd) }
							.toTypedArray()

						if (isStar(
								subMatrix,
								Position(i - subXStart, j - subYStart)
							)
						) {
							result++
						}
					}
				}
			}
		}

		return result
	}

	check(
		part1(
			listOf(
				"MMMSXXMASM",
				"MSAMXMSMSA",
				"AMXSXMAAMM",
				"MSAMASMSMX",
				"XMASAMXAMM",
				"XXAMMXXAMA",
				"SMSMSASXSS",
				"SAXAMASAAA",
				"MAMMMXMMMM",
				"MXMXAXMASX",
			)
		).also {
			it.println()
		} == 18
	)

	check(
		part2(
			listOf(
				"MMMSXXMASM",
				"MSAMXMSMSA",
				"AMXSXMAAMM",
				"MSAMASMSMX",
				"XMASAMXAMM",
				"XXAMMXXAMA",
				"SMSMSASXSS",
				"SAXAMASAAA",
				"MAMMMXMMMM",
				"MXMXAXMASX",
			)
		).also {
			it.println()
		} == 9
	)

	val input = readInput("Day04")
	part1(input).println()
	part2(input).println()
}