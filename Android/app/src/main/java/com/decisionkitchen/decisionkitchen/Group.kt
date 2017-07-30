package com.decisionkitchen.decisionkitchen

/**
 * Created by kevin on 2017-07-29.
 */

typealias RestaurantID = String;
typealias GroupID = String;

data public class RestaurantOrder(val id: String? = null, val service: String? = null)

data public class Restaurant(val address: String? = null,
                             val name: String? = null,
                             val id: RestaurantID? = null,
                             val order: RestaurantOrder? = null,
                             val state: String? = null,
                             val zip: String? = null,
                             val city: String? = null,
                             val image: String? = null)

data public class GameMeta(val end: String? = null, val start: String? = null)

data public class GameResult(val restaurant_id: RestaurantID? = null)

data public class Game(
    val meta: GameMeta? = null,
    val rating: HashMap<UserID, Int>? = null,
    val responses: HashMap<UserID, ArrayList<ArrayList<Int>>>? = null,
    val result: GameResult? = null)

data public class Group(
    val password: String? = null,
    val members: ArrayList<UserID>? = null,
    val name: String? = null,
    val restaurants: HashMap<RestaurantID, Restaurant>? = null,
    val id: GroupID? = null,
    val games: ArrayList<Game>? = null
)