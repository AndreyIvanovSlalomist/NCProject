package ru.nc.musiclib.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nc.musiclib.model.Token;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto  {
    private String value;

    public static TokenDto from(Token token){
        return new TokenDto(token.getValue());
    }
}
