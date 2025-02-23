package org.medilabo.mspatient.configuration;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(Patient userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//    /**
//     * Loads user-specific data.
//     *
//     * @param email the email of the user
//     *
//     * @return a UserDetails object that contains the user's data
//     * @throws UsernameNotFoundException if the user is not found
//     */
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserEntity user = userRepository.findByEmail(email).orElseThrow();
//
//        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
//    }
//
//
//    /**
//     * Retrieves the authorities granted to the user based on their role.
//     *
//     * @param role the role of the user
//     *
//     * @return a list of GrantedAuthority objects representing the user's authorities
//     */
//    private List<GrantedAuthority> getGrantedAuthorities(String role) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//        return authorities;
//    }
}
