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
public class Certificate {
    private String id;
    private String name;
    private String studentId;
    private String cores;
    private String issueDate;
    private String expiryDate;

}
