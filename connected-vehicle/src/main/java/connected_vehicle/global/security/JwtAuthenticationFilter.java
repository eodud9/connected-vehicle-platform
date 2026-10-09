package connected_vehicle.global.security;

import connected_vehicle.domain.user.User;
import connected_vehicle.domain.user.UserRepository;
import connected_vehicle.exception.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
                                    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if(authorization != null && authorization.startsWith("Bearer ")){

            String token = authorization.substring(7);

            if(jwtProvider.validateToken(token)){

                Long userId = jwtProvider.getUserId(token);
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }


        }

        filterChain.doFilter(request, response);
    }

}
