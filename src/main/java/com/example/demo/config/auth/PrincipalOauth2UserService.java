package com.example.demo.config.auth;

import ch.qos.logback.core.joran.action.NewRuleAction;
import com.example.demo.config.auth.provider.GoogleUserInfo;
import com.example.demo.config.auth.provider.NaverUserInfo;
import com.example.demo.config.auth.provider.OAuth2UserInfo;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    //후처리 함수 구글에서 받은 리퀘스트 데이터
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        String clientId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo auth2UserInfo = null;

        if(clientId.equals("google")) {
            System.out.println("구글 요청");
            auth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }else if(clientId.equals("naver")){
            System.out.println("네이버 요청");
            auth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }
        String provider = auth2UserInfo.getProvider();
        String providerId = auth2UserInfo.getProviderId();
        String username = provider+"_"+providerId;
        String email = auth2UserInfo.getEmail();
        String role = "ROLE_USER";
        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(null)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
