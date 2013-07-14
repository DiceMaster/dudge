/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import dudge.db.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    protected static final Logger logger = Logger.getLogger(ReportingBean.class.toString());
    @PersistenceContext
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public ReportingBean() {
    }

    @Override
    public File printContestProtocol(int contestId) throws IOException {
        if (contestId > 0) {

            Contest contest = (Contest) em.createQuery(
                    "SELECT c FROM Contest c "
                    + "WHERE c.contestId = :contestId", Contest.class).setParameter("contestId", contestId).getSingleResult();

            String path = ReportingBean.class.getResource("templates/ContestProtocol_RptTemplate.xlsx").getPath().substring(1);
            FileInputStream fis = new FileInputStream(path);

            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Лист1");

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Row row = sheet.getRow(1);
            row.createCell(2);
            setCellValue(row, 2, contest.getCaption());
            row.createCell(4);
            setCellValue(row, 4, sdf.format(contest.getStartTime()));

            List<ContestProblem> contestProblems = contest.getContestProblems();
            List<Problem> problems = new ArrayList<>();

            if (contestProblems != null && contestProblems.size() > 0) {
                for (ContestProblem contestProblem : contestProblems) {
                    problems.add(contestProblem.getProblem());
                }

                if (problems.size() > 0) {
                    Row headerRow = sheet.getRow(3);
                    CellStyle cs = workbook.createCellStyle();
                    cs.cloneStyleFrom(headerRow.getCell(6).getCellStyle());
                    Row templateRow = sheet.getRow(4);

                    int i = 7;
                    Cell newCell;
                    for (Problem problem : problems) {
                        newCell = headerRow.createCell(i);
                        newCell.setCellStyle(cs);
                        newCell.setCellValue(problem.getTitle());

                        newCell = templateRow.createCell(i);
                        newCell.setCellStyle(cs);
                        i++;
                    }
                }

                Map<User, Map<Problem, String>> results = new HashMap<>();
                for (Role role : contest.getRoles()) {
                    if (RoleType.USER.equals(role.getRoleType())) {
                        results.put(role.getUser(), new HashMap<Problem, String>());
                    }
                }

                List<Solution> solutions = (List<Solution>) contest.getSolutions();
                for (Solution solution : solutions) {

                    List<Run> runs = (List<Run>) solution.getRuns();
                    Collections.sort(runs);
                    StringBuilder runsResult = new StringBuilder();
                    for (Run run : runs) {
                        if (RunResultType.SUCCESS.equals(run.getResultType())) {
                            runsResult.append("+");
                        } else {
                            runsResult.append("-");
                        }
                    }

                    if (results.get(solution.getUser()) != null) {
                        results.get(solution.getUser()).put(solution.getProblem(), runsResult.toString());
                    }
                }

                int i = 4;
                for (User user : results.keySet()) {
                    copyRow(workbook, sheet, i, i + 1);
                    row = sheet.getRow(i++);

                    setCellValue(row, 2, user.getLogin());
                    setCellValue(row, 3, user.getOrganization());
                    setCellValue(row, 4, user.getFaculty());
                    setCellValue(row, 5, user.getCourse());
                    setCellValue(row, 6, user.getGroup());

                    int j = 7;
                    for (Problem problem : problems) {
                        if (results.get(user).get(problem) != null) {
                            setCellValue(row, j++, results.get(user).get(problem));
                        }
                    }

                }
                sheet.removeRow(sheet.getRow(i));

            }

            FileOutputStream fos = null;
            try {
                File reportFile = new File(String.format("ContestProtocol_%s.xlsx", System.currentTimeMillis()));
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

            List<User> users = new ArrayList<>();
            for (Role role : contest.getRoles()) {
                if (RoleType.USER.equals(role.getRoleType())) {
                    users.add(role.getUser());
                }
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
            if (cell == null) {
                cell = row.createCell(cellIndex);
            }
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
                    
                default:
                    newCell.setCellValue(oldCell.getStringCellValue());
            }
        }

    }
}
