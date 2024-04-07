package com.imbidgod.jwt.service;

import com.imbidgod.db.entity.Member;
import com.imbidgod.db.service.IMemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Getter
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired(required=true)
	@Qualifier("memberService")
	private IMemberService service;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = getService().getEntityByAccount( username );
		if (member == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(member.getAccount(), member.getPassword(),
				new ArrayList<>());
	}

}