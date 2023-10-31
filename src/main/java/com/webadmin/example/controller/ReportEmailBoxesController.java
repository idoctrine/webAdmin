package com.webadmin.example.controller;

import com.webadmin.example.bean.PopularMailBox;
import com.webadmin.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportEmailBoxesController {

    private final UserService userService;

    @GetMapping("/popularMailboxes")
    public List<PopularMailBox> popularMailboxes() {
        return userService.popularMailboxes();
    }
}
