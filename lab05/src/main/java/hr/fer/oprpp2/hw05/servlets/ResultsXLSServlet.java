package hr.fer.oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.model.PollOption;

/**
 * Servlet used to generate excel table with the results of the voting.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/servleti/glasanje-xls")
public class ResultsXLSServlet extends HttpServlet {

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
		
		List<PollOption> options;
		try {
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
		
		resp.setContentType("application/vnd.ms-excel");
	    resp.setHeader("Content-Disposition", "attachment; filename=voting_table.xls");

		try (HSSFWorkbook hwb = new HSSFWorkbook();) {
			HSSFSheet sheet = hwb.createSheet("Voting results");

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("place");
			rowhead.createCell((short) 1).setCellValue("option");
			rowhead.createCell((short) 2).setCellValue("nr. of votes");
			rowhead.createCell((short) 3).setCellValue("popular song");

			int rowNr = 1;
			for(PollOption option : options) {
				HSSFRow row = sheet.createRow((short) rowNr);
				row.createCell((short) 0).setCellValue(rowNr++ + ".");
				row.createCell((short) 1).setCellValue(option.getTitle());
				row.createCell((short) 2).setCellValue(option.getVotesCount());
				row.createCell((short) 3).setCellValue(option.getLink());
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			
			hwb.write(resp.getOutputStream());
		} catch (Exception ex) {}
	}
}
