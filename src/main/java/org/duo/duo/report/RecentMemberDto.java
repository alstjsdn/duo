package org.duo.duo.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duo.duo.user.User;

@Getter
@AllArgsConstructor
public class RecentMemberDto {
    private String riotId;
    private String riotTag;
    private String name;

    public static RecentMemberDto from(User user) {
        return new RecentMemberDto(user.getRiotId(), user.getRiotTag(), user.getName());
    }
}