/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge;

import java.io.File;
import java.io.IOException;
import javax.ejb.Local;

/**
 *
 * @author Mikhail
 */
@Local
public interface ReportingLocal {

    public File printContestInfo(int contestId) throws IOException;

}
