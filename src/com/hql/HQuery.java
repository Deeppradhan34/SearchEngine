package com.hql;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.food.Foods;


import hibernateHelper.HibernateUtil;

public class HQuery {

	public List<Foods> getAllFoods() {
		List<Foods> foods2 = new ArrayList<Foods>();
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			foods2 = session.createQuery("from Foods").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return foods2;
	}

	public List<Foods> getMaxHundredScore() {
		List<Foods> foods2 = new ArrayList<Foods>();
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			trns = session.beginTransaction();
			foods2 = session.createQuery(" from Foods order by score desc limit 200").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return foods2;
	}
	public List<Foods> getDetailsWithReviewScore(double score) {
		List<Foods> foods2 = new ArrayList<Foods>();
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Foods food = new Foods(); 
		try {
			trns = session.beginTransaction();
			String queryString = "from Foods  where score = :score order by reviewscore desc";
            Query query = session.createQuery(queryString);
            query.setDouble("score", score);
            foods2 =  query.list();

            
			//foods2 = session.createQuery(" from Foods order by score = :score order by reviewscore desc limit 20").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return foods2;
	}
	public List<Foods> getDocuments(int sno) {
		List<Foods> foods2 = new ArrayList<Foods>();
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Foods food = new Foods(); 
		try {
			trns = session.beginTransaction();
			String queryString = "from Foods  where sno = :sno ";
            Query query = session.createQuery(queryString);
            query.setDouble("sno", sno);
            foods2 =  query.list();

            
			//foods2 = session.createQuery(" from Foods order by score = :score order by reviewscore desc limit 20").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return foods2;
	}

	public void addScoreInDB(Foods food2) {
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			session.update(food2);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			if (trns != null) {
				trns.rollback();
			}
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
	}
	
	public List<Foods> getDistinctScores() {
		List<Foods> foods2 = new ArrayList<Foods>();
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			trns = session.beginTransaction();
			foods2 = session.createQuery(" from Foods group by score having count(*) = 1 order by score desc ").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return foods2;
	}
	
}
