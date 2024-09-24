package com.example.neo_v2.controller;

import com.example.neo_v2.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        model.addAttribute("location", location);
        log.info("카카오 로그인 요청: {}", location);
        return "보이는 경로로 로그인 가능" + location;
    }

    /*

    @GetMapping("/code")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        log.info("code = {}", code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
     */

    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> getAccessToken(@RequestParam String code) {
        // KakaoService를 통해 access_token을 가져옴
        String accessToken = kakaoService.getKakaoAccessToken(code);
        log.info("카카오 코드 = {}",code);

        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        System.out.println("login Controller : " + userInfo);

        if (accessToken != null && !accessToken.isEmpty()) {
            log.info("카카오 Access Token: {}", accessToken);  // 로그로 access_token 출력
            return new ResponseEntity<>(HttpStatus.OK);  // 클라이언트로 access_token 반환
        } else {
            log.error("Failed to retrieve Kakao Access Token. Access token is null or empty.");
            return new ResponseEntity<>("Failed to retrieve access token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /*

    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> getAccessToken(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        log.info("카카오 코드 = {}", code);

        String accessToken = kakaoService.getKakaoAccessToken(code);

        if (accessToken != null && !accessToken.isEmpty()) {
            log.info("카카오 Access Token: {}", accessToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("카카오 액세스 토큰 검색에 실패했습니다. 액세스 토큰이 null이거나 비어 있습니다.");
            return new ResponseEntity<>("액세스 토큰을 검색하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

      */
}