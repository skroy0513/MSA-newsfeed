package kr.co.reservesite.service;

import kr.co.reservesite.client.NewsfeedClient;
import kr.co.reservesite.client.Userclient;
import kr.co.reservesite.dto.CommentDto;
import kr.co.reservesite.dto.PostDto;
import kr.co.reservesite.entity.*;
import kr.co.reservesite.exception.NoContentException;
import kr.co.reservesite.exception.NotCorrectException;
import kr.co.reservesite.exception.NotFoundException;
import kr.co.reservesite.repository.*;
import kr.co.reservesite.web.form.NewsfeedForm;
import kr.co.reservesite.web.form.PostForm;
import kr.co.reservesite.web.type.NewsfeedType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ThumbsRepository thumbsRepository;
    private final CmtThumbsRepository cmtThumbsRepository;
    private final FollowRepository followRepository;
    private final NewsfeedClient newsfeedClient;
    private final Userclient userclient;

    /*
    유저 아이디를 User-module로 전송해서 해당 아이디가 존재하는지 확인하고
    존재한다면 닉네임을 반환해준다.
    그 닉네임을 Post에 저장
     */

    /*
    게시글, 댓글, 좋아요를 저장할때
    newsfeed-module에 (내 아이디, 받는사람아이디, 뉴스피드 타입, 해당 타입의 아이디)를 전송
    데이터의 형태는 (@RequestBody NewsfeedForm newsfeedForm)의 형태..?
    newsfeed-module에서 받은 데이터를 저장한다.
     */

    public PostDto createPost(Long userId, PostForm postForm) {
        String nickname = userclient.getNickname(userId);
        Post post = postRepository.save(Post.builder()
                        .userId(userId)
                        .title(postForm.getTitle())
                        .nickname(nickname)
                        .content(postForm.getContent()).build());

        // 게시글을 작성하면 나의 팔로워들에게 뉴스피드를 생성시킨다.
        // 팔로우 테이블에서 to_user가 작성자인 데이터를 찾은 뒤 그 데이터에서 from_user 아이디로 String 리스트를 만든다.
        List<Follow> follows = followRepository.findAllByToUser(userId);
        List<Long> ids = new ArrayList<>();

        for (Follow follow : follows) {
            ids.add(follow.getFromUser());
        }

        for (Long followerId : ids) {
            newsfeedClient.saveNewsfeed(NewsfeedForm.builder()
                    .userId(followerId)
                    .senderId(userId)
                    .type(NewsfeedType.POST)
                    .typeId(post.getId()).build());
        }

        return PostDto.builder()
                .userId(post.getUserId())
                .id(post.getId())
                .title(post.getTitle())
                .nickname(post.getNickname())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .likeCnt(post.getLikeCnt())
                .createdDate(post.getCreatedDate()).build();
    }

    public PostDto modifyPost(Long userId, PostDto postDto) {
        Post post = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다"));

        if (!userId.equals(postDto.getUserId())) {
            throw new NotFoundException("게시물을 찾을 수 없습니다.");
        }
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        Post mdfPost = postRepository.save(post);
        return PostDto.builder()
                .userId(userId)
                .id(mdfPost.getId())
                .title(mdfPost.getTitle())
                .content(mdfPost.getContent())
                .commentCnt(mdfPost.getCommentCnt())
                .likeCnt(mdfPost.getLikeCnt())
                .createdDate(post.getCreatedDate()).build();
    }

    public PostDto readPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        return PostDto.builder()
                .userId(post.getUserId())
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .likeCnt(post.getLikeCnt())
                .createdDate(post.getCreatedDate()).build();
    }

    public CommentDto createCmt(Long userId, String content, Long postId) {
        // 존재하지 않는 게시글의 접근 시
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        if (content.isBlank()){
            throw new NoContentException("댓글을 입력해주세요");
        }

        // 댓글 저장
        Comment comment = commentRepository.save(Comment.builder()
                        .content(content)
                        .postId(postId)
                        .userId(userId).build());

        //게시글 댓글수 증가
        post.setCommentCnt(post.getCommentCnt() + 1);
        postRepository.save(post);

        newsfeedClient.saveNewsfeed(NewsfeedForm.builder()
                .userId(post.getUserId())
                .senderId(userId)
                .type(NewsfeedType.COMMENT)
                .typeId(comment.getId()).build());

        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .content(content)
                .likeCnt(0)
                .build();
    }

    public CommentDto modifyCmt(Long userId, String content, Long cmtId, Long postId) {
        // 존재하지 않는 게시글 및 댓글에 접근 시
        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(cmtId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

        if (!userId.equals(comment.getUserId())) {
            throw new NotCorrectException("댓글의 작성자와 일치하지 않습니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);

        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .likeCnt(comment.getLikeCnt())
                .build();
    }

    public String deleteCmt(Long userId, Long cmtId, Long postId) {
        // 존재하지 않는 게시글 및 댓글에 접근 시
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(cmtId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (!userId.equals(comment.getUserId())) {
            throw new NotCorrectException("댓글의 작성자와 일치하지 않습니다.");
        }
        commentRepository.deleteById(cmtId);

        // 해당 게시글의 댓글 수 감소
        post.setCommentCnt(post.getCommentCnt() - 1);
        postRepository.save(post);

        return "delete success";
    }

    public String thumbsUpPost(Long userId, Long postId) {
        // 존재하지 않는 게시글에 접근 시
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        Thumbs thumbs = thumbsRepository.save(Thumbs.builder()
                .postId(postId)
                .userId(userId).build());

        // 게시글의 좋아요 수 증가
        post.setLikeCnt(post.getLikeCnt() + 1);
        postRepository.save(post);

        newsfeedClient.saveNewsfeed(NewsfeedForm.builder()
                .userId(post.getUserId())
                .senderId(userId)
                .type(NewsfeedType.POST_THUMBS_UP)
                .typeId(thumbs.getId()).build());

        return "success";
    }

    public String thumbsDownPost(Long userId, Long postId, Long thumbsId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Thumbs thumbs = thumbsRepository.findById(thumbsId)
                        .orElseThrow(() -> new NotFoundException("해당 좋아요를 찾을 수 없습니다."));
        if (!userId.equals(thumbs.getUserId())) {
            throw new NotCorrectException("좋아요한 사용자와 일치하지 않습니다.");
        }

        thumbsRepository.deleteById(thumbsId);

        // 해당 게시글의 좋아요 수 감소
        post.setLikeCnt(post.getLikeCnt() - 1);
        postRepository.save(post);

        return "delete success";
    }

    public String thumbsUpCmt(Long userId, Long cmtId) {
        Comment cmt = commentRepository.findById(cmtId)
                // 존재하지 않는 댓글에 접근 시
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        CmtThumbs cmtThumbs = cmtThumbsRepository.save(CmtThumbs.builder()
                .commentId(cmtId)
                .userId(userId).build());

        // 댓글의 좋아요 수 증가
        cmt.setLikeCnt(cmt.getLikeCnt() + 1);
        commentRepository.save(cmt);

        newsfeedClient.saveNewsfeed(NewsfeedForm.builder()
                .userId(cmt.getUserId())
                .senderId(userId)
                .type(NewsfeedType.COMMENT_THUMBS_UP)
                .typeId(cmt.getId()).build());

        return "success";
    }

    public String thumbsDownCmt(Long userId, Long cmtId, Long cmtThumbsId) {
        Comment cmt = commentRepository.findById(cmtId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        CmtThumbs cmtThumbs = cmtThumbsRepository.findById(cmtThumbsId)
                .orElseThrow(() -> new NotFoundException("좋아요를 찾을 수 없습니다."));

        if (!userId.equals(cmtThumbs.getUserId())){
            throw new NotCorrectException("좋아요한 사용자와 일치하지 않습니다.");
        }

        cmtThumbsRepository.deleteById(cmtThumbsId);

        cmt.setLikeCnt(cmt.getLikeCnt() - 1);
        commentRepository.save(cmt);

        return "delete success";
    }

    public Page<Post> getFollowerPost(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        // 내(from_user)가 팔로우 하는 사람의 아이디(to_user)를 가져온다.
        List<Follow> follows = followRepository.findAllByFromUser(userId);
        List<Long> users = new ArrayList<>();
        for (Follow follow : follows) {
            users.add(follow.getToUser());
        }
        return postRepository.findByUserIdIn(users, pageable);
    }
}
