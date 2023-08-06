package web.member.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import web.member.dao.MemberDao;
import web.member.entity.Member;

public class MemberDaoImpl implements MemberDao {

	@Override
	public int insert(Member member) {
		getSession().persist(member);
		return 1;
	}

	@Override
	public int deleteById(Integer id) {
		Session session = getSession();
		Member member = session.get(Member.class, id);
		session.remove(member);
		return 1;
	}

	@Override
	public int update(Member member) {
		final StringBuilder hql = new StringBuilder()
				.append("UPDATE Member SET ");
			final String password = member.getPassword();
			if (password != null && !password.isEmpty()) {
				hql.append("password = :password,");
			}
			hql.append("nickname = :nickname,")
				.append("pass = :pass,")
				.append("roleId = :roleId,")
				.append("updater = :updater,")
				.append("lastUpdatedDate = NOW() ")
				.append("WHERE username = :username");
			
			Query<?> query = getSession().createQuery(hql.toString());
			if(password != null && !password.isEmpty()) {
				query.setParameter("password", password);
			}
			
			return query
				.setParameter("nickname", member.getNickname())
				.setParameter("pass", member.getPass())
				.setParameter("roleId", member.getRoleId())
				.setParameter("updater", member.getUpdater())
				.setParameter("username", member.getUsername())
				.executeUpdate();
		//----------------------------------------------------------	
		// JDBC寫法		
//		final StringBuilder sql = new StringBuilder()
//			.append("update MEMBER set ");
//		int offset = 0;
//		final String password = member.getPassword();
//		if (password != null && !password.isEmpty()) {
//			sql.append("PASSWORD = ?,");
//			offset = 1;
//		}
//		sql.append("NICKNAME = ?,")
//			.append("PASS = ?,")
//			.append("ROLE_ID = ?,")
//			.append("UPDATER = ?,")
//			.append("LAST_UPDATED_DATE = NOW() ")
//			.append("where USERNAME = ?");
//		try (
//			Connection conn = getConnection();
//			PreparedStatement pstmt = conn.prepareStatement(sql.toString())
//		) {
//			if (password != null && !password.isEmpty()) {
//				pstmt.setString(1, member.getPassword());
//			}
//			pstmt.setString(1 + offset, member.getNickname());
//			pstmt.setBoolean(2 + offset, member.getPass());
//			pstmt.setInt(3 + offset, member.getRoleId());
//			pstmt.setString(4 + offset, member.getUpdater());
//			pstmt.setString(5 + offset, member.getUsername());
//			return pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return -1;
	}

	@Override
	public Member selectById(Integer id) {
		return getSession().get(Member.class, id);
	}

	@Override
	public List<Member> selectAll() {
		// HQL寫法
		final String hql = "FROM Member ORDER BY id";
		return getSession()
				.createQuery(hql, Member.class)
				.getResultList();
		
		//----------------------------------------------------------	
//		// JDBC寫法
//		final String sql = "select * from MEMBER order by ID";
//		try (
//			Connection conn = getConnection();
//			PreparedStatement pstmt = conn.prepareStatement(sql);
//			ResultSet rs = pstmt.executeQuery()) {
//			List<Member> list = new ArrayList<>();
//			while (rs.next()) {
//				Member member = new Member();
//				member.setId(rs.getInt("ID"));
//				member.setUsername(rs.getString("USERNAME"));
//				member.setPassword(rs.getString("PASSWORD"));
//				member.setNickname(rs.getString("NICKNAME"));
//				member.setPass(rs.getBoolean("PASS"));
//				member.setRoleId(rs.getInt("ROLE_ID"));
////				member.setCreator(rs.getString("CREATOR"));
////				member.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
////				member.setUpdater(rs.getString("UPDATER"));
////				member.setLastUpdatedDate(rs.getTimestamp("LAST_UPDATED_DATE"));
//				list.add(member);
//			}
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	@Override
	public Member selectByUsername(String username) {
		// Criteria
		Session session = getSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("username"), username));
		return session
				.createQuery(criteriaQuery)
				.uniqueResult();
		
//		// JDBC
//		final String sql = "select * from MEMBER where USERNAME = ?";
//		try (
//			Connection conn = getConnection();
//			PreparedStatement pstmt = conn.prepareStatement(sql)) {
//			pstmt.setString(1, username);
//			try (
//				ResultSet rs = pstmt.executeQuery()) {
//				if (rs.next()) {
//					Member member = new Member();
//					member.setId(rs.getInt("ID"));
//					member.setUsername(rs.getString("USERNAME"));
//					member.setPassword(rs.getString("PASSWORD"));
//					member.setNickname(rs.getString("NICKNAME"));
//					member.setPass(rs.getBoolean("PASS"));
//					member.setRoleId(rs.getInt("ROLE_ID"));
//					member.setCreator(rs.getString("CREATOR"));
//					member.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
//					member.setUpdater(rs.getString("UPDATER"));
//					member.setLastUpdatedDate(rs.getTimestamp("LAST_UPDATED_DATE"));
//					return member;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	@Override
	public Member selectForLogin(String username, String password) {
		// NativeSQL
		final String sql = "select * from MEMBER where USERNAME = :username and PASSWORD = :password";
		return getSession()
				.createNativeQuery(sql, Member.class)
				.setParameter("username", username)
				.setParameter("password", password)
				.uniqueResult();
		
		// JDBC
//		final String sql = "select * from MEMBER where USERNAME = ? and PASSWORD = ?";
//		try (
//			Connection conn = getConnection();
//			PreparedStatement pstmt = conn.prepareStatement(sql)) {
//			pstmt.setString(1, username);
//			pstmt.setString(2, password);
//			try (
//				ResultSet rs = pstmt.executeQuery()) {
//				if (rs.next()) {
//					Member member = new Member();
//					member.setId(rs.getInt("ID"));
//					member.setUsername(rs.getString("USERNAME"));
//					member.setPassword(rs.getString("PASSWORD"));
//					member.setNickname(rs.getString("NICKNAME"));
//					member.setPass(rs.getBoolean("PASS"));
//					member.setRoleId(rs.getInt("ROLE_ID"));
//					member.setCreator(rs.getString("CREATOR"));
//					member.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
//					member.setUpdater(rs.getString("UPDATER"));
//					member.setLastUpdatedDate(rs.getTimestamp("LAST_UPDATED_DATE"));
//					return member;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}
}
