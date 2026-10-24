package com.c2w.model.user;

import java.util.ArrayList;
import java.util.List;

public class SessionModel {
    private static SessionModel instance;
    private User currentUser;
    private final List<Runnable> authListeners = new ArrayList<>();

    private SessionModel() {
    }

    public static synchronized SessionModel getInstance() {
        if (instance == null) {
            instance = new SessionModel();
        }
        return instance;
    }

    public void addListener(Runnable listener) {
        if (listener != null && !authListeners.contains(listener)) {
            authListeners.add(listener);
        }
    }

    public void removeListener(Runnable listener) {
        authListeners.remove(listener);
    }

    public void notifyListeners() {
        for (Runnable r : authListeners) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        notifyListeners();
    }

    public void clear() {
        this.currentUser = null;
        notifyListeners();
    }
}
