package com.school.model;

public class Classroom {
    private String roomNumber;
    private boolean hasComputer;
    private boolean hasSmartboard;

    public Classroom(String roomNumber, boolean hasComputer, boolean hasSmartboard) {
        this.roomNumber = roomNumber;
        this.hasComputer = hasComputer;
        this.hasSmartboard = hasSmartboard;
    }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public boolean isHasComputer() { return hasComputer; }
    public void setHasComputer(boolean hasComputer) { this.hasComputer = hasComputer; }

    public boolean isHasSmartboard() { return hasSmartboard; }
    public void setHasSmartboard(boolean hasSmartboard) { this.hasSmartboard = hasSmartboard; }

    @Override
    public String toString() {
        return "Classroom{" +
                "roomNumber='" + roomNumber + '\'' +
                ", hasComputer=" + hasComputer +
                ", hasSmartboard=" + hasSmartboard +
                '}';
    }
}

