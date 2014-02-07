package io.macgyver.chat.hipchat;

public interface HipChatConstants
{
    public static final String JSON_FORMAT = "json";
    public static final String API_BASE = "https://api.hipchat.com/v1/";

    // API Operation URL components
    public static final String ROOMS_LIST = "rooms/list";
    public static final String ROOMS_LIST_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String ROOMS_CREATE = "rooms/create";
    public static final String ROOMS_CREATE_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String ROOMS_DELETE = "rooms/delete";
    public static final String ROOMS_DELETE_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String ROOMS_SHOW = "rooms/show";
    public static final String ROOMS_SHOW_QUERY_FORMAT = "?room_id=%s&format=%s&auth_token=%s";

    public static final String ROOMS_HISTORY = "rooms/history";
    public static final String ROOMS_HISTORY_QUERY_FORMAT = "?room_id=%s&date=%s&timezone=%s&format=%s&auth_token=%s";

    public static final String ROOMS_MESSAGE = "rooms/message";
    public static final String ROOMS_MESSAGE_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String USERS_CREATE = "users/create";
    public static final String USERS_CREATE_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String USERS_DELETE = "users/delete";
    public static final String USERS_DELETE_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String USERS_LIST = "users/list";
    public static final String USERS_LIST_QUERY_FORMAT = "?format=%s&auth_token=%s";

    public static final String USERS_SHOW = "users/show";
    public static final String USERS_SHOW_QUERY_FORMAT = "?user_id=%s&format=%s&auth_token=%s";

    public static final String USERS_UPDATE = "users/update";
    public static final String USERS_UPDATE_QUERY_FORMAT = "?format=%s&auth_token=%s";
}