package sg.com.ncs.brain.utils;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Object principal = null;
		try {
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {

			return Optional.of("System");
		}

		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return Optional.of(username);
	}

}