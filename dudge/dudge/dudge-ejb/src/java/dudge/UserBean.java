package dudge;

import dudge.db.Role;
import dudge.db.RoleType;
import dudge.db.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mikhail Ivanov
 */
@Stateless
public class UserBean implements UserLocal {
    
	private static final Logger logger = Logger.getLogger(UserBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @param password
	 * @return
	 */
	@Override
	public String calcHash(String password) {

		try {
			byte[] bytes = password.getBytes("utf-8");

			java.security.MessageDigest algorithm = java.security.MessageDigest.getInstance("MD5");

			algorithm.reset();
			algorithm.update(bytes);
			byte messageDigest[] = algorithm.digest();

			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1) {
					hexString.append("0").append(hex);
				} else {
					hexString.append(hex);
				}
			}
			return hexString.toString();
		} catch (java.security.NoSuchAlgorithmException nsae) {
			logger.log(Level.SEVERE, "Unable to find hash algorithm.\n{0}", nsae.getMessage());
			return null;
		} catch (java.io.UnsupportedEncodingException uee) {
			logger.log(Level.SEVERE, "Unsupported hash encoding.\n{0}", uee.getMessage());
			return null;
		}
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @return
	 */
	@Override
	public boolean authenticate(String login, String password) {

		if (login == null) {
			throw new IllegalArgumentException("login is null.");
		}

		if (password == null) {
			throw new IllegalArgumentException("password is null.");
		}

		User dbUser = em.find(User.class, login.toLowerCase(Locale.ENGLISH));

		//Если пользователь не авторизован
		if (dbUser == null) {
			logger.log(Level.WARNING, "Authentication for user ''{0}'' failed - user does not exist.", login);
			return false;
		}

		if (!dbUser.getPwdHash().equals(calcHash(password))) {
			logger.log(Level.WARNING, "Authentication for user ''{0}'' failed - incorrect password. ", login);
			return false;
		}

		// Пользователь существует и пароль верен.
		return true;
	}

	/**
	 *
	 * @param login
	 * @param contestId
	 * @param roleType
	 * @return
	 */
	@Override
	public boolean isInRole(String login, int contestId, RoleType roleType) {
		long count = 0;
		if (login != null) {
			count = (Long) em.createQuery(
					"SELECT COUNT(r) FROM Role r WHERE r.contest.contestId = :contestId AND r.user.login = :username AND r.roleType = :roleType")
					.setParameter("contestId", contestId).setParameter("username", login.toLowerCase(Locale.ENGLISH)).setParameter("roleType", roleType.toString())
					.getResultList().get(0);
		}
		return count != 0;
	}

	/**
	 *
	 * @param login
	 * @param contestId
	 * @return
	 */
	@Override
	public boolean haveNoRoles(String login, int contestId) {
		long count = 0;
		if (login != null) {
			count = (Long) em.createQuery(
					"SELECT COUNT(r) FROM Role r WHERE r.contest.contestId = :contestId AND r.user.login = :username")
					.setParameter("contestId", contestId).setParameter("username", login.toLowerCase(Locale.ENGLISH)).getResultList().get(0);
		}
		return count == 0;
	}

	/**
	 *
	 * @param login
	 * @return
	 */
	@Override
	public User getUser(String login) {
		User dbuser = (User) em.find(User.class, login.toLowerCase(Locale.ENGLISH));
		return dbuser;
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @param email
	 * @return
	 */
	@Override
	public User registerUser(String login, String password, String email) {
		logger.finest("Attempting to register new user.");

		User dbUser = new User(login.toLowerCase(Locale.ENGLISH), calcHash(password), email);
		dbUser.setRolesCollection(new ArrayList<Role>());

		em.persist(dbUser);
		em.flush();
		return dbUser;
	}

	/**
	 *
	 * @param user
	 */
	@Override
	public void modifyUser(User user) {
		em.merge(user);
	}

	/**
	 *
	 * @param login
	 */
	@Override
	public void deleteUser(String login) {
		em.remove((User) em.find(User.class, login.toLowerCase(Locale.ENGLISH)));
	}

        /**
	 * Возвращает количество пользователей, зарегестрированных в системе.
	 *
	 * @return Количество пользователей системы.
	 */
        @Override
	public long getUsersCount()
        {
            return (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        }
        
	/**
	 *
	 * @return
	 */
	@Override
	public List<User> getUsers() {
            return (List<User>) em.createNamedQuery("User.getUsers").getResultList();
	}
        
	@Override
	public FilteredUsers getUsers(String searchCriteria, String orderBy, boolean descending, int start, int length) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery usersCriteriaQuery = builder.createQuery();
		Root usersRoot = usersCriteriaQuery.from(User.class);

		CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root countRoot = countCriteriaQuery.from(User.class);
		countCriteriaQuery.select(builder.count(countRoot));

		if (searchCriteria != null) {
			usersCriteriaQuery.where(builder.or(
					builder.like(usersRoot.get("login"), "%" + searchCriteria + "%"),                        
					builder.like(usersRoot.get("organization"), "%" + searchCriteria + "%"),
					builder.like(usersRoot.get("realName"), "%" + searchCriteria + "%")
				));
			countCriteriaQuery.where(builder.or(
					builder.like(countRoot.get("login"), "%" + searchCriteria + "%"),                        
					builder.like(countRoot.get("organization"), "%" + searchCriteria + "%"),
					builder.like(countRoot.get("realName"), "%" + searchCriteria + "%")
				));
		}

		final long count = em.createQuery(countCriteriaQuery).getSingleResult();

		if (orderBy != null) {
			usersCriteriaQuery.orderBy(descending ? builder.desc(usersRoot.get(orderBy)) : builder.asc(usersRoot.get(orderBy)));
		}

		Query usersQuery = em.createQuery(usersCriteriaQuery);
		if (start >= 0) {
			usersQuery = usersQuery.setFirstResult(start);
		}
		if (length > 0) {
			usersQuery = usersQuery.setMaxResults(length);
		}
		final List<User> filteredUsers = (List<User>) usersQuery.getResultList();

		return new FilteredUsers() {

			@Override
			public long getFilteredTotal() {
				return count;
			}

			@Override
			public List<User> getFilteredUsers() {
				return filteredUsers;
			}
		};
	}
}
