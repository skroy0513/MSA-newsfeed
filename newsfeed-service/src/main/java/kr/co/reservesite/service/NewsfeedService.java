package kr.co.reservesite.service;

import kr.co.reservesite.entity.Newsfeed;
import kr.co.reservesite.exception.NotCorrectException;
import kr.co.reservesite.exception.NotFoundException;
import kr.co.reservesite.repository.NewsfeedRepository;
import kr.co.reservesite.web.form.NewsfeedForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsfeedService {

    private final NewsfeedRepository newsfeedRepository;

    // 뉴스피드를 생성한다.
    public void createNewsfeed(NewsfeedForm newsfeedForm) {
        log.info("newsfeed값 보기 -> {}", newsfeedForm);
        Newsfeed newsfeed = Newsfeed.builder().newsfeedType(newsfeedForm.getType())
                .userId(newsfeedForm.getUserId())
                .senderId(newsfeedForm.getSenderId())
                .typeId(newsfeedForm.getTypeId())
                .build();
        newsfeedRepository.save(newsfeed);
        log.info("newfeed저장 성공 -> {}", newsfeed);
    }

    // 뉴스피드를 불러온다.
    public Page<Newsfeed> showMyNewsfeed(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        return newsfeedRepository.findByUserIdAndNotSenderId(userId, userId, pageable);
    }

    // 읽음 처리를 한다.
    public String readNewsfeed(Long userId, Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 알림을 찾을 수 없습니다."));
        if (!userId.equals(newsfeed.getUserId())) {
            throw new NotCorrectException("알림의 수신자가 아닙니다.");
        }
        newsfeed.setRead(true);
        newsfeedRepository.save(newsfeed);

        return "read";
    }

}
