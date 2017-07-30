package com.decisionkitchen.decisionkitchen

/**
 * Created by kevin on 2017-07-29.
 */

typealias RestaurantID = String;
typealias GroupID = String;
typealias UserID = String;

data public class Category(val name: String? = null, val type: String? = null, val value: Any? = null)

data public class RestaurantOrder(val id: String? = null, val service: String? = null)

data public class Restaurant(val address: String? = null, val name: String? = null, val id: RestaurantID? = null, val order: RestaurantOrder? = null)

data public class GameMeta(val end: String? = null, val start: String? = null)

data public class GameResult(val restaurant_id: RestaurantID? = null)

data public class Game(
    val categories: ArrayList<Category>? = null,
    val meta: GameMeta? = null,
    val rating: HashMap<UserID, Integer>? = null,
    val responses: HashMap<UserID, ArrayList<Integer>>? = null,
    val result: GameResult? = null)

data public class Group(
    val password: String? = null,
    val members: ArrayList<UserID>? = null,
    val name: String? = null,
    val restaurants: HashMap<RestaurantID, Restaurant>? = null,
    val id: GroupID? = null,
    val games: ArrayList<Game>? = null
)