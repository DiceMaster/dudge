/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import dudge.db.Contest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mikhail
 */
@Stateless
public class ReportingBean implements ReportingLocal {

    protected Logger logger = Logger.getLogger(ReportingBean.class.toString());
    @PersistenceContext
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public ReportingBean() {
    }

    @Override
    public File printContestInfo(int contestId) throws IOException {
        if (contestId > 0) {

            Contest contest = (Contest) em.createQuery(
                    "SELECT c FROM Contest c "
                    + "WHERE c.contestId = :contestId", Contest.class).setParameter("contestId", contestId).getSingleResult();

            String path = ReportingBean.class.getResource("templates/ContestInfo_RptTemplate.xlsx").getPath().substring(1);
            FileInputStream fis = new FileInputStream(path);

            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Лист1");

            Row row = sheet.getRow(0);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue(contest.getCaption());
            cell = row.createCell(1);
            cell.setCellValue(contest.getDescription());
            cell = row.createCell(2);
            cell.setCellValue(contest.getDuration());

            FileOutputStream fos = null;
            try {
                File reportFile = new File(String.format("ContestInfo_%s.xlsx", System.currentTimeMillis()));
                fos = new FileOutputStream(reportFile);
                workbook.write(fos);
                return reportFile;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
        
    }
}
