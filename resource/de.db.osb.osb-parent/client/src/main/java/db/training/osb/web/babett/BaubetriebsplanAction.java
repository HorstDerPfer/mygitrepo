package db.training.osb.web.babett;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;

import db.training.bob.model.BaubetriebsplanReport;
import db.training.easy.web.BaseDispatchAction;
import db.training.logwrapper.Logger;
import db.training.osb.service.BaubetriebsplanService;
import db.training.osb.service.BaubetriebsplanServiceImpl;

/**
 * @author Sebastian Hennebrueder Date: 24.02.2010
 */
public class BaubetriebsplanAction extends BaseDispatchAction {
	final Logger logger = Logger.getLogger(BaubetriebsplanAction.class);

	private BaubetriebsplanService baubetriesplanService;

	static final String SESSION_KEY_REPORT = "report";

	public BaubetriebsplanAction() {
		super();
		this.baubetriesplanService = new BaubetriebsplanServiceImpl(); // serviceFactory.createBaubetriebsplanService();
	}

	@SuppressWarnings("deprecation")
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaubetriebsplanForm baubetriebsplanForm = (BaubetriebsplanForm) form;
		final Date start = new Date(2010, 3, 1);
		final Date end = new Date(2010, 3, 31);
		BaubetriebsplanReport baubetriebsplanReport = baubetriesplanService.createReportData(
		    baubetriebsplanForm.getMassnahmeId(), start, end);
		request.getSession().setAttribute(SESSION_KEY_REPORT, baubetriebsplanReport);

		return mapping.findForward("SUCCESS");
	}

	public ActionForward renderChart(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaubetriebsplanReport report = (BaubetriebsplanReport) request.getSession().getAttribute(
		    BaubetriebsplanAction.SESSION_KEY_REPORT);
		// request.getSession().removeAttribute(BaubetriebsplanAction.SESSION_KEY_REPORT);

		final Date start = report.getStart();
		final Date end = report.getEnd();

		JFreeChart chart = null;
		boolean renderHeader = (request.getParameter("header") != null);
		if (renderHeader) {
			chart = createHeaderChart(start, end);
		} else {

			int row = Integer.parseInt(request.getParameter("row"));
			int massnahme = Integer.parseInt(request.getParameter("massnahme"));

			final BaubetriebsplanReport.MassnahmeReport massnahmeReport = report
			    .getMassnahmeReportList().get(massnahme);
			if (massnahmeReport == null)
				logger.debug("Massnahme nicht gefunden, ", report.getMassnahmeReportList());
			final BaubetriebsplanReport.RegelungReport regelungReport = massnahmeReport
			    .getRegelungReportList().get(row);
			if (regelungReport == null)
				logger.debug("Regelung fuer Massnahme nicht gefunden, ", massnahmeReport
				    .getRegelungReportList());

			chart = createChart(start, end, regelungReport);
		}
		response.setContentType("image/png");

		BufferedImage img = new BufferedImage(600, 30, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		chart.draw(g2, new Rectangle2D.Double(0, 0, 600, 30), new ChartRenderingInfo());

		ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), img);
		return null;
	}

	private JFreeChart createChart(Date start, Date end,
	    BaubetriebsplanReport.RegelungReport regelungReport) {
		JFreeChart chart;
		IntervalCategoryDataset dataset = regelungReport.getTaskSeries();

		chart = ChartFactory.createGanttChart(null, // chart title
		    null, // domain axis label
		    "Date", // range axis label
		    dataset, // data
		    false, // include legend
		    false, // tooltips
		    false // urls
		    );

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.getDomainAxis().setVisible(false);

		DateAxis dateAxis = new DateAxis();
		dateAxis.setVisible(false);
		dateAxis.setMinimumDate(start);
		dateAxis.setMaximumDate(end);
		plot.setRangeAxis(dateAxis);

		final CategoryItemRenderer itemRenderer = plot.getRenderer();
		itemRenderer.setSeriesPaint(0, regelungReport.getColor());
		return chart;
	}

	private JFreeChart createHeaderChart(Date start, Date end) {

		final JFreeChart chart = ChartFactory.createGanttChart(null, // chart title
		    null, // domain axis label
		    "Date", // range axis label
		    null, // data
		    false, // include legend
		    false, // tooltips
		    false // urls
		    );

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.getDomainAxis().setVisible(false);
		DateAxis dateAxis = new DateAxis();
		dateAxis.setMinimumDate(start);
		dateAxis.setMaximumDate(end);

		// Label vertikal zeichnen
		// dateAxis.setVerticalTickLabels(true);
		SimpleDateFormat sdf = new SimpleDateFormat("d");
		dateAxis.setDateFormatOverride(sdf);
		plot.setRangeAxis(dateAxis);
		return chart;
	}
}
