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

    @Column(nullable = false,length = 20)
    @Builder.Default
    private String country = "India";

    @Column(nullable = false,length = 20)
    private String state;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false,length = 50)
    private String city;

    @Column(nullable = false,length = 6)
    private String pinCode;

    private String postOffice;

    private String building;

    private String houseNo;
}
