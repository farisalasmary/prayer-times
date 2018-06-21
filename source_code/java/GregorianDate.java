
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
            Date:  Sep 10, 2017
*/

public class GregorianDate{

    private int y, m, d;

    public GregorianDate(int y, int m, int d){
    	this.setYear(y);
    	this.setMonth(m);
    	this.setDay(d);
    }
    
    public GregorianDate(GregorianDate gregorianDate){
    	this.setYear(gregorianDate.getYear());
    	this.setMonth(gregorianDate.getMonth());
    	this.setDay(gregorianDate.getDay());
    }
    
    public int getYear(){
        return this.y;
    }
    
    public int getMonth(){
        return this.m;
    }
    
    public int getDay(){
        return this.d;
    }
    
    
    public void setYear(int y){
    	if(y < 0)
            throw new IllegalArgumentException("year should be > 0!");
        this.y = y;
    }
    
    public void setMonth(int m){
    	if(m < 0 || m > 12)
            throw new IllegalArgumentException("month should be between 1 and 12 inclusive!");
        this.m = m;
    }
    
    public void setDay(int d){
        if(d < 0 || d > 31)
            throw new IllegalArgumentException("day should be between 1 and 31 inclusive!");
        this.d = d;
    }
    
    public HijriDate toHijriDate(){
    	return JulianDayToHijri(GregorianToJulianDay(this.y, this.m, this.d));
    }
    
    public static double GregorianToJulianDay(int y, int m, int d) {

	    if (m <= 2){ y -= 1; m += 12; }

			int A, B;
			double JD;

			A = (int)(y / 100.0);
			B = 2 - A + (int)(A / 4.0);
			JD = (365.25 * (y + 4716)) + (30.6001 * (m + 1)) + d + B - 1524.5;	

			return JD;

	}
		
	public static HijriDate JulianDayToHijri(double JD){
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

		return new HijriDate(Y, M, D);

	}    
    
	public String getDayOfTheWeek(){
		int i = (14 - this.m) / 12;
		int y = this.y - i;
		int day = ((((i * 12 + this.m - 2) * 13 - 1) / 5) + this.d + y + (y / 4) - (y / 100) + (y / 400)) % 7;
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
    
	public String toString(){
		return this.y + "-" + this.m + "-" + this.d;
	}

}

