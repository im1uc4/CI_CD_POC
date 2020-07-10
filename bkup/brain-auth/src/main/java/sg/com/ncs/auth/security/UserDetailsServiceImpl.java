package sg.com.ncs.auth.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sg.com.ncs.auth.repository.UserRepository;
import sg.com.ncs.auth.user.maint.UserRole;

@Transactional
@Service // It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService {
	/*
	 * @Autowired private BCryptPasswordEncoder encoder;
	 */

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		List<sg.com.ncs.auth.user.maint.User> users = userRepository.findByUsername(userName);

		for (sg.com.ncs.auth.user.maint.User appUser : users) {
			if (appUser.getUsername().equals(userName)) {

				List<UserRole> userRoles = appUser.getUserRoles();
				List<String> authorityList = new ArrayList<>();

				authorityList.add("fullname:" + appUser.getFullname());
				authorityList.add("email:" + appUser.getEmail());
				authorityList.add("username:" + appUser.getUsername());

				userRoles.forEach(role -> {
					authorityList.add("ROLE_" + role.getRoles().getName());
				});

				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
						.commaSeparatedStringToAuthorityList(String.join(",", authorityList));

				return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
			}
		}
		return null;
	}

}