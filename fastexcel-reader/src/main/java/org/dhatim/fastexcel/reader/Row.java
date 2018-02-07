package org.dhatim.fastexcel.reader;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Row implements Iterable<Cell> {

    private final int rowNum;
    private final Cell[] cells;
    private final int physicalCellCount;

    public Row(int rowNum, int physicalCellCount, Cell[] cells) {
        this.rowNum = rowNum;
        this.physicalCellCount = physicalCellCount;
        this.cells = cells.clone();
    }

    public Cell getCell(int index) {
        if (index < 0 || index >= cells.length) {
            throw new IndexOutOfBoundsException("row-index: " + rowNum + ", index: " + index + ", count: " + cells.length);
        }
        return cells[index];
    }

    public Cell getCell(CellAddress address) {
        if (rowNum != address.getRow()) {
            throw new IllegalArgumentException("The given address " + address + " concerns another row (" + rowNum + ")");
        }
        return getCell(address.getColumn());
    }

    public List<Cell> getCells(int beginIndex, int endIndex) {
        return Arrays.asList(Arrays.copyOfRange(cells, beginIndex, endIndex));
    }

    public Optional<Cell> getOptionalCell(int index) {
        return index < 0 || index >= cells.length ? Optional.empty() : Optional.ofNullable(cells[index]);
    }

    public Optional<Cell> getFirstNonEmptyCell() {
        return stream().filter(Objects::nonNull).filter(cell -> !cell.getText().isEmpty()).findFirst();
    }

    public int getCellCount() {
        return cells.length;
    }

    public boolean hasCell(int index) {
        return index >= 0 && index < cells.length && cells[index] != null;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getPhysicalCellCount() {
        return physicalCellCount;
    }

    @Override
    public String toString() {
        return "Row " + rowNum + ' ' + Arrays.toString(cells);
    }

    @Override
    public Iterator<Cell> iterator() {
        return Arrays.asList(cells).iterator();
    }

    public Stream<Cell> stream() {
        return Arrays.stream(cells);
    }

    public Optional<String> getCellAsString(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::asString);
    }

    public Optional<OffsetDateTime> getCellAsDate(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::asDate);
    }

    public Optional<BigDecimal> getCellAsNumber(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::asNumber);
    }

    public Optional<Boolean> getCellAsBoolean(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::asBoolean);
    }

    public String getCellText(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::getText).orElse("");
    }

    public Optional<String> getCellRawValue(int cellIndex) {
        return getOptionalCell(cellIndex).map(Cell::getRawValue);
    }

}
