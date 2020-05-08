package ru.nc.musiclib.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nc.musiclib.model.User;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
private Integer id;
    private String userName;
    private String password;
    private RoleDto roleDto;


    public static UserDto fromUser(User user){
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .roleDto(RoleDto.fromRole(user.getRole()))
                .password("")
                .build();
    }
    public static User fromUserDto(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .userName(userDto.getUserName())
                .role(RoleDto.fromRoleDto(userDto.getRoleDto()))
                .password("")
                .build();
    }
}
