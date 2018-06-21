
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

public class HijriDate{

    private int y, m, d;

    public HijriDate(int y, int m, int d){
    	this.setYear(y);
    	this.setMonth(m);
    	this.setDay(d);
    }
    
    public HijriDate(HijriDate hijriDate){
    	this.setYear(hijriDate.getYear());
    	this.setMonth(hijriDate.getMonth());
    	this.setDay(hijriDate.getDay());
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
        if(d < 0 || d > 30)
            throw new IllegalArgumentException("day should be between 1 and 30 inclusive!");
        this.d = d;
    }
    
    public GregorianDate toGregorianDate(){
    	return JulianDayToGregorian(HijriToJulianDay(this.y, this.m, this.d));
    }
    
	public static double HijriToJulianDay(int Y, int M, int D){

		return (int)((11 * Y + 3) / 30.0) + (354 * Y) + (30 * M) -
			   (int)((M - 1) / 2.0) + D + 1948440 - 385;

	}
	
	public static GregorianDate JulianDayToGregorian(double JD){
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

		return new GregorianDate(Y, M, D);

	}

	public String getDayOfTheWeek(){
		GregorianDate gregorianDate = this.toGregorianDate();
		int m = gregorianDate.getMonth();
		int d = gregorianDate.getDay();
		int i = (14 - this.m) / 12;
		int y = gregorianDate.getYear() - i;
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
	
	public String toString(){
		return this.y + "-" + this.m + "-" + this.d;
	}
	
}


