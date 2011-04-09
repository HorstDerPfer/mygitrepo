package db.training.osb.web.ajax.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

public class BbpGanttRenderer extends GanttRenderer {

	private static final long serialVersionUID = -2083855883044327292L;

	public BbpGanttRenderer() {
		super();
		setDefaultBarPainter(new StandardBarPainter());
		setShadowVisible(false);

		setSeriesPaint(0, Color.GRAY); // Maßnahme
		setSeriesPaint(1, Color.BLUE); // Gleissperrung
		setSeriesPaint(2, Color.BLUE); // Oberleitung
		setSeriesPaint(3, Color.GREEN); // Langsamfahren
	}

	@Override
	public CategoryItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea,
	    CategoryPlot plot, int rendererIndex, PlotRenderingInfo info) {

		// Basisimplementierung aufrufen
		return super.initialise(g2, dataArea, plot, rendererIndex, info);

		// Bereich neu einstellen

	}

}
