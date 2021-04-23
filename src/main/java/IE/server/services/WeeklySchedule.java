package IE.server.services;

import java.util.ArrayList;

public class WeeklySchedule {
    private ArrayList<Course> lastFinalizedCourses = new ArrayList<Course>();
    private ArrayList<Course> lastWaitingCourses = new ArrayList<Course>();

    private ArrayList<Course> finalizedCourses = new ArrayList<Course>();
    private ArrayList<Course> nonFinalizedCourses = new ArrayList<Course>();
    private ArrayList<Course> waitingCourses = new ArrayList<Course>();

    public ArrayList<Course> getLastFinalizedCourses() {
        return lastFinalizedCourses;
    }

    public ArrayList<Course> getLastWaitingCourses() {
        return lastWaitingCourses;
    }

    public ArrayList<Course> getFinalizedCourses() {
        return finalizedCourses;
    }

    public ArrayList<Course> getNonFinalizedCourses() {
        return nonFinalizedCourses;
    }

    public ArrayList<Course> getWaitingCourses() {
        return waitingCourses;
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.addAll(finalizedCourses);
        courses.addAll(nonFinalizedCourses);
        courses.addAll(waitingCourses);
        return courses;
    }

    public int sumOfUnits() {
        int sum = 0;
        for (Course course: finalizedCourses)
            sum += course.getUnits();
        for (Course course: nonFinalizedCourses)
            sum += course.getUnits();
        for (Course course: waitingCourses)
            sum += course.getUnits();
        return sum;
    }

    public void addCourse(Course course) {
        nonFinalizedCourses.add(course);
    }

    public void addToWaitList(Course course) {
        waitingCourses.add(course);
    }

    public void removeCourse(Course course) {
        finalizedCourses.remove(course);
        nonFinalizedCourses.remove(course);
        waitingCourses.remove(course);
    }

    public void reset() {
        finalizedCourses = new ArrayList<Course>();
        nonFinalizedCourses = new ArrayList<Course>();
        waitingCourses = new ArrayList<Course>();
        finalizedCourses.addAll(lastFinalizedCourses);
        waitingCourses.addAll(lastWaitingCourses);
    }

    public void submit() {
        for (Course course: lastFinalizedCourses)
            course.decreaseSignedUp();
        for (Course course: finalizedCourses)
            course.increaseSignedUp();
        for (Course course: nonFinalizedCourses)
            course.increaseSignedUp();
        for (Course course: lastWaitingCourses)
            course.decreaseSignedUp();
        for (Course course: waitingCourses)
            course.increaseSignedUp();
        lastFinalizedCourses = new ArrayList<Course>();
        lastWaitingCourses = new ArrayList<Course>();
        lastFinalizedCourses.addAll(finalizedCourses);
        lastFinalizedCourses.addAll(nonFinalizedCourses);
        lastWaitingCourses.addAll(waitingCourses);
    }

    public void waitListToFinalizedCourse() {
        for (Course course: lastWaitingCourses) {
            lastFinalizedCourses.add(course);
            course.increaseCapacity();
        }
        lastWaitingCourses = new ArrayList<Course>();
    }
}