package com.oarango.meli.challenge.user.infrastructure.repository;

import com.oarango.meli.challenge.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApi {
    private String nickname;

    public static User toDomain(UserApi userApi) {
        return User.builder()
                .nickname(userApi.getNickname())
                .build();
    }
}
