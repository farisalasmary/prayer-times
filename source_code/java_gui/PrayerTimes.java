
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


import java.util.Date;
import java.util.Calendar;


public class PrayerTimes{

	private boolean isUAQS; // is Umm AlQura settings ?
	private String Name;
	private double Latitude;
	private double Longitude;
	private double Dec; // The Sun Declination
	private double EqTime; // The Equation of Time
	private double RefLong;
	private double FajrAngle;
	private double IshaAngle;
	private double GMT;
	private int AsrL;
	private int y; // year in gregorian date
	private int m; // month in gregorian date
	private int d; // day in gregorian date
	private int Y; // year in Hijri date
	private int M; // month in Hijri date
	private int D; // day in Hijri date
///////////////////////////////////////////////////////////////////////////////////////////

	public PrayerTimes(int y, int m, int d, double Lat, double Long, double GMT, String Name){
		this.initializing(y, m, d, Lat, Long, GMT, Name);
	}
	
	public PrayerTimes(){ 
		this(21.422510, 39.826168, +3); // The coordinates of Makkah
	}

	public PrayerTimes(double Lat, double Long){
		this(Lat, Long, 0);
	}

	public PrayerTimes(double Lat, double Long, double GMT){
		this(Lat, Long, GMT, "Unknown");
	}
	
	public PrayerTimes(double Lat, double Long, double GMT, String Name){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// months start from 0 not 1! so we have to add +1 to the number of months
		this.initializing(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), Lat, Long, GMT, Name);
	}

	 // copy constructor
	public PrayerTimes(PrayerTimes p){

		this.isUAQS    = p.isUAQS ;
		this.Latitude  = p.Latitude;
		this.Longitude = p.Longitude;
		this.Dec       = p.Dec;
		this.EqTime    = p.EqTime;
	 	this.RefLong   = p.RefLong;
		this.FajrAngle = p.FajrAngle;
		this.IshaAngle = p.IshaAngle;
	 	this.AsrL      = p.AsrL;
	 	this.GMT       = p.GMT;
	 	this.y         = p.y;
	 	this.m         = p.m;
	 	this.d         = p.d;
	 	this.Y         = p.Y;
	 	this.M         = p.M;
	 	this.D         = p.D;

	}

	
	private void initializing(int y, int m, int d, double Lat, double Long, double GMT, String Name){
		this.setName(Name);
		
		this.setGregorianYear(y);
		this.setGregorianMonth(m);
		this.setGregorianDay(d);
		
		int HijriDate[] = JulianDayToHijri(GregorianToJulianDay(this.y, this.m, this.d));
		this.setHijriYear(HijriDate[0]);
		this.setHijriMonth(HijriDate[1]);
		this.setHijriDay(HijriDate[2]);
		
		this.setLongitude(Long);
		this.setLatitude(Lat);
		this.setGMT(GMT);
		this.RefLong = this.GMT * 15;
		this.SolarCoordinates(this.y, this.m, this.d);
		this.UmmAlQuraSettings();
		this.AsrInShafai();
	}
///////////////////////////////////////////////////////////////////////////////////////////

	public String getGregorianDate(){ return String.format("%02d / %02d / %04d", this.d, this.m, this.y); }
	public String getHijriDate()	{ return String.format("%02d / %02d / %04d", this.D, this.M, this.Y); }
	public String getFajr()			{ return FormatHours(Fajr()); }
	public String getShrouq()		{ return FormatHours(Shrouq()); }
	public String getDhuhr()		{ return FormatHours(Dhuhr()); }
	public String getAsr()			{ return FormatHours(Asr()); }
	public String getMaghrib()		{ return FormatHours(Maghrib()); }

	public String getIsha(){
		if(isUAQS) return FormatHoursForUAQS(Isha());
		else	   return FormatHours(Isha());
	}
	
	public String getName()			 { return Name; }
	public double getLatitude()	 	 { return this.Latitude; }
	public double getLongitude()	 { return this.Longitude; }
	public double getGMT()	    	 { return this.GMT; }
	public int    getGregorianYear() { return this.y; }
	public int    getGregorianMonth(){ return this.m; }
	public int    getGregorianDay()  { return this.d; }
	public int    getHijriYear()	 { return this.Y; }
	public int    getHijriMonth()	 { return this.M; }
	public int    getHijriDay()	  	 { return this.D; }


	/*
	public int[][] getPrayerTimes(){
		
		int PrayerTimes[][] = new int[6][2];

		PrayerTimes[0] = FormatPrayerTime(Fajr());
		PrayerTimes[1] = FormatPrayerTime(Shrouq());
		PrayerTimes[2] = FormatPrayerTime(Dhuhr());
		PrayerTimes[3] = FormatPrayerTime(Asr());
		PrayerTimes[4] = FormatPrayerTime(Maghrib());
		
		if(isUAQS)
			PrayerTimes[5] = FormatPrayerTimeForUAQS(Isha());
		else
			PrayerTimes[5] = FormatPrayerTime(Isha());

		return PrayerTimes;

	}*/
	
	public String[] getPrayerTimes(){
		String prayers[] = new String[6];
		
		prayers[0] = getFajr();
		prayers[1] = getShrouq();
		prayers[2] = getDhuhr();
		prayers[3] = getAsr();
		prayers[4] = getMaghrib();
		prayers[5] = getIsha();
		
		return prayers;
	}

