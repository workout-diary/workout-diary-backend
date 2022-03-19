package com.deukgeun.workout.user.service;

import com.deukgeun.workout.auth.dto.AccessToken;
import com.deukgeun.workout.user.domain.Provider;
import com.deukgeun.workout.user.domain.Role;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.dto.OAuth2User;
import com.deukgeun.workout.user.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdate(OAuth2User oAuth2User) {
        User user = userRepository.findByEmail(oAuth2User.getEmail()).orElse(null);
        if (user == null) {
            return userRepository.save(User.builder()
                    .email(oAuth2User.getEmail())
                    .providerId("kakao_" + oAuth2User.getId())
                    .provider(Provider.KAKAO)
                    .name(oAuth2User.getName())
                    .image(oAuth2User.getImage())
                    .role(Role.USER)
                    .build());
        }
        return user;
    }

    public OAuth2User getKakaoUser(AccessToken accessToken) throws ParseException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken.getAccessToken());
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

        return OAuth2User.builder()
                .email(email)
                .id(id)
                .image(image)
                .name(name)
                .build();

    }
}
