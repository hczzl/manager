package com.ruoyi.web.controller.excel.bean;

import java.io.Serializable;

public class RowCellIndex implements Serializable {
    private static final long serialVersionUID = 8635468356052187781L;
    /** 单元格的行索引 */
    private int rowIndex;
    /** 单元格的列索引 */
    private int cellIndex;
    public RowCellIndex(int rowIndex, int cellIndex) {
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
    }
    public int getRowIndex() {
        return rowIndex;
    }
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    public int getCellIndex() {
        return cellIndex;
    }
    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }
    public int incrementRowIndexAndGet() {
        this.rowIndex++;
        return this.getRowIndex();
    }
    public int incrementCellIndexAndGet() {
        this.cellIndex++;
        return this.getCellIndex();
    }
}
