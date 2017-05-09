package ControllerHquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.food.Foods;
import com.hql.HQuery;
import com.hql.KMP;

import hibernateHelper.HibernateUtil;
import java.util.*;
public class CHquery {

	
	HQuery hquery = new HQuery();
	ArrayList<Double> scoreArr = new ArrayList<Double>();
	ArrayList<Integer> documentsArr = new ArrayList<Integer>();
	ArrayList<Integer> UniqueScores = new ArrayList<Integer>();
	ArrayList<Double> duplicatesScores = new ArrayList<Double>();
	
	SessionFactory sf = HibernateUtil.getSessionFactory();
	Session session = sf.openSession();
	
	public void searchPatters(){
		
		session.beginTransaction();
		KMP kmp = new KMP();
		String checkKMP = null;
		
		HashMap<Integer, String> patternData = new HashMap<Integer, String>();
		
		Scanner noOfPatterns = new Scanner(System.in); 
		System.out.println("Enter  number of patterns to be searched: ");
		int countOfPattern = noOfPatterns.nextInt(); 
		
		Scanner enternPatter = new Scanner(System.in); 
		for(int i = 1; i<= countOfPattern; i++){
			System.out.println("Enter a pattern to be searched: ");
			String pattern = enternPatter.nextLine(); 
			patternData.put(i, pattern);
				
		}
		
		
		double score = 0.0;
		float sizePattern = patternData.size();
		

		for (Foods iter : hquery.getAllFoods()) {
			int count = 0;
			checkKMP = (iter.getSummary() + iter.getText());
			

			for (Map.Entry<Integer, String> entry : patternData.entrySet()) {

				String pattern = entry.getValue();

				boolean matchingStringFound = kmp.searchPattern(checkKMP, pattern);

				if (matchingStringFound) {
					count++;
				} 

			}
			score = count / sizePattern;
		
			 Foods food = (Foods)session.get(Foods.class, iter.getSno());
			 food.setScore(score);

		}	
		getTopHundredScores();
	}
	
	public void getTopHundredScores(){
		int k = 0;
		for (Foods iter : hquery.getMaxHundredScore()) {
			k++;
			double scoreValue = iter.getScore();

			scoreArr.add(scoreValue);
			
			if(k == 20)//top 20
				break;
			
		}
		getDuplicatesInScore();
	}
	
	public void getDuplicatesInScore(){

		double tempval = 0.0;
		int i;
		 
		for( i = 1; i < scoreArr.size(); i++) {
			
			int sameValue = Double.compare(scoreArr.get(i), scoreArr.get(i-1));
		    if(sameValue == 0 && (Double.compare(scoreArr.get(i), tempval)!=0)){
		    		tempval = scoreArr.get(i);
		    		duplicatesScores.add(tempval);
		    	
		     }
		         
		 }
		getDistinctScore();
	}
	
	public void getDistinctScore(){
     for (Foods iter : hquery.getDistinctScores()) {
			int sno = iter.getSno();
			System.out.println("distinct:"+sno);
			UniqueScores.add(sno);
			
		}
     getOrderedReviewScoreWithSno();
	}
	
	public void getOrderedReviewScoreWithSno(){
		int count = 0;
		while(duplicatesScores.size() > count){
			
			for (Foods iter : hquery.getDetailsWithReviewScore(duplicatesScores.get(count))) {
				int sno = iter.getSno();
				documentsArr.add(sno);
			}
			count++;
		}
		getDocuments();
	}
	
	public void getDocuments(){
		
		int count = 0;
		while(documentsArr.size() > count){
			
			for (Foods iter : hquery.getDocuments(documentsArr.get(count))) {
				String finalDocument = (iter.getSummary() + iter.getText());	
				System.out.println(finalDocument);
			}
			count++;
			
			}
		
		int counterDistinct = 0;
		while(UniqueScores.size() > counterDistinct){
			
			for (Foods iter31 : hquery.getDocuments(UniqueScores.get(counterDistinct))) {
				String finalDocument = (iter31.getSummary() + iter31.getText());	
				System.out.println(finalDocument);
			}
			counterDistinct++;
			
			}
	
		session.getTransaction().commit();
		session.close();
	}
}
