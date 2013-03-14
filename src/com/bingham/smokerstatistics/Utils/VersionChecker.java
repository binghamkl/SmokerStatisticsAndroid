package com.bingham.smokerstatistics.Utils;

import httphelper.HttpHandler;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionChecker {

	private int mVersionCode = 0;
	private String mVersion = "";
	private String mMarketPlacePoint = "";
	private String mUpgradedescription = "";

	private static final String URL_UPGRADE = "http://ibssoftware.com/cigstats/status.html";

	public void setVersionCode(int mVersionCode) {
		this.mVersionCode = mVersionCode;
	}

	public int getVersionCode() {
		return mVersionCode;
	}

	public void setUpgradedescription(String mUpgradedescription) {
		this.mUpgradedescription = mUpgradedescription;
	}

	public String getUpgradedescription() {
		return mUpgradedescription;
	}

	public void setMarketPlacePoint(String mMarketPlacePoint) {
		this.mMarketPlacePoint = mMarketPlacePoint;
	}

	public String getMarketPlacePoint() {
		return mMarketPlacePoint;
	}

	public void setVersion(String mVersion) {
		this.mVersion = mVersion;
	}

	public String getVersion() {
		return mVersion;
	}

	public boolean upgradeAvailable(PackageManager pm)
	{
		int versionCode = 0;
		try
		{
			PackageInfo pi = pm.getPackageInfo("com.bingham.smokerstatistics", 0);
			versionCode = pi.versionCode;
		}
		catch(NameNotFoundException e)
		{
			return false;
		}
		
		//Todo add code to get http information.
		String htmlResponse = "";
		try
		{
			HttpHandler httpHandler = new HttpHandler();
			htmlResponse = httpHandler.executeGet(URL_UPGRADE);
		}
		catch (Exception ex)
		{
			return false;
		}
		

		if (!htmlResponse.equals(""))
		{
			try
			{
				buildUpgradeItems(htmlResponse);
			}
			catch(Exception ex)
			{
				return false;
			}
			
			if (this.mVersionCode > versionCode)
				return true;
		}
		return false;
	}
	
	public void buildUpgradeItems(String xml) throws Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory
									.newInstance()
									.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		NodeList versionCodes = doc.getElementsByTagName("versioncode");
		for (int i=0; i< versionCodes.getLength(); i++) 
		{
			Element element = (Element)versionCodes.item(i);
			if (element.getFirstChild() != null)
				this.mVersionCode = Integer.parseInt(element.getFirstChild().getNodeValue());
		}
		
		NodeList versions = doc.getElementsByTagName("version");
		for (int i=0; i< versions.getLength(); i++) 
		{
			Element element = (Element)versions.item(i);
			if (element.getFirstChild() != null)
				this.mVersion = element.getFirstChild().getNodeValue();
		}

		NodeList marketplacepointers = doc.getElementsByTagName("marketplacepointer");
		for (int i=0; i< marketplacepointers.getLength(); i++) 
		{
			Element element = (Element)marketplacepointers.item(i);
			if (element.getFirstChild() != null)
				this.mMarketPlacePoint = element.getFirstChild().getNodeValue();
		}

		NodeList upgradedescriptions = doc.getElementsByTagName("upgradedescription");
		for (int i=0; i< upgradedescriptions.getLength(); i++) 
		{
			Element element = (Element)upgradedescriptions.item(i);
			if (element.getFirstChild() != null)
				this.mUpgradedescription = element.getFirstChild().getNodeValue();
		}
		
									
	}
	
}
