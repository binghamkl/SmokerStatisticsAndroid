package com.bingham.smokerstatistics;

import java.text.NumberFormat;

public class StatDifference {

	private int mYear = 0;
	private int mMonth = 0;
	private int mDate = 0;
	private int mHour = 0;
	private int mMinute = 0;
	private int mSecond = 0;
	private int mTotalDays = 0;
		
	public void setYear(int mYear) {
		this.mYear = mYear;
	}
	public int getYear() {
		return mYear;
	}
	public void setMonth(int mMonth) {
		this.mMonth = mMonth;
	}
	public int getMonth() {
		return mMonth;
	}
	public void setDate(int mDate) {
		this.mDate = mDate;
	}
	public int getDate() {
		return mDate;
	}
	public void setHour(int mHour) {
		this.mHour = mHour;
	}
	public int getHour() {
		return mHour;
	}
	public void setMinute(int mMinute) {
		this.mMinute = mMinute;
	}
	public int getMinute() {
		return mMinute;
	}
	public void setSecond(int mSecond) {
		this.mSecond = mSecond;
	}
	public int getSecond() {
		return mSecond;
	}
	public void setTotalDays(int mTotalDays) {
		this.mTotalDays = mTotalDays;
	}
	public int getTotalDays() {
		return mTotalDays;
	}

	
	public String dateDifference() 
	{
		String returnValue = "";
		boolean showRest = false;
		if (mYear > 0)
		{
			showRest = true;
			returnValue = Integer.toString(mYear);
			if (Math.abs(mYear) != 1)
			{
				returnValue += " Years ";
			}
			else
			{
				returnValue += " Year ";
			}
		}
		if (showRest || mMonth > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mMonth);
			if (Math.abs(mMonth) != 1)
				returnValue += " months\n";
			else
				returnValue += " month\n";
		}
		if (showRest || mDate > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mDate);
			if (Math.abs(mDate) != 1)
				returnValue += " days ";
			else
				returnValue += " day ";
		}
		if (showRest || mHour > 0 || mMinute > 0 || mSecond > 0)
		{
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumIntegerDigits(2);
			numberFormat.setMaximumIntegerDigits(2);
			
			returnValue += numberFormat.format((long)mHour) + ":" + numberFormat.format((long)mMinute) + ":" + numberFormat.format((long)mSecond);
		}
		return returnValue;
	}
	public String getShortDescription()
	{
		String returnValue = "";
		boolean showRest = false;
		if (mYear > 0)
		{
			showRest = true;
			returnValue = Integer.toString(mYear) + "y ";
		}
		if (showRest || mMonth > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mMonth) + "m ";
		}
		if (showRest || mDate > 0)
		{
			showRest = true;
			returnValue += Integer.toString(mDate) + "d ";
		}
		if (showRest || mHour > 0 || mMinute > 0 || mSecond > 0)
		{
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumIntegerDigits(2);
			numberFormat.setMaximumIntegerDigits(2);
			
			returnValue += numberFormat.format((long)mHour) + ":" + numberFormat.format((long)mMinute) + ":" + numberFormat.format((long)mSecond);
		}
		
		return returnValue;
	}
	
}
