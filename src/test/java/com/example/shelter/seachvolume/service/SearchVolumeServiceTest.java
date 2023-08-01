package com.example.shelter.seachvolume.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.seachvolume.dto.ShelterVolumeDto;
import com.example.shelter.seachvolume.repository.SearchVolumeRepository;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchVolumeServiceTest {

    @Mock
    SearchVolumeRepository searchVolumeRepository;

    @InjectMocks
    SearchVolumeService searchVolumeService;


    @Test
    public void updateSearchVolume_생성_테스트(@Mock Dong dong) {
        //given
        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        when(searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(searchVolumeRepository.save(any(SearchVolume.class))).thenReturn(searchVolume);

        ///when
        searchVolumeService.updateSearchVolume(dong, ShelterType.TSUNAMI, LocalDate.now());

        //then
        verify(searchVolumeRepository, times(1))
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now());
        verify(searchVolumeRepository, times(1)).save(any(SearchVolume.class));
    }

    @Test
    public void updateSearchVolume_업데이트_테스트(@Mock Dong dong) {
        //given
        int initVolume = 1;
        SearchVolume searchVolume = SearchVolume.builder()
                .volume(initVolume)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        when(searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now()))
                .thenReturn(Optional.of(searchVolume));

        ///when
        searchVolumeService.updateSearchVolume(dong, ShelterType.TSUNAMI, LocalDate.now());

        //then
        assertThat(searchVolume.getVolume()).isEqualTo(initVolume + 1);
        verify(searchVolumeRepository, times(1))
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now());
    }

    @Test
    public void getTotalVolumeByDate_테스트() {
        //given
        LocalDate now = LocalDate.now();
        int result = 100;
        when(searchVolumeRepository.getTotalVolumeByDateNotDeleted(now)).thenReturn(result);

        ///when
        int totalVolume = searchVolumeService.getTotalVolumeByDate(now);

        //then
        assertThat(totalVolume).isEqualTo(result);
        verify(searchVolumeRepository, times(1))
                .getTotalVolumeByDateNotDeleted(now);
    }

    @Test
    public void getTotalVolumeByDateRange_테스트() {
        //given
        LocalDate to = LocalDate.now();
        LocalDate from = LocalDate.now().minusDays(7);
        int result = 100;
        when(searchVolumeRepository.getTotalVolumeByDateRangeNotDeleted(from, to)).thenReturn(result);

        ///when
        int totalVolume = searchVolumeService.getTotalVolumeByDateRange(from, to);

        //then
        assertThat(totalVolume).isEqualTo(result);
        verify(searchVolumeRepository, times(1))
                .getTotalVolumeByDateRangeNotDeleted(from, to);
    }

    @Test
    public void getSidoSVolumeMap_테스트() {
        //given
        LocalDate now = LocalDate.now();
        List<RegionVolumeDto> regionVolumeDtoList = List.of(
                new RegionVolumeDto("서울특별시", ShelterType.TSUNAMI, 10L),
                new RegionVolumeDto("서울특별시", ShelterType.EARTHQUAKE, 10L),
                new RegionVolumeDto("서울특별시", ShelterType.CIVIL_DEFENCE, 10L),
                new RegionVolumeDto("부산광역시", ShelterType.TSUNAMI, 10L),
                new RegionVolumeDto("부산광역시", ShelterType.EARTHQUAKE, 10L),
                new RegionVolumeDto("부산광역시", ShelterType.CIVIL_DEFENCE, 10L)
        );
        when(searchVolumeRepository.countSidoByDateNotDeleted(now)).thenReturn(regionVolumeDtoList);

        ///when
        Map<String, ShelterVolumeDto> sidoVolumeMap = searchVolumeService.getSidoVolumeMap(now);

        //then
        assertThat(sidoVolumeMap.containsKey("서울특별시")).isTrue();
        assertThat(sidoVolumeMap.get("서울특별시").getTsunami()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("서울특별시").getEarthquake()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("서울특별시").getCivilDefense()).isEqualTo(10L);
        assertThat(sidoVolumeMap.containsKey("부산광역시")).isTrue();
        assertThat(sidoVolumeMap.get("부산광역시").getTsunami()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("부산광역시").getEarthquake()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("부산광역시").getCivilDefense()).isEqualTo(10L);
        verify(searchVolumeRepository, times(1)).countSidoByDateNotDeleted(now);
    }

    @Test
    public void getSigunguVolumeMap_테스트(@Mock Sido sido) {
        //given
        LocalDate now = LocalDate.now();
        List<RegionVolumeDto> regionVolumeDtoList = List.of(
                new RegionVolumeDto("강동구", ShelterType.TSUNAMI, 10L),
                new RegionVolumeDto("강동구", ShelterType.EARTHQUAKE, 10L),
                new RegionVolumeDto("강동구", ShelterType.CIVIL_DEFENCE, 10L),
                new RegionVolumeDto("동대문구", ShelterType.TSUNAMI, 10L),
                new RegionVolumeDto("동대문구", ShelterType.EARTHQUAKE, 10L),
                new RegionVolumeDto("동대문구", ShelterType.CIVIL_DEFENCE, 10L)
        );
        when(searchVolumeRepository.countSigunguBySidoAndDateNotDeleted(sido, now))
                .thenReturn(regionVolumeDtoList);

        ///when
        Map<String, ShelterVolumeDto> sidoVolumeMap = searchVolumeService.getSigunguVolumeMap(sido, now);

        //then
        assertThat(sidoVolumeMap.containsKey("강동구")).isTrue();
        assertThat(sidoVolumeMap.get("강동구").getTsunami()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("강동구").getEarthquake()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("강동구").getCivilDefense()).isEqualTo(10L);
        assertThat(sidoVolumeMap.containsKey("동대문구")).isTrue();
        assertThat(sidoVolumeMap.get("동대문구").getTsunami()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("동대문구").getEarthquake()).isEqualTo(10L);
        assertThat(sidoVolumeMap.get("동대문구").getCivilDefense()).isEqualTo(10L);
        verify(searchVolumeRepository, times(1))
                .countSigunguBySidoAndDateNotDeleted(sido, now);
    }

    @Test
    public void getDateVolumeMap_테스트(@Mock Dong dong) {
//        //given
//        LocalDate to = LocalDate.now();
//        LocalDate from = LocalDate.now().minusDays(7);
//        List<SearchVolume> searchVolumeList = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            SearchVolume searchVolume = SearchVolume.builder()
//                    .createdDate(LocalDate.now().minusDays(i))
//                    .volume(10)
//                    .shelterType(ShelterType.TSUNAMI)
//                    .dong(dong)
//                    .build();
//            searchVolumeList.add(searchVolume);
//        }
//
//        when(searchVolumeRepository
//                .findAllByDongAndTypeAndDateRangeNotDeleted(dong, ShelterType.TSUNAMI, from, to))
//                .thenReturn(searchVolumeList);
//
//        ///when
//        Map<LocalDate, ShelterVolumeDto> dateVolumeMap = searchVolumeService
//                .getDateVolumeMap(dong, ShelterType.TSUNAMI, from, to);
//
//        //then
    }

}