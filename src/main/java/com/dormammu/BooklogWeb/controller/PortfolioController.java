package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostPortfolioReq;
import com.dormammu.BooklogWeb.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    // 내 포트폴리오 조회 페이지
    @GetMapping("/api/user/{id}/porfols")
    public List<Portfolio> myPortfolioList(@PathVariable int id, Authentication authentication) {
        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()) {
            System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername() + "&&&&&" + user.getUsername());
            return portfolioService.myPortfolioList(user);
        }
        return null;
    }

    // 포트폴리오 생성
    @PostMapping("/auth/portfolio")
    public String createPortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (postPortfolioReq.getUserId() == principalDetails.getUser().getId()) {
            portfolioService.createPortfolio(principalDetails.getUser(), postPortfolioReq);

            return "포트폴리오 생성 완료";
        }
        return null;
    }

    // 포트폴리오 수정
    @PatchMapping("/auth/portfolio/{p_id}")
    public String updatePortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication, @PathVariable int p_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        portfolioService.updatePortfolio(principalDetails.getUser(), postPortfolioReq, p_id);

        return "포트폴리오 수정 완료";

    }

    // 포트폴리오 삭제
    @DeleteMapping("/auth/portfolio/{p_id}")
    public String deletePortfolio(Authentication authentication, @PathVariable int p_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        portfolioService.deletePortfolio(principalDetails.getUser(), p_id);

        return "포트폴리오 삭제 완료";

    }

}
