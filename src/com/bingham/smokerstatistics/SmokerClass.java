package com.bingham.smokerstatistics;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.Context;
import android.database.Cursor;

public class SmokerClass {

	private static SmokerClass singleton;
	
	private Calendar mQuitDateTime;
	private Long mSmokerId;
	private String mSmokerName;
	private int mCigsPerDay = 20;
	private BigDecimal mCostPerPack = new BigDecimal(4.00);
	private int mMinutesPerCig = 7;
	private boolean mIsNew = false;
	private Context mctx;
	private StatDifference mstatDifference = new StatDifference();
	private long mTotalcigs = 0;
	private double mMoneySaved = 0;
	private int mLifeSavedMinutes = 0;
	private int mLifeSavedHours = 0;
	private int mLifeSavedDays = 0;
	private int mLifeSavedYears = 0;
	private int mLifeTotalDays = 0;
	private String mErrorMessage = "";
	private boolean mHasAlreadyQuit = false;
	private boolean mNotifications = true;
	private boolean mTwidroid = true;
	private int mFrequency = 1;
	private int mCigsFrequency = 100;
	private int mCurrencyFrequency = 1000;
	private int mLifeFrequency = 1;
	private boolean mMilestones = true;
	private String mTwitterUser;
	private String mTwitterPW;

	// Constructor

	private SmokerClass(Long smokerId, Context ctx) {

		mctx = ctx;

		SmokerDbAdapter smokerAdapter = new SmokerDbAdapter(ctx);
		smokerAdapter.open();

		Cursor cursor = smokerAdapter.fetchSetting(smokerId);

		loadRecord(cursor);
		mSmokerId = smokerId;

		smokerAdapter.close();

	}
	
	private SmokerClass(String smokerName, Context ctx) {

		mctx = ctx;

		SmokerDbAdapter smokerAdapter = new SmokerDbAdapter(ctx);
		smokerAdapter.open();

		Cursor cursor = smokerAdapter.fetchSettingByName(smokerName);

		loadRecord(cursor);
		mSmokerName = smokerName;

		smokerAdapter.close();
	}

	// Properties

	public void setQuitDateTime( Calendar mQuitDateTime) {
		this.mQuitDateTime = mQuitDateTime;
	}

	public void setQuitDateTime(int year, int month, int day, int hour,
			int minutes) {
		mQuitDateTime.set(year, month, day, hour, minutes);
	}

	public Calendar getQuitDateTime() {
		return mQuitDateTime;
	}

	public void setSmokerId(Long mSmokerId) {
		this.mSmokerId = mSmokerId;
	}

	public Long getSmokerId() {
		return mSmokerId;
	}

	public void setMSmokerName(String mSmokerName) {
		this.mSmokerName = mSmokerName;
	}

	public String getMSmokerName() {
		return mSmokerName;
	}

	public void setStatDifference(StatDifference mstatDifference) {
		this.mstatDifference = mstatDifference;
	}

	public StatDifference getStatDifference() {
		return mstatDifference;
	}

	public void setCigsPerDay(int mCigsPerDay) {
		this.mCigsPerDay = mCigsPerDay;
	}

	public int getCigsPerDay() {
		return mCigsPerDay;
	}

	public void setCostPerPack(BigDecimal mCostPerPack) {
		this.mCostPerPack = mCostPerPack;
	}

	public BigDecimal getCostPerPack() {
		return mCostPerPack;
	}

	public void setMinutesPerCig(int mMinutesPerCig) {
		this.mMinutesPerCig = mMinutesPerCig;
	}

	public int getMinutesPerCig() {
		return mMinutesPerCig;
	}
	
	public boolean getIsNew() {
		return mIsNew;
	}

	public long getTotalcigs() {
		return mTotalcigs;
	}

	public double getMoneySaved() {
		return mMoneySaved;
	}

	public int getLifeSavedMinutes() {
		return mLifeSavedMinutes;
	}

	public int getLifeSavedHours() {
		return mLifeSavedHours;
	}

	public int getLifeSavedYears() {
		return mLifeSavedYears;
	}

	public int getLifeSavedDays() {
		return mLifeSavedDays;
	}
	
	public boolean getHasAlreadyQuit() {
		return mHasAlreadyQuit;
	}

	public void setNotifications(boolean mNotifications) {
		this.mNotifications = mNotifications;
	}

	public boolean getNotifications() {
		return mNotifications;
	}

