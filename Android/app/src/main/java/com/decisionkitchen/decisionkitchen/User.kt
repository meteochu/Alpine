package com.decisionkitchen.decisionkitchen

/**
 * Created by kevin on 2017-07-29.
 */
typealias UserID = String;
data public class User(
        val name: String? = null,
        val id: UserID? = null,
        val email: String? = null,
        val img: String? = null,
        val first_name: String? = null,
        val last_name: String? = null
);