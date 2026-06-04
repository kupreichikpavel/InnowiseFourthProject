package com.example.innowisefourthproject.command;

import com.example.innowisefourthproject.command.impl.*;

public enum CommandType {
    ADD_USER(new AddUserCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),

    DEFAULT(new DefaultCommand()),

    SHOW_ITEMS(new ShowItemsCommand()),
    OPEN_ADD_ITEM_PAGE(new OpenAddItemPageCommand()),
    ADD_ITEM(new AddItemCommand()),
    DELETE_ITEM(new DeleteItemCommand()),
    OPEN_EDIT_ITEM_PAGE(new OpenEditItemPageCommand()),
    UPDATE_ITEM(new UpdateItemCommand()),

    CREATE_ORDER(new CreateOrderCommand()),
    SHOW_ORDERS(new ShowOrdersCommand()),
    CANCEL_ORDER(new CancelOrderCommand()),
    COMPLETE_ORDER(new CompleteOrderCommand());


    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }


    public static Command define(String commandStr) {
        if (commandStr == null || commandStr.isBlank()) {
            return DEFAULT.command;
        }

        try {
            return CommandType.valueOf(commandStr.toUpperCase()).command;
        } catch (IllegalArgumentException e) {
            return DEFAULT.command;
        }
    }
}