	public void setTwidroid(boolean mTwidroid) {
		this.mTwidroid = mTwidroid;
	}

	public boolean getTwidroid() {
		return mTwidroid;
	}

	public void setFrequency(int mFrequency) {
		this.mFrequency = mFrequency;
	}

	public int getFrequency() {
		return mFrequency;
	}

	public void setCigsFrequency(int mCigsFrequency) {
		this.mCigsFrequency = mCigsFrequency;
	}

	public int getCigsFrequency() {
		return mCigsFrequency;
	}

	public void setCurrencyFrequency(int mCurrencyFrequency) {
		this.mCurrencyFrequency = mCurrencyFrequency;
	}

	public int getCurrencyFrequency() {
		return mCurrencyFrequency;
	}

	public void setLifeFrequency(int mLifeFrequency) {
		this.mLifeFrequency = mLifeFrequency;
	}

	public int getLifeFrequency() {
		return mLifeFrequency;
	}

	public void setMilestones(boolean mMilestones) {
		this.mMilestones = mMilestones;
	}

	public boolean getMilestones() {
		return mMilestones;
	}

	public void setLifeTotalDays(int mLifeTotalDays) {
		this.mLifeTotalDays = mLifeTotalDays;
	}

	public int getLifeTotalDays() {
		return mLifeTotalDays;
	}
	public void setTwitterUser(String mTwitterUser) {
		this.mTwitterUser = mTwitterUser;
	}

	public String getTwitterUser() {
		return mTwitterUser;
	}

	public void setTwitterPW(String mTwitterPW) {
		this.mTwitterPW = mTwitterPW;
	}

	public String getTwitterPW() {
		return mTwitterPW;
	}

	public String getLifeSavedString() {
		String returnValue = "";
		
		boolean showRest = false;
		if (mLifeSavedYears > 0)
		{
			showRest = true;
			returnValue = Integer.toString(mLifeSavedYears);
			if (Math.abs(mLifeSavedYears) == 1)
				returnValue += " Year ";
			else
				returnValue += " Years ";
		}
		if (showRest || mLifeSavedDays > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mLifeSavedDays);
			if (mLifeSavedDays == 1)
				returnValue += " day ";
			else
				returnValue += " days ";
		}
		if (showRest || mLifeSavedHours > 0 || mLifeSavedMinutes > 0)
		{
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumIntegerDigits(2);
			numberFormat.setMaximumIntegerDigits(2);

			returnValue += numberFormat.format((long)mLifeSavedHours) + "h " + numberFormat.format((long)mLifeSavedMinutes) + "m";
		}
		
