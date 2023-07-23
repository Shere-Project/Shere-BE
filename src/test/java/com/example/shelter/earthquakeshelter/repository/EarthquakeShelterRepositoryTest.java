package com.example.shelter.earthquakeshelter.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class EarthquakeShelterRepositoryTest {

    @Autowired
    EarthquakeShelterRepository earthquakeShelterRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void findByIdNotDeleted_존재하는_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                .name("전일중학교")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .area(100)
                .build();
        em.persistAndFlush(earthquakeShelter);
        em.clear();

        ///when
        Optional<EarthquakeShelter> findShelter = earthquakeShelterRepository
                .findByIdNotDeleted(earthquakeShelter.getId());

        //then
        assertThat(findShelter).isNotEmpty();
        assertThat(findShelter.get().getId()).isEqualTo(earthquakeShelter.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                .name("전일중학교")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .area(100)
                .build();
        earthquakeShelter.updateDeleted(true);
        em.persistAndFlush(earthquakeShelter);
        em.clear();

        ///when
        Optional<EarthquakeShelter> findShelter = earthquakeShelterRepository
                .findByIdNotDeleted(earthquakeShelter.getId());

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_지진대피소_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<EarthquakeShelter> findShelter = earthquakeShelterRepository
                .findByIdNotDeleted(id);

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findAllByDongNotDeleted_존재하는_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<EarthquakeShelter> findShelters = earthquakeShelterRepository
                .findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(findShelters.getNumber()).isEqualTo(0);
        assertThat(findShelters.getSize()).isEqualTo(4);
        assertThat(findShelters.getNumberOfElements()).isEqualTo(4);
    }

    @Test
    public void findAllByDongNotDeleted_삭제된_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            earthquakeShelter.updateDeleted(true);
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<EarthquakeShelter> findShelters = earthquakeShelterRepository
                .findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(findShelters.isEmpty()).isTrue();
    }

    @Test
    public void findAllBySquareRangeNotDeleted_존재하는_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        List<Double[]> gpsList = List.of(
                new Double[]{34.1, 124.1},
                new Double[]{35.1, 125.1},
                new Double[]{36.1, 126.1},
                new Double[]{37.1, 127.1},
                new Double[]{38.1, 128.1},
                new Double[]{39.1, 129.1}
        );

        for (int i = 0; i < 6; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<EarthquakeShelter> findShelters = earthquakeShelterRepository
                .findAllBySquareRangeNotDeleted(35, 40, 126, 129);

        //then
        assertThat(findShelters.size()).isEqualTo(3);
    }

    @Test
    public void findAllBySquareRangeNotDeleted_삭제된_지진대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        List<Double[]> gpsList = List.of(
                new Double[]{34.1, 124.1},
                new Double[]{35.1, 125.1},
                new Double[]{36.1, 126.1},
                new Double[]{37.1, 127.1},
                new Double[]{38.1, 128.1},
                new Double[]{39.1, 129.1}
        );

        for (int i = 0; i < 6; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .area(100)
                    .build();
            earthquakeShelter.updateDeleted(true);
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<EarthquakeShelter> findShelters = earthquakeShelterRepository
                .findAllBySquareRangeNotDeleted(35, 40, 126, 129);

        //then
        assertThat(findShelters.isEmpty()).isTrue();
    }

    @Test
    public void countAllNotDeleted_존재하는_지진해일대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = earthquakeShelterRepository.countAllNotDeleted();

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllNotDeleted_삭제된_지진해일대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = earthquakeShelterRepository.countAllNotDeleted();

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllBySidoNotDeleted_시도에_존재하는_지진해일대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = earthquakeShelterRepository.countAllBySidoNotDeleted(sido);

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllBySidoNotDeleted_시도에_존재하지_않는_지진해일대피소_테스트() {
        //given
        Sido sido1 = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido1);

        Sido sido2 = Sido.builder()
                .name("경기도")
                .build();
        em.persist(sido2);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido1)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            EarthquakeShelter earthquakeShelter = EarthquakeShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            em.persist(earthquakeShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = earthquakeShelterRepository.countAllBySidoNotDeleted(sido2);

        //then
        assertThat(count).isZero();
    }

}