package hr.fer.oprpp2.hw04.servlets.report;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet used to generate image of pie chart.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/reportImage")
public class ReportImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		OutputStream outputStream = resp.getOutputStream();
		
		JFreeChart chart = createChart("What operating system are you using?");
		
		ChartUtils.writeChartAsPNG(outputStream, chart, 500, 500);
	}
    
    /**
     * Creates a chart with given title.
     * 
     * @param title of the chart
     * @return generated chart
     */
    private JFreeChart createChart(String title) {
    	DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
    	dataset.setValue("Linux", 29);
    	dataset.setValue("Mac", 20);
        dataset.setValue("Windows", 51);
        
        JFreeChart chart = ChartFactory
        		.createPieChart(title, dataset, dataset, 0, false, true, false, false, false, false);
        return chart;
    }
}
