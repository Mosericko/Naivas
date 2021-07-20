package com.mdevs.naivas.distributionmanager.classes;

public class DriverDetails {
    String id, firstName, lastName, phoneNumber, status;

    public DriverDetails(String id, String firstName, String lastName, String phoneNumber, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public DriverDetails(String id, String firstName, String lastName, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }
}
