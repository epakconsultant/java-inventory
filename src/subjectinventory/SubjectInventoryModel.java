/*  Name:  [your name here]
    Assignment:  [assignment name]
    Program: [your program name here]
    Date:  [assignment due date here]

    Description:
    [program description in your own words]
*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package subjectinventory;

/**
 *
 * @author meraj
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import subjectinventory.CategoryTyps;
import subjectinventory.Subject;

public class SubjectInventoryModel {
    private ArrayList<Subject> courses = new ArrayList();
    private ArrayList<Subject> categoryCourses = new ArrayList();
    private ArrayList<String> categories = new ArrayList();
    private String selectedCourseId = null;
    private String newCourseId = null;
    private String errorMessage;

    public SubjectInventoryModel() {
        Enum[] constants = (Enum[])CategoryTyps.class.getEnumConstants();
        for (int i = 0; i < constants.length; ++i) {
            String cat = constants[i].name();
            if (cat.equals("NONE")) continue;
            this.categories.add(cat);
        }
        this.errorMessage = "No Error";
    }

    public void readCourseFile(File file) {
        this.errorMessage = "No Error";
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            this.errorMessage = "[ERROR] " + e.getMessage();
            System.out.println(this.errorMessage);
            return;
        }
        Collections.sort(lines);
        this.courses.clear();
        for (int i = 0; i < lines.size(); ++i) {
            String[] tokens = ((String)lines.get(i)).split(";");
            if (tokens.length < 4) continue;
            for (int j = 0; j < tokens.length; ++j) {
                tokens[j] = tokens[j].trim();
            }
            Subject course = new Subject(tokens[0], tokens[1], Integer.parseInt(tokens[2]), Subject.getCategoryFromString(tokens[3]));
            this.courses.add(course);
        }
        System.out.println("Loaded " + this.courses.size() + " courses from " + file.getName() + ".");
    }

    public void saveCourseFile(File file) {
        this.errorMessage = "No Error";
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < this.courses.size(); ++i) {
                Subject course = this.courses.get(i);
                String line = course.getId() + " ; " + course.getTitle() + " ; " + course.getCredit() + " ; " + course.getCategoryName();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
            System.out.println("Saved " + this.courses.size() + " courses to " + file.getName() + ".");
        }
        catch (Exception e) {
            this.errorMessage = "[ERROR] " + e.getMessage();
            System.out.println(this.errorMessage);
            return;
        }
    }

    public int getCourseCount() {
        return this.courses.size();
    }

    public Subject getCourse(int index) {
        Subject course = null;
        if (index >= 0 && index < this.courses.size()) {
            course = this.courses.get(index);
        }
        return course;
    }

    public Subject getCourse(String id) {
        return this.getCourse(this.findCourseIndex(id));
    }

    public String getCourseId(int index) {
        Subject course = this.getCourse(index);
        if (course != null) {
            return course.getId();
        }
        return null;
    }

    public int getCategoryCount() {
        return this.categories.size();
    }

    public String getCategoryName(int index) {
        String cat = null;
        if (index >= 0 && index < this.categories.size()) {
            cat = this.categories.get(index);
        }
        return cat;
    }

    public String getSelectedCourseId() {
        return this.selectedCourseId;
    }

    public String getNewCourseId() {
        return this.newCourseId;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public final ArrayList<String> getCourseIds() {
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < this.courses.size(); ++i) {
            ids.add(this.courses.get(i).getId());
        }
        return ids;
    }

    public int findCourseIndex(String id) {
        for (int i = 0; i < this.courses.size(); ++i) {
            Subject course = this.courses.get(i);
            if (!course.getId().equals(id)) continue;
            return i;
        }
        return -1;
    }

    public ArrayList<Subject> findCoursesById(String id) {
        id = id.toLowerCase();
        ArrayList<Subject> searchCourses = new ArrayList<Subject>();
        int count = this.courses.size();
        for (int i = 0; i < count; ++i) {
            Subject c = this.courses.get(i);
            if (!c.getId().toLowerCase().contains(id.toLowerCase())) continue;
            searchCourses.add(c);
        }
        return searchCourses;
    }

    public ArrayList<Subject> findCoursesByTitle(String title) {
        ArrayList<Subject> searchCourses = new ArrayList<Subject>();
        int count = this.courses.size();
        for (int i = 0; i < count; ++i) {
            Subject c = this.courses.get(i);
            if (!c.getTitle().toLowerCase().contains(title.toLowerCase())) continue;
            searchCourses.add(c);
        }
        return searchCourses;
    }

    public ArrayList<Subject> findCoursesByCategory(String cat) {
        CategoryTyps category = Subject.getCategoryFromString(cat.toUpperCase());
        ArrayList<Subject> searchCourses = new ArrayList<Subject>();
        int count = this.courses.size();
        for (int i = 0; i < count; ++i) {
            Subject course = this.courses.get(i);
            if (course.getCategory() != category) continue;
            searchCourses.add(course);
        }
        return searchCourses;
    }

    public void setSelectedCourseId(String id) {
        this.selectedCourseId = id;
    }

    public void setNewCourseId(String id) {
        this.newCourseId = id;
    }

    public void removeCourse(int index) {
        if (index >= 0 && index < this.courses.size()) {
            String id = this.courses.get(index).getId();
            this.courses.remove(index);
            System.out.println("Removed a course " + id + " from the list.");
        }
    }

    public void removeCourse(String id) {
        int index = this.findCourseIndex(id);
        this.removeCourse(index);
    }

    public void updateCourse(String id, String newTitle, int newCredit, String newCat) {
        Subject course = this.getCourse(id);
        CategoryTyps cat = Subject.getCategoryFromString(newCat);
        course.set(id, newTitle, newCredit, cat);
        System.out.println("Updated " + id);
    }

    public void addCourse(Subject course) {
        this.courses.add(course);
        this.sort();
        this.newCourseId = course.getId();
    }

    private void sort() {
        Collections.sort(this.courses, new Comparator<Subject>(){

            @Override
            public int compare(Subject c1, Subject c2) {
                return c1.getId().compareTo(c2.getId());
            }
        });
    }

    public boolean validateCourseId(String id) {
        String pattern = "^[A-Za-z]{4}[0-9]{5}$";
        if (id != null && id.matches("^[A-Za-z]{4}[0-9]{5}$")) {
            return true;
        }
        return false;
    }

}
