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
import java.io.PrintStream;
import java.util.Objects;
import subjectinventory.CategoryTyps;

public class Subject
implements Comparable<Subject> {
    String id;
    String title;
    int credit;
    CategoryTyps category;

    public Subject() {
        this("", "", 0, CategoryTyps.NONE);
    }

    public Subject(String id, String title, int credit, CategoryTyps cat) {
        this.set(id, title, credit, cat);
    }

    public String toString() {
        return "Course(" + this.id + ", " + this.title + ")";
    }

    public void printSelf() {
        System.out.println(this);
    }

    public void set(String id, String title, int credit, CategoryTyps cat) {
        this.id = id;
        this.title = title;
        this.credit = credit;
        this.category = cat;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setCategory(CategoryTyps cat) {
        this.category = cat;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getCredit() {
        return this.credit;
    }

    public CategoryTyps getCategory() {
        return this.category;
    }

    public String getCategoryName() {
        return this.category.name();
    }

    public static CategoryTyps getCategoryFromString(String s) {
        CategoryTyps cat = s.equals("DATABASE") ? CategoryTyps.DATABASE : (s.equals("INFORMATION") ? CategoryTyps.INFORMATION : (s.equals("MATH") ? CategoryTyps.MATH : (s.equals("PROGRAMMING") ? CategoryTyps.PROGRAMMING : (s.equals("SYSTEM") ? CategoryTyps.SYSTEM : CategoryTyps.NONE))));
        return cat;
    }

    public boolean equals(Object rhs) {
        if (rhs instanceof Subject) {
            return this.id.equals(((Subject)rhs).getId());
        }
        return this == rhs;
    }

    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(Subject rhs) {
        return this.id.compareTo(rhs.getId());
    }
}


