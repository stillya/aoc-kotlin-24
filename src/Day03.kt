fun main() {

	fun part1(input: List<String>): Int = input.sumOf { Graph.parse(it, false) }

	fun part2(input: List<String>): Int = input.sumOf { Graph.parse(it, true) }


	check(
		part1(
			listOf(
				"xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))",
			)
		) == 161
	)

	check(
		part2(
			listOf(
				"xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))\n",
			)
		) == 48
	)

	val input = readInput("Day03")
	part1(input).println()
	part2(input).println()
}

object Graph {
	// global on the whole input
	private var includeLast = true

	enum class State(val c: Char) {
		M('m'),
		U('u'),
		L('l'),
		BRT_OPEN('('),
		FIR_NUM('f'),
		COMMA(','),
		SEC_NUM('s'),
		BRT_CLOSE(')'),

		D('d'),
		O('o'),
		DBRT_OPEN('('),
		DBRT_CLOSE(')'),
		N('n'),
		APOST('\''),
		T('t'),
		DNBRT_OPEN('('),
		DNBRT_CLOSE(')');

		companion object {
			fun valueOf(c: Char): State? {
				entries.forEach {
					if (it.c == c) {
						return it
					}
				}
				return null
			}
		}

		fun next(c: Char): State? {
			return when (this) {
				M -> if (c == 'u') U else null
				U -> if (c == 'l') L else null
				L -> if (c == '(') BRT_OPEN else null
				BRT_OPEN -> if (c.isDigit()) FIR_NUM else null
				FIR_NUM -> {
					if (c == ',') COMMA
					else if (c.isDigit()) FIR_NUM
					else null
				}

				COMMA -> if (c.isDigit()) SEC_NUM else null
				SEC_NUM -> {
					if (c == ')') BRT_CLOSE
					else if (c.isDigit()) SEC_NUM
					else null
				}

				BRT_CLOSE -> {
					when (c) {
						'm' -> M
						'd' -> D
						else -> null
					}
				}

				D -> if (c == 'o') O else null
				O -> {
					when (c) {
						'(' -> DBRT_OPEN
						'n' -> N
						else -> null
					}
				}

				DBRT_OPEN -> if (c == ')') DBRT_CLOSE else null

				DBRT_CLOSE -> {
					when (c) {
						'm' -> M
						'd' -> D
						else -> null
					}
				}

				N -> if (c == '\'') APOST else null
				APOST -> if (c == 't') T else null
				T -> if (c == '(') DNBRT_OPEN else null

				DNBRT_OPEN -> if (c == ')') DNBRT_CLOSE else null

				DNBRT_CLOSE -> {
					when (c) {
						'm' -> M
						'd' -> D
						else -> null
					}
				}
			}
		}
	}

	fun parse(input: String, part2: Boolean): Int {
		var result = 0
		var state: State? = null
		val numberFir = StringBuilder()
		val numberSec = StringBuilder()
		for (c in input) {
			if (state == null) {
				val s = State.valueOf(c)
				if (s != null && (s == State.M || s == State.D)) { // init state
					state = s
				}
				numberFir.clear()
				numberSec.clear()
				continue
			}
			state = state.next(c)

			when (state) {
				State.FIR_NUM -> {
					numberFir.append(c)
				}

				State.SEC_NUM -> {
					numberSec.append(c)
				}

				State.BRT_CLOSE -> {
					if (includeLast) {
						result += numberFir.toString().toInt() * numberSec.toString()
							.toInt()
					}
					numberFir.clear()
					numberSec.clear()
				}

				State.DBRT_CLOSE -> {
					if (part2) {
						includeLast = true
					}
				}

				State.DNBRT_CLOSE -> {
					if (part2) {
						includeLast = false
					}
				}

				else -> {
					// do nothing
				}
			}
		}
		return result
	}
}
