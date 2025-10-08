package com.sample.codes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.codes.entity.Payment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FormModel {
    @JsonProperty(value = "full_name")
    private String fullName;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "dob")
    private Date dob;

    @JsonProperty(value = "short_bio")
    private String shortBio;

    @JsonProperty(value = "resume")
    private String resume;

    @JsonProperty(value = "resume_file")
    private byte[] resumeFile;

    @JsonProperty(value = "payments")
    private List<Payment> payments;

}
