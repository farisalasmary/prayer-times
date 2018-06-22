
/**
    @author
          ______         _                  _
         |  ____|       (_)           /\   | |
         | |__ __ _ _ __ _ ___       /  \  | | __ _ ___ _ __ ___   __ _ _ __ _   _
         |  __/ _` | '__| / __|     / /\ \ | |/ _` / __| '_ ` _ \ / _` | '__| | | |
         | | | (_| | |  | \__ \    / ____ \| | (_| \__ \ | | | | | (_| | |  | |_| |
         |_|  \__,_|_|  |_|___/   /_/    \_\_|\__,_|___/_| |_| |_|\__,_|_|   \__, |
                                                                              __/ |
                                                                             |___/
            Email: farisalasmary@gmail.com
            Date:  Jun 22, 2018
*/


public class test {

	public static void main(String[] args){
		
		PrayerTimes p = new PrayerTimes(18.3053030, 42.7292820, 3.0, "Khamis Mushait");

//		System.out.println(p.getFajr());
//		System.out.println(PrayerTimes.addMinutes(p.getFajr(), 3600));
//		System.out.println(PrayerTimes.addMinutes(PrayerTimes.addMinutes(p.getFajr(), 3600), 480));
//		
//		System.out.println(PrayerTimes.addMinutes(p.getFajr(), -3600));
//
//		System.out.println(p.getFajr());
//		System.out.println(PrayerTimes.addMinutes(p.getFajr(), 20));
//		System.out.println(PrayerTimes.isInBetween("04:34 AM", p.getFajr(),
//						   PrayerTimes.addMinutes(p.getFajr(), 20)));
		
		System.out.println("04:15 AM" +  PrayerTimes.addMinutes("04:03 AM", 10) +
				   PrayerTimes.addMinutes("04:03 AM", 20));
		System.out.println(PrayerTimes.isBetween("04:15 AM", PrayerTimes.addMinutes("04:03 AM", 10),
						   PrayerTimes.addMinutes("04:03 AM", 20)));
		
		
	//	System.out.println(p);
		
	}

}
