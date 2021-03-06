package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.QnA.AdminQnARepository;
import com.dormammu.BooklogWeb.domain.QnA.UserQnA;
import com.dormammu.BooklogWeb.domain.QnA.UserQnARepository;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.dormammu.BooklogWeb.domain.hastag.HashTagRepository;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUserRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.MeetingRes;
import com.dormammu.BooklogWeb.dto.PatchMeetingReq;
import com.dormammu.BooklogWeb.dto.PostAnswerReq;
import com.dormammu.BooklogWeb.dto.PostMeetingReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final AdminQnARepository adminQnARepository;
    private final HashTagRepository hashTagRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final UserQnARepository userQnARepository;

    @Transactional
    public String createMeeting(User user, PostMeetingReq postMeetingReq){
        Meeting meeting = new Meeting();
        meeting.setName(postMeetingReq.getName());
        meeting.setInfo(postMeetingReq.getInfo());
        meeting.setMent(postMeetingReq.getMent());
        meeting.setImage(postMeetingReq.getImage());
        meeting.setUserId(user.getId());
        meeting.setCur_num(1);
        meeting.setMax_num(postMeetingReq.getMax_num());

        AdminQnA adminQnA = new AdminQnA();
        adminQnA.setMeeting(meeting);
        adminQnA.setQ1(postMeetingReq.getQ1());
        adminQnA.setQ2(postMeetingReq.getQ2());
        adminQnA.setQ3(postMeetingReq.getQ3());
        adminQnA.setQ4(postMeetingReq.getQ4());
        adminQnA.setQ5(postMeetingReq.getQ5());


        HashTag hashTag = new HashTag();
        hashTag.setMeeting(meeting);
        hashTag.setTag1(postMeetingReq.getH1());
        hashTag.setTag2(postMeetingReq.getH2());
        hashTag.setTag3(postMeetingReq.getH3());
        hashTag.setTag4(postMeetingReq.getH4());
        hashTag.setTag5(postMeetingReq.getH5());

        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);
        adminQnARepository.save(adminQnA);
        hashTagRepository.save(hashTag);
        meetingRepository.save(meeting);

        return "?????? ?????? ??????";
    }

    @Transactional
    public List<Meeting> meetingList(){
        System.out.println("meetingList ?????????");
        List<Meeting> meetingList = meetingRepository.findAll();
        System.out.println("?????? ????????? ?????? : " + meetingList);
        return meetingList;
    }

    @Transactional(readOnly = true)
    public List<Meeting> myMeetingList(User user){
        System.out.println("myMeetingList ?????????");
        List<Meeting> myMeetingList = meetingRepository.findByUserId(user.getId());
        System.out.println("??? ?????? ????????? ??????: " + myMeetingList);
        return myMeetingList;
    }

    @Transactional
    public String addMeeting(User user, Meeting meeting){
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setUser(user);
        meetingUser.setMeeting(meeting);
        meeting.setCur_num(meeting.getCur_num()+1);  // ?????? ??????

        meetingUserRepository.save(meetingUser);
        return "?????? ?????? ??????";
    }

    @Transactional
    public String outMeeting(User user, Meeting meeting){

        MeetingUser meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());
        System.out.println(user.getId() +" AND " + meeting.getId());
        meetingUserRepository.delete(meetingUser);

        return "?????? ?????? ??????";
    }

    @Transactional
    public String update(int id, PatchMeetingReq patchMeetingReq){
        Meeting meeting = meetingRepository.findById(id);
//        System.out.println(meeting.getName() + "??? ?????? ????????? ???????????????");
        meeting.setName(patchMeetingReq.getName());
        meeting.setInfo(patchMeetingReq.getInfo());
        meeting.setMent(patchMeetingReq.getMent());
        meeting.setImage(patchMeetingReq.getImage());
        meeting.setOnoff(patchMeetingReq.isOnoff());
        meeting.setMax_num(patchMeetingReq.getMax_num());

        return "?????? ?????? ?????? ??????";
    }

    //    @Transactional
//    public String deleteMeeting(User user, Meeting meeting){
//        System.out.println("User :" + user.getUsername() + ", " + "Meeting : " + meeting.getName());
//
//        meetingRepository.delete(meeting);
//        return "?????? ?????? ??????";
//    }

    @Transactional(readOnly = true)
    public MeetingRes questionList(Meeting meeting){
        // ????????? ????????????
        AdminQnA question = adminQnARepository.findByMeetingId(meeting.getId());
        List<String> questions = new ArrayList<>();

        questions.add(question.getQ1());
        questions.add(question.getQ2());
        questions.add(question.getQ3());
//        System.out.println("?????? ????????? ?????? : " + questions);

        MeetingRes meetingRes = MeetingRes.builder()
                .Q1(question.getQ1())
                .Q2(question.getQ2())
                .Q3(question.getQ3())
                .Q4(question.getQ4())
                .Q5(question.getQ5()).build();
        return meetingRes;
    }

    @Transactional
    public String createAnswer(int id, PostAnswerReq postAnswerReq){
        UserQnA userQnA = new UserQnA();

        userQnA.setA1(postAnswerReq.getA1());
        userQnA.setA2(postAnswerReq.getA2());
        userQnA.setA3(postAnswerReq.getA3());
        userQnA.setUserId(postAnswerReq.getUserId());

        AdminQnA adminQnA = adminQnARepository.findByMeetingId(id);
        userQnA.setAdminQnA(adminQnA);
        userQnARepository.save(userQnA);
        return "?????? ?????? ??????";
    }
//
//    @Transactional(readOnly = true)
////    public List<UserQnA> answerList(int id, int answers_id){
////        List<UserQnA> userqnaList = userQnARepository.findAll();
////        System.out.println(userqnaList);
////        return userqnaList;
//    public String answerList(int id, int answers_id){
//        System.out.println("answerList ?????????");
//        return "?????????";
//    }
}