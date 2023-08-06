package app;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import core.util.HibernateUtil;
import web.emp.entity.Dept;
import web.emp.entity.Emp;
import web.member.entity.Member;

public class TestApp {

	public static void main(String[] args) {
		TestApp app = new TestApp();
		// 新增
//		Member member = new Member();
//		member.setUsername("使用者名稱");
//		member.setPassword("密碼");
//		member.setNickname("暱稱");
//		app.insert(member);
//		System.out.println(member.getId());

		// 刪除
//		System.out.println(app.deleteById(9));

		// 修改
//		Member member = new Member();
//		member.setId(1);
//		member.setPass(true);
//		member.setRoleId(1);
//		System.out.println(app.updateById(member));

		// 查詢(ById)
//		System.out.println(app.selectById(1).getNickname());

		//查詢(All)
//		app.selectAll().forEach(member -> System.out.println(member.getNickname()));
		// or
//		for(Member member : app.selectAll()) {
//			System.out.println(member.getNickname());
//		}
		
//		// Criteria寫法
//		SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
//		Session session = sessionFactory.openSession();
//		
//		// select USERNAME, NICKNAME
//		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
//		// from MEMBER
//		Root<Member> root = criteriaQuery.from(Member.class);
//		// where USERNAME = ? and PASSWORD = ?
//		criteriaQuery.where(criteriaBuilder.and(
//			criteriaBuilder.equal(root.get("username"), "admin"),
//			criteriaBuilder.equal(root.get("password"), "P@ssw0rd")
//		));
//		
//		// select USERNAME, NICKNAME
//		criteriaQuery.multiselect(root.get("username"), root.get("nickname"));
//		
//		// order by ID
//		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
//		
//		Member member = session.createQuery(criteriaQuery).uniqueResult();
//		System.out.println(member.getNickname());
		
//		// 測試1對N取值
//		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//		Session session = sessionFactory.openSession();
//		
//		Dept dept = session.get(Dept.class, 30);
//		var emps = dept.getEmps();
//		for(var emp : emps) {
//			System.out.println(emp.getEname());
//		}
		
//		// 測試N對1取值
//		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//		Session session = sessionFactory.openSession();
//		
//		Emp emp = session.get(Emp.class, 7369);
//		Dept dept = emp.getDept();
//		System.out.println(dept.getDeptno());
//		System.out.println(dept.getDname());
		
		// 雙向1對N
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		Emp emp = session.get(Emp.class, 7369);
		Dept dept = emp.getDept();
		List<Emp> emps =dept.getEmps();
		for(Emp tmp : emps) {
			System.out.println(tmp.getEname());
		}
	}

	public Integer insert(Member member) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction(); // 開始交易
			session.persist(member); // 將實體物件新增⾄對應資料表中
			transaction.commit(); // 送交，同時會結束交易
			return member.getId();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback(); // 還原，同時會結束交易
			return null;
		}
	}

	public int deleteById(Integer id) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Member member = session.get(Member.class, id);
			session.remove(member);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return -1;
		}
	}

	public int updateById(Member newMember) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Member oldMember = session.get(Member.class, newMember.getId());
			// 如果傳pass
			final Boolean pass = newMember.getPass();
			if (pass != null) {
				oldMember.setPass(pass);
			}
			// 如果傳roleId
			final Integer roleId = newMember.getRoleId();
			if (roleId != null) {
				oldMember.setRoleId(roleId);
			}
			transaction.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return -1;
		}
	}
	public List<Member> selectAll() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query<Member> query = session.createQuery(
					"SELECT new web.member.pojo.Member(username, nickname) FROM Member", Member.class);
			List<Member> list = query.getResultList();
			transaction.commit();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}
}