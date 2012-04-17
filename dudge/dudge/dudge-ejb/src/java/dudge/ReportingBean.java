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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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

            // Заголовок

            List<String> headers = new ArrayList<String>() {

                {
                    add("Название соревнования");
                    add("Допустимые языки соревнования");
                    add("Задания соревнования");
                    add("Описание соревнования");
                    add("Продолжительность соревнования");
                    add("Дата окончания соревнования");
                    add("Время от конца соревнования, за которое будет заморожен монитор");
                    add("Роли");
                    add("Правила соревнования");
                    add("Решения");
                    add("Дата начала соревнования");
                    add("Тип соревнования");
                }
            };

            for (int i = 0; i < 12; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers.get(i));
            }

            // Заполнение листа

            row = sheet.createRow(1);
            
            // Название соревнования
            cell = row.createCell(0);
            cell.setCellValue(contest.getCaption());

            // Допустимые языки соревнования
            cell = row.createCell(1);
            cell.setCellValue(contest.getContestLanguages().toString());

            // Задания соревнования
            cell = row.createCell(2);
            cell.setCellValue(contest.getContestProblems().toString());

            // Описание соревнования
            cell = row.createCell(3);
            cell.setCellValue(contest.getDescription());

            // Продолжительность соревнования
            cell = row.createCell(4);
            cell.setCellValue(contest.getDuration());

            // Дата окончания соревнования
            cell = row.createCell(5);
            cell.setCellValue(contest.getEndTime());

            // Время от конца соревнования, за которое будет заморожен монитор
            cell = row.createCell(6);
            cell.setCellValue(contest.getFreezeTime());

            // Роли
            cell = row.createCell(7);
            cell.setCellValue(contest.getRoles().toString());

            // Правила соревнования
            cell = row.createCell(8);
            cell.setCellValue(contest.getRules());

            // Решения
            cell = row.createCell(9);
            cell.setCellValue(contest.getSolutions().toString());

            // Дата начала соревнования
            cell = row.createCell(10);
            cell.setCellValue(contest.getStartTime());

            // Тип соревнования
            cell = row.createCell(11);
            cell.setCellValue(contest.getTraits().getMonitorSuffix());


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
