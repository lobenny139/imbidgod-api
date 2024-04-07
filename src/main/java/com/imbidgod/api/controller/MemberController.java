package com.imbidgod.api.controller;

import com.imbidgod.db.entity.Member;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IMemberService;
import com.imbidgod.jwt.JwtTokenUtil;
import com.imbidgod.jwt.model.JwtRequest;
import com.imbidgod.jwt.model.JwtResponse;
import com.imbidgod.jwt.service.JwtUserDetailsService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@RestController
@Api(tags = "Member Table 操作", value = "Member Table 操作 API 說明")
public class MemberController {

    @Autowired(required=true)
    @Qualifier("memberService")
    private IMemberService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value="以id取出單一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(Member)")
    })
    @GetMapping("/member/{id}")
    public Member get(
            @ApiParam(required=true, value="請傳入物件(member)的id")
            @PathVariable String id,
            HttpServletResponse response) throws Exception {
        try{
            Member entity = getService().getEntityById( Long.parseLong( id ) );
            return entity;
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(DataException e) {
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="更新物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=404, message="未找到物件(Order)id")
    })
    @PutMapping("member/{id}")
    public void update(
            @ApiParam(required=true, value="請傳入物件(member)的id")
            @PathVariable String id,
            @ApiParam(required=true, value="請傳入物件(member)的 JSON 格式")
            @RequestBody(required=true) Member entity,
            HttpServletRequest request,
            HttpServletResponse response) {
        try{
            String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
            entity.setUpdateBy(currentUser);
            getService().updateEntity(Long.parseLong( id ), entity);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="新增單一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/member")
    public Member create(
            @ApiParam(required=true, value="請傳入物件(Member)的 JSON 格式")
            @RequestBody(required=true) Member entity,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
            entity.setCreateBy( currentUser );
            return getService().createEntity(entity);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e) {
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="驗證(Member Token)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/member/validate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        authenticate(jwtRequest.getAccount(), jwtRequest.getPassword());
        final UserDetails userDetails = getUserDetailsService().loadUserByUsername(jwtRequest.getAccount());
        final String token = getJwtTokenUtil().generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String account, String password) throws Exception {
        try {
            getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(account, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
