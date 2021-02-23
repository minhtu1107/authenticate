package com.taira.cntl.persistent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.taira.cntl.common.Constant;
import com.taira.cntl.dto.UserDTO;
import com.taira.cntl.entity.User;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllUser(Integer page) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer("FROM User as u");
		Query query = entityManager.createQuery(hql.toString());
		
		if(page != null && page >= 0) {
			query.setFirstResult(page * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			Long total = countUser();
			result.put("total", total);
			result.put("pageCount", (total % Constant.ITEM_PER_PAGE) > 0 ? (total / Constant.ITEM_PER_PAGE + 1)
					: (total / Constant.ITEM_PER_PAGE));
		}
		
		List<UserDTO> users = new ArrayList<UserDTO>();
		try {
			List<User> u = query.getResultList();
			u.forEach(c -> {
				UserDTO d = new UserDTO(c);
				users.add(d);
			});
		} catch (Exception e) {
		}
		
		result.put("userList", users);
		
		return result;
	}

	private Long countUser() {
		StringBuffer countHql = new StringBuffer("SELECT count(u.id) FROM User as u");
		Query countQuery = entityManager.createQuery(countHql.toString());
		Long count = 0l;
		try {
			count = Long.parseLong(countQuery.getSingleResult().toString());
		} catch (Exception e) {
		}
		return count;
	}
}
