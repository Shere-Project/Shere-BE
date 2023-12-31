package com.example.shelter.tsunamishelter.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.sido.Sido;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TsunamiShelterRepository extends JpaRepository<TsunamiShelter, Long> {

    @Query("select t from TsunamiShelter t where t.id = :id and t.isDeleted = false")
    Optional<TsunamiShelter> findByIdNotDeleted(@Param("id") Long id);

    @Query("select t from TsunamiShelter t where t.dong = :dong and t.isDeleted = false")
    List<TsunamiShelter> findAllByDongNotDeleted(@Param("dong") Dong dong);

    @Query("select t from TsunamiShelter t where t.dong = :dong and t.isDeleted = false")
    Page<TsunamiShelter> findAllByDongNotDeleted(@Param("dong") Dong dong, Pageable pageable);

    @Query("select t from TsunamiShelter t " +
            "where t.latitude between :minLat and :maxLat and " +
            "t.longitude between :minLon and :maxLon and " +
            "t.isDeleted = false")
    List<TsunamiShelter> findAllBySquareRangeNotDeleted(@Param("minLat") double minLat,
                                                        @Param("maxLat") double maxLat,
                                                        @Param("minLon") double minLon,
                                                        @Param("maxLon") double maxLon);

    @Query("select count(t) from TsunamiShelter t where t.isDeleted = false")
    int countAllNotDeleted();

    @Query("select count(t) from TsunamiShelter t " +
            "left join Dong d on t.dong = d " +
            "left join Sigungu sgg on d.sigungu = sgg " +
            "left join Sido sd on sgg.sido = sd " +
            "where sd = :sido and t.isDeleted = false")
    int countAllBySidoNotDeleted(@Param("sido") Sido sido);

}
