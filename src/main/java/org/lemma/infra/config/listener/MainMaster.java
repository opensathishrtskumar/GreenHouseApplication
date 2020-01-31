package org.lemma.infra.config.listener;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.map.HashedMap;
import org.joda.time.LocalDate;

public class MainMaster {


	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		
	Map<String, Object> map = new HashedMap();
	float minValue;
	float maxValue;
	String minConsumeDate;
	String maxConsumeDate;
	minValue = -1;
	maxValue = 0;
	minConsumeDate = "";
	maxConsumeDate = "";	
	int difference = 3;
	String key;
	//Random rand = new Random();
	for (int i=1;i<31;i++) {
		key = String.valueOf(i);
		difference = difference + new Random().nextInt(10);

		map.put(key, difference);
		if(minValue ==-1 ) {
            minValue = difference;
            minConsumeDate = key;					
		}else if(difference < minValue){
            minValue = difference;
            minConsumeDate = key;
        }
		if(difference > maxValue){
			maxValue = difference;
			maxConsumeDate = key;
		}
	}
	System.out.println("MinDate=" + minConsumeDate + ";MaxDate=" + maxConsumeDate);
	long m = LocalDate.now().minusDays(1).toDate().getTime();//.getMonthOfYear();
	SimpleDateFormat originalFormat = new SimpleDateFormat("MMM");
	String date = originalFormat.format(m);
    System.out.println(date);
	}

}