///////////////////////////////////////////////////////////////////////////////////////////
	
	public void setName(String Name) {
		if(Name == null)
			throw new NullPointerException("There is no name");
		this.Name = Name;
	}
	
	public void setLatitude(double Lat){
		if(Lat > 90.0	|| Lat < -90.0)
			throw new IllegalArgumentException("The latitude should be between +90 and -90!");
		this.Latitude = Lat; //this will be executed only if the previous condition is NOT correct
				
	}

	public void setLongitude(double Long){
		if(Long > 180.0 || Long < -180.0)
			throw new IllegalArgumentException("The longitude should be between +180 and -180!");
		this.Longitude = Long; //this will be executed only if the previous condition is NOT correct

	}

	public void setGMT(double GMT){
		if(GMT > 12.0	|| GMT < -14.0)
			throw new IllegalArgumentException("GMT should be between +12 and -14!");
		this.GMT = GMT; //this will be executed only if the previous condition is NOT correct
		this.RefLong = this.GMT * 15;
	}

	public void setGregorianYear(int y){
		if(y < 2000 || y > 99999)
			throw new IllegalArgumentException("The Gregorian year should be at least greater than 2000 and below 99999!");

		//these lines will be executed only if the previous condition is NOT correct
		this.y = y; 
		SolarCoordinates(this.y, this.m, this.d);
		
		int HijriDate[] = JulianDayToHijri(GregorianToJulianDay(this.y, this.m, this.d));
		this.Y = HijriDate[0];
		this.M = HijriDate[1];
		this.D = HijriDate[2];

	}

	public void setGregorianMonth(int m){
		if(m > 12 || m < 1)
			throw new IllegalArgumentException("The Gregorian month should be between 1 and 12!");

		//this will be executed only if the previous condition is NOT correct
		this.m = m; 
		SolarCoordinates(this.y, this.m, this.d);

		int HijriDate[] = JulianDayToHijri(GregorianToJulianDay(this.y, this.m, this.d));
		this.Y = HijriDate[0];
		this.M = HijriDate[1];
		this.D = HijriDate[2];
		
	}

	public void setGregorianDay(int d){
		if(d > 31 || d < 1)
			throw new IllegalArgumentException("The Gregorian day should be between 1 and 31!");

		//this will be executed only if the previous condition is NOT correct
		this.d = d;
		SolarCoordinates(this.y, this.m, this.d);
		
		int HijriDate[] = JulianDayToHijri(GregorianToJulianDay(this.y, this.m, this.d));
		this.Y = HijriDate[0];
		this.M = HijriDate[1];
		this.D = HijriDate[2];

	}

	public void setHijriYear(int Y){
		if(Y < 1)
			throw new IllegalArgumentException("The Hijri year should be at least greater than 0!");

		//these lines will be executed only if the previous condition is NOT correct
		this.Y = Y; 
	}

	public void setHijriMonth(int M){
		if(M > 12 || M < 1)
			throw new IllegalArgumentException("The Hijri month should be between 1 and 12!");

		//this will be executed only if the previous condition is NOT correct
		this.M = M; 
	}

	public void setHijriDay(int D){
		if(D > 30 || D < 1)
			throw new IllegalArgumentException("The Hijri day should be between 1 and 30!");

		//this will be executed only if the previous condition is NOT correct
		this.D = D;
	}

