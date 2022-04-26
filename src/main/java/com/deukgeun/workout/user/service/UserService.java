package com.deukgeun.workout.user.service;

import com.deukgeun.workout.auth.dto.AccessTokenDto;
import com.deukgeun.workout.user.domain.Provider;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.domain.UserAuthority;
import com.deukgeun.workout.user.domain.UserProperty;
import com.deukgeun.workout.user.dto.UserDto;
import com.deukgeun.workout.user.repository.UserAuthorityRepository;
import com.deukgeun.workout.user.repository.UserPropertyRepository;
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
import java.util.ArrayList;
import java.util.HashSet;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    public UserService(UserRepository userRepository,
                       UserAuthorityRepository userAuthorityRepository,
                       UserPropertyRepository userPropertyRepository) {
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.userPropertyRepository = userPropertyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User saveOrUpdate(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user == null) {
            UserAuthority userAuthority = UserAuthority.builder()
                    .authority("USER")
                    .build();
            userAuthorityRepository.save(userAuthority);

            UserProperty userProperty = new UserProperty();
            userPropertyRepository.save(userProperty);

            User newUser = User.builder()
                    .email(userDto.getEmail())
                    .image(userDto.getImage())
                    .name(userDto.getName())
                    .enabled(true)
                    .providerId("kakao_" + userDto.getId())
                    .provider(Provider.KAKAO)
                    .authorities(new HashSet<>())
                    .workoutSets(new HashSet<>())
                    .build();
            userRepository.save(newUser);

            userAuthority.setUser(newUser);
            userAuthorityRepository.save(userAuthority);
            userProperty.setUser(newUser);
            userPropertyRepository.save(userProperty);

            newUser.addAuthority(userAuthority);
            newUser.setUserProperty(userProperty);
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
