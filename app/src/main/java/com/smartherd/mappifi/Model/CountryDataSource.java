package com.smartherd.mappifi.Model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {
    public static final String COUNTREY_KEY="country";
    public static final float MINIMUM_CONFIDENCE_LEVEL=0.4F;
    public static final String DEFAULT_COUNTRY_NAME="India";



    //28.623079, 77.210493 NEW DELHI

    public static final double DEFAULT_COUNTRY_LATITUDE=28.623079;
    public static final double DEFAULT_COUNTRY_LONGITUDE=77.210493;
    public static final String DEFAULT_MESSAGE="Be happy wherever you go.";
    public static String acceptedUserWord= "DEFAULT";

    private Hashtable<String, String> countriesAndMessages;


    public CountryDataSource(Hashtable<String ,String> countriesAndMessages)
    {
        this.countriesAndMessages = countriesAndMessages;
    }


    public String matchWithMinimumConfidenceLevelOfUserWords(ArrayList<String> userWords, float[] confidenceLevel)
    {
        if(userWords==null || confidenceLevel==null)
        {
            return DEFAULT_COUNTRY_NAME;
        }
        int numberOfUserWords = userWords.size();
        Enumeration<String>countries;
        acceptedUserWord=userWords.get(0);

        for(int index=0; index<numberOfUserWords && index<confidenceLevel.length; index++)
        {
            if(confidenceLevel[index]<MINIMUM_CONFIDENCE_LEVEL)
            {
                break;
            }
            acceptedUserWord = userWords.get(index);
            countries=countriesAndMessages.keys();
            while(countries.hasMoreElements())
            {
                String selectedCountry= countries.nextElement();
                if(acceptedUserWord.equalsIgnoreCase(selectedCountry))
                {
                    return acceptedUserWord;
                }
            }
        }
        return DEFAULT_COUNTRY_NAME;
    }




    public String getInfoOfTheCountry(String country)
    {
        return ("Welcome to " +country);
    }
}