///////////////////////////////////////////////////////////////////////////////////////////

	public void UmmAlQuraSettings(){
		this.FajrAngle = 18.5;
		isUAQS = true;
	}

	public void MuslimWorldLeagueSettings(){
		this.FajrAngle = 18;
		this.IshaAngle = 17;
		isUAQS = false;
	} 

	public void ISNA_Settings(){
		this.FajrAngle = 15;
		this.IshaAngle = 15;
		isUAQS = false;
	} 

	public void EgyptianGeneralAuthorityOfSurveySettings(){
		this.FajrAngle = 19.5;
		this.IshaAngle = 17.5;
		isUAQS = false;
	} 

	public void UniversityOfIslamicSciencesSettings(){
		this.FajrAngle = 18;
		this.IshaAngle = 18;
		isUAQS = false;
	}

	public void UnionOfIslamicOrganizationsInFrance(){
		this.FajrAngle = 12;
		this.IshaAngle = 12;
		isUAQS = false;
	} 

	public void AsrInHanafi(){
		this.AsrL = 2;
	}

	public void AsrInShafai(){
		this.AsrL = 1;
	}
///////////////////////////////////////////////////////////////////////////////////////////

	private double Fajr()	{ return (Dhuhr() - T(this.FajrAngle)); }
	private double Shrouq()	{ return (Dhuhr() - T(0.83333)); }
	private double Dhuhr()	{ return (12.0 + ((this.RefLong - this.Longitude) / 15.0 - (this.EqTime))); }
	private double Asr()	{ return (Dhuhr()+ A(AsrL)); }
	private double Maghrib(){ return (Dhuhr() + T(0.83333)); }
	private double Isha()	{ 
		if(isUAQS) return Maghrib(); // return "Maghrib()" and add 90 mins (or 120 in Ramadan) to it for UmmAlQuraSettings
		else 	   return (Dhuhr() + T(this.IshaAngle)); // use the angle for the others
	}

///////////////////////////////////////////////////////////////////////////////////////////

	private double dtr(double d) 	{ return (d * Math.PI) / 180.0; }
	private double rtd(double r) 	{ return (r * 180.0) / Math.PI; }

	private double sin(double d) 	{ return Math.sin(dtr(d)); }
	private double cos(double d) 	{ return Math.cos(dtr(d)); }
	private double cot(double d) 	{ return ( 1 / Math.tan(dtr(d))); }

	private double arcsin(double d) { return rtd(Math.asin(d)); }
	private double arccos(double d) { return rtd(Math.acos(d)); }

	private double arccot(double x) { return rtd(Math.atan(1 / x)); }
	private double arctan2(double y, double x) {return rtd(Math.atan2(y, x)); }
	
	private int roundToMinute(double num){
		double fraction;
		int newNum;
		
		newNum = (int) Math.floor(num);
		fraction = num - newNum;
		
		 // if the number of seconds greater than 30s then round it, otherwise do not round
		if((fraction * 60) >= 30)
			return newNum + 1;
		else
			return newNum;
		
	}

///////////////////////////////////////////////////////////////////////////////////////////

	private double T(double t){
		return (1.0/ 15.0) * arccos((-sin(t)-sin(Dec) * sin(Latitude)) / (cos(Dec) * cos(Latitude)));
	}

	private double A(double t){
		
		//double a = arcsin(sin(this.Latitude)*sin(this.Dec) + cos(this.Latitude)*cos(this.Dec));
		double a = arcsin(cos(this.Latitude - this.Dec));
		double thetaBar = Math.abs(90.0 - arccot(t + cot(a)));
		double H = arccos((cos(thetaBar) - sin(this.Latitude)*sin(this.Dec)) / (cos(this.Latitude)*cos(this.Dec)));
		return H / 15.0;

	}
	

///////////////////////////////////////////////////////////////////////////////////////////

