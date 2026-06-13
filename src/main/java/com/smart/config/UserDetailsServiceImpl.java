package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
private UserRepository userRepository;
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		//fetching user from database
//	User user=	userRepository.getUserByUserName(username);
//		if(user==null) {
//			throw new UsernameNotFoundException("could not found user");
//		}
//	CustomUserDetails customUserDetails=new CustomUserDetails(user);
//	return customUserDetails;
//	}
	@Override
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {

	    System.out.println("Trying login with: " + username);

	    // ✅ CHANGE HERE
	    User user = userRepository.getUserByUserEmail(username);

	    System.out.println("User found: " + user);

	    if (user == null) {
	        throw new UsernameNotFoundException("User not found");
	    }

	    return new CustomUserDetails(user);
	}
	

}
