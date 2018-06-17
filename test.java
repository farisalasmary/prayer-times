


public class test {

	public static void main(String[] args){
		
		double Latitude = 18.3053030, Longitude = 42.7292820, GMT = 3.0;
		String citryName = "Khamis Mushait"; 
		
		PrayerTimes p = new PrayerTimes(Latitude, Longitude, GMT, citryName);

        p = new PrayerTimes(p); // construct new object using the data from the previous one

        // check if a given time is between a specific interval
		String time = "04:15 AM";
		System.out.println("Is this time: " + time + " between " +  PrayerTimes.addMinutes("04:03 AM", 10) + " and " +
				   PrayerTimes.addMinutes("04:03 AM", 20) + "?");
		
		if(PrayerTimes.isBetween(time, PrayerTimes.addMinutes("04:03 AM", 10), PrayerTimes.addMinutes("04:03 AM", 20))){
		    System.out.println("Yes, It is!");
		}else{
		    System.out.println("No, It is NOT!");
		}
		
		if(PrayerTimes.isBetween(time, PrayerTimes.addMinutes(04031, 10), PrayerTimes.addMinutes("04:03 AM", 20))){
		    System.out.println("Yes, It is!");
		}else{
		    System.out.println("No, It is NOT!");
		}
		
		System.out.println("\nGetting ALL prayer times as an array and printing them\n");
		int prayers[] = p.getPrayerTimes();
		
		System.out.println("Unformatted form");
		for(int i = 0; i < prayers.length; i++)
		    System.out.println(prayers[i]);
		
		System.out.println("\nFormatted form");
		for(int i = 0; i < prayers.length; i++)
		    System.out.println(PrayerTimes.formatTime(prayers[i]));
		
		System.out.println("\nPrinting the content returned from the toString() method\n");
	    System.out.println(p);  // print the toString() method of the class
		
	}

}
