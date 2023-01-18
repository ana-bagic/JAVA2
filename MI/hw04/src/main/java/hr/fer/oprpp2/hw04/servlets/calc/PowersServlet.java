package hr.fer.oprpp2.hw04.servlets.calc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw04.util.ErrorUtil;

/**
 * Servlet used to generate excel table with powers of numbers.
 * 
 * @author Ana Bagić
 *
 */
@WebServlet(urlPatterns = "/powers")
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aS = req.getParameter("a");
		String bS = req.getParameter("b");
		String nS = req.getParameter("n");

		int a, b, n;
		try {
			a = Integer.parseInt(aS);
			if (a < -100 || a > 100) throw new Exception();
		} catch (Exception e) {
			ErrorUtil.sendErrorMessage(req, resp, "Parameter \"a\" has to be an Integer in interval [-100,100].");
			return;
		}

		try {
			b = Integer.parseInt(bS);
			if (b < -100 || b > 100) throw new Exception();
		} catch (Exception e) {
			ErrorUtil.sendErrorMessage(req, resp, "Parameter \"b\" has to be an Integer in interval [-100,100].");
			return;
		}

		try {
			n = Integer.parseInt(nS);
			if (n < 1 || n > 5) throw new Exception();
		} catch (Exception e) {
			ErrorUtil.sendErrorMessage(req, resp, "Parameter \"n\" has to be an Integer in interval [1,5].");
			return;
		}
		
		resp.setContentType("application/vnd.ms-excel");
	    resp.setHeader("Content-Disposition", "attachment; filename=table.xls");

		try (HSSFWorkbook hwb = new HSSFWorkbook();) {
			for(int i = 1; i <= n; i++) {
				HSSFSheet sheet = hwb.createSheet(i + ". sheet");

				HSSFRow rowhead = sheet.createRow((short) 0);
				rowhead.createCell((short) 0).setCellValue("x");
				rowhead.createCell((short) 1).setCellValue("x^" + i);

				for(int j = a, rowNr = 1; j <= b; j++, rowNr++) {
					HSSFRow row = sheet.createRow((short) rowNr);
					row.createCell((short) 0).setCellValue(j);
					row.createCell((short) 1).setCellValue(Math.pow(j, i));
				}
			}
			
			hwb.write(resp.getOutputStream());
		} catch (Exception ex) {}
	}

}
