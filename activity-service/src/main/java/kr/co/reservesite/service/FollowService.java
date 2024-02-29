package kr.co.reservesite.service;

import kr.co.reservesite.entity.Follow;
import kr.co.reservesite.exception.DuplicatedFollowException;
import kr.co.reservesite.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;

    public Long followUser(Long fromUserId, Long toUserId){
        try {
            Follow nFollow = followRepository.save(Follow.builder()
                    .fromUser(fromUserId)
                    .toUser(toUserId)
                    .build());

//            newsfeedService.createNewsfeed(nFollow.getToUser(),
//                    nFollow.getFromUser(),
//                    NewsfeedType.FOLLOW,
//                    nFollow.getId());

        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedFollowException("이미 팔로우하였습니다.");
        }


        return toUserId;
    }

    public Long followCancel(Long fromUserId, Long toUserId) {
        Follow follow = followRepository.findByToUserAndFromUser(toUserId, fromUserId)
                .orElseThrow(() -> new RuntimeException("팔로우 정보를 찾을 수 없습니다."));

        followRepository.deleteById(follow.getId());

        return toUserId;
    }

    public List<Follow> showMyFollower(Long userId) {
        return followRepository.findAllByToUser(userId);
    }


    public List<Follow> showMyFollowing(Long userId) {
        return followRepository.findAllByFromUser(userId);
    }
}
