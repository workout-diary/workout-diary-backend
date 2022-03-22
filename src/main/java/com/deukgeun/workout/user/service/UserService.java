package com.deukgeun.workout.user.service;

import com.deukgeun.workout.auth.dto.AccessTokenDto;
import com.deukgeun.workout.user.domain.Provider;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.domain.UserAuthority;
import com.deukgeun.workout.user.domain.UserProperty;
import com.deukgeun.workout.user.dto.UserDto;
import com.deukgeun.workout.user.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashSet;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User saveOrUpdate(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user == null) {
            User newUser = User.builder()
                    .email(userDto.getEmail())
                    .providerId("kakao_" + userDto.getId())
                    .provider(Provider.KAKAO)
                    .name(userDto.getName())
                    .image(userDto.getImage())
                    .enabled(true)
                    .build();
            newUser = userRepository.save(newUser);

            HashSet<UserAuthority> authorities = new HashSet<>();
            UserAuthority newRole = new UserAuthority(newUser.getId(), "USER");
            authorities.add(newRole);
            newUser.setAuthorities(authorities);
            newUser.setUserProperty(UserProperty.builder()
                    .id(newUser.getId())
                    .build());

            return userRepository.save(newUser);
        }
        user.setEmail(userDto.getEmail());
        user.setImage(userDto.getImage());
        user.setName(userDto.getName());
        return userRepository.save(user);
    }

    public UserDto getKakaoUser(AccessTokenDto accessTokenDto) throws ParseException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessTokenDto.getAccessToken());
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject kakaoAccount = (JSONObject) jsonObject.get("kakao_account");
        JSONObject profile = (JSONObject) kakaoAccount.get("profile");
        String name = (String) profile.get("nickname");
        String image = (String) profile.get("profile_image_url");
        String email = (String) kakaoAccount.get("email");
        Long id = (Long) jsonObject.get("id");

        return UserDto.builder()
                .email(email)
                .id(id)
                .image(image)
                .name(name)
                .build();

    }

}
