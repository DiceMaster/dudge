/*
 * RoleType.java
 *
 * Created on 14 Май 2007 г., 16:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.db;

/**
 * Перечисление возможных ролей пользователя в соревновании.
 * @author virl
 */
public enum RoleType {
	/**
	 * Администратор - может делать что угодно
	 * в пределах соревнования.
	 */
	ADMINISTRATOR,
	
	/**
	 * Пользователь - может отправлять решения
	 * и смотреть монитор соревнования, а также свой статус.
	 */
	USER,
	
	/**
	 * Наблюдатель - может смотреть монитор соревнования.
	 */
	OBSERVER	
	}