		return returnValue;
	}
	
	public String getErrorMessage() {
		return this.mErrorMessage;
	}
	
	public boolean IsValid()
	{
		boolean isValid = true;
		mErrorMessage = "";
		if (this.mCigsPerDay > 100)
		{
			isValid = false;
			mErrorMessage += "Cigs Per day must be less than 100\n"; 
		}
		if (this.mCigsPerDay < 1)
		{
			isValid = false;
			mErrorMessage += "Cigs per day must be greater than 0.\n";
		}
		if (this.mMinutesPerCig > 50)
		{
			isValid = false;
			mErrorMessage += "Min saved per cig must be less than 50\n";
		}
		if (this.mMinutesPerCig < 0)
		{
			isValid = false;
			mErrorMessage += "Min save per cig must be greater than 0.\n";
		}
		if (this.mCostPerPack.compareTo(new BigDecimal(200)) > 0)
		{
			isValid = false;
			mErrorMessage += "Cost per pack should be less than 200\n";
		}
		if (this.mCostPerPack.compareTo(new BigDecimal(0)) <= 0)
		{
			isValid = false;
			mErrorMessage += "Cost per pack must be greater than 0.\n";
		}
		if (this.mFrequency > 1000)
		{
			isValid = false;
			mErrorMessage += "Frequency must be less than 1000 days";
		}
		if (this.mFrequency < 0)
		{
			isValid = false;
			mErrorMessage += "Frequency must be greater than 0.";
		}
		if (this.mCigsFrequency > 10000)
		{
			isValid = false;
			mErrorMessage += "Cig Frequency must be less than 10000";
		}
		if (this.mCigsFrequency < 0)
		{
			isValid = false;
			mErrorMessage += "Cig Frequency must be greater or equal to 0.";
		}
		if (this.mLifeFrequency > 1000)
		{
			isValid = false;
			mErrorMessage += "Life saved frequency must be less than 1000";
		}
		if (this.mLifeFrequency < 0)
		{
			isValid = false;
			mErrorMessage += "Life frequency must be greater or equal to 0.";
		}
		if (this.mCurrencyFrequency > 10000)
		{
			isValid = false;
			mErrorMessage += "Currency Frequency must be less than 10,000";
		}
		if (this.mCurrencyFrequency < 0)
		{
			isValid = false;
			mErrorMessage += "Currency Frequency must be greater or equal to 0.";
		}
		
		return isValid;
	}
	
	public boolean RunService()
	{
		if (this.mNotifications || this.mMilestones || this.mTwidroid)
			return true;
		else
			return false;
	}
	
	public String getShortDayStatistics()
	{
		String returnValue = "";
		if (this.mHasAlreadyQuit)
		{
			returnValue = "quit smoking for " + this.mstatDifference.getShortDescription();
		}
		else
		{
			returnValue = "will quit smoking in " + this.mstatDifference.getShortDescription(); 
		}
		return returnValue;
	}
	public String getShortCigStatistics()
	{
		String returnValue = "";
		returnValue = "have not smoked " + this.mTotalcigs + " cigarettes.";
		return returnValue;
	}
	public String getShortCurrencyStats()
	{
		String returnValue = "";
		DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
		returnValue = "saved $" + decimalFormat.format(this.mMoneySaved) + " by not smoking";
		return returnValue;
	}
	public String getShortLife()
	{
		String returnValue = "";
		boolean showRest = false;
		if (mLifeSavedYears > 0)
		{
			showRest = true;
			returnValue = Integer.toString(mLifeSavedYears) + "y ";
		}
		if (showRest || mLifeSavedDays > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mLifeSavedDays) + "d ";
		}
		if (showRest || mLifeSavedHours > 0 || mLifeSavedMinutes > 0)
		{
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumIntegerDigits(2);
			numberFormat.setMaximumIntegerDigits(2);

			returnValue += numberFormat.format((long)mLifeSavedHours) + "h " + numberFormat.format((long)mLifeSavedMinutes) + "m";
		}
		return returnValue;
	}
	public String getShortLifeStats()
	{
		String returnValue = "";
		returnValue = this.getShortLife();
		returnValue = "saved " + returnValue + " of life by not smoking";
		return returnValue;
	}
	public String buildShortStatList()
	{
    	String msg = "I " + getShortDayStatistics();
		DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
		msg += ", saving $" + decimalFormat.format(getMoneySaved());
		msg += " and " + getShortLife() + " of life";
		msg += " by not smoking " + Long.toString(getTotalcigs()) + " cigs";
		return msg;
	}
	public String getCurrentMilestone()
	{

		if (this.mHasAlreadyQuit)
		{
			if (mstatDifference.getYear() >= 10)
				return "* Lung Cancer risk is equivalent to non smoker\n" + 
						"* Pre-cancerous cells are replaced";
			if (mstatDifference.getYear() >= 5)
				return "* Lung cancer death rate decreased by half\n" + 
						"* Stroke risk is reduced\n" + 
						"* Mouth, throat and esophogus cancer is halved.";
			if (mstatDifference.getYear() >= 2)
				return "* Heart attack risk drops to normal";
			if (mstatDifference.getYear() >= 1)
				return "* Risk of heart disease is halved";
			if (mstatDifference.getMonth() >= 1)
				return "* Coughing, fatique, shortness of breath decrease\n" + 
						"* Overall energy level increases\n" + 
						"* Cilia re-grows on lungs, increasing ability to handle mucus, clean lungs, reduce infection.";
			if (mstatDifference.getDate() >= 14)
				return "* Circulation improves\n" + 
						"* Walking becomes easier\n" +
						"* Lung function increases up to 30%";
			if (mstatDifference.getDate() >= 3)
				return "* Bronchial tubes relax making it easier to breath\n" + 
						"* Lung capacity increases making it easier to do physical activities.";
			if (mstatDifference.getDate() >= 2)
				return "* Nerve ending regrowing\n" + 
						"* Ability to smell and taste is returning";
			if (mstatDifference.getDate() >= 1)
				return "* Chance of heart attack has decreased.";
			if (mstatDifference.getHour() >= 8)
				return "* Carbon Monoxide in blood drops to normal\n" + 
						"* Oxygen levels increase to normal\n" +
						"* Smoker's breath disappears";
			if (mstatDifference.getHour() >= 1 || mstatDifference.getMinute() >= 20)
				return "* Blood pressure drops to normal\n" + 
						"* Pulse rate returns to normal\n" + 
						"* Body temp of hands and feet increase to normal";
			return "Congratulations on choosing to quit smoking";
		}
		return "Congratulations..  Make a plan and be prepared to quit.";
	}

	// Methods

	public void Save() {
		SmokerDbAdapter smokerDb = new SmokerDbAdapter(mctx);
		smokerDb.open();

		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date normalDate = mQuitDateTime.getTime();
		String dateValue = sdf.format(normalDate);

		sdf = new SimpleDateFormat("HH:mm");
		String timeValue = sdf.format(normalDate);

		if (this.mIsNew) {

			this.mSmokerId = smokerDb.createSetting(dateValue, timeValue,
					this.mSmokerName, mCigsPerDay, mCostPerPack, mMinutesPerCig, mNotifications, 
					mTwidroid, mFrequency, mCigsFrequency, mCurrencyFrequency, mLifeFrequency, mMilestones, mTwitterUser, mTwitterPW);
			this.mIsNew = false;

		} else {

			smokerDb.updateSetting(this.mSmokerId, dateValue, timeValue,
					this.mSmokerName, mCigsPerDay, mCostPerPack, mMinutesPerCig, mNotifications, 
					mTwidroid, mFrequency, mCigsFrequency, mCurrencyFrequency, mLifeFrequency, mMilestones, mTwitterUser, mTwitterPW);

		}

		smokerDb.close();
	}
	
	private void loadRecord(Cursor cursor) {

		mQuitDateTime = Calendar.getInstance();
		if (cursor != null && cursor.getCount() > 0) {

			this.mSmokerId = cursor.getLong(cursor
					.getColumnIndexOrThrow(SmokerDbAdapter.KEY_ROWID));
			this.mSmokerName = cursor.getString(cursor
					.getColumnIndexOrThrow(SmokerDbAdapter.KEY_SMOKERNAME));
			mCigsPerDay = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_CIGARETTESPERDAY));
			String costPerPackString = cursor.getString(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_COSTPERPACK));
			if (!costPerPackString.equals("")){
				mCostPerPack = new BigDecimal(costPerPackString);
			} else {
				mCostPerPack = new BigDecimal(0);
			}
			mMinutesPerCig = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_MINUTESPERCIG));
			if ( cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_NOTIFICATIONS)) == 0)
				mNotifications = false;
			else
				mNotifications = true;
			if (cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_TWIDROID)) == 0)
				mTwidroid = false;
			else
				mTwidroid = true;
			
			mTwitterUser = cursor.getString(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_TWITTERUSER));
			if (mTwitterUser == null)
				mTwitterUser = "";
			mTwitterPW = cursor.getString(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_TWITTERPW));
			if (mTwitterPW == null)
				mTwitterPW = "";
			
			if (cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_MILESTONES)) == 0)
				mMilestones = false;
			else
				mMilestones = true;
			mFrequency = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_FREQUENCY));
			mCigsFrequency = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_CIGFREQUENCY));
			mLifeFrequency = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_LIFEFREQUENCY));
			mCurrencyFrequency = cursor.getInt(cursor.getColumnIndexOrThrow(SmokerDbAdapter.KEY_CURRENCYFREQUENCY));
			
			String quitDateValue = cursor.getString(cursor
					.getColumnIndexOrThrow(SmokerDbAdapter.KEY_QUITDATE));
			int year = Integer.parseInt(quitDateValue
					.substring(0, 4));
			int month = Integer.parseInt(quitDateValue
					.substring(5, 7));
			int day = Integer.parseInt(quitDateValue
					.substring(8, 10));

			String quitTimeValue = cursor.getString(cursor
					.getColumnIndexOrThrow(SmokerDbAdapter.KEY_QUITTIME));
			int hour = Integer.parseInt(quitTimeValue
					.substring(0, 2));
			int minute = Integer.parseInt(quitTimeValue
					.substring(3, 5));

			mQuitDateTime.set(year, month - 1, day, hour, minute);

			mIsNew = false;

		} else {
			mIsNew = true;
		}
		cursor.close();
	}
	
	public void updateStatistics()
	{
		Calendar currentDate = Calendar.getInstance();
		if (currentDate.compareTo(this.mQuitDateTime) > 0) {

			mHasAlreadyQuit = true;
			Calendar cloneDate = (Calendar)this.mQuitDateTime.clone();
			mstatDifference.setTotalDays((int)((currentDate.getTimeInMillis() - mQuitDateTime.getTimeInMillis()) / 1000 / 60 / 60 / 24));
			
			this.mstatDifference.setYear(dateDifference(currentDate, cloneDate, Calendar.YEAR));
			this.mstatDifference.setMonth(dateDifference(currentDate, cloneDate, Calendar.MONTH));
			this.mstatDifference.setDate(dateDifference(currentDate, cloneDate, Calendar.DATE));
			this.mstatDifference.setHour(dateDifference(currentDate, cloneDate, Calendar.HOUR_OF_DAY));
			this.mstatDifference.setMinute(dateDifference(currentDate, cloneDate, Calendar.MINUTE));
			this.mstatDifference.setSecond(dateDifference(currentDate, cloneDate, Calendar.SECOND));
			
			long millisecondsSinceQuit = currentDate.getTimeInMillis() - mQuitDateTime.getTimeInMillis();
			long minutesSinceQuit = millisecondsSinceQuit / 1000 / 60;
			
			double cigsPerMinute = (double)this.mCigsPerDay / 24 / 60;
			mTotalcigs = (long)(cigsPerMinute * minutesSinceQuit);
			mMoneySaved = this.mCostPerPack.doubleValue() / 20 * mTotalcigs;			
			
			long lifeSavedMinutesTemp = this.mMinutesPerCig * mTotalcigs;
			this.mLifeSavedMinutes = (int)(lifeSavedMinutesTemp % 60);
			lifeSavedMinutesTemp /= 60;
			this.mLifeSavedHours = (int)(lifeSavedMinutesTemp % 24);
			lifeSavedMinutesTemp /= 24;
			this.mLifeTotalDays = (int)lifeSavedMinutesTemp;
			this.mLifeSavedDays = (int)(lifeSavedMinutesTemp % 365);
			this.mLifeSavedYears = (int)(lifeSavedMinutesTemp / 365);
			
		}
		else
		{
			mHasAlreadyQuit = false;
			
			mTotalcigs = 0;
			mLifeSavedMinutes = 0;
			mLifeSavedHours = 0;
			mLifeSavedDays = 0;
			mLifeSavedYears = 0;
			mMoneySaved = 0;

			Calendar cloneDate = (Calendar)currentDate.clone();

			mstatDifference.setTotalDays((int)((mQuitDateTime.getTimeInMillis() - currentDate.getTimeInMillis()) / 1000 / 60 / 60 / 24));
			
			this.mstatDifference.setYear(dateDifference(mQuitDateTime, cloneDate, Calendar.YEAR));
			this.mstatDifference.setMonth(dateDifference(mQuitDateTime, cloneDate, Calendar.MONTH));
			this.mstatDifference.setDate(dateDifference(mQuitDateTime, cloneDate, Calendar.DATE));
			this.mstatDifference.setHour(dateDifference(mQuitDateTime, cloneDate, Calendar.HOUR_OF_DAY));
			this.mstatDifference.setMinute(dateDifference(mQuitDateTime, cloneDate, Calendar.MINUTE));
			this.mstatDifference.setSecond(dateDifference(mQuitDateTime, cloneDate, Calendar.SECOND));

		}
	}
	
	private int dateDifference(Calendar largeDate, Calendar smallDate, int field) {
		int diff = 0;
		Calendar testDate = (Calendar)smallDate.clone();
		testDate.add(field, diff + 1);
		while (testDate.compareTo(largeDate) < 0) {
			diff++;
			testDate = (Calendar)smallDate.clone();
			testDate.add(field, diff + 1);
		}
		smallDate.add(field, diff);
		return diff;
	}
	
	
	// Factory Methods

	public static SmokerClass createInstance(Long smokerId, Context ctx){
		if (singleton == null){
			singleton = new SmokerClass(smokerId, ctx);
		}
		return singleton;
	}
	
	public static SmokerClass createInstance(String smokerName, Context ctx){
		if (singleton == null){
			singleton = new SmokerClass(smokerName, ctx);
		}
		return singleton;
	}

}
