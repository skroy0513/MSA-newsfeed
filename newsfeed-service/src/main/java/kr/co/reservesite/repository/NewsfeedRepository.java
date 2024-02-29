package kr.co.reservesite.repository;

import kr.co.reservesite.entity.Newsfeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
    @Query("SELECT n FROM Newsfeed n WHERE n.userId = :userId AND n.senderId <> :senderId")
    Page<Newsfeed> findByUserIdAndNotSenderId(@Param("userId") Long userId,
                                                        @Param("senderId") Long senderId,
                                                        Pageable pageable);
}
