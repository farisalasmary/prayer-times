


public class test {

	public static void main(String[] args){
		
		double Latitude = 18.3053030, Longitude = 42.7292820, GMT = 3.0;
		String citryName = "Khamis Mushait"; 
		
		PrayerTimes p = new PrayerTimes(Latitude, Longitude, GMT, citryName);


        // check if a given time is between a specific interval
		String time = "04:15 AM";
		System.out.println("Is this time: " + time + " between " +  PrayerTimes.addMinutes("04:03 AM", 10) + " and " +
				   PrayerTimes.addMinutes("04:03 AM", 20) + "?");
		
		if(PrayerTimes.isBetween(time, PrayerTimes.addMinutes("04:03 AM", 10), PrayerTimes.addMinutes("04:03 AM", 20))){
		    System.out.println("Yes, It is!");
		}else{
		    System.out.println("No, It is NOT!");
		}
		
		System.out.println("\nGetting ALL prayer times as an array and printing them\n");
		String prayers[] = p.getPrayerTimes();
		for(int i = 0; i < prayers.length; i++)
		    System.out.println(prayers[i]);
		
		System.out.println("\nPrinting the content returned from the toString() method\n");
	    System.out.println(p);  // print the toString() method of the class
		
	}

}
