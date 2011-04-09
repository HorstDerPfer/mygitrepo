package db.training.osb.web.ajax.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

public class GanttPlot extends CategoryPlot {

	private static final long serialVersionUID = -248063669028495759L;

	public GanttPlot(CategoryDataset dataset) {
		this(dataset, new CategoryAxis(), new DateAxis());
	}

	public GanttPlot(CategoryDataset dataset, CategoryAxis categoryAxis, ValueAxis valueAxis) {
		super(dataset, categoryAxis, valueAxis, new BbpGanttRenderer());

		initialize();
	}

	public GanttPlot(CategoryDataset dataset, Date minimumDate, Date maximumDate,
	    boolean showRangeAxis) {
		super();

		setDataset(dataset);

		// Zeilen-Achse ausblenden
		CategoryAxis rowAxis = new CategoryAxis();
		rowAxis.setVisible(false);
		setDomainAxis(rowAxis);

		// Zeit-Achse formatieren
		DateAxis dateAxis = new DateAxis();
		dateAxis.setMinimumDate(minimumDate);
		dateAxis.setMaximumDate(maximumDate);

		// Label vertikal zeichnen
		// dateAxis.setVerticalTickLabels(true);
		SimpleDateFormat sdf = new SimpleDateFormat("d.MMM.");
		dateAxis.setDateFormatOverride(sdf);
		dateAxis.setVisible(showRangeAxis);
		setRangeAxis(dateAxis);

		initialize();
	}

	private void initialize() {
		setRenderer(new BbpGanttRenderer());
		setOrientation(PlotOrientation.HORIZONTAL);

		RectangleInsets insets = new RectangleInsets(0, 0, 1, 1);
		setInsets(insets);
	}
}
