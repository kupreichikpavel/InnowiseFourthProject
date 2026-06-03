package com.example.innowisefourthproject.router;

public class Router {
    private String page = "index.jsp";
    private Type type = Type.REDIRECT;

    public Router(String page) {
        this.page = page;
    }
    public Router(String page, Type type) {
        this.page = page;
        this.type = type;
    }

    enum Type {
        FORWARD, REDIRECT
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setRedirect(Type type) {
        this.type = Type.REDIRECT;
    }
}
