package hr.fer.oprpp2.hw04.servlets.voting;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw04.Band;
import hr.fer.oprpp2.hw04.util.BandsUtil;

/**
 * Servlet used to generate excel table with results of voting.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Band> bands = BandsUtil.getBands(req);
		Map<Integer, Integer> votes = BandsUtil.getVotes(req);
		bands.forEach(b -> b.setVotes(votes.get(b.getId())));
		bands.sort((b1, b2) -> b2.getVotes().compareTo(b1.getVotes()));
		
		resp.setContentType("application/vnd.ms-excel");
	    resp.setHeader("Content-Disposition", "attachment; filename=voting_table.xls");

		try (HSSFWorkbook hwb = new HSSFWorkbook();) {
			HSSFSheet sheet = hwb.createSheet("Voting results");

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("place");
			rowhead.createCell((short) 1).setCellValue("band");
			rowhead.createCell((short) 2).setCellValue("nr. of votes");
			rowhead.createCell((short) 3).setCellValue("popular song");

			int rowNr = 1;
			for(Band band : bands) {
				HSSFRow row = sheet.createRow((short) rowNr);
				row.createCell((short) 0).setCellValue(rowNr++ + ".");
				row.createCell((short) 1).setCellValue(band.getName());
				row.createCell((short) 2).setCellValue(band.getVotes());
				row.createCell((short) 3).setCellValue(band.getSongLink());
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			
			hwb.write(resp.getOutputStream());
		} catch (Exception ex) {}
	}
}
