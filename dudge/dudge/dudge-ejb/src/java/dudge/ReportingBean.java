/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import dudge.db.Contest;
import dudge.db.Role;
import dudge.db.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.*;
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

    @Override
    public File printContestParticipants(int contestId) throws IOException {

        if (contestId > 0) {

            Contest contest = (Contest) em.createQuery(
                    "SELECT c FROM Contest c "
                    + "WHERE c.contestId = :contestId", Contest.class).setParameter("contestId", contestId).getSingleResult();

            String path = ReportingBean.class.getResource("templates/ContestParticipants_RptTemplate.xlsx").getPath().substring(1);
            FileInputStream fis = new FileInputStream(path);

            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Лист1");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Row row = sheet.getRow(1);
            row.createCell(2);
            setCellValue(row, 2, contest.getCaption());
            row.createCell(4);
            setCellValue(row, 4, sdf.format(contest.getStartTime()));

            List<User> users = new ArrayList<User>();
            for (Role role : contest.getRoles()) {
                users.add(role.getUser());
            }

            int i = 4;
            for (User user : users) {
                copyRow(workbook, sheet, i, i + 1);
                row = sheet.getRow(i++);

                setCellValue(row, 1, user.getLogin());
                setCellValue(row, 2, user.getRealName());
                setCellValue(row, 3, user.getOrganization());
                setCellValue(row, 4, user.getFaculty());
                setCellValue(row, 5, user.getCourse());
                setCellValue(row, 6, user.getGroup());
            }
            sheet.removeRow(sheet.getRow(i));

            FileOutputStream fos = null;
            try {
                File reportFile = new File(String.format("ContestParticipants_%s.xlsx", System.currentTimeMillis()));
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

    private void setCellValue(Row row, int cellIndex, Object value) {
        if (value != null) {
            Cell cell = row.getCell(cellIndex);
            cell.setCellValue(String.valueOf(value));
        }
    }

    private static void copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

    }
}
