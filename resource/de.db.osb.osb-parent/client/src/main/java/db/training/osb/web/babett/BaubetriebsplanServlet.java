package db.training.osb.web.babett;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rendered einen Gant-Chart
 * 
 * @author Sebastian Hennebrueder Date: 03.03.2010
 */
public class BaubetriebsplanServlet extends HttpServlet {

	private static final long serialVersionUID = 4634953088576135921L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	    IOException {
		// BaubetriebsplanReport report = (BaubetriebsplanReport) req.getSession().getAttribute(
		// BaubetriebsplanAction.SESSION_KEY_REPORT);
		req.getSession().removeAttribute(BaubetriebsplanAction.SESSION_KEY_REPORT);

		// IntervalCategoryDataset dataset = report.getMassnahmeReportList().get(0).getTaskSeries();

		// for (BaubetriebsplanReport.RegelungReport regelungReport :
		// report.getMassnahmeReportList()) {
		//
		// }

		// final JFreeChart chart = ChartFactory.createGanttChart(
		// "Gantt Chart Demo", // chart title
		// "Task", // domain axis label
		// "Date", // range axis label
		// dataset, // data
		// true, // include legend
		// true, // tooltips
		// false // urls
		// );

		// final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		// final CategoryItemRenderer itemRenderer = plot.getRenderer();
		//
		// resp.setContentType("image/png");
		//
		// BufferedImage img = new BufferedImage(400, 100, BufferedImage.TYPE_INT_RGB);
		// Graphics2D g2 = img.createGraphics();
		// chart.draw(g2, new Rectangle2D.Double(0, 0, 400, 100), new ChartRenderingInfo());
		//
		// ChartUtilities.writeBufferedImageAsPNG(resp.getOutputStream(), img);

	}
}
