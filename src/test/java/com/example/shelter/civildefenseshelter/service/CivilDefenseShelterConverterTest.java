package com.example.shelter.civildefenseshelter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class CivilDefenseShelterConverterTest {

    @Autowired
    CivilDefenseShelterConverter civilDefenseShelterConverter;

//    @Test
//    @Rollback(value = false)
    public void convert_테스트() {
        //given

        ///when
        civilDefenseShelterConverter.convert();

        //then
    }

}