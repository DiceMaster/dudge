package dudge;

import dudge.db.RoleType;
import dudge.db.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface UserLocal {

	public String calcHash(String password);

	/**
	 * Аутентифицирует пользователя по его имени и паролю. При успешной аутентификации ее состояние будет запомнено на всю последующую сессию.
	 *
	 * @param login имя пользователя.
	 * @param password пароль пользователя.
	 * @return true при успешной аутентификации, иначе false.
	 */
	boolean authenticate(String login, String password);

	/**
	 * Проверяет наличие у пользователя права в соревновании.
	 *
	 * @param login имя пользователя.
	 * @contestId идентификатор соревнования.
	 * @roleType роль пользователя в соревновании.
	 * @return true при наличии данного права у пользователя в этом соревновании, иначе false.
	 */
	boolean isInRole(String login, int contestId, RoleType roleType);

	/**
	 * Проверяет отсутствие у пользователя каких-либо ролей в соревновании.
	 *
	 * @param login имя пользователя.
	 * @contestId идентификатор соревнования.
	 * @return true при наличии у пользователя какой-либо роли в этом соревновании, иначе false.
	 */
	boolean haveNoRoles(String login, int contestId);

	/**
	 * Позволяет получить пользователя по его имени.
	 *
	 * @param login имя пользователя.
	 * @return информация о пользователе.
	 */
	User getUser(String login);

	/**
	 * Регистрирует нового пользователя в системе.
	 *
	 * @param login имя нового пользователя (то же, что и в параметре user).
	 * @param password пароль пользователя.
	 * @param e-mail пользователя.
	 * @return созданный пользователь.
	 */
	public User registerUser(String login, String password, String email);

	/**
	 * Модифицирует данные пользователя.
	 *
	 * @param user пользователь с измененными полями.
	 */
	void modifyUser(User user);

	/**
	 * Удаляет из системы выбранного пользователя.
	 *
	 * @param login Имя удаляемого пользователя.
	 */
	public void deleteUser(String login);

	/**
	 * Возвращает количество пользователей, зарегестрированных в системе.
	 *
	 * @return Количество пользователей системы.
	 */
	long getUsersCount();
        
       	/**
	 * Возвращает всех пользователей, зарегестрированных в системе.
	 *
	 * @return Список пользователей системы.
	 */
	List<User> getUsers();

       	/**
	 * Возвращает диапазон пользователей, зарегестрированных в системе.
	 *
         * @param start Начало диапазона.
         * @param length Длина диапазона. 
	 * @return Диапазон пользователей системы.
	 */
	List<User> getUsersRange(int start, int length);
}