/*	
	private int[] FormatPrayerTime(double hrs) {

		// this method is to return "int[]" instead of "String" in "FormatHours()"
		int PrayerTime[] = new int[2];
		int hour = (int)hrs;
		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);

			while(min >= 60){
				hour++;
				min -= 60;
			}
		
			if(hour > 12)
				hour -= 12;	

			PrayerTime[0] = hour;
			PrayerTime[1] = min;
		
		return PrayerTime;

	}

	private int[] FormatPrayerTimeForUAQS(double hrs) { 

		// all methods for "Isha" are to add 90 mins to "Maghrib" time to get "Isha" time according to Umm-AlQura Settings
		int PrayerTime[] = new int[2];
		int hour = (int)hrs;
		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);
		
			if(this.M != 9) 
				min += 90; // this line which I'm talking about
			else
				min += 120;

			while(min >= 60){
				hour++;
				min -= 60;
			}
		
			if(hour > 12)
				hour -= 12;	

			PrayerTime[0] = hour;
			PrayerTime[1] = min;
		
		return PrayerTime;

	}
*/

	private String FormatHours(double hrs) {
		int hour = (int)hrs;
		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);

		while(min >= 60){
			hour++;
			min -= 60;
		}
		
		boolean isPM = false;
		// if the number of hours is greater than 12 such as 20 that means this is 
		// after noon and we have to subtract 12 from the total number of hours to
		// get the time in "AM PM" format.
		if(hour > 12){ 
			hour -= 12;
			isPM = true;
		}else if(hour == 12){
			isPM = true;
		}			

		return String.format("%02d:%02d ", hour, min) + (isPM ? "PM" : "AM");

	}

	private String FormatHoursForUAQS(double hrs) {
		
		// all methods for "Isha" are to add 90 mins to "Maghrib" time to get "Isha" time according to Umm-AlQura Settings
		
		int hour = (int)hrs;
  		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);
		
		if(this.M != 9) 
			min += 90; // this line which I'm talking about
		else
			min += 120;

		while(min >= 60){
			hour++;
			min -= 60;
		}
		
		boolean isPM = false;
		// if the number of hours is greater than 12 such as 20 that means this is 
		// after noon and we have to subtract 12 from the total number of hours to
		// get the time in "AM PM" format.
		if(hour > 12){ 
			hour -= 12;
			isPM = true;
		}else if(hour == 12){
			isPM = true;
		}			

		return String.format("%02d:%02d ", hour, min) + (isPM ? "PM" : "AM");

	}

	private double fixhour(double a) {

		while(a > 24)
			a -= 24;

		if(a < 0)
			a += 24;

    return a;

	}

	private double fixangle(double a) {
		
		while(a > 360)
			a -= 360;

		if(a < 0)
			a += 360;

    return a;

	}

///////////////////////////////////////////////////////////////////////////////////////////

	private void SolarCoordinates(int y, int m, int d){

		double D = GregorianToJulianDay(y, m, d) - 2451545.0;
		double g = fixangle(357.529 + 0.98560028 * D);
		double q = fixangle(280.459 + 0.98564736 * D);
		double L = fixangle(q + 1.915 * sin(g) + 0.020 * sin(2.0* g));
		//double R = 1.00014 - 0.01671 * cos(g) - 0.00014 * cos(2.0* g); // No need to use it
		double e = 23.439 - 0.00000036 * D;
		double RA = arctan2(cos(e) * sin(L), cos(L)) / 15.0;

    	this.Dec = arcsin(sin(e) * sin(L));
    	this.EqTime = q / 15.0 - fixhour(RA);
    
	}

