package com.taira.cntl.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taira.cntl.entity.User;

@Component
public class CustomTokenEnhancer  implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		try {
			Gson gson = new GsonBuilder().create();
			User u = gson.fromJson(authentication.getPrincipal().toString(), new TypeToken<User>() {
			}.getType());
			final Map<String, Object> additionalInfo = new HashMap<>();
			additionalInfo.put("email", u.getEmail());
			additionalInfo.put("id", u.getId());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		} catch (Exception e) {
		}
		return accessToken;
	}

}
