package hr.fer.oprpp2.hw04.servlets.voting;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw04.Band;
import hr.fer.oprpp2.hw04.util.BandsUtil;

/**
 * Servlet used to generate voting results as a pie chart.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Band> bands = BandsUtil.getBands(req);
		Map<Integer, Integer> votes = BandsUtil.getVotes(req);
		
		bands.forEach(b -> b.setVotes(votes.get(b.getId())));
		
		resp.setContentType("image/png");
		OutputStream outputStream = resp.getOutputStream();	
		
		JFreeChart chart = createChart(bands, "Which band is your favourite?");
	
		ChartUtils.writeChartAsPNG(outputStream, chart, 500, 500);
	}
	
    /**
     * Creates a chart with given title.
     * 
     * @param bands list of bands to display
     * @param title of the chart
     * @return generated chart
     */
    private JFreeChart createChart(List<Band> bands, String title) {
    	DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
    	bands.forEach(b -> dataset.setValue(b.getName(), b.getVotes()));
        
        JFreeChart chart = ChartFactory
        		.createPieChart(title, dataset, dataset, 0, false, true, false, false, false, false);
        return chart;
    }
}
