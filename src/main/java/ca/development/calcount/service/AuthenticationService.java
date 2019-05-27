package ca.development.calcount.service;

import ca.development.calcount.exception.AuthenticationException;
import ca.development.calcount.exception.NullObjectException;
import ca.development.calcount.model.*;
import ca.development.calcount.repository.UserRepository;
import ca.development.calcount.security.*;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class AuthenticationService {

	@Autowired
	private UserRepository repository;

	private static final HashMap<String, String> userBySession = new HashMap<>();
	private static final HashMap<String, String> sessionByUser = new HashMap<>();

	/**
	 * Constructor
	 */
	public AuthenticationService() {}

	/**
	 * gets an User by session
	 * @param sessionGuid
	 * @return User
	 * @throws NullObjectException when session is invalid or expired
	 */
	public User getUserBySession(String sessionGuid) throws NullObjectException {
		String name = userBySession.get(sessionGuid);
		if (name == null) {
			throw new NullObjectException("Session has expired or is invalid.");
		}
		User user = findUserByUsername(name);
		return user;
	}

	/**
	 * gets an User in the repository using the username
	 * @param username
	 * @return User
	 * @throws NullObjectException when user does not exist
	 */
	private User findUserByUsername(String username) throws NullObjectException  {
        User user = null;
        try {
            user = repository.getUser(username);
        } catch (NullObjectException e) {
            throw new NullObjectException("User does not exist");
        }
        return user;

    }
	/**
	 * Allows to login: validate username and password
	 * @param username
	 * @param password
	 * @return sessionGuid
	 * @throws AuthenticationException when password is invalid
	 * @throws NullObjectException when user does not exist
	 * @throws Exception when password checking fails
	 */
	public String login(String username, String password) throws Exception{
		User user = null;

		try {
            user = findUserByUsername(username);
            Password.check(password, user.getPassword());
		}
        catch(NullObjectException e){
            throw new NullObjectException(e.getMessage());
        }
		catch(AuthenticationException e){
			throw new AuthenticationException("Invalid login password!!!");
		}
		catch(Exception e){
			throw new Exception(e.getMessage());
		}

		if (sessionByUser.containsKey(user.getUsername())) {
		    // Invalidate old session
            logout(user.getUsername());
		}

        String sessionGuid = UUID.randomUUID().toString();
        userBySession.put(sessionGuid, user.getUsername());
        sessionByUser.put(user.getUsername(), sessionGuid);
        return sessionGuid;

    }
	/**
	 * Allows to logout given the username
	 * @param username
	 * @throws AuthenticationException when user is not logged in
	 */
	public void logout(String username) throws AuthenticationException {
		if(sessionByUser.get(username)==null){
			throw new AuthenticationException("This user is not logged in");
		}
		userBySession.remove(sessionByUser.remove(username));

	}
}