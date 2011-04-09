package db.training.bob.web.baumassnahme;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

public class Column implements Comparable<Column> {

	enum ValueType {
		NUMERIC, STRING, DATE
	};

	final static short headerColor = HSSFColor.TEAL.index;

	static HSSFCellStyle cellStyle;

	static HSSFCellStyle headerStyle;

	static HSSFCellStyle dateStyle;

	int index;

	int width;

	int colspan;

	int rowspan;

	int cellType;

	String headerText;

	ValueType valueType;

	public Column(int index, int cellType, String headerText) {
		this.index = index;
		this.cellType = cellType;
		this.headerText = headerText;
		this.colspan = this.rowspan = 0;
	}

	@Deprecated
	public Column(int index, int cellType, String headerText, int colspan, int rowspan) {
		this(index, cellType, headerText);
		this.colspan = colspan;
		this.rowspan = rowspan;
	}

	public Column(int index, int cellType, ValueType valueType, String headerText) {
		this(index, cellType, headerText);
		this.valueType = valueType;
	}

	@Deprecated
	public Column(int index, int cellType, ValueType valueType, String headerText, int colspan,
	    int rowspan) {
		this(index, cellType, valueType, headerText);
		this.colspan = colspan;
		this.rowspan = rowspan;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int i) {
		index = i;
	}

	public int getWidthInPoints() {
		return (int) Math.floor((width / 267));
	}

	public void setWidthInPoints(int width) {
		this.width = (width * 267);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public static HSSFCellStyle getCellStyle() {
		return cellStyle;
	}

	public static void setCellStyle(HSSFCellStyle cellStyle) {
		Column.cellStyle = cellStyle;
	}

	public static HSSFCellStyle getHeaderStyle() {
		return headerStyle;
	}

	public static void setHeaderStyle(HSSFCellStyle headerStyle) {
		Column.headerStyle = headerStyle;
	}

	public static HSSFCellStyle getDateStyle() {
		return dateStyle;
	}

	public static void setDateStyle(HSSFCellStyle dateStyle) {
		Column.dateStyle = dateStyle;
	}

	public int getCellType() {
		return cellType;
	}

	public void setCellType(int cellType) {
		this.cellType = cellType;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public static short getHeaderColor() {
		return headerColor;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType type) {
		valueType = type;
	}

	public HSSFCell createCell(HSSFRow row, boolean isHeaderCell) {
		if (row == null) {
			throw new IllegalArgumentException("row is null");
		}

		HSSFCell cell = row.createCell(getIndex(), getCellType());
		cell.setCellValue(new HSSFRichTextString(""));

		if (isHeaderCell) {
			cell.setCellStyle(getHeaderStyle());
			cell.setCellValue(new HSSFRichTextString(getHeaderText()));
		} else {

			if (getValueType() == ValueType.DATE) {
				cell.setCellStyle(getDateStyle());
			}
		}

		if (colspan != 0 || rowspan != 0) {
			CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 0);

			cra.setFirstRow(cell.getRowIndex());
			cra
			    .setLastRow(((rowspan != 0) ? cell.getRowIndex() + rowspan - 1 : cell.getRowIndex()));

			cra.setFirstColumn(cell.getColumnIndex());
			cra.setLastColumn(((colspan != 0) ? cell.getColumnIndex() + colspan - 1 : cell
			    .getColumnIndex()));

			HSSFSheet sheet = cell.getSheet();
			sheet.addMergedRegion(cra);
		}

		return cell;
	}

	public static HSSFCell createCell(HSSFRow row, int columnIndex, int cellType,
	    String headerText, boolean isHeaderCell) {
		Column col = new Column(columnIndex, cellType, headerText);
		return col.createCell(row, isHeaderCell);
	}

	public int compareTo(Column arg0) {
		if (arg0 == null) {
			return 0;
		}

		if (getIndex() < arg0.getIndex()) {
			return -1;
		}

		if (getIndex() > arg0.getIndex()) {
			return 1;
		}

		return 0;
	}
}
