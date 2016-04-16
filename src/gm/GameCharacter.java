package gm;

public interface GameCharacter {

	void dead();

	boolean isTeam(String team);

	boolean isValidSeat();

	boolean hasGun();

	boolean hasKnife();

	boolean isEmpty();

	void sleep();

	boolean isSleeping();

	void wakeUp();

	GameCharacter cloneCharacters();

	boolean isKing();

	String getTeam();

}
