package model;

import java.util.Comparator;
import java.util.Locale;
import java.util.UUID;

/**
 * An event in a person's life, containing type of event, location, year, and associated user
 */
public class Event implements Comparable<Event> {
    /**
     * Unique ID of event
     */
    private String eventID;
    /**
     * Associated username
     */
    private String associatedUsername;
    /**
     * Associated personID
     */
    private String personID;
    /**
     * Location of event in latitude
     */
    private float latitude;
    /**
     * Location of event in longitude
     */
    private float longitude;
    /**
     * Location of event by country
     */
    private String country;
    /**
     * Location of event by city
     */
    private String city;
    /**
     * Type of event (birth, death, marriage, etc.)
     */
    private String eventType;
    /**
     * Year the event took place
     */
    private int year;

    /**
     * Create event with given params
     * @param eventID unique ID of event
     * @param username associated username
     * @param personID associated personID
     * @param latitude location of event in latitude
     * @param longitude location of event in longitude
     * @param country location of event by country
     * @param city location of event by city
     * @param eventType type of event (birth, death, marriage, etc.)
     * @param year year the event took place
     */
    public Event(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    /**
     * Check if object is equal to this event
     * @param o Object to compare
     * @return true if object has identical data to this event, else false
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (o.getClass() == Event.class) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        }
        else {
            return false;
        }
    }

    /**
     * Create hash code for use in hash map
     * @return generated hash code
     */
    @Override
    public int hashCode() {
        return UUID.fromString(this.eventID).hashCode();
    }

    @Override
    public int compareTo(Event event) {
        if (event == this)
            return 0;
        if (event == null)
            return 0;
        if (this.eventType.equalsIgnoreCase("birth")) {
            return -1;
        }
        else if (this.eventType.equalsIgnoreCase("death")) {
            return 1;
        }

        if (event.eventType.equalsIgnoreCase("birth")) {
            return 1;
        }
        else if (event.eventType.equalsIgnoreCase("death")) {
            return -1;
        }

        if (this.year < event.getYear()) {
            return -1;
        }
        else if (this.year > event.getYear()) {
            return 1;
        }
        else {
            return this.eventType.toLowerCase(Locale.ROOT).compareTo(event.eventType.toLowerCase(Locale.ROOT));
        }
    }

    public static class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            if (o1 == o2)
                return 0;
            if (o1 == null || o2 == null)
                return 0;
            if (o1.eventType.equalsIgnoreCase("birth")) {
                return -1;
            }
            else if (o1.eventType.equalsIgnoreCase("death")) {
                return 1;
            }

            if (o2.eventType.equalsIgnoreCase("birth")) {
                return 1;
            }
            else if (o2.eventType.equalsIgnoreCase("death")) {
                return -1;
            }

            if (o1.year < o2.getYear()) {
                return -1;
            }
            else if (o1.year > o2.getYear()) {
                return 1;
            }
            else {
                return o1.eventType.toLowerCase(Locale.ROOT).compareTo(o2.eventType.toLowerCase(Locale.ROOT));
            }
        }
    }
}