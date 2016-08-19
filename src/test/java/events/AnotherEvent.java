package events;

/**
 * @author Colin Rosen
 */
public class AnotherEvent extends SomeEvent {
    private int nr;
    private boolean called;

    public AnotherEvent(String message, int nr) {
        super(message);

        this.nr = nr;
    }

    public boolean wasCalled() {
        return called;
    }

    public void setCalled() {
        called = true;
    }

    public int getNr() {
        return nr;
    }
}
