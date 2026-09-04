package com.proto.simpletictactoe.domain.model

class Board(val size: Int) {
    private val cells = Array(size * size) { Cell.EMPTY }

    fun get(row: Int, column: Int): Cell {
        return cells[row * size + column]
    }

    fun set(row: Int, column: Int, cell: Cell) {
        cells[row * size + column] = cell
    }

    fun isEmpty(row: Int, column: Int): Boolean {
        return get(row, column) == Cell.EMPTY
    }

    fun isFull(): Boolean {
        return cells.none { it == Cell.EMPTY }
    }

    fun getEmptyCells(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (isEmpty(r, c)) {
                    result.add(r to c)
                }
            }
        }
        return result
    }

    fun copy(): Board {
        val result = Board(size)
        System.arraycopy(this.cells, 0, result.cells, 0, cells.size)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false
        if (size != other.size) return false
        return cells.contentEquals(other.cells)
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + cells.contentHashCode()
        return result
    }
}
