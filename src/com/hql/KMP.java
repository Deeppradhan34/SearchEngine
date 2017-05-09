package com.hql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
 
/** Class KnuthMorrisPratt **/
public class KMP
{
    /** Failure array **/
    private int[] failed;
    /** Constructor **/
    public boolean searchPattern(String text, String pattter)
    {
        /** pre construct failure array for a pattern **/
        failed = new int[pattter.length()];
        getFail(pattter);
        /** find match **/
        int position = findMatch(text, pattter);
        if (position == -1)
            return false;
        else
            return true;
    }
    /** Failure function for a pattern **/
    private void getFail(String pat)
    {
        int n = pat.length();
        failed[0] = -1;
        for (int j = 1; j < n; j++)
        {
            int i = failed[j - 1];
            while ((pat.charAt(j) != pat.charAt(i + 1)) && i >= 0)
                i = failed[i];
            if (pat.charAt(j) == pat.charAt(i + 1))
            	failed[j] = i + 1;
            else
            	failed[j] = -1;
        }
    }
    /** Function to find match for a pattern **/
    private int findMatch(String text, String pat)
    {
    	int value=0;
    	try{
        int i = 0, j = 0;
        int length = text.length();
        int patternLength = pat.length();
        while (i < length && j < patternLength)
        {
            if (text.charAt(i) == pat.charAt(j))
            {
                i++;
                j++;
            }
            else if (j == 0)
                i++;
            else
                j = failed[j - 1] + 1;
        }
        value =(j == patternLength) ? (i - patternLength) : -1;
    }
    	catch(Exception e){
    		
    	}
    	return value;
}
}