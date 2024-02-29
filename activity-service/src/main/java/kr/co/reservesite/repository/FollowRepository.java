package kr.co.reservesite.repository;

import kr.co.reservesite.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByToUser(Long toUser);
    List<Follow> findAllByFromUser(Long fromUser);
    Optional<Follow> findByToUserAndFromUser(Long toUser, Long fromUser);
}
