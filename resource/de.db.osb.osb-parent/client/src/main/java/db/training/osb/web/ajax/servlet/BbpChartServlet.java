package db.training.osb.web.ajax.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import db.training.osb.model.SAPMassnahme;

public class BbpChartServlet extends HttpServlet {

	private final int width = 340;

	private final int height = 30;

	private static final long serialVersionUID = 7040252475458557978L;

	public static final String RequestParameter_RangeAxis = "axis";

	public static final String RequestParameter_TypMassnahme = "mn";

	public static final String RequestParameter_TypGleissperrung = "gl";

	public static final String RequestParameter_TypOberleitung = "ol";

	public static final String RequestParameter_TypLangsamfahren = "lf";

	public enum TYPE {
		MASSNAHME, GLEISSPERRUNG, OBERLEITUNG, LANGSAMFAHREN, UNDEFINED
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {

		CategoryPlot plot = null;

		if (req.getParameter(RequestParameter_RangeAxis) != null) {
			plot = new GanttPlot(null, getDate(1, Calendar.APRIL, 2010), getDate(31,
			    Calendar.APRIL, 2010), true);
		} else {
			plot = new GanttPlot(createDataset(), getDate(1, Calendar.APRIL, 2010), getDate(31,
			    Calendar.APRIL, 2010), false);
		}

		CategoryItemRenderer renderer = plot.getRenderer();
		// Farbe/Typ setzen
		if (req.getParameter(RequestParameter_TypGleissperrung) != null) {
			renderer.setSeriesPaint(0, Color.BLUE);
		} else if (req.getParameter(RequestParameter_TypOberleitung) != null) {
			renderer.setSeriesPaint(0, Color.BLUE);
		} else if (req.getParameter(RequestParameter_TypLangsamfahren) != null) {
			renderer.setSeriesPaint(0, Color.GREEN);
		}

		resp.setContentType("image/png");

		if (req.getParameter(RequestParameter_RangeAxis) != null) {
			renderChart(resp.getOutputStream(), plot, width, 20);
		} else {
			renderChart(resp.getOutputStream(), plot, width, height);
		}
	}

	private void renderChart(OutputStream os, CategoryPlot plot, int width, int height)
	    throws IOException {

		//
		// Chart zeichnen und in Ausgabestream schreiben
		//
		JFreeChart chart = new JFreeChart(null, null, plot, false);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		chart.draw(g2, new Rectangle2D.Double(0, 0, width, height), new ChartRenderingInfo());

		ChartUtilities.writeBufferedImageAsPNG(os, img);
	}

	public static IntervalCategoryDataset createDataset() {
		TaskSeriesCollection result = new TaskSeriesCollection();

		Random random = new Random();
		int tagVon = random.nextInt(10) + 1;
		int tagBis = random.nextInt(30 - tagVon + 1) + tagVon;

		// Serie 1
		TaskSeries s1 = new TaskSeries("Serie 1");
		s1.add(new Task("T1", new SimpleTimePeriod(getDate(tagVon, Calendar.APRIL, 2010), getDate(
		    tagBis, Calendar.APRIL, 2010))));

		result.add(s1);

		return result;
	}

	public static IntervalCategoryDataset createDataset(SAPMassnahme mn) {

		TaskSeriesCollection result = new TaskSeriesCollection();

		TaskSeries massnahme = new TaskSeries("Maßnahme");
		TaskSeries gleissperrungen = new TaskSeries("Gleissperrungen");
		TaskSeries oberleitungen = new TaskSeries("Oberleitungen");
		TaskSeries langsamfahren = new TaskSeries("Langsamfahren");

		//
		// Maßnahme
		//
		Task mnTask = new Task("Maßnahme", getDate(2, Calendar.APRIL, 2010), getDate(20,
		    Calendar.APRIL, 2010));
		massnahme.add(mnTask);

		//
		// Gleissperrungen
		//
		Task glTask = new Task("Gleissperrung 1", null);
		glTask.addSubtask(new Task("", getDate(1, Calendar.APRIL, 2010), getDate(2, Calendar.APRIL,
		    2010)));
		glTask.addSubtask(new Task("Test", getDate(3, Calendar.APRIL, 2010), getDate(4,
		    Calendar.APRIL, 2010)));
		glTask.addSubtask(new Task("", getDate(5, Calendar.APRIL, 2010), getDate(6, Calendar.APRIL,
		    2010)));
		glTask.addSubtask(new Task("", getDate(7, Calendar.APRIL, 2010), getDate(8, Calendar.APRIL,
		    2010)));
		glTask.addSubtask(new Task("", getDate(9, Calendar.APRIL, 2010), getDate(10,
		    Calendar.APRIL, 2010)));
		gleissperrungen.add(glTask);

		Task g2Task = new Task("Gleissperrung 2", null);
		g2Task.addSubtask(new Task("", getDate(1, Calendar.APRIL, 2010), getDate(2, Calendar.APRIL,
		    2010)));
		g2Task.addSubtask(new Task("", getDate(3, Calendar.APRIL, 2010), getDate(4, Calendar.APRIL,
		    2010)));
		g2Task.addSubtask(new Task("", getDate(5, Calendar.APRIL, 2010), getDate(6, Calendar.APRIL,
		    2010)));
		g2Task.addSubtask(new Task("", getDate(7, Calendar.APRIL, 2010), getDate(8, Calendar.APRIL,
		    2010)));
		g2Task.addSubtask(new Task("", getDate(9, Calendar.APRIL, 2010), getDate(10,
		    Calendar.APRIL, 2010)));
		gleissperrungen.add(g2Task);

		//
		// Langsamfahren
		//
		Task langsamTask = new Task("Langsamfahren 1", null);
		langsamTask.addSubtask(new Task("", getDate(10, Calendar.APRIL, 2010), getDate(12,
		    Calendar.APRIL, 2010)));
		langsamTask.addSubtask(new Task("", getDate(13, Calendar.APRIL, 2010), getDate(14,
		    Calendar.APRIL, 2010)));
		langsamTask.addSubtask(new Task("", getDate(15, Calendar.APRIL, 2010), getDate(16,
		    Calendar.APRIL, 2010)));
		langsamTask.addSubtask(new Task("", getDate(17, Calendar.APRIL, 2010), getDate(18,
		    Calendar.APRIL, 2010)));
		langsamTask.addSubtask(new Task("", getDate(19, Calendar.APRIL, 2010), getDate(20,
		    Calendar.APRIL, 2010)));
		langsamfahren.add(langsamTask);

		result.add(massnahme);
		result.add(gleissperrungen);
		result.add(oberleitungen);
		result.add(langsamfahren);

		return result;
	}

	public static Date getDate(final int day, final int month, final int year) {
		final Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}
}