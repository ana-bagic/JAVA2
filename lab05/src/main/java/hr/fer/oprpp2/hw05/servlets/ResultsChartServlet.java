package hr.fer.oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Servlet used to generate voting results as a pie chart.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/glasanje-grafika")
public class ResultsChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id;
		try {
			id = Long.parseLong(req.getParameter("pollID"));
		} catch (NumberFormatException e) {
			ErrorUtil.sendErrorMessage(req, resp, "Poll id has to be a whole number.");
			return;
		}
		
		String title;
		List<PollOption> options;
		try {
			title = DAOProvider.getDao().getPoll(id).getTitle();
			
			options = DAOProvider.getDao().getOptions(id);
			options.sort((b1, b2) -> Long.compare(b2.getVotesCount(), b1.getVotesCount()));
		} catch (DAOException e) {
			if (e.getCause() == null) {
				ErrorUtil.sendErrorMessage(req, resp, e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}
		
		JFreeChart chart = createChart(options, title);
	
		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 500, 500);
	}
	
    /**
     * Creates a chart with given title.
     * 
     * @param options list of options to display
     * @param title of the chart
     * @return generated chart
     */
    private JFreeChart createChart(List<PollOption> options, String title) {
    	DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
    	options.forEach(b -> dataset.setValue(b.getTitle(), b.getVotesCount()));
        
        JFreeChart chart = ChartFactory
        		.createPieChart(title, dataset, dataset, 0, false, true, false, false, false, false);
        return chart;
    }
}

