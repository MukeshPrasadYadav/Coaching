package com.projects.coaching_offline_support.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @Column(length = 20)
    @Builder.Default
    private String country = "India";

    @Column(length = 20)
    private String state;


    private String area;

    @Column(length = 50)
    private String city;

    @Column(length = 6)
    private String pinCode;

    private String postOffice;

    private String building;

    private String houseNo;
}
