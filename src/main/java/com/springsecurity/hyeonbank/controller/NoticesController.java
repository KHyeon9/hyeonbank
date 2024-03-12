package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Notice;
import com.springsecurity.hyeonbank.repository.NoticeRepository;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class NoticesController {

    private NoticeRepository noticeRepository;

    public NoticesController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices() {
        List<Notice> notices =noticeRepository.findAllActiveNotices();

        if (notices != null) {
            return ResponseEntity.ok()
                    // 데이터를 가져온 후 60초 동안은 새로고침해도 db에 접근하지 않고
                    // 캐시에서 가져와 트래픽을 줄임
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)) 
                    .body(notices);
        }

        return null;
    }
}
