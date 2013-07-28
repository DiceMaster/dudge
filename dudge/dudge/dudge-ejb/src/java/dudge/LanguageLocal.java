package dudge;

import dudge.db.Language;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface LanguageLocal {

	/**
	 * Позволяет получить язык по его идентификатору.
	 *
	 * @param languageId строка-идентификатор языка.
	 * @return язык программирования.
	 */
	Language getLanguage(String languageId);

	/**
	 * Возвращает все языки, поддерживаемые системой.
	 *
	 * @return список поддерживаемых системой языков программирования.
	 */
	List<Language> getLanguages();

	/**
	 * Добавляет новый язык в систему.
	 *
	 * @param language язык для добавления.
	 * @return добавленный язык программирования.
	 */
	Language addLanguage(Language language);

	/**
	 * Изменяет существующий в системе язык.
	 *
	 * @param language язык программирования с измененными полями.
	 */
	void modifyLanguage(Language language);

	/**
	 * Удаляет из системы выбранный язык программирования.
	 *
	 * @param languageId
	 */
	public void deleteLanguage(String languageId);
}
