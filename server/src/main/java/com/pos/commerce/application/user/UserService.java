package com.pos.commerce.application.user;

import java.util.Optional;

import com.pos.commerce.application.user.command.DeleteUserCommand;
import com.pos.commerce.application.user.command.UpdateUserCommand;
import com.pos.commerce.application.user.command.LoginUserCommand;
import com.pos.commerce.application.user.command.RegisterUserCommand;
import com.pos.commerce.application.user.query.GetUserByIdQuery;
import com.pos.commerce.application.user.query.GetUserByUsernameQuery;
import com.pos.commerce.domain.user.User;

public interface UserService {

    /* @회원가입 */
    User createUser(RegisterUserCommand command);

    /* @사용자 조회 (고유 아이디 )*/
    Optional<User> getUserById(GetUserByIdQuery query);

    /* @사용자 조회 (유저 아이디)*/
    Optional<User> getUserByUsername(GetUserByUsernameQuery query);

    /* @사용자 수정 */
    User updateUser(UpdateUserCommand command);

    /* @사용자 삭제 */
    void deleteUser(DeleteUserCommand command);

    /* @로그인 */
    String authenticate(LoginUserCommand command);
}

