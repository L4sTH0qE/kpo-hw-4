package se.hse.kpo.homework.three.service


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import se.hse.kpo.homework.three.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Service
class UserService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails {
        if (email == null) {
            throw UsernameNotFoundException("There is no such user")
        }

        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("There is no such user: $email")
        val tmp: List<GrantedAuthority> = ArrayList()
        return User(user.nickname, user.password, tmp)
    }
}