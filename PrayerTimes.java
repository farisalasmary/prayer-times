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
            Date:  Jul 14, 2015
*/


import java.util.Date;
import java.util.Calendar;


public class PrayerTimes{

    public static final int ASR_IN_SHAFAI = 1;
    public static final int ASR_IN_HANAFI = 2;
    	
    public static final int UMM_AL_QURA_SETTINGS = 1;
	public static final int MUSLIM_WORLD_LEAGUE_SETTINGS = 2;
    public static final int ISNA_SETTINGS = 3;
	public static final int EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY_SETTINGS = 4;
    public static final int UNIVERSITY_OF_ISLAMIC_SCIENCES_SETTINGS = 5;
    public static final int UNION_OF_ISLAMIC_ORGANIZATIONS_IN_FRANCE = 6;

///////////////////////////////////////////////////////////////////////////////////////////

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
	private GregorianDate gregorianDate;
	private HijriDate hijriDate;
	
///////////////////////////////////////////////////////////////////////////////////////////

	public PrayerTimes(int y, int m, int d, double Lat, double Long, double GMT, String Name){
		this.initialize(y, m, d, Lat, Long, GMT, Name);
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
		this.initialize(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), Lat, Long, GMT, Name);
	}

	 // copy constructor
	public PrayerTimes(PrayerTimes p){
	    this.Name          = p.Name;
		this.isUAQS        = p.isUAQS ;
		this.Latitude      = p.Latitude;
		this.Longitude     = p.Longitude;
		this.Dec           = p.Dec;
		this.EqTime        = p.EqTime;
	 	this.RefLong       = p.RefLong;
		this.FajrAngle     = p.FajrAngle;
		this.IshaAngle     = p.IshaAngle;
	 	this.AsrL          = p.AsrL;
	 	this.GMT           = p.GMT;
	 	this.hijriDate     = new HijriDate(p.hijriDate);
	 	this.gregorianDate = new GregorianDate(p.gregorianDate);

	}

	private void initialize(int y, int m, int d, double Lat, double Long, double GMT, String Name){
		this.setName(Name);
		
		this.setGregorianDate(new GregorianDate(y, m, d));
		this.setHijriDate(this.gregorianDate.toHijriDate());
		
		this.setLongitude(Long);
		this.setLatitude(Lat);
		this.setGMT(GMT);
		this.RefLong = this.GMT * 15;
		this.SolarCoordinates();
		this.setAnglesSettings(UMM_AL_QURA_SETTINGS);
		this.setAsrConfig(ASR_IN_SHAFAI);
		
	}

	public GregorianDate getGregorianDate(){ 
		return new GregorianDate(this.gregorianDate);
	}
	
	public HijriDate getHijriDate()	{
		return new HijriDate(this.hijriDate);
	}
	
	public int getFajr(){
		return FormatHours(Fajr());
	}
	
	public int getShrouq(){
		return FormatHours(Shrouq());
	}
	
	public int getDhuhr(){
		return FormatHours(Dhuhr());
	}
	
	public int getAsr(){
		return FormatHours(Asr());
	}
	
	public int getMaghrib(){
		return FormatHours(Maghrib());
	}

	public int getIsha(){
		if(isUAQS)
			return FormatHoursForUAQS(Isha());
		else
			return FormatHours(Isha());
	}
	
	public String getName(){
		return Name;
	}
	
	public double getLatitude(){
		return this.Latitude;
	}
	
	public double getLongitude(){
		return this.Longitude;
	}
	
	public double getGMT(){
		return this.GMT;
	}
	
	public static String formatTime(int formatted){
	    int hour   = formatted / 1000;
        formatted  = formatted % 1000;
        int min  = (formatted) / 10;
        formatted  = formatted % 10;
        boolean isPM = formatted == 1;
        
        return String.format("%02d:%02d ", hour, min) + (isPM ? "PM" : "AM");
	}
	
	public int[] getPrayerTimes(){
		int prayers[] = new int[6];
		
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

	public void setHijriDate(HijriDate hijriDate){
		if(hijriDate == null)
			throw new NullPointerException("No date found!");
		
		this.hijriDate = hijriDate;
		this.gregorianDate = this.hijriDate.toGregorianDate();
		this.update();
		
	}
	
	public void setGregorianDate(GregorianDate gregorianDate){
		if(gregorianDate == null)
			throw new NullPointerException("No date found!");
		this.gregorianDate = gregorianDate;
		this.hijriDate = this.gregorianDate.toHijriDate();
		this.update();
	}
	
	private void update(){
		SolarCoordinates();
	}

///////////////////////////////////////////////////////////////////////////////////////////

    public void setAnglesSettings(int option){
        
        if(option == UMM_AL_QURA_SETTINGS){
            this.FajrAngle = 18.5;
		    isUAQS = true;
		    
        }else if(option == MUSLIM_WORLD_LEAGUE_SETTINGS){
            this.FajrAngle = 18;
		    this.IshaAngle = 17;
		    isUAQS = false;
		    
        }else if(option == ISNA_SETTINGS){
            this.FajrAngle = 15;
		    this.IshaAngle = 15;
		    isUAQS = false;
		    
        }else if(option == EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY_SETTINGS){
            this.FajrAngle = 19.5;
		    this.IshaAngle = 17.5;
		    isUAQS = false;
		    
        }else if(option == UNIVERSITY_OF_ISLAMIC_SCIENCES_SETTINGS){
            this.FajrAngle = 18;
		    this.IshaAngle = 18;
		    isUAQS = false;
		    
        }else if(option == UNION_OF_ISLAMIC_ORGANIZATIONS_IN_FRANCE){
            this.FajrAngle = 12;
		    this.IshaAngle = 12;
		    isUAQS = false;
		    
        }else{
            throw new IllegalArgumentException("Invalid option!!"); 
        }
    }

    public void setAsrConfig(int option){
        if(option != ASR_IN_SHAFAI && option != ASR_IN_HANAFI)
            throw new IllegalArgumentException("Invalid option!!");
        
        this.AsrL = option;
    }

///////////////////////////////////////////////////////////////////////////////////////////

	private double Fajr()	{ return (Dhuhr() - T(this.FajrAngle)); }
	private double Shrouq()	{ return (Dhuhr() - T(0.83333)); }
	private double Dhuhr()	{ return (12.0 + ((this.RefLong - this.Longitude) / 15.0 - (this.EqTime))); }
	private double Asr()	{ return (Dhuhr()+ A(AsrL)); }
	private double Maghrib(){ return (Dhuhr() + T(0.83333)); }
	private double Isha()	{ 
		if(isUAQS) return Maghrib(); // return "Maghrib()" and add 90 mins (or 120 in Ramadan) to it for UMM_AL_QURA_SETTINGS
		else 	   return (Dhuhr() + T(this.IshaAngle)); // use the angle for others
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
		
		// all methods for "Isha" are to add 90 mins to "Maghrib" time to get "Isha" time according to
		// Umm-AlQura Settings
		
		int hour = (int)hrs;
  		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);
		
		// if it is Ramadan
		if(this.hijriDate.getMonth() != 9) 
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
*/

	private int FormatHours(double hrs) {
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
        
        return hour * 1000 + min * 10 + (isPM? 1:0);
	}

	private int FormatHoursForUAQS(double hrs) {
		// all methods for "Isha" are to add 90 mins to "Maghrib" time to get "Isha" time according to
		// Umm-AlQura Settings
		
		int hour = (int)hrs;
  		//int min = (int)Math.floor((hrs - hour) * 60);
		int min = roundToMinute((hrs - hour) * 60);
		
		// if it is Ramadan
		if(this.hijriDate.getMonth() != 9) 
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
        
		return hour * 1000 + min * 10 + (isPM? 1:0);

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

	private void SolarCoordinates(){
		int y = this.gregorianDate.getYear();
		int m = this.gregorianDate.getMonth();
		int d = this.gregorianDate.getDay();
		
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

	private static double GregorianToJulianDay(int y, int m, int d) {

	    if (m <= 2){
	    	y -= 1; m += 12;
	    }

		int A, B;
		double JD;

		A = (int)(y / 100.0);
		B = 2 - A + (int)(A / 4.0);
		JD = (365.25 * (y + 4716)) + (30.6001 * (m + 1)) + d + B - 1524.5;	

		return JD;

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
		
	public static String addMinutes(int time, int minutes){
		return addMinutes(formatTime(time), minutes);
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
		
		int thisPrayers[] = this.getPrayerTimes();
		int pPrayers[] 	  = p.getPrayerTimes();
		
		for(int i = 0; i < thisPrayers.length; i++)
			if(thisPrayers[i] != pPrayers[i])
				return false;
					
		return true;

	}

	public String toString(){
	
		return   "Name : "				+ getName()			        +
				 "\nHijri Date : " 		+ getHijriDate()	        +
				 "\nGregorian Date : "  + getGregorianDate()        +
				 "\n\nFajr :    "		+ formatTime(getFajr())		+
				 "\nShrouq :  " 		+ formatTime(getShrouq())	+ 
				 "\nDhuhr :   " 		+ formatTime(getDhuhr())	+
				 "\nAsr :     " 		+ formatTime(getAsr())		+ 
				 "\nMaghrib : " 		+ formatTime(getMaghrib())	+ 
				 "\nIsha :    " 		+ formatTime(getIsha());
	
	}

}

