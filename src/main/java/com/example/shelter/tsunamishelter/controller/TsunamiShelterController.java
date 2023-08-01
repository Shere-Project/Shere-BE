package com.example.shelter.tsunamishelter.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.TsunamiShelterDto;
import com.example.shelter.common.dto.ShelterListDto;
import com.example.shelter.common.dto.ShelterPageDto;
import com.example.shelter.tsunamishelter.service.TsunamiShelterService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class TsunamiShelterController {

    private final TsunamiShelterService tsunamiShelterService;
    private final DongService dongService;
    private final int PAGE_SIZE = 8;
    private final int RADIUS = 500;

    @GetMapping("/api/shelters/tsunami/{id}")
    public ResponseEntity<TsunamiShelterDto> getTsunamiShelter(@PathVariable("id") Long id) {
        TsunamiShelter tsunamiShelter = tsunamiShelterService.findById(id);
        return ResponseEntity.ok(TsunamiShelterDto.of(tsunamiShelter));
    }

    @GetMapping("/api/shelters/tsunami")
    public ResponseEntity<ShelterPageDto> getTsunamiSheltersByDong(@RequestParam("dong_id") Long dongId,
                                                                   @RequestParam("page") @Positive int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        Dong dong = dongService.findById(dongId);
        Page<TsunamiShelter> tsunamiShelters = tsunamiShelterService.findAllByDong(dong, pageRequest);
        return ResponseEntity.ok(ShelterPageDto.of(tsunamiShelters));
    }

    @GetMapping("/api/shelters/tsunami/current")
    public ResponseEntity<ShelterListDto> getTsunamiSheltersByCurrent(@RequestParam("lat") double latitude,
                                                                      @RequestParam("lon") double longitude) {

        List<TsunamiShelter> tsunamiShelters = tsunamiShelterService.findAllByCurrent(latitude, longitude, RADIUS);
        return ResponseEntity.ok(ShelterListDto.of(tsunamiShelters));
    }

}