package com.example.demosudoku.model.user;

/**
 * Represents a user/player in the Sudoku game.
 * Stores the player's nickname for personalization purposes.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class User {

    /**
     * The user's nickname.
     */
    private String nickname;

    /**
     * Constructs a new User with the specified nickname.
     *
     * @param nickname the nickname for this user
     */
    public User(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the user's nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the user's nickname.
     *
     * @param nickname the new nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}