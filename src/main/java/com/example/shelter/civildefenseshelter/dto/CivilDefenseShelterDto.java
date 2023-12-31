package com.example.shelter.civildefenseshelter.dto;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CivilDefenseShelterDto {

    private Long id;

    private String name;

    private String fullAddress;

    private String roadAddress;

    private double latitude;

    private double longitude;

    private double area;

    private String type;

    protected CivilDefenseShelterDto(CivilDefenseShelter civilDefenseShelter) {
        this.id = civilDefenseShelter.getId();
        this.name = civilDefenseShelter.getName();
        this.fullAddress = civilDefenseShelter.getAddress().getFullAddress();
        this.roadAddress = civilDefenseShelter.getRoadAddress();
        this.latitude = civilDefenseShelter.getLatitude();
        this.longitude = civilDefenseShelter.getLongitude();
        this.area = civilDefenseShelter.getArea();
        this.type = civilDefenseShelter.getType();
    }

    public static CivilDefenseShelterDto of(CivilDefenseShelter civilDefenseShelter) {
        return new CivilDefenseShelterDto(civilDefenseShelter);
    }

}
