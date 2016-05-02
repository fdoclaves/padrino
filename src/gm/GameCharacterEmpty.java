package gm;


public class GameCharacterEmpty extends GameCharacter{

    @Override
    public void dead() {

    }

    @Override
    public boolean isTeam(String team) {
        return false;
    }

    @Override
    public boolean isValidSeat() {
        return true;
    }

    @Override
    public boolean hasGun() {
        return false;
    }

    @Override
    public boolean hasKnife() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void sleep() {
        
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public void wakeUp() {

    }

    @Override
    public GameCharacter cloneCharacters() {
        return this;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public String getTeam() {
        return null;
    }

}
