package com.example.msa_db_sep_hongkd.common.member.controller;




import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import com.example.msa_db_sep_hongkd.common.member.domain.UserStatus;
import com.example.msa_db_sep_hongkd.common.member.dto.*;
import com.example.msa_db_sep_hongkd.common.member.service.JwtTokenProvider;
import com.example.msa_db_sep_hongkd.common.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("rtdb")
    private final RedisTemplate<String,Object> redisTemplate;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }
    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDTO memberSaveReqDTO){
        System.out.println("<<<<<<<<<MemberController : Create New User>>>>>>>>>>");
        Long memberId = memberService.save(memberSaveReqDTO);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDTO dto){
        //email.pw로 검증
        Member member =memberService.login(dto);
        String token = jwtTokenProvider.createToken(member.getId().toString(),member.getRole().toString(),member.getUserStatus().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(),member.getRole().toString());

        redisTemplate.opsForValue().set(member.getEmail(),refreshToken,200, TimeUnit.DAYS);

        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id",member.getId());
        loginInfo.put("token",token);
        loginInfo.put("refreshToken",refreshToken);
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<?> generateNewAt(@RequestBody MemberRefreshDTO dto){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(dto.getRefreshToken())
                .getBody();

        Object rt =redisTemplate.opsForValue().get(claims.getSubject());
        if(rt==null || rt.toString().equals(dto.getRefreshToken())){
            return new ResponseEntity<>((Object) null,HttpStatus.BAD_REQUEST);
        }
        String token = jwtTokenProvider.createToken(claims.getSubject(), claims.get("role").toString(),claims.get("status").toString());

        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("token",token);
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);

    }
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable Long memberId,@RequestHeader(value = "X-USER-STATUS",required=false)String status){
        MemberResponseDTO dto = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{memberId}/activate")
    public ResponseEntity<?> memberStatusActivate(@PathVariable Long memberId,
                                                @RequestHeader("X-USER-ROLE")String role){
        memberService.updateUserStatusActivate(memberId,role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/{memberId}/banned")
    public ResponseEntity<?> memberStatusBanned(@PathVariable Long memberId,
                                                  @RequestHeader("X-USER-ROLE")String role){
        memberService.updateUserStatusBanned(memberId,role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/seller/create")
    public ResponseEntity<?> createSeller(@RequestBody SellerCreateDTO dto,@RequestHeader("X-USER-ROLE")String role){
        Long sellerId = memberService.createSeller(dto,role);
        return new ResponseEntity<>(sellerId,HttpStatus.CREATED);
    }
    @DeleteMapping("/seller/{sellerId}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long sellerId,@RequestHeader("X-USER-ROLE")String role){
        memberService.deleteSeller(sellerId,role);
        return new ResponseEntity<>(sellerId,HttpStatus.OK);
    }
    @GetMapping("/memberList")
    public ResponseEntity<List<MemberListDTO>> getMemberList(@RequestHeader("X-USER-ROLE") String role){
        List<MemberListDTO> list = memberService.getMemberList(role);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/userList")
    public ResponseEntity<List<MemberListDTO>> getUserList(@RequestHeader("X-USER-ROLE") String role){
        List<MemberListDTO> list = memberService.getUserList(role);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/sellerList")
    public ResponseEntity<List<MemberListDTO>> getSellerList(@RequestHeader("X-USER-ROLE") String role){
        List<MemberListDTO> list = memberService.getSellerList(role);
        return ResponseEntity.ok(list);
    }



}
