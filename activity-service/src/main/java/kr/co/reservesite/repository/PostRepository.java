package kr.co.reservesite.repository;

import kr.co.reservesite.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);
}
