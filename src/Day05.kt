fun main() {

	data class Spec(
		val rules: MutableMap<Int, Set<Int>> = mutableMapOf(),
		var updates: MutableList<Array<Int>> = mutableListOf()
	)

	fun parseSpec(input: List<String>): Spec {
		val spec = Spec()
		var isRule = true
		input.forEach { line ->
			if (line == "---") {
				isRule = false
			} else if (isRule) {
				val (key, value) = line.split("|").map { it.toInt() }
				spec.rules[key] = (spec.rules[key] ?: emptySet()) + value
			} else {
				spec.updates.add(line.split(",").map { it.toInt() }.toTypedArray())
			}
		}

		return spec
	}

	fun part1(input: List<String>): Int {
		val spec = parseSpec(input)

		var sum = 0
		spec.updates.forEach { update ->
			var isValid = true
			for (i in update.indices) {
				val cur = update[i]
				val curRules = spec.rules[cur] ?: continue
				for (j in (i downTo 0)) {
					val chk = update[j]
					if (curRules.contains(chk)) {
						isValid = false
						break
					}
				}
			}
			if (isValid) {
				sum += update[update.size / 2]
			}
		}

		return sum
	}

	fun part2(input: List<String>): Int {
		val spec = parseSpec(input)

		var sum = 0
		spec.updates.forEach { update ->
			var isValid = true
			var range = update.indices
			var i = range.first

			while (i <= range.last) {
				val cur = update[i]
				val curRules = spec.rules[cur]
				if (curRules != null) {
					for (j in (i downTo 0)) {
						val chk = update[j]
						if (curRules.contains(chk)) {
							isValid = false
							update[i] = update[j].also {
								update[j] = update[i]
							}
							range = IntRange(j, update.size - 1)
							i = range.first
							continue
						}
					}
				}
				i++
			}
			if (!isValid) {
				sum += update[update.size / 2]
			}
		}

		return sum
	}

	check(
		part1(
			listOf(
				"47|53",
				"97|13",
				"97|61",
				"97|47",
				"75|29",
				"61|13",
				"75|53",
				"29|13",
				"97|29",
				"53|29",
				"61|53",
				"97|53",
				"61|29",
				"47|13",
				"75|47",
				"97|75",
				"47|61",
				"75|61",
				"47|29",
				"75|13",
				"53|13",
				"---",
				"75,47,61,53,29",
				"97,61,53,29,13",
				"75,29,13",
				"75,97,47,61,53",
				"61,13,29",
				"97,13,75,29,47",
			)
		).also {
			it.println()
		} == 143
	)

	check(
		part2(
			listOf(
				"47|53",
				"97|13",
				"97|61",
				"97|47",
				"75|29",
				"61|13",
				"75|53",
				"29|13",
				"97|29",
				"53|29",
				"61|53",
				"97|53",
				"61|29",
				"47|13",
				"75|47",
				"97|75",
				"47|61",
				"75|61",
				"47|29",
				"75|13",
				"53|13",
				"---",
				"75,47,61,53,29",
				"97,61,53,29,13",
				"75,29,13",
				"75,97,47,61,53",
				"61,13,29",
				"97,13,75,29,47",
			)
		).also {
			it.println()
		} == 123
	)

	val input = readInput("Day05")
	part1(input).println()
	part2(input).println()
}