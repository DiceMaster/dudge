package dudge;

import dudge.db.Language;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mikhail Ivanov
 */
@Stateless
public class LanguageBean implements LanguageLocal {

	private static final Logger logger = Logger.getLogger(LanguageBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @param languageId
	 * @return
	 */
	@Override
	public Language getLanguage(String languageId) {
		return (Language) em.find(Language.class, languageId);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Language> getLanguages() {
		return (List<Language>) em.createNamedQuery("Language.getLanguages").getResultList();
	}

	/**
	 *
	 * @param language
	 * @return
	 */
	@Override
	public Language addLanguage(Language language) {
		em.persist(language);
		em.flush();
		return language;
	}

	/**
	 *
	 * @param language
	 */
	@Override
	public void modifyLanguage(Language language) {
		em.merge(language);
	}

	/**
	 *
	 * @param languageId
	 */
	@Override
	public void deleteLanguage(String languageId) {
		em.remove((Language) em.find(Language.class, languageId));
	}
}