///////////////////////////////////////////////////////////////////////////////////////////
	
	/*public double GregorianToJulianDay(int y, int m, int d) {

	    if (m <= 2){ y -= 1; m += 12; }
		
			return (367 * y) - ((7 * (y + ((m + 9) / 12.0))) / 4.0)
				 + ((275 * m) / 9.0) + d + 1721014;

		}*/
	
	
	private static double GregorianToJulianDay(int y, int m, int d) {

	    if (m <= 2){ y -= 1; m += 12; }

			int A, B;
			double JD;

			A = (int)(y / 100.0);
			B = 2 - A + (int)(A / 4.0);
			JD = (365.25 * (y + 4716)) + (30.6001 * (m + 1)) + d + B - 1524.5;	

			return JD;

	}

	public double HijriToJulianDay(int Y, int M, int D){

		return (int)((11 * Y + 3) / 30.0) + (354 * Y) + (30 * M) -
			   (int)((M - 1) / 2.0) + D + 1948440 - 385;

	}
		
	public int[] JulianDayToHijri(double JD){
		int L, N, J, Y, M, D;

			L = (int)(JD - 1948440 + 10632);
			N = (int)((L - 1) / 10631.0);
			L = L - (10631 * N) + 354;
			J = (int)((10985 - L) / 5316.0) * (int)((50 * L) / 17719.0) + 
				 (int)(L / 5670.0) * (int)((43 * L) / 15238.0);
			
			L = L - (int)((30 - J) / 15.0) * (int)((17719 * J) / 50.0) - 
					  (int)(J / 16.0) * (int)((15238 * J) / 43.0) + 29;

			M = (int)((24 * L) / 709.0); // Month
			D = L - (int)((709 * M) / 24.0); // Day
			Y = (30 * N) + J - 30; // Year

			int HijriDate[] = new int[3];
				
			HijriDate[0] = Y;
			HijriDate[1] = M;
			HijriDate[2] = D;

		return HijriDate;

	}

	public int[] JulianDayToGregorian(double JD){
		int L, N, J, I, Y, M, D;
			
			L = (int)(JD + 68569);
			N = (int)((4 * L) / 146097.0);
			L = L - (int)((146097 * N + 3) / 4.0);
			I = (int)((4000 * (L + 1)) / 1461001.0);
			L = L - (int)((1461 * I) / 4.0) + 31;
			J = (int)((80 * L) / 2447.0);
			D = L - (int)((2447 * J) / 80.0); // Day
			L = (int)(J / 11.0);
			M = J + 2 - (12 * L); // Month
			Y = 100 * (N - 49) + I + L; // Year

			int GregorianDate[] = new int[3];

			GregorianDate[0] = Y;
			GregorianDate[1] = M;
			GregorianDate[2] = D;

		return GregorianDate;

	}
	
	public static String dayOfTheWeek(int y, int m, int d){
		int i = (14 - m) / 12;
		y -= i;
		int day = ((((i * 12 + m - 2) * 13 - 1) / 5) + d + y + (y / 4) - (y / 100) + (y / 400)) % 7;
	    switch (day){
		    case 0: 
		      return "Sunday";
		    case 1: 
		      return "Monday";
		    case 2: 
		      return "Tuesday";
		    case 3: 
		      return "Wednesday";
		    case 4: 
		      return "Thursday";
		    case 5: 
		      return "Friday";
	    }
	    return "Saturday";
	}
	
	public String getDayOfTheWeek(){
	    return dayOfTheWeek(this.y, this.m, this.d);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////

	public static String convertToMilitaryTime(String time){
		int hours = (CharToInt(time.charAt(0)) * 10) + CharToInt(time.charAt(1));
		int mins  = (CharToInt(time.charAt(3)) * 10) + CharToInt(time.charAt(4));
		boolean isAM  = (time.charAt(6) == 'A');
		
		if(hours > 12 || mins > 59)
			throw new IllegalArgumentException("Invalid time!!");
		else if(time.length() != 8)
			throw new IllegalArgumentException("The time should be like this format : \"00:00 AM\"");
		
		if(isAM && hours == 12)
			hours = 0;
		else if(!isAM && hours != 12)
			hours += 12;
		
		return String.format("%02d:%02d", hours, mins);
	}
	
	public static boolean isBetween(String target, String start, String stop){
		
		if(target.length() != 8 || start.length() != 8 || stop.length() != 8)
			throw new IllegalArgumentException("The time should be like this format : \"00:00 AM\"");
		
		target = convertToMilitaryTime(target);
		start  = convertToMilitaryTime(start);
		stop   = convertToMilitaryTime(stop);
		
		int startHours = (CharToInt(start.charAt(0)) * 10) + CharToInt(start.charAt(1));
		int startMins  = (CharToInt(start.charAt(3)) * 10) + CharToInt(start.charAt(4));
		
		int stopHours = (CharToInt(stop.charAt(0)) * 10) + CharToInt(stop.charAt(1));
		int stopMins  = (CharToInt(stop.charAt(3)) * 10) + CharToInt(stop.charAt(4));
		
		int targetHours = (CharToInt(target.charAt(0)) * 10) + CharToInt(target.charAt(1));
		int targetMins  = (CharToInt(target.charAt(3)) * 10) + CharToInt(target.charAt(4));
	
		if(targetHours >= startHours && targetHours <= stopHours && startHours != stopHours)
			return true;
		else if(startHours == stopHours)
			if(targetMins >= startMins && targetMins <= stopMins)
				return true;
				

		return false; // otherwise return false
	}
	
	public static String addMinutes(String time, int minutes){
		if(minutes >= 0)
			return addMins(time, minutes);
		else
			return subtractMins(time, minutes);
	}
	
	private static String addMins(String time, int minutes){
		  
		if(time.length() != 8)
			throw new IllegalArgumentException("The time should be like this format : \"00:00 AM\""); 
		  
		int hours = (CharToInt(time.charAt(0)) * 10) + CharToInt(time.charAt(1));
		int mins  = (CharToInt(time.charAt(3)) * 10) + CharToInt(time.charAt(4));
		boolean isAM  = (time.charAt(6) == 'A');
		
		while(minutes >= 60){
			hours++;
	    	minutes -= 60;
		}
		
		mins += minutes; // add the remaining minutes to mins
		
		if(mins >= 60){ // if mins greater than 60 then convert it to hours + minutes
			hours++;
			mins -= 60;
		}
	  
		// check if isAM needs to be converted
		if(hours == 12){ 
			if(isAM)
				isAM = false;
			else
				isAM = true;		
		}else if(hours > 12){
			while(hours > 12){
				hours -= 12;
				if(isAM)
					isAM = false;
				else
					isAM = true;
			}
		}
		
		return String.format("%02d:%02d ", hours, Math.abs(mins)) + (isAM ? "AM" : "PM");
		
	}
	
	private static String subtractMins(String time, int minutes){
		
		if(time.length() != 8)
			throw new IllegalArgumentException("The time should be like this format : \"00:00 AM\""); 
		  
		int hours = (CharToInt(time.charAt(0)) * 10) + CharToInt(time.charAt(1));
		int mins  = (CharToInt(time.charAt(3)) * 10) + CharToInt(time.charAt(4));
		boolean isAM  = (time.charAt(6) == 'A');	
		
		while(Math.abs(minutes) >= 60){
			hours--;
			minutes += 60;
		}
				
		mins += minutes;
		
		if(mins < 0){ // to convert negative minutes into positive ones
			hours--;
			mins += 60;
		}
		
		// check if isAM needs to be converted
		if(hours == 00){ 
			hours = 12;		
		}else if(hours < 0){
			while(hours < 0){
				hours += 12;
				if(isAM)
					isAM = false;
				else
					isAM = true;
			}
		}
		
		return String.format("%02d:%02d ", hours, Math.abs(mins)) + (isAM ? "AM" : "PM");
		
	}
	
	private static int CharToInt(char chr){
		switch(chr){
			case '0' : return  0;
			case '1' : return  1;
			case '2' : return  2;
			case '3' : return  3;
			case '4' : return  4;
			case '5' : return  5;
			case '6' : return  6;
			case '7' : return  7;
			case '8' : return  8;
			case '9' : return  9;
			default  : return -1; // Error has occured
		}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean equals(PrayerTimes p){

		if(p == null)
			throw new NullPointerException();

	/*	int myPrayerTimes[][] = getPrayerTimes();
		int pPrayerTimes[][]  = p.getPrayerTimes();
		
		for(int i = 0; i < myPrayerTimes.length; i++)
			for(int j = 0; j < myPrayerTimes[0].length; j++)
				if(myPrayerTimes[i][j] != pPrayerTimes[i][j])
					return false;*/
		
		String thisPrayers[] = this.getPrayerTimes();
		String pPrayers[] 	 = p.getPrayerTimes();
		
		for(int i = 0; i < thisPrayers.length; i++)
			if(!thisPrayers[i].equalsIgnoreCase(pPrayers[i]))
				return false;
					
		return true;

	}

	public String toString(){
	
		return   "Name : "				+ getName()			 +
				 "\nHijri Date : " 		+ getHijriDate()	 +
				 "\nGregorian Date : "  + getGregorianDate() +
				 "\nDay of the week : " + getDayOfTheWeek()  +
				 "\n\nFajr :    "		+ getFajr()			 +
				 "\nShrouq :  " 		+ getShrouq()		 + 
				 "\nDhuhr :   " 		+ getDhuhr()		 +
				 "\nAsr :     " 		+ getAsr()			 + 
				 "\nMaghrib : " 		+ getMaghrib()		 + 
				 "\nIsha :    " 		+ getIsha();
	
	}

}

