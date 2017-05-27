package project.services;

public class UserToken {

    public UserToken(Object token){
        this.token = token;
    }

    private Object token;

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserToken userToken = (UserToken) o;

        if (token != null ? !token.equals(userToken.token) : userToken.token != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
