/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.db;

/**
 *
 * @author olorin
 */
public enum ApplicationStatus {

	/**
	 * Новая заявка -- решение об ее одобрении или отклонении еще не принималось.
	 */
	NEW,
	
	/**
	 * Заявка принята --- соотв. пользователь добавлен в участники соотв. 
	 * соревнования с ролью USER.
	 */
	ACCEPTED,
	
	/**
	 * Заявка отклонена.
	 */
	DECLINED
}
