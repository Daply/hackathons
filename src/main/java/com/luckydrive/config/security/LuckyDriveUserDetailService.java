package com.luckydrive.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luckydrive.model.User;
import com.luckydrive.repository.UserRepository;

@Service("luckyDriveUserDetailService")
public class LuckyDriveUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		final User user = userRepository.findByEmail(login);
		if (user == null) {
			throw new UsernameNotFoundException("User with login=" + login + " does not exists");
		}
		return new LuckyDriveUserPrincipal(user);
	}

}
