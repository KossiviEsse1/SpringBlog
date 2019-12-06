package org.koko.kokoprojectone;

import org.koko.kokoprojectone.models.MyUserDetails;
import org.koko.kokoprojectone.models.User;
import org.koko.kokoprojectone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        Optional<User> user = userDao.findByUsername(userName);

        user.orElseThrow(()-> new UsernameNotFoundException("Not Found: " + userName));

        return user.map(MyUserDetails::new).get();
    }
}
