package dev.mobile.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {
    private String id;
    private String studentId;
    private String name;
    private String dateOfBirth;
    private String phone;
    private String status;
    private String major;
    private String classYear;
    private String joiningDate;
    private String gpa;
}
