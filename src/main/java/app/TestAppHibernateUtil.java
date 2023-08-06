package app;

import org.hibernate.Session;
//import core.util.*;
import core.util.HibernateUtil;
import web.member.entity.Member;

public class TestAppHibernateUtil {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Member member = session.get(Member.class, 1);
		System.out.println(member.getNickname());
		HibernateUtil.shutdown();
	}
}