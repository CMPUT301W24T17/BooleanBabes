package com.example.evenz;

import java.util.ArrayList;


/**
 * The Attendee class represents an attendee of an event. It extends the User class and includes additional properties and methods specific to an attendee.
 * <p>
 * Each Attendee has a geolocation, a notifications flag, and a list of events they are attending. The geolocation represents the current location of the attendee. The notifications flag indicates whether the attendee has notifications enabled. The event list contains the IDs of the events the attendee is attending.
 * <p>
 * The Attendee class provides getter and setter methods for each of its properties. It also provides methods to add an event to the event list, remove an event from the event list, and clear the event list.
 * <p>
 * The Attendee class has two constructors. The first constructor takes the name, profile picture ID, phone number, and email of the attendee. The second constructor takes the same parameters as the first constructor, plus the geolocation, notifications flag, and event list of the attendee.
 *
 * @version 1.0
 * @see User
 * @see Geolocation
 * @see ArrayList
 */
public class Attendee extends User
    {
        private Geolocation geolocation;
        private boolean notifications;
        private ArrayList<String> eventList;

            /**
             * Constructs a new Attendee object with the specified name, profile picture ID, phone number, and email.
             *
             * @param name         the name of the attendee
             * @param profilePicID the profile picture ID of the attendee
             * @param phone        the phone number of the attendee
             * @param email        the email of the attendee
             */
//            public Attendee(String name, String profilePicID, String phone, String email) {
//                this(name, profilePicID, phone, email, null, false, new ArrayList<>());
//            }


            /**
             * Constructs a new Attendee object with the specified name, profile picture ID, phone number, email, geolocation, notifications flag, and event list.
             *
             * @param name         the name of the attendee
             * @param profilePicID the profile picture ID of the attendee
             * @param phone        the phone number of the attendee
             * @param email        the email of the attendee
             * @param geolocation  the geolocation of the attendee
             * @param notifications the notifications flag of the attendee
             * @param eventList    the event list of the attendee
             */
            public Attendee(String name, String profilePicID, String phone, String email, Geolocation geolocation, boolean notifications, ArrayList<String> eventList) {
                this.setName(name);
                this.setProfilePicID(profilePicID);
                this.setPhone(phone);
                this.setEmail(email);
                this.geolocation = geolocation;
                this.notifications = notifications;
                this.eventList = eventList;
            }

        /**
         * Returns the geolocation of the attendee.
         *
         * @return the geolocation of the attendee
         */
        public Geolocation getGeolocation() {
            return geolocation;
        }

        /**
         * Sets the geolocation of the attendee.
         *
         * @param geolocation the geolocation of the attendee
         */
        public void setGeolocation(Geolocation geolocation) {
            this.geolocation = geolocation;
        }

        /**
         * Returns the notifications flag of the attendee.
         *
         * @return the notifications flag of the attendee
         */
        public boolean isNotifications() {
            return notifications;
        }

        /**
         * Sets the notifications flag of the attendee.
         *
         * @param notifications the notifications flag of the attendee
         */
        public void setNotifications(boolean notifications) {
            this.notifications = notifications;
        }

        /**
         * Returns the event list of the attendee.
         *
         * @return the event list of the attendee
         */
        public ArrayList<String> getEventList() {
            return eventList;
        }

        /**
         * Sets the event list of the attendee.
         *
         * @param eventList the event list of the attendee
         */
        public void setEventList(ArrayList<String> eventList) {
            this.eventList = eventList;
        }

        /**
         * Adds an event to the event list of the attendee.
         *
         * @param eventID the ID of the event to add
         */
        public void addEvent(String eventID) {
            eventList.add(eventID);
        }

        /**
         * Removes an event from the event list of the attendee.
         *
         * @param eventID the ID of the event to remove
         */
        public void removeEvent(String eventID) {
            eventList.remove(eventID);
        }

        /**
         * Clears the event list of the attendee.
         */
        public void clearEventList() {
            eventList.clear();
        }



    }
